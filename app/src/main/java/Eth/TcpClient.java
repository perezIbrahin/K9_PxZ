package Eth;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Observable;

public class TcpClient extends Observable {
    private static final String TAG = "TcpClient";

    private String address;
    private Integer port;
    private Integer timeout = 100;

    private TcpClientState state = TcpClientState.DISCONNECTED;

    PrintWriter bufferOut;
    BufferedReader bufferIn;

    private Socket socket;

    /*public TcpClient(TcpClient.OnMessageReceived onMessageReceived) {
    }*/

    public TcpClient(String address, int port) {
        this.address = address;
        this.port = port;
    }

    protected void fireEvent(TcpEvent event) {
        setChanged();
        notifyObservers(event);
        clearChanged();
    }

    public void setPort(int port) {
        if (state == TcpClientState.CONNECTED) {
            throw new RuntimeException("Cannot change port while connected");
        }
        this.port = port;
    }

    public void connect() {
        if (state == TcpClientState.DISCONNECTED || state == TcpClientState.FAILED) {
            if (address == null || port == null) {
                throw new RuntimeException("Address or port missing");
            }
            new ConnectThread().start();
        } else {
            throw new RuntimeException("This client is already connected or connecting");
        }
    }

    private class ConnectThread extends Thread {
        @Override
        public void run() {
            try {
                state = TcpClientState.CONNECTING;
                fireEvent(new TcpEvent(TcpEventType.CONNECTION_STARTED, null));

                socket = new Socket();
                socket.connect(new InetSocketAddress(InetAddress.getByName(address), port), timeout);

                bufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                bufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                state = TcpClientState.CONNECTED;
                fireEvent(new TcpEvent(TcpEventType.CONNECTION_ESTABLISHED, null));

                new ReceiveMessagesThread().start();

            } catch (SocketTimeoutException e) {
                fireEvent(new TcpEvent(TcpEventType.CONNECTION_FAILED, e));

                Log.e(TAG, "Socket timed out: " + e);
                state = TcpClientState.FAILED;
            } catch (IOException e) {
                fireEvent(new TcpEvent(TcpEventType.CONNECTION_FAILED, e));

                Log.e(TAG, "Could not connect to host: " + e);
                state = TcpClientState.FAILED;
            }
        }
    }

    private class ReceiveMessagesThread extends Thread {
        @Override
        public void run() {
            Log.d(TAG, "run: ReceiveMessagesThread");

            while (state == TcpClientState.CONNECTED) {
                try {
                    Log.d(TAG, "run: ReceiveMessagesThread inside while");
                    if (bufferIn.ready()) {
                        String message = bufferIn.readLine();
                        Log.d(TAG, "run: ReceiveMessagesThread preparation");
                        //orig
                        if (message != null) {
                            Log.d(TAG, "run: ReceiveMessagesThread:" + message);
                            fireEvent(new TcpEvent(TcpEventType.MESSAGE_RECEIVED, message));
                        } else {
                            Log.d(TAG, "run: message null");
                        }
                    } else {
                        Log.d(TAG, "run: buffer input not ready");
                    }

                } catch (IOException e) {
                    fireEvent(new TcpEvent(TcpEventType.CONNECTION_LOST, null));

                    try {
                        bufferOut.flush();
                        bufferOut.close();

                        bufferIn.close();

                        socket.close();
                    } catch (IOException er) {
                        Log.e(TAG, "Error clearing connection: " + er);
                    }
                    state = TcpClientState.DISCONNECTED;
                }
            }
        }
    }

    public void receiveMessage() {
        Log.d(TAG, "receiveMessage: state:" + state);

        if (state == TcpClientState.CONNECTED) {
            new ReceiveMessagesThread().start();
        } else {
            throw new RuntimeException("This client is not connected, and can not receive any message");
        }
    }

    public void sendMessage(String message) {
        if (state == TcpClientState.CONNECTED) {
            new SendMessageThread(message).start();
        } else {
            throw new RuntimeException("This client is not connected, and cannot send any message");
        }
    }

    private class SendMessageThread extends Thread {
        private String messageLine;

        public SendMessageThread(String message) {
            this.messageLine = message;//this.messageLine = message + "\n";
        }

        @Override
        public void run() {
            if (bufferOut.checkError()) {
                try {
                    bufferOut.flush();
                    bufferOut.close();

                    bufferIn.close();
                    Log.d(TAG, "run: bufferIn.close ");

                } catch (IOException e) {
                    Log.e(TAG, "Error sending this message: " + e);
                }
            } else {
                bufferOut.print(messageLine);
                bufferOut.flush();

                fireEvent(new TcpEvent(TcpEventType.MESSAGE_SENT, messageLine.toString()));
                //flush. Added because so solve problem with end of the carrier
                sendMessageFlush("n");
            }
        }

    }

    public void sendMessageFlush(String message) {
        if (state == TcpClientState.CONNECTED) {
            new SendMessageThreadFlush(message).start();
        } else {
            throw new RuntimeException("This client is not connected, and cannot send any message");
        }
    }

    private class SendMessageThreadFlush extends Thread {
        private String messageLine;

        public SendMessageThreadFlush(String message) {
            this.messageLine = message;//this.messageLine = message + "\n";
        }

        @Override
        public void run() {
            if (bufferOut.checkError()) {
                try {
                    bufferOut.flush();
                    bufferOut.close();

                    bufferIn.close();
                    Log.d(TAG, "run: bufferIn.close ");

                } catch (IOException e) {
                    Log.e(TAG, "Error sending this message: " + e);
                }
            } else {
                bufferOut.println();
                bufferOut.flush();
                fireEvent(new TcpEvent(TcpEventType.MESSAGE_FLUSH, messageLine.toString()));
            }
        }
    }

    public void disconnect() {
        new DisconnectThread().run();
    }

    private class DisconnectThread extends Thread {
        @Override
        public void run() {
            try {
                bufferOut.flush();
                bufferOut.close();

                bufferIn.close();

                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "Error disconnecting this client: " + e);
            }

            fireEvent(new TcpEvent(TcpEventType.DISCONNECTED, null));
        }
    }
}
