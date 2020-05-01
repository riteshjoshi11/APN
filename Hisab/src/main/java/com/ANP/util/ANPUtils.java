package com.ANP.util;

public class ANPUtils {
    public static boolean isNullOrEmpty(String str) {
        boolean retValue=false;
        if( str==null || (str.trim().length())==0) {
            retValue = true;
        }
        return retValue;
    }
}
