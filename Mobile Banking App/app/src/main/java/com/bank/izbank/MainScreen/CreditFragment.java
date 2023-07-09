package com.bank.izbank.MainScreen;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bank.izbank.Credit.Credit;
import com.bank.izbank.Adapters.CreditAdapter;
import com.bank.izbank.Credit.CustomEventListener;
import com.bank.izbank.R;
import com.bank.izbank.Sign.SignIn;
import com.bank.izbank.UserInfo.BankAccount;
import com.bank.izbank.UserInfo.History;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import static com.bank.izbank.Sign.SignIn.mainUser;
import static com.parse.Parse.getApplicationContext;

public class CreditFragment extends Fragment {

    private Toolbar toolbarCredit;
    private RecyclerView recyclerViewCredit;
    private ArrayList<Credit> list,searchList;
    private CreditAdapter creditAdapter;
    private FloatingActionButton floatingActionButtonCredit;
    private Credit credit;
    private AdapterView.OnItemClickListener mListener;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_2,container,false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        floatingActionButtonCredit = getView().findViewById(R.id.floatingActionButton_credit);

        toolbarCredit = getView().findViewById(R.id.toolbar_credit);
        toolbarCredit.setTitle("Credit Screen");
        toolbarCredit.setLogo(R.drawable.icon_credit);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbarCredit);

        setHasOptionsMenu(true);
        recyclerViewCredit = getView().findViewById(R.id.recyclerView_credit);
        recyclerViewCredit.setHasFixedSize(true);
        recyclerViewCredit.setLayoutManager(new LinearLayoutManager(getContext()));

        list = SignIn.mainUser.getCredits();

        //creditAdapter = new CreditAdapter(getContext(),list);
        creditAdapter = new CreditAdapter(getContext(),list);
        creditAdapter.setListener(new CustomEventListener() {
            @Override
            public void MyEventListener() {


                list= mainUser.getCredits();
                creditAdapter = new CreditAdapter(getContext(),list);
                creditAdapter.setListener(new CustomEventListener() {
                    @Override
                    public void MyEventListener() {


                        list= mainUser.getCredits();
                        creditAdapter = new CreditAdapter(getContext(),list);


                        recyclerViewCredit.setAdapter(creditAdapter);
                    }
                });


                recyclerViewCredit.setAdapter(creditAdapter);
            }
        });

        recyclerViewCredit.setAdapter(creditAdapter);

        floatingActionButtonCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // kredi çekme alertdialog

                AlertDialog.Builder creditPopup = new AlertDialog.Builder(getContext());
                creditPopup.setTitle("Set Credit");
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView= inflater.inflate(R.layout.credit_screen_credit_first_step_popup, null);
                creditPopup.setView(dialogView);
                EditText creditAmount = dialogView.findViewById(R.id.editText_credit_amount) ;
                TextView interestRate = dialogView.findViewById(R.id.textView_credit_interestRate);
                EditText installment = dialogView.findViewById(R.id.editText_credit_installment);

                TextView staticMaxAmount = dialogView.findViewById(R.id.textView_static_credit_max_amount);
                TextView staticMaxInstallment = dialogView.findViewById(R.id.textView_static_credit_max_installment);

                interestRate.setText("Rate: %"+SignIn.mainUser.getJob().getInterestRate());
                staticMaxAmount.setText(SignIn.mainUser.getJob().getMaxCreditAmount());
                staticMaxInstallment.setText(SignIn.mainUser.getJob().getMaxCreditInstallment());



                creditPopup.setNegativeButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                       if(!(creditAmount.getText().toString().matches("") || installment.getText().toString().matches(""))){



                           int maxAmount = Integer.parseInt(SignIn.mainUser.getJob().getMaxCreditAmount());
                           int maxInstallment = Integer.parseInt(SignIn.mainUser.getJob().getMaxCreditInstallment());
                           int currentAmount = Integer.parseInt(creditAmount.getText().toString());
                           int currentInstallment = Integer.parseInt(installment.getText().toString());

                           if(maxAmount>=currentAmount && maxInstallment>=currentInstallment){
                               // yeni popup

                               AlertDialog.Builder creditPopupSecond = new AlertDialog.Builder(getContext());
                               creditPopupSecond.setTitle("Check Credit");
                               LayoutInflater inflaterSecond = getActivity().getLayoutInflater();
                               View dialogViewSecond= inflaterSecond.inflate(R.layout.credit_screen_credit_second_step_popup, null);
                               creditPopupSecond.setView(dialogViewSecond);

                               TextView amountSecond = dialogViewSecond.findViewById(R.id.textView_taken_amount);
                               TextView payAmountSecond = dialogViewSecond.findViewById(R.id.textView_pay_amount);

                               amountSecond.setText(String.valueOf(currentAmount));
                               payAmountSecond.setText(String.valueOf(currentAmount + ((currentAmount/Integer.parseInt(SignIn.mainUser.getJob().getInterestRate()))*currentInstallment)/1200));

                               creditPopupSecond.setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialogInterface, int i) {

                                       // database vesaire


                                       int payCurrentAmount = currentAmount + ((currentAmount*Integer.parseInt(SignIn.mainUser.getJob().getInterestRate()))*currentInstallment)/1200;

                                       Credit tempCredit = new Credit(currentAmount,currentInstallment,Integer.parseInt(SignIn.mainUser.getJob().getInterestRate()),payCurrentAmount);


                                       receiveCredit(tempCredit);



                                   }
                               });

                               creditPopupSecond.create().show();

                           }
                           else {
                               Toast.makeText(getContext(), "Type proper values!", Toast.LENGTH_SHORT).show();
                           }



                       }
                       else {

                           Toast.makeText(getContext(), "Type proper values!", Toast.LENGTH_SHORT).show();

                       }



                    }
                });





                creditPopup.create().show();
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

    public void creditToDatabase(Credit tempCredit){
        ParseObject object=new ParseObject("Credit");
        object.put("amount",String.valueOf(tempCredit.getAmount()));
        object.put("username", SignIn.mainUser.getId());
        object.put("installment",String.valueOf(tempCredit.getInstallment()));
        object.put("interestRate",String.valueOf(tempCredit.getInterestRate()));
        object.put("payAmount",String.valueOf(tempCredit.getPayAmount()));

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
    public java.util.Date getDateRealTime(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date currentTime = Calendar.getInstance().getTime();
        return currentTime;
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

    public void receiveCredit(Credit tempCredit){

        int max=Integer.MIN_VALUE;
        int index=-1;

        for(int i =0;i<SignIn.mainUser.getBankAccounts().size();i++){

            if(SignIn.mainUser.getBankAccounts().get(i).getCash()>max){
                max=SignIn.mainUser.getBankAccounts().get(i).getCash();
                index=i;
            }

        }

        if(SignIn.mainUser.getBankAccounts().size()>0 ){

            SignIn.mainUser.getBankAccounts().get(index).setCash(SignIn.mainUser.getBankAccounts().get(index).getCash()+tempCredit.getAmount());

            updateBankAccount(SignIn.mainUser.getBankAccounts().get(index));

            Toast.makeText(getApplicationContext(),"Credit Received!",Toast.LENGTH_LONG).show();

            creditToDatabase(tempCredit);
            list.add(tempCredit);
            creditAdapter = new CreditAdapter(getContext(),list);
            creditAdapter.setListener(new CustomEventListener() {
                @Override
                public void MyEventListener() {


                    list= mainUser.getCredits();
                    creditAdapter = new CreditAdapter(getContext(),list);
                    creditAdapter.setListener(new CustomEventListener() {
                        @Override
                        public void MyEventListener() {


                            list= mainUser.getCredits();
                            creditAdapter = new CreditAdapter(getContext(),list);


                            recyclerViewCredit.setAdapter(creditAdapter);
                        }
                    });


                    recyclerViewCredit.setAdapter(creditAdapter);
                }
            });


            recyclerViewCredit.setAdapter(creditAdapter);
            History hs = new History(mainUser.getId(),"Credit Received.(" + tempCredit.getAmount()+")",getDateRealTime() );
            mainUser.getHistory().push(hs);
            historyToDatabase(hs);

        }
        else {

            Toast.makeText(getApplicationContext(),"Don't Have Bank Account!",Toast.LENGTH_LONG).show();


        }


    }




}
