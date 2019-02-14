
package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
  
  public static ObservableList<Part> allParts = FXCollections.observableArrayList();
  
  public static ObservableList<Product> products = FXCollections.observableArrayList();
  
  public static int partIdCount = 100;
  
  public static int productIdCount = 200;
  
  public static void addPart(Part part){
    allParts.add(part);
  }
  
  public static void updatePart(int index, Part part){
    allParts.set(index, part);
  }
  
  public static void deletePart(Part part){
    allParts.remove(part);
  }
  
  public static Part lookupPart(int id){
    
    for(Part p : allParts){
      if(p.getPartID() == id){
        return p;
      }
    } 
    return null;
  }
  
  public static Part lookupPart(String name){
    
    for(Part p : allParts){
      if(p.getName().toLowerCase().contains(name)){
        return p;
      }
    }
    return null;
  }
  
  
  //PRODUCTS
  public static void addProduct(Product product){
    products.add(product);
  }
  
  public static void updateProduct(int index, Product product){
    products.set(index, product);
  }
  
  public static boolean removeProduct(Product product){
    products.remove(product);
    return true;
  }
    
  public static Product lookupProduct(int id){
    for(Product p : products){
      if(p.getProductID() == id){
        return p;
      }
    }
    return null;
  }
  
  public static Product lookupProduct(String name){
    for(Product p : products){
      if(p.getName().toLowerCase().contains(name)){
        return p;
      }
    }
    return null;
  }
  
}
