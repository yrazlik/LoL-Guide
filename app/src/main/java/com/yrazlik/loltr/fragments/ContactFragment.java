package com.yrazlik.loltr.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.yrazlik.loltr.R;

/**
 * Created by yrazlik on 1/6/15.
 */
public class ContactFragment extends Fragment{

    private Button send;
    private EditText message;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TAGGG", "ContactFragmentOnCreateView");
        View v = inflater.inflate(R.layout.fragment_contact, container, false);
        send = (Button)v.findViewById(R.id.buttonContact);
        message = (EditText)v.findViewById(R.id.edittextContactBox);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = message.getText().toString();
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL, new String[] { "loltrdestek@gmail.com" });
                i.putExtra(Intent.EXTRA_TEXT, s);
                startActivity(i);
            }
        });

        return v;
    }
}
