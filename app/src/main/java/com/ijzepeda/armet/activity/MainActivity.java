package com.ijzepeda.armet.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ijzepeda.armet.R;
import com.ijzepeda.armet.model.DataSingleton;
import com.ijzepeda.armet.model.DataSingleton;
import com.ijzepeda.armet.model.User;

public class MainActivity extends Activity {

    Button logoutBtn;
    Context context;
    FirebaseUser user;
    Button newTaskBtn;
    Button addClientBtn;

    DataSingleton dataSingleton;
    User currentUser;
    com.firebase.ui.auth.data.model.User fbUser;
//DataSingleton singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        user = FirebaseAuth.getInstance().getCurrentUser();
dataSingleton= DataSingleton.getInstance();
        setupUser();

//singleton.getInstance(context);
        initComponents();

  }

  public void setupUser(){
//        sincronizar con el user de firebase
        currentUser=new User();
        currentUser.setFirstName("Ivan");
        currentUser.setLastName("Zepeda");
        currentUser.setEmail("ijzepeda@hotmail.com");
        currentUser.setPhone("7221767190");
        currentUser.setId(1);
        currentUser.setPosition("dev");
        dataSingleton.setUser(currentUser);
  }

  public void initComponents(){


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


      newTaskBtn=findViewById(R.id.newTaskBtn);
      addClientBtn=findViewById(R.id.addClientBtn);

      newTaskBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              Intent intent=new Intent(context, EditTaskActivity.class);
              intent.putExtra("user","IvaNZepedA");

              startActivity(intent);
          }
      });
      addClientBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              startActivity(new Intent(context,AddClientActivity.class));
          }
      });
  }
}
