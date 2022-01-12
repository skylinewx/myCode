package com.example.io;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

/**
 * http协议处理器
 */
public class HttpProtocolHandler extends BaseProtocolHandler{
    @Override
    public String protocolType() {
        return "http/1.1";
    }

    @Override
    public byte[] handleRequest(String requestParam) {
        String[] split = requestParam.split("\r\n");
        String json = split[split.length - 1];
        JSONObject jsonObject = new JSONObject(json);
        String formula = jsonObject.optString("formula");
        String bizResult = bizHandler.handleBiz(formula);
        StringBuilder builder = new StringBuilder();
        builder.append(protocolType()).append(" ").append("200").append(" OK\r\n");
        builder.append("\r\n");
        builder.append(bizResult);
        return builder.toString().getBytes(StandardCharsets.UTF_8);
    }


}
