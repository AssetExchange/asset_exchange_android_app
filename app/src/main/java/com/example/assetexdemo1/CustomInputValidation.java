package com.example.assetexdemo1;

public class CustomInputValidation {
    public static boolean validateEmail(String email) {
        if (email != null && email.matches("[A-Za-z0-9\\.-]+@[A-Za-z0-9\\.-]+\\.[a-zA-Z]{2,}")) {
            return true;
        }
        return false;
    }
    public static boolean validatePassword(String password, String name, String email) {
        if (
            password.length() < 8 ||
            password.contains(" ") ||
            !password.matches(".*\\d.*") ||
            (name != null && (password.contains(name.trim()) || name.trim().equals(""))) ||
            (email != null && (password.contains(email.trim()) || email.trim().equals("")))
        ) {
            return false;
        }
        return true;
    }
    public static boolean[] validatePasswordWithCheck(String password, String name, String email) {
        boolean[] conditions = {false, false, false, false};

        if (password.length() >= 8)
            conditions[0] = true;
        if (!password.contains(name.trim()) && !password.contains(email.trim()))
            conditions[1] = true;
        if (password.matches(".*\\d.*"))
            conditions[2] = true;
        if (!password.contains(" "))
            conditions[3] = true;

        return conditions;
    }
}
