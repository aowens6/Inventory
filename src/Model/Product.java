
package Model;

import javafx.collections.ObservableList;

public class Product {
  
  private ObservableList<Part> associatedParts;
  private int productID;
  private String name;
  private double price;
  private int inStock;
  private int min;
  private int max;
  
  public Product() {
    
  }

  public Product(ObservableList<Part> associatedParts, int productID, String name, double price, int inStock, int min, int max) {
    this.associatedParts = associatedParts;
    this.productID = productID;
    this.name = name;
    this.price = price;
    this.inStock = inStock;
    this.min = min;
    this.max = max;
  }
  
  public void addAssociatedPart(Part part){
    associatedParts.add(part);
  }
  
  public boolean removeAssociatedPart(int id){
    
    for(Part p : associatedParts){
      if(p.getPartID() == id){
        associatedParts.remove(p);
        return true;
      }
    }
    return false;
  }
  
  
  public static Part lookupAssociatedPart(int id, ObservableList<Part> availableParts){

    for(Part p : availableParts){
      if(p.getPartID() == id){
        return p;
      }
    }
    return null;
  }
  
  public static Part lookupAssociatedPart(String name, ObservableList<Part> availableParts){

    for(Part p : availableParts){
      if(p.getName().toLowerCase().contains(name)){
        return p;
      }
    }
    return null;
  }

  public ObservableList<Part> getAssociatedParts() {
    return associatedParts;
  }

  public void setAssociatedParts(ObservableList<Part> associatedParts) {
    this.associatedParts = associatedParts;
  }
  
  public int getProductID() {
    return productID;
  }

  public void setProductID(int productID) {
    this.productID = productID;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public int getInStock() {
    return inStock;
  }

  public void setInStock(int inStock) {
    this.inStock = inStock;
  }

  public int getMin() {
    return min;
  }

  public void setMin(int min) {
    this.min = min;
  }

  public int getMax() {
    return max;
  }

  public void setMax(int max) {
    this.max = max;
  }
  
}
