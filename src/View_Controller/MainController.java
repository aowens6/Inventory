/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javafx.collections.transformation.FilteredList;
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

/**
 *
 * @author Austyn
 */
public class MainController extends Application{
  
  @FXML
  public TableView<Part> partsTable;

  @FXML
  TableColumn<Part, Integer> partID;

  @FXML
  TableColumn<Part, String> partName;

  @FXML
  TableColumn<Part, Integer> invLevel;

  @FXML
  TableColumn<Part, Double> pricePerUnit;

  @FXML
  TableView<Product> productsTable;

  @FXML
  TableColumn<Product, Integer> prodID;

  @FXML
  TableColumn<Product, String> prodName;

  @FXML
  TableColumn<Product, Integer> prodInvLvl;

  @FXML
  TableColumn<Product, Double> prodPrice;
  
  public ObservableList<Part> parts = Inventory.allParts;
  public ObservableList<Product> products = Inventory.products;
//  public FilteredList<Part> filteredParts = Inventory.filteredParts;
//  public static ModifyController modController;
  Part currentPart;
  
  @FXML
  TextField searchField, prodSearchField;

  @Override
  public void start(Stage stage){

    try{
      Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
      Scene scene = new Scene(root);
      
//      stage.setOnCloseRequest(event -> exitProgram(event));     
    
      stage.setTitle("Inventory");
      stage.setScene(scene);
      stage.show();
    }catch(Exception e){
      e.printStackTrace();
    }
    
  }
  
  @FXML
  private void viewAddPartStage(){
    System.out.println("You clicked add part");
    try{
      FXMLLoader addLoader = new FXMLLoader(getClass().getResource("AddPart.fxml"));
      Parent addPartParent = (Parent) addLoader.load();
      Scene addPartScene = new Scene(addPartParent);

      Stage stage = new Stage();
      
      AddPartController addController = addLoader.getController();
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
    
//    searchField.textProperty().addListener((observable, oldValue, newValue) -> {
//
//      filteredParts.setPredicate(part -> {
//        
//        if (newValue == null || newValue.isEmpty()) {
//          return true;
//        }
//
//        
//        if(Integer.toString(part.getPartID()).contains(newValue)){
//          partsTable.getSelectionModel().select(part);
//          return true;
//        }else if(newValue.length() > 1 && part.getName().toLowerCase().contains(newValue.toLowerCase())){
//          partsTable.getSelectionModel().select(part);
//          return true;
//        }  
//        
//        return false;
//      });
//      
//      SortedList<Part> sortedParts = new SortedList<>(filteredParts);
//      
//      sortedParts.comparatorProperty().bind(partsTable.comparatorProperty());
//      
//      partsTable.setItems(sortedParts);
//    
//    });
    
  }
  
  @FXML
  private void viewModifyPartStage(){
    System.out.println("You clicked modify part");
    try{
      FXMLLoader modPartFXML = new FXMLLoader(getClass().getResource("ModifyPart.fxml"));
      Parent modPartParent = (Parent) modPartFXML.load();
      Scene modPartScene = new Scene(modPartParent);
      
      ModifyPartController modController = modPartFXML.getController();
      modController.setPart(partsTable.getSelectionModel().getSelectedItem());
      
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
  public void addProduct(){
    try{
      FXMLLoader addProductFXML = new FXMLLoader(getClass().getResource("AddProduct.fxml"));
      Parent addProductParent = (Parent) addProductFXML.load();
      Scene addProdScene = new Scene(addProductParent);
      
      AddProductController addProdController = addProductFXML.getController();
      
      Stage stage = new Stage();

//      stage.setOnCloseRequest(event -> addProdController.cancel(event));
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
  public void modifyProduct(){
    try{
      FXMLLoader modifyProductFXML = new FXMLLoader(getClass().getResource("ModifyProduct.fxml"));
      Parent modifyProductParent = (Parent) modifyProductFXML.load();
      Scene modifyProdScene = new Scene(modifyProductParent);

      ModifyProductController modProdController = modifyProductFXML.getController();
      modProdController.setProduct(productsTable.getSelectionModel().getSelectedItem());
      
      Stage stage = new Stage();

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

    Inventory.removeProduct(productsTable.getSelectionModel().getSelectedItem());
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
  
  @FXML
  public void initialize(){
    partID.setCellValueFactory(new PropertyValueFactory<>("partID"));
    partName.setCellValueFactory(new PropertyValueFactory<>("name"));
    invLevel.setCellValueFactory(new PropertyValueFactory<>("inStock"));
    pricePerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));

    parts.add(new Outsourced("ABC company", 1, "hammer", 19.99, 20, 1, 99));
    parts.add(new Outsourced("ABC company", 2, "Tool belt", 19.99, 20, 1, 99));
    parts.add(new Outsourced("ABC company", 3, "Nail", 19.99, 20, 1, 99));
    parts.add(new Outsourced("ABC company", 4, "Chisel", 19.99, 20, 1, 99));
    parts.add(new InHouse(242, 4, "Chisel", 19.99, 20, 1, 99));

    partsTable.setItems(parts);

    prodID.setCellValueFactory(new PropertyValueFactory<>("productID"));
    prodName.setCellValueFactory(new PropertyValueFactory<>("name"));
    prodInvLvl.setCellValueFactory(new PropertyValueFactory<>("inStock"));
    prodPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
      
    products.add(new Product(parts, 546, "partPackage1", 29.00, 10, 1, 99));
    products.add(new Product(parts, 546, "partPackage2", 27.00, 10, 1, 99));
    products.add(new Product(parts, 546, "partPackage3", 26.00, 10, 1, 99));
    products.add(new Product(parts, 546, "partPackage4", 25.00, 10, 1, 99));

    productsTable.setItems(products);

    partsTable.getSelectionModel().selectFirst();
    productsTable.getSelectionModel().selectFirst();
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
  
  public static void main(String[] args){
    launch(args);
  }
  
}
