/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Austyn
 */

public class Inventory {
  
  public static ObservableList<Part> parts = FXCollections.observableArrayList();
  
  public static void addPart(Part part){
    parts.add(part);
  }
  
  public static void updatePart(int index, Part part){
    parts.set(index, part);
  }
}
