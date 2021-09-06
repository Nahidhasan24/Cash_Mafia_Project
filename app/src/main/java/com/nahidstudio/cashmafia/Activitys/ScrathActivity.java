package com.nahidstudio.cashmafia.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.anupkumarpanwar.scratchview.ScratchView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nahidstudio.cashmafia.Models.Counter;
import com.nahidstudio.cashmafia.Models.PutTime;
import com.nahidstudio.cashmafia.Models.UserModel;
import com.nahidstudio.cashmafia.R;
import com.nahidstudio.cashmafia.databinding.ActivityScrathBinding;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.startapp.sdk.adsbase.adlisteners.VideoListener;


import java.util.Date;
import java.util.HashMap;
import java.util.Random;


public class ScrathActivity extends AppCompatActivity {


    ActivityScrathBinding binding;
    int CARD_POINT;
    FirebaseAuth mAuth;
    DatabaseReference mrRef;
    String  uid;
    int mainPoint;
    int totalPoint;
    int COUNT=0;
    int CURRENT_TIME;
    int TASK_TIME;
    Date date;
    int pointr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrath);
        binding = ActivityScrathBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mrRef= FirebaseDatabase.getInstance().getReference("Users");
        mAuth=FirebaseAuth.getInstance();
        uid= mAuth.getCurrentUser().getUid();
        date=new Date();
        CURRENT_TIME= date.getHours();
        Random random=new Random();
        CARD_POINT=random.nextInt(11)+1;
        binding.cardPointTv.setText(String.valueOf(CARD_POINT));



        MainCheck();
        CheckTime();

        binding.scratchView.setRevealListener(new ScratchView.IRevealListener() {
            @Override
            public void onRevealed(ScratchView scratchView) {

                binding.scratchView.clear();
                CheckCount();

                Toast.makeText(getApplicationContext(), "Congregation", Toast.LENGTH_SHORT).show();
                ShowAd();



            }

            @Override
            public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {

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

                        //Toast.makeText(getApplicationContext(), "Close Ads", Toast.LENGTH_SHORT).show();
                        UpdatePoint();
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

        mrRef.child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        UserModel userModel=snapshot.getValue(UserModel.class);
                        assert userModel != null;
                        totalPoint=CARD_POINT+userModel.getPoint();
                        //Toast.makeText(getActivity(), String.valueOf(totalPoint), Toast.LENGTH_SHORT).show();

                        HashMap<String,Object> map=new HashMap<>();
                        map.put("point",totalPoint);
                        ///Toast.makeText(getContext(), String.valueOf(totalPoint), Toast.LENGTH_SHORT).show();
                        mrRef.child(uid)
                                .updateChildren(map)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(), "Point Added Success", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(getApplicationContext(), "Close Ads", Toast.LENGTH_SHORT).show();
                                            CheckTime();

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
        AlertDialog.Builder  alertDialogBuilder1 = new AlertDialog.Builder(ScrathActivity.this);
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

        PutTime putTime=new PutTime(date.getHours()+2,"scratch");
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Tasks").child("scratch");
        databaseReference.child(uid)
                .setValue(putTime);

    }

    private void UpdateCount(int c){




        Counter counter=new Counter(c+1,uid);


        DatabaseReference drs=FirebaseDatabase.getInstance().getReference("Counter")
                .child("scratch");
        drs.child(uid)
                .setValue(counter);




    }

    private void getCount(){
        DatabaseReference drs=FirebaseDatabase.getInstance().getReference("Counter")
                .child("scratch");
        drs.child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()){
                            start(1);
                        }else {
                            Counter counter = snapshot.getValue(Counter.class);
                            binding.cardPointTv.setText(String.valueOf(counter.getCount()));
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
                .child("scratch");
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

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Tasks").child("scratch");
        databaseReference.child(uid).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            PutTime putTime = snapshot.getValue(PutTime.class);
                            TASK_TIME = putTime.getTime();

                            if (CURRENT_TIME >= TASK_TIME) {
                                DatabaseReference  mPostReference = FirebaseDatabase.getInstance().getReference()
                                        .child("Tasks").child("scratch").
                                                child(uid);
                                mPostReference.removeValue();
                                binding.cont.setVisibility(View.VISIBLE);

                            } else {

                                Toast.makeText(getApplicationContext(), "Your Task Limit 13 is end pls try after 2 Hour's", Toast.LENGTH_LONG).show();
                                binding.cont.setVisibility(View.GONE);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        DatabaseReference drs=FirebaseDatabase.getInstance().getReference("Counter")
                .child("scratch");
        drs.child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Counter counter = snapshot.getValue(Counter.class);
                            binding.cardCountTv.setText(String.valueOf(counter.getCount()));
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
                .child("scratch");
        drs.child(uid)
                .setValue(counter);


    }

    private void CheckCountResume(){

        DatabaseReference drs=FirebaseDatabase.getInstance().getReference("Counter")
                .child("scratch");
        drs.child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Counter counter=snapshot.getValue(Counter.class);
                        if (snapshot.exists()) {
                            if (counter.getCount() >= 14) {
                                UpdateTime();
                                start(0);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



    }

    @Override
    protected void onResume() {
        CheckTime();
        super.onResume();
    }
}















