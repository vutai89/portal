package com.mcredit.websocket;
import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.mcredit.websocket.manager.SessionManager;

@ServerEndpoint(value = "/qr/{username}")
public class QRWebsocket {

	private SessionManager sesManager = SessionManager.getInstance();
	@OnOpen
	public void onOpen(Session session,@PathParam("username") String username) {
		
		sesManager.processQRCode();
		
		sesManager.set(username, session);
		System.out.println("TOTAL USERS: " + sesManager.total());
		System.out.println("WEBSOCKET ADD USER: " + username);
	}

	@OnMessage
	public void onMessage(String message, Session userSession,@PathParam("username") String username) throws IOException, InterruptedException {	
		System.out.println("WEBSOCKET USER: " + username);
		userSession.getBasicRemote().sendText(message);
	}

	@OnClose
	public void onClose(Session session,@PathParam("username") String username) {
		sesManager.remove(username);
		System.out.println("WEBSOCKET REMOVE USER: " + username);
	}

	@OnError
	public void onError(Throwable t) {
		t.printStackTrace();
	}

}
