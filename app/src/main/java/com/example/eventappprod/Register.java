package com.example.eventappprod;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mFullName,mEmail,mPassword, confirmPassword;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;
    User u;
    private TextView profileName;


    FirebaseDatabase database;
    DatabaseReference ref;
    static int usercount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ref =FirebaseDatabase.getInstance().getReference();

        mFullName   = findViewById(R.id.fullName);
        mEmail      = findViewById(R.id.Email);
        mPassword   = findViewById(R.id.password);
        confirmPassword   = findViewById(R.id.cfmPassword);

        mRegisterBtn= findViewById(R.id.registerBtn);
        mLoginBtn   = findViewById(R.id.createText);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        profileName = findViewById(R.id.profileName);


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                String cfmPassword = confirmPassword.getText().toString().trim();
                final String fullName = mFullName.getText().toString();

                //check if valid ucsd email
                String domain = email .substring(email .indexOf("@") + 1);
                if (domain.equals("ucsd.edu") == false) {
                    mEmail.setError("Email is invalid. Using ucsd email");
                    mPassword.setText("");
                    confirmPassword.setText("");
                    return;
                }

                if (password.equals(cfmPassword) == false) {
                    confirmPassword.setError("Passwords do not match. Please type again");
                    confirmPassword.setText("");
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 8){
                    mPassword.setError("Password Must Be Longer than 7 Characters");
                    return;
                }



                progressBar.setVisibility(View.VISIBLE);

                // register the user in firebase
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            fAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Register.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                        mEmail.setText("");
                                        mPassword.setText("");
                                        u  = new User(fullName, email, password, "", "https://firebasestorage.googleapis.com/v0/b/event-b161b.appspot.com/o/EVENT%2F285871589?alt=media&token=0a2f3f7e-e6f8-4b44-ac9b-b15d0669ecd1",
                                                "https://firebasestorage.googleapis.com/v0/b/event-b161b.appspot.com/o/EVENT%2F285871589?alt=media&token=0a2f3f7e-e6f8-4b44-ac9b-b15d0669ecd1","", "", "", "", "", "");
                                        usercount++;
                                        String userRef = Integer.toString(usercount);
                                        //String userID = fullName + "/" + email.substring(0, email.indexOf("@"));
                                        //ref.child(userRef).setValue(u);
                                        String userID = email.substring(0, email.indexOf("@"));//+"@ucsd,edu";
        //                                profileName.setText(userID);

                                        ref.child("/USER").child(userID).setValue(u);
                                        startActivity(new Intent(getApplicationContext(),Login.class));

                                    } else {
                                        Log.e(TAG, "Email hasn't been verified. EmailVerification", task.getException());
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                }
                            });


                            //usercount++;
                            //String userRef = Integer.toString(usercount);
                            //ref.child(userRef).setValue(u);

                            //profileName.setText(userID);

                        }else {
                            Toast.makeText(Register.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });



        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });



    }

}
