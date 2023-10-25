package com.example.danhba.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.danhba.Model.Contact;
import com.example.danhba.R;
import java.sql.Array;
import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact>{
    private Context context;
    private int resource;
    private List<Contact> arrayContact;

    public ContactAdapter(Context context, int resource, List<Contact> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrayContact = objects;
    }
        @NonNull
        @Override
    public View getView(int position, View converView, ViewGroup parent){
        ViewHolder viewHolder;
        if(converView == null){
            viewHolder = new ViewHolder();
            converView = LayoutInflater.from(context).inflate(R.layout.item_contact_listview,parent,false);
            viewHolder.imgAvatar = (ImageView) converView.findViewById(R.id.img_avatar);
            viewHolder.tvName = (TextView) converView.findViewById(R.id.tv_name);
            viewHolder.tvNumber = (TextView) converView.findViewById(R.id.tv_number);

            converView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) converView.getTag();
        }

        Contact contact = arrayContact.get(position);

        viewHolder.tvName.setText(contact.getmName());
        viewHolder.tvNumber.setText(contact.getmNumber());
        if (contact.isMale()){
            viewHolder.imgAvatar.setBackgroundResource(R.drawable.nam);
        }else {
            viewHolder.imgAvatar.setBackgroundResource(R.drawable.nu);
        }

        return converView;
    }
    public class ViewHolder{
        ImageView imgAvatar;
        TextView tvName;
        TextView tvNumber;
    }
}
