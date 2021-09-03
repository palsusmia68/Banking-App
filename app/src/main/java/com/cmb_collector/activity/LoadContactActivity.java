package com.cmb_collector.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.cmb_collector.R;
import com.cmb_collector.adapter.MyContactAdapter;

import java.util.ArrayList;

public class LoadContactActivity extends AppCompatActivity {
    private ImageView img_back;
    private ArrayList<String> storeContactName = new ArrayList<>();
    private ArrayList<String> storeContactNumber = new ArrayList<>();
    private Cursor cursor;
    private String contactName, contactNumber;
    private ListView listContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_contact);

        img_back = findViewById(R.id.img_back);
        listContact = findViewById(R.id.listContact);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoadContactActivity.this, MobileRecharge.class)
                        .putExtra("title", getIntent().getStringExtra("title")));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }
        });


        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            storeContactName.add(contactName);
            storeContactNumber.add(contactNumber);
        }
        cursor.close();

        MyContactAdapter contactAdapter = new MyContactAdapter(LoadContactActivity.this, storeContactName, storeContactNumber);
        listContact.setAdapter(contactAdapter);


        listContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(LoadContactActivity.this, MobileRecharge.class)
                        .putExtra("title", getIntent().getStringExtra("title"))
                        .putExtra("number", storeContactNumber.get(position).replace(" ", "")));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoadContactActivity.this, MobileRecharge.class)
                .putExtra("title", getIntent().getStringExtra("title")));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }
}
