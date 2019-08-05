package me.maiz.app.little.orm.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBHelper {

	private static  String URL = null;
	private static  String DB_USERNAME = null;
	private static  String DB_PASSWORD = null;


	public static void init(String url,String username,String password,String driverClassName){
		URL=url;
		DB_USERNAME=username;
		DB_PASSWORD=password;
		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("加载数据库驱动失败");
		}
	}
	
	//持有connection，避免每次创建connection的性能消耗
	private Connection conn = null;
	
	/**
	 * 新增操作
	 * @param sql
	 * @param params
	 */
	public int add(String sql, Object... params) {
		return modify(sql, params);
	}

	/**
	 * 删除操作
	 * @param sql
	 * @param params
	 */
	public void remove(String sql, Object... params) {
		modify(sql, params);
	}

	/**
	 * 修改操作
	 * @param sql
	 * @param params
	 */
	public int modify(String sql, Object... params) {
		PreparedStatement pstmt = null;
		try {

			pstmt = prepareAndSetSql(sql, params);

			return pstmt.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException("SQL异常", e);
		} finally {
			closePreparedStatement(pstmt);
		}
	}
	
	public <T> T queryOne(String sql, Transformer<T> extractor, Object... params) {
		 List<T> t = query(sql,extractor,params);
		 if( t.size()!=0 && t.size()>1) {
			 throw new RuntimeException("查询结果数据量不符合预期");
		 }
		return t.size()==0?null:t.get(0);
	}

	
	/**
	 * 查询操作
	 * @param sql
	 * @param extractor
	 * @param params
	 * @return
	 */
	public <T> List<T> query(String sql, Transformer<T> extractor, Object... params) {
		List<T> resultList = new ArrayList<T>();
		PreparedStatement pstmt = null;
		try {

			pstmt = prepareAndSetSql(sql, params);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				T t = extractor.extract(rs);
				resultList.add(t);
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("SQL异常", e);

		} finally {

			closePreparedStatement(pstmt);
		}

		return resultList;
	}
	
	private PreparedStatement prepareAndSetSql(String sql, Object... params) throws SQLException {
		PreparedStatement pstmt;
		pstmt = getConnection().prepareStatement(sql);
		// 循环设置参数
		for (int i = 0; i < params.length; i++) {
			pstmt.setObject(i + 1, params[i]);
		}
		System.out.println("SQL： "+pstmt);
		return pstmt;
	}
	
	private void closePreparedStatement(PreparedStatement pstmt) {
		try {
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException("关闭PreparedStatement时异常", e);
		}
	}
	
	/*
	 * 获取并且持有链接，减少因创建链接造成的性能消耗；如需关闭或者重新获取链接，可以调用cleanup方法
	 */
	private Connection getConnection() {
		if (conn == null) {
			try {
				conn = DriverManager.getConnection(URL, DB_USERNAME, DB_PASSWORD);
			} catch (SQLException e) {
				throw new RuntimeException("获得Connection时异常", e);
			}
		}
		return conn;
	}

	/** 
	 * 清理操作，关闭链接
	 */
	public void cleanup() {
		System.out.println("关闭链接，清理资源");
		try {
			if(conn!=null) {
				conn.close();
			}
		}catch (SQLException e) {
			throw new RuntimeException("关闭Connection异常", e);
		}
		conn=null;
	}
	
	/**
	 * 数据解压器
	 * 
	 * @author Administrator
	 *
	 * @param <T>
	 */
	public static interface Transformer<T> {
		/**
		 * 将ResultSet中的数据转换成List
		 * 
		 * @param rs
		 * @return
		 * @throws SQLException
		 */
		T extract(ResultSet rs) throws SQLException;
	}

}
