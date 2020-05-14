package com.ANP.util;

import com.ANP.bean.SearchParam;

import java.util.ArrayList;
import java.util.Collection;

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
        if (searchParamCollection != null || !searchParamCollection.isEmpty()) {
            for (SearchParam searchParam : searchParamCollection) {
                String value = searchParam.getValue();

                if (ANPConstants.SEARCH_FIELDTYPE_STRING.equalsIgnoreCase(searchParam.getFieldType())
                        || ANPConstants.SEARCH_FIELDTYPE_DATE.equalsIgnoreCase(searchParam.getFieldType())) {
                    value = "'" + value + "'";
                }
                where = where + " " + searchParam.getCondition() + " " + searchParam.getFieldName() + " " +
                            searchParam.getOperator() + " " + value;
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
        searchParam.setOperator("=");
        searchParam.setValue("Pune");

        l.add(searchParam);

        SearchParam searchParam1 = new SearchParam();
        searchParam1.setCondition("AND");
        searchParam1.setFieldName("city");
        searchParam1.setFieldType("Number");
        searchParam1.setOperator(">");
        searchParam1.setValue("30000");
        l.add(searchParam1);
        System.out.println(getWhereClause(l));
    }
}
