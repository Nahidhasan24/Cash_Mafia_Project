package com.nahidstudio.cashmafia.Fragments;

import static java.lang.System.exit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nahidstudio.cashmafia.Activitys.LoginActivity;
import com.nahidstudio.cashmafia.Activitys.ScrathActivity;
import com.nahidstudio.cashmafia.Activitys.SplashActivity;
import com.nahidstudio.cashmafia.Activitys.WatchAdsActivity;
import com.nahidstudio.cashmafia.Activitys.WebActivity;
import com.nahidstudio.cashmafia.Activitys.WheelActivity;
import com.nahidstudio.cashmafia.Models.AppConig;
import com.nahidstudio.cashmafia.Models.Counter;
import com.nahidstudio.cashmafia.Models.DailyChecking;
import com.nahidstudio.cashmafia.Models.UserModel;
import com.nahidstudio.cashmafia.R;
import com.nahidstudio.cashmafia.databinding.FragmentHomeBinding;
import com.squareup.picasso.Picasso;
import com.startapp.sdk.ads.video.VideoEnabledAd;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    FirebaseAuth mAuth;
    DatabaseReference mrRef;
    String  uid;
    Date currentDate;
    SimpleDateFormat simpleDateFormat;
    String TodayDateString;
    int POINT_CHECKING=10;
    int totalPoint;
    StartAppAd startAppAd;
    ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentHomeBinding.inflate(inflater, container, false);
        mrRef= FirebaseDatabase.getInstance().getReference("Users");
        mAuth=FirebaseAuth.getInstance();
        uid= mAuth.getCurrentUser().getUid();
        StartAppSDK.init(getActivity(),getString(R.string.startapp_id));
        startAppAd=new StartAppAd(getActivity());

        AppConfig();
        MainCheck();


        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading....");

        currentDate= Calendar.getInstance().getTime();
        simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        LoadShowData();
        binding.luckyspinCard.setOnClickListener(v->{
            startActivity(new Intent(getContext(), WheelActivity.class));
        });
        binding.scrathCard.setOnClickListener(v->{
            startActivity(new Intent(getContext(), ScrathActivity.class));

        });
        binding.visitWeb.setOnClickListener(v->{
            startActivity(new Intent(getContext(), WebActivity.class));
        });
        binding.watchAds.setOnClickListener(v->{
            startActivity(new Intent(getContext(), WatchAdsActivity.class));
        });
        binding.dailChecking.setOnClickListener(v->{
            DailyChecking();
            progressDialog.show();
        });
        binding.telegramCard.setOnClickListener(v->{

            Intent telegram = new Intent(android.content.Intent.ACTION_SEND);
            telegram.setData(Uri.parse(getString(R.string.telegram_url)));
            telegram.setPackage("org.telegram.messenger");
            startActivity(Intent.createChooser(telegram, "Share with "));

        });


        return binding.getRoot();
    }

    private void LoadShowData(){

        mrRef.child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            UserModel userModel=snapshot.getValue(UserModel.class);
                            assert userModel != null;
                            binding.profileName.setText(userModel.getName());
                            binding.profileMail.setText(userModel.getMail());
                            binding.pointTv.setText(String.valueOf(userModel.getPoint()));

                            if (userModel.getBlock().equals("OFF")){
                                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                                builder.setTitle("Your Account Block");
                                builder.setIcon(R.drawable.ic_baseline_warning_24);
                                builder.setMessage("Your account is block by admin !" );
                                builder.setCancelable(false);
                                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        exit(0);

                                    }
                                });
                                AlertDialog alertDialog=builder.create();
                                alertDialog.show();
                            }


                        }else {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
    private void DailyChecking(){
        DatabaseReference daily;
         daily=FirebaseDatabase.getInstance().getReference("Daily Checking");
        daily.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists()){

                    NewUserChecking(daily);

                }else{
                    CheckingUser(daily);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void NewUserChecking(DatabaseReference daily){

        TodayDateString=simpleDateFormat.format(currentDate);
        DailyChecking dailyChecking=new DailyChecking(TodayDateString);
        daily.child(uid)
                .setValue(dailyChecking)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            UpdatePoint();
                        }else {
                            Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });



    }
    //checking
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void CheckingUser(DatabaseReference daily){

        TodayDateString=simpleDateFormat.format(currentDate);
        daily.child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            DailyChecking dailyChecking = snapshot.getValue(DailyChecking.class);

                            try {


                                Date dbDate=simpleDateFormat.parse(dailyChecking.getDate());

                                Date nowDate=simpleDateFormat.parse(TodayDateString);

                                if (nowDate.after(dbDate) && nowDate.compareTo(dbDate) !=0){
                                    UpdatePoint();
                                    UpDate();
                                }else{
                                    Toast.makeText(getContext(), "Sorry You Already Claimed ", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }

                            } catch (ParseException e) {
                                progressDialog.dismiss();
                                e.printStackTrace();
                            }


                        }else {
                            Toast.makeText(getContext(),"Error".toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });




    }
    //up point
    private void UpdatePoint(){

        startAppAd.showAd();
        startAppAd.showAd(new AdDisplayListener() {

            @Override
            public void adHidden(Ad ad) {
                upp();
            }
            @Override
            public void adDisplayed(Ad ad) {
                //Toast.makeText(getApplicationContext(), "ads Displayed", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void adClicked(Ad ad) {
                //Toast.makeText(getApplicationContext(), "ads clicked", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void adNotDisplayed(Ad ad) {
                Toast.makeText(getActivity(), "ads not Displayed", Toast.LENGTH_SHORT).show();
            }
        });


    }
    //up time
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void UpDate(){

        TodayDateString=simpleDateFormat.format(currentDate);
        DailyChecking dailyChecking=new DailyChecking(TodayDateString);
        DatabaseReference daily;
        daily=FirebaseDatabase.getInstance().getReference("Daily Checking");
        daily.child(uid)
                .setValue(dailyChecking)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){

                            ///final all done
                            Toast.makeText(getContext(), "Point Added Success", Toast.LENGTH_SHORT).show();


                        }else {
                            Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
    //app configs
    private void AppConfig(){

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("appconig");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AppConig appConig=snapshot.getValue(AppConig.class);

                if (appConig.getServer().equals("off")){
                   AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                   builder.setTitle("Server Is OFF");
                   builder.setIcon(R.drawable.ic_baseline_warning_24);
                   builder.setMessage("Server Is Of Due To Technical Issues Try again After Some Time" );
                   builder.setCancelable(false);
                   builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           exit(0);

                       }
                   });
                   AlertDialog alertDialog=builder.create();
                   alertDialog.show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                handler.postDelayed(this, 500);

            }
        }, 1000);
    }

    private  void checkNet() {

        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo vpn = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            vpn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_VPN);
        }
        NetworkInfo mobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


        if (!vpn.isConnected()) {
            showDialog("Connect Vpn");

        }
    }
    private void showDialog(String msg){
        AlertDialog.Builder  alertDialogBuilder1 = new AlertDialog.Builder(getActivity());
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
    private void upp(){

        mrRef.child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        UserModel userModel=snapshot.getValue(UserModel.class);
                        totalPoint=POINT_CHECKING+userModel.getPoint();
                        //Toast.makeText(getActivity(), String.valueOf(totalPoint), Toast.LENGTH_SHORT).show();

                        HashMap<String,Object> map=new HashMap<>();
                        map.put("point",totalPoint);
                        ///Toast.makeText(getContext(), String.valueOf(totalPoint), Toast.LENGTH_SHORT).show();
                        mrRef.child(uid)
                                .updateChildren(map)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        if (task.isSuccessful()){
                                            Toast.makeText(getContext(), "Point Added Success", Toast.LENGTH_SHORT).show();
                                            //StartAppAd.showAd(getActivity());
                                        }else {
                                            Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                    }
                });



    }



}




































