package com.bank.izbank.UserInfo;

public class UserContext implements UserTypeState {
    private UserTypeState userState;

    public void setState(UserTypeState state) {
        this.userState=state;
    }

    public UserTypeState getState() {
        return this.userState;
    }
    @Override
    public void TypeChange(User user) {
        this.userState.TypeChange(user);
    }



}
