package com.example.io;

import java.io.IOException;

/**
 * server服务
 * @author skyline
 */
public interface IServer {

    /**
     * 启动服务
     * @param protocolHandler 协议处理器
     * @param port 端口号
     */
    void start(BaseProtocolHandler protocolHandler, int port) throws IOException;
}
