package com.example.sara.dinneraid;

import java.io.Serializable;

public class ShoppingListLine implements Serializable {
    public enum GroceryCategory {
        MEAT,DAIRY,VEGETABLES
    }

    private String content;
    private boolean isDone;
    private GroceryCategory category;

    public GroceryCategory getCategory() {
        return category;
    }

    public boolean getIsDone(){
        return isDone;
    }

    public String getContent() {
        return content;
    }

    public ShoppingListLine( String inContent, GroceryCategory inCategory) {
        category = inCategory;
        content = inContent;
    }

    public void toggleDone() {
        isDone = !isDone;
    }
}