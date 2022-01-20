package com.example.market.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.example.market.repository.BinanceMarketRepository;
import com.example.market.service.MarketWebSocketService;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	private MarketWebSocketService marketWebSocketService;
	
	public WebSocketConfig(MarketWebSocketService marketWebSocketService) {
		this.marketWebSocketService = marketWebSocketService;
	}

	@Bean 
	public WebSocketClient webSocketClient() {
		var client = new StandardWebSocketClient();
		
		return client;
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(marketWebSocketService, "/trades").setAllowedOrigins("*");
	}
	
}
