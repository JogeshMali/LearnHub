package com.example.learnhub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.learnhub.faculty.Facultyhome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    Spinner spinnerprofession ;
    EditText editTextusername;
    EditText editTextpassword,editemail;
    Button loginbtn ,signupbtn;
    DatabaseReference databaseReference;
    public static final String SHARED_PREFS = "shared_prefs";
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        databaseReference = FirebaseDatabase.getInstance().getReference();
        spinnerprofession = findViewById(R.id.spinner);
        editTextusername = findViewById(R.id.uname);
        editTextpassword = findViewById(R.id.password);
        editemail=findViewById(R.id.email);
        loginbtn = findViewById(R.id.signupbtn);
        signupbtn=findViewById(R.id.Signupbtn);
        progressBar = findViewById(R.id.progressBarlogin);
        checkBox();
        changeinProgress(false);

        loginbtn.setOnClickListener(v -> {
            String profession = spinnerprofession.getSelectedItem().toString();
            String username = editTextusername.getText().toString();
            String password = editTextpassword.getText().toString();
            String email = editemail.getText().toString().trim();

            if (!profession.isEmpty() && !email.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
                if(isValidEmail(email)){
                if (profession.equals("Student")) {
                    CheckLogin("Students", username,email, password);

                } else {
                    CheckLogin("Faculty",username, email, password);
                }


            }
            else {
                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            }}
        else {
                Toast.makeText(Login.this, "PLease fill all the option", Toast.LENGTH_SHORT).show();
            }});
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });




            }

    private void checkBox() {
        SharedPreferences sharedPreferences =  getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        String  check = sharedPreferences.getString("name","");
        String  username = sharedPreferences.getString("username","");
        String  email = sharedPreferences.getString("email","");
        String  usertype = sharedPreferences.getString("usertype","");
        if (check.equals("true")){
            Intent intent = new Intent(Login.this, Facultyhome.class);
            intent.putExtra("username",username);
            intent.putExtra("email",email);
            intent.putExtra("usertype",usertype);
            startActivity(intent);
            finish();
        }
    }

    private void CheckLogin(String usertype ,String username,String email,String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeinProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeinProgress(false);
                if (task.isSuccessful()) {
                    if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                        FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();
                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                        user.updateProfile(profileChangeRequest);
                        SharedPreferences sharedPreferences =  getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                        SharedPreferences.Editor editor  = sharedPreferences.edit();
                        editor.putString("name","true");
                        editor.putString("username",username);
                        editor.putString("email",email);
                        editor.putString("usertype",usertype);
                        editor.apply();
                        Intent intent = new Intent(Login.this, Facultyhome.class);
                        intent.putExtra("username",username);
                        intent.putExtra("email",email);
                        intent.putExtra("usertype",usertype);
                        startActivity(intent);
                        finish();


                    } else {
                        Toast.makeText(Login.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Login.this, "Authentication failed. Check your credentials.", Toast.LENGTH_SHORT).show();

                    }
            }
        });
    /*   Query emailquery = databaseReference.child(usertype).orderByChild("email").equalTo(email);
        emailquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot passsnapshot : snapshot.getChildren()){
                        String storedpass = passsnapshot.child("password").getValue(String.class);
                        if(storedpass!=null && storedpass.equals(password)){
                            String userid = databaseReference.getKey();
                            Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                            // Proceed to next activity or whatever your login success action is
                            Intent intent = new Intent(Login.this, Facultyhome.class);
                            intent.putExtra("username",username);
                            intent.putExtra("email",email);
                            intent.putExtra("usertype",usertype);
                            startActivity(intent);
                            finish();

                            return;
                        } else {
                            Toast.makeText(Login.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(Login.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Login.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/



    }

    private void changeinProgress(boolean inProgress) {
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginbtn.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            loginbtn.setVisibility(View.VISIBLE);
        }
    }

    private boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}

