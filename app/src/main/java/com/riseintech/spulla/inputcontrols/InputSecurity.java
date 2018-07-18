package com.riseintech.spulla.inputcontrols;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by adm on 8/12/2016.
 */
public class InputSecurity {

    public static boolean validateInput(EditText rname,EditText rcontact, EditText remail,
                                  EditText rpass,EditText rcpass, Context con) {


        String inputName=rname.getText().toString();
        String inputEmail=remail.getText().toString();
        String inputPass=rpass.getText().toString();
        String inputCPass=rcpass.getText().toString();
        String inputContact=rcontact.getText().toString();



        if (TextUtils.isEmpty(inputName))
        {
            rname.setError("Oops! Name field blank");
            rname.requestFocus();
            return false;
        }
        // Check for a valid email address.

         if (TextUtils.isEmpty(inputEmail))
        {
            remail.setError("Oops! Email field blank");
            remail.requestFocus();
            return false;
        }

        else if (isEmailValid(inputEmail) == false)
        {
            remail.setError("Invalid Email");
            remail.requestFocus();
            return false;

        }
        // Check for a valid Password.
        else if (TextUtils.isEmpty(inputPass))
        {
            rpass.setError("Oops! Password field blank");
            rpass.requestFocus();
            return false;
        }

        // Check for a valid Date of birth.
        else if (inputCPass!= null && !inputCPass.equals(inputPass))
        {
            rcpass.setError("Oops! Confirm password is not the same as password");
            rcpass.requestFocus();
            return false;
        }

        else if (inputContact.length()!=10)
        {
            rcontact.setError("Invalid Contact");
            rcontact.requestFocus();
            return false;

        }

        return true;
    }


    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 6) {
            return true;
        }
        return false;
    }




    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isNameValid(String name) {
        boolean isValid = false;

        String expression = "^[a-zA-Z]{1,50}$";
        CharSequence inputStr = name;

        Pattern pattern = Pattern.compile(expression, Pattern.UNICODE_CASE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {
            isValid = true;
        } else if (name.length() > 1) {
            if (name.contains(" ")) {
                isValid = true;
            }

        }
        return isValid;
    }

    public boolean isEditTextEmpty(EditText mInput) {
        if (mInput.length() == 0)
            return true;
        else
            return false;
    }


    /*private void requestFocus(View view,Context context) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }*/

}
