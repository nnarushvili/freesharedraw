package com.nika.freedrawing.messaging;



import javax.websocket.*;

import com.google.gson.Gson;

public class MessageEncoder implements Encoder.Text<Message>{

	private static final Gson GSON = new Gson();
	
	

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(EndpointConfig arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String encode(Message message) throws EncodeException {
		return GSON.toJson(message);	
	}

}
