package com.ijzepeda.armet.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.ijzepeda.armet.R;
import com.ijzepeda.armet.model.DataSingleton;
import com.ijzepeda.armet.model.Task;
import com.ijzepeda.armet.model.User;

public class EditTaskActivity extends AppCompatActivity {
Context context;

EditText startTimeTextView;
EditText endTimeTextView;
EditText placeTextView;
EditText locationTextView;
EditText actionTextView;
EditText tec1TextView;
EditText tec2TextView;
EditText tec3TextView;
Button doneBtn;

Task task;

FirebaseApp app;
FirebaseDatabase database;
FirebaseAuth auth;
FirebaseStorage storage;
DatabaseReference databaseReference;

DataSingleton singleton;
User user;
com.firebase.ui.auth.data.model.User fbUser;


//loading files if editing
    boolean editingTask=false;
    String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        context = this;
        singleton=DataSingleton.getInstance();
        initFirebase();
        getUser();
        initComponents();

    }

    public void getUser(){
        user=singleton.getUser();
    }


    public void initFirebase(){
        app=FirebaseApp.getInstance();
        database=FirebaseDatabase.getInstance(app);
        auth=FirebaseAuth.getInstance(app);
        storage=FirebaseStorage.getInstance(app);
        databaseReference=database.getReference("task");
    }


    public void initComponents(){

         startTimeTextView=findViewById(R.id.startTimeText);
         endTimeTextView=findViewById(R.id.finalTimeText);
         placeTextView=findViewById(R.id.placeText);
         actionTextView=findViewById(R.id.actionText);
         tec1TextView=findViewById(R.id.tec1Text);
         tec2TextView=findViewById(R.id.tec2Text);
         tec3TextView=findViewById(R.id.tec3Text);
         doneBtn=findViewById(R.id.saveTasktBtn);

         doneBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 verifyData();
             }
         });
    }

    public void verifyData(){
        boolean noError=true;
        EditText[] fields= {startTimeTextView,placeTextView,actionTextView,tec1TextView};
        for(EditText view : fields){
            if(view.getText().toString().equals("")){
                view.setError("No dejar vacio");
                noError=false;
            }

        }
        if(noError){
            createTask();
            if(editingTask)
                updateTask();
            else
            saveTask();
        }

    }

    public void createTask(){
        task=new Task();
        task.setId(123123);
        task.setStartingTime(startTimeTextView.getText().toString()); ;
        task.setAddress(placeTextView.getText().toString());
        task.setAction(actionTextView.getText().toString());
        task.setTec1Name(tec1TextView.getText().toString());
        task.setTec2Name(tec2TextView.getText().toString());
        task.setTec3Name(tec3TextView.getText().toString());
        task.setFinalTime(endTimeTextView.getText().toString());

    }
    public void updateTask(){
        databaseReference.child(taskId).setValue(task);
    }
    public void saveTask(){
databaseReference.push().setValue(task);
    }


}
