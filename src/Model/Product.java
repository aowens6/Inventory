/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import static Model.Inventory.allParts;
import Model.Part;
import View_Controller.AddProductController;
import java.util.ArrayList;
import javafx.collections.ObservableList;

/**
 *
 * @author Austyn
 */
public class Product {
  
  ObservableList<Part> associatedParts;
  int productID;
  String name;
  double price;
  int inStock;
  int min;
  int max;
  
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
//    System.out.println("In addpart");
    this.associatedParts.add(part);
  }
  
  public boolean removeAssociatedPart(Integer index){
    this.associatedParts.remove(index);
    return true;
  }
  
  public static Part lookupAssociatedPart(int id){

    for(Part p : AddProductController.availableParts){
      if(p.getPartID() == id){
        return p;
      }
    }
    return null;
  }
  
  public static Part lookupAssociatedPartName(String name){

    for(Part p : AddProductController.availableParts){
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
