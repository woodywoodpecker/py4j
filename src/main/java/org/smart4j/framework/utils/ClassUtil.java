package org.smart4j.framework.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Author Warren
 * @CreateTime 01.May.2018
 * @Version V1.0
 */
public final class ClassUtil {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(ClassUtil.class);

    public static ClassLoader getClassLoader () {
        return Thread.currentThread().getContextClassLoader();
    }

    public static Class<?> loadClass(String className) {
        return loadClass(className,false);
    }

    public static Class<?> loadClass(String className,boolean isInitialized) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className,isInitialized,getClassLoader());
        } catch (ClassNotFoundException pE) {
            LOGGER.error("load class failure",pE);
            throw new RuntimeException(pE);
        }
        return clazz;
    }

    public static Set<Class<?>> getClassSet (String packageName) {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".","/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url == null) continue;
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String packagePath = url.getPath().replaceAll("%20"," ");
                    addClass(classSet,packagePath,packageName);
                }
                else if ("jar".equals(protocol)) {
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    if (jarURLConnection != null) {
                        JarFile jarFile = jarURLConnection.getJarFile();
                        if (jarFile != null) {
                            Enumeration<JarEntry> jarEntries = jarFile.entries();
                            while (jarEntries.hasMoreElements()) {
                                JarEntry jarEntry = jarEntries.nextElement();
                                String jarEntryName = jarEntry.getName();
                                if (jarEntryName.endsWith(".class")) {
                                    String className =
                                            jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                                    doAddClass(classSet,className);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException pE) {
            LOGGER.error("get class set failure",pE);
            throw new RuntimeException(pE);
        }
        return classSet;
    }

    private static void doAddClass(Set<Class<?>> pClassSet, String pClassName) {
        Class<?> cls = loadClass(pClassName, false);
        pClassSet.add(cls);
    }

    private static void addClass(Set<Class<?>> pClassSet, String pPackagePath, String pPackageName) {
        File[] files = new File(pPackagePath).listFiles(new FileFilter() {
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
            }
        });
        for (File file: files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0,fileName.lastIndexOf("."));
                if (StringUtils.isNotEmpty(pPackageName)) {
                    className = pPackageName + "." + className;
                }
                doAddClass(pClassSet,className);
            }
            else {
                String subPackagePath = fileName;
                if (StringUtils.isNotEmpty(pPackagePath)) {
                    subPackagePath = pPackagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if (StringUtils.isNotEmpty(pPackageName)) {
                    subPackageName = pPackageName + "." + subPackageName;
                }
                addClass(pClassSet,subPackagePath,subPackageName);
            }
        }
    }

}
