package com.imooc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

/**
 * WebSocket的配置
 */
@Component
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return  new ServerEndpointExporter();
    }


}
