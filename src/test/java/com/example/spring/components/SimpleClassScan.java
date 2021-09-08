package com.example.spring.components;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 一个简单的class查找工具，目前仅支持jar包查找和本地查找
 */
public class SimpleClassScan {

    private final Set<Class<?>> classSet;
    private final Map<String, ProtocolHandler> handlerMap;

    public SimpleClassScan() {
        classSet = new HashSet<>();
        handlerMap = new HashMap<>();
        FileProtocolHandler fileProtocolHandler = new FileProtocolHandler();
        JarProtocolHandler jarProtocolHandler = new JarProtocolHandler();
        handlerMap.put(fileProtocolHandler.handleProtocol(), fileProtocolHandler);
        handlerMap.put(jarProtocolHandler.handleProtocol(), jarProtocolHandler);
    }

    public Set<Class<?>> scan(String... basePackages) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        for (String basePackage : basePackages) {
            //将com.aa.bb 替换成 com/aa/bb
            String resourceName = basePackage.replace('.', '/') + "/";
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
                String protocol = url.getProtocol();
                ProtocolHandler protocolHandler = handlerMap.get(protocol);
                if (protocolHandler == null) {
                    throw new RuntimeException("need support protocol [" + protocol + "]");
                }
                protocolHandler.handle(basePackage, url);
            }
        }
        return classSet;
    }

    private void addResult(String classFullName) {
        Class<?> aClass = null;
        try {
            aClass = Class.forName(classFullName.substring(0, classFullName.length() - 6));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (aClass != null) {
            classSet.add(aClass);
        }
    }

    private boolean checkIsNotClass(String fileName) {
        //只要class类型的文件
        boolean isClass = fileName.endsWith(".class");
        if (!isClass) {
            return true;
        }
        //排除内部类
        return fileName.indexOf('$') != -1;
    }

    public Set<Class<?>> getClassSet() {
        return classSet;
    }

    /**
     * 协议处理器
     */
    private interface ProtocolHandler {
        /**
         * 适配的协议
         *
         * @return
         */
        String handleProtocol();

        /**
         * 处理url，最后需要调用{@link #addResult(String)}将结果存储到result中
         *
         * @param url
         */
        void handle(String basePackage, URL url);
    }

    private class JarProtocolHandler implements ProtocolHandler {

        @Override
        public String handleProtocol() {
            return "jar";
        }

        @Override
        public void handle(String basePackage, URL url) {
            try {
                String resourceName = basePackage.replace('.', '/') + "/";
                JarURLConnection conn = (JarURLConnection) url.openConnection();
                JarFile jarFile = conn.getJarFile();
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    String entryName = jarEntry.getName();
                    if (!entryName.startsWith(resourceName)) {
                        continue;
                    }
                    if (checkIsNotClass(entryName)) {
                        continue;
                    }
                    String classNameFullName = entryName.replace('/', '.');
                    addResult(classNameFullName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class FileProtocolHandler implements ProtocolHandler {

        @Override
        public String handleProtocol() {
            return "file";
        }

        @Override
        public void handle(String basePackage, URL url) {
            //文件包类型
            File rootFile = new File(url.getFile());
            findClass(rootFile, File.separator + basePackage.replace('.', File.separatorChar) + File.separator);
        }

        private void findClass(File rootFile, String subFilePath) {
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
                    findClass(file, subFilePath);
                }
            }
            String fileName = rootFile.getName();
            if (checkIsNotClass(fileName)) {
                return;
            }
            String path = rootFile.getPath();
            int i = path.indexOf(subFilePath);
            String subPath = path.substring(i + 1);
            String fullClassPath = subPath.replace(File.separatorChar, '.');
            addResult(fullClassPath);
        }
    }
}
