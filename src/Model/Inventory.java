/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 *
 * @author Austyn
 */

public class Inventory {
  
  public static ObservableList<Part> parts = FXCollections.observableArrayList();
  public static FilteredList<Part> filteredParts = new FilteredList(parts, p -> true);
  
  public static void addPart(Part part){
    parts.add(part);
  }
  
  public static void updatePart(int index, Part part){
    parts.set(index, part);
  }
  
  public static void deletePart(Part part){
    parts.remove(part);
  }
  
  public static Part lookupPart(int id){
    
    for(Part p : parts){
      if(p.getPartID() == id){
        return p;
      }
    }
    
    return null;
  }
  
    public static Part lookupPartName(String name){
    
    for(Part p : parts){
      if(p.getName().toLowerCase().contains(name)){
        return p;
      }
    }
    
    return null;
  }
}
