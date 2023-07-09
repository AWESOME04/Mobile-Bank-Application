package com.bank.izbank.service;

import com.bank.izbank.MainScreen.FinanceScreen.CryptoModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ICryptoAPI {

    //GET POST UPDATE DELETE
    //GET
    //https://api.nomics.com/v1/currencies/ticker?key=c5d0683b83dc6e2dbd00841b72f7c86c&sort%22rank%22&per-page=25&page=1&interval=1d
    @GET("currencies/ticker?key=c5d0683b83dc6e2dbd00841b72f7c86c&sort%22rank%22&per-page=25&page=1&interval=1d")
    Call<List<CryptoModel>> getData();

}
