package ro.pub.cs.systems.eim.lab06.pheasantgame.network;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import ro.pub.cs.systems.eim.lab06.pheasantgame.general.Constants;
import ro.pub.cs.systems.eim.lab06.pheasantgame.general.Utilities;

public class ClientCommunicationThread extends Thread {

    private Socket socket = null;

    private String mostRecentWordSent = new String();
    private String mostRecentValidPrefix = new String();

    private Context context;
    private Handler handler;
    private EditText wordEditText;
    private Button sendButton;
    private TextView clientHistoryTextView;

    public ClientCommunicationThread(Socket socket, Context context, Handler handler, EditText wordEditText, Button sendButton, TextView clientHistoryTextView) {
        this.socket = socket;
        this.context = context;
        this.handler = handler;
        this.wordEditText = wordEditText;
        this.sendButton = sendButton;
        this.clientHistoryTextView = clientHistoryTextView;
        if (socket == null) {
            try {
                socket = new Socket(Constants.SERVER_HOST, Constants.SERVER_PORT);
            } catch (UnknownHostException unknownHostException) {
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
        Log.d(Constants.TAG, "[CLIENT] Created communication thread with: " + socket.getInetAddress() + ":" + socket.getLocalPort());
    }

    public void run() {
        try {
            BufferedReader responseReader = Utilities.getReader(socket);
            PrintWriter requestPrintWriter = Utilities.getWriter(socket);
            // TODO exercise 7b
            final String myWord = wordEditText.getText().toString();
            final String line;

            if (myWord.length() > 2) {
                requestPrintWriter.println(myWord);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        clientHistoryTextView.setText(clientHistoryTextView.getText() + "\nClient sent word " + myWord);
                    }
                });

                line = responseReader.readLine();
                if (line == null) {
                    return;
                }

                if (line.equals(Constants.END_GAME)) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            sendButton.setEnabled(false);
                            wordEditText.setEnabled(false);
                            clientHistoryTextView.setEnabled(false);
                        }
                    });

                }
                else if (line.equals(wordEditText.getText().toString())){
                    clientHistoryTextView.post(new Runnable() {
                        @Override
                        public void run() {
                            clientHistoryTextView.setText(clientHistoryTextView.getText() + "\nClient recived word " + line);
                        }
                    });
                }
                else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            wordEditText.setText(line.substring(line.length() - 2));
                            wordEditText.setSelection(2);
                            clientHistoryTextView.setText(clientHistoryTextView.getText() + "\nClient recived word " + line);
                        }
                    });
                }
            }
            else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        clientHistoryTextView.setText(clientHistoryTextView.getText() + "\nError word incorrect " + myWord);
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