package Model;

public class InHouse extends Part {
  
  private int machineID;

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
