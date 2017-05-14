package ro.pub.cs.systems.eim.lab06.pheasantgame.network;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Random;

import ro.pub.cs.systems.eim.lab06.pheasantgame.general.Constants;
import ro.pub.cs.systems.eim.lab06.pheasantgame.general.Utilities;

public class ServerCommunicationThread extends Thread {

    private Socket socket;
    private TextView serverHistoryTextView;

    private Random random = new Random();

    private String expectedWordPrefix = new String();

    public ServerCommunicationThread(Socket socket, TextView serverHistoryTextView) {
        if (socket != null) {
            this.socket = socket;
            Log.d(Constants.TAG, "[SERVER] Created communication thread with: " + socket.getInetAddress() + ":" + socket.getLocalPort());
        }
        this.serverHistoryTextView = serverHistoryTextView;
    }

    public void run() {
        try {
            if (socket == null) {
                return;
            }
            boolean isRunning = true;
            BufferedReader requestReader = Utilities.getReader(socket);
            PrintWriter responsePrintWriter = Utilities.getWriter(socket);
            String prefix = null;
            List<String> newWords;

            while (isRunning) {

                // TODO exercise 7a
                final String line = requestReader.readLine();
                Log.d("SERVER", "run: RECEIVED " + line);
                serverHistoryTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        serverHistoryTextView.setText(serverHistoryTextView.getText() + "\nServer Received " + line);
                    }
                });
                if (line.equals(Constants.END_GAME)) {
                    break;
                }
                else if (Utilities.wordValidation(line)) {
                    if (prefix == null || prefix.startsWith(prefix)) {
                        prefix = line.substring(line.length() - 2);
                        newWords = Utilities.getWordListStartingWith(prefix);

                        if (newWords == null || newWords.isEmpty()) {
                            serverHistoryTextView.post(new Runnable() {
                                @Override
                                public void run() {
                                    serverHistoryTextView.setText(serverHistoryTextView.getText() + "\nServer Send " + Constants.END_GAME);
                                }
                            });
                            responsePrintWriter.println(Constants.END_GAME);
                            prefix = null;

                        } else {
                            final String newWord = newWords.get(random.nextInt(newWords.size()));
                            prefix = newWord.substring(newWord.length() - 2);
                            serverHistoryTextView.post(new Runnable() {
                                @Override
                                public void run() {
                                    serverHistoryTextView.setText(serverHistoryTextView.getText() + "\nServer Send " + newWord);
                                }
                            });
                            responsePrintWriter.println(newWord);
                        }
                    }
                    else {
                        serverHistoryTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                serverHistoryTextView.setText(serverHistoryTextView.getText() + "\nServer Send " + line);
                            }
                        });
                        responsePrintWriter.println(line);
                    }
                }
                else {
                    serverHistoryTextView.post(new Runnable() {
                        @Override
                        public void run() {
                            serverHistoryTextView.setText(serverHistoryTextView.getText() + "\nServer Send " + line);
                        }
                    });
                    responsePrintWriter.println(line);
                }
            }
            isRunning = false;
            socket.close();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }
}
