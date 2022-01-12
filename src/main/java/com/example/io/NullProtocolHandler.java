package com.example.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * 空协议，没有任何协议的tcp连接处理器
 * @author skyline
 */
public class NullProtocolHandler extends BaseProtocolHandler {
    private static final Logger logger = LoggerFactory.getLogger(NullProtocolHandler.class);

    @Override
    public String protocolType() {
        return "";
    }

    @Override
    public byte[] handleRequest(String requestParam) {
        logger.info("空协议，不需要任何解析");
        String result = bizHandler.handleBiz(requestParam);
        return result.getBytes(StandardCharsets.UTF_8);
    }
}
