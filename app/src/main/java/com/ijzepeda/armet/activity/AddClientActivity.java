package com.ijzepeda.armet.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ijzepeda.armet.R;

public class AddClientActivity extends AppCompatActivity {
    private static final String TAG = "AddClientActivity";
    Context context;
    Button addServiceBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
        context=this;
        initComponents();
    }

    public void initComponents(){
        addServiceBtn=findViewById(R.id.addServiceBtn);
        addServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,AddServiceActivity.class));
            }
        });

    }
}
