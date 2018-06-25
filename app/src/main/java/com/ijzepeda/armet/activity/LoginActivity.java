package com.ijzepeda.armet.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.ijzepeda.armet.R;
import com.ijzepeda.armet.model.User;
import com.ijzepeda.armet.network.VolleySingleton;
import com.ijzepeda.armet.util.Constants;
import com.ijzepeda.armet.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String VOLLEY = "Volley";
    private static final String VOLLEY_ERROR = "Volley_error";
    private static final String AUTHORIZATION = "Authorization";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String LOGGING = "Logging";

    Context context;
    TextView emailTextView;
    TextView passwordTextView;
    Button loginBtn;
    Button registerBtn;
    Button logoutBtn;
    String email;
    String password;
    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "LoginActivity";
    boolean showingProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        initComponents();
//        initNetwork();

    }

    @Override
    protected void onResume() {
        super.onResume();

        //loadFirebaseUI();
    }

    @Override
    public void onBackPressed() {
        // used with android networking
        if (showingProgress)
            stopLogin();
        else
            super.onBackPressed();
    }

    void stopLogin() {
        VolleySingleton.getInstance(getApplicationContext()).cancelRequestQueue(LOGGING);
        showProgress(false);
    }

    public void showProgress(boolean show) {
        showingProgress = show;
        if (show)
            Util.showProgress(context, "please wait...");
        else
            Util.dismissProgress();
    }

    public void initComponents() {
        emailTextView = findViewById(R.id.emailTextView);
        passwordTextView = findViewById(R.id.passwordTextView);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(context)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                            }
                        });

                loadFirebaseUI();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
                showProgress(true);
                verifyData();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, RegisterActivity.class));
            }
        });

        loadFirebaseUI();

    }


    public void loadFirebaseUI(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
//                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());


// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.logo)      // Set logo drawable
//                        .setTheme(R.style.MySuperAppTheme)      // Set theme
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Intent intent= new Intent(context, MainActivity.class);
                startActivity(intent);
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }



    public void clearData() {
        email = "";
        password = "";
        emailTextView.setError(null);
        passwordTextView.setError(null);
    }

    public void verifyData() {
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();
        boolean hasError = false;

//        hasError = !Util.isValidEmailAddress(email);

        if (!Util.isValidEmailAddress(email)) {
            emailTextView.setError("Invalid email");
            hasError = true;
        }
        if (email.equals("")) {
            emailTextView.setError("Must not be empty");
            hasError = true;
        }
        if (password.equals("")) {
            emailTextView.setError("Must not be empty");
            hasError = true;
        }

        if (hasError) {
            Util.showMessage(context, "Verify your information.");
        } else {
            getUserVolley(email, password);
        }
    }

    /***
     * This logic is awful , please correct everything for the user details  [usualy backend verifies password, and gives 200, just load details and then proceeds]
     * @param email
     * @param password
     */
    private void getUserVolley(final String email, String password) {

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        Constants.URL_API + Constants.VER_API + Constants.URL_LOGIN,
                        null,
                        new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                Log.d(TAG, "onResponse: " + response.toString());

                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                try {
                                    JSONArray array = response.getJSONArray("users");

                                    for (int i = 0; i < array.length(); i++) {
                                        User user = gson.fromJson(array.getString(i), User.class);
                                        if (user.getEmail().equals(email)) {
                                            Util.showMessage(context, "Welcome");
                                            Util.saveUserSession(context, user);
                                            Intent intent = new Intent(context, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                    Util.showMessage(context, "user not found");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(VOLLEY, "2." + VOLLEY_ERROR);

                                try {
                                    Util.processErrorResponse(error, LoginActivity.this);
                                } catch (Exception e) {
                                    Util.showMessage(LoginActivity.this, Constants.ERROR_REQUEST);
                                }

                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put(AUTHORIZATION, email); //userID // Session.getInstance().getToken(getApplicationContext())
                        params.put(CONTENT_TYPE, "application/json");
                        return params;
                    }
                }.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        );
    }


}
