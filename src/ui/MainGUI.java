package ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class MainGUI {

	@FXML
	private JFXButton btnMinimize;

	@FXML
	private JFXTextField nStates;

	@FXML
	private JFXTextField alphabet;

	@FXML
	private JFXTextField separator;

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

	private Machine machine;

	private JFXTextField[][] mInput;

	public MainGUI() {
		machine = new Machine();
	}

	@FXML
	public void minimizeTable(ActionEvent event) {
		/*int statesNumber = Integer.parseInt(nStates.getText());
		String[] alph = alphabet.getText().split(separator.getText());
		int alphabetLength = alph.length;
		int addCols = moore.isSelected() ? 1 : 0;
		String[][] mString = new String[statesNumber + 1][alphabetLength + addCols + 1];
		mString[0][0] = "";
		for (int i = 0; i < alph.length; i++)
			mString[0][i + 1] = alph[i];
		if (moore.isSelected())
			mString[0][alphabetLength + addCols] = "S";
		for (int i = 0; i < statesNumber; i++)
			mString[i + 1][0] = ((char) (65 + i)) + "";
		for (int i = 1; i <= statesNumber; i++) {
			for (int j = 1; j <= alphabetLength + addCols; j++)
				mString[i][j] = mInput[i - 1][j - 1].getText();
		}
		printMatrix(mString);*/
		int statesNumber = 9;
		String[][] mString = {{"","0","1"},{"A","B,0","C,0"}, {"B","C,1","D,1"}, {"C","D,0","E,0"}, {"D","C,1","B,1"}, {"E","F,1","E,1"}, {"F","G,0","C,0"}, {"G","F,1","G,1"}, {"H","I,1","B,0"}, {"I","H,1","D,0"}};
		String[][] minimized = mealy.isSelected() ? machine.minimizeMealy(mString, statesNumber) : machine.minimizeMoore(mString, statesNumber);
	}

	public void printMatrix(String[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				System.out.print("[" + matrix[i][j] + "]");
			}
			System.out.println();
		}
	}

	@FXML
	public void configMealy(ActionEvent event) {
		moore.setSelected(false);
	}

	@FXML
	public void configMoore(ActionEvent event) {
		mealy.setSelected(false);
	}

	@FXML
	public void statesIsNumber(KeyEvent event) {
		if (!Character.isDigit(event.getCharacter().charAt(0))) {
			event.consume();
		}
	}

	@FXML
	public void gTable(ActionEvent event) {
		if (!mealy.isSelected() && !moore.isSelected()) {
			return;
		}

		int n = Integer.parseInt(nStates.getText());
		String[] alph = alphabet.getText().split(separator.getText());
		matrixInput = new GridPane();
		matrixInput.setAlignment(Pos.CENTER);

		placeInMatrix(new Label(""), matrixInput, 0, 0);
		for (int i = 0; i < alph.length; i++) {
			placeInMatrix(new Label(alph[i]), matrixInput, i + 1, 0);
		}
		int cols = alph.length;
		if (moore.isSelected()) {
			placeInMatrix(new Label("S"), matrixInput, cols + 1, 0);
			cols++;
		}
		for (int i = 0; i < n; i++) {
			placeInMatrix(new Label(((char) (65 + i)) + ""), matrixInput, 0, i + 1);
		}
		mInput = new JFXTextField[n][cols];
		if (mealy.isSelected()) {
			for (int i = 1; i <= n; i++) {
				for (int j = 1; j <= cols; j++) {
					JFXTextField aux = new JFXTextField(",");
					aux.focusedProperty().addListener(new ChangeListener<Boolean>() {
						@Override
						public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
								Boolean newPropertyValue) {
							if (!newPropertyValue) {
								if (aux.getText() != null && aux.getText().length() >= 3
										&& (',' == aux.getText().charAt(1)) &&
										('0' == aux.getText().charAt(2) || '1' == aux.getText().charAt(2))) {
									boolean ok = false;
									for (int i = 0; i < n && !ok; i++) {
										if (65 + i == aux.getText().charAt(0)) {
											ok = true;
										}
									}
									if (!ok) {
										aux.setText(",");
									}
								} else {
									aux.setText(",");
								}
							}
						}
					});
					placeInMatrix(aux, matrixInput, j, i);
					mInput[i - 1][j - 1] = aux;
				}
			}
		} else {
			for (int i = 1; i <= n; i++) {
				for (int j = 1; j <= cols; j++) {
					JFXTextField aux = new JFXTextField("");
					if (j == cols) {
						aux.focusedProperty().addListener(new ChangeListener<Boolean>() {
							@Override
							public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
									Boolean newPropertyValue) {
								if (!newPropertyValue) {
									if (aux.getText() != null && aux.getText().length() == 1
											&& ('0' == aux.getText().charAt(0) || '1' == aux.getText().charAt(0))) {

									} else {
										aux.setText("");
									}
								}
							}
						});
					} else {
						aux.focusedProperty().addListener(new ChangeListener<Boolean>() {
							@Override
							public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
									Boolean newPropertyValue) {
								if (!newPropertyValue) {
									if (aux.getText() != null && aux.getText().length() == 1) {
										boolean ok = false;
										for (int i = 0; i < n && !ok; i++) {
											if (65 + i == aux.getText().charAt(0)) {
												ok = true;
											}
										}
										if (!ok) {
											aux.setText("");
										}
									} else {
										aux.setText("");
									}
								}
							}
						});
					}
					placeInMatrix(aux, matrixInput, j, i);
					mInput[i - 1][j - 1] = aux;
				}
			}
		}
		tSpace.getChildren().remove(0);
		tSpace.getChildren().add(0, matrixInput);
		matrixInput.setVisible(true);
	}

	/*
	 * private void experimental() {
	 * int n = 9;
	 * String[] alph = {"0", "1"};
	 * String[] A = {"A", "I", "C", "1"};
	 * String[] B = {"B", "B", "I", "1"};
	 * String[] C = {"C", "C", "G", "1"};
	 * String[] D = {"D", "I", "C", "0"};
	 * String[] E = {"E", "D", "E", "0"};
	 * String[] F = {"F", "I", "C", "0"};
	 * String[] G = {"G", "E", "F", "0"};
	 * String[] H = {"H", "H", "A", "1"};
	 * String[] I = {"I", "A", "C", "1"};
	 * String[] AB = {"A", "B"};
	 * JFXTextField[][] data = new JFXTextField[9][alph.length];
	 * ArrayList<ArrayList<Integer>> equivalence = new ArrayList<>();
	 * for(int i=0; i<n-1; i++) {
	 * for(int j=i+1; j<n; j++) {
	 * ///if(!data[i][alph.length].getText().equals(data[j][alph.length].getText()))
	 * {
	 * //
	 * //}
	 * }
	 * }
	 * }
	 */

	private <Type extends Control> void placeInMatrix(Type element, GridPane matrix, int column, int row) {
		matrix.add(element, column, row);
		GridPane.setHalignment(element, HPos.CENTER);
		GridPane.setValignment(element, VPos.CENTER);
		element.setStyle("-fx-font-size: 24px; -fx-font-family: Consolas");
		element.setPadding(new Insets(5, 10, 5, 10));
	}
}
