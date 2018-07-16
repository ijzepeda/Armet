package com.ijzepeda.armet.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

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
import com.ijzepeda.armet.adapter.TasksAdapter;
import com.ijzepeda.armet.model.DataSingleton;
import com.ijzepeda.armet.model.Day;
import com.ijzepeda.armet.model.ExcelFile;
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

    ImageButton addTaskBigButton;
    ImageButton addServiceBigButton;

    LinearLayout emptyTaskLayout;
    LinearLayout taskLayout;
    LinearLayout emptyServiceLayout;
    LinearLayout serviceLayout;


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
    DatabaseReference databaseTaskReference;

    //Services List
    ArrayList<Servicio> serviciosTotales;
    ServicesAdapter servicesAdapter;
    RecyclerView servicesRecyclerView;
    LinearLayoutManager llm;

    //Tasks List
    ArrayList<com.ijzepeda.armet.model.Task> tasksTotales;
    TasksAdapter taskAdapter;
    RecyclerView tasksRecyclerView;
    LinearLayoutManager llm2;

    private String correo = "ijzepeda@hotmail.com";
    String currentDateandTime;

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataSingleton=  DataSingleton.loadSingleton(context);
        day= dataSingleton.getDay();
        if(day==null) {
            createDay();
            Log.e(TAG, "onCreate: createing new day as singleton was empty" );
        }
//        else{ //funciona pero a medias
//            taskList.addAll(day.getTasksIds());
//             tasksTotales.addAll(day.getTasks());
//            taskAdapter.notifyDataSetChanged();
//            serviceList.addAll(day.getClientServiceIds());
//             serviciosTotales.addAll(day.getServices());
//            servicesAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) {
            throw new Error("Can't find tool bar, did you forget to add it in Activity layout file?");
        }
        if (savedInstanceState != null) {
            day = savedInstanceState.getParcelable("day");
        }
        setActionBar(toolbar);

        context = this;
        user = FirebaseAuth.getInstance().getCurrentUser();
        //dataSingleton = DataSingleton.getInstance();
        dataSingleton=  DataSingleton.loadSingleton(context);

        askPermissions();
        initFirebase();
        setupUser();
        initComponents();
        fetchServices();
        createDate();
        //Log.e(TAG, "onCreate dataSingleton.getDay().getDate(): "+dataSingleton.getDay().getDate() );

        day= dataSingleton.getDay();

        if(day==null || !day.getDate().equals(currentDateandTime)) {
            dataSingleton.clearSingleton(context);//just to be sure: delete data on singleton
            createDay();
            Log.e(TAG, "onCreate: creating new day as singleton was empty" );
        } else{

            taskList.addAll(day.getTasksIds());
            if(taskList.size()>0) {
                fetchTasks();
                taskAdapter.notifyDataSetChanged();

                taskLayout.setVisibility(View.VISIBLE);
                emptyTaskLayout.setVisibility(View.GONE);
            }

            serviceList.addAll(day.getClientServiceIds());
            if(serviceList.size()>0) {
                fetchServices();
                servicesAdapter.notifyDataSetChanged();

                emptyServiceLayout.setVisibility(View.GONE);
                serviceLayout.setVisibility(View.VISIBLE);
            }
        }

//singleton.getInstance(context);

        verifyDate();   //



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
//                Toast.makeText(context, "LOGOUT", Toast.LENGTH_SHORT).show();
                AuthUI.getInstance()
                        .signOut(context)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                logoutBtn.setText("Cerrar sesion ");
                                finish();
                                // ...
                            }
                        });
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        outState.putParcelableArrayList("day", day);
        outState.putParcelable("day",day);
        Log.e(TAG, "onSaveInstanceState: "+day.getUserName() );
        servicesAdapter.notifyDataSetChanged();
        taskAdapter.notifyDataSetChanged();
        super.onSaveInstanceState(outState);
    }

    public void initFirebase() {
        app = FirebaseApp.getInstance();
        database = FirebaseDatabase.getInstance(app);
        auth = FirebaseAuth.getInstance(app);
        storage = FirebaseStorage.getInstance(app);
        databaseReference = database.getReference("day");
        databaseServiceReference = database.getReference("service");
        databaseTaskReference = database.getReference("task");

    }

