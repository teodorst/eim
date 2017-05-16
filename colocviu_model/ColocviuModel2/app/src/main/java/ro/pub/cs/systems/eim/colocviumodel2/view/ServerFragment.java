package ro.pub.cs.systems.eim.colocviumodel2.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ro.pub.cs.systems.eim.colocviumodel2.R;
import ro.pub.cs.systems.eim.colocviumodel2.model.WeatherInformation;
import ro.pub.cs.systems.eim.colocviumodel2.network.ServerThread;

public class ServerFragment extends Fragment {

    private ServerThread serverThread;
    private List<WeatherInformation> db = new ArrayList<WeatherInformation>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        return inflater.inflate(R.layout.fragment_server, parent, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        serverThread = new ServerThread(db);
        serverThread.start();
    }

    @Override
    public void onDestroy() {
        if (serverThread != null) {
            serverThread.stopServer();
        }
        super.onDestroy();
    }
}
