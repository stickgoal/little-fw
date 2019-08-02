package me.maiz.app.little.orm.meta.model;

import org.apache.commons.lang3.StringUtils;

public class SqlGenerator {

    public static String byId(Mapping table){
        return "select * from "+table.getTableName()+" where "+table.getPkName()+"=?";
    }

    public static String insert(Mapping table){
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("insert into ")
                .append(table.getTableName()).append("(");
        for (ColumnMapping c : table.getColumnMappings()) {
            sqlBuilder.append(c.getColumn())
                    .append(",");
        }
        sqlBuilder.deleteCharAt(sqlBuilder.length()-1);
        sqlBuilder.append(") values(");
        sqlBuilder.append(StringUtils.repeat("?,",table.getColumnMappings().size()));
        sqlBuilder.deleteCharAt(sqlBuilder.length()-1);
        sqlBuilder.append(")");
        return sqlBuilder.toString();
    }


}
