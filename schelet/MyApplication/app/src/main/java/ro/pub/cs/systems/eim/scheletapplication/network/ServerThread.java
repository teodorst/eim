package ro.pub.cs.systems.eim.scheletapplication.network;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import ro.pub.cs.systems.eim.scheletapplication.general.Constants;
import ro.pub.cs.systems.eim.scheletapplication.models.MyModel;

public class ServerThread extends Thread {

    private boolean isRunning;
    private List<MyModel> db;
    private ServerSocket serverSocket;

    public ServerThread(List<MyModel> db) {
        this.db = db;
        try {
            Log.d(Constants.TAG, "[SERVER] Created server thread, listening on port " + Constants.SERVER_PORT);
            serverSocket = new ServerSocket(Constants.SERVER_PORT);
            isRunning = true;
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                Log.d(Constants.TAG, "[SERVER] Incomming communication " + socket.getInetAddress() + ":" + socket.getLocalPort());
            } catch (SocketException socketException) {
                Log.e(Constants.TAG, "An exception has occurred: " + socketException.getMessage());
                if (Constants.DEBUG) {
                    socketException.printStackTrace();
                }
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
            if (socket != null) {
                // Start communication handler thread
                ServerCommunicationThread serverCommunicationThread = new ServerCommunicationThread(socket, db);
                serverCommunicationThread.start();
            }
        }
    }

    public void stopServer() {
        try {
            isRunning = false;
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

}
