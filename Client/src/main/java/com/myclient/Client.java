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

                    String[] splitMessage = message.split(" ");
                    if (splitMessage[0].equals("-file")) {
                        File file = new File("d:/file1.txt");
                        //InputStream inputStream = null;
                        OutputStream outputStream = clientSocket.getOutputStream();

                        try {
                            FileInputStream fileIn = new FileInputStream(file);
                            outputStream = clientSocket.getOutputStream();
                            //out.write(file.getName());
                            //out.flush();
                            //DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file));

                            byte[] bt = new byte[1024];
                            while ((fileIn.read(bt)) > 0) {
                                outputStream.write(bt);
                            }
                            out.flush();

                            //FileInputStream fis = new FileInputStream(file);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
//                        sendFileToServer(clientSocket.getOutputStream(), splitMessage[1]);
//                        //получаем OutputStream, чтобы писать в него данные запроса
//                        OutputStream outputStream = clientSocket.getOutputStream();
//
//                        Reader fr = new FileReader("d:/file1.txt");
//                        fr.transferTo(out);
//                        out.flush();
//
//                        outputStream.write(1);
//                        //outputStream.flush();
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

    private static void sendFileToServer(OutputStream outputStream, String s) {

    }
}