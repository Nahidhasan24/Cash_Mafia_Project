package com.nahidstudio.cashmafia.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nahidstudio.cashmafia.Models.UserModel;
import com.nahidstudio.cashmafia.Models.Withdraw;
import com.nahidstudio.cashmafia.Models.WithdrawConfigModel;
import com.nahidstudio.cashmafia.Models.WithdrowModel;
import com.nahidstudio.cashmafia.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WithdrawAdapter extends RecyclerView.Adapter<WithdrawAdapter.ViewHolder> {

    List<Withdraw> withdrawListModeels;
    Context context;
    DatabaseReference user;
    FirebaseAuth mAuth;


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
    @SuppressLint("RecyclerView")
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
            Button button=dialog.findViewById(R.id.btn);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    EditText acco,con;
                    acco=dialog.findViewById(R.id.mail_et);
                    con=dialog.findViewById(R.id.pass_et);
                    String account = acco.getText().toString();
                    String contity=con.getText().toString();

                    if (account.isEmpty()){
                        acco.setError("Enter Details");
                        return;
                    } if (contity.isEmpty()){
                        con.setError("Enter Details");
                        return;
                    }


                    ////
                    getpoint(position,account,contity,dialog);



                }
            });
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
    private void getpoint(int pos, String account, String conti, Dialog dialog) {



        mAuth=FirebaseAuth.getInstance();
        user=FirebaseDatabase.getInstance().getReference("Users");
        user.child(mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel=snapshot.getValue(UserModel.class);


                         DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("withdrawcon");
                         databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot snapshot) {
                                 WithdrawConfigModel wc=snapshot.getValue(WithdrawConfigModel.class);

                                 int co=Integer.parseInt(conti);


                                     if (userModel.getPoint() >= wc.getMinimum() && co>=wc.getMinimum()) {

                                         Withdraw withdraw = withdrawListModeels.get(pos);
                                         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                                         Date currentDate = Calendar.getInstance().getTime();
                                         String today = simpleDateFormat.format(currentDate);

                                         WithdrowModel withdrowModel = new WithdrowModel(withdraw.getTitel(), Integer.parseInt(conti), today,account);
                                         DatabaseReference dr = FirebaseDatabase.getInstance().getReference("withdraw");

                                         dr.child(mAuth.getCurrentUser().getUid()+ SystemClock.currentThreadTimeMillis())
                                                 .setValue(withdrowModel)
                                                 .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<Void> task) {

                                                      int update=  userModel.getPoint()-Integer.parseInt(conti);

                                                         HashMap<String, Object> map = new HashMap<>();

                                                         map.put("point",update);

                                                         if (task.isSuccessful()) {

                                                             user.child(mAuth.getCurrentUser().getUid())
                                                                     .updateChildren(map)
                                                                     .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                         @Override
                                                                         public void onComplete(@NonNull Task<Void> task) {
                                                                             if (task.isSuccessful()) {
                                                                                 Toast.makeText(context, "Your Request In Progress...", Toast.LENGTH_LONG).show();
                                                                                 dialog.dismiss();
                                                                             }else {
                                                                                 Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                                                 dialog.dismiss();
                                                                             }
                                                                         }
                                                                     });

                                                         }else{
                                                             Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                         }
                                                     }
                                                 });


                                     } else {

                                         Toast.makeText(WithdrawAdapter.this.context, "You Don't Have Enough Point", Toast.LENGTH_LONG).show();
                                     }
                                 }




                             @Override
                             public void onCancelled(@NonNull DatabaseError error) {

                             }
                         });







                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}
