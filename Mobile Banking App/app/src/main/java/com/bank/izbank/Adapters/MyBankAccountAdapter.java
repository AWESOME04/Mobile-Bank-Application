package com.bank.izbank.Adapters;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bank.izbank.R;
import com.bank.izbank.UserInfo.BankAccount;


import java.util.ArrayList;

public class MyBankAccountAdapter extends RecyclerView.Adapter<MyBankAccountAdapter.ViewHolder> {


    ArrayList<BankAccount> MyBankAccounts;
    Activity context;

    public MyBankAccountAdapter(ArrayList<BankAccount> myMovieData,Activity activity) {
        this.MyBankAccounts = myMovieData;
        this.context = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.bank_account_cardview,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }



    @NonNull


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final BankAccount account = MyBankAccounts.get(position);
        holder.textviewmoney.setText("$ " +String.valueOf(account.getCash()));
        holder.textviewbankno.setText(account.getAccountno());




    }

    @Override
    public int getItemCount() {
        return MyBankAccounts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textviewbankno;
        TextView textviewmoney;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textviewbankno = itemView.findViewById(R.id.text_view_bank_account_no);
            textviewmoney = itemView.findViewById(R.id.text_view_bank_account_money);

        }
    }
}
