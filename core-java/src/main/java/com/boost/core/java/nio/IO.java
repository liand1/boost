package com.boost.core.java.nio;

import java.net.ServerSocket;
import java.net.Socket;

public class IO {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(9000);
        while (true) {
            System.out.println("Waiting for connection");
            // blocking
            Socket clientSocket = serverSocket.accept();
            System.out.println("There is a client connection");
            handler(clientSocket);
        }
    }

    private static void handler(Socket clientSocket) throws Exception{
        byte[] bytes = new byte[1024];
        System.out.println("ready for read");
        int read = clientSocket.getInputStream().read(bytes);
        if(read != -1) {
            System.out.println("data: " + new String(bytes, 0, read));
        }
    }
}
