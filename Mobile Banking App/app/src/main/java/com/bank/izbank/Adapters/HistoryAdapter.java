package com.bank.izbank.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bank.izbank.MainScreen.FinanceScreen.CryptoModel;
import com.bank.izbank.R;
import com.bank.izbank.UserInfo.History;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.PostHolder> {
    private Context context;
    private ArrayList<History> historyArrayList;
    private Activity activity;

    public class PostHolder extends RecyclerView.ViewHolder {
        private  Context context;
        private TextView textViewDate,textViewProcess;
        private ImageView imageViewHistory;
        public PostHolder(@NonNull @NotNull View itemView, Context context) {
            super(itemView);
            this.context=context;
            textViewDate = itemView.findViewById(R.id.text_view_history_date);
            textViewProcess = itemView.findViewById(R.id.text_view_history_process);
            imageViewHistory = itemView.findViewById(R.id.image_view_history);

        }
    }

    public HistoryAdapter(ArrayList<History> history, Activity activity, Context context){
        this.historyArrayList=history;
        this.activity = activity;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public HistoryAdapter.PostHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view =layoutInflater.inflate(R.layout.history_card_view,parent,false);
        return new HistoryAdapter.PostHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PostHolder holder, int position) {
        holder.textViewProcess.setText(historyArrayList.get(position).getProcess());
        holder.textViewDate.setText((CharSequence) historyArrayList.get(position).getDateString());

    }

    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }


}
