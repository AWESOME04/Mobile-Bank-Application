package com.bank.izbank.MainScreen;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bank.izbank.Adapters.HistoryAdapter;
import com.bank.izbank.Adapters.MyBankAccountAdapter;
import com.bank.izbank.Adapters.MyCreditCardAdapter;
import com.bank.izbank.R;
import com.bank.izbank.UserInfo.BankAccount;
import com.bank.izbank.UserInfo.CreditCard;
import com.bank.izbank.Sign.SignIn;
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

public class AccountFragment extends Fragment {
    LinearLayout linear_layout_request_money,linear_layout_send_money, linear_layout_history;
    ImageView add_bank_account, add_credit_card;
    RecyclerView recyclerView;
    RecyclerView recyclerViewbankaccount, recyclerViewHistory;
    TextView text_view_name, date,text_view_total_money;
    ArrayList<CreditCard> myCreditCard;
    ArrayList<BankAccount> myBankAccount;
    BankAccount sendUser = null;
    String bankAccountAnother = null;
    String anotherUserid;
    private HistoryAdapter historyAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_1,container,false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myCreditCard = mainUser.getCreditcards();
        myBankAccount = mainUser.getBankAccounts();

        define();
        setDate();
        click();
        setTotalMoney(myBankAccount);

        text_view_name.setText("HELLO, " + mainUser.getName().toUpperCase()+".");


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerViewbankaccount.setHasFixedSize(true);
        recyclerViewbankaccount.setLayoutManager(new LinearLayoutManager(getActivity()));

        MyCreditCardAdapter myCreditCardAdapter = new MyCreditCardAdapter(myCreditCard,getActivity(),myBankAccount ,recyclerViewbankaccount);
        recyclerView.setAdapter(myCreditCardAdapter);

