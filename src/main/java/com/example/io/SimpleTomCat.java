package com.example.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ServiceLoader;

/**
 * 劣化版tomcat<br/>
 * 支持设置不同的IO模型
 */
public class SimpleTomCat {
    private static final Logger logger = LoggerFactory.getLogger(SimpleTomCat.class);

    public static void main(String[] args) throws IOException {
        Properties systemProperties = getSystemProperties();
        String serverModel = systemProperties.getProperty("server.model");
        String port = systemProperties.getProperty("server.port");
        String protocolType = systemProperties.getProperty("protocol.type");
        IServer startServer = getServer(serverModel);
        BaseProtocolHandler protocolHandler = getProtocolHandler(protocolType);
        startServer.start(protocolHandler,Integer.parseInt(port));
    }

    private static BaseProtocolHandler getProtocolHandler(String protocolType){
        ServiceLoader<BaseProtocolHandler> protocolHandlers = ServiceLoader.load(BaseProtocolHandler.class);
        for (BaseProtocolHandler protocolHandler : protocolHandlers) {
            String type = protocolHandler.protocolType();
            if (protocolType.equals(type)) {
                return protocolHandler;
            }
        }
        throw new RuntimeException("protocol.type="+ protocolType +"，不存在");
    }

    private static IServer getServer(String serverModel) {
        ServiceLoader<IServer> servers = ServiceLoader.load(IServer.class);
        for (IServer server : servers) {
            String simpleName = server.getClass().getSimpleName();
            if (serverModel.equals(simpleName)) {
                return server;
            }
        }
        throw new RuntimeException("server.model="+ serverModel +"，不存在");
    }

    private static Properties getSystemProperties() throws IOException {
        InputStream resourceInputStream = SimpleTomCat.class.getClassLoader().getResourceAsStream("simple-tomcat.properties");
        Properties systemProperties = new Properties();
        systemProperties.load(resourceInputStream);
        resourceInputStream.close();
        logger.info("读取系统配置{}", systemProperties);
        return systemProperties;
    }
}
