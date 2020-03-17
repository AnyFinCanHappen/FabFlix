package edu.uci.ics.sidneyjt.service.idm.helper;

public class InfoChecker
{
    public static boolean checkAlphanumeric(String s)
    {
        return s.matches("^[a-zA-Z0-9]*$");
    }
    public static boolean validateEmailLength(String email)
    {
        return email == null || email.length() >= 50 || email.length() == 0;
    }
    public static boolean validateEmail(String email)
    {
        //if email contains any space characters
        if(validateEmailLength(email))
            return false;
        for(char c: email.toCharArray())
            if(c == ' ')
                return false;

        //check if email has one '@' and has characters before and after
        //also to check if there are no '.' before '@'
        String[] split = email.split("@");
        if(split.length != 2)
            return false;
        if(split[0].equals("") || split[1].equals(""))
            return false;
        //check if it is alphanumeric
        if(!checkAlphanumeric(split[0]))
            return false;
        if(split[1].charAt(0) == '.')
        {
            return false;
        }
        for(char c: split[0].toCharArray())
            if(c == '.')
                return false;

        //check if email has correct extension
        if(email.charAt(email.length() - 1) == '.')
            return false;
        else {
            String domain = split[1];
            String[] SplitDomain = domain.split(".");
            for(String s: SplitDomain)
                if(!checkAlphanumeric(s))
                    return false;

            //check if no two '.' next to each other
            for(int i = 0; i < domain.length(); i ++)
                if(domain.charAt(i) == '.')
                    if(domain.charAt(i + 1) == '.')
                        return false;
        }
        return true;
    }
    public static boolean validatePasswordLength(char[] password)
    {
        if(password == null)
            return true;
        return password.length < 7 || password.length >= 16;
    }
    public static boolean validatePassword(char[] charList)
    {
        if(validatePasswordLength(charList))
            return false;
        String password = String.valueOf(charList);
        if(!checkAlphanumeric(password))
            return false;
        boolean upper = false, lower = false, number = false;
        for(char c: password.toCharArray())
        {
            if(Character.isUpperCase(c))
                upper = true;
            if(Character.isLowerCase(c))
                lower = true;
            if(Character.isDigit(c))
                number = true;
        }
        return upper && lower && number;
    }
    public static boolean validateTokenLength(String token)
    {
        if(token == null || token.length() == 0 || token.length() != 128)
            return false;
        return true;
    }
}
