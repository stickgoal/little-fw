package me.maiz.app.little.orm.util;

import me.maiz.app.little.orm.exceptions.PersistException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PackageScan {

    public List<Class> getClassesIn(String packageName) {
        //报名转换为路径
        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        List<Class> classes = new ArrayList<>();
        List<File> dirs = new ArrayList<File>();

        try {
            final Enumeration<URL> resources = classLoader.getResources(path);

            //遍历路径下的资源，如果有多个则加入
            while (resources.hasMoreElements()) {
                final URL url = resources.nextElement();
                dirs.add(new File(new URI(url.toString())));

            }
        } catch (IOException | URISyntaxException e) {
            throw new PersistException("遍历获取实体类出错",e);
        }

        for (File dir : dirs) {
            //为递归，抽出方法findClass
            classes.addAll(findClass(dir, packageName));
        }

        return classes;
    }

    /**
     * 找到目录下的所有class
     * @param dir
     * @param packageName
     * @return
     */
    private List<Class> findClass(File dir, String packageName) {
        List<Class> classes = new ArrayList<>();
        //判断路径是否存在
        if (!dir.exists()) {
            return classes;
        }
        //遍历子文件
        final File[] files = dir.listFiles();
        for (File file : files) {
            final String fileName = file.getName();
            //如果是文件夹则递归遍历，直到非文件为止
            if (file.isDirectory()) {
                classes.addAll(findClass(file, packageName + "." + fileName));
            } else {
                //只处理class文件
                if (fileName.endsWith(".class")) {
                    final String className = packageName + "." + fileName.substring(0, fileName.length() - 6);
                    try {
                        //加载class并放入list中
                        classes.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        throw new IllegalStateException("无法获得class[" + className + "]", e);
                    }
                }
            }
        }

        return classes;
    }


}
