package com.ijzepeda.armet.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ijzepeda.armet.R;

public class MainActivity extends Activity {

    Button logoutBtn;
    Context context;
FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
user = FirebaseAuth.getInstance().getCurrentUser();

        logoutBtn = findViewById(R.id.logoutBtn);
logoutBtn.setText("Logout "+user.getDisplayName());
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
    }
}
