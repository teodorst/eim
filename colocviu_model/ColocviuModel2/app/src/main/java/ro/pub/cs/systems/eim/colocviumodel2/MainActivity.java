package ro.pub.cs.systems.eim.colocviumodel2;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ro.pub.cs.systems.eim.colocviumodel2.view.ClientFragment;
import ro.pub.cs.systems.eim.colocviumodel2.view.ServerFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.server_container, new ServerFragment());
        fragmentTransaction.add(R.id.client_container, new ClientFragment());
        fragmentTransaction.commit();
    }
}
