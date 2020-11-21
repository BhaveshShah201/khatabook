/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khatabook.util;

import java.util.regex.Pattern;
import javafx.scene.control.Label;

/**
 *
 * @author ME
 */
public class ValidationUtil {

    public static boolean isMobileNumber(String letter, String txt) {
        Pattern pattern = Pattern.compile(".*\\D.*");
        return (pattern.matcher(letter).matches() || txt.length() >= 10);
    }

    public static boolean checkEmail(String txt) {
        String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(txt).matches();
    }

    public static String validateMobileNumber(String mobileTxt) {
        if (mobileTxt.equalsIgnoreCase("")) {
            return "Enter Valid Mobile Number!!";
        }
        if (mobileTxt.length() != 10) {
            return "Mobile Number be 10 digit!!";
        }
        return "";
    }

    public static String validateEmail(String emailTxt) {
        if (emailTxt.equalsIgnoreCase("") || !ValidationUtil.checkEmail(emailTxt)) {
            return "Enter Valid Email!!";
        }
        return "";
    }

    public static String validatePwd(String pwd, String label) {
        if (pwd.equalsIgnoreCase("")) {
            return "Enter Valid " + label + " Password!!";
        }
        return "";
    }

    public static String validateConfirmPwd(String pwd, String conPwd) {
        if (!conPwd.equalsIgnoreCase(pwd)) {
            return "Confirm Password Not Matched!!";
        }
        return "";
    }

    public static String validateLoginPwd(String pwd, String conPwd, String label) {
        if (!conPwd.equalsIgnoreCase(pwd)) {
            return "Mobile number And " + label + " Password Not Match!!";
        }
        return "";
    }
}
