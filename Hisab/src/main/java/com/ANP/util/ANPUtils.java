package com.ANP.util;

import com.ANP.bean.SearchParam;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/*
* this class is util class where all general purpose common method will be there.
 */
public class ANPUtils implements ANPConstants {
    public static boolean isNullOrEmpty(String str) {
        boolean retValue = false;
        if (str == null || (str.trim().length()) == 0) {
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
    public static String getWhereClause(final Collection<SearchParam> searchParamCollection) {
        String where = "";
        if (searchParamCollection != null) {
            for (SearchParam searchParam : searchParamCollection) {
                String value = searchParam.getValue();

                if (ANPConstants.SEARCH_FIELDTYPE_STRING.equalsIgnoreCase(searchParam.getFieldType())
                        || ANPConstants.SEARCH_FIELDTYPE_DATE.equalsIgnoreCase(searchParam.getFieldType())) {
                    value = "'" + value + "'";
                }
                where = where + " " + searchParam.getCondition() + " " + searchParam.getFieldName() + " " +
                            searchParam.getSoperator() + " " + value;
            }
        }
        return where;
    }

    public static void main(String argsp[]) {
        Collection<SearchParam> l = new ArrayList();
        SearchParam searchParam = new SearchParam();
        searchParam.setCondition("OR");
        searchParam.setFieldName("city");
        searchParam.setFieldType(ANPConstants.SEARCH_FIELDTYPE_STRING);
        searchParam.setSoperator("=");
        searchParam.setValue("Pune");

        l.add(searchParam);

        SearchParam searchParam1 = new SearchParam();
        searchParam1.setCondition("AND");
        searchParam1.setFieldName("city");
        searchParam1.setFieldType("Number");
        searchParam1.setSoperator(">");
        searchParam1.setValue("30000");
        l.add(searchParam1);
        System.out.println(getWhereClause(l));
        System.out.println("Month Part=" + getMonthPart(new Date()));
        System.out.println("Month Part2=" + getMonthPart(getPreviousMonth()));

        System.out.println("Next Day" + addOneDay(new Date()));

    }

    public static String getMonthPart(Date date) {
        String retStr = (new SimpleDateFormat("MMMM").format(date)).toString() + " " + (new SimpleDateFormat("YYYY").format(date)).toString();
        return retStr ;
    }

    public static Date getPreviousMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        return calendar.getTime();
    }

    public static Date addOneDay(final Date pDate) {
        Calendar calendar = Calendar.getInstance() ;
        calendar.setTime(pDate);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        return calendar.getTime();
    }



}
