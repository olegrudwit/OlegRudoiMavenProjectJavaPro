package com.myserver;

import java.io.*;
import java.net.Socket;

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
            String greetingsMassage = "[SERVER] "
                    + client.getName()
                    + "join to conversation";
            sendResponse(greetingsMassage);
            while (isActive) {
                String message = getRequest();

                String[] splitMessage = message.split(" ");
                switch (splitMessage[0]) {
                    case CLOSE_CONNECTION_KEY -> disconnectClientFromServer();
                    case SAVE_FILE_KEY -> saveFileFromClient();
                    default -> sendResponse(message);
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private void disconnectClientFromServer() {
        isActive = false;
        Server.deleteClientConnection(client);
        System.out.println("Client left the chat " + client);
    }

    private void saveFileFromClient() throws IOException {
        //saveFileFromClient(clientSocket.getInputStream(), splitMessage[1]);
        //File file = new File("/resources");

        //Writer fwr = new FileWriter
        //String fileName = getRequest(in);
        String fileName = "file.txt";
        String filePath = "/resources" + fileName;

        InputStream inputStream = socket.getInputStream();
        FileOutputStream fileReceive = new FileOutputStream("d:/file.txt", true);
        //FileWriter fwr = new FileWriter(file);
        byte[] bt = new byte[1024];
        while ((inputStream.read(bt)) > 0) {
            fileReceive.write(bt);
            //fwr.write(bt);
            //Files.write(Paths.get(filePath), bt, StandardOpenOption.APPEND);
        }

        //Files.copy(inputStream, new File("d:/file0.txt").toPath());
        out.flush();
    }
//
//    public static void main(String[] args) {
//        System.out.println("System started");
//
//        try {
//            try {
////                server = new ServerSocket(SERVER_SOCKET_PORT);
////                System.out.println("Server started");
//
//                while (true) {
//
//
////                    Socket clientSocket = server.accept();
//
//
////                    try (BufferedReader in = new BufferedReader(
////                            new InputStreamReader(clientSocket.getInputStream()));
////                         BufferedWriter out = new BufferedWriter(
////                                 new OutputStreamWriter(clientSocket.getOutputStream()))) {
//
//                    //if (in.)
//                    while (true) {
//
//                        //System.out.println("1 ready");
//                        String message = getRequest(in);
//
//                        if (message.equals("-exit")) {
//                            clientSocket.close();
//                            break;
//                        }
//                        String[] splitMessage = message.split(" ");
//                        if (splitMessage[0].equals("-file")) {
//                            //saveFileFromClient(clientSocket.getInputStream(), splitMessage[1]);
//                            //File file = new File("/resources");
//
//                            //Writer fwr = new FileWriter
//                            //String fileName = getRequest(in);
//                            String fileName = "file.txt";
//                            String filePath = "/resources" + fileName;
//
//                            InputStream inputStream = clientSocket.getInputStream();
//                            FileOutputStream fileReceive = new FileOutputStream("d:/file.txt", true);
//                            //FileWriter fwr = new FileWriter(file);
//                            byte[] bt = new byte[1024];
//                            while ((inputStream.read(bt)) > 0) {
//                                fileReceive.write(bt);
//                                //fwr.write(bt);
//                                //Files.write(Paths.get(filePath), bt, StandardOpenOption.APPEND);
//                            }
//
//                            //Files.copy(inputStream, new File("d:/file0.txt").toPath());
//                            out.flush();
//                        }
//
//                        sendResponse(out, message);
//
//
//                    }
////                    } finally {
////                        clientSocket.close();
////                    }
//                }
//            } finally {
//                System.out.println("Server stopped");
//                server.close();
//            }
//
//
//        } catch (IOException e) {
//            System.err.println(e);
//        }
//
//    }

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

    private String getRequest() throws IOException {
        String message = in.readLine();
        System.out.println(message);
        return message;
    }

    private void sendResponse(String message) throws IOException {
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
