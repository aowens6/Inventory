
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

public class AddPartController implements Initializable {
    
  @FXML
  private AnchorPane anchorPane;

  @FXML 
  private ToggleGroup sourceGroup;

  @FXML
  private Label sourceLabel;

  @FXML
  private TextField addID, addName, addPrice, addInv, addMax, addMin, addSource;

  private String selectedSource;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    selectedSource = "In-house";
    sourceGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
      
      public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
        RadioButton chk = (RadioButton) t1.getToggleGroup().getSelectedToggle(); 
        
        selectedSource = chk.getText();
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
  private void savePart() {
    
    int partMin = Integer.parseInt(addMin.getText());
    int partMax = Integer.parseInt(addMax.getText());
    int partInv = Integer.parseInt(addInv.getText());
    
    if(partMin > partMax ||
       partMax < partMin ||
       partInv > partMax ||
       partInv < partMin){
      
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Bounds Error");
      alert.setHeaderText("Inventory max and min out of bounds");
      alert.setContentText("Please check that the max value is greater than the min value"
                           + " and the inventory fits between them.");
      alert.showAndWait();
      
    }else{
      if(selectedSource.equals("Outsourced")){
        Part part = new Outsourced(addSource.getText(),
                                    Inventory.partIdCount++, 
                                    addName.getText(), 
                                    Double.parseDouble(addPrice.getText()), 
                                    partInv, 
                                    partMax, 
                                    partMin);
        Inventory.addPart(part);
      }else{
        Part part = new InHouse(Integer.parseInt(addSource.getText()),
                                Inventory.partIdCount++, 
                                addName.getText(), 
                                Double.parseDouble(addPrice.getText()), 
                                partInv, 
                                partMax, 
                                partMin);

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

  @FXML
  private void sourceToggle() {

    if(selectedSource.equals("Outsourced")){
      sourceLabel.setText("Company Name");
    }else{
      sourceLabel.setText("Machine ID");
    }

  }

}
