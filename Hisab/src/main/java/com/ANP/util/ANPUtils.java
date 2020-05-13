package com.ANP.util;

import com.ANP.bean.SearchParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


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
    public static String getWhereClause(final Collection<SearchParam> searchParam)
    {
        String where=" ";
        for (Object obj : searchParam) {
                SearchParam searchParam1 = (SearchParam) obj;
                String d = searchParam1.getValue();
                if(searchParam1.getFieldType().equals("String")||searchParam1.getFieldType().equals("DATE"))
                    d = "'"+d+"'";
                where = where + " " + searchParam1.getCondition() + " " + searchParam1.getFieldName() + " " +
                        searchParam1.getOperator() + " " +d;
        }
        if(where.equals(" ") )
            where = null;
        System.out.println(where);
        return where ;
    }
}
