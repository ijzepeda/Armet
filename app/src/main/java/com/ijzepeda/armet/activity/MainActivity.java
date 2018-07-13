package com.ijzepeda.armet.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.ijzepeda.armet.adapter.ServicesAdapter;
import com.ijzepeda.armet.model.DataSingleton;
import com.ijzepeda.armet.model.Day;
import com.ijzepeda.armet.model.Servicio;
import com.ijzepeda.armet.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.ijzepeda.armet.util.Constants.EXTRA_CLIENT_ID;
import static com.ijzepeda.armet.util.Constants.EXTRA_CLIENT_NAME;
import static com.ijzepeda.armet.util.Constants.EXTRA_TASK_ID;
import static com.ijzepeda.armet.util.Constants.REQUEST_SERVICE;
import static com.ijzepeda.armet.util.Constants.RESULT_TASK;
import static com.ijzepeda.armet.util.Constants.SERVICE_ID;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    Button logoutBtn;
    Context context;
    FirebaseUser user;
    Button newTaskBtn;
    Button addServiceBtn;
    Button addDayButton;

    DataSingleton dataSingleton;
    User currentUser;
    com.firebase.ui.auth.data.model.User fbUser;
    //DataSingleton singleton;
    ArrayList<String> taskList;
    ArrayList<String> serviceList;
    Day day;


    FirebaseApp app;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseStorage storage;
    DatabaseReference databaseReference;
    DatabaseReference databaseServiceReference;

    ArrayList<Servicio> serviciosTotales;
    ArrayList<com.ijzepeda.armet.model.Task> tasksTotales;
    ServicesAdapter servicesAdapter;
    RecyclerView servicesRecyclerView;
    LinearLayoutManager llm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        user = FirebaseAuth.getInstance().getCurrentUser();
        dataSingleton = DataSingleton.getInstance();

        initFirebase();
        setupUser();
        createDay();

//singleton.getInstance(context);
        initComponents();
        fetchServices();

    }

    public void initFirebase() {
        app = FirebaseApp.getInstance();
        database = FirebaseDatabase.getInstance(app);
        auth = FirebaseAuth.getInstance(app);
        storage = FirebaseStorage.getInstance(app);
        databaseReference = database.getReference("day");
        databaseServiceReference = database.getReference("service");
    }


    public void createDay() {
        day = new Day();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm"); //TODO so far will add todays date. but I might add a button to add manually the user
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_HH:mm"); //TODO  si dejo diagonales separa bonito en dia, pero para hacer un armado de excel creo que debo hacer un for mas
        String currentDateandTime = sdf.format(new Date());

        day.setDate(currentDateandTime);
        day.setUserId(currentUser.getId());
        //everything else will be added later on SAVE DAY
        //TODO el id de day, lo podria generar con FECHA+ID

    }

    public void setupUser() {
//        sincronizar con el user de firebase
        currentUser = new User();
        currentUser.setFirstName("Ivan");
        currentUser.setLastName("Zepeda");
        currentUser.setEmail("ijzepeda@hotmail.com");
        currentUser.setPhone("7221767190");
        currentUser.setId("1");
        currentUser.setPosition("dev");
        dataSingleton.setUser(currentUser);
    }

    public void initComponents() {

        taskList = new ArrayList<>();
        serviceList = new ArrayList<>();

        tasksTotales = new ArrayList<>();
        serviciosTotales = new ArrayList<>();
        servicesRecyclerView = findViewById(R.id.serviciosRecyclerView);
        llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        servicesRecyclerView.setLayoutManager(llm);
        servicesAdapter = new ServicesAdapter(context, serviciosTotales);
        servicesRecyclerView.setAdapter(servicesAdapter);

        //set adapter for tasks


        logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setText("Logout " + user.getDisplayName());
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(context)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                logoutBtn.setText("Logout ");
                                finish();  // ...
                            }
                        });

            }
        });

        addDayButton = findViewById(R.id.finishDaybtn);
        addDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyData();
            }
        });

        newTaskBtn = findViewById(R.id.newTaskBtn);
        addServiceBtn = findViewById(R.id.addServiceBtn);

        newTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, EditTaskActivity.class);
                intent.putExtra("user", "IvaNZepedA");

                startActivityForResult(intent, RESULT_TASK);
            }
        });
        addServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(context, AddClientActivity.class));

                Intent productIntent = new Intent(context, AddServiceActivity.class);
                productIntent.putExtra(EXTRA_CLIENT_NAME, "CORAL & MARINA"); //todo borrar
                productIntent.putExtra(EXTRA_CLIENT_ID, "123");
                startActivityForResult(productIntent, REQUEST_SERVICE);

            }
        });
    }


    public void verifyData() {
        //checar que no esten nulos

        saveDay();

    }


    public void saveDay() {
//        databaseReference.push().setValue(day); UUID
        databaseReference.child(day.getDate()+"_"+day.getUserId()).setValue(day);
        Toast.makeText(context, "Day saved. Clear and close. Salvar el dia, y presionar de nuevo para cerrar", Toast.LENGTH_SHORT).show();
        //todo, so far it will keep updating the day
    }

    public void fetchServices() {
        //Need to clear list before fetching
serviciosTotales=new ArrayList<>();
//fetch services from FB add them to arraylist
        for (String serviceId : serviceList) {
            databaseServiceReference.child(serviceId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.e(TAG, "fetch services onDataChange: " + dataSnapshot.toString());
                    Servicio servicioTemp = dataSnapshot.getValue(Servicio.class);
                    serviciosTotales.add(servicioTemp);
                    servicesAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }

    public void fetchTasks() {
        //todo trabajar aqui para mostrar los elementos en el adapter
        tasksTotales=new ArrayList<>();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_TASK) {
            switch (resultCode) {
                case Activity.RESULT_OK:
//                    data.getStringExtra(PRODUCT_ID)
                    String taskid = data.getStringExtra(EXTRA_TASK_ID);
//                    taskList.add(taskid);
                    day.setTask(taskid);
                    fetchTasks();

                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        }

        if (requestCode == REQUEST_SERVICE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    // Servicio service = singleton.getService(data.getStringExtra(SERVICE_ID)); // no hace falta de singleton. ya lo leera de servidor
//ad data.getStringExtra(SERVICE_ID) to list, and fill the adapter with the objects from that list

                    String serviceId = data.getStringExtra(SERVICE_ID);
                    serviceList.add(serviceId); //to store on firebase DayObject
                    day.setService(serviceId);

                    fetchServices();

                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(context, "Something happened", Toast.LENGTH_SHORT).show();
                    break;

            }
        }

    }
}
