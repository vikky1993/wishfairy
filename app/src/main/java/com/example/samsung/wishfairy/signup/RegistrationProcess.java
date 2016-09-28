package com.example.samsung.wishfairy.signup;

/**
 * Created by SAMSUNG on 29-07-2015.
 */
public class RegistrationProcess {
    public static String uname;
    public static String password;
    public static String email;
    public static String phone;
    public RegistrationProcess(String uname,String password,String phone,String email){
        this.uname=uname;
        this.password=password;
        this.email=email;
        this.phone=phone;
    }

    public static String getUname() {
        return uname;
    }

    public static void setUname(String uname) {
        RegistrationProcess.uname = uname;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        RegistrationProcess.password = password;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        RegistrationProcess.email = email;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        RegistrationProcess.phone = phone;
    }
}
