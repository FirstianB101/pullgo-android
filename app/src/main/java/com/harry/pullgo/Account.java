package com.harry.pullgo;

public class Account {
    private String userName;
    private String password;
    private String fullName;
    private String phone;

    public Account(String userName,String password,String fullName,String phone){
        this.userName=userName;
        this.password=password;
        this.fullName=fullName;
        this.phone=phone;
    }

    public Account(Account source){
        this.userName=source.userName;
        this.password=source.password;
        this.fullName=source.fullName;
        this.phone=source.phone;
    }

    public String getPhone() { return phone; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public String getUserName() { return userName; }

    public void setPassword(String password) { this.password = password; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setPhone(String phone) { this.phone = phone; }
}
