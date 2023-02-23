package com.myserver;

import java.io.*;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public class MessageRunner extends Thread {
    private static final String CLOSE_CONNECTION_KEY = "-exit";
    private static final String SAVE_FILE_KEY = "-file";
    private Socket socket;
    private ClientConnection client;
    private BufferedReader in;
    private BufferedWriter out;
    private boolean isActive = true;


    public MessageRunner(ClientConnection client) throws IOException {
        this.client = client;
        this.socket = client.getClientSocket();

        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream()));
        start();
    }

    @Override
    public void run() {
        try {
            while (isActive) {
                String message = getRequest();

                String response = "[" + client.getName() + "]: " + message + "\n";

                String[] splitMessage = message.split(" ");
                switch (splitMessage[0]) {
                    case CLOSE_CONNECTION_KEY -> disconnectClientFromServer();
                    case SAVE_FILE_KEY -> getFileFromClient();
                    default -> Server.sendToEveryone(response);
                }
            }
            System.out.println("[" + client.getName() + "] left the chat");
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void disconnectClientFromServer() {
        isActive = false;
        Server.deleteClientConnection(client);
    }

    private void getFileFromClient() throws IOException {
        String fileName = getRequest();
        fileName = fileName.substring(0, fileName.length() - 2);

        Path filePath = Path.of("server/resources/" + client.getName());
        System.out.println(Files.exists(filePath));
        if(!Files.exists(filePath)) {
            Files.createDirectories(filePath);
            System.out.println(Files.exists(filePath));
        }

        //String filePath = "server/resources/"+ client.getName() + "/" + fileName;

        File file = new File(filePath + "/" + fileName);
//        while (!file.createNewFile()) {
//            fileName = "(1)" + fileName;
//            file.renameTo(new File(filePath + "/" + fileName));
//        }

        InputStream inputStream = socket.getInputStream();
        FileOutputStream fileReceive = new FileOutputStream(file, true);

//        SocketChannel sc = socket.getChannel();
//        FileChannel fc = new FileOutputStream(file).getChannel();
//
//
//        fc.transferFrom(sc,0, fc.size());

        Files.copy(inputStream, file.toPath());

//        byte[] bt = new byte[1024];
//        while ((inputStream.read(bt)) > 0) {
//            fileReceive.write(bt);
//            System.out.print("1");
//        }
        out.flush();
        System.out.println("end");

        sendResponse("[SERVER]: done!");
    }

    private String getRequest() throws IOException {
        return in.readLine();
    }

    void sendResponse(String message) {
        try {
            System.out.print(message);
            out.write(message);
            out.flush();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}