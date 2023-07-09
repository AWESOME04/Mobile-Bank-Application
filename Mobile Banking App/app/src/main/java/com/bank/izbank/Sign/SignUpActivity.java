package com.bank.izbank.Sign;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bank.izbank.Job.Contractor;
import com.bank.izbank.Job.Doctor;
import com.bank.izbank.Job.Driver;
import com.bank.izbank.Job.Engineer;
import com.bank.izbank.Job.Entrepreneur;
import com.bank.izbank.Job.Farmer;
import com.bank.izbank.Job.Job;
import com.bank.izbank.Job.Police;
import com.bank.izbank.Job.Soldier;
import com.bank.izbank.Job.Sportsman;
import com.bank.izbank.Job.Student;
import com.bank.izbank.Job.Teacher;
import com.bank.izbank.Job.Waiter;
import com.bank.izbank.Job.Worker;
import com.bank.izbank.MainScreen.MainScreenActivity;
import com.bank.izbank.R;
import com.bank.izbank.UserInfo.Address;
import com.bank.izbank.UserInfo.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static com.bank.izbank.Sign.SignIn.mainUser;

public class SignUpActivity extends AppCompatActivity {

    private EditText userNameText , userPassText ,userIdText,userPhoneText;

    private Spinner spinner;
    private ArrayAdapter<String> jobArrayAdapter;
    private String [] jobs;
    private Job [] defaultJobs;
    public Job tempJob ;
    public String job;
    private ImageView imageView;
    
    private Address newAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        userNameText=findViewById(R.id.edittext_id_name_sign_up);
        userPassText=findViewById(R.id.edittext_user_password_sign_up);
        userIdText=findViewById(R.id.edittext_id_number_sign_up);
        userPhoneText=findViewById(R.id.edittext_phone_sign_up);
        spinner = findViewById(R.id.jobSpinner);
        imageView=findViewById(R.id.fragment5_ImageView);

        defineJobSpinner();

    }

    public void defineJobSpinner(){

        defaultJobs = new Job[]{new Contractor(),new Doctor(),new Driver(),new Engineer(),new Entrepreneur(),
        new Farmer(),new Police(),new Soldier(),new Sportsman(),new Student(),new Teacher(),new Waiter(),new Worker()};

        jobs = new String[] {"Contractor","Doctor","Driver","Engineer","Entrepreneur","Farmer","Police","Soldier",
                "Sportsman","Student","Teacher","Waiter","Worker"};

        jobArrayAdapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,jobs);

        spinner.setAdapter(jobArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                job = adapterView.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void signUp(View view){
        mainUser=new User(userNameText.getText().toString(),
                userIdText.getText().toString(),
                userPassText.getText().toString(),
                userPhoneText.getText().toString(),newAddress
                );

        ParseUser parseUser=new ParseUser();
        parseUser.setUsername(mainUser.getId());
        parseUser.setPassword(mainUser.getPass());



        ParseObject object=new ParseObject("UserInfo");
        object.put("userRealName",mainUser.getName());
        object.put("phone",mainUser.getPhoneNumber());
        object.put("address",mainUser.addressWrite());
        object.put("username", mainUser.getId());


        for(Job x:defaultJobs){
            if(x.getName().equals(job)){
                tempJob=x;
                break;
            }
        }

        mainUser.setJob(tempJob);
        object.put("job",tempJob.getName());
        object.put("maxCreditAmount",tempJob.getMaxCreditAmount());
        object.put("maxCreditInstallment",tempJob.getMaxCreditInstallment());
        object.put("interestRate",tempJob.getInterestRate());
        object.put("address",mainUser.addressWrite());

        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }

            }
        });

       parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"User Created" ,Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getApplicationContext(), MainScreenActivity.class);
                    startActivity(intent);

                    //intent
                }
            }
        });


    }


    public void createAddress(View v){

        AlertDialog.Builder ad = new AlertDialog.Builder(this);

        ad.setTitle("Address");
        ad.setIcon(R.drawable.ic_address);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView= inflater.inflate(R.layout.settings_address_popup, null);
        ad.setView(dialogView);
        ad.setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextView street=dialogView.findViewById(R.id.setting_address_street);
                TextView blockNo=dialogView.findViewById(R.id.setting_address_block);
                TextView floor=dialogView.findViewById(R.id.setting_address_floor);
                TextView houseNo=dialogView.findViewById(R.id.setting_address_house);
                TextView country=dialogView.findViewById(R.id.setting_address_country);
                TextView neighborhood=dialogView.findViewById(R.id.setting_address_neigh);
                TextView town=dialogView.findViewById(R.id.setting_address_town);
                TextView state=dialogView.findViewById(R.id.setting_address_state);
                if(street.getText().toString() !=null &&neighborhood.getText().toString()!=null && blockNo.getText().toString()!=null&&floor.getText().toString()!=null &&houseNo.getText().toString()!=null&& town.getText().toString()!=null &&state.getText().toString()!=null&& country.getText().toString()!=null){
                    newAddress= new Address(street.getText().toString(),neighborhood.getText().toString(),Integer.parseInt(blockNo.getText().toString()),Integer.parseInt(floor.getText().toString()),Integer.parseInt(houseNo.getText().toString()),town.getText().toString(),state.getText().toString(),country.getText().toString());


                }else{
                    Toast.makeText(getApplicationContext(),"Please Fill the all field",Toast.LENGTH_SHORT).show();
                }
            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Canceled",Toast.LENGTH_SHORT).show();
            }
        });
        ad.create().show();


    }


}