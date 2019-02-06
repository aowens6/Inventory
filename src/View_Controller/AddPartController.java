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
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Austyn
 */
public class AddPartController implements Initializable {
    
  @FXML
  private AnchorPane anchorPane;

  @FXML 
  private ToggleGroup sourceGroup;

  @FXML
  private RadioButton inHouse;

  @FXML
  private RadioButton outSourced;

  @FXML
  private Label sourceLabel;

  @FXML
  private TextField addID;

  @FXML
  private TextField addName;

  @FXML
  private TextField addPrice;

  @FXML
  private TextField addInv;

  @FXML
  private TextField addMax;

  @FXML
  private TextField addMin;

  @FXML
  private TextField addMachineID;

  private String selectedSource;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    selectedSource = "In-house";
    sourceGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
      public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
        RadioButton chk = (RadioButton)t1.getToggleGroup().getSelectedToggle(); // Cast object to radio button
        selectedSource = chk.getText();
      }
    });
    
// DUMMY DATA DELETE LATER
    addID.setText("3");
    addName.setText("Boot");
    addPrice.setText("99");
    addInv.setText("19");
    addMin.setText("1");
    addMax.setText("91");
    addMachineID.setText("456");
    
    
//    stage.setOnCloseRequest(event -> cancel(event));
  }
  

  @FXML
  private void savePart() {
    System.out.println("You clicked savePart");

    if(selectedSource.equals("Outsourced")){
      Part part = new Outsourced("Company Name",
                                  100, 
                                  addName.getText(), 
                                  Double.parseDouble(addPrice.getText()), 
                                  Integer.parseInt(addInv.getText()), 
                                  Integer.parseInt(addMax.getText()), 
                                  Integer.parseInt(addMin.getText()));
      Inventory.addPart(part);
    }else{
      Part part = new InHouse(3000,
                              100, 
                              addName.getText(), 
                              Double.parseDouble(addPrice.getText()), 
                              Integer.parseInt(addInv.getText()), 
                              Integer.parseInt(addMax.getText()), 
                              Integer.parseInt(addMin.getText()));

      Inventory.addPart(part);
    }

    Stage stage = (Stage) anchorPane.getScene().getWindow();
    stage.close();

  }

  
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

  @FXML
  private void sourceToggle() {

    if(selectedSource.equals("Outsourced")){
      sourceLabel.setText("Company Name");
    }else{
      sourceLabel.setText("Machine ID");
    }

  }

}
