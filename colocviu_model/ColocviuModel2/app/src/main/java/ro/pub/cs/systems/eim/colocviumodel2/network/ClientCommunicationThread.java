package ro.pub.cs.systems.eim.colocviumodel2.network;

import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import android.os.Handler;

import ro.pub.cs.systems.eim.colocviumodel2.general.Constants;
import ro.pub.cs.systems.eim.colocviumodel2.general.Utilities;

public class ClientCommunicationThread extends Thread {
    private Socket socket;
    private Handler handler;
    private String city, parameter;
    private TextView clientResultTextView;

    public ClientCommunicationThread(Socket socket, Handler handler, String city, String parameter,
                                     TextView clientResultTextView) {
        this.socket = socket;
        this.handler = handler;
        this.city = city;
        this.parameter = parameter;
        this.clientResultTextView = clientResultTextView;
    }

    public void run() {
        if (socket == null) {
            try {
                this.socket = new Socket(Constants.SERVER_HOST, Constants.SERVER_PORT);
                Log.d(Constants.TAG, "[CLIENT] Created communication thread with: " + socket.getInetAddress() + ":" + socket.getLocalPort());
            }
            catch (UnknownHostException unknownHostException) {
                Log.e(Constants.TAG, "An exception has occurred: " + unknownHostException.getMessage());
                if (Constants.DEBUG) {
                    unknownHostException.printStackTrace();
                }
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
        try {
            BufferedReader socketReader = Utilities.getReader(socket);
            PrintWriter socketWriter = Utilities.getWriter(socket);

            socketWriter.println(city + "," + parameter);
            final String line = socketReader.readLine();
            if (line != null && !line.isEmpty()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        clientResultTextView.setText(line);
                    }
                });
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }
}
