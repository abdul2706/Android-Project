package com.example.abdul.messagesapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final int READ_SMS_PERMISSION_CODE = 1;
    private ListView inboxListView;
    private ArrayAdapter<String> inboxAdapter;
    private ArrayList<String> inboxList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inboxListView = findViewById(R.id.listView);
        inboxList = new ArrayList<>();
        inboxAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, inboxList);
        inboxListView.setAdapter(inboxAdapter);

        if (!checkPermission(Manifest.permission.READ_SMS)) {
//            Toast.makeText(this, "Getting Permission", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, READ_SMS_PERMISSION_CODE);
        } else {
//            Toast.makeText(this, "Already Granted Permission", Toast.LENGTH_SHORT).show();
        }

        try {
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
            int addressIndex = cursor.getColumnIndex("address");
            int bodyIndex = cursor.getColumnIndex("body");

            Toast.makeText(this, "address:" + addressIndex + "\nbody:" + bodyIndex, Toast.LENGTH_SHORT).show();

            while (cursor.moveToNext()) {
                String address = cursor.getString(addressIndex);
                String body = cursor.getString(bodyIndex);
                inboxList.add(address + " -> " + body);
            }

            inboxAdapter.notifyDataSetChanged();
            inboxAdapter.notifyDataSetInvalidated();
            cursor.close();
        } catch (NullPointerException e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermission(String permission) {
        int checkPermission = ContextCompat.checkSelfPermission(this, permission);
        return checkPermission == PackageManager.PERMISSION_GRANTED;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case READ_SMS_PERMISSION_CODE:
//                if ((grantResults.length > 0) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, "Permission is Granted", Toast.LENGTH_SHORT).show();
//                }
//                break;
//        }
//    }

}
