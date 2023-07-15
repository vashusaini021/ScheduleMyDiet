package com.example.schedulemydiet.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.schedulemydiet.R;
import com.example.schedulemydiet.helpers.DatabaseHelper;
import com.example.schedulemydiet.helpers.TaskCompletion;
import com.example.schedulemydiet.home.NavigationMainActivity;
import com.example.schedulemydiet.models.MyUserData;
import com.example.schedulemydiet.network.Loader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;

public class SignupActivity extends AppCompatActivity {

    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextPhone;
    EditText editTextAddress;
    EditText editTextCity;
    EditText editTextConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        DatabaseHelper.getInstance().initialiseFirebase();

        editTextFirstName = findViewById(R.id.idEdiTextFnameSignup);
        editTextLastName = findViewById(R.id.idEdiTextLnameSignup);
        editTextEmail = findViewById(R.id.idEditTextEmailSignUp);
        editTextPassword = findViewById(R.id.idEditTextPasswordSignup);
        editTextConfirmPassword = findViewById(R.id.idEditTextConfirmPasswordPassword);
        editTextPhone =  findViewById(R.id.idPhoneNumberEditText);
        editTextAddress =  findViewById(R.id.idSignupAddressEditText);
        editTextCity =  findViewById(R.id.idCityEditText);

        Button signupButton = findViewById(R.id.idSignupButtonSignup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpAction();
            }
        });
    }

    //Email Regex
    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    //Button methods
    void signUpAction() {

        //Check for empty fields and then signup

         if (editTextFirstName.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Please enter first name.", Toast.LENGTH_SHORT).show();

        } else if (editTextLastName.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Please enter last name.", Toast.LENGTH_SHORT).show();

        } else if (editTextEmail.getText().toString().trim().equals("")) {
             Toast.makeText(this, "Please enter email.", Toast.LENGTH_SHORT).show();

         } else if (!isValidEmail(editTextEmail.getText().toString().trim())) {
            Toast.makeText(this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();

        }
         else if (editTextPhone.getText().toString().trim().equals("") || editTextPhone.getText().toString().trim().length() < 10 ) {
             Toast.makeText(this, "Please enter valid phone number.", Toast.LENGTH_SHORT).show();

         }
         else if (editTextCity.getText().toString().trim().equals("")) {
             Toast.makeText(this, "Please enter city.", Toast.LENGTH_SHORT).show();

         }
         else if (editTextAddress.getText().toString().trim().equals("")) {
             Toast.makeText(this, "Please enter address.", Toast.LENGTH_SHORT).show();

         } else if (editTextPassword.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Please enter password.", Toast.LENGTH_SHORT).show();

        } else if (editTextPassword.getText().toString().trim().length() < 6) {
            Toast.makeText(this, "Password should be at least 7 digits.", Toast.LENGTH_SHORT).show();

        } else if (!editTextPassword.getText().toString().trim().equals(editTextConfirmPassword.getText().toString().trim())) {
             Toast.makeText(this, "Password and confirm should be same.", Toast.LENGTH_SHORT).show();
         }
         else {
             addAuthUser();
        }
    }

    private void addAuthUser()
    {
        Loader.show(this);
        // [START create_user_with_email]
        DatabaseHelper.getInstance().mAuth.createUserWithEmailAndPassword(editTextEmail.getText().toString().trim().toLowerCase(), editTextPassword.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String userId = task.getResult().getUser().getUid();

                            MyUserData mydata = new MyUserData();
                            mydata.setFirstName(editTextFirstName.getText().toString().trim());
                            mydata.setLastName(editTextLastName.getText().toString().trim());
                            mydata.setUserId(userId);
                            mydata.setEmail(editTextEmail.getText().toString().toLowerCase().trim());
                            mydata.setAddress(editTextAddress.getText().toString().toLowerCase().trim());
                            mydata.setCity(editTextCity.getText().toString().toLowerCase().trim());
                            mydata.setPhone(editTextPhone.getText().toString().toLowerCase().trim());

                                SharedPreferences.Editor editor = DatabaseHelper.getInstance().getPref().edit();
                                editor.putString("userId",userId);
                                editor.apply();


                            Log.d("DATABASE :", "createUserWithEmail:success");
                            saveUserToDatabase(mydata);

                        } else {
                            Loader.dismiss();

                            // If sign in fails, display a message to the user.
                            Log.w("DataBase :", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserToDatabase(MyUserData mydata) {
        DatabaseHelper.getInstance().saveUserData(mydata, new TaskCompletion<MyUserData>() {
            @Override
            public void taskCompletion(boolean isSuccess, MyUserData data)  {
                Loader.dismiss();
                Toast.makeText(SignupActivity.this, "User Created",
                        Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(SignupActivity.this, NavigationMainActivity.class));
            }
        });
    }

}