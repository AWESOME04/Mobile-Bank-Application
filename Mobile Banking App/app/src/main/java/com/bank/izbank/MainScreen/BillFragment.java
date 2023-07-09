package com.bank.izbank.MainScreen;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bank.izbank.Bill.Bill;
import com.bank.izbank.Adapters.BillAdapter;
import com.bank.izbank.Bill.ElectricBill;
import com.bank.izbank.Bill.GasBill;
import com.bank.izbank.Bill.InternetBill;
import com.bank.izbank.Bill.PhoneBill;
import com.bank.izbank.Bill.WaterBill;
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

public class BillFragment extends Fragment{

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ArrayList<Bill> list;
    private BillAdapter billAdapter;
    private FloatingActionButton floatingActionButtonBill;
    private Bill bill;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_4,container,false);


    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        floatingActionButtonBill = getView().findViewById(R.id.floatingActionButton_bill);

        toolbar = getView().findViewById(R.id.toolbar_bill);
        toolbar.setTitle("Bill Screen");
        toolbar.setLogo(R.drawable.icon_receipt);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        setHasOptionsMenu(true);

        recyclerView = getView().findViewById(R.id.recyclerView_bill);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = SignIn.mainUser.getUserbills();

        billAdapter = new BillAdapter(getContext(),list);

        recyclerView.setAdapter(billAdapter);

        floatingActionButtonBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText editText = new EditText(getContext());
                editText.setHint("type amount");
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);

                AlertDialog.Builder ad = new AlertDialog.Builder(getContext());

                ad.setTitle("Choose bill which you want pay");
                ad.setIcon(R.drawable.icon_bill);
                ad.setView(editText);

                String[] items = {"Electric","Gas","Internet","Phone","Water"};

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
                        switch (i) {
                            case 0:
                                try {
                                    bill=new ElectricBill();
                                }catch (NumberFormatException e){

                                }
                                break;
                            case 1:
                                try {
                                    bill=new GasBill();
                                }catch (NumberFormatException e){

                                }
                                break;
                            case 2:
                                try {
                                    bill=new InternetBill();
                                }catch (NumberFormatException e){

                                }
                                break;
                            case 3:
                                try {
                                    bill=new PhoneBill();
                                }catch (NumberFormatException e){

                                }
                                break;
                            case 4:
                                try {
                                    bill=new WaterBill();
                                }catch (NumberFormatException e){

                                }
                                break;
                        }

                        try {


                            bill.setAmount(Integer.parseInt(editText.getText().toString()));
                            setDate(bill);


                            payBill(bill);



                        }catch (NumberFormatException e){

                        }



                    }
                });

                ad.create().show();

            }
        });

    }

    public void payBill(Bill bill){

        int max=Integer.MIN_VALUE;
        int index=-1;

        for(int i =0;i<SignIn.mainUser.getBankAccounts().size();i++){

            if(SignIn.mainUser.getBankAccounts().get(i).getCash()>max){
                max=SignIn.mainUser.getBankAccounts().get(i).getCash();
                index=i;
            }

        }

        if(SignIn.mainUser.getBankAccounts().size()>0 &&
                bill.getAmount()<=SignIn.mainUser.getBankAccounts().get(index).getCash()){

            SignIn.mainUser.getBankAccounts().get(index).
                    setCash(SignIn.mainUser.getBankAccounts().get(index).getCash()-bill.getAmount());

            updateBankAccount(SignIn.mainUser.getBankAccounts().get(index));

            Toast.makeText(getApplicationContext(),"Bill Paid",Toast.LENGTH_LONG).show();

            billToDatabase(bill);
            list.add(bill);
            billAdapter = new BillAdapter(getContext(),list);


            recyclerView.setAdapter(billAdapter);
            History hs = new History(mainUser.getId(),"Bill Paid.(" + bill.getType()+")",getDateRealTime() );
            mainUser.getHistory().push(hs);
            historyToDatabase(hs);

        }
        else {

            Toast.makeText(getApplicationContext(),"You Don't Have Enough Money",Toast.LENGTH_LONG).show();


        }


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
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage().
                            toString(),Toast.LENGTH_LONG).show();
                }
                else{


                }
            }
        });
    }


    public void billToDatabase(Bill bill){
        ParseObject object=new ParseObject("Bill");
        object.put("type",bill.getType());
        object.put("username", SignIn.mainUser.getId());
        object.put("amount",String.valueOf(bill.getAmount()));
        object.put("date",bill.getDate().getDay()+"/"+bill.getDate().getMonth()+
                "/"+bill.getDate().getYear());

        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){

                }
                else{


                }
            }
        });
    }

    public void setDate(Bill newBill){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date currentTime = Calendar.getInstance().getTime();
        String str = format.format(currentTime);

        String [] date = str.split("/");

        newBill.getDate().setDay(date[0]);
        newBill.getDate().setMonth(date[1]);
        newBill.getDate().setYear(date[2]);

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



}
