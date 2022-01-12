package com.example.io;

/**
 * 协议处理器
 * @author skyline
 */
public abstract class BaseProtocolHandler {

    final BizHandler bizHandler;

    public BaseProtocolHandler(){
        bizHandler = new BizHandler();
    }

    /**
     * 处理的协议类型
     * @return 可以处理的协议名
     */
    public abstract String protocolType();

    /**
     * 解析请求并处理业务逻辑
     * @param requestParam
     * @return
     */
    public abstract byte[] handleRequest(String requestParam);
}
