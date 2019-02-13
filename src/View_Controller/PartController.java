
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
  private TextField addID, addName, addPrice, addInv, addMax, addMin, addSource;
  
  @FXML
  public Button saveBtn;

  private String selectedSource;
  
  private boolean modifying;
  
  private Part part;
  
  private int index;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    selectedSource = "In-house";
    
    //setting up a change listener to find the radio button source
    //and change the sourceLabel accordingly
    sourceGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
      
      public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
        RadioButton checkedBtn = (RadioButton) t1.getToggleGroup().getSelectedToggle(); 
        
        selectedSource = checkedBtn.getText();
      }
      
    });
    
    addID.setText(Integer.toString(Inventory.partIdCount));
    addName.setText("Boot");
    addPrice.setText("99");
    addInv.setText("19");
    addMin.setText("1");
    addMax.setText("91");
    addSource.setText("456");
    
  }
  
  @FXML
  private void sourceToggle() {

    if(selectedSource.equals("Outsourced")){
      sourceLabel.setText("Company Name");
    }else{
      sourceLabel.setText("Machine ID");
    }

  }
  
  public void setPart(int index, Part part){
    
    modifying = true;
    
    this.index = index;
    this.part = part;
    
    addID.setText(Integer.toString(part.getPartID()));
    addName.setText(part.getName());
    addPrice.setText(Double.toString(part.getPrice()));
    addInv.setText(Integer.toString(part.getInStock()));
    addMin.setText(Integer.toString(part.getMin()));
    addMax.setText(Integer.toString(part.getMax()));

    if(part instanceof InHouse){
      inHouse.setSelected(true);
      InHouse p = (InHouse) part;
      addSource.setText(Integer.toString(p.getMachineID()));
    }else{
      outSourced.setSelected(true);
      sourceLabel.setText("Company Name");
      Outsourced p = (Outsourced) part;
      addSource.setText(p.getCompanyName());
    }

  }
  
  private boolean isInput(){
    
    int partMin = 0;
    int partMax = 0;
    int partInv = 0;
    boolean input = true;
    
    if (addName.getText().trim().equals("") || 
        addPrice.getText().trim().equals("") ||
        addInv.getText().trim().equals("") ||
        addMax.getText().trim().equals("") ||    
        addMin.getText().trim().equals("") ){
      
      input = false;
      
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Missing Value");
      alert.setHeaderText("At least one of the inputs is missing a value");
      alert.setContentText("All fields are mandatory.");
      alert.showAndWait();
      
    }else{
      partMin = Integer.parseInt(addMin.getText());
      partMax = Integer.parseInt(addMax.getText());
      partInv = Integer.parseInt(addInv.getText());
    }
    
    if(partMax < partMin ||
       partInv > partMax ||
       partInv < partMin) {
      
      input = false;
      
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Bounds Error");
      alert.setHeaderText("Inventory max and min out of bounds");
      alert.setContentText("Please check that the max value is greater than the min value"
                           + " and the inventory fits between them.");
      alert.showAndWait();
      
    }
    
    return input;
    
  }
  
  @FXML
  public void savePart() {

    if(isInput()){
        
        if(selectedSource.equals("Outsourced")){
          part = new Outsourced(addSource.getText(),
                                     Inventory.partIdCount++, 
                                     addName.getText(), 
                                     Double.parseDouble(addPrice.getText()), 
                                     Integer.parseInt(addInv.getText()), 
                                     Integer.parseInt(addMax.getText()), 
                                     Integer.parseInt(addMin.getText()));
        }else{//In-House radio button is selected
          part = new InHouse(Integer.parseInt(addSource.getText()),
                                  Inventory.partIdCount++, 
                                  addName.getText(), 
                                  Double.parseDouble(addPrice.getText()), 
                                  Integer.parseInt(addInv.getText()), 
                                  Integer.parseInt(addMax.getText()), 
                                  Integer.parseInt(addMin.getText()));
        }
        
        if(modifying){
          Inventory.updatePart(index, part);
        }else{
          Inventory.addPart(part);
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
