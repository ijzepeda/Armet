package com.ijzepeda.armet.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.ijzepeda.armet.R;
import com.ijzepeda.armet.model.DataSingleton;
import com.ijzepeda.armet.model.DataSingleton;
import com.ijzepeda.armet.model.Day;
import com.ijzepeda.armet.model.User;
import com.ijzepeda.armet.util.Constants;

import java.util.ArrayList;
import java.util.Date;

import static com.ijzepeda.armet.util.Constants.EXTRA_TASK_ID;
import static com.ijzepeda.armet.util.Constants.PRODUCT_ID;
import static com.ijzepeda.armet.util.Constants.RESULT_TASK;

public class MainActivity extends Activity {

    Button logoutBtn;
    Context context;
    FirebaseUser user;
    Button newTaskBtn;
    Button addClientBtn;
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

    }

    public void initFirebase(){
        app=FirebaseApp.getInstance();
        database=FirebaseDatabase.getInstance(app);
        auth=FirebaseAuth.getInstance(app);
        storage=FirebaseStorage.getInstance(app);
        databaseReference=database.getReference("day");
    }



    public void createDay(){
        day=new Day();
        taskList=new ArrayList<>();
//        day.setTask();
        String date ="";
        day.setDate(date);
        day.setUserId(currentUser.getId());

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

        addDayButton=findViewById(R.id.finishDaybtn);
        addDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyData();
            }
        });

        newTaskBtn = findViewById(R.id.newTaskBtn);
        addClientBtn = findViewById(R.id.addClientBtn);

        newTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, EditTaskActivity.class);
                intent.putExtra("user", "IvaNZepedA");

                startActivityForResult(intent, RESULT_TASK);
            }
        });
        addClientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AddClientActivity.class));
            }
        });
    }


    public void verifyData(){
        //checar que no esten nulos

saveDay();

    }


    public void saveDay(){
        databaseReference.push().setValue(day);
        Toast.makeText(context, "Day saved. Clear and close", Toast.LENGTH_SHORT).show();
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

                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        }
    }
}
