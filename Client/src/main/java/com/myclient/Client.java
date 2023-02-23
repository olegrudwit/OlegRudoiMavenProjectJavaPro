package com.myclient;

import java.io.*;
import java.net.Socket;

public class Client {
    private static final String CLOSE_CONNECTION_KEY = "-exit";
    private static final String SEND_FILE_KEY = "-file";
    private static Socket clientSocket;
    private static boolean isActive = true;

    public static void main(String[] args) {
        System.out.println("Hello, client!");

        try {
            clientSocket = new Socket("localhost", 4004);
            try (
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(System.in));
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()));
                    BufferedWriter out = new BufferedWriter(
                            new OutputStreamWriter(clientSocket.getOutputStream()))) {

                System.out.println(in.readLine());

                while (isActive) {
                    System.out.print("You: ");
                    String message = reader.readLine();

                    sendMessage(out, message);

                    String[] splitMessage = message.split(" ");
                    switch (splitMessage[0]) {
                        case CLOSE_CONNECTION_KEY -> disconnectClientFromServer();
                        case SEND_FILE_KEY -> sendFileToServer(out, splitMessage[1]);
                    }

                    String serverOutback = in.readLine();
                    System.out.println(serverOutback);
                }
            } finally {
                System.out.println("Goodbye, client!");
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private static void sendMessage(BufferedWriter out, String message) throws IOException {
        out.write(message + "\n");
        out.flush();
    }

    private static void sendFileToServer(BufferedWriter out, String path) throws IOException {
        try {
            File file = new File(path);
            try (FileInputStream fileIn = new FileInputStream(file)) {
                OutputStream outputStream = clientSocket.getOutputStream();

                sendMessage(out, file.getName() + "/n");
                fileIn.transferTo(outputStream);
            }
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void disconnectClientFromServer() {
        isActive = false;
    }
}