
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
    
    availableParts = FXCollections.observableArrayList(Inventory.allParts);
    partsAddTable.setItems(availableParts);
  }  
  
  @FXML
  private void searchAvailParts() {
    String searchAvailPartTxt = searchAvailParts.getText().toLowerCase();
    boolean found = false;
    Part foundPart;
    
    foundPart = Product.lookupAssociatedPart(searchAvailPartTxt, availableParts);
    //First check if the search term matches a name of a part
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

    Part addPart = partsAddTable.getSelectionModel().getSelectedItem();
    if(addPart != null){
      availableParts.remove(addPart);
      assdParts = assdPartsTable.getItems();
      assdParts.add(addPart);
      assdPartsTable.setItems(assdParts);
    }
    
  }
  
  @FXML
  private void deleteAssdPart() {
    
    Part removePart = assdPartsTable.getSelectionModel().getSelectedItem();
    if (removePart != null){
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Confirm Delete");
      alert.setHeaderText("Confirm Delete");
      alert.setContentText("Are you sure you want to delete this associated part?");
      alert.showAndWait();
      
      if(alert.getResult() == ButtonType.OK){
        availableParts.add(removePart);
        assdParts.remove(removePart);
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
    
    if (addProdName.getText().trim().equals("") ||
        addProdInv.getText().trim().equals("") ||
        addProdPrice.getText().trim().equals("") ||
        addProdMax.getText().trim().equals("") ||
        addProdMin.getText().trim().equals("") ){
      
      validInput = false;
      
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Missing Value");
      alert.setHeaderText("At least one of the inputs is missing a value");
      alert.setContentText("All fields are mandatory.");
      alert.showAndWait();
      
    }
      
    try{
      prodMinInt = Integer.parseInt(addProdMin.getText());
      prodMaxInt = Integer.parseInt(addProdMax.getText());
      prodInvInt = Integer.parseInt(addProdInv.getText());
      prodPriceDbl = Double.parseDouble(addProdPrice.getText());
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
      Product product = new Product();
      product.setAssociatedParts(assdParts);
      product.setProductID(Inventory.productIdCount++);
      product.setName(addProdName.getText());
      product.setPrice(Double.parseDouble(addProdPrice.getText()));
      product.setInStock(Integer.parseInt(addProdInv.getText()));
      product.setMax(Integer.parseInt(addProdMax.getText()));
      product.setMin(Integer.parseInt(addProdMin.getText()));

      Inventory.addProduct(product);

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
