package com.bank.izbank.Adapters;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bank.izbank.R;
import com.bank.izbank.Sign.SignIn;
import com.bank.izbank.UserInfo.BankAccount;
import com.bank.izbank.UserInfo.User;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


import java.util.ArrayList;
import java.util.List;

import static com.bank.izbank.Sign.SignIn.mainUser;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {


    ArrayList<User> users;
    Activity context;

    public UserAdapter(ArrayList<User> myMovieData,Activity activity) {
        this.users = myMovieData;
        this.context = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.user_admin_card_view,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }



    @NonNull


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User account = users.get(position);
        holder.textviewmoney.setText(account.getName());
        holder.textviewbankno.setText(account.getId());

        holder.photo.setImageBitmap(account.getPhoto());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder buyCryptoPopup=new AlertDialog.Builder(context);
                buyCryptoPopup.setTitle("USER INFO");

                LayoutInflater inflater = context.getLayoutInflater();
                View dialogView= inflater.inflate(R.layout.admin_screen_user_popup, null);
                buyCryptoPopup.setView(dialogView);
                TextView phone= dialogView.findViewById(R.id.text_view_phone_number);
                TextView adres= dialogView.findViewById(R.id.text_view_adres);
                TextView job= dialogView.findViewById(R.id.text_view_job);
                TextView bankcount= dialogView.findViewById(R.id.text_view_bank_account_counter);
                TextView creditcount= dialogView.findViewById(R.id.text_view_credit_card_counter);
                TextView interestRate= dialogView.findViewById(R.id.text_view_interest_rate);

                interestRate.setText("Interest Rate: " + account.getJob().getInterestRate() +"\n" + "Maximum Credit Amount: " + account.getJob().getMaxCreditAmount());
                phone.setText("Phone: +" + account.getPhoneNumber());
                adres.setText("Address: " + account.addressWrite());
                job.setText("Profession: " + account.getJob().getName());
                String tempStr = "Bank Accounts:\n";

                for (int i = 0; i < account.getBankAccounts().size(); i++) {
                    tempStr+= account.getBankAccounts().get(i).getAccountno() + ": " + account.getBankAccounts().get(i).getCash() + "$";
                    if (account.getBankAccounts().size()-1!=i){
                        tempStr+= "\n";
                    }
                }
                String tempCC = "Credit Cards:\n";
                for (int i = 0; i < account.getCreditcards().size(); i++) {
                    tempCC+= account.getCreditcards().get(i).getCreditCardNo() + ": " + account.getCreditcards().get(i).getLimit() + "$";
                    if (account.getCreditcards().size()-1!=i){
                        tempCC+= "\n";
                    }
                }


                bankcount.setText(tempStr);
                creditcount.setText(tempCC);

                buyCryptoPopup.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                buyCryptoPopup.create().show();
            }
        });



    }


    @Override
    public int getItemCount() {
        return users.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textviewbankno;
        TextView textviewmoney;
        ImageView photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textviewbankno = itemView.findViewById(R.id.text_view_bank_account_no);
            textviewmoney = itemView.findViewById(R.id.text_view_bank_account_money);
            photo = itemView.findViewById(R.id.bank_account_ImageView);


        }
    }
}
