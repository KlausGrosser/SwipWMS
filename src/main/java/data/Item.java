package data;

import java.util.Date;

/**
 * This stores details of an Item
 * @author riteshp
 *
 */
public class Item implements Comparable<Item> {
  //Fields:
  /**
   * Current state of the item
   */
  private String state;
  /**
   * Category of the item
   */
  private String category;
  /**
   * The warehouse ID
   */
  private int warehouse;
  /**
   * The date when this item was added to the warehouse
   */
  private Date dateOfStock;

  //Constructors:
  public Item(){}

  public Item(String state, String category, int warehouse, Date dateOfStock){
    this.state = state.substring(0,1).toUpperCase()+state.substring(1);
    this.category = category;
    this.warehouse = warehouse;
    this.dateOfStock = dateOfStock;
  }


  // Setters and getters:
  public String getState() {
    return state;
  }
  public void setState(String state) {
    this.state = state;
  }
  public String getCategory() {
    return category;
  }
  public void setCategory(String category) {
    this.category = category;
  }
  public int getWarehouse() {
    return warehouse;
  }
  public void setWarehouse(int warehouse) {
    this.warehouse = warehouse;
  }
  public Date getDateOfStock() {
    return dateOfStock;
  }
  public void setDateOfStock(Date dateOfStock) {
    this.dateOfStock = dateOfStock;
  }

  //Public methods:
  /**
   * Textual representation of the item.
   * It's a combination of the state followed by the category
   */
  @Override
  public String toString() {
    return String.format("%s %s", this.getState(), this.getCategory().toLowerCase());
  }

  @Override
  public int compareTo(Item o) {
    return state.compareTo(o.getState());
  }
}