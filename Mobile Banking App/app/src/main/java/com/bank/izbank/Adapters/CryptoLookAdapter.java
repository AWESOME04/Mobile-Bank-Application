package com.bank.izbank.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadrosid.svgloader.SvgLoader;
import com.bank.izbank.MainScreen.FinanceScreen.CryptoModel;
import com.bank.izbank.MainScreen.FinanceScreen.ItemClickListener;
import com.bank.izbank.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CryptoLookAdapter extends RecyclerView.Adapter<CryptoLookAdapter.PostHolder> {
    private Context context;
    private ArrayList<CryptoModel> models;
    private Activity activity;

    class PostHolder extends RecyclerView.ViewHolder{
        private  Context context;
        private TextView cryptoNameText,cryptoSymbolText,cryptoAmountText,cryptoPriceText;
        private ImageView cryptoImageView;


        public PostHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context=context;

            cryptoNameText=itemView.findViewById(R.id.crypto_symbol_text);
            cryptoAmountText=itemView.findViewById(R.id.crypto_name_text);
            cryptoImageView=itemView.findViewById(R.id.crypto_imageview);
            cryptoPriceText=itemView.findViewById(R.id.crypto_price_text_view);
        }
    }

    public CryptoLookAdapter(ArrayList<CryptoModel> models, Activity activity, Context context) {
        this.models = models;
        this.activity=activity;
        this.context=context;
    }


    @NonNull
    @Override
    public CryptoLookAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view =layoutInflater.inflate(R.layout.custom_view,parent,false);


        return new CryptoLookAdapter.PostHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.cryptoNameText.setText(models.get(position).getCurrencyName());
        holder. cryptoAmountText.setText(models.get(position).getAmount());
        holder. cryptoPriceText.setText(models.get(position).getPrice()+" USD");

        //for .svg image file
        if(models.get(position).getLogoUrl().substring(models.get(position).getLogoUrl().length()-3).equalsIgnoreCase("svg")){
            SvgLoader.pluck()
                    .with(activity)
                    .setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
                    .load(models.get(position).getLogoUrl(),holder.cryptoImageView);


        }else{//for all image file without .svg
            Picasso.get().load(models.get(position).getLogoUrl()).into(holder.cryptoImageView);
        }


    }


    @Override
    public int getItemCount() {
        return models.size();
    }



}
