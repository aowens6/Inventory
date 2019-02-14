
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PartController implements Initializable {
    
  @FXML
  private AnchorPane anchorPane;

  @FXML 
  private ToggleGroup sourceGroup;

  @FXML
  public Label sourceLabel, titleLabel;
  
  @FXML
  private RadioButton inHouse;

  @FXML
  private RadioButton outSourced;

  @FXML
  private TextField partID, partName, partPrice, partInv, partMax, partMin, partSource;
  
  @FXML
  public Button saveBtn;

  private boolean isInhouse;
  
  private boolean modifying;
  
  private Part part;
  
  private int index;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    isInhouse = true;
    
    //setting up a change listener to find the radio button source
    //and change the isInhouse flag accordingly
    sourceGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
      
      public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
        RadioButton checkedBtn = (RadioButton) t1.getToggleGroup().getSelectedToggle(); 
        
        if(checkedBtn.getText().equals("In-house")){
          isInhouse = true;
        }else{
          isInhouse = false;
        }
        System.out.println(isInhouse);
      }
      
    });
    
  }
  
  @FXML
  private void sourceToggle() {

    if(isInhouse){
      sourceLabel.setText("Company Name");
    }else{
      sourceLabel.setText("Machine ID");
    }

  }
  
  public void setPart(int index, Part part){
    
    modifying = true;
    titleLabel.setText("Modify Part");
    
    this.index = index;
    this.part = part;
    
    partID.setText(Integer.toString(part.getPartID()));
    partName.setText(part.getName());
    partPrice.setText(Double.toString(part.getPrice()));
    partInv.setText(Integer.toString(part.getInStock()));
    partMin.setText(Integer.toString(part.getMin()));
    partMax.setText(Integer.toString(part.getMax()));

    if(part instanceof InHouse){
      inHouse.setSelected(true);
      InHouse p = (InHouse) part;
      partSource.setText(Integer.toString(p.getMachineID()));
    }else{
      outSourced.setSelected(true);
      sourceLabel.setText("Company Name");
      Outsourced p = (Outsourced) part;
      partSource.setText(p.getCompanyName());
    }

  }
  
  private boolean isValidInput(){
    
    int partMinInt = 0;
    int partMaxInt = 0;
    int partInvInt = 0;
    double partPriceDbl = 0.0;
    int machineIdInt = 0;
    boolean validInput = true;
    
    if (partName.getText().trim().equals("") || 
        partPrice.getText().trim().equals("") ||
        partInv.getText().trim().equals("") ||
        partMax.getText().trim().equals("") ||    
        partMin.getText().trim().equals("") ){
      
      validInput = false;
      
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Missing Value");
      alert.setHeaderText("At least one of the inputs is missing a value");
      alert.setContentText("All fields are mandatory.");
      alert.showAndWait();
      
    }
    
    if(isInhouse){
      try{
        machineIdInt = Integer.parseInt(partSource.getText());
      }catch(NumberFormatException e){
        
        validInput = false;
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Not a number");
        alert.setHeaderText("Machine ID must be a number");
        alert.setContentText("Please check for numeric input");
        alert.showAndWait();
        
      }
    }
    
    try{
      partMinInt = Integer.parseInt(partMin.getText());
      partMaxInt = Integer.parseInt(partMax.getText());
      partInvInt = Integer.parseInt(partInv.getText());
      partPriceDbl = Double.parseDouble(partPrice.getText());
    }catch (NumberFormatException e){
      validInput = false;
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Not a number");
      alert.setHeaderText("Price, Inventory, Min and Max must be numbers");
      alert.setContentText("Please check number fields for numeric input");
      alert.showAndWait();
    }
    
    if(partMaxInt < partMinInt ||
       partInvInt > partMaxInt ||
       partInvInt < partMinInt) {
      
      validInput = false;
      
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Bounds Error");
      alert.setHeaderText("Inventory max and min out of bounds");
      alert.setContentText("Please check that the max value is greater than the min value"
                           + " and the inventory fits between them.");
      alert.showAndWait();
      
    }
    
    return validInput;
    
  }
  
  @FXML
  public void savePart() {
    
    if(isValidInput()){
      
      if(!isInhouse){
        
        Outsourced newPart = new Outsourced();
        newPart.setCompanyName(partSource.getText());
        newPart.setName(partName.getText());
        newPart.setPrice(Double.parseDouble(partPrice.getText())); 
        newPart.setInStock(Integer.parseInt(partInv.getText()));
        newPart.setMax(Integer.parseInt(partMax.getText()));
        newPart.setMin(Integer.parseInt(partMin.getText()));
        
        if(modifying){
          newPart.setPartID(part.getPartID());
          Inventory.updatePart(index, newPart);
        }else{
          newPart.setPartID(Inventory.partIdCount++);
          Inventory.addPart(newPart);
        }
        
      }else{//In-House radio button is selected
        InHouse newPart = new InHouse();
        newPart.setMachineID(Integer.parseInt(partSource.getText()));
        newPart.setName(partName.getText());
        newPart.setPrice(Double.parseDouble(partPrice.getText()));
        newPart.setInStock(Integer.parseInt(partInv.getText()));
        newPart.setMax(Integer.parseInt(partMax.getText()));
        newPart.setMin(Integer.parseInt(partMin.getText()));

        
        if(modifying){
          newPart.setPartID(part.getPartID());
          Inventory.updatePart(index, newPart);
        }else{
          newPart.setPartID(Inventory.partIdCount++);
          Inventory.addPart(newPart);
        }
        
      }
        
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
        
      }

  }
  
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
