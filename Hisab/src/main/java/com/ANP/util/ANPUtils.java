package com.ANP.util;

public class ANPUtils implements ANPConstants {
    public static boolean isNullOrEmpty(String str) {
        boolean retValue=false;
        if( str==null || (str.trim().length())==0) {
            retValue = true;
        }
        return retValue;
    }
}
