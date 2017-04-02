package ro.pub.cs.systems.eim.lab04.contactsmanager;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsManagerActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, emailEditText, addressEditText,
        imEditText, websiteEditText, jobEditText, companyEditText;

    private Button showMoreButton, showLessButton, saveButton, cancelButton;
    private LinearLayout more_info_container;


    public static final int CONTACTS_MANAGER_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_manager);

        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        phoneEditText = (EditText) findViewById(R.id.phone_edit_text);
        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        addressEditText = (EditText) findViewById(R.id.address_edit_text);
        imEditText = (EditText) findViewById(R.id.im_edit_text);
        websiteEditText = (EditText) findViewById(R.id.website_edit_text);
        jobEditText = (EditText) findViewById(R.id.job_edit_text);
        companyEditText = (EditText) findViewById(R.id.company_edit_text);

        showLessButton = (Button) findViewById(R.id.show_less_button);
        showMoreButton = (Button) findViewById(R.id.show_more_button);
        saveButton = (Button) findViewById(R.id.save_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);

        more_info_container = (LinearLayout) findViewById(R.id.more_info_container);
        setListeners();

        Intent intent = getIntent();
        if (intent != null) {
            String phone = intent.getStringExtra("ro.pub.cs.systems.eim.lab04.contactsmanager.PHONE_NUMBER_KEY");
            if (phone != null) {
                phoneEditText.setText(phone);
            } else {
                Toast.makeText(this, getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        }

    }

    public void setListeners() {
        showMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreButton.setVisibility(View.INVISIBLE);
                showLessButton.setVisibility(View.VISIBLE);
                more_info_container.setVisibility(View.VISIBLE);
            }
        });

        showLessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreButton.setVisibility(View.VISIBLE);
                showLessButton.setVisibility(View.INVISIBLE);
                more_info_container.setVisibility(View.INVISIBLE);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED, new Intent());
                finish();
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String address = addressEditText.getText().toString();
                String job = jobEditText.getText().toString();
                String company = companyEditText.getText().toString();
                String website = websiteEditText.getText().toString();
                String im = imEditText.getText().toString();

                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                if (!name.isEmpty()) {
                    intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                }

                if (!phone.isEmpty()) {
                    intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
                }

                if (!email.isEmpty()) {
                    intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
                }

                if (!address.isEmpty()) {
                    intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
                }

                if (!job.isEmpty()) {
                    intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, job);
                }

                if (!company.isEmpty()) {
                    intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);
                }

                ArrayList<ContentValues> contactData = new ArrayList<ContentValues>();
                if (!website.isEmpty()) {
                    ContentValues websiteRow = new ContentValues();
                    websiteRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
                    websiteRow.put(ContactsContract.CommonDataKinds.Website.URL, website);
                    contactData.add(websiteRow);
                }
                if (!im.isEmpty()) {
                    ContentValues imRow = new ContentValues();
                    imRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
                    imRow.put(ContactsContract.CommonDataKinds.Im.DATA, im);
                    contactData.add(imRow);
                }

                intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);
//                startActivity(intent);
                startActivityForResult(intent, CONTACTS_MANAGER_REQUEST_CODE);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch(requestCode) {
            case CONTACTS_MANAGER_REQUEST_CODE:
                setResult(resultCode, new Intent());
                finish();
                break;
        }
    }

}
