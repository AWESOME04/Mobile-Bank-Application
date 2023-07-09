package com.bank.izbank.MainScreen;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import static android.app.Activity.RESULT_OK;

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
import com.bank.izbank.R;
import com.bank.izbank.Sign.SignIn;
import com.bank.izbank.UserInfo.Address;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bank.izbank.Sign.SignIn.mainUser;
import static com.parse.Parse.getApplicationContext;

public class SettingFragment extends Fragment {
   private TextView name,phone,userId,userAdress,prof,logOut;
    private Bitmap selectedImage;
    private ImageView imageView;
    private Spinner spinner;
    private ArrayAdapter<String> jobArrayAdapter;
    private  RelativeLayout relativeLayoutMobileRow,relativeLayoutNameRow,relativeLayoutAddressRow,relativeLayoutPassRow,relativeLayoutDeleteRow;
    private String newChangeItem ="";
    private Address newAddress;
    private String [] jobs;
    private Job [] defaultJobs;
    private  String job;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_profile, container, false);
        name = (TextView) rootView.findViewById(R.id.settings_userNameTextView);
        phone=(TextView)rootView.findViewById(R.id.settings_phoneTextView);
        userId=(TextView)rootView.findViewById(R.id.setting_userId_Big);
        userAdress=(TextView)rootView.findViewById(R.id.settings_addressTextView);
        prof=(TextView)rootView.findViewById(R.id.settings_profTextView);
        imageView=rootView.findViewById(R.id.fragment5_ImageView);
        relativeLayoutMobileRow=rootView.findViewById(R.id.account_mobile_row);
        relativeLayoutNameRow=rootView.findViewById(R.id.account_setting_name_row);
        relativeLayoutAddressRow=rootView.findViewById(R.id.settings_addres_row);
        relativeLayoutPassRow=rootView.findViewById(R.id.settings_change_pass_row);
        relativeLayoutDeleteRow=rootView.findViewById(R.id.settings_account_delete_row);
        logOut=rootView.findViewById(R.id.setting_logout_textView);

        spinner = rootView.findViewById(R.id.jobSpinner);
        name.setText(mainUser.getName());
        phone.setText(mainUser.getPhoneNumber());
        userId.setText(mainUser.getId());
        userAdress.setText(mainUser.addressWrite());
        prof.setText(mainUser.getJob().getName());
        if(mainUser.getPhoto()!=null){
            imageView.setImageBitmap(mainUser.getPhoto());
        }
        defineJobSpinner();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        relativeLayoutNameRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeName(v);
            }
        });
        relativeLayoutMobileRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePhone(v);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(v);
            }
        });
        relativeLayoutPassRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePass(v);
            }
        });

