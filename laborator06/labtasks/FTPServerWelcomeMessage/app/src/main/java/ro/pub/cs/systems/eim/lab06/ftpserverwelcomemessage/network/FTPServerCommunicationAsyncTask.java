package ro.pub.cs.systems.eim.lab06.ftpserverwelcomemessage.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.net.Socket;

import ro.pub.cs.systems.eim.lab06.ftpserverwelcomemessage.general.Constants;
import ro.pub.cs.systems.eim.lab06.ftpserverwelcomemessage.general.Utilities;

public class FTPServerCommunicationAsyncTask extends AsyncTask<String, String, Void> {

    private TextView welcomeMessageTextView;

    public FTPServerCommunicationAsyncTask(TextView welcomeMessageTextView) {
        this.welcomeMessageTextView = welcomeMessageTextView;
    }

    @Override
    protected Void doInBackground(String... params) {
        Socket socket = null;
        try {
            socket = new Socket(params[0], Constants.FTP_PORT);
            BufferedReader socketReader = Utilities.getReader(socket);
            String line = socketReader.readLine();
            if (line.startsWith(Constants.FTP_MULTILINE_START_CODE)) {
                Log.d("Async task", "doInBackground: " + line);
                line = line.substring(line.indexOf(Constants.FTP_MULTILINE_START_CODE));
                publishProgress(line);
            }

            line = socketReader.readLine();
            while (!line.equals(Constants.FTP_MULTILINE_END_CODE1) && !line.startsWith(Constants.FTP_MULTILINE_END_CODE2)) {
                publishProgress(line);
                line = socketReader.readLine();
            }


            socket.close();
            // TODO exercise 4
            // open socket with FTPServerAddress.getText().toString() (taken from param[0]) and port (Constants.FTP_PORT = 21)
            // get the BufferedReader attached to the socket (call to the Utilities.getReader() method)
            // should the line start with Constants.FTP_MULTILINE_STARTCODE = "220-", the welcome message is processed
            // read lines from server while
            // - the value is different from Constants.FTP_MULTILINE_END_CODE1 = "220"
            // - the value does not start with Constants.FTP_MULTILINE_END_CODE2 = "220 "
            // append the line to the welcomeMessageTextView text view content (on the UI thread !!!) - publishProgress(...)
            // close the socket
        } catch (Exception exception) {
            Log.d(Constants.TAG, exception.getMessage());
            if (Constants.DEBUG) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        welcomeMessageTextView.setText("");
    }

    @Override
    protected void onProgressUpdate(String... progres) {
        // TODO exercise 4
        // append the progress[0] to the welcomeMessageTextView text view
        welcomeMessageTextView.setText(welcomeMessageTextView.getText() + progres[0]);
    }

    @Override
    protected void onPostExecute(Void result) {}

}
