package com.nahidstudio.cashmafia.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.nahidstudio.cashmafia.SpinWheel.LuckyWheelView;
import com.nahidstudio.cashmafia.SpinWheel.model.LuckyItem;
import com.nahidstudio.cashmafia.databinding.ActivityWheelBinding;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.startapp.sdk.adsbase.adlisteners.VideoListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import io.paperdb.Paper;

public class WheelActivity extends AppCompatActivity {

    ActivityWheelBinding binding;
    int point;
    FirebaseAuth mAuth;
    DatabaseReference mrRef;
    String  uid;
    int totalPoint;
    CountDownTimer countDownTimer;
    int mainPoint;
    boolean isCompleted=true;
    int COUNT=0;
    int CURRENT_TIME;
    int TASK_TIME;
    Date date;
    int pointr;
    public static String COUNT_NAME="counter";







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWheelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mrRef= FirebaseDatabase.getInstance().getReference("Users");
        mAuth=FirebaseAuth.getInstance();
        uid= mAuth.getCurrentUser().getUid();
        Paper.init(this);
        StartAppSDK.init(getApplicationContext(),getString(R.string.startapp_id));
        date=new Date();
        CURRENT_TIME= date.getHours();




        list();
        CheckTime();
        getpoint();
        MainCheck();


        setSupportActionBar(binding.toolbar);

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//we need to set -1 count on checkcount for count

