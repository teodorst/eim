package ro.pub.cs.systems.eim.practicaltest02.view;

import android.app.Fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ro.pub.cs.systems.eim.practicaltest02.R;
import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.network.ServerThread;


public class ServerFragment extends Fragment {
    private ServerThread serverThread;
    private EditText portEditText;
    private Button serverStartButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        return inflater.inflate(R.layout.fragment_server, parent, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        portEditText = (EditText) getActivity().findViewById(R.id.server_port_edit_text);
        serverStartButton = (Button) getActivity().findViewById(R.id.server_start_button);
        serverStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String portString = portEditText.getText().toString();
                if (portString != null && !portString.isEmpty()) {
                    int port = Integer.parseInt(portString);
                    serverThread = new ServerThread(port);
                    serverThread.start();
                }
                else {
                    Log.d(Constants.TAG, "PORTUL ESTE NULL");
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        if (serverThread != null) {
            serverThread.stopServer();
        }
        super.onDestroy();
    }
}
