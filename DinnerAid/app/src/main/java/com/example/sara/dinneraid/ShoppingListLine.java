package com.example.sara.dinneraid;



public class ShoppingListLine {


    private String content;
    private boolean isDone;
    private GroceryCategory category;
    private int color;


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
        // provide default value for inDone
        this(inContent,inCategory,false);
    }

    public ShoppingListLine(String inContent, GroceryCategory inCategory, boolean inDone) {
        category = inCategory;
        content = inContent;
        isDone = inDone;
    }

    public void toggleDone() {
        isDone = !isDone;
    }

    public String toString() {
        String s = isDone ? "[x]" : "[ ]";
        return s+" "+content+" "+" ("+category.toString()+")";
    }
}