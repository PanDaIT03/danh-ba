package com.example.danhba;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.danhba.Adapter.ContactAdapter;
import com.example.danhba.Database.ContactDatabase;
import com.example.danhba.Model.Contact;
import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Contact> arrayContact;
    private ContactAdapter adapter;
    private EditText edtName;
    private EditText edtNumber;
    private RadioButton rbtnMale;
    private RadioButton rbtnFemale;
    private Button btnAddContact;
    private ListView lvContact;
    private ContactDatabase contactDatabase;
    private String name, numberPhone;
    private int male;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setWidget();

        arrayContact = new ArrayList<>();
        adapter = new ContactAdapter(this, R.layout.item_contact_listview, arrayContact);
        lvContact.setAdapter(adapter);

        checkAndRequestPermissions();
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialogConfirm(position);
            }
        });

        //Create database
        contactDatabase = new ContactDatabase(this, "ContactManagement.sqlite", null, 1);

        //Delete table
        String sqlDrop = "DROP TABLE CONTACT";
        contactDatabase.QueryData(sqlDrop);

        //Create table Contact
        String sql = "CREATE TABLE IF NOT EXISTS Contact(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Ten NVARCHAR(100), " +
                "Gender BIT, " +
                "SDT VARCHAR(15)" +
                ")";
        contactDatabase.QueryData(sql);

        //Insert Data
        initDataContact();
        //Load Data
        loadDataContact();

        btnAddContact.setOnClickListener(v -> {
            getData();
            Log.d("data", name + "\n" + numberPhone);
            insertContact();
            loadDataContact();
        });

        registerContextMenu();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int position = info.position;
        String name = adapter.getItem(position).getmName(),
                numberPhone = adapter.getItem(position).getmNumber();
        int male = adapter.getItem(position).getIsMale(),
                id = adapter.getItem(position).getId();

        switch (item.getItemId()) {
            case R.id.update:
                updateData();
                return true;
                break;
            case R.id.delete:
                return true;
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return super.onContextItemSelected(item);
    }

    private void getData() {
        name = edtName.getText().toString();
        numberPhone = edtNumber.getText().toString();

        if (rbtnMale.isChecked()) {
            male = 1;
        } else {
            male = 0;
        }
    }

    private void initDataContact() {
        String sqlInsert1 = "INSERT INTO Contact VALUES(NULL, 'Tâm ngu', 1, '0928591')";
        String sqlInsert2 = "INSERT INTO Contact VALUES(NULL, 'Tu ngâm', 0, '123')";
        String sqlInsert3 = "INSERT INTO Contact VALUES(NULL, 'Tu', 1, '9912587')";

        contactDatabase.QueryData(sqlInsert1);
        contactDatabase.QueryData(sqlInsert2);
        contactDatabase.QueryData(sqlInsert3);
    }

    private void loadDataContact() {
        String sqlSelect = "SELECT * FROM Contact";
        Cursor contactData = contactDatabase.QueryGetData(sqlSelect);

        arrayContact.clear();
        while (contactData.moveToNext()) {
            int ID = contactData.getInt(0);
            String strName = contactData.getString(1);
            int gender = contactData.getInt(2);
            String strSDT = contactData.getString(3);

            Log.d("data", strName);
            arrayContact.add(new Contact(ID, gender, strName, strSDT));
        }

        adapter.notifyDataSetChanged();
        lvContact.setAdapter(adapter);
    }

    private void insertContact() {
        String sqlInsert = "INSERT INTO Contact VALUES(NULL, '" + name + "',  " + male + " , '" + numberPhone + "')";
        contactDatabase.QueryData(sqlInsert);
    }

    private void updateData() {

    }

    private void registerContextMenu() {
        registerForContextMenu(lvContact);
    }

    private void checkAndRequestPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.CALL_PHONE,
                android.Manifest.permission.SEND_SMS
        };
        List<String> listPermissionNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionNeeded.add(permission);
            }
        }
        if (!listPermissionNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]), 1);
        }
    }

    public void setWidget() {
        edtName = (EditText) findViewById(R.id.edt_name);
        edtNumber = (EditText) findViewById(R.id.edt_number);
        rbtnMale = (RadioButton) findViewById(R.id.rbtn_male);
        rbtnFemale = (RadioButton) findViewById(R.id.rbtn_female);
        btnAddContact = (Button) findViewById(R.id.btn_add_contract);
        lvContact = (ListView) findViewById(R.id.lv_contract);
    }

    public void addContact(View view) {
        if (view.getId() == R.id.btn_add_contract) {
            String name = edtName.getText().toString().trim();
            String number = edtNumber.getText().toString().trim();

            int isMale = 1;
            if (rbtnMale.isChecked()) {
                isMale = 1;
            } else {
                isMale = 0;
            }
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(number)) {
                Toast.makeText(this, "Hãy nhập SĐT hoặc Tên", Toast.LENGTH_SHORT).show();
            } else {
                Contact contact = new Contact(isMale, name, number);
                arrayContact.add(contact);
            }

            adapter.notifyDataSetChanged();
            lvContact.setAdapter(adapter);
        }
    }

    public void showDialogConfirm(int position) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_layout);
        Button btnCall = (Button) dialog.findViewById(R.id.btn_call);
        Button btnSend = (Button) dialog.findViewById(R.id.btn_send);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentCall(position);
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentSend(position);
            }
        });
        dialog.show();
    }

    private void intentSend(int position) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + arrayContact.get(position).getmNumber()));
        startActivity(intent);
    }

    private void intentCall(int position) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + arrayContact.get(position).getmNumber()));
        startActivity(intent);
    }

}