relativeLayoutAddressRow.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        changeAddress(v);
    }
});
relativeLayoutDeleteRow.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        deleteAccount(v);
    }
});
logOut.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        logOut(v);
    }
});
    }

    public void changeName(View v){

        final EditText editText = new EditText(getContext());
        editText.setHint("Enter new Name");

        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());

        ad.setTitle("Change Name");
        ad.setIcon(R.drawable.ic_name);
        ad.setView(editText);
        ad.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newChangeItem=editText.getText().toString();
                change( newChangeItem,"userRealName");
                name.setText( newChangeItem);
                mainUser.setName( newChangeItem);
            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(),"Canceled",Toast.LENGTH_SHORT).show();
            }
        });
        ad.create().show();

    }
    public void changePass(View v){

        final EditText editText = new EditText(getContext());
        editText.setHint("Enter new Password");

        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());

        ad.setTitle("Change Password");
        ad.setIcon(R.drawable.ic_user_def);
        ad.setView(editText);
        ad.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newChangeItem=editText.getText().toString();
                ParseUser user=ParseUser.getCurrentUser();
               user.setPassword(newChangeItem);
               user.saveInBackground(new SaveCallback() {
                   @Override
                   public void done(ParseException e) {
                       if(e ==null ) {

                           Toast.makeText(getContext(),"Password changed",Toast.LENGTH_SHORT).show();
                       } else {
                           Toast.makeText(getContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                       }
                   }
               });
            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(),"Canceled",Toast.LENGTH_SHORT).show();
            }
        });
        ad.create().show();

    }
    public void changePhone(View v){

        final EditText editTextPhone = new EditText(getContext());
        editTextPhone.setHint("Enter new Phone");

        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());

        ad.setTitle("Change Phone");
        ad.setIcon(R.drawable.ic_mobile);
        ad.setView(editTextPhone);
        ad.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newChangeItem =editTextPhone.getText().toString();
                change(newChangeItem,"phone");
                mainUser.setPhoneNumber(newChangeItem);
                phone.setText(newChangeItem);
            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(),"Canceled",Toast.LENGTH_SHORT).show();
            }
        });
        ad.create().show();


    }
    public void changeAddress(View v){

        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());

        ad.setTitle("Change Address");
        ad.setIcon(R.drawable.ic_address);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView= inflater.inflate(R.layout.settings_address_popup, null);
        ad.setView(dialogView);
        ad.setPositiveButton("Change", new DialogInterface.OnClickListener() {
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
                    mainUser.setAddress(newAddress);
                    userAdress.setText(mainUser.addressWrite());
                    change(mainUser.addressWrite(),"address");
                }else{
                    Toast.makeText(getContext(),"Please Fill the all field",Toast.LENGTH_SHORT).show();
                }


            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(),"Canceled",Toast.LENGTH_SHORT).show();
            }
        });
        ad.create().show();


    }


    private void deleteAccount(View v){
        ParseUser user = ParseUser.getCurrentUser();

        ParseQuery<ParseObject> queryBankAccount=ParseQuery.getQuery("BankAccount");
        queryBankAccount.whereEqualTo("userId", mainUser.getId());
        queryBankAccount.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e!=null){
                    e.printStackTrace();
                }else{
                    if(objects.size()>0){
                        ParseObject.deleteAllInBackground(objects);
                    }
                }
            }
        });
        ParseQuery<ParseObject> queryDel=ParseQuery.getQuery("Bill");
        queryDel.whereEqualTo("username", mainUser.getId());
        queryDel.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e!=null){
                    e.printStackTrace();
                }else{
                    if(objects.size()>0){
                        ParseObject.deleteAllInBackground(objects);
                    }
                }
            }
        });
        ParseQuery<ParseObject> queryDel2=ParseQuery.getQuery("Credit");
        queryDel2.whereEqualTo("username", mainUser.getId());
        queryDel2.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e!=null){
                    e.printStackTrace();
                }else{
                    if(objects.size()>0){
                        ParseObject.deleteAllInBackground(objects);
                    }
                }
            }
        });
        ParseQuery<ParseObject> queryDel3=ParseQuery.getQuery("CreditCard");
        queryDel3.whereEqualTo("userId", mainUser.getId());
        queryDel3.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e!=null){
                    e.printStackTrace();
                }else{
                    if(objects.size()>0){
                        ParseObject.deleteAllInBackground(objects);
                    }
                }
            }
        });
        ParseQuery<ParseObject> queryDel4=ParseQuery.getQuery("CryptoAmount");
        queryDel4.whereEqualTo("username",mainUser.getId());
        queryDel4.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e!=null){
                    e.printStackTrace();
                }else{
                    if(objects.size()>0){
                        ParseObject.deleteAllInBackground(objects);
                    }
                }
            }
        });
        ParseQuery<ParseObject> queryDel5=ParseQuery.getQuery("History");
        queryDel5.whereEqualTo("userId", mainUser.getId());
        queryDel5.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e!=null){
                    e.printStackTrace();
                }else{
                    if(objects.size()>0){
                        ParseObject.deleteAllInBackground(objects);
                    }
                }
            }
        });
        ParseQuery<ParseObject> queryDel6=ParseQuery.getQuery("UserInfo");
        queryDel6.whereEqualTo("username", mainUser.getId());
        queryDel6.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e!=null){
                    e.printStackTrace();
                }else{
                    if(objects.size()>0){
                        ParseObject.deleteAllInBackground(objects);

                    }
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                user.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        if (e == null) {
                            Toast.makeText(getContext(), user.getUsername() + " deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), user.getUsername() + "not deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            },1000);


        Intent intent=new Intent(getActivity(),SignIn.class);
        startActivity(intent);

    }

    private void change(String changeItem,String changeColumName){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserInfo");
        query.whereEqualTo("username", SignIn.mainUser.getId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    if (objects.size() > 0) {
                        for (ParseObject object : objects) {
                            ParseObject user = objects.get(0);
                            user.put(changeColumName,changeItem );
                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Toast.makeText(getContext(), e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }

                }
            }
            });
    }





    public void selectImage(View view){
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        else{
            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,2);
        }
    }

    public void defineJobSpinner(){

        defaultJobs = new Job[]{new Contractor(),new Doctor(),new Driver(),new Engineer(),new Entrepreneur(),
                new Farmer(),new Police(),new Soldier(),new Sportsman(),new Student(),new Teacher(),new Waiter(),new Worker()};

        jobs = new String[] {"Contractor","Doctor","Driver","Engineer","Entrepreneur","Farmer","Police","Soldier",
                "Sportsman","Student","Teacher","Waiter","Worker"};

        jobArrayAdapter = new ArrayAdapter(getContext(),R.layout.support_simple_spinner_dropdown_item,jobs);

        spinner.setAdapter(jobArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                job = adapterView.getSelectedItem().toString();

                change( job,"job");
                prof.setText( job);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            Uri dataUri = data.getData();
            try {
                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), dataUri);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    imageView.setImageBitmap(selectedImage);

                } else {
                    selectedImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), dataUri);
                    imageView.setImageBitmap(selectedImage);

                }
                mainUser.setPhoto(selectedImage);
                Bitmap smallerImage = makeSmallerImage(selectedImage, 300);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                smallerImage.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);

                byte[] bytes = byteArrayOutputStream.toByteArray();

                ParseQuery<ParseObject> query = ParseQuery.getQuery("UserInfo");
                query.whereEqualTo("username", mainUser.getId());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e != null) {
                            e.printStackTrace();
                        } else {
                            if (objects.size() > 0) {
                                for (ParseObject object : objects) {
                                    ParseObject userInfo = objects.get(0);
                                        ParseFile file = new ParseFile("image.png", bytes);
                                        userInfo.put("images", file);
                                        object.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e != null) {
                                                    Toast.makeText(getContext(), e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();

                                                }

                                            }
                                        });



                                }
                            }
                        }

                    }
                });

            }catch (Exception e){
                Toast.makeText(getActivity().getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1 && grantResults[0]==PackageManager.PERMISSION_GRANTED && permissions.length>0){
            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,2);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }





    private Bitmap makeSmallerImage(Bitmap image, int maximumSize) {

        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {
            width = maximumSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maximumSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image,width,height,true);
    }


}
