package com.bank.izbank.Adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bank.izbank.R;
import com.bank.izbank.Sign.SignIn;
import com.bank.izbank.UserInfo.BankAccount;
import com.bank.izbank.UserInfo.CreditCard;
import com.bank.izbank.UserInfo.History;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import static com.bank.izbank.Sign.SignIn.mainUser;
import static com.parse.Parse.getApplicationContext;

public class MyCreditCardAdapter extends RecyclerView.Adapter<MyCreditCardAdapter.ViewHolder> {
    ArrayList<BankAccount> MyBankAccounts;
    ArrayList<CreditCard> MyCreditCards;
    Activity context;
    RecyclerView recyclerViewbankaccount;
    TextView text_view_total_money;

    public MyCreditCardAdapter(ArrayList<CreditCard> myCreditCardData,Activity activity,ArrayList<BankAccount> MyBankAccounts,RecyclerView recyclerViewbankaccount) {
        this.MyCreditCards = myCreditCardData;
        this.context = activity;
        this.MyBankAccounts = MyBankAccounts;
        this.recyclerViewbankaccount = recyclerViewbankaccount;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.credit_car_cardview,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @NonNull


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        text_view_total_money = context.findViewById(R.id.text_view_total_money);
        final CreditCard CreditCard = MyCreditCards.get(position);
        holder.textCreditCardNo.setText(CreditCard.getCreditCardNo());
        holder.textCreditCardLimit.setText("$ "+String.valueOf(CreditCard.getLimit()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyBankAccounts.size()==0){
                    AlertDialog.Builder ad = new AlertDialog.Builder(context);
                    ad.setTitle("You dont have any bank account. Please add one before pay off credit card debt.");
                    ad.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i){

                        }
                    });
                    ad.create().show();
                }
                else{
                    final EditText editText = new EditText(context);
                    editText.setHint("How much do you want to pay?");
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);

                    AlertDialog.Builder ad = new AlertDialog.Builder(context);

                    ad.setTitle("Which Bank Account Do You Want to Pay with?");
                    ad.setIcon(R.drawable.icon_credit_card);
                    ad.setView(editText);
                    String[] items = new String[MyBankAccounts.size()];
                    for (int i =0; i<MyBankAccounts.size();i++){
                        String data= MyBankAccounts.get(i).getAccountno() + "  $" + Integer.toString(MyBankAccounts.get(i).getCash());
                        items[i] = data;
                    }
                    final int[] checkedItem = {0};
                    ad.setSingleChoiceItems(items, checkedItem[0], new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (i) {
                                case 0:
                                    checkedItem[0] =i;


                                    break;
                                case 1:

                                    checkedItem[0] =i;

                                    break;
                                case 2:

                                    checkedItem[0] =i;

                                    break;
                                case 3:

                                    checkedItem[0] =i;

                                    break;
                                case 4:

                                    checkedItem[0] =i;
                                    break;
                            }
                        }
                    });
                    ad.setNegativeButton("Pay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            i= checkedItem[0];
                            MyCreditCards.get(position).setLimit(MyCreditCards.get(position).getLimit() + Integer.parseInt(editText.getText().toString()));
                            holder.textCreditCardLimit.setText(String.valueOf(CreditCard.getLimit()));
                            MyBankAccounts.get(i).setCash(MyBankAccounts.get(i).getCash() -Integer.parseInt(editText.getText().toString()));
                            updateBankAccount(MyBankAccounts.get(i));
                            updateCreditCards(MyCreditCards.get(position));
                            setTotalMoney(MyBankAccounts);
                            MyBankAccountAdapter myBankAccountAdapter = new MyBankAccountAdapter(MyBankAccounts,context );
                            recyclerViewbankaccount.setAdapter(myBankAccountAdapter);
                            History hs = new History(mainUser.getId(),"Credit Card Paid.",getDate() );
                            mainUser.getHistory().push(hs);
                            historyToDatabase(hs);



                        }
                    });
                    ad.create().show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return MyCreditCards.size();
    }
    public void setTotalMoney(ArrayList<BankAccount> MyBankAccounts){
        int totalmoney = 0;
        for (int i = 0; i<MyBankAccounts.size();i++){
            totalmoney += MyBankAccounts.get(i).getCash();
        }
        text_view_total_money.setText(Integer.toString(totalmoney));
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
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"banka datada",Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public void cardsToDatabase(CreditCard card){
        ParseObject object=new ParseObject("CreditCard");
        object.put("creditCardNo",card.getCreditCardNo());
        object.put("userId", SignIn.mainUser.getId());

        object.put("limit",String.valueOf(card.getLimit()));


        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"kart datada",Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public void updateCreditCards(CreditCard card){
        ParseQuery<ParseObject> queryBankAccount=ParseQuery.getQuery("CreditCard");
        queryBankAccount.whereEqualTo("creditCardNo", card.getCreditCardNo());
        queryBankAccount.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e!=null){
                    e.printStackTrace();
                }else{

                    if(objects.size()>0){
                        for(ParseObject object:objects){
                            object.deleteInBackground();
                            Toast.makeText(getApplicationContext(),"sildi",Toast.LENGTH_LONG).show();
                            cardsToDatabase(card);





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
                            Toast.makeText(getApplicationContext(),"sildi",Toast.LENGTH_LONG).show();
                            accountsToDatabase(bankac);





                        }


                    }

                }


            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageCreditCard;
        TextView textCreditCardLimit;
        TextView textCreditCardNo;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCreditCard = itemView.findViewById(R.id.image_view_add_credit_card);
            textCreditCardLimit = itemView.findViewById(R.id.text_view_credit_card_limit);
            textCreditCardNo = itemView.findViewById(R.id.text_view_credit_card_no);
            cardView = itemView.findViewById(R.id.card_view_credit_card);

        }
    }
    public void historyToDatabase(History history){
        ParseObject object=new ParseObject("History");
        object.put("process",history.getProcess());
        object.put("userId", mainUser.getId());

        object.put("date",history.getDateDate());


        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"history datada",Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public ArrayList<History> stackToArrayList(Stack<History> stack){
        ArrayList<History> arraylistHistory = new ArrayList<>();
        while (stack.size() !=0){
            arraylistHistory.add(stack.pop());
        }
        for (int i =arraylistHistory.size()-1;i>=0; i-- ) {
            mainUser.getHistory().push(arraylistHistory.get(i));
        }
        return arraylistHistory;
    }
    public Date getDate(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date currentTime = Calendar.getInstance().getTime();
        return currentTime;
    }

}
