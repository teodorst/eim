package ro.pub.cs.systems.eim.scheletapplication.network;

import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import ro.pub.cs.systems.eim.scheletapplication.general.Constants;
import ro.pub.cs.systems.eim.scheletapplication.general.Utilities;

public class ClientCommunicationThread extends Thread {

    private Socket socket;
    private Handler handler;
    private String queryParam;
    private TextView updateComponent;

    public ClientCommunicationThread(Socket socket, Handler handler, String queryParam,
                                     TextView updateComponent) {
        this.socket = socket;
        this.handler = handler;
        this.queryParam = queryParam;
        this.updateComponent = updateComponent;
    }

    public void run() {
        if (socket == null) {
            try {
                this.socket = new Socket(Constants.SERVER_HOST, Constants.SERVER_PORT);
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
            socketWriter.println(queryParam);
            final String line = socketReader.readLine();
            if (line != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        updateComponent.setText(line);
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
