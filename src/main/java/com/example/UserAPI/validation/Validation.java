package com.example.UserAPI.validation;


import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Validation {

    public boolean mobileNumberValidate(String mobileNumber){
        //if length is greater than 10 then number is invalid
        if(mobileNumber.length()!=10)
            return false;

        //Else it will check the regular expression of mobile number
        String regex = "[0-9]+";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(mobileNumber);
        //if it matches the pattern then returns true else returns false
        return matcher.matches();
    }
    public boolean emailValidation(String email){
        //Regular expression for email
        //$ here shows end of email
        String regex = "^[A-Za-z0-9+-._]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);
        //if it matches the pattern then return true else returns false
        return matcher.matches();
    }
}
