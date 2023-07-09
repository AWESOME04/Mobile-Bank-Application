package com.bank.izbank.UserInfo;


import java.util.Stack;

public class Admin implements UserTypeState{
    private String name;
    private Stack<History> history;
    private boolean userType;

    public Admin(String name) {
        this.name = name;
        this.history = new Stack<>();
        this.userType=false;
    }
    public Admin(){

    }
    @Override
    public void TypeChange(User user) {
        this.userType=true;
        this.name = user.getName();
       this.history=user.getHistory();

    }
    public Stack<History> getHistory() {
        return history;
    }

    public void setHistory(Stack<History> history) {
        this.history = history;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUserType() {
        return userType;
    }

    public void setUserType(boolean userType) {
        this.userType = userType;
    }


}