                getCount();
                CheckCount();
                binding.button.setEnabled(false);
                isCompleted=false;
                Random random=new Random();
                int index=random.nextInt(11)+1;
                binding.wheelView.startLuckyWheelWithTargetIndex(index);
                Log.d("MyTag", "onClick: "+pointr);



            }
        });

        binding.wheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                point=index+1;
                ShowDailoag(point);
                countDownTimer=new CountDownTimer(20000,1000) {
                    @Override
                    public void onTick(long l) {
                        binding.button.setText(String.valueOf(l/1000));
                    }

                    @Override
                    public void onFinish() {
                        binding.button.setEnabled(true);
                        binding.button.setText("Spin");

                    }
                };

            }
        });

    }

 ////////////////////////////////////////////////////////////////////////


    private void getpoint() {

        mrRef.child(uid)
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
    private void list(){
        List<LuckyItem> luckyItemList=new ArrayList<>();

        LuckyItem item=new LuckyItem();
        item.topText="01";
        item.secondaryText="Coin";
        item.color= Color.parseColor("#FFFFFF");
        item.textColor=Color.parseColor("#000000");
        luckyItemList.add(item);

         LuckyItem item1=new LuckyItem();
        item1.topText="02";
        item1.secondaryText="Coin";
        item1.color= Color.parseColor("#000000");
        item1.textColor=Color.parseColor("#FFFFFF");
        luckyItemList.add(item1);

         LuckyItem item2=new LuckyItem();
        item2.topText="03";
        item2.secondaryText="Coin";
        item2.color= Color.parseColor("#FFFFFF");
        item2.textColor=Color.parseColor("#000000");
        luckyItemList.add(item2);

         LuckyItem item3=new LuckyItem();
        item3.topText="04";
        item3.secondaryText="Coin";
        item3.color= Color.parseColor("#000000");
        item3.textColor=Color.parseColor("#FFFFFF");
        luckyItemList.add(item3);

         LuckyItem item4=new LuckyItem();
        item4.topText="05";
        item4.secondaryText="Coin";
        item4.color= Color.parseColor("#FFFFFF");
        item4.textColor=Color.parseColor("#000000");
        luckyItemList.add(item4);

         LuckyItem item5=new LuckyItem();
        item5.topText="06";
        item5.secondaryText="Coin";
        item5.color= Color.parseColor("#000000");
        item5.textColor=Color.parseColor("#FFFFFF");
        luckyItemList.add(item5);

         LuckyItem item6=new LuckyItem();
        item6.topText="7";
        item6.secondaryText="Coin";
        item6.color= Color.parseColor("#FFFFFF");
        item6.textColor=Color.parseColor("#000000");
        luckyItemList.add(item6);

         LuckyItem item7=new LuckyItem();
        item7.topText="08";
        item7.secondaryText="Coin";
        item7.color= Color.parseColor("#000000");
        item7.textColor=Color.parseColor("#FFFFFF");
        luckyItemList.add(item7);

         LuckyItem item8=new LuckyItem();
        item8.topText="09";
        item8.secondaryText="Coin";
        item8.color= Color.parseColor("#FFFFFF");
        item8.textColor=Color.parseColor("#000000");
        luckyItemList.add(item8);

         LuckyItem item9=new LuckyItem();
        item9.topText="10";
        item9.secondaryText="Coin";
        item9.color= Color.parseColor("#000000");
        item9.textColor=Color.parseColor("#FFFFFF");
        luckyItemList.add(item9);

        LuckyItem item10=new LuckyItem();
        item10.topText="11";
        item10.secondaryText="Coin";
        item10.color= Color.parseColor("#FFFFFF");
        item10.textColor=Color.parseColor("#000000");
        luckyItemList.add(item10);


        LuckyItem item11=new LuckyItem();
        item11.topText="12";
        item11.secondaryText="Coin";
        item11.color= Color.parseColor("#000000");
        item11.textColor=Color.parseColor("#FFFFFF");
        luckyItemList.add(item11);

        binding.wheelView.setData(luckyItemList);
        binding.wheelView.setRound(10);


    }
    private void ShowDailoag(int point){
        Dialog dialog=new Dialog(WheelActivity.this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.show();
        TextView textView=dialog.findViewById(R.id.dialog_score_id);
        Button btn=dialog.findViewById(R.id.cool_id);
        textView.setText(String.valueOf(point));
        btn.setOnClickListener(v->{
                dialog.dismiss();
            ShowAd();

        });
    }
    private void UPdata(int point){


        totalPoint=mainPoint+point;
        //Toast.makeText(getApplicationContext(), String.valueOf(totalPoint), Toast.LENGTH_SHORT).show();
        HashMap<String,Object> map=new HashMap<>();
        map.put("point",totalPoint);
        mrRef.child(uid)
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Point Added Success", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Close Ads", Toast.LENGTH_SHORT).show();



//                            countDownTimer.start();
                            isCompleted=true;
                        }else {
                            Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }
    @Override
    public void onBackPressed() {
        if (isCompleted){
            finish();
        }else {
            Toast.makeText(getApplicationContext(), "Wait Task is running !", Toast.LENGTH_SHORT).show();
        }

    }
    private void ShowAd(){
        final StartAppAd rewardedVideo = new StartAppAd(this);

        rewardedVideo.setVideoListener(new VideoListener() {


            @Override
            public void onVideoCompleted() {

            }
        });

        rewardedVideo.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {

            @Override
            public void onReceiveAd(Ad ad) {
                rewardedVideo.showAd();
                Toast.makeText(getApplicationContext(), "Watch Full Ads !", Toast.LENGTH_SHORT).show();
                CountDownTimer countDownTimer=new CountDownTimer(60000,1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        UPdata(point);
                        COUNT=COUNT+1;
                        Log.d("MyTag", "onFinish: "+COUNT);


                    }
                }.start();


            }

            @Override
            public void onFailedToReceiveAd(Ad ad) {
                Toast.makeText(getApplicationContext(), ad.getErrorMessage(), Toast.LENGTH_SHORT).show();

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
       AlertDialog.Builder  alertDialogBuilder1 = new AlertDialog.Builder(WheelActivity.this);
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


    /////////////////////////////////////////////////////////////////////


    private void UpdateTime(){

        PutTime putTime=new PutTime(date.getHours()+2,"wheel");
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Tasks").child("wheel");
        databaseReference.child(uid)
                .setValue(putTime);

    }

    private void UpdateCount(int c){




        Counter counter=new Counter(c+1,uid);


        DatabaseReference drs=FirebaseDatabase.getInstance().getReference("Counter")
                .child("wheel");
        drs.child(uid)
                .setValue(counter);




    }

    private void getCount(){
        DatabaseReference drs=FirebaseDatabase.getInstance().getReference("Counter")
                .child("wheel");
        drs.child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()){
                            start(1);
                        }else {
                            Counter counter = snapshot.getValue(Counter.class);
                            binding.spinCountTv.setText(String.valueOf(counter.getCount()));
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
                .child("wheel");
        drs.child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Counter counter=snapshot.getValue(Counter.class);
                        binding.spinCountTv.setText(String.valueOf(COUNT));
                        if (counter.getCount()>=13){
                            UpdateTime();
                            start(0);
                        }else {
                            UpdateCount(counter.getCount());
                            CheckTime();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



    }

    private void CheckTime(){

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Tasks").child("wheel");
        databaseReference.child(uid).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            PutTime putTime = snapshot.getValue(PutTime.class);
                            TASK_TIME = putTime.getTime();

                            if (CURRENT_TIME >= TASK_TIME) {
                              DatabaseReference  mPostReference = FirebaseDatabase.getInstance().getReference()
                                        .child("Tasks").child("wheel").
                                      child(uid);
                                mPostReference.removeValue();
                                binding.button.setEnabled(true);

                            } else {
                                binding.button.setOnClickListener(v->{
                                    Toast.makeText(getApplicationContext(), "Your Task Limit 13 is end pls try after 2 Hour's", Toast.LENGTH_LONG).show();

                                });
                                Toast.makeText(getApplicationContext(), "Your Task Limit 13 is end pls try after 2 Hour's", Toast.LENGTH_LONG).show();
                                binding.button.setEnabled(false);
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
                            binding.spinCountTv.setText(String.valueOf(counter.getCount()));
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
                .child("wheel");
        drs.child(uid)
                .setValue(counter);


    }




}







































