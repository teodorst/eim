package ro.pub.cs.systems.eim.colocviumodel2.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import ro.pub.cs.systems.eim.colocviumodel2.general.Constants;
import ro.pub.cs.systems.eim.colocviumodel2.general.Utilities;
import ro.pub.cs.systems.eim.colocviumodel2.model.WeatherInformation;

import static android.content.ContentValues.TAG;

public class ServerCommunicationThread extends Thread {
    private Socket socket;
    private List<WeatherInformation> db;

    public ServerCommunicationThread(Socket socket, List<WeatherInformation> db) {
        this.socket = socket;
        this.db = db;
        Log.d(Constants.TAG, "[SERVER] Created communication thread with: " + socket.getInetAddress() + ":" + socket.getLocalPort());
    }

    public WeatherInformation findWeatherInformation(String city) {
        WeatherInformation foundWeatherInformation = null;
        for (WeatherInformation wi : db) {
            if (wi.getCity().equals(city)) {
                foundWeatherInformation = wi;
                break;
            }
        }

        if (foundWeatherInformation == null) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                String url = Constants.API_SERVER + city + ".json";
                HttpGet httpGet = new HttpGet(url);
                ResponseHandler<String> requestHandler = new BasicResponseHandler();
                String responseString = httpClient.execute(httpGet, requestHandler);
                JSONObject resultJson = new JSONObject(responseString).getJSONObject("current_observation");
                String humidity = resultJson.getString(Constants.HUMIDITY);
                String weather = resultJson.getString(Constants.WEATHER);
                Double temperature = resultJson.getDouble(Constants.TEMPERATURE);
                Double pressure = resultJson.getDouble(Constants.PRESSURE);
                foundWeatherInformation = new WeatherInformation(weather, humidity, city,
                        temperature, pressure);
                synchronized (db) {
                    db.add(foundWeatherInformation);
                }

            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return foundWeatherInformation;
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
                String[] paramsFromSocket = line.split(",");
                String city = paramsFromSocket[0];
                String param = paramsFromSocket[1];
                WeatherInformation foundWeatherInformation = findWeatherInformation(city);
                String responseToClient = null;
                if (param.equals("weather")) {
                    responseToClient = foundWeatherInformation.getWeather();
                }
                else if (param.equals("humidity")){
                    responseToClient = foundWeatherInformation.getHumidity();
                }
                else if (param.equals("temperature")) {
                    responseToClient = "" + foundWeatherInformation.getTemperature();
                }
                else if (param.equals("pressure")) {
                    responseToClient = "" + foundWeatherInformation.getPressure();
                }
                else {
                    responseToClient = foundWeatherInformation.getCity() + ","
                            + foundWeatherInformation.getWeather() + "," + foundWeatherInformation.getHumidity()
                            + "," + foundWeatherInformation.getTemperature() + ","
                            + foundWeatherInformation.getPressure();
                }
                Log.d(TAG, "run: Server Seding" + responseToClient);
                responsePrintWriter.println(responseToClient);
                socket.close();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
