package com.example.shoppinglist;

public class ListItem {
    private boolean checked = false;
    private String itemtext = "";
    public boolean isChecked(){
        return checked;
    }
    public void setChecked(boolean checked){
        this.checked = checked;
    }
    public String getItemtext() {
        return itemtext;
    }
    public void setItemtext(String itemtext){
        this.itemtext = itemtext;
    }
}
