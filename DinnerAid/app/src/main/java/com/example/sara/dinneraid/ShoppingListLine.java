package com.example.sara.dinneraid;



public class ShoppingListLine {


    private String content;
    private boolean isDone;
    private GroceryCategory category;
    private int id;


    public GroceryCategory getCategory() {
        return category;
    }

    public boolean getIsDone(){
        return isDone;
    }

    public String getContent() {
        return content;
    }

    public ShoppingListLine( String inContent, GroceryCategory inCategory, int inId) {
        // provide default value for inDone
        this(inContent,inCategory,false, inId);
    }

    public ShoppingListLine(String inContent, GroceryCategory inCategory, boolean inDone, int inId) {
        category = inCategory;
        content = inContent;
        isDone = inDone;
        id = inId;
    }

    public void toggleDone() {
        isDone = !isDone;
    }

    public String toString() {
        String s = isDone ? "[x]" : "[ ]";
        return s+" "+content+" "+" ("+category.toString()+")";
    }

    public int getId() {
        return id;
    }
}