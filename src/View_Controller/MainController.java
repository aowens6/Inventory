
package View_Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import Model.Product;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController extends Application{
  
  @FXML
  private TableView<Part> partsTable;

  @FXML
  private TableColumn<Part, Integer> partID;

  @FXML
  private TableColumn<Part, String> partName;

  @FXML
  private TableColumn<Part, Integer> invLevel;

  @FXML
  private TableColumn<Part, Double> pricePerUnit;

  @FXML
  private TableView<Product> productsTable;

  @FXML
  private TableColumn<Product, Integer> prodID;

  @FXML
  private TableColumn<Product, String> prodName;

  @FXML
  private TableColumn<Product, Integer> prodInvLvl;

  @FXML
  private TableColumn<Product, Double> prodPrice;
  
  private ObservableList<Part> parts = Inventory.allParts;
  
  private ObservableList<Product> products = Inventory.products;
  
  @FXML
  private TextField searchField, prodSearchField;

  @Override
  public void start(Stage stage){

    try{
      Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
      Scene scene = new Scene(root);
      
      stage.setOnCloseRequest(event -> exitProgram(event));     
    
      stage.setTitle("Inventory");
      stage.setScene(scene);
      stage.show();
    }catch(Exception e){
      e.printStackTrace();
    }
    
  }
  
  @FXML
  public void initialize(){
    partID.setCellValueFactory(new PropertyValueFactory<>("partID"));
    partName.setCellValueFactory(new PropertyValueFactory<>("name"));
    invLevel.setCellValueFactory(new PropertyValueFactory<>("inStock"));
    pricePerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));

    parts.add(new Outsourced("ABC company", Inventory.partIdCount++, "hammer", 19.99, 20, 1, 99));
    parts.add(new Outsourced("ABC company", Inventory.partIdCount++, "Tool belt", 19.99, 20, 1, 99));
    parts.add(new Outsourced("ABC company", Inventory.partIdCount++, "Nail", 19.99, 20, 1, 99));
    parts.add(new Outsourced("ABC company", Inventory.partIdCount++, "Chisel", 19.99, 20, 1, 99));
    parts.add(new InHouse(242, Inventory.partIdCount++, "Chisel", 19.99, 20, 1, 99));

    partsTable.setItems(parts);

    prodID.setCellValueFactory(new PropertyValueFactory<>("productID"));
    prodName.setCellValueFactory(new PropertyValueFactory<>("name"));
    prodInvLvl.setCellValueFactory(new PropertyValueFactory<>("inStock"));
    prodPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
      
    products.add(new Product(FXCollections.observableArrayList(parts), Inventory.productIdCount++, "partPackage1", 29.00, 10, 1, 99));
    products.add(new Product(FXCollections.observableArrayList(parts), Inventory.productIdCount++, "partPackage2", 27.00, 10, 1, 99));
    products.add(new Product(FXCollections.observableArrayList(parts), Inventory.productIdCount++, "partPackage3", 26.00, 10, 1, 99));
    products.add(new Product(FXCollections.observableArrayList(parts), Inventory.productIdCount++, "partPackage4", 25.00, 10, 1, 99));

    productsTable.setItems(products);

    partsTable.getSelectionModel().selectFirst();
    productsTable.getSelectionModel().selectFirst();
  }
  
  @FXML
  private void viewAddPartStage(){
    try{
      FXMLLoader addLoader = new FXMLLoader(getClass().getResource("Part.fxml"));
      Parent addPartParent = (Parent) addLoader.load();
      Scene addPartScene = new Scene(addPartParent);

      Stage stage = new Stage();
      
      PartController addController = addLoader.getController();
      stage.setOnCloseRequest(event -> addController.cancel(event));
      
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.setScene(addPartScene);
      stage.setTitle("Add Part");
      stage.show();

    }catch(Exception e){
      e.printStackTrace();
    }
    
  }
  
  @FXML
  private void searchParts(){
    
    String searchText = searchField.getText().toLowerCase();
    boolean found = false;
    Part foundPart;
    
    //First check if the search term matches a name of a part
    if(null != Inventory.lookupPartName(searchText)){
      foundPart = Inventory.lookupPartName(searchText);
      partsTable.getSelectionModel().select(foundPart);
      found = true;
    }else{ //otherwise, look for the id
      try{
        Integer searchInt = Integer.parseInt(searchText);
        if (null != Inventory.lookupPart(searchInt)){
          foundPart = Inventory.lookupPart(searchInt);
          partsTable.getSelectionModel().select(foundPart);
          found = true;
        }
      }catch(NumberFormatException e){
        System.out.println("Not a number");
      }
    }
    
    if(found == false){
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Search Error");
      alert.setHeaderText("Part Not Found");
      alert.setContentText("The search term does not match any part");
      alert.showAndWait();
    }
    
    searchField.clear();
    
  }
  
  @FXML
  private void viewModifyPartStage(){
    try{
      FXMLLoader modPartFXML = new FXMLLoader(getClass().getResource("Part.fxml"));
      Parent modPartParent = (Parent) modPartFXML.load();
      Scene modPartScene = new Scene(modPartParent);
      
      PartController modController = modPartFXML.getController();
      modController.setPart(partsTable.getSelectionModel().getSelectedIndex(),
                            partsTable.getSelectionModel().getSelectedItem());
      
      Stage stage = new Stage();
      
      stage.setOnCloseRequest(event -> modController.cancel(event));
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.setScene(modPartScene);
      stage.setTitle("Modify Part");
      stage.show();
      
    }catch(Exception e){
      e.printStackTrace();
    }
    
  } 
  
    
  @FXML
  private void deletePart(){
    
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION); 
    alert.initModality(Modality.APPLICATION_MODAL);
    alert.setTitle("Confirm Delete");
    alert.setContentText("Are you sure you want to delete this part?");
    alert.showAndWait();
    
    if(alert.getResult() == ButtonType.OK){
      Inventory.deletePart(partsTable.getSelectionModel().getSelectedItem());
    }else{
      alert.close();
    }  
  }
  
  @FXML
  public void viewAddProductStage(){
    try{
      FXMLLoader addProductFXML = new FXMLLoader(getClass().getResource("AddProduct.fxml"));
      Parent addProductParent = (Parent) addProductFXML.load();
      Scene addProdScene = new Scene(addProductParent);
      
      AddProductController addProdController = addProductFXML.getController();
      
      Stage stage = new Stage();

      stage.setOnCloseRequest(event -> addProdController.cancel(event));
      addProdController.setAvailParts(parts);
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.setScene(addProdScene);
      stage.setTitle("Add Product");
      stage.show();
      
    }catch(Exception e){
      e.printStackTrace();
    }
  }
  
  @FXML
  public void viewModifyProductStage(){
    try{
      FXMLLoader modifyProductFXML = new FXMLLoader(getClass().getResource("ModifyProduct.fxml"));
      Parent modifyProductParent = (Parent) modifyProductFXML.load();
      Scene modifyProdScene = new Scene(modifyProductParent);

      ModifyProductController modProdController = modifyProductFXML.getController();
      modProdController.setProduct(productsTable.getSelectionModel().getSelectedIndex(),
                                   productsTable.getSelectionModel().getSelectedItem());
      modProdController.setAvailParts(parts);
      
      Stage stage = new Stage();
      
      stage.setOnCloseRequest(event -> modProdController.cancel(event));
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.setScene(modifyProdScene);
      stage.setTitle("Modify Product");
      stage.show();
      
    }catch(Exception e){
      e.printStackTrace();
    }
  }
  
  @FXML
  private void deleteProduct(){

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION); 
    alert.initModality(Modality.APPLICATION_MODAL);
    alert.setTitle("Confirm Delete");
    alert.setContentText("Are you sure you want to delete this product? It still has a part associated with it.");
    alert.showAndWait();

    if(alert.getResult() == ButtonType.OK){
      Inventory.removeProduct(productsTable.getSelectionModel().getSelectedItem());
    }else{
      alert.close();
    }
    
  }
  
  @FXML
  private void searchProducts(){
    String searchText = prodSearchField.getText().toLowerCase();
    boolean found = false;
    Product foundProduct;
    
    //First check if the search term matches a name of a product
    if(null != Inventory.lookupProductName(searchText)){
      foundProduct = Inventory.lookupProductName(searchText);
      productsTable.getSelectionModel().select(foundProduct);
      found = true;
    }else{ //otherwise, look for the id
      try{
        Integer searchInt = Integer.parseInt(searchText);
        if (null != Inventory.lookupProduct(searchInt)){
          foundProduct = Inventory.lookupProduct(searchInt);
          productsTable.getSelectionModel().select(foundProduct);
          found = true;
        }
      }catch(NumberFormatException e){
        System.out.println("Not a number");
      }
    }
    
    if(found == false){
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Search Error");
      alert.setHeaderText("Part Not Found");
      alert.setContentText("The search term does not match any part");
      alert.showAndWait();
    }
    
    prodSearchField.clear();
  }

  public void exitProgram(Event e){
    
    e.consume();
    
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION); 
    alert.initModality(Modality.APPLICATION_MODAL);
    alert.setTitle("Confirm exit");
    alert.setContentText("Are you sure you want to exit?");
    alert.showAndWait();
    
    if(alert.getResult() == ButtonType.OK){
      Platform.exit();
    }else{
      alert.close();
    }
    
  }

  public static void main(String[] args){
    launch(args);
  }
}