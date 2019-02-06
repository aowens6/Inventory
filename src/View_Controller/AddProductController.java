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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Austyn
 */
public class AddProductController implements Initializable {

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
  private TableColumn<Part, Integer> assdPartID;
  
  @FXML
  private TableColumn<Part, String> assdPartName;
  
  @FXML
  private TableColumn<Part, Integer> assdPartInv;
  
  @FXML
  private TableColumn<Part, Double> assdPartPrice;
  
  @FXML
  private TextField searchAvailParts, addProdName, addProdInv, addProdPrice, addProdMax, addProdMin;
  
  public static ObservableList<Part> availableParts = Inventory.availableParts, assdParts;
  
  private Part part;
  
//  private Product product = new Product();
  
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    
    prodPartID.setCellValueFactory(new PropertyValueFactory<>("partID"));
    prodPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
    prodPartInv.setCellValueFactory(new PropertyValueFactory<>("inStock"));
    prodPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    
//    partsAddTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    
    assdPartID.setCellValueFactory(new PropertyValueFactory<>("partID"));
    assdPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
    assdPartInv.setCellValueFactory(new PropertyValueFactory<>("inStock"));
    assdPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
  
  }  
  
  @FXML
  private void searchAvailParts() {
    String searchAvailPartTxt = searchAvailParts.getText().toLowerCase();
    boolean found = false;
    Part foundPart;
    
    if(null != Product.lookupAssociatedPartName(searchAvailPartTxt)){
      foundPart = Product.lookupAssociatedPartName(searchAvailPartTxt);
      partsAddTable.getSelectionModel().select(foundPart);
      found = true;
    }else{ //otherwise, look for the id
      try{
        Integer searchInt = Integer.parseInt(searchAvailPartTxt);
        if (null != Product.lookupAssociatedPart(searchInt)){
          foundPart = Product.lookupAssociatedPart(searchInt);
          partsAddTable.getSelectionModel().select(foundPart);
          found = true;
        }
      }catch(NumberFormatException e){
        System.out.println("Not a number");
      }
    }
    
    if(found == false){
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Search Error");
      alert.setHeaderText("Part Not Found");
      alert.setContentText("The search term does not match any part");
      alert.showAndWait();
    }
    
    searchAvailParts.clear();
  }
  
  public void setAvailParts(ObservableList<Part> parts){
    //creating a copy of allParts when this is called from MainController
    this.availableParts = FXCollections.observableArrayList(parts);
    
    partsAddTable.setItems(availableParts);
  }
  
  @FXML
  private void addAssdPart() {
    
    part = (Part) partsAddTable.getSelectionModel().getSelectedItem();
    availableParts.remove(part);
    assdParts = assdPartsTable.getItems();
    assdParts.add(part);
    assdPartsTable.setItems(assdParts);
    
  }
  
  @FXML
  private void deleteAssdPart() {
    assdParts.remove(assdPartsTable.getSelectionModel().getSelectedIndex());
  }
  
  @FXML
  private void saveProduct() {
    Product product = new Product();
    product.setAssociatedParts(assdParts);
    product.setProductID(99);
    product.setName(addProdName.getText());
    product.setPrice(Double.parseDouble(addProdPrice.getText()));
    product.setInStock(Integer.parseInt(addProdInv.getText()));
    product.setMax(Integer.parseInt(addProdMax.getText()));
    product.setMin(Integer.parseInt(addProdMin.getText()));
    
    Inventory.addProduct(product);
  }

  @FXML
  public void cancel(Event e) {
    e.consume();
    System.out.println("You clicked cancel");

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION); 
    alert.initModality(Modality.APPLICATION_MODAL);
    alert.setTitle("Confirm Cancel");
    alert.setContentText("Are you sure you want to leave without saving?");
    alert.showAndWait();

    if(alert.getResult() == ButtonType.OK){
      Stage stage = (Stage) anchorPane.getScene().getWindow();
      stage.close();
    }else{
      alert.close();
    }
  }
  
}
