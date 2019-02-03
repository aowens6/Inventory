/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Model.Part;
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

  public Product(ObservableList<Part> associatedParts, int productID, String name, double price, int inStock, int min, int max) {
    this.associatedParts = associatedParts;
    this.productID = productID;
    this.name = name;
    this.price = price;
    this.inStock = inStock;
    this.min = min;
    this.max = max;
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
