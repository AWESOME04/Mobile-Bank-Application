package com.bank.izbank.MainScreen.FinanceScreen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bank.izbank.Adapters.CryptoLookAdapter;
import com.bank.izbank.Adapters.CryptoPostAdapter;
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
import java.util.Stack;

import static com.bank.izbank.Sign.SignIn.mainUser;
import static com.parse.Parse.getApplicationContext;

public class FinanceFragment extends Fragment implements SearchView.OnQueryTextListener {
        //exchange https://nomics.com/docs/#tag/Exchange-Rates
        //https://api.nomics.com/v1/exchange-rates?key=c5d0683b83dc6e2dbd00841b72f7c86c
    private ArrayList<CryptoModel> cryptoModels, searchList,ownCryptos;
    private RecyclerView recyclerView,ownCryptoPopup;
    private CryptoPostAdapter cryptoPostAdapter ,searchAdapter;
    private ItemClickListener itemClickListener;
    private Toolbar toolbar;
    private String decMoney;
    private String currentMoney;
    private FloatingActionButton floatingActionButtonCrypto;
    private CryptoModel buyedCrypto;
    private String buyedCrptoAmount;
    private CryptoLookAdapter cryptoLookPopup;
    private  int index;

    public FinanceFragment(ArrayList<CryptoModel> list){
        this.cryptoModels=list;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.finance_fragment,container,false);


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = getView().findViewById(R.id.toolbar);
        toolbar.setTitle("Search ");
        toolbar.setLogo(R.drawable.icons_bitcoin);
        floatingActionButtonCrypto = getView().findViewById(R.id.floatingActionButton_crypto);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        ownCryptos=new ArrayList<>();
        cryptoDatabase();
        recyclerView=view.findViewById(R.id.recyclerView_finance);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        itemClickListener=new ItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView.ViewHolder vh, CryptoModel item, int pos) {
                //popup menu

                AlertDialog.Builder buyCryptoPopup=new AlertDialog.Builder(getContext());
                buyCryptoPopup.setTitle("Crypto Currency Buy");
                Drawable d = item.getImage().getDrawable();
                buyCryptoPopup.setIcon(d);
                buyedCrypto=cryptoModels.get(pos);
              //  buyCryptoPopup.setView(R.layout.finance_screen_cryto_buy_popup);
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView= inflater.inflate(R.layout.finance_screen_cryto_buy_popup, null);
                buyCryptoPopup.setView(dialogView);
                TextView name= dialogView.findViewById(R.id.CrytoNameTextView);
                TextView value= dialogView.findViewById(R.id.CryptovalueTextView);
                TextView amount= dialogView.findViewById(R.id.CryptoAmountTextView);
                TextView cryptoSymbol= dialogView.findViewById(R.id.CrytoSymbolTextView);
                EditText buyAmount= dialogView.findViewById(R.id.CyrptoBuyAmountEditText);
                name.setText(item.getCurrencyName());
                value.setText(item.getPrice());
                cryptoSymbol.setText(item.getCurrencySymbol());


                buyAmount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if(s.toString().equals("")){
                            amount.setText("0.0000");
                        }else{
                           buyedCrptoAmount= String.valueOf((double) (Integer.parseInt(s.toString()) / Double.parseDouble(item.getPrice())));
                            amount.setText(buyedCrptoAmount);
                            decMoney=s.toString();
                        }

                    }
                });

                buyCryptoPopup.setPositiveButton("Buy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        buyCrypto(Integer.parseInt(decMoney),buyedCrptoAmount, buyedCrypto);


                    }
                });
                buyCryptoPopup.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
                    }
                });
                buyCryptoPopup.create().show();

            }
        };

        cryptoPostAdapter = new CryptoPostAdapter(cryptoModels, getActivity(), getContext(), itemClickListener);

        recyclerView.setAdapter(cryptoPostAdapter);



        floatingActionButtonCrypto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder buyCryptoPopup=new AlertDialog.Builder(getContext());

                buyCryptoPopup.setTitle("Crypto Currency Buy");

                buyCryptoPopup.setView(R.layout.finance_screen_look_own_crypto_popup);
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView= inflater.inflate(R.layout.finance_screen_look_own_crypto_popup, null);
                buyCryptoPopup.setView(dialogView);
                ownCryptoPopup =(RecyclerView)dialogView.findViewById(R.id.recyclerView_finance_look);
                ownCryptoPopup.setLayoutManager(new LinearLayoutManager(getActivity()));

                cryptoLookPopup=new CryptoLookAdapter(ownCryptos,getActivity(),getContext());
                ownCryptoPopup.setAdapter(cryptoLookPopup);
                cryptoLookPopup.notifyDataSetChanged();
                buyCryptoPopup.create().show();
            }
        });
        cryptoPostAdapter.notifyDataSetChanged();


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
       inflater.inflate(R.menu.crypto_search,menu);
        MenuItem menuItem=menu.findItem(R.id.search_view);
        SearchView searchView=(SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
    }
    public void toolbarSearch(String searchWord){

         searchList = searchCrypto(cryptoModels, searchWord);

        searchAdapter = new CryptoPostAdapter(searchList,getActivity(),getContext(),itemClickListener);
        recyclerView.setAdapter(searchAdapter);

    }

    public ArrayList<CryptoModel> searchCrypto(ArrayList<CryptoModel> list, String string){

        ArrayList<CryptoModel> returnList = new ArrayList<>();

        if(string.equals("")){
            return list;
        }
        else {
            for(CryptoModel cryptoModel: list){
                if( cryptoModel.getCurrencyName().toLowerCase().contains(string.toLowerCase())
                        || String.valueOf(cryptoModel.getPrice()).toLowerCase().contains(string.toLowerCase()) || cryptoModel.getCurrencySymbol().toLowerCase().contains(string.toLowerCase())){
                    returnList.add(cryptoModel);
                }

            }
            return returnList;
        }




    }
        public void buyCrypto(int decMoney,String buyedCrptoAmount,CryptoModel buyedCrypto) {
            index = -1;
            for (BankAccount bankAc : mainUser.getBankAccounts()) {
                if (bankAc.getCash() > decMoney) {
                    index = mainUser.getBankAccounts().indexOf(bankAc);
                    break;
                }
            }
            if (index == -1) {
                Toast.makeText(getApplicationContext(), "No Money", Toast.LENGTH_LONG).show();
            } else {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("CryptoAmount");
            query.whereEqualTo("username", mainUser.getId());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e != null) {
                        e.printStackTrace();
                    } else {
                        if (objects.size() > 0) {
                            for (ParseObject object : objects) {
                                ParseObject cryptoBuy = objects.get(0);
                                String cryptoAmount = cryptoBuy.getString(buyedCrypto.getCurrencySymbol());
                                if (cryptoAmount == null) {
                                    cryptoAmount = "0";
                                }

                                cryptoAmount = String.valueOf(Double.parseDouble(cryptoAmount) + Double.parseDouble(buyedCrptoAmount));
                                cryptoBuy.put(buyedCrypto.getCurrencySymbol().toUpperCase(), cryptoAmount);

                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e != null) {
                                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getActivity(), "Buyed", Toast.LENGTH_SHORT).show();
                                            History hs = new History(mainUser.getId(),"Crypto Buyed.("+ buyedCrypto.getCurrencyName()+")",getDate() );
                                            mainUser.getHistory().push(hs);
                                            historyToDatabase(hs);


                                        }
                                    }
                                });


                            }


                        } else {
                            ParseObject object = new ParseObject("CryptoAmount");
                            object.put(buyedCrypto.getCurrencySymbol().toUpperCase(), buyedCrptoAmount);
                            object.put("username", mainUser.getId());
                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Buyed", Toast.LENGTH_LONG).show();
                                        History hs = new History(mainUser.getId(),"Crypto Buyed.(" + buyedCrypto.getCurrencyName()+")",getDate() );
                                        mainUser.getHistory().push(hs);
                                        historyToDatabase(hs);

                                    }
                                }
                            });
                        }

                    }
                }
            });


            ParseQuery<ParseObject> query2 = ParseQuery.getQuery("BankAccount");
            query2.whereEqualTo("accountNo", mainUser.getBankAccounts().get(index).getAccountno());
            query2.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e != null) {
                        e.printStackTrace();
                    } else {
                        if (objects.size() > 0) {
                            for (ParseObject object : objects) {
                                ParseObject cryptoBuy = objects.get(0);
                                currentMoney = cryptoBuy.getString("cash");
                                currentMoney = String.valueOf(Integer.parseInt(currentMoney) - decMoney);
                                cryptoBuy.put("cash", currentMoney);

                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e != null) {
                                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                                        } else {

                                            mainUser.getBankAccounts().get(index).setCash(Integer.parseInt(currentMoney));
                                            cryptoDatabase();

                                        }
                                    }
                                });


                            }


                        }

                    }
                }
            });


        }
        }

    @Override
    public boolean onQueryTextSubmit(String query) {
        toolbarSearch(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        toolbarSearch(newText);
        return false;
    }
    private void cryptoDatabase(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CryptoAmount");
        query.whereEqualTo("username", mainUser.getId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e!=null){
                    e.printStackTrace();
                }else{
                    if(objects.size()>0){
                        for(ParseObject object:objects){
                            ParseObject cryptoLook=objects.get(0);
                            for (int i=0; i<25; i++){
                                String ownCrypto=cryptoLook.getString(cryptoModels.get(i).getCurrencySymbol().toUpperCase());
                                if(ownCrypto!=null){
                                    Double price=Double.parseDouble(ownCrypto) *Double.parseDouble( cryptoModels.get(i).getPrice());

                                    ownCryptos.add(new CryptoModel(cryptoModels.get(i).getCurrencySymbol(),cryptoModels.get(i).getCurrencyName(),String.valueOf(price),cryptoModels.get(i).getImage(),ownCrypto,cryptoModels.get(i).getLogoUrl()));
                                }

                            }

                        }


                    }

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

            }
        });
    }


    public Date getDate(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date currentTime = Calendar.getInstance().getTime();
        return currentTime;
    }
}
