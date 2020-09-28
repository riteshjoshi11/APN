package com.ANP.bean;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UIItem uiItem = (UIItem) o;
        return uiItemCode.equals(uiItem.uiItemCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uiItemCode);
    }
}
