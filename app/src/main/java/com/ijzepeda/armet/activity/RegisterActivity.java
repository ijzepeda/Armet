package com.ijzepeda.armet.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ijzepeda.armet.model.User;
import com.ijzepeda.armet.R;
import com.ijzepeda.armet.util.Constants;
import com.ijzepeda.armet.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private static final String REGISTER_URL = Constants.URL_API+Constants.VER_API+Constants.URL_REGISTER ;

    EditText emailTV;
    EditText emailRepeatTV;
    EditText firstnameTV;
    EditText lastnameTV;
//    EditText companyTV;
//    EditText titleTV;
    EditText phoneTV;
    EditText passwordTV;
    EditText passwordRepeatTV;
    Button uploadPhotoBtn;
    Button registerBtn;
    Button gobackBtn;
    Button tryAgainBtn;
    Button continueBtn;
    LinearLayout fieldsLayout;
    LinearLayout messagesLayout;
    TextView messageTextView;
    ImageView userPhotoIV;
    private View mProgressView;
    private View mLoginFormView;
    boolean goodToGo;

    private User user = new User();
    boolean cancel = false;
    View focusView = null;

    Context context;
    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = this;

        emailTV = findViewById(R.id.email_edittext);
        emailRepeatTV = findViewById(R.id.email_doublecheck_edittext);
        firstnameTV = findViewById(R.id.firstname_edittext);
