package com.nahidstudio.cashmafia.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nahidstudio.cashmafia.Models.Counter;
import com.nahidstudio.cashmafia.R;

public class SplashActivity extends AppCompatActivity {


    FirebaseAuth mAuth;
    FirebaseUser user;
    String  uid;
    boolean isDeceted=true;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        MainCheck();





    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void MainCheck() {

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @SuppressLint("NewApi")
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void run() {
              checkNet();
//                handler.postDelayed(this, 2000);
//
//            }
//        }, 1000);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private  void checkNet() {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo vpn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_VPN);
        NetworkInfo mobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


        if (!vpn.isConnected()) {
            showDialog("Connect Vpn");

        }else if (!mobileNetwork.isConnected() && !wifi.isConnected()){
            showDialog("No InterNet Connection");
        }else {
            pass();
        }
    }
    private void showDialog(String msg){
        AlertDialog.Builder  alertDialogBuilder1 = new AlertDialog.Builder(SplashActivity.this);
        alertDialogBuilder1.setTitle("Error !");
        alertDialogBuilder1.setCancelable(false);
        alertDialogBuilder1.setIcon(R.drawable.ic_baseline_warning_24);
        alertDialogBuilder1.setMessage(msg);
        alertDialogBuilder1.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                System.exit(0);
            }
        });

       AlertDialog alertDialog = alertDialogBuilder1.create();
       alertDialog.show();



    }
    private void pass(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user != null) {
                    uid= mAuth.getCurrentUser().getUid();
                    start(0);

                } else {
                    startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                    finishAffinity();

                }
            }
        }, 3000);
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


        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finishAffinity();







    }
}















