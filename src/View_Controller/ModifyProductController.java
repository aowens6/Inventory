
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
  private TableView<Part> partsAddTable, assdPartsTable;
  
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
    
    availableParts = FXCollections.observableArrayList(Inventory.allParts);
    
    partsAddTable.setItems(availableParts);

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

    assdParts = product.getAssociatedParts();
    assdPartsTable.setItems(assdParts);

  }
  
  @FXML
  private void searchAvailParts() {
    
    String searchAvailPartTxt = searchAvailParts.getText().toLowerCase();
    boolean found = false;
    Part foundPart;
    
    foundPart = Product.lookupAssociatedPart(searchAvailPartTxt, availableParts);
    
    if(null != foundPart){
      
      partsAddTable.getSelectionModel().select(foundPart);
      found = true;
    }else{ //otherwise, look for the id
      try{
        Integer searchInt = Integer.parseInt(searchAvailPartTxt);
        foundPart = Product.lookupAssociatedPart(searchInt, availableParts);
        if (null != foundPart){
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

    boolean partExists = false;
    
    Part addPart = partsAddTable.getSelectionModel().getSelectedItem();
    
    for(Part p : product.getAssociatedParts()){
      if(p.equals(addPart)) {
        partExists = true;
      }
    }
    
    if (partExists){
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Part Exists");
      alert.setHeaderText("Part already on list");
      alert.setContentText("This part is already on the associated parts list for this product");
      alert.showAndWait();
    } else {
      availableParts.remove(addPart);
      product.addAssociatedPart(addPart);
      assdPartsTable.refresh();
    }
    
  }
  
  @FXML
  private void deleteAssdPart() {
    
    Part removePart = assdPartsTable.getSelectionModel().getSelectedItem();
    
    if(removePart != null){
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Confirm Delete");
      alert.setHeaderText("Confirm Delete");
      alert.setContentText("Are you sure you want to delete this associated part?");
      alert.showAndWait();

      if(alert.getResult() == ButtonType.OK){
        availableParts.add(removePart);
        product.removeAssociatedPart(removePart.getPartID());
      }else{
        alert.close();
      }
    }

  }
  
  private boolean isValidInput(){
    
    int prodMinInt = 0;
    int prodMaxInt = 0;
    int prodInvInt = 0;
    double prodPriceDbl = 0.0;
    boolean validInput = true;
    
    if (prodName.getText().trim().equals("") ||
        prodInvLvl.getText().trim().equals("") ||
        prodPrice.getText().trim().equals("") ||
        prodMax.getText().trim().equals("") ||
        prodMin.getText().trim().equals("") ){
      
      validInput = false;
      
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Missing Value");
      alert.setHeaderText("At least one of the inputs is missing a value");
      alert.setContentText("All fields are mandatory.");
      alert.showAndWait();
      
    }
    
    try{
      prodMinInt = Integer.parseInt(prodMin.getText());
      prodMaxInt = Integer.parseInt(prodMax.getText());
      prodInvInt = Integer.parseInt(prodInvLvl.getText());
      prodPriceDbl = Double.parseDouble(prodPrice.getText());
    }catch(NumberFormatException e){
      validInput = false;
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Not a number");
      alert.setHeaderText("Price, Inventory, Min and Max must be numbers");
      alert.setContentText("Please check number fields for numeric input");
      alert.showAndWait();
    }
    
    if (prodMaxInt < prodMinInt ||
        prodInvInt < prodMinInt ||
        prodInvInt > prodMaxInt){
      
      validInput = false;
      
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Bounds Error");
      alert.setHeaderText("Inventory max and min out of bounds");
      alert.setContentText("Please check that the max value is greater than the min value"
                           + " and the inventory fits between them.");
      alert.showAndWait();
      
    }
    
    if (assdPartsTable.getItems().isEmpty()){
      
      validInput = false;
      
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Associated Parts Missing");
      alert.setHeaderText("Associated parts is empty");
      alert.setContentText("Please make sure there is at least one associated part");
      alert.showAndWait();
      
    }
    
    return validInput;
    
  }

  @FXML
  private void saveProduct() {
    
    if (isValidInput()){
      
      product.setName(prodName.getText());
      product.setPrice(Double.parseDouble(prodPrice.getText()));
      product.setInStock(Integer.parseInt(prodInvLvl.getText()));
      product.setMax(Integer.parseInt(prodMax.getText()));
      product.setMin(Integer.parseInt(prodMin.getText()));

      Inventory.updateProduct(index, product);

      Stage stage = (Stage) anchorPane.getScene().getWindow();
      stage.close();
      
    }

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
