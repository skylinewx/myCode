package com.example.spring.components;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * 一个简单的class查找工具，但是因为扫描不到jar包中的内容，所以很鸡肋
 */
public class SimpleClassScan {

    public Set<Class<?>> scan(String... basePackages) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        Set<Class<?>> classSet = new HashSet<>();
        for (String basePackage : basePackages) {
            //将com.aa.bb 替换成 com/aa/bb 需要注意的是/或者\是要根据File.separator来动态获取的
            String resourceName = basePackage.replaceAll("\\.", Matcher.quoteReplacement(File.separator));
            Enumeration<URL> resources = null;
            try {
                //通过classLoader获取所有的resources
                resources = classLoader.getResources(resourceName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (resources == null) {
                continue;
            }
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                File rootFile = null;
                try {
                    rootFile = new File(url.toURI());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                findClass(rootFile, classSet, basePackage, resourceName);
            }
        }
        return classSet;
    }

    private void findClass(File rootFile, Set<Class<?>> classSet, String basePackage, String resourceName) {
        if (rootFile == null) {
            return;
        }
        //如果是文件夹
        if (rootFile.isDirectory()) {
            File[] files = rootFile.listFiles();
            if (files == null) {
                return;
            }
            for (File file : files) {
                findClass(file, classSet, basePackage, resourceName);
            }
        }
        String fileName = rootFile.getName();
        //只要class类型的文件
        boolean endsWith = fileName.endsWith(".class");
        if (!endsWith) {
            return;
        }
        //排除内部类
        if (fileName.indexOf('$') != -1) {
            return;
        }
        String path = rootFile.getPath();
        int i = path.indexOf(resourceName);
        String subPath = path.substring(i);
        String fullClassPath = subPath.replaceAll(Matcher.quoteReplacement(File.separator), ".");
        Class<?> aClass = null;
        try {
            aClass = Class.forName(fullClassPath.substring(0, fullClassPath.length() - 6));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (aClass != null) {
            classSet.add(aClass);
        }
    }
}
