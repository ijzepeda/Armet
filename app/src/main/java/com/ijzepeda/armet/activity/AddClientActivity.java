package com.ijzepeda.armet.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ijzepeda.armet.R;
import com.ijzepeda.armet.adapter.ServicesAdapter;
import com.ijzepeda.armet.model.DataSingleton;
import com.ijzepeda.armet.model.Servicio;

import java.util.ArrayList;

import static com.ijzepeda.armet.util.Constants.SERVICE_ID;

public class AddClientActivity extends BaseActivity {
    private static final String TAG = "AddClientActivity";
    Context context;
    DataSingleton singleton;
    Button addNewServiceBtn;

    ImageButton scanBtn;
    ImageButton addServiceBtn;
    ImageButton searchBtn;

    TextView clientTextView;

    ArrayList<Servicio> serviciosTotales;
    ArrayList<Servicio> serviciosLocales;
    private  static int REQUEST_CLIENT=222;

    ServicesAdapter servicesAdapter;
    RecyclerView servicesRecyclerView;
    LinearLayoutManager llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
        context=this;
        //loadValues() >
        //getData();
        initComponents();
    }

    public void initComponents(){
        singleton = DataSingleton.getInstance();
        serviciosTotales =new ArrayList<>();

        scanBtn=findViewById(R.id.photoButton);
        addServiceBtn =findViewById(R.id.addServiceBtn);
        searchBtn=findViewById(R.id.searchButton);
        clientTextView=findViewById(R.id.clientTextView);

        servicesRecyclerView=findViewById(R.id.servicesRecyclerView);
        llm=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        servicesRecyclerView.setLayoutManager(llm);
        servicesAdapter=new ServicesAdapter(context, serviciosTotales);
        servicesRecyclerView.setAdapter(servicesAdapter);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBarcode();
            }
        });

        addNewServiceBtn =findViewById(R.id.addNewServiceBtn);
        addNewServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productIntent = new Intent(context, AddServiceActivity.class);
                startActivityForResult(productIntent, REQUEST_CLIENT);

                //startActivity(new Intent(context,AddServiceActivity.class));
            }
        });

    }
    @Override
    public void makeUseOfBarcode() {
        super.makeUseOfBarcode();
        //search for this serial number in clients, if exists load everything into the screen
        clientTextView.setText(getBarcode());

    }
    public void getData() {

        //todo, after creating a service, when creating a new service the previous localQty keeps prevs values

         serviciosLocales= new ArrayList<>();
        serviciosTotales = new ArrayList<>();
        serviciosTotales.addAll(singleton.getServicesList());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CLIENT){
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Servicio service = singleton.getService(data.getStringExtra(SERVICE_ID));
                    serviciosTotales.add(service);
                    servicesAdapter.notifyDataSetChanged();

                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(context, "Something happened", Toast.LENGTH_SHORT).show();
                    break;

            }
        }



    }
}
