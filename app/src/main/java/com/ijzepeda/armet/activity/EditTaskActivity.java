package com.ijzepeda.armet.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.ijzepeda.armet.model.DataSingleton;
import com.ijzepeda.armet.model.Product;
import com.ijzepeda.armet.model.Servicio;
import com.ijzepeda.armet.model.Task;
import com.ijzepeda.armet.model.User;

import java.util.ArrayList;

import static com.ijzepeda.armet.util.Constants.EXTRA_EDITING_SERVICE;
import static com.ijzepeda.armet.util.Constants.EXTRA_EDITING_TASK;
import static com.ijzepeda.armet.util.Constants.EXTRA_EDIT_SERVICE;
import static com.ijzepeda.armet.util.Constants.EXTRA_TASK_ID;

public class EditTaskActivity extends AppCompatActivity {
Context context;

EditText startTimeTextView;
EditText endTimeTextView;
EditText placeTextView;
EditText clientTextView;
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
FirebaseUser firebaseUser;

DataSingleton singleton;
//User user;
com.firebase.ui.auth.data.model.User fbUser;



        //loading files if editing
    boolean editingTask=false;
    String taskId;
    private static final String TAG = "EditTaskActivity";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        context = this;
        singleton=DataSingleton.getInstance();
        Intent intent = getIntent();
        editingTask = intent.getBooleanExtra(EXTRA_EDITING_TASK, false);  //EXTRA_TASK_ID

        initFirebase();
        initComponents();

        if (editingTask) {
            LoadEditService(intent.getStringExtra(EXTRA_TASK_ID));
        }

    }


    public void initFirebase(){
        app=FirebaseApp.getInstance();
        database=FirebaseDatabase.getInstance(app);
        auth=FirebaseAuth.getInstance(app);
        storage=FirebaseStorage.getInstance(app);
        databaseReference=database.getReference("task");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }


    public void initComponents(){

         startTimeTextView=findViewById(R.id.startTimeText);
         endTimeTextView=findViewById(R.id.finalTimeText);
         placeTextView=findViewById(R.id.placeText);
        clientTextView=findViewById(R.id.clientText);
         actionTextView=findViewById(R.id.actionText);
         tec1TextView=findViewById(R.id.tec1Text);
         tec1TextView.setText(firebaseUser.getDisplayName());
         tec2TextView=findViewById(R.id.tec2Text);
         tec3TextView=findViewById(R.id.tec3Text);
         doneBtn=findViewById(R.id.saveTasktBtn);

         doneBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Log.e(TAG, "onClick: DONEbtn" );
                 verifyData();
             }
         });
    }

    public void LoadEditService(String id){
        Log.e(TAG, "LoadEditService id: "+id );
        DatabaseReference databaseCurrentServiceRef = databaseReference.child(id);
        databaseCurrentServiceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Task task= dataSnapshot.getValue(Task.class);
                    //Product product = dataSnapshot.getValue(Product.class);
                    fillFields(task);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void fillFields(Task task) {
        Log.e(TAG, "fillFields: " );
        startTimeTextView.setText(task.getStartingTime());
        endTimeTextView.setText(task.getFinalTime());
        placeTextView.setText(task.getAddress());
        clientTextView.setText(task.getClient());
        actionTextView.setText(task.getAction());
        tec1TextView.setText(task.getTec1Name());
        tec2TextView.setText(task.getTec2Name());
        tec3TextView.setText(task.getTec3Name());

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
        task.setId("000");
        task.setStartingTime(startTimeTextView.getText().toString()); ;
        task.setAddress(placeTextView.getText().toString());
        task.setAction(actionTextView.getText().toString());
        task.setTec1Name(tec1TextView.getText().toString());
        task.setTec2Name(tec2TextView.getText().toString());
        task.setTec3Name(tec3TextView.getText().toString());
        task.setFinalTime(endTimeTextView.getText().toString());
        task.setClient(clientTextView.getText().toString());
        Log.e(TAG, "createTask: taskaction"+task.getAction());

    }
    public void updateTask(){

        Log.e(TAG, "updateTask: " );databaseReference.child(taskId).setValue(task);
    }
    public void saveTask(){

        Log.e(TAG, "saveTask action: "+task.getAction() );
//        databaseReference.push().setValue(task);

        String currentTaskId = databaseReference.push().getKey();
        task.setId(currentTaskId);
        databaseReference.child(currentTaskId).setValue(task);
        Log.e(TAG, "saveTask id: "+task.getAction() );


        singleton.setTask(task);

        Intent intent = getIntent();
        intent.putExtra(EXTRA_TASK_ID, task.getId());
        setResult(Activity.RESULT_OK, intent);
//        singleton.update(context);//notrequired anymore
        finish();


    }


}
