package com.ANP.bean;

public class SearchParam {
    String soperator;  //e.g (greater than, less than, equal to ...)
    String fieldType; //e.g String, DATE, NUMBER
    String fieldName; //e.g Name
    String condition; //e.g AND,OR
    String value;//   String operator;

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSoperator() {
        return soperator;
    }

    public void setSoperator(String soperator) {
        this.soperator = soperator;
    }

    @Override
    public String toString() {
        return "SearchParam{" +
                "soperator='" + soperator + '\'' +
                ", fieldType='" + fieldType + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", condition='" + condition + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
