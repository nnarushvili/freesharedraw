package com.nika.freedrawing;

import com.nika.freedrawing.messaging.Message;
import com.nika.freedrawing.messaging.MessageDecoder;
import com.nika.freedrawing.messaging.MessageEncoder;
import javax.websocket.server.*;
import javax.websocket.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/join/{username}",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class)

public class DrawingServerEndPoint {

    private Session session;
    private final static Set<DrawingServerEndPoint> DRAWINGSERVERENDPOINTS
            = new CopyOnWriteArraySet<>();
    private final static HashMap<String, String> USERS = new HashMap<>();

    @OnOpen
    public void onOpen(Session session,
            @PathParam("username") String username) throws IOException, EncodeException {
        this.session = session;
        DRAWINGSERVERENDPOINTS.add(this);
        Message message = new Message();
        message.setRes("connected");
        USERS.values().forEach((userName) -> {
            new Runnable() {
                @Override
                public void run() {
                    message.setFrom(userName);
                    try {
                        session.getBasicRemote().sendObject(message);
                    } catch (IOException | EncodeException ex) {
                    }
                }
            }.run();
        });
        USERS.put(session.getId(), username);
        message.setFrom(username);
        broadcast(message);
    }

    @OnMessage
    public void onMessage(Session session, Message message) {
        DRAWINGSERVERENDPOINTS.remove(this);
        message.setFrom(USERS.get(session.getId()));
        try {
            broadcast(message);
        } catch (IOException | EncodeException ex) {
        }
        DRAWINGSERVERENDPOINTS.add(this);

    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        String username = USERS.get(session.getId());
        new Runnable() {
            @Override
            public void run() {
                USERS.remove(session.getId());
            }
        }.run();
        DRAWINGSERVERENDPOINTS.remove(this);
        new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.setFrom(username);
                message.setRes("disconnected");
                try {
                    broadcast(message);
                } catch (IOException | EncodeException ex) {
                }
            }
        }.run();
    }

    @OnError
    public void onError(Session session, Throwable throwable) throws IOException, EncodeException {
        session.close();
    }

    private static void broadcast(Message message) throws IOException, EncodeException {
        DRAWINGSERVERENDPOINTS.forEach((DrawingServerEndPoint endpoint) -> {
            new Runnable() {
                @Override
                public void run() {
                    try {
                        endpoint.session.getBasicRemote().sendObject(message);
                    } catch (IOException | EncodeException ex) {
                    }
                }
            }.run();
        });
    }
}
