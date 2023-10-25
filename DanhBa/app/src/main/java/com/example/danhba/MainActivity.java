package com.example.danhba;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;
import com.example.danhba.Adapter.ContactAdapter;
import com.example.danhba.Model.Contact;
import com.google.common.base.MoreObjects;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Contact> arrayContact;;
    private ContactAdapter adapter;
    private EditText edtName;
    private EditText edtNumber;
    private RadioButton rbtnMale;
    private RadioButton rbtnFemale;
    private Button btnAddContact;
    private ListView lvContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setWidget();
        arrayContact = new ArrayList<>();
        adapter = new ContactAdapter(this,R.layout.item_contact_listview,arrayContact);
        lvContact = (ListView) findViewById(R.id.lv_contract);
        checkAndRequestPermissions();
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialogConfirm(position);
            }
        });
    }

    private void checkAndRequestPermissions(){
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
              ActivityCompat.requestPermissions(this, listPermissionNeeded.toArray(new String[ listPermissionNeeded.size()]),1);
          }
    }
    public void setWidget(){
        edtName = (EditText)  findViewById(R.id.edt_name);
        edtNumber = (EditText)  findViewById(R.id.edt_number);
        rbtnMale = (RadioButton) findViewById(R.id.rbtn_male);
        rbtnFemale = (RadioButton) findViewById(R.id.rbtn_female);
        btnAddContact = (Button) findViewById(R.id.btn_add_contract);
        lvContact = (ListView) findViewById(R.id.lv_contract);
    }
    public void addContact(View view){
        if(view.getId()==R.id.btn_add_contract){
           String name = edtName.getText().toString().trim();
           String number = edtNumber.getText().toString().trim();
           boolean isMale = true;
           if(rbtnMale.isChecked()){
               isMale = true;
           }else{
               isMale = false;
           }
           if(TextUtils.isEmpty(name) || TextUtils.isEmpty(number)){
                Toast.makeText(this,"Hãy nhập SĐT hoặc Tên", Toast.LENGTH_SHORT).show();
           }else{
               Contact contact = new Contact(isMale,name,number);
               arrayContact.add(contact);
            }
            adapter.notifyDataSetChanged();
            lvContact.setAdapter(adapter);
        }
    }

    public void showDialogConfirm(int position){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_layout);
        Button btnCall = (Button) dialog.findViewById(R.id.btn_call);
        Button btnSend = (Button) dialog.findViewById(R.id.btn_send);

        btnCall.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                intentCall(position);
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                intentSend(position);
            }
            });
        dialog.show();
    }

    private void intentSend(int position) {
        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("sms:"+arrayContact.get(position).getmNumber()));
        startActivity(intent);
    }

    private void intentCall(int position) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+arrayContact.get(position).getmNumber()));
        startActivity(intent);
    }

}

