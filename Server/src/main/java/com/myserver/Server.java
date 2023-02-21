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
    static Map<String, ClientConnection> activeConnections = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("System started");

        try {
            try {
                server = new ServerSocket(SERVER_SOCKET_PORT);
                System.out.println("Server started");

                while (true) {
                    //System.out.println("1");
                    Socket socket = server.accept();

                    //System.out.println("2");
                    ClientConnection client = initClientConnection(socket);

                    try {
                        MessageRunner messageRunner = new MessageRunner(client);
                    } catch (IOException e) {
                        System.err.println(e);
                        System.err.println(client.getName() + " lost connection");
                        //deleteClientConnection(client);
                        System.out.println("delete");
                        socket.close();
                    }
                    //System.out.println("4");
                }
            } finally {
                System.out.println("Server stopped");
                server.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    static void deleteClientConnection(ClientConnection client) {
        //System.out.println("before " + activeConnections.size());
        activeConnections.remove(client.getName());
        //System.out.println("after " + activeConnections.size());
    }

    private static ClientConnection initClientConnection(Socket socket) {
        var client = new ClientConnection(socket);
        String clientName = CLIENT_NAME_PATTERN + clientCounter++;
        //System.out.println("3 " + clientName);
        client.setName(clientName);
        client.setLoginTime(new Timestamp(new Date().getTime()));

        activeConnections.put(clientName, client);
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
