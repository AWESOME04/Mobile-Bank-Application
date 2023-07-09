package com.bank.izbank.database;

import android.app.Application;

import com.parse.Parse;

public class ParseStarterClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("D8ZgDeOQTsuqVhlij1GLWT2BRwWTALUEQ0lmFNKP")
                // if desired
                .clientKey("pt1i1StL5R4KQLkEK42PJsJBUs6MPAabZiouiikP")
                .server("https://parseapi.back4app.com/")
                .build()
        );

    }
}
