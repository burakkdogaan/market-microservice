package com.example.market.service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.example.market.document.TradeDocument;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MarketWebSocketService implements WebSocketHandler {
	private Map<String,WebSocketSession> sessions = new ConcurrentHashMap<>();
	private ObjectMapper objectMapper;
	
	public MarketWebSocketService(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	@EventListener
	public void handleTradeEvent(TradeDocument trade) throws JsonProcessingException {
		var tradeAsJson = objectMapper.writeValueAsString(trade);
		sessions.values().forEach(session -> {
			synchronized(session) {
				try {
					session.sendMessage(new TextMessage(tradeAsJson));
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}
			}
			
		});
	}
	@Override
	
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.err.println("New Ws Connection :" + session.getId());
		sessions.put(session.getId(), session);
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		// tarayıcıdan mesaj gelmeyecek
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable t) throws Exception {
			System.err.println("Error in Websocket :" + t.getMessage());
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
			sessions.remove(session).getId();
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

}
