package com.bank.izbank.MainScreen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bank.izbank.Adapters.HistoryAdapter;
import com.bank.izbank.Adapters.MyBankAccountAdapter;
import com.bank.izbank.Adapters.UserAdapter;
import com.bank.izbank.R;
import com.bank.izbank.Sign.SignIn;
import com.bank.izbank.UserInfo.History;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Stack;
import java.util.ArrayList;

import static com.bank.izbank.Sign.SignIn.mainUser;

public class AdminPanelActivity extends AppCompatActivity {

    private LinearLayout linear_layout_history, linear_layout_logout;
    RecyclerView recyclerViewHistory;
    private HistoryAdapter historyAdapter;
    private TextView date;
    boolean flag = true;
    private RecyclerView recyclerViewUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        define();
        recyclerViewUser.setHasFixedSize(true);
        recyclerViewUser.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        UserAdapter useradapter = new UserAdapter(SignIn.allUsers,AdminPanelActivity.this );
        recyclerViewUser.setAdapter(useradapter);


        setDate();
        click();

    }
    public void define(){
        linear_layout_history = findViewById(R.id.linear_layout_history);
        date = findViewById(R.id.text_view_date_admin);
        recyclerViewUser = findViewById(R.id.recyclerview_user);
    }
    public void click(){
        linear_layout_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder history_popup=new AlertDialog.Builder(AdminPanelActivity.this);

                history_popup.setTitle("HISTORY");

                history_popup.setView(R.layout.history_popup);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView= inflater.inflate(R.layout.history_popup, null);
                history_popup.setView(dialogView);
                recyclerViewHistory =(RecyclerView)dialogView.findViewById(R.id.history_recycler_view);
                recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                historyAdapter=new HistoryAdapter(stackToArrayList(mainUser.getHistory()),AdminPanelActivity.this,getApplicationContext());
                flag=false;

                recyclerViewHistory.setAdapter(historyAdapter);
                historyAdapter.notifyDataSetChanged();
                history_popup.create().show();
            }
        });

    }
    public ArrayList<History> stackToArrayList(Stack<History> stack){
        ArrayList<History> arraylistHistory = new ArrayList<>();
        while (stack.size() !=0){
            arraylistHistory.add(stack.pop());
        }
        for (int i =arraylistHistory.size()-1;i>=0; i-- ) {
            if (flag){
                arraylistHistory.get(i).setProcess(arraylistHistory.get(i).getUserId() +": " + arraylistHistory.get(i).getProcess());
            }

            mainUser.getHistory().push(arraylistHistory.get(i));
        }
        return arraylistHistory;
    }
    public void setDate(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date currentTime = Calendar.getInstance().getTime();
        date.setText(format.format(currentTime));

    }

    public void logOut(View view){
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if(e !=null){
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }else{
                    Intent intent=new Intent(getApplicationContext(), SignIn.class);
                    startActivity(intent);
                }
            }
        });
    }
}