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

//    private static void saveFileFromClient(InputStream ins, String filePath) {
////        ** -file путь_файлу_тут
////        Отправка файла на сервер.
////        В этот момент указанный файл принимается на стороне сервера и сохраняется в произвольном месте.
////        Например: клиент пишет ... -file c:/path/to/data.txt
//
//        File file = new File("/resources");
//        FileReader fr = new FileReader(filePath);
//        try (
//        FileWriter fwr = new FileWriter(fr)) {
//            fwr.
//        }
//
//        try {
////            URL url = new URL("https://www.google.com.ua/images/srpr/logo11w.png");
////            InputStream inputStream = url.openStream();
////            Files.copy(inputStream, new File("c:/google.png").toPath());
//
//
//
//            InputStream fis = new FileInputStream(filePath);
//            Writer writer = new FileWriter();
//            Files.copy(filePath,fis);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//
//        try (OutputStream out = new FileOutputStream(file, false);
//             Writer writer = new OutputStreamWriter(out)) {
//            writer.write();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private static String getRequest(BufferedReader in) throws IOException {
        String message = in.readLine();
        System.out.println(message);
        return message;
    }

    private static void sendResponse(BufferedWriter out, String message) throws IOException {
        out.write("Hi, this is Server! You recently wrote:" + message + "\n");
        //System.out.println("3 Hi, this is Server! You recently wrote:" + message);
        out.flush();
        //System.out.println("4");
    }

//    private void send(String msg) {
//        try {
//            out.write(msg + "\n");
//            out.flush();
//        } catch (IOException ignored) {}
//    }
}
