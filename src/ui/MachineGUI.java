package ui;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import model.Machine;

public class MachineGUI {
	@FXML
    private JFXRadioButton mealy, moore;
    @FXML
    private JFXTextField states, alphabet;
    @FXML
    private JFXButton buttonMinimize;
    @FXML
    private HBox tableSpace;
	@FXML
    private Label arrow1, arrow2;
    @FXML
    private GridPane matrixInput, matrixIntermediate, matrixOutput;

	private JFXTextField[][] inputs;

	private Machine machine;

	public MachineGUI() {
		machine = new Machine();
	}

	@FXML
	public void minimize(ActionEvent event) {
		String[][] matrix = generateMatrix();
		String[][] intermediate = mealy.isSelected() ? machine.minimizeMealy(matrix, true) : machine.minimizeMoore(matrix, true);
		configureMatrix(matrixIntermediate, intermediate, 2);
		String[][] minimized = mealy.isSelected() ? machine.minimizeMealy(matrix, false) : machine.minimizeMoore(matrix, false);
		configureMatrix(matrixOutput, minimized, 4);
		arrow1.setText("  -->  ");
		arrow2.setText("  -->  ");
	}

	private String[][] generateMatrix() {
		int rows = Integer.parseInt(states.getText());
		String[] columns = alphabet.getText().split(",");
		int additional = moore.isSelected() ? 1 : 0;
		String[][] matrix = new String[rows + 1][columns.length + additional + 1];
		matrix[0][0] = "";
		for (int j = 0; j < columns.length; j++)
			matrix[0][j + 1] = columns[j];
		if (moore.isSelected())
			matrix[0][columns.length + additional] = "S";
		for (int i = 0; i < rows; i++)
			matrix[i + 1][0] = ((char) (65 + i)) + "";
		for (int i = 1; i <= rows; i++) {
			for (int j = 1; j <= columns.length + additional; j++)
				matrix[i][j] = inputs[i - 1][j - 1].getText();
		}
		return matrix;
	}

	private void configureMatrix(GridPane gridpane, String[][] matrix, int index){
		gridpane = new GridPane();
		gridpane.setAlignment(Pos.CENTER);
		gridpane.setGridLinesVisible(true);
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				Label temp = new Label(matrix[i][j]);
				placeInMatrix(temp, gridpane, i, j);
			}
		}
		tableSpace.getChildren().set(index, gridpane);
	}

	@FXML
	public void configureMealy(ActionEvent event) {
		moore.setSelected(false);
	}

	@FXML
	public void configureMoore(ActionEvent event) {
		mealy.setSelected(false);
	}

	@FXML
	public void statesIsNumber(KeyEvent event) {
		if (!Character.isDigit(event.getCharacter().charAt(0)))
			event.consume();
	}

	@FXML
	public void generateTable(ActionEvent event) {
		if (!mealy.isSelected() && !moore.isSelected()) {
			return;
		}

		int rows = Integer.parseInt(states.getText());
		String[] alph = alphabet.getText().split(",");
		int columns = alph.length;

		//Clear tables on screen if they already exist.
		matrixInput = new GridPane();
		matrixIntermediate = new GridPane();
		matrixOutput = new GridPane();

		matrixInput.setAlignment(Pos.CENTER);

		for (int i = 0; i < columns; i++) {
			Label temp = new Label(alph[i]);
			placeInMatrix(temp, matrixInput, 0, i + 1);
		}
		if (moore.isSelected()) {
			columns++;
			Label temp = new Label("S");
			placeInMatrix(temp, matrixInput, 0, columns);
		}
		for (int i = 0; i < rows; i++) {
			Label temp = new Label((char)('A' + i) + "");
			placeInMatrix(temp, matrixInput, i + 1, 0);
		}
		inputs = new JFXTextField[rows][columns];
		for (int i = 1; i <= rows; i++) {
			for (int j = 1; j <= columns; j++) {
				JFXTextField temp = new JFXTextField("");
				placeInMatrix(temp, matrixInput, i, j);
				inputs[i - 1][j - 1] = temp;
			}
		}
		tableSpace.getChildren().set(0, matrixInput);
		tableSpace.getChildren().set(2, matrixIntermediate);
		tableSpace.getChildren().set(4, matrixOutput);

		arrow1.setText("");
		arrow2.setText("");

		buttonMinimize.setVisible(true);
	}

	private <Type extends Control> void placeInMatrix(Type element, GridPane matrix, int row , int column) {
		matrix.add(element, column, row);
		GridPane.setHalignment(element, HPos.CENTER);
		GridPane.setValignment(element, VPos.CENTER);
		element.setStyle("-fx-font-size: 24px; -fx-font-family: Consolas");
		element.setPadding(new Insets(5, 10, 5, 10));
	}
}