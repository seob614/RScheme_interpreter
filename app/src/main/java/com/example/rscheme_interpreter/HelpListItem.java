package com.example.rscheme_interpreter;

public class HelpListItem {
    private String titleStr;
    private String expStr;

    private String setStr;

    public void setTitle(String title) {
        titleStr = title;
    }
    public void setExp(String exp) {
        expStr = exp;
    }

    public void setSet(String set) {
        setStr = set;
    }

    public String getTitle() {
        return this.titleStr;
    }
    public String getExp() {
        return this.expStr;
    }

    public String getSet() {
        return this.setStr;
    }
}