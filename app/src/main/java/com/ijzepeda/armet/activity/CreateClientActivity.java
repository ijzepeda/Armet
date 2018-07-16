package com.ijzepeda.armet.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.ijzepeda.armet.R;
import com.ijzepeda.armet.model.Client;
import com.ijzepeda.armet.model.DataSingleton;
import com.ijzepeda.armet.model.User;

import static com.ijzepeda.armet.util.Constants.CLIENT_ID;
import static com.ijzepeda.armet.util.Constants.EXTRA_TASK_ID;
import static com.ijzepeda.armet.util.Constants.SERVICE_ID;

public class CreateClientActivity extends AppCompatActivity {
    EditText clientId;
    EditText clientName;
    EditText clientAddress;
    EditText clientTelephone;
    EditText clientContactName;
    Button addClientBtn;

    boolean editingClient;
    Client client;

    Context context;

    FirebaseApp app;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseStorage storage;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    DataSingleton singleton;
    User user;
    com.firebase.ui.auth.data.model.User fbUser;

    private static final String TAG = "CreateClientActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_client);
        context = this;
        singleton=DataSingleton.getInstance();
        initFirebase();
        initComponents();
    }

    public void initComponents() {
        clientId = findViewById(R.id.clientTextView);
        clientName = findViewById(R.id.clientNameTextView);
        clientAddress = findViewById(R.id.clientAddressTextView);
        clientTelephone = findViewById(R.id.clientPhoneTextView);
        clientContactName = findViewById(R.id.clientContactNameTextView);
        addClientBtn = findViewById(R.id.addClientBtn);
        addClientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyData();
            }
        });
    }

    public void initFirebase(){
        app=FirebaseApp.getInstance();
        database=FirebaseDatabase.getInstance(app);
        auth=FirebaseAuth.getInstance(app);
        storage=FirebaseStorage.getInstance(app);
        databaseReference=database.getReference("client");


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void verifyData() {
        boolean noError = true;
        EditText[] fields = {clientId, clientName, clientAddress, clientTelephone};
        for (EditText view : fields) {
            if (view.getText().toString().equals("")) {
                view.setError("No dejar vacio");
                noError = false;
            }

        }
        if (noError) {
            createClient();
//            if(editingClient)
//                updateClient();
//            else
            saveClient();
        }
    }

    public void createClient() {

        client = new Client();
        client.setId(clientId.getText().toString());
        client.setName(clientName.getText().toString());
        client.setAddress(clientAddress.getText().toString());
        client.setContactName(clientContactName.getText().toString());
        client.setPhone(clientTelephone.getText().toString());
//        client.se ;
    }

    public void saveClient() {


singleton.setClient(client);


        //if(editingService) {
        DatabaseReference mref = databaseReference.child(client.getId());
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && !editingClient) {
                    Toast.makeText(context, "Ya existe ese numero de cliente", Toast.LENGTH_LONG).show();
                } else {

                    databaseReference.child(client.getId()).setValue(client);

                    Toast.makeText(context, "Servicio creado", Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(CLIENT_ID, client.getId());
                    setResult(Activity.RESULT_OK, returnIntent);
                    singleton.update(context);

                    finish();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        }else{
//            databaseServiceRef.pus
//        }






    }

}
