/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * @Authors: Carlos Jimmy Pantoja and Juan Esteban Caicedo
 * @Date: March, 15th 2022
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
*/
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

	// -----------------------------------------------------------------
	// Attributes
	// -----------------------------------------------------------------

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

	// -----------------------------------------------------------------
	// Relations
	// -----------------------------------------------------------------

	private Machine machine;

	// -----------------------------------------------------------------
	// Methods
	// -----------------------------------------------------------------

	/**
     * Name: MachineGUI <br>
     * <br> GUI constructor method. <br>
    */
	public MachineGUI() {
		machine = new Machine();
	}

	/**
     * Name: initialize
     * GUI initializer method. <br>
    */
	public void initialize() {
		alphabet.addEventFilter(KeyEvent.KEY_TYPED, evt -> {
			if (" ".equals(evt.getCharacter()))
				evt.consume();
		});
	}

	/**
     * Name: minimize
     * Method used to start the minimization process of the Mealy and Moore machines. <br>
	 * <b>post: </b> An intermediate table with the final blocks of the partitioning, and the final table M' with the minimized machine is shown to the user. <br>
     * @param event - event representing the start the minimization process of the finite state machines - event = ActionEvent
    */
	@FXML
	public void minimize(ActionEvent event) {
		String[][] matrix = generateMatrix();
		String[][] intermediate = mealy.isSelected() ? machine.minimizeMealy(matrix, true)
				: machine.minimizeMoore(matrix, true);
		configureMatrix(matrixIntermediate, intermediate, 2);
		String[][] minimized = mealy.isSelected() ? machine.minimizeMealy(matrix, false)
				: machine.minimizeMoore(matrix, false);
		configureMatrix(matrixOutput, minimized, 4);
		arrow1.setText("  -->  ");
		arrow2.setText("  -->  ");
	}

	/**
     * Name: generateMatrix
     * Method used to create a Java String matrix according to the entered values in the GUI input table. <br>
	 * <b>pre: </b> The input table was already filled with the desired values of the user. <br>
	 * <b>post: </b> A Java String matrix with the entered values in the input table. <br>
	 * @return A String matrix with the entered values in the GUI input table.
    */
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

	/**
     * Name: configureMatrix
     * Method used to configure a GUI table from the intermediate or final Java String matrix returned from the model. <br>
	 * <b>pre: </b> The Java String matrix returned from the model is already initialized and isn't empty. <br>
	 * <b>post: </b> A GUI table is displayed with the values passed from the Java String matrix. <br>
	 * @param gridPane - GUI matrix - gridPane = GridPane
	 * @param matrix - Java String matrix with the intermediate table or the final minimized table - matrix = String[][], matrix != null
	 * @param index - index to place the Grid pane in the HBox - index = int, index != null
    */
	private void configureMatrix(GridPane gridPane, String[][] matrix, int index) {
		gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setGridLinesVisible(true);
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				Label temp = new Label(matrix[i][j]);
				placeInMatrix(temp, gridPane, i, j);
			}
		}
		tableSpace.getChildren().set(index, gridPane);
	}

	/**
     * Name: configureMealy
     * Method used to handle the selection change of the Moore radio button when choosing the type of machine. <br>
	 * <b>post: </b> The Moore radio button is deselected when the Mealy radio button is selected. <br>
	 * @param event - event representing handling the selection change of the Moore radio button when choosing the type of machine - event = ActionEvent
    */
	@FXML
	public void configureMealy(ActionEvent event) {
		moore.setSelected(false);
	}

	/**
     * Name: configureMoore
     * Method used to handle the selection change of the Mealy radio button when choosing the type of machine. <br>
	 * <b>post: </b> The Mealy radio button is deselected when the Moore radio button is selected. <br>
	 * @param event - event representing handling the selection change of the Mealy radio button when choosing the type of machine - event = ActionEvent
    */
	@FXML
	public void configureMoore(ActionEvent event) {
		mealy.setSelected(false);
	}

	/**
     * Name: statesIsNumber
     * Method used to check that only numbers can be typed when typing the number of states. <br>
	 * <b>post: </b> The key event when typing the number of states is consumed if the key isn't a number, otherwise nothing happens. <br>
	 * @param event - event representing the entering of a key in the States text field - event = KeyEvent
    */
	@FXML
	public void statesIsNumber(KeyEvent event) {
		if (!Character.isDigit(event.getCharacter().charAt(0)))
			event.consume();
	}

	/**
     * Name: generateTable
     * Method used to generate the GUI input table, according to the selected machine type. <br>
	 * <b>post: </b> A GUI input table is generating, according to the selected machine type. <br>
	 * @param event - event representing the generation of the GUI input table - event = ActionEvent
    */
	@FXML
	public void generateTable(ActionEvent event) {
		if ((mealy.isSelected() || moore.isSelected()) && !states.getText().equals("")) {
			int rows = Integer.parseInt(states.getText());
			String[] alph = alphabet.getText().split(",");
			int columns = alph.length;

			// Clear tables on screen if they already exist.
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
				Label temp = new Label((char) ('A' + i) + "");
				placeInMatrix(temp, matrixInput, i + 1, 0);
			}
			inputs = new JFXTextField[rows][columns];
			for (int i = 1; i <= rows; i++) {
				for (int j = 1; j <= columns; j++) {
					JFXTextField temp = new JFXTextField("");
					temp.setMaxWidth(120);
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
	}

	/**
     * Name: placeInMatrix
     * Method used to place any element Type that extends from Control in a Grid pane (matrix) in a specified row and column. <br>
	 * <b>pre: </b> The Grid pane (matrix) is already initialized. <br>
	 * <b>post: </b> Element Type extending from Control is placed in the Grid pane (matrix) in a specified row and column. <br>
	 * @param element - element type as cell in the matrix - element = Type, element != null
	 * @param matrix - GUI matrix without all its type cells defined - matrix = GridPane, matrix != null
	 * @param row - row in the matrix - row = int, row != null
	 * @param column - column in the matrix - column = int, column != null
    */
	private <Type extends Control> void placeInMatrix(Type element, GridPane matrix, int row, int column) {
		matrix.add(element, column, row);
		GridPane.setHalignment(element, HPos.CENTER);
		GridPane.setValignment(element, VPos.CENTER);
		element.setStyle("-fx-font-size: 24px; -fx-font-family: Consolas");
		element.setPadding(new Insets(5, 10, 5, 10));
	}
}