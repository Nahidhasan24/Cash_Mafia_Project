package com.nahidstudio.cashmafia.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nahidstudio.cashmafia.Models.AppConig;
import com.nahidstudio.cashmafia.Models.Counter;
import com.nahidstudio.cashmafia.Models.PutTime;
import com.nahidstudio.cashmafia.Models.UserModel;
import com.nahidstudio.cashmafia.R;
import com.nahidstudio.cashmafia.databinding.ActivityMainBinding;
import com.nahidstudio.cashmafia.databinding.ActivityWatchAdsBinding;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.startapp.sdk.adsbase.adlisteners.VideoListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class WatchAdsActivity extends AppCompatActivity {


    ActivityWatchAdsBinding binding;
    FirebaseAuth mAuth;
    DatabaseReference mrRef;
    String  uid;
    int mainPoint;
    int totalPoint;
    int POINT_PER_TASK;
    DatabaseReference user;
    int COUNT=0;
    int CURRENT_TIME;
    int TASK_TIME;
    Date date;
    int pointr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWatchAdsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mrRef= FirebaseDatabase.getInstance().getReference("appconig");
         user=FirebaseDatabase.getInstance().getReference("Users");

        mAuth=FirebaseAuth.getInstance();
        StartAppSDK.init(getApplicationContext(),getString(R.string.startapp_id));
        uid= mAuth.getCurrentUser().getUid();
        date=new Date();
        CURRENT_TIME= date.getHours();

        MainCheck();
        CheckTime();

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ShowData();
        binding.watchAdsBtn.setOnClickListener(v->{

            getCount();
            CheckCount();
            ShowAd();
            Toast.makeText(getApplicationContext(), "Wait Ads is Loading..", Toast.LENGTH_SHORT).show();

        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
    private void ShowData(){
        mrRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                AppConig appConig=snapshot.getValue(AppConig.class);
                assert appConig != null;
                binding.watchAdsMessage.setText(appConig.getWatchadsmassage());
                Random random=new Random();
                POINT_PER_TASK=random.nextInt(11)+1;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getpoint() {

        user.child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel=snapshot.getValue(UserModel.class);
                        mainPoint=userModel.getPoint();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
    private void UPdata(int point){


        totalPoint=mainPoint+point;

        HashMap<String,Object> map=new HashMap<>();
        map.put("point",totalPoint);
        mrRef.child(uid)
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Point Added Success", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }
    private void ShowAd(){
        final StartAppAd rewardedVideo = new StartAppAd(this);

        rewardedVideo.setVideoListener(new VideoListener() {


            @Override
            public void onVideoCompleted() {
                //UpdatePoint();
                //Toast.makeText(getApplicationContext(), "Close Ads onVideoCompleted", Toast.LENGTH_SHORT).show();

            }
        });

        rewardedVideo.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {

            @Override
            public void onReceiveAd(Ad ad) {
                rewardedVideo.showAd();
                //Toast.makeText(getApplicationContext(), "ads is finish", Toast.LENGTH_SHORT).show();
                CountDownTimer countDownTimer=new CountDownTimer(60000,1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        UpdatePoint();
                        Toast.makeText(getApplicationContext(), "Close Ads", Toast.LENGTH_SHORT).show();

                    }
                }.start();

            }

            @Override
            public void onFailedToReceiveAd(Ad ad) {
                Toast.makeText(getApplicationContext(), "onFailedToReceiveAd", Toast.LENGTH_SHORT).show();

            }

        });


    }
    private void UpdatePoint(){

        user.child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        UserModel userModel=snapshot.getValue(UserModel.class);
                        assert userModel != null;
                        totalPoint=POINT_PER_TASK+userModel.getPoint();
                        //Toast.makeText(getActivity(), String.valueOf(totalPoint), Toast.LENGTH_SHORT).show();

                        HashMap<String,Object> map=new HashMap<>();
                        map.put("point",totalPoint);
                        ///Toast.makeText(getContext(), String.valueOf(totalPoint), Toast.LENGTH_SHORT).show();
                        user.child(uid)
                                .updateChildren(map)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(), "Point Added Success", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(getApplicationContext(), "Close Ads", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });





    }
    private void MainCheck() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @SuppressLint("NewApi")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                checkNet();
                handler.postDelayed(this, 2000);

            }
        }, 1000);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private  void checkNet() {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo vpn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_VPN);
        NetworkInfo mobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


        if (!vpn.isConnected()) {
            showDialog("Connect Vpn");

        }
    }
    private void showDialog(String msg){
       AlertDialog.Builder  alertDialogBuilder1 = new AlertDialog.Builder(WatchAdsActivity.this);
        alertDialogBuilder1.setTitle("Error !");
        alertDialogBuilder1.setCancelable(false);
        alertDialogBuilder1.setIcon(R.drawable.ic_baseline_warning_24);
        alertDialogBuilder1.setMessage(msg);
        alertDialogBuilder1.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.exit(0);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder1.create();
        alertDialog.show();



    }






    private void UpdateTime(){

        PutTime putTime=new PutTime(date.getHours()+2,"ads");
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Tasks").child("ads");
        databaseReference.child(uid)
                .setValue(putTime);

    }

    private void UpdateCount(int c){




        Counter counter=new Counter(c+1,uid);


        DatabaseReference drs=FirebaseDatabase.getInstance().getReference("Counter")
                .child("ads");
        drs.child(uid)
                .setValue(counter);




    }

    private void getCount(){
        DatabaseReference drs=FirebaseDatabase.getInstance().getReference("Counter")
                .child("ads");
        drs.child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()){
                            start(1);
                        }else {
                            Counter counter = snapshot.getValue(Counter.class);
                            binding.adsCountTv.setText(String.valueOf(counter.getCount()));
                            COUNT = counter.getCount();
                            UpdateCount(COUNT);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void CheckCount(){

        DatabaseReference drs=FirebaseDatabase.getInstance().getReference("Counter")
                .child("ads");
        drs.child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Counter counter=snapshot.getValue(Counter.class);
                        if (snapshot.exists()) {
                            if (counter.getCount() >= 14) {
                                UpdateTime();
                                start(0);
                            } else {
                                UpdateCount(counter.getCount());
                                CheckTime();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



    }

    private void CheckTime(){

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Tasks").child("ads");
        databaseReference.child(uid).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            PutTime putTime = snapshot.getValue(PutTime.class);
                            TASK_TIME = putTime.getTime();

                            if (CURRENT_TIME >= TASK_TIME) {
                                DatabaseReference  mPostReference = FirebaseDatabase.getInstance().getReference()
                                        .child("Tasks").child("ads").
                                                child(uid);
                                mPostReference.removeValue();
                                binding.watchAdsBtn.setEnabled(true);

                            } else {
                                binding.watchAdsBtn.setOnClickListener(v->{
                                    Toast.makeText(getApplicationContext(), "Your Task Limit 13 is end pls try after 2 Hour's", Toast.LENGTH_LONG).show();

                                });
                                Toast.makeText(getApplicationContext(), "Your Task Limit 13 is end pls try after 2 Hour's", Toast.LENGTH_LONG).show();
                                binding.watchAdsBtn.setEnabled(false);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        DatabaseReference drs=FirebaseDatabase.getInstance().getReference("Counter")
                .child("ads");
        drs.child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Counter counter = snapshot.getValue(Counter.class);
                            binding.adsCountTv.setText(String.valueOf(counter.getCount()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void start(int c){

        Counter counter=new Counter(c,uid);


        DatabaseReference drs=FirebaseDatabase.getInstance().getReference("Counter")
                .child("ads");
        drs.child(uid)
                .setValue(counter);


    }








}




















