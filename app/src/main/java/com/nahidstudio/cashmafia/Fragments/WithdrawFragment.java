package com.nahidstudio.cashmafia.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nahidstudio.cashmafia.Adapters.WithdrawAdapter;
import com.nahidstudio.cashmafia.Models.Withdraw;
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

        return binding.getRoot();
    }
    private void SetList(){
        withdrawArrayList.add(new Withdraw(R.drawable.ttu,"Recharge","0.13"));
        withdrawArrayList.add(new Withdraw(R.drawable.bk,"Bkash","0.6"));
        withdrawArrayList.add(new Withdraw(R.drawable.nagad,"Nagad","0.6"));
        withdrawArrayList.add(new Withdraw(R.drawable.rocket,"Rocket","5"));
        withdrawArrayList.add(new Withdraw(R.drawable.ff,"Free Fire","5"));
        withdrawArrayList.add(new Withdraw(R.drawable.coniebase,"CoinBase","5"));
        withdrawArrayList.add(new Withdraw(R.drawable.paypal,"Paypal","5"));
        withdrawArrayList.add(new Withdraw(R.drawable.pbb,"Pubg","5"));



    }
}




























