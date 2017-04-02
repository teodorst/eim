package phonedialer.lab03.eim.systems.cs.pub.ro.phonedialer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


public class PhoneDialerActivity extends AppCompatActivity {
    private ButtonClickListener buttonListener;
    private CallClickListener callClickListener;
    private DeleteClickListener deleteClickListener;

    private Button button0, button1, button2, button3, button4, button5, button6, button7, button8,
            button9, buttonhashtag, buttonstar;
    private ImageButton buttondelete, buttoncall, buttonhang;

    private EditText phoneNumberEditText;
    final static public int PERMISSION_REQUEST_CALL_PHONE = 12312;

    private class ButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String newChar = ((Button) view).getText().toString();
            String newString = phoneNumberEditText.getText().toString();
            newString = newString + newChar;
            phoneNumberEditText.setText(newString);
        }
    }

    private class DeleteClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String newString = phoneNumberEditText.getText().toString();
            newString = newString.substring(0, newString.length() - 1);
            phoneNumberEditText.setText(newString);
        }
    }

    private class CallClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            if (ContextCompat.checkSelfPermission(PhoneDialerActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        PhoneDialerActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        PERMISSION_REQUEST_CALL_PHONE);
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneNumberEditText.getText().toString()));
                startActivity(intent);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_dialer);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        buttonListener = new ButtonClickListener();
        callClickListener = new CallClickListener();
        deleteClickListener = new DeleteClickListener();


        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);
        buttonhashtag = (Button) findViewById(R.id.buttonhashtag);
        buttonstar = (Button) findViewById(R.id.buttonstar);

        buttondelete = (ImageButton) findViewById(R.id.buttondel);
        buttoncall = (ImageButton) findViewById(R.id.buttoncall);
        buttonhang = (ImageButton) findViewById(R.id.buttonhang);


        button0.setOnClickListener(buttonListener);
        button1.setOnClickListener(buttonListener);
        button2.setOnClickListener(buttonListener);
        button3.setOnClickListener(buttonListener);
        button4.setOnClickListener(buttonListener);
        button5.setOnClickListener(buttonListener);
        button6.setOnClickListener(buttonListener);
        button7.setOnClickListener(buttonListener);
        button8.setOnClickListener(buttonListener);
        button9.setOnClickListener(buttonListener);
        buttonhashtag.setOnClickListener(buttonListener);
        buttonstar.setOnClickListener(buttonListener);


        buttondelete.setOnClickListener(deleteClickListener);
        buttoncall.setOnClickListener(callClickListener);
        buttonhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        phoneNumberEditText = (EditText) findViewById(R.id.phonenumber);

    }


}
