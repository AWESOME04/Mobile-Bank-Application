package com.bank.izbank.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bank.izbank.Credit.Credit;
import com.bank.izbank.Credit.CustomEventListener;
import com.bank.izbank.R;
import com.bank.izbank.Sign.SignIn;
import com.bank.izbank.UserInfo.BankAccount;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import static com.bank.izbank.Sign.SignIn.mainUser;
import static com.parse.Parse.getApplicationContext;

public class CreditAdapter extends RecyclerView.Adapter<CreditAdapter.CardViewObjectHolder> {

    private Context mContext;
    private List<Credit> list;
    private CustomEventListener listener;

    public void setListener(CustomEventListener listener){
        this.listener=listener;
    }




    public CreditAdapter(Context mContext, List<Credit> list) {
        this.mContext = mContext;
        this.list = list;
    }



    public class CardViewObjectHolder extends RecyclerView.ViewHolder{

        public TextView textViewInstallment;
        public TextView textViewInterestRate;
        public TextView textViewAmount;
        public Button buttonCreditPay;
        public CardView cardViewCredit;

        public CardViewObjectHolder(View view){

            super(view);
            textViewInstallment = view.findViewById(R.id.textView_installment);
            textViewInterestRate = view.findViewById(R.id.textView_interest_rate);
            textViewAmount = view.findViewById(R.id.textView_credit_amount);
            buttonCreditPay = view.findViewById(R.id.buttonCreditPay);
            cardViewCredit = view.findViewById(R.id.cardView_credit);


        }


    }

    @NonNull
    @Override
    public CreditAdapter.CardViewObjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.credit_cardview,parent,false);
        return new CreditAdapter.CardViewObjectHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CreditAdapter.CardViewObjectHolder holder, int position) {

        Credit credit = list.get(position);

        holder.textViewInstallment.setText(String.valueOf(credit.getInstallment())+" months");
        holder.textViewInterestRate.setText("%"+credit.getInterestRate());
        holder.textViewAmount.setText(credit.getPayAmount()+" TL");
        holder.cardViewCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(listener!=null){
                    payCredit(credit,position);
                    listener.MyEventListener();






                }
            }
        });





    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void payCredit(Credit tempCredit,int position){

        int max=Integer.MIN_VALUE;
        int index=-1;

        for(int i =0;i<SignIn.mainUser.getBankAccounts().size();i++){

            if(SignIn.mainUser.getBankAccounts().get(i).getCash()>max){
                max=SignIn.mainUser.getBankAccounts().get(i).getCash();
                index=i;
            }

        }

        if((SignIn.mainUser.getBankAccounts().size()>0) && ((SignIn.mainUser.getBankAccounts().get(index).getCash()) >= (tempCredit.getPayAmount()))){

            SignIn.mainUser.getBankAccounts().get(index).setCash(SignIn.mainUser.getBankAccounts().get(index).getCash()-tempCredit.getPayAmount());

            updateBankAccount(SignIn.mainUser.getBankAccounts().get(index));




            //KREDİYİ DATABASE DEN  VE USERDAN SİLME
            deleteCreditFromDatabase(tempCredit);
            list.remove(position);

            mainUser.setCredits(new ArrayList<Credit>(list));

            Toast.makeText(getApplicationContext(),"Credit Paid",Toast.LENGTH_LONG).show();




        }
        else {




        }


    }

    public void deleteCreditFromDatabase(Credit tempCredit){

        String payAmount = String.valueOf(tempCredit.getPayAmount());
        String installment = String.valueOf(tempCredit.getInstallment());
        String interestRate = String.valueOf(tempCredit.getInterestRate());
        String amount = String.valueOf(tempCredit.getAmount());


        ParseQuery<ParseObject> queryDel2=ParseQuery.getQuery("Credit");
        queryDel2.whereEqualTo("username", SignIn.mainUser.getId());
        queryDel2.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e!=null){
                    e.printStackTrace();
                }else{
                    if(objects.size()>0){
                        for (ParseObject object: objects){

                            if(object.getString("amount").equals(amount) && object.getString("installment").equals(installment) &&
                            object.getString("interestRate").equals(interestRate) && object.getString("payAmount").equals(payAmount)){
                                object.deleteInBackground();
                            }

                        }

                    }
                }
            }
        });

    }

    public void updateBankAccount(BankAccount bankac){
        ParseQuery<ParseObject> queryBankAccount=ParseQuery.getQuery("BankAccount");
        queryBankAccount.whereEqualTo("accountNo", bankac.getAccountno());
        queryBankAccount.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e!=null){
                    e.printStackTrace();
                }else{

                    if(objects.size()>0){
                        for(ParseObject object:objects){
                            object.deleteInBackground();

                            accountsToDatabase(bankac);
                        }
                    }
                }
            }
        });
    }

    public void accountsToDatabase(BankAccount bankAc){
        ParseObject object=new ParseObject("BankAccount");
        object.put("accountNo",bankAc.getAccountno());
        object.put("userId", SignIn.mainUser.getId());

        object.put("cash",String.valueOf(bankAc.getCash()));


        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    //Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                else{


                }
            }
        });
    }
}
