package com.nahidstudio.cashmafia.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import com.nahidstudio.cashmafia.Fragments.AboutFragment;
import com.nahidstudio.cashmafia.Fragments.HomeFragment;
import com.nahidstudio.cashmafia.Fragments.WithdrawFragment;
import com.nahidstudio.cashmafia.R;
import com.nahidstudio.cashmafia.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        showfragment(new HomeFragment());
        binding.bottomNav.setOnNavigationItemSelectedListener(item -> {

            if (item.getItemId()==R.id.withdraw){
                showfragment(new WithdrawFragment());
            }else if (item.getItemId()==R.id.home){
                showfragment(new HomeFragment());
            }else if (item.getItemId()==R.id.contactus){

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getString(R.string.facebook)));
                startActivity(i);

            }else if (item.getItemId()==R.id.about){
                showfragment(new AboutFragment());
            }


            return true;
        });




    }
    private void showfragment(Fragment f){
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, f);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


}






















