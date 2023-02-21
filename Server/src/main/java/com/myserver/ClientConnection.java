package com.myserver;


import java.net.Socket;
import java.sql.Timestamp;
import java.util.Objects;

public class ClientConnection {
    private String name;
    private Timestamp loginTime;
    private Socket clientSocket;

    public ClientConnection(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Timestamp loginTime) {
        this.loginTime = loginTime;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientConnection that = (ClientConnection) o;

        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(loginTime, that.loginTime)) return false;
        return clientSocket.equals(that.clientSocket);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (loginTime != null ? loginTime.hashCode() : 0);
        result = 31 * result + clientSocket.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ClientConnection{" +
                "name='" + name + '\'' +
                ", loginTime=" + loginTime +
                ", clientSocket=" + clientSocket +
                '}';
    }
}