//        lastnameTV = findViewById(R.id.lastname_edittext);
//        companyTV = findViewById(R.id.company_edittext);
//        titleTV = findViewById(R.id.title_edittext);
        phoneTV = findViewById(R.id.telephone_edittext);
        passwordTV = findViewById(R.id.password_edittext);
        passwordRepeatTV = findViewById(R.id.password_repeat_edittext);
        registerBtn = findViewById(R.id.register_button);
        gobackBtn = findViewById(R.id.go_back_btn);
        continueBtn = findViewById(R.id.continue_btn);
        messageTextView = findViewById(R.id.messageTextview);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        fieldsLayout = findViewById(R.id.fields_layout);
        messagesLayout = findViewById(R.id.information_layout);


        emailTV.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!Util.isValidEmailAddress(emailTV.getText().toString()) && !emailTV.getText().toString().isEmpty()) {
                    emailTV.setError(getString(R.string.error_invalid_email));
                    focusView = emailTV;
                    cancel = true;
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isConnectedToInternet(getApplication()))
                    verifyData();


            }
        });


        emailRepeatTV.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!fieldsMatch(emailTV.getText().toString(), emailRepeatTV.getText().toString())) {
                    emailRepeatTV.setError(getString(R.string.error_email_match));
                } else {
                    emailRepeatTV.setError(null);
                }
            }
        });

        passwordTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//nothing to do here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView myOutputBox = findViewById(R.id.password_edittext);
                String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=()_.\\-]).{6,}";


                Drawable line = null;
                if ((s.toString().matches(pattern))) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        line = context.getDrawable(R.drawable.password_line_shape_green);
                    }
                    goodToGo = true;
                } else if ((s.toString().length() > 5)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        line = context.getDrawable(R.drawable.password_line_shape_yellow);
                    }
                    goodToGo = false;
                    setErrorTextMissingPasswordChar(s.toString());
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        line = context.getDrawable(R.drawable.password_line_shape_red);
                    }
                    goodToGo = false;
                    setErrorTextMissingPasswordChar(s.toString());

                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    myOutputBox.setCompoundDrawablesWithIntrinsicBounds(null, null, null, line);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//nothing to do here
            }
        });

        passwordRepeatTV.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!fieldsMatch(passwordTV.getText().toString(), passwordRepeatTV.getText().toString())) {
                    passwordRepeatTV.setError(getString(R.string.error_password_match));
                } else {
                    passwordRepeatTV.setError(null);
                }
            }
        });


        gobackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagesLayout.setVisibility(View.GONE);
                fieldsLayout.setVisibility(View.VISIBLE);
            }
        });
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//go back to the login activity
                finish();
            }
        });

        fieldsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissKeyboard();
            }
        });
    }

    public void setErrorTextMissingPasswordChar(String password) {
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=()_.\\-]).{6,}";
        String patternLower = "(?=.*[a-z]).{1,}";
        String patternUpper = "(?=.*[A-Z]).{1,}";
        String patternNumber = "(?=.*[0-9]).{1,}";
        String patternSpecialChar = "(?=.*[!@#$%^&+=()_.\\-]).{1,}";
        String error = "";

        if (!(password.matches(patternLower))) {
            goodToGo = false;
            error += "\'lower case\'";
        }
        if (!(password.matches(patternUpper))) {
            goodToGo = false;
            if (!error.equals(""))
                error += " ";
            error += "\'upper case\'";
        }
        if (!(password.matches(patternNumber))) {
            goodToGo = false;
            if (!error.equals(""))
                error += " ";
            error += "\'number\'";
        }
        if (!(password.matches(patternSpecialChar))) {
            goodToGo = false;
            if (!error.equals(""))
                error += " ";
            error += "\'special character\'";
        }
        if ((password.matches(pattern))) {
            goodToGo = true;
            passwordTV.setError(null);
        } else {
            if (error.equals(""))//
                error = "Too short";//
            else//
                error = "Missing: " + error;
            passwordTV.setError(error);
        }


    }

    public void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != getCurrentFocus()) {
            assert imm != null;
            imm.hideSoftInputFromWindow(getCurrentFocus()
                    .getApplicationWindowToken(), 0);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    void verifyData() {
        boolean anyError = false;
        EditText[] fields = new EditText[]{emailTV,
                emailRepeatTV,
                firstnameTV,
                passwordTV,
                passwordRepeatTV};
        for (EditText currentField : fields) {
            if (currentField.getText().toString().length() <= 0) {
                currentField.setError(getString(R.string.error_empty_field));
                anyError = true;
                goodToGo = false;
            }
        }

        if (!fieldsMatch(passwordTV.getText().toString(), passwordRepeatTV.getText().toString())) {
            passwordRepeatTV.setError(getString(R.string.error_password_match));
        } else {
            passwordRepeatTV.setError(null);
        }
        if (!fieldsMatch(emailTV.getText().toString(), emailRepeatTV.getText().toString())) {
            emailRepeatTV.setError(getString(R.string.error_email_match));
        } else {
            emailRepeatTV.setError(null);
        }

        if (!fieldsMatch(passwordTV.getText().toString(), passwordRepeatTV.getText().toString())
                || !fieldsMatch(emailTV.getText().toString(), emailRepeatTV.getText().toString())
                || !goodToGo || anyError
                ) {
            Log.d("REGISTRER", "There are some errors on password confirmation");
        } else {
            createUser();
        }

    }


    /**
     * createUser(). It is called from verify fields, if everything is OK it will create an User object. It call sendDate()
     */
    void createUser() {
        showProgress(true);
        user.setEmail(emailTV.getText().toString());
        user.setFirstName(firstnameTV.getText().toString());
        user.setLastName(lastnameTV.getText().toString());
        user.setPhone(phoneTV.getText().toString());
//        user.setPosition(passwordTV.getText().toString());

        sendData();
    }


    /***sendData() Use the User object and send it to the endpoint. it just receives a response.code 200
     * */
    void sendData() {
        //create json from user object
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .build();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, gson.toJson(user));
        Request request = new Request.Builder()
                .url(REGISTER_URL)
                .post(body)
                .addHeader("Content-Type", Constants.JSON_POSTMAN_STRING)
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "788c8eb0-71de-1d09-4c90-748eb5fc4d5c")
                .build();


        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e);
                RegisterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgress(false);
                    }
                });
                if (e.toString().contains("timeout")) {
                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showErrorMessage(getResources().getString(R.string.server_took_too_long) + " " + getResources().getString(R.string.please_try_again));
                        }
                    });
                }

            }


            @Override
            public void onResponse(okhttp3.Call call, final  okhttp3.Response response) throws IOException {
                Log.e(TAG, "onResponse code: " + response.code());
                if (response.code() == Constants.RESPONSE_OK) {

                    //user to sharedprefs
                    Util.saveUserSession(context, user);
                    //Show messages before going to next activity
                    runOnUiThread(new Runnable() {  //this thread is used to update the progressbar
                        @Override
                        public void run() {
                            showProgress(false);//make visible messages and continueBtn
                            continueBtn.setVisibility(View.VISIBLE);
                            gobackBtn.setVisibility(View.GONE);
                            showErrorMessage(getString(R.string.message_register_success));
                        }
                    });


                } else {
                    runOnUiThread(new Runnable() {  //this thread is used to update the progressbar
                        @Override
                        public void run() {

                            showProgress(false);
                            //make visible messages and continueBtn
                            continueBtn.setVisibility(View.GONE);
                            gobackBtn.setVisibility(View.VISIBLE);

                            String responseJson = null;
                            try {
                                responseJson = response.body().string();
                            } catch (IOException e) {
                                Log.e(TAG, e.getMessage());
                            }
                            try {
                                final JSONObject mainObject = new JSONObject(responseJson);
                                showErrorMessage(mainObject.getString("message"));
                            } catch (JSONException e) {
                                Log.e(TAG, e.getMessage());

                            }
                        }
                    });

                }

            }
        });


    }

    /**
     * showErrorMessage(String) receives a message that is going to show on the screen. It also hides the previous fields and then shows the message and back button
     */
    void showErrorMessage(String message) {
        messageTextView.setText(message);
        fieldsLayout.setVisibility(View.GONE);
        messagesLayout.setVisibility(View.VISIBLE);

    }


    boolean fieldsMatch(String field1, String field2) {
        return field1.equals(field2);
    }


}
