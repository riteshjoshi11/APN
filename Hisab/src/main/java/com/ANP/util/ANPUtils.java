package com.ANP.util;

import com.ANP.bean.SearchParam;

import java.util.Collection;


public class ANPUtils implements ANPConstants {
    public static boolean isNullOrEmpty(String str) {
        boolean retValue=false;
        if( str==null || (str.trim().length())==0) {
            retValue = true;
        }
        return retValue;
    }

    /*
     * TODO PARAS: Consider SearchParam a single criteria e.g on customer table i can pass
     *  SearchParam.fieldName = "city"
     *  SearchParam.fieldType = "string"
     *  SearchParam.operator = "="
     *  SearchParam.value     = "Indore"
     *  SearchParam.condition = "and"
     *
     * So it should return as::   and city="Indore"
     *
     * Logic you need to iterate one by one searchParam and create above kind of query and keep appending.
     *

     */
    public static String getWhereClause(final Collection<SearchParam> searchParam) {
        return null ;
    }
}
