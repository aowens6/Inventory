
package View_Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ModifyPartController{

  @FXML
  private AnchorPane anchorPane;

  @FXML
  private RadioButton inHouse;

  @FXML
  private RadioButton outSourced;

  @FXML
  private Label sourceLabel;

  @FXML
  private TextField modifyID, modifyName, modifyPrice, modifyInv, modifyMax, modifyMin, modSourceID;
  
  private Part part;
  
  public void setPart(Part part){
    
    this.part = part;
    
    modifyID.setText(Integer.toString(part.getPartID()));
    modifyName.setText(part.getName());
    modifyPrice.setText(Double.toString(part.getPrice()));
    modifyInv.setText(Integer.toString(part.getInStock()));
    modifyMin.setText(Integer.toString(part.getMin()));
    modifyMax.setText(Integer.toString(part.getMax()));

    if(part instanceof InHouse){
      inHouse.setSelected(true);
      InHouse p = (InHouse) part;
      modSourceID.setText(Integer.toString(p.getMachineID()));
    }else{
      outSourced.setSelected(true);
      sourceLabel.setText("Company Name");
      Outsourced p = (Outsourced) part;
      modSourceID.setText(p.getCompanyName());
    }

  }
  
  @FXML
  private void savePart(){

    this.part.setName(modifyName.getText());
    this.part.setPrice(Double.parseDouble(modifyPrice.getText()));
    this.part.setInStock(Integer.parseInt(modifyInv.getText()));
    this.part.setMax(Integer.parseInt(modifyMax.getText()));
    this.part.setMin(Integer.parseInt(modifyMin.getText()));
    
    if(this.part instanceof InHouse){
      InHouse p = (InHouse) this.part;
      p.setMachineID(Integer.parseInt(modSourceID.getText()));
    }else{
      Outsourced p = (Outsourced) this.part;
      p.setCompanyName(modSourceID.getText());
    }
    
    Inventory.updatePart(Inventory.allParts.indexOf(this.part), part);
    
    Stage stage = (Stage) anchorPane.getScene().getWindow();
    stage.close();
  }
  
  public void cancel(Event e) {
      
    e.consume();

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION); 
    alert.initModality(Modality.APPLICATION_MODAL);
    alert.setTitle("Confirm exit");
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
