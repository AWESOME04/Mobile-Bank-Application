package com.bank.izbank.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bank.izbank.Bill.Bill;
import com.bank.izbank.R;

import java.util.List;

public class BillAdapter extends  RecyclerView.Adapter<BillAdapter.CardViewObjectHolder> {

    private Context mContext;
    private List<Bill> list;

    public BillAdapter(Context mContext, List<Bill> list) {
        this.mContext = mContext;
        this.list = list;
    }



    public class CardViewObjectHolder extends RecyclerView.ViewHolder{

        public TextView textViewBillType;
        public TextView textViewBillAmount;
        public TextView textViewBillDate;
        public CardView cardViewBill;

        public CardViewObjectHolder(View view){

            super(view);
            textViewBillType = view.findViewById(R.id.textView_bill_type);
            textViewBillAmount = view.findViewById(R.id.textView_bill_amount);
            textViewBillDate = view.findViewById(R.id.textView_bill_date);
            cardViewBill = view.findViewById(R.id.cardView_bill);


        }


    }

    @NonNull
    @Override
    public CardViewObjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bill_cardview,parent,false);
        return new CardViewObjectHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewObjectHolder holder, int position) {

        Bill bill = list.get(position);

        holder.textViewBillType.setText(bill.getType());
        holder.textViewBillAmount.setText(bill.getAmount()+" TL");
        String dateStr = bill.getDate().getDay() +"."+ bill.getDate().getMonth() +
                "."+ bill.getDate().getYear();
        holder.textViewBillDate.setText(dateStr);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
