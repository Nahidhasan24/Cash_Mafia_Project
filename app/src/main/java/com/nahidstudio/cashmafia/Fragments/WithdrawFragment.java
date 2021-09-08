package com.nahidstudio.cashmafia.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nahidstudio.cashmafia.Adapters.WithdrawAdapter;
import com.nahidstudio.cashmafia.Models.UserModel;
import com.nahidstudio.cashmafia.Models.Withdraw;
import com.nahidstudio.cashmafia.Models.WithdrawConfigModel;
import com.nahidstudio.cashmafia.R;
import com.nahidstudio.cashmafia.databinding.FragmentWithdrawBinding;

import java.util.ArrayList;

public class WithdrawFragment extends Fragment {

    ArrayList<Withdraw> withdrawArrayList=new ArrayList<>();
    FragmentWithdrawBinding binding;
    Withdraw withdraw;
    WithdrawAdapter withdrawAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        SetList();
        binding= FragmentWithdrawBinding.inflate(inflater, container, false);
        binding.withdrawRecycler.setLayoutManager(new GridLayoutManager(getContext(),3));
        withdrawAdapter=new WithdrawAdapter(withdrawArrayList,getContext());
        binding.withdrawRecycler.setAdapter(withdrawAdapter);
        getdata();

        return binding.getRoot();
    }
    private void SetList(){
        withdrawArrayList.add(new Withdraw(R.drawable.ttu,"Recharge","300tk"));
        withdrawArrayList.add(new Withdraw(R.drawable.bk,"Bkash","300tk"));
        withdrawArrayList.add(new Withdraw(R.drawable.nagad,"Nagad","300tk"));
        withdrawArrayList.add(new Withdraw(R.drawable.rocket,"Rocket","300tk"));
        withdrawArrayList.add(new Withdraw(R.drawable.ff,"Free Fire","3.62$"));
        withdrawArrayList.add(new Withdraw(R.drawable.coniebase,"CoinBase","3.62$"));
        withdrawArrayList.add(new Withdraw(R.drawable.paypal,"Paypal","3.62$"));
        withdrawArrayList.add(new Withdraw(R.drawable.pbb,"Pubg","3.62$"));



    }

    private void getdata(){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("withdrawcon");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                WithdrawConfigModel wc =snapshot.getValue(WithdrawConfigModel.class);
                binding.withdrawMessage.setText(wc.getMessage());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }





}




























