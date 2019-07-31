package me.maiz.app.little.config;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PackageScan {

    /**
     * Scans all classes accessible from the context class loader which belong
     * to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public Iterable<Class> getClassesAnnotatedWith(Class<? extends Annotation> annoClass, String packageName) {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = null;
        List<Class> classes = new ArrayList<>();

        try {
            resources = classLoader.getResources(path);

            List<File> dirs = new ArrayList<File>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                URI uri = new URI(resource.toString());
                dirs.add(new File(uri.getPath()));
            }
            for (File directory : dirs) {
                classes.addAll(findClasses(directory, packageName, annoClass));
            }
        } catch (IOException | ClassNotFoundException | URISyntaxException e) {
            throw new RuntimeException("扫描包路径["+packageName+"]失败",e);
        }
        return classes;
    }

    /**
     * Recursive method used to find all classes in a given directory and
     * subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @param annoClass
     * @return The classes
     * @throws ClassNotFoundException
     */
    private List<Class> findClasses(File directory, String packageName, Class<? extends Annotation> annoClass) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName(), annoClass));
            } else if (file.getName().endsWith(".class")) {


                final Class<?> aClass = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
                if (aClass.isAnnotationPresent(annoClass)) {
                    classes.add(aClass);
                }
            }
        }
        return classes;
    }
}
