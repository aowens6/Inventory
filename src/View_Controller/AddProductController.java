
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
  private TextField searchAvailParts, addProdID, addProdName, addProdInv, addProdPrice, addProdMax, addProdMin;
  
  private ObservableList<Part> availableParts, assdParts;
  
  private Part part;
  
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    
    prodPartID.setCellValueFactory(new PropertyValueFactory<>("partID"));
    prodPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
    prodPartInv.setCellValueFactory(new PropertyValueFactory<>("inStock"));
    prodPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    
    assdPartID.setCellValueFactory(new PropertyValueFactory<>("partID"));
    assdPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
    assdPartInv.setCellValueFactory(new PropertyValueFactory<>("inStock"));
    assdPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    
    addProdID.setText(Integer.toString(Inventory.productIdCount));
  }  
  
  @FXML
  private void searchAvailParts() {
    String searchAvailPartTxt = searchAvailParts.getText().toLowerCase();
    boolean found = false;
    Part foundPart;
    
    //First check if the search term matches a name of a part
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
    availableParts.add((Part) assdPartsTable.getSelectionModel().getSelectedItem());
    assdParts.remove(assdPartsTable.getSelectionModel().getSelectedIndex());
  }
  
  @FXML
  private void saveProduct() {
    
    int prodMin = Integer.parseInt(addProdMin.getText());
    int prodMax = Integer.parseInt(addProdMax.getText());
    int prodInv = Integer.parseInt(addProdInv.getText());

    if(prodMin > prodMax || 
       prodMax < prodMin ||
       prodInv < prodMin ||
       prodInv > prodMax ) {
      
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Bounds Error");
      alert.setHeaderText("Inventory max and min out of bounds");
      alert.setContentText("Please check that the max value is greater than the min value.");
      alert.showAndWait();
      
    }else if(assdParts.isEmpty()){
      
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Add Associated Part");
      alert.setHeaderText("Associated parts list is empty");
      alert.setContentText("Please make sure you have at least one associated part.");
      alert.showAndWait();
      
    }else{
      Product product = new Product();
      product.setAssociatedParts(assdParts);
      product.setProductID(Inventory.productIdCount++);
      product.setName(addProdName.getText());
      product.setPrice(Double.parseDouble(addProdPrice.getText()));
      product.setInStock(prodInv);
      product.setMax(prodMax);
      product.setMin(prodMin);

      Inventory.addProduct(product);

      Stage stage = (Stage) anchorPane.getScene().getWindow();
      stage.close();
    }
    
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
