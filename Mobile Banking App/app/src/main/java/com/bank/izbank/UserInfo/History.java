package com.bank.izbank.UserInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class History {
    private String userId;
    private String Process;
    private Date date;

    public History(String userId, String process, Date date) {
        this.userId = userId;
        Process = process;
        this.date = date;
    }

    public Date getDateDate() {

        return date;
    }
    public String getDateString() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProcess() {
        return Process;
    }

    public void setProcess(String process) {
        Process = process;
    }
}