public void createDate(){

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm"); //TODO so far will add todays date. but I might add a button to add manually the user
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd"); //TODO -HHmm si dejo diagonales separa bonito en dia, pero para hacer un armado de excel creo que debo hacer un for mas
    currentDateandTime = sdf.format(new Date());
    Log.e(TAG, "todays date is: "+currentDateandTime );

}

    public void createDay() {
        day = new Day();
        day.setDate(currentDateandTime);
        day.setUserId(currentUser.getId());
        day.setUserName(user.getDisplayName());
        //everything else will be added later on SAVE DAY
        //TODO el id de day, lo podria generar con FECHA+ID

    }

    /***
     * if it is a different date, it will delete previous saved data.
     * If it is the same day, then it will load everything that hs been done by the same user
     *
     * compare:
     * user
     * date
     *
     */
    public void verifyDate(){

       // currentDateandTime dataSingleton.getDay().getDate();

    }
    public void updateDay(){
        dataSingleton.updateDay(day);
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
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        llm2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        tasksRecyclerView.setLayoutManager(llm2);
        taskAdapter = new TasksAdapter(context, tasksTotales);
        tasksRecyclerView.setAdapter(taskAdapter);
        taskAdapter.notifyDataSetChanged();


        emptyServiceLayout = findViewById(R.id.emptyServiceLayout);
        emptyTaskLayout = findViewById(R.id.emptyTaskLayout);
        taskLayout = findViewById(R.id.taskLayout);
        serviceLayout = findViewById(R.id.serviceLayout);

        taskLayout.setVisibility(View.GONE);
        serviceLayout.setVisibility(View.GONE);


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
                                finish();
                                // ...
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
                addTask();

            }
        });
        addServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addService();
            }
        });


        addTaskBigButton = findViewById(R.id.addTaskBigButton);
        addServiceBigButton = findViewById(R.id.addServiceBigButton);

        addTaskBigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });

        addServiceBigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addService();

            }
        });


    }

    public void addService() {

        Intent productIntent = new Intent(context, AddServiceActivity.class);
        productIntent.putExtra(EXTRA_CLIENT_NAME, "CORAL & MARINA"); //todo borrar
        productIntent.putExtra(EXTRA_CLIENT_ID, "123");
        startActivityForResult(productIntent, REQUEST_SERVICE);
    }

    public void addTask() {
        Intent intent = new Intent(context, EditTaskActivity.class);
        intent.putExtra("user", "IvaNZepedA");

        startActivityForResult(intent, RESULT_TASK);
    }


    public void verifyData() {
        //checar que no esten nulos

        saveDay();

    }


    public void saveDay() {
//        databaseReference.push().setValue(day); UUID

        //add objects on day, before will add them on firebase. else,
        day.setServices(serviciosTotales);
        day.setTasks(tasksTotales);

        databaseReference.child(day.getDate() + "_" + day.getUserId()).setValue(day);
        Toast.makeText(context, "Creando archivo para enviar por correo.", Toast.LENGTH_LONG).show();
        //todo, so far it will keep updating the day
askPermissions();
       createExcel();

    }

    public void createExcel() {

        Runnable task = new Runnable() {
            public void run() {
                ExcelFile excelFile = new ExcelFile(day);
                excelFile.setName("Armet-" + user.getDisplayName() + currentDateandTime);
                excelFile.setEmail(correo);
                excelFile.setContext(context);
                excelFile.exportToExcel(day);
            }
        };
        task.run();

    }

private static final int WRITE_PERMISSION_CODE=1;
    public void askPermissions(){

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.e(TAG, "askPermissions: 2" );
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_CODE);

            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_PERMISSION_CODE);
            }
        }else{

        }



    }

    public void fetchServices() {
        //Need to clear list before fetching
//serviciosTotales=new ArrayList<>();
//fetch services from FB add them to arraylist
        serviciosTotales.clear();
        for (String serviceId : serviceList) {
            databaseServiceReference.child(serviceId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
//        tasksTotales=new ArrayList<>();
        tasksTotales.clear();
        for (String taskId : taskList) {
            databaseTaskReference.child(taskId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    com.ijzepeda.armet.model.Task taskTemp = dataSnapshot.getValue(com.ijzepeda.armet.model.Task.class);
                    tasksTotales.add(taskTemp);
                    taskAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_TASK) {
            switch (resultCode) {
                case Activity.RESULT_OK:
//                    data.getStringExtra(PRODUCT_ID)


                    //hide Button
                    taskLayout.setVisibility(View.VISIBLE);
                    emptyTaskLayout.setVisibility(View.GONE);


                    String taskid = data.getStringExtra(EXTRA_TASK_ID);

                    taskList.add(taskid);
                    day.setTask(taskid);
                    fetchTasks();

                    dataSingleton.updateDay(day);
                    dataSingleton.update(context);

                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        }

        if (requestCode == REQUEST_SERVICE) {
            switch (resultCode) {
                case Activity.RESULT_OK:

                    emptyServiceLayout.setVisibility(View.GONE);
                    serviceLayout.setVisibility(View.VISIBLE);


                    // Servicio service = singleton.getService(data.getStringExtra(SERVICE_ID)); // no hace falta de singleton. ya lo leera de servidor
//ad data.getStringExtra(SERVICE_ID) to list, and fill the adapter with the objects from that list
                    // serviciosTotales=new ArrayList<>();
                    String serviceId = data.getStringExtra(SERVICE_ID);
                    serviceList.add(serviceId); //to store on firebase DayObject
                    day.setService(serviceId);

                    fetchServices();
                    dataSingleton.updateDay(day);
                    dataSingleton.update(context);

                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(context, "Algo paso", Toast.LENGTH_SHORT).show();
                    break;

            }
        }

    }
}
