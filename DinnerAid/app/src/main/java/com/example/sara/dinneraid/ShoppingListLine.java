package com.example.sara.dinneraid;

/**
 * A java class representation of a line in the Shopping List.
 */
class ShoppingListLine {

    private String content;
    private boolean isBought;
    private GroceryCategory category;
    private int id;

    /**
     * Default constructor
     *
     * @param inContent  Name of the grocery.
     * @param inCategory Category of the grocery.
     * @param inDone     Have you bought it?
     * @param inId       An integer for the line. Used for convenience when handling database references.
     */
    ShoppingListLine(String inContent, GroceryCategory inCategory, boolean inDone, int inId) {
        category = inCategory;
        content = inContent;
        isBought = inDone;
        id = inId;
    }

    /**
     * Get method.
     *
     * @return The Grocery category.
     */
    GroceryCategory getCategory() {
        return category;
    }

    /**
     * Get method.
     *
     * @return Whether the item has been bought or not.
     */
    boolean getIsBought() {
        return isBought;
    }

    /**
     * Getter for field "content"
     *
     * @return The thing to buy.
     */
    public String getContent() {
        return content;
    }

    /**
     * Getter method for the field "id".
     *
     * @return field value.
     */
    public int getId() {
        return id;
    }

    /**
     * Set the bought-value to true if false, and vice versa.
     */
    void toggleDone() {
        isBought = !isBought;
    }

    /**
     * A string representation of the line. Useful in debugging.
     *
     * @return a string representation.
     */
    public String toString() {
        String s = isBought ? "[x]" : "[ ]";
        return s + " " + content + " " + " (" + category.toString() + ")";
    }

}