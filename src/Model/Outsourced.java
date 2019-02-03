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
public class Outsourced extends Part{
  
  String companyName;

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public Outsourced(String companyName, int partID, String name, double price, int inStock, int min, int max) {
    super(partID, name, price, inStock, min, max);
    this.companyName = companyName;
  }
  
  
  
}
