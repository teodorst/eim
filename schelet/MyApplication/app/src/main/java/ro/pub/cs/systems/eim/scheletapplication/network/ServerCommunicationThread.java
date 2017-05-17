package ro.pub.cs.systems.eim.scheletapplication.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import ro.pub.cs.systems.eim.scheletapplication.general.Constants;
import ro.pub.cs.systems.eim.scheletapplication.general.Utilities;
import ro.pub.cs.systems.eim.scheletapplication.models.MyModel;

import static android.content.ContentValues.TAG;

public class ServerCommunicationThread extends Thread {

    private Socket socket;
    private List<MyModel> db;

    public ServerCommunicationThread(Socket socket, List<MyModel> db) {
        this.socket = socket;
        this.db = db;
        Log.d(Constants.TAG, "[SERVER] Created communication thread with: " + socket.getInetAddress() + ":" + socket.getLocalPort());
    }

    public String httpGetRequest(String queryParam) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        String url = Constants.API_SERVER + queryParam + ".json";
        HttpGet httpGet = new HttpGet(url);
        ResponseHandler<String> requestHandler = new BasicResponseHandler();
        String responseString = httpClient.execute(httpGet, requestHandler);
        Log.d(Constants.TAG, responseString);
        return responseString;
    }

    public String httpPostRequest(String bodyData) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Constants.WEB_POST_SERVER_ADDRESS);
        List<NameValuePair> bodyList = new ArrayList<NameValuePair>();
        bodyList.add(new BasicNameValuePair("BodyData:", bodyData));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(bodyList, HTTP.UTF_8);
        httpPost.setEntity(urlEncodedFormEntity);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseString = httpClient.execute(httpPost, responseHandler);
        Log.d(Constants.TAG, responseString);
        return responseString;
    }


    public MyModel findMyModelInDb(String queryParam) {
        MyModel myModel = null;

        for (MyModel dbEntry : db) {
            if (queryParam.equals(dbEntry.getField())) {
                myModel = dbEntry;
                break;
            }
        }

        if (myModel == null) {

            try {

                // Dummy Post Request
                String responseString = httpPostRequest("EIM");

                // Get REQUEST
                responseString = httpGetRequest(queryParam);

                // Parse JSON
                JSONObject resultJson = new JSONObject(responseString).getJSONObject("current_observation");
                String field = resultJson.getString(Constants.RESPONSE_FIELD);

                myModel = new MyModel(queryParam);

                synchronized (db) {
                    db.add(myModel);
                }

            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            myModel = new MyModel();
            myModel.setField(queryParam);
        }

        return myModel;
    }


    public void run() {
        try {
            if (socket == null) {
                return;
            }
            BufferedReader requestReader = Utilities.getReader(socket);
            PrintWriter responsePrintWriter = Utilities.getWriter(socket);

            String line = requestReader.readLine();
            if (line != null && !line.isEmpty()) {
                MyModel myModel = findMyModelInDb(line);
                String responseToClient = null;
                responseToClient = myModel.getField();
                Log.d(TAG, "run: Server Seding" + responseToClient);
                responsePrintWriter.println(responseToClient);
                socket.close();
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}

