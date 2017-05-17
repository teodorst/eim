package ro.pub.cs.systems.eim.scheletapplication.views;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ro.pub.cs.systems.eim.scheletapplication.R;
import ro.pub.cs.systems.eim.scheletapplication.models.MyModel;
import ro.pub.cs.systems.eim.scheletapplication.network.ServerThread;

public class ServerFragment extends Fragment {
    private ServerThread serverThread;
    private List<MyModel> db = new ArrayList<MyModel>();

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