        MyBankAccountAdapter myBankAccountAdapter = new MyBankAccountAdapter(myBankAccount,getActivity() );
        recyclerViewbankaccount.setAdapter(myBankAccountAdapter);

    }

    public void define(){


        text_view_name = getView().findViewById(R.id.text_view_name);
        date = getView().findViewById(R.id.text_view_date_main);
        recyclerView = getView().findViewById(R.id.recyclerview_credit_card);
        recyclerViewbankaccount = getView().findViewById(R.id.recyclerview_bank_account);
        add_bank_account = getView().findViewById(R.id.image_view_add_bank_account);
        add_credit_card = getView().findViewById(R.id.image_view_add_credit_card);
        linear_layout_request_money = getView().findViewById(R.id.linear_layout_request_money);
        text_view_total_money = getView().findViewById(R.id.text_view_total_money);
        linear_layout_send_money = getView().findViewById(R.id.linear_layout_send_money);
        linear_layout_history = getView().findViewById(R.id.linear_layout_history);

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
        object.put("userId", mainUser.getId());

        object.put("cash",String.valueOf(bankAc.getCash()));


        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                else{


                }
            }
        });
    }

    public void cardsToDatabase(CreditCard card){
        ParseObject object=new ParseObject("CreditCard");
        object.put("creditCardNo",card.getCreditCardNo());
        object.put("userId", mainUser.getId());

        object.put("limit",String.valueOf(card.getLimit()));


        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                else{


                }
            }
        });
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
    public void updateBankAccountAnotherUser(BankAccount bankac, String userId){
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

                            accountsToDatabaseAnotherUser(bankac,userId);
                        }
                    }
                }
            }
        });
    }
    public void accountsToDatabaseAnotherUser(BankAccount bankAc,String userId){
        ParseObject object=new ParseObject("BankAccount");
        object.put("accountNo",bankAc.getAccountno());
        object.put("userId", userId);

        object.put("cash",String.valueOf(bankAc.getCash()));


        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                else{


                }
            }
        });
    }


    public void click(){
        linear_layout_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder history_popup=new AlertDialog.Builder(getContext());

                history_popup.setTitle("HISTORY");

                history_popup.setView(R.layout.history_popup);
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView= inflater.inflate(R.layout.history_popup, null);
                history_popup.setView(dialogView);
                recyclerViewHistory =(RecyclerView)dialogView.findViewById(R.id.history_recycler_view);
                recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getActivity()));

                historyAdapter=new HistoryAdapter(stackToArrayList(mainUser.getHistory()),getActivity(),getContext());
                recyclerViewHistory.setAdapter(historyAdapter);
                historyAdapter.notifyDataSetChanged();
                history_popup.create().show();
            }
        });
        add_bank_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myBankAccount.size()>=5){
                    Toast.makeText(getContext(), "YOU CANT ADD MORE THAN 5 BANK ACCOUNT", Toast.LENGTH_LONG).show();
                }
                else{
                    final EditText editText = new EditText(getContext());
                    editText.setHint("0");
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
                    ad.setTitle("How Much Money Do You Want?");
                    ad.setIcon(R.drawable.icon_save_money);
                    ad.setView(editText);
                    ad.setNegativeButton("ADD", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            try {

                                myBankAccount.add(new BankAccount(Integer.parseInt(editText.getText().toString())));
                                MyBankAccountAdapter myBankAccountAdapter = new MyBankAccountAdapter(myBankAccount,getActivity() );
                                recyclerViewbankaccount.setAdapter(myBankAccountAdapter);
                                setTotalMoney(myBankAccount);


                            }catch (NumberFormatException e){
                                myBankAccount.add(new BankAccount(0));
                                MyBankAccountAdapter myBankAccountAdapter = new MyBankAccountAdapter(myBankAccount,getActivity() );
                                recyclerViewbankaccount.setAdapter(myBankAccountAdapter);
                                setTotalMoney(myBankAccount);

                            }
                            accountsToDatabase(myBankAccount.get(myBankAccount.size()-1));
                            History hs = new History(mainUser.getId(),"New Bank Account Added.",getDate() );
                            mainUser.getHistory().push(hs);
                            historyToDatabase(hs);






                        }
                    });
                    ad.create().show();

                }

            }
        });
        add_credit_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myCreditCard.size()>=5){
                    Toast.makeText(getContext(), "YOU CANT ADD MORE THAN 5 CREDIT CARD", Toast.LENGTH_LONG).show();
                }
                else{


                    final EditText editText = new EditText(getContext());
                    editText.setHint("0");
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
                    ad.setTitle("How Much Credit Card Limit Do You Want?");
                    ad.setIcon(R.drawable.icon_credit_card);
                    ad.setView(editText);
                    ad.setNegativeButton("ADD", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                myCreditCard.add(new CreditCard(Integer.parseInt(editText.getText().toString())));
                                MyCreditCardAdapter myCreditCardAdapter = new MyCreditCardAdapter(myCreditCard,getActivity(),myBankAccount ,recyclerViewbankaccount);
                                recyclerView.setAdapter(myCreditCardAdapter);


                            }catch (NumberFormatException e){
                                myCreditCard.add(new CreditCard(0));
                                MyCreditCardAdapter myCreditCardAdapter = new MyCreditCardAdapter(myCreditCard,getActivity(),myBankAccount ,recyclerViewbankaccount);
                                recyclerView.setAdapter(myCreditCardAdapter);

                            }
                            cardsToDatabase(myCreditCard.get(myCreditCard.size()-1));
                            History hs = new History(mainUser.getId(),"New Credit Card Added.",getDate() );
                            mainUser.getHistory().push(hs);
                            historyToDatabase(hs);

                        }
                    });
                    ad.create().show();

                }


            }
        });
        linear_layout_request_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myBankAccount.size()==0){
                    AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
                    ad.setTitle("You dont have any bank account. Please add one before request.");
                    ad.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i){

                        }
                    });
                    ad.create().show();
                }
                else{
                    final EditText editText = new EditText(getContext());
                    editText.setHint("How Much Do You Want to Request?");
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);

                    AlertDialog.Builder ad = new AlertDialog.Builder(getContext());

                    ad.setTitle("Which Bank Account Do You Want to Request?");
                    ad.setIcon(R.drawable.icon_credit_card);
                    ad.setView(editText);
                    String[] items = new String[myBankAccount.size()];
                    for (int i =0; i<myBankAccount.size();i++){
                        String data= myBankAccount.get(i).getAccountno() + "  $" + Integer.toString(myBankAccount.get(i).getCash());
                        items[i] = data;
                    }
                    final int[] checkedItem = {0};
                    ad.setSingleChoiceItems(items, checkedItem[0], new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            checkedItem[0] =i;
                        }
                    });
                    ad.setNegativeButton("Request", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            i= checkedItem[0];
                            myBankAccount.get(i).setCash(myBankAccount.get(i).getCash() + Integer.parseInt(editText.getText().toString()));
                            updateBankAccount(myBankAccount.get(i));
                            MyBankAccountAdapter myBankAccountAdapter = new MyBankAccountAdapter(myBankAccount,getActivity() );
                            recyclerViewbankaccount.setAdapter(myBankAccountAdapter);
                            setTotalMoney(myBankAccount);
                            History hs = new History(mainUser.getId(),"Money Requested.",getDate() );
                            mainUser.getHistory().push(hs);
                            historyToDatabase(hs);



                        }
                    });
                    ad.create().show();

                }

            }
        });

        linear_layout_send_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
                ad.setTitle("What Do You Want to Do?");
                ad.setIcon(R.drawable.icon_save_money);
                String arr[] = {("SEND MONEY TO ONE OF YOUR ACCOUNTS") , "SEND ANOTHER PERSON"};
                ad.setItems(arr, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                AlertDialog.Builder ad2 = new AlertDialog.Builder(getContext());

                                ad2.setTitle("Which Bank Account Do You Want to Send From?");
                                ad2.setIcon(R.drawable.icon_save_money);

                                String[] items = new String[myBankAccount.size()];
                                for (int i =0; i<myBankAccount.size();i++){
                                    String data= myBankAccount.get(i).getAccountno() + "  $" + Integer.toString(myBankAccount.get(i).getCash());
                                    items[i] = data;
                                }
                                final int[] from = {0};
                                ad2.setSingleChoiceItems(items, from[0], new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        from[0] =i;
                                    }
                                });
                                ad2.setNegativeButton("CONTINUE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AlertDialog.Builder ad3 = new AlertDialog.Builder(getContext());

                                        ad3.setTitle("Which Bank Account Do You Want to Send to?");
                                        ad3.setIcon(R.drawable.icon_save_money);

                                        String[] items = new String[myBankAccount.size()];
                                        for (int i =0; i<myBankAccount.size();i++){
                                            String data= myBankAccount.get(i).getAccountno() + "  $" + Integer.toString(myBankAccount.get(i).getCash());
                                            items[i] = data;
                                        }
                                        final int[] to = {0};
                                        ad3.setSingleChoiceItems(items, to[0], new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                to[0] =i;
                                            }
                                        });
                                        ad3.setNegativeButton("CONTINUE", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                final EditText editText = new EditText(getContext());
                                                editText.setHint("0");
                                                editText.setInputType(InputType.TYPE_CLASS_NUMBER);

                                                AlertDialog.Builder ad4 = new AlertDialog.Builder(getContext());

                                                ad4.setTitle("How Much Do You Want To Send?");
                                                ad4.setIcon(R.drawable.icon_credit_card);
                                                ad4.setView(editText);
                                                ad4.setNegativeButton("CONTINUE", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        try {
                                                            int toint = to[0];
                                                            int fromint = from[0];
                                                            if (Integer.parseInt(editText.getText().toString())<= myBankAccount.get(fromint).getCash()){
                                                                myBankAccount.get(toint).setCash(myBankAccount.get(toint).getCash()+Integer.parseInt(editText.getText().toString()));
                                                                myBankAccount.get(fromint).setCash(myBankAccount.get(fromint).getCash() - Integer.parseInt(editText.getText().toString()));
                                                                MyBankAccountAdapter myBankAccountAdapter = new MyBankAccountAdapter(myBankAccount,getActivity() );
                                                                recyclerViewbankaccount.setAdapter(myBankAccountAdapter);
                                                                updateBankAccount(myBankAccount.get(fromint));
                                                                updateBankAccount(myBankAccount.get(toint));
                                                                History hs = new History(mainUser.getId(),"Money Sent to Your Account.",getDate() );
                                                                mainUser.getHistory().push(hs);
                                                                historyToDatabase(hs);

                                                            }
                                                            else{
                                                                Toast.makeText(getApplicationContext(),"You don't have enough money.",Toast.LENGTH_LONG).show();
                                                            }
                                                        }catch (NumberFormatException e){

                                                        }

                                                    }
                                                });
                                                ad4.create().show();

                                            }
                                        });
                                        ad3.create().show();
                                    }
                                });
                                ad2.create().show();
                                break;

                            case 1:

                                final EditText editText = new EditText(getContext());
                                editText.setHint("Bank Account No");
                                editText.setInputType(InputType.TYPE_CLASS_NUMBER);

                                AlertDialog.Builder typeAccountNo = new AlertDialog.Builder(getContext());

                                typeAccountNo.setTitle("Type Account No Which you want to send.");
                                typeAccountNo.setIcon(R.drawable.icon_save_money);
                                typeAccountNo.setView(editText);
                                typeAccountNo.setNegativeButton("CONTINUE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ParseQuery<ParseObject>  query=ParseQuery.getQuery("BankAccount");
                                        query.whereEqualTo("accountNo",editText.getText().toString());
                                        bankAccountAnother = editText.getText().toString();
                                        query.findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(List<ParseObject> objects, ParseException e) {
                                                if(e!=null){
                                                    e.printStackTrace();
                                                }else{
                                                    if(objects.size()>0){
                                                        for(ParseObject object:objects){

                                                            anotherUserid = object.getString("userId");
                                                            String bankno=object.getString("accountNo");
                                                            String cash = object.getString("cash");
                                                            sendUser = new BankAccount(bankno, Integer.parseInt(cash));

                                                            break;

                                                        }



                                                    }
                                                    else {
                                                        Toast.makeText(getApplicationContext(),"Invalid Bank No",Toast.LENGTH_LONG).show();
                                                    }
                                                }

                                            }
                                        });

                                        AlertDialog.Builder adFrom = new AlertDialog.Builder(getContext());

                                        adFrom.setTitle("Which Bank Account Do You Want to Send From?");
                                        adFrom.setIcon(R.drawable.icon_save_money);

                                        String[] items = new String[myBankAccount.size()];
                                        for (int i =0; i<myBankAccount.size();i++){
                                            String data= myBankAccount.get(i).getAccountno() + "  $" + Integer.toString(myBankAccount.get(i).getCash());
                                            items[i] = data;
                                        }
                                        final int[] from = {0};
                                        adFrom.setSingleChoiceItems(items, from[0], new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                from[0] =i;
                                            }
                                        });
                                        adFrom.setNegativeButton("CONTINUE", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                final EditText editText = new EditText(getContext());
                                                editText.setHint("0");
                                                editText.setInputType(InputType.TYPE_CLASS_NUMBER);

                                                AlertDialog.Builder adTypeMoney = new AlertDialog.Builder(getContext());

                                                adTypeMoney.setTitle("How Much Do You Want To Send?");
                                                adTypeMoney.setIcon(R.drawable.icon_credit_card);
                                                adTypeMoney.setView(editText);
                                                adTypeMoney.setNegativeButton("CONTINUE", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if (sendUser !=null){
                                                            if (myBankAccount.get(from[0]).getCash()>=Integer.parseInt(editText.getText().toString())){
                                                                sendUser.setCash(sendUser.getCash() + Integer.parseInt(editText.getText().toString()));
                                                                myBankAccount.get(from[0]).setCash(myBankAccount.get(from[0]).getCash()- Integer.parseInt(editText.getText().toString()));
                                                                updateBankAccountAnotherUser(sendUser, anotherUserid);
                                                                updateBankAccount(myBankAccount.get(from[0]));
                                                                Toast.makeText(getApplicationContext(),"Sent",Toast.LENGTH_LONG).show();
                                                                MyBankAccountAdapter myBankAccountAdapter = new MyBankAccountAdapter(myBankAccount,getActivity() );
                                                                recyclerViewbankaccount.setAdapter(myBankAccountAdapter);
                                                                History hs = new History(mainUser.getId(),"Money Sent to Another User.",getDate() );
                                                                mainUser.getHistory().push(hs);
                                                                historyToDatabase(hs);
                                                            }
                                                            else {
                                                                Toast.makeText(getApplicationContext(),"You don't have enough money.",Toast.LENGTH_LONG).show();
                                                            }

                                                        }
                                                        else{
                                                            Toast.makeText(getApplicationContext(),"Invalid Id",Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                                adTypeMoney.create().show();
                                            }
                                        });
                                        adFrom.create().show();

                                    }
                                });
                                typeAccountNo.create().show();


                        }
                    }
                });
                ad.create().show();
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

    public void setDate(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date currentTime = Calendar.getInstance().getTime();
        date.setText(format.format(currentTime));

    }
    public Date getDate(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date currentTime = Calendar.getInstance().getTime();
        return currentTime;
    }



}
