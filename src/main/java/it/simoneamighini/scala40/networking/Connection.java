package it.simoneamighini.scala40.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.TimerTask;

class Connection {
    private static final int SOCKET_TIMEOUT = 10000;

    private final Socket foreignHostSocket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private TimerTask echoSender;

    Connection(Socket socket) throws IOException {
        this.foreignHostSocket = socket;
        foreignHostSocket.setSoTimeout(SOCKET_TIMEOUT);
        outputStream = new ObjectOutputStream(foreignHostSocket.getOutputStream());
        inputStream = new ObjectInputStream(foreignHostSocket.getInputStream());
    }

    TimerTask getEchoSender() {
        return echoSender;
    }

    void setEchoSender(TimerTask echoSender) {
        this.echoSender = echoSender;
    }

    String getRemoteAddress() {
        return foreignHostSocket.getInetAddress().getHostAddress() + ":" + foreignHostSocket.getPort();
    }

    void send(Event event) throws IOException {
        int errorCount = 0;
        while (true) {
            try {
                synchronized (outputStream) {
                    outputStream.writeObject(event);
                    outputStream.flush();
                    outputStream.reset();
                }
                break;
            } catch (IOException exception) {
                errorCount++;
                if (errorCount >= 3) {
                    throw exception;
                }
            }
        }
    }

    Event receive() throws IOException, ClassNotFoundException {
        return (Event) inputStream.readObject();
    }

    void close() throws IOException {
        synchronized (outputStream) {
            outputStream.close();
        }
        inputStream.close();
        foreignHostSocket.close();
    }
}
