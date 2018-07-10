package com.example.abdul.messagesapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.*;

import java.util.*;

public class HassanCode extends AppCompatActivity {

    private final int REQUEST_CODE_PERMISSION_READ_SMS = 456;
    private EditText contactNumberEditText;
    private Button theProcessButton;
    private ListView conversationListView;
    private TextView textView;
    private String number = "03439555320";
    private String textViewConversation = "";

    private ArrayList<String> conversationArrayList;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        contactNumberEditText = findViewById(R.id.contactNumberEditText);
//        theProcessButton = findViewById(R.id.theProcessButton);
//        conversationListView = findViewById(R.id.conversationListView);
//        textView = findViewById(R.id.textViewConversation);
        conversationArrayList = new ArrayList<>();

        if (!checkPermission(Manifest.permission.READ_SMS)) {
            ActivityCompat.requestPermissions(HassanCode.this, new String[]{(Manifest.permission.READ_SMS)}, REQUEST_CODE_PERMISSION_READ_SMS);
        }

        //{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{{}{}{}{}{}{}{
        textView.setMovementMethod(new ScrollingMovementMethod());

        try {
            contactNumberEditText.setText(number);
            //=======================
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
            int indexBody = cursor.getColumnIndex("body");
            int indexAddress = cursor.getColumnIndex("address");

            try {
                if (number.charAt(0) == '+' && number.charAt(1) == '9' && number.charAt(2) == '2')
                    number = number.substring(3, number.length());
                if (number.charAt(0) == '0')
                    number = number.substring(1, number.length());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            if (indexBody < 0 || !cursor.moveToFirst()) {
                Toast.makeText(HassanCode.this, "There is no message to be shown!", Toast.LENGTH_SHORT).show();
                return;
            }

            String smsDate = "";
            String sms = "";

            try {
//                        conversationArrayList.clear();
                do {
                    if (cursor.getString(indexAddress).equals("+92" + number) || cursor.getString(indexAddress).equals("0" + number) || cursor.getString(indexAddress).equals(number)) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            String date = cursor.getString(cursor.getColumnIndex("date"));
                            Long timestamp = Long.parseLong(date);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(timestamp);
                            Date finalDate = calendar.getTime();
                            System.out.println(1900 + finalDate.getYear());
                            String time = finalDate.toString();
                            smsDate = time.substring(0, time.indexOf('G') - 1);
                            smsDate = smsDate.substring(0, 3) + " " + (1900 + finalDate.getYear()) + smsDate.substring(3, smsDate.length());
                        }
                        String str = cursor.getString(indexBody);
                        sms = str + "\n" + smsDate + " \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t From: " + contactNumberEditText.getText().toString();
                        textViewConversation += sms + "\n";
                        conversationArrayList.add(sms);
                    }
                } while (cursor.moveToNext());
                textView.setText(sms);
            } catch (Exception e) {
                Toast.makeText(HassanCode.this, "Exception: \n" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
//            finally {
//                        arrayAdapter.notifyDataSetChanged();
//                        arrayAdapter.notifyDataSetInvalidated();
//            }

            cursor.close();
        } catch (Exception e) {
            if (contactNumberEditText.getText().toString().length() == 0)
                Toast.makeText(HassanCode.this, "Please Enter a contact number first!", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(HassanCode.this, "Please Enter a correct number! " + contactNumberEditText.getText().toString() + " has no conversation.", Toast.LENGTH_LONG).show();
        }
        //{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{{}{}{}{}{}{}{
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, conversationArrayList);

        theProcessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HassanCode.this, "Don't shokologosis", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getMessagesFromInbox(String number) throws Exception{
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = cursor.getColumnIndex("body");
        int indexAddress = cursor.getColumnIndex("address");

        try {
            if (number.charAt(0) == '+' && number.charAt(1) == '9' && number.charAt(2) == '2')
                number = number.substring(3, number.length());
            if (number.charAt(0) == '0')
                number = number.substring(1, number.length());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (indexBody < 0 || !cursor.moveToFirst()) {
            return;
        }

        String smsDate = "";

        try {
            conversationArrayList.clear();
            do {
                if (cursor.getString(indexAddress).equals("+92" + number) || cursor.getString(indexAddress).equals("0" + number) || cursor.getString(indexAddress).equals(number)) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String date = cursor.getString(cursor.getColumnIndex("date"));
                        Long timestamp = Long.parseLong(date);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(timestamp);
                        Date finalDate = calendar.getTime();
                        System.out.println(1900 + finalDate.getYear());
                        String time = finalDate.toString();
                        smsDate = time.substring(0, time.indexOf('G') - 1);
                        smsDate = smsDate.substring(0, 3) + " " + (1900 + finalDate.getYear()) + smsDate.substring(3, smsDate.length());
                    }
                    String str = cursor.getString(indexBody);
                    conversationArrayList.add(str + "\n" + smsDate + " \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t From: " + contactNumberEditText.getText().toString());
                }
            } while (cursor.moveToNext());
        } catch (Exception e) {
            Toast.makeText(this, "Exception: \n" + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            arrayAdapter.notifyDataSetChanged();
            arrayAdapter.notifyDataSetInvalidated();
        }

        cursor.close();
    }

    private boolean checkPermission(String permission) {
        int checkPermission = ContextCompat.checkSelfPermission(this, permission);
        return checkPermission == PackageManager.PERMISSION_GRANTED;
    }

}
