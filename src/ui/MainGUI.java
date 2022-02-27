package ui;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class MainGUI {

    @FXML
    private JFXTextField nStates;

    @FXML
    private JFXTextField alphabet;
    
    @FXML
    private HBox tSpace;
    
    @FXML
    private GridPane matrixInput;
    
    @FXML
    private GridPane matrixOutput;
    
    @FXML
    private JFXRadioButton mealy;

    @FXML
    private JFXRadioButton moore;
    
    @FXML
    public void configMealy(ActionEvent event) {
    	moore.setSelected(false);
    }
    
    @FXML
    public void configMoore(ActionEvent event) {
    	mealy.setSelected(false);
    }
    
    @FXML
    public void gTable(ActionEvent event) {
    	int n = Integer.parseInt(nStates.getText());
    	String[] alph = alphabet.getText().split(",");
    	matrixInput = new GridPane();
    	matrixInput.setAlignment(Pos.CENTER);
		
		placeInMatrix(new Label(""), matrixInput, 0, 0);
		for(int i=0; i<n; i++) {
			placeInMatrix(new Label(((char)(65+i))+""), matrixInput, 0, i+1);
		}
		for(int i=0; i<alph.length; i++) {
			placeInMatrix(new Label(alph[i]), matrixInput, i+1, 0);
		}
		int cols = alph.length;
		if(moore.isSelected()) {
			placeInMatrix(new Label("S"), matrixInput, cols+1, 0);
			cols++;
		}
		for(int i=1; i<=n; i++) {
			for(int j=1; j<=cols; j++) {
				placeInMatrix(new JFXTextField(""), matrixInput, j, i);
			}
		}
		tSpace.getChildren().remove(0);
    	tSpace.getChildren().add(0, matrixInput);
    	matrixInput.setVisible(true);
    }
    
    /*private void experimental() {
    	int n = 9;
    	String[] alph = {"0", "1"};
    	String[] A = {"A", "I", "C", "1"};
    	String[] B = {"B", "B", "I", "1"};
    	String[] C = {"C", "C", "G", "1"};
    	String[] D = {"D", "I", "C", "0"};
    	String[] E = {"E", "D", "E", "0"};
    	String[] F = {"F", "I", "C", "0"};
    	String[] G = {"G", "E", "F", "0"};
    	String[] H = {"H", "H", "A", "1"};
    	String[] I = {"I", "A", "C", "1"};
    	String[] AB = {"A", "B"};
    	JFXTextField[][] data = new JFXTextField[9][alph.length];
    	ArrayList<ArrayList<Integer>> equivalence = new ArrayList<>();
    	for(int i=0; i<n-1; i++) {
    		for(int j=i+1; j<n; j++) {
        		///if(!data[i][alph.length].getText().equals(data[j][alph.length].getText())) {
        			//
        		//}
    		}
    	} 
    }*/
    
    private <Type extends Control> void placeInMatrix(Type element, GridPane matrix, int column, int row) {
		matrix.add(element, column, row);
		GridPane.setHalignment(element, HPos.CENTER);
		GridPane.setValignment(element, VPos.CENTER);
		element.setStyle("-fx-font-size: 24px; -fx-font-family: Consolas");
		element.setPadding(new Insets(5, 10, 5, 10));
	}
}
