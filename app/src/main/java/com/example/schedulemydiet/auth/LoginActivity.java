package com.example.schedulemydiet.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.schedulemydiet.R;
import com.example.schedulemydiet.databinding.ActivityLoginBinding;
import com.example.schedulemydiet.helpers.DatabaseHelper;
import com.example.schedulemydiet.helpers.TaskCompletion;
import com.example.schedulemydiet.home.NavigationMainActivity;
import com.example.schedulemydiet.models.MyUserData;
import com.example.schedulemydiet.network.Loader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.idTextForgetPasswordLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        binding.idLoginButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAction();
            }
        });

        binding.idTextViewSignupLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    //Button methods
    void loginAction() {
        //Check for empty fields and then login

        if (binding.idEditTextEamilSignIn.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Please enter email address.", Toast.LENGTH_SHORT).show();
        } else if (!isValidEmail(binding.idEditTextEamilSignIn.getText().toString().toLowerCase().trim())) {
            Toast.makeText(this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
        } else if (binding.idEditTextPasswordSignin.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter password.", Toast.LENGTH_SHORT).show();
        } else {
            checkInFirebase();
        }
    }

    private void checkInFirebase() {

        DatabaseHelper.getInstance().initialiseFirebase();
        Loader.show(this);
        DatabaseHelper.getInstance().mAuth.signInWithEmailAndPassword(binding.idEditTextEamilSignIn.getText().toString().toLowerCase().trim(),
                        binding.idEditTextPasswordSignin.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            try {

                                //Save details in shared pref
                                SharedPreferences.Editor editor = DatabaseHelper.getInstance().getPref().edit();
                                editor.putString("userId", DatabaseHelper.getInstance().firebaseUser.getUid());
                                editor.apply(); // commit changes
                                DatabaseHelper.getInstance().fetchUser(new TaskCompletion<MyUserData>() {
                                    @Override
                                    public void taskCompletion(boolean isSuccess, MyUserData data) {
                                        Loader.dismiss();
                                        finish();
                                        startActivity(new Intent(LoginActivity.this, NavigationMainActivity.class));
                                    }
                                });
                            } catch (Exception ex) {
                                Loader.dismiss();
                                Toast.makeText(LoginActivity.this, "Error occured!!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Loader.dismiss();
                            Log.w("FIREBASE ::", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
