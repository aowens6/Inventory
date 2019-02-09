
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
  private TextField searchAvailParts, prodID, prodName, prodInvLvl, prodPrice, prodMax, prodMin;
  
  private ObservableList<Part> availableParts, assdParts;
  
  private Product product;
  
  private int index;
  
  private Part part;
  
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    
    prodPartID.setCellValueFactory(new PropertyValueFactory<>("partID"));
    prodPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
    prodPartInv.setCellValueFactory(new PropertyValueFactory<>("inStock"));
    prodPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

    associatedPartID.setCellValueFactory(new PropertyValueFactory<>("partID"));
    associatedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
    associatedPartInv.setCellValueFactory(new PropertyValueFactory<>("inStock"));
    associatedPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

  }  
  
  public void setProduct(int index, Product product){
    
    this.product = product;
    this.index = index;
    
    prodID.setText(Integer.toString(product.getProductID()));
    prodName.setText(product.getName());
    prodInvLvl.setText(Integer.toString(product.getInStock()));
    prodPrice.setText(Double.toString(product.getPrice()));
    prodMax.setText(Integer.toString(product.getMax()));
    prodMin.setText(Integer.toString(product.getMin()));

    assdParts = FXCollections.observableArrayList(product.getAssociatedParts());
    assdPartsTable.setItems(assdParts);

  }
  
  public void setAvailParts(ObservableList<Part> parts){
    //creating a copy of allParts when this is called from MainController
    this.availableParts = FXCollections.observableArrayList(parts);
    
    partsAddTable.setItems(availableParts);
    
  }
  
  @FXML
  private void searchAvailParts() {
    
    String searchAvailPartTxt = searchAvailParts.getText().toLowerCase();
    boolean found = false;
    Part foundPart;
    
    if(null != Product.lookupAssociatedPartName(searchAvailPartTxt, availableParts)){
      foundPart = Product.lookupAssociatedPartName(searchAvailPartTxt, availableParts);
      partsAddTable.getSelectionModel().select(foundPart);
      found = true;
    }else{ //otherwise, look for the id
      try{
        Integer searchInt = Integer.parseInt(searchAvailPartTxt);
        if (null != Product.lookupAssociatedPart(searchInt, availableParts)){
          foundPart = Product.lookupAssociatedPart(searchInt, availableParts);
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
  
  @FXML
  private void addAssdPart() {
    
    part = (Part) partsAddTable.getSelectionModel().getSelectedItem();
    availableParts.remove(part);
    product.addAssociatedPart(part);
    assdPartsTable.setItems(product.getAssociatedParts());
    
  }
  
  @FXML
  private void deleteAssdPart() {
    
    part = (Part) assdPartsTable.getSelectionModel().getSelectedItem();
    product.removeAssociatedPart(assdPartsTable.getSelectionModel().getSelectedIndex());
    availableParts.add(part);
    assdPartsTable.setItems(product.getAssociatedParts());
    
  }

  @FXML
  private void saveProduct() {
    
    this.product.setName(prodName.getText());
    this.product.setPrice(Double.parseDouble(prodPrice.getText()));
    this.product.setInStock(Integer.parseInt(prodInvLvl.getText()));
    this.product.setMax(Integer.parseInt(prodMax.getText()));
    this.product.setMin(Integer.parseInt(prodMin.getText()));
    
    Inventory.updateProduct(this.index, product);
    
    Stage stage = (Stage) anchorPane.getScene().getWindow();
    stage.close();
    
  }

  @FXML
  public void cancel(Event e) {
    
    e.consume();

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
