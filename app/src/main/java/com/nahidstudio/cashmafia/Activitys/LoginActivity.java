package com.nahidstudio.cashmafia.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.nahidstudio.cashmafia.databinding.ActivityLoginBinding;
import com.nahidstudio.cashmafia.databinding.ActivityWatchAdsBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading....");

        binding.loginBtn.setOnClickListener(v->{

            if (binding.loginMailEt.getText().toString().isEmpty()){
                binding.loginMailEt.setError("Enter Mail");
                return;

            }if (binding.loginPassEt.getText().toString().isEmpty()){
                binding.loginPassEt.setError("Enter Password");
                return;
            }

            CheckLogin(binding.loginMailEt.getText().toString(),
                    binding.loginPassEt.getText().toString());
            progressDialog.show();

        });

        binding.sendSignupPageTv.setOnClickListener(v->{

            startActivity(new Intent(getApplicationContext(),SignUpActivity.class));

        });


    }
    private void CheckLogin(String mail,String pws){
        mAuth.signInWithEmailAndPassword(mail,pws)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Login Successfully !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finishAffinity();
                        }else{
                            Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
















































