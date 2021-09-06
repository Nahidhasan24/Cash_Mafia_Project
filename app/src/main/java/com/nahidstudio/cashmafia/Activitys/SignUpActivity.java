package com.nahidstudio.cashmafia.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nahidstudio.cashmafia.Models.Counter;
import com.nahidstudio.cashmafia.Models.UserModel;
import com.nahidstudio.cashmafia.databinding.ActivityLoginBinding;
import com.nahidstudio.cashmafia.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth mAuth;
    DatabaseReference mrRef;
    String  uid;
    ProgressDialog progressDialog;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mrRef= FirebaseDatabase.getInstance().getReference("Users");
        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(SignUpActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading....");
        binding.signupBtn.setOnClickListener(view -> {
            
            if (binding.signupMailEt.getText().toString().isEmpty()){
                binding.signupMailEt.setError("Enter Mail");
                return;
            }if (binding.signupNameEt.getText().toString().isEmpty()){
                binding.signupNameEt.setError("Enter Name");
                return;
            }if (binding.signupPassEt.getText().toString().isEmpty()){
                binding.signupPassEt.setError("Enter Password");
                return;
            }if (binding.signupPassConfirmEt.getText().toString().isEmpty()){
                binding.signupPassConfirmEt.setError("Enter Password");
                return;
            }if (!binding.signupPassEt.getText().toString().equals(binding.signupPassConfirmEt.getText().toString())){
                binding.signupPassConfirmEt.setText("");
                binding.signupPassEt.setText("");
                binding.signupPassConfirmEt.setError("Password Not Matching");
                binding.signupPassEt.setError("Password Not Matching");
                return;
            }
            CreateAccount(binding.signupNameEt.getText().toString(),
                    binding.signupMailEt.getText().toString(),
                    binding.signupPassConfirmEt.getText().toString());
            progressDialog.show();

        });
        binding.sendLoginPageTv.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        });
    }
    private void CreateAccount(String name,String mail,String psw){

        mAuth.createUserWithEmailAndPassword(mail,psw)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                      if (task.isSuccessful()){
                          AddData(name,mail,psw);
                      }else{
                          showToast(task.getException().toString());
                      }
                    }
                });


    }
    private void AddData(String name,String mail,String pws){


        uid=mAuth.getCurrentUser().getUid();
        UserModel userModel=new UserModel(uid,name,mail,"","NO",10);
        mrRef.child(uid)
                .setValue(userModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            showToast("Successfully SignUp");
                            start(0);
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finishAffinity();
                        }else {
                            showToast(task.getException().toString());
                        }
                    }
                });

    }
    private void showToast(String msg){
        progressDialog.dismiss();
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    private void start(int c){

        Counter counter=new Counter(c,uid);
        DatabaseReference wheel= FirebaseDatabase.getInstance().getReference("Counter").child("wheel");
        wheel.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    wheel.child(uid)
                            .setValue(counter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Counter counter1=new Counter(c,uid);
        DatabaseReference scratch=FirebaseDatabase.getInstance().getReference("Counter").child("scratch");
        scratch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    scratch.child(uid)
                            .setValue(counter1);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Counter counter2=new Counter(c,uid);
        DatabaseReference ads=FirebaseDatabase.getInstance().getReference("Counter").child("ads");
        ads.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    ads.child(uid)
                            .setValue(counter2);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Counter counter3=new Counter(c,uid);
        DatabaseReference web=FirebaseDatabase.getInstance().getReference("Counter").child("web");
        web.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    web.child(uid)
                            .setValue(counter3);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });










    }
}





















