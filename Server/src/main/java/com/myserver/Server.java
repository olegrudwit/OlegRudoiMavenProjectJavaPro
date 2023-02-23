package com.myserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private static ServerSocket server;
    private static final int SERVER_SOCKET_PORT = 4004;
    private static final String CLIENT_NAME_PATTERN = "client-";
    private static int clientCounter = 1;
    static Map<String, MessageRunner> activeConnections = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("System started");

        try {

            try {
                server = new ServerSocket(SERVER_SOCKET_PORT);
                System.out.println("Server started");

                while (true) {
                    Socket socket = server.accept();
                    ClientConnection client = initClientConnection(socket);

                    try {
                        MessageRunner runner = new MessageRunner(client);
                        activeConnections.put(client.getName(), runner);

                        String greetingsMassage = "[SERVER]: ["
                                + client.getName()
                                + "] joined the conversation" + "\n";
                        sendToEveryone(greetingsMassage);
                    } catch (IOException e) {
                        System.err.println(e);
                        socket.close();
                    }
                }
            } finally {
                System.out.println("Server stopped");
                server.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    static void sendToEveryone(String message) {
        activeConnections.forEach((key, value) -> value.sendResponse(message));
    }

    static void deleteClientConnection(ClientConnection client) {
        activeConnections.remove(client.getName());
    }

    private static ClientConnection initClientConnection(Socket socket) {
        var client = new ClientConnection(socket);
        String clientName = CLIENT_NAME_PATTERN + clientCounter++;

        client.setName(clientName);
        client.setLoginTime(new Timestamp(new Date().getTime()));

        return client;
    }
}