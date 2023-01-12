package com.myserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {
    private static Socket clientSocket;
    private static ServerSocket server;
    private static List<Socket> activeConnections;

    public static void main(String[] args) {
        System.out.println("System started");

        try {
            try {


                server = new ServerSocket(4004);
                System.out.println("Server started");

                clientSocket = server.accept();

                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                     BufferedWriter out = new BufferedWriter(
                             new OutputStreamWriter(clientSocket.getOutputStream()))) {

                    while (true) {

                        //System.out.println("1 ready");
                        String message = in.readLine();
                        System.out.println(message);

                        out.write("Hi, this is Server! You recently wrote:" + message + "\n");
                        //System.out.println("3 Hi, this is Server! You recently wrote:" + message);
                        out.flush();
                        //System.out.println("4");


                    }
                } finally {
                    clientSocket.close();
                }
            } finally {
                System.out.println("Server stopped");
                server.close();
            }


        } catch (IOException e) {
            System.err.println(e);
        }

    }

//    private void send(String msg) {
//        try {
//            out.write(msg + "\n");
//            out.flush();
//        } catch (IOException ignored) {}
//    }
}
