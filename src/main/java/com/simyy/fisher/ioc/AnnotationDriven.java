package com.simyy.fisher.ioc;

import com.simyy.fisher.Fisher;
import com.simyy.fisher.annotion.Route;
import com.simyy.fisher.annotion.View;
import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AnnotationDriven {

    public static void init(Fisher fisher, String packName) {
        // inject View
        List<Class<?>> viewPaths = getClassListByAnnotation(packName, View.class);
        if (CollectionUtils.isNotEmpty(viewPaths)) {
            Map<String, Object> viewClass = parseClassPath(viewPaths);
            fisher.addBeans(viewClass);
            viewClass.forEach((key, value) -> {
                Method[] methods = value.getClass().getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(Route.class)) {
                        Route route = method.getAnnotation(Route.class);
                        fisher.addRoute(route.value(), method.getName(), value);
                    }
                }
            });
        }
    }

    private static Map<String, Object> parseClassPath(List<Class<?>> classSavePaths) {
        if (CollectionUtils.isEmpty(classSavePaths)) {
            return Collections.EMPTY_MAP;
        }

        Map<String, Object> result = new HashMap<>();
        for (Class<?> classPath : classSavePaths) {
            try {
                Class c = Class.forName(classPath.getName());
                Object o = c.newInstance();
                result.put(classPath.getName(), o);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private static List<Class<?>> getClassListByAnnotation(String packageName, Class<? extends Annotation> annotationClass) {
        List<Class<?>> classList = new ArrayList<Class<?>>();
        try {
            Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packageName.replaceAll("\\.", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url == null) {
                    continue;
                }

                String protocol = url.getProtocol();
                if (protocol.equals("file")) {
                    String packagePath = url.getPath();
                    addClassByAnnotation(classList, packagePath, packageName, annotationClass);
                } else if (protocol.equals("jar")) {
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    JarFile jarFile = jarURLConnection.getJarFile();
                    Enumeration<JarEntry> jarEntries = jarFile.entries();
                    while (jarEntries.hasMoreElements()) {
                        JarEntry jarEntry = jarEntries.nextElement();
                        String jarEntryName = jarEntry.getName();
                        if (jarEntryName.endsWith(".class")) {
                            String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                            Class<?> cls = Class.forName(className);
                            if (cls.isAnnotationPresent(annotationClass)) {
                                classList.add(cls);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classList;
    }

    private static void addClassByAnnotation(List<Class<?>> classList, String packagePath, String packageName, Class<? extends Annotation> annotationClass) {
        try {
            File[] files = getClassFiles(packagePath);
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    if (file.isFile()) {
                        String className = getClassName(packageName, fileName);
                        Class<?> cls = Class.forName(className);
                        if (cls.isAnnotationPresent(annotationClass)) {
                            classList.add(cls);
                        }
                        Field[] fields=cls.getFields();
                        for (Field field : fields) {
                            if(field.isAnnotationPresent(annotationClass)){
                                classList.add(cls);
                            }
                        }
                    } else {
                        String subPackagePath = getSubPackagePath(packagePath, fileName);
                        String subPackageName = getSubPackageName(packageName, fileName);
                        addClassByAnnotation(classList, subPackagePath, subPackageName, annotationClass);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static File[] getClassFiles(String packagePath) {
        return new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
            }
        });
    }

    private static String getClassName(String packageName, String fileName) {
        String className = fileName.substring(0, fileName.lastIndexOf("."));
        if (!packageName.equals("")) {
            className = packageName + "." + className;
        }
        return className;
    }

    private static String getSubPackagePath(String packagePath, String filePath) {
        String subPackagePath = filePath;
        if (!packagePath.equals("")) {
            subPackagePath = packagePath + "/" + subPackagePath;
        }
        return subPackagePath;
    }

    private static String getSubPackageName(String packageName, String filePath) {
        String subPackageName = filePath;
        if (!packageName.equals("")) {
            subPackageName = packageName + "." + subPackageName;
        }
        return subPackageName;
    }
}
