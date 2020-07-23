package com.mcredit.websocket.manager;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.websocket.Session;

import com.mcredit.model.object.CreditApplicationSignatureOBJ;
import com.mcredit.sharedbiz.aggregate.QRCodeKafkaConsumerAggregate;
import com.mcredit.util.JSONConverter;

public class SessionManager {
	
	private HashMap<String, Session> users = new HashMap<>();
	private ReentrantLock lock = new ReentrantLock();
	private Thread thread = null;
	private static SessionManager instance;

	public static synchronized SessionManager getInstance() {
		if (instance == null) {
			synchronized (SessionManager.class) {
				if (null == instance) {
					instance = new SessionManager();
				}
			}
		}
		return instance;
	}
	
	public void set(String key,Session value){
		users.put(key, value);
	}
	
	public Session get(String key){
		return users.get(key);
	}
	
	public void remove(String key){
		users.remove(key);
	}
	
	public Integer total(){
		return users.size();
	}
	
	public void processQRCode(){
		try{
			lock.lock();
			
			if(thread != null) 
				return;
			
			System.out.println("INIT PROCESS QR CODE");
			QRCodeKafkaConsumerAggregate eventSource = new QRCodeKafkaConsumerAggregate();

	        eventSource.addObserver((obj, arg) -> {
	            System.out.println("Received response: " + arg);
	            
	            try{
	            	if(arg != null){
			            CreditApplicationSignatureOBJ tempValue = JSONConverter.toObject(String.valueOf(arg),CreditApplicationSignatureOBJ.class);
			            System.out.println("TOTAL USERS: " + users.size());
			            
			            if(users.size() > 0){
			            	Session ses = this.get(tempValue.getScannedBy());
			            	if(ses != null)
			            		ses.getBasicRemote().sendText(tempValue.getMcContractNumber());
			            }
		            }
	            }catch(Throwable th){
	            	th.printStackTrace();
	            }
	            
	        });

	        thread = new Thread(eventSource);
	        thread.start();
	        
		}finally{
			lock.unlock();
		}
		
	}
	
}
