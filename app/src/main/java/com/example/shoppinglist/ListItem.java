package com.example.shoppinglist;

public class ListItem {
    private int pid = 0;
    private boolean checked = false;
    private boolean strikethrough = false;
    private String itemtext = "";
    public void setPid(int pid){
        this.pid = pid;
    }
    public int getPid(){
        return this.pid;
    }
    public boolean isChecked(){
        return checked;
    }
    public void setChecked(boolean checked){
        this.checked = checked;
    }
    public void setStrikethrough(boolean strikethrough){
        this.strikethrough = strikethrough;
    }
    public boolean isStrikethrough() {
        return strikethrough;
    }
    public String getItemtext() {
        return itemtext;
    }
    public void setItemtext(String itemtext){
        this.itemtext = itemtext;
    }
}
