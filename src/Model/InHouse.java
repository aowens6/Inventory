/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Austyn
 */
public class InHouse extends Part {
  
  int machineID;

  public int getMachineID() {
    return machineID;
  }

  public void setMachineID(int machineID) {
    this.machineID = machineID;
  }

  public InHouse(int machineID, int partID, String name, double price, int inStock, int min, int max) {
    super(partID, name, price, inStock, min, max);
    this.machineID = machineID;
  }
  
  
}
