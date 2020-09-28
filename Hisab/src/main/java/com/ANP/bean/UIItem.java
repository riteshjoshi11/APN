package com.ANP.bean;

public class UIItem {
    String uiItemCode;
    String uiItemName;

    public UIItem() {

    }

    public UIItem(String code, String name) {
        this.uiItemCode = code ;
        this.uiItemName = name ;
    }
    public String getUiItemName() {
        return uiItemName;
    }

    public void setUiItemName(String uiItemName) {
        this.uiItemName = uiItemName;
    }

    public String getUiItemCode() {
        return uiItemCode;
    }

    public void setUiItemCode(String uiItemCode) {
        this.uiItemCode = uiItemCode;
    }
}
