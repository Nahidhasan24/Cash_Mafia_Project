package com.nahidstudio.cashmafia.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nahidstudio.cashmafia.Models.Withdraw;
import com.nahidstudio.cashmafia.R;

import java.util.List;

public class WithdrawAdapter extends RecyclerView.Adapter<WithdrawAdapter.ViewHolder> {

    List<Withdraw> withdrawListModeels;
    Context context;

    public WithdrawAdapter(List<Withdraw> withdrawListModeels, Context context) {
        this.withdrawListModeels = withdrawListModeels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.withdraw_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Withdraw withdraw=withdrawListModeels.get(position);
        holder.imageView.setImageResource(withdraw.getImage());
        holder.textView.setText(withdraw.getTitel());
        holder.point.setText(withdraw.getPoint());

        holder.itemView.setOnClickListener(v->{

            Dialog dialog=new Dialog(context);
            dialog.setContentView(R.layout.cd);
            dialog.show();
        });

    }

    @Override
    public int getItemCount() {
        return withdrawListModeels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView,point;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image_withdraw);
            textView=itemView.findViewById(R.id.text_withdraw);
            point=itemView.findViewById(R.id.text_point);

        }
    }
}
