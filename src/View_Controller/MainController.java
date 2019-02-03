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
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
  
  public ObservableList<Part> parts = Inventory.parts;
//  public static ModifyController modController;
  Part currentPart;

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
      FXMLLoader addLoader = new FXMLLoader(getClass().getResource("AddPartFXML.fxml"));
      Parent addPartParent = (Parent) addLoader.load();
      Scene addPartScene = new Scene(addPartParent);

      Stage stage = new Stage();
      
      AddController addController = addLoader.getController();
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
  private void viewModifyPartStage(){
    System.out.println("You clicked modify part");
    try{
      FXMLLoader modPartFXML = new FXMLLoader(getClass().getResource("ModifyFXML.fxml"));
      Parent modPartParent = (Parent) modPartFXML.load();
      Scene modPartScene = new Scene(modPartParent);
      
      ModifyController modController = modPartFXML.getController();
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

//      ObservableList<Product> products = FXCollections.observableArrayList();
//      
//      products.add(new Product(parts, 546, "partPackage1", 29.00, 10, 1, 99));
//      products.add(new Product(parts, 546, "partPackage2", 27.00, 10, 1, 99));
//      products.add(new Product(parts, 546, "partPackage3", 26.00, 10, 1, 99));
//      products.add(new Product(parts, 546, "partPackage4", 25.00, 10, 1, 99));
//      
//      productsTable.setItems(products);

    partsTable.getSelectionModel().selectFirst();
//      productsTable.getSelectionModel().selectFirst();
  }
  
  public static void main(String[] args){
    launch(args);
  }
  
}
