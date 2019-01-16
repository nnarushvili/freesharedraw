package com.nika.freedrawing.messaging;


import javax.websocket.*;

import com.google.gson.Gson;
public class MessageDecoder implements Decoder.Text<Message> {
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
	public Message decode(String message) throws DecodeException {
		// TODO Auto-generated method stub
		return GSON.fromJson(message, Message.class);
	}

	@Override
	public boolean willDecode(String s) {
		return (s != null);
	}
	
	
}
