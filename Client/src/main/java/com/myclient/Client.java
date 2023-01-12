package com.myclient;

import java.io.*;
import java.net.Socket;

public class Client {
    private static Socket clientSocket;

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

                while (true) {

                    System.out.println("Input your message:");
                    String message = reader.readLine();

                    //System.out.println("2");

                    out.write(message + "\n");
                    //System.out.println("3");
                    out.flush();
                    //System.out.println("4 ok");

                    if (message.equals("-exit")) {
                        break;
                    }

                    //System.out.println("5");

                    String serverOutback = in.readLine();
                    //System.out.println("6");
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
}