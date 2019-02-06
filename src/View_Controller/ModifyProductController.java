/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Austyn
 */
public class ModifyProductController implements Initializable {
  
  @FXML
  private AnchorPane anchorPane;
  
  @FXML
  private TableView partsAddTable, assdPartsTable;
  
  @FXML
  private TableColumn<Part, Integer> prodPartID;
  
  @FXML
  private TableColumn<Part, String> prodPartName;
  
  @FXML
  private TableColumn<Part, Integer> prodPartInv;
  
  @FXML
  private TableColumn<Part, Double> prodPartPrice;
  
  @FXML
  private TableColumn<Part, Integer> associatedPartID;
  
  @FXML
  private TableColumn<Part, String> associatedPartName;
  
  @FXML
  private TableColumn<Part, Integer> associatedPartInv;
  
  @FXML
  private TableColumn<Part, Double> associatedPartPrice;
  
  @FXML 
  private TextField prodID, prodName, prodInvLvl, prodPrice, prodMax, prodMin;
  
  private ObservableList<Part> parts = Inventory.allParts;
  
  private Product product;
  
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    prodPartID.setCellValueFactory(new PropertyValueFactory<>("partID"));
    prodPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
    prodPartInv.setCellValueFactory(new PropertyValueFactory<>("inStock"));
    prodPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    
    partsAddTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    
    partsAddTable.setItems(parts);
    
    associatedPartID.setCellValueFactory(new PropertyValueFactory<>("partID"));
    associatedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
    associatedPartInv.setCellValueFactory(new PropertyValueFactory<>("inStock"));
    associatedPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    
    
  }  
  
  public void setProduct(Product product){
    
//    this.product = product;
    
    prodID.setText(Integer.toString(product.getProductID()));
    prodName.setText(product.getName());
    prodInvLvl.setText(Integer.toString(product.getInStock()));
    prodPrice.setText(Double.toString(product.getPrice()));
    prodMax.setText(Integer.toString(product.getMax()));
    prodMin.setText(Integer.toString(product.getMin()));
    
    assdPartsTable.setItems(parts);

  }
  
  @FXML
  private void searchProduct(ActionEvent event) {
      System.out.println("You clicked searchProduct");
  }
  
  @FXML
  private void addProduct(ActionEvent event) {
      System.out.println("You clicked addProduct");
  }
  
  @FXML
  private void deleteProduct(ActionEvent event) {
      System.out.println("You clicked deleteProduct");
  }
  
  @FXML
  private void saveProduct(ActionEvent event) {
      System.out.println("You clicked saveProduct");
  }

  @FXML
  private void cancel(ActionEvent event) {
      System.out.println("You clicked cancel");
  }
  
}
