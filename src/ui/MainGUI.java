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
	private GridPane matrixOutput, matrixIntermediate;

	@FXML
	private JFXRadioButton mealy;

	@FXML
	private JFXRadioButton moore;

	@FXML
	private JFXButton tableGen;

	@FXML
	private Label lbStates, lbAlph, lbChoose, lbSep;

	private JFXTextField[][] mInput;

	private Machine machine;

	public MainGUI() {
		machine = new Machine();
	}

	@FXML
	public void minimizeTable(ActionEvent event) {
		String[][] mString = generateMatrix();
		if (mealy.isSelected())
			minimizeMealy(mString);
		else
			mooreMinimize(mString);
	}

	private String[][] generateMatrix() {
		int statesNumber = Integer.parseInt(nStates.getText());
		String[] alph = alphabet.getText().split(separator.getText());
		int alphabetLength = alph.length;
		int addCols = moore.isSelected() ? 1 : 0;
		String[][] matrix = new String[statesNumber + 1][alphabetLength + addCols + 1];
		matrix[0][0] = "";
		for (int i = 0; i < alph.length; i++)
			matrix[0][i + 1] = alph[i];
		if (moore.isSelected())
			matrix[0][alphabetLength + addCols] = "S";
		for (int i = 0; i < statesNumber; i++)
			matrix[i + 1][0] = ((char) (65 + i)) + "";
		for (int i = 1; i <= statesNumber; i++) {
			for (int j = 1; j <= alphabetLength + addCols; j++)
				matrix[i][j] = mInput[i - 1][j - 1].getText();
		}
		return matrix;
	}

	private void mooreMinimize(String[][] mString) {
		String[][] minimized = machine.minimizeMoore(mString, true);
		minimized = machine.minimizeMoore(mString, false);
	}

	private void minimizeMealy(String[][] mString) {
		String[][] minimized = machine.minimizeMealy(mString, false);
		matrixOutput = new GridPane();
		for (int i = 1; i < minimized.length; i++) {
			for (int j = 1; j < minimized[0].length; j++) {
				Label aux = new Label(minimized[i][j]);
				placeInMatrix(aux, matrixOutput, j, i);
			}
		}
		for (int j = 0; j < minimized[0].length; j++) {
			Label aux = new Label(minimized[0][j]);
			placeInMatrix(aux, matrixOutput, j, 0);
		}
		for (int i = 0; i < minimized.length; i++) {
			Label aux = new Label(minimized[i][0]);
			placeInMatrix(aux, matrixOutput, 0, i);
		}
		tSpace.getChildren().remove(2);
		tSpace.getChildren().add(2, matrixOutput);
		// ------------------------------------------
		String[][] temp = machine.minimizeMealy(mString, true);
		matrixIntermediate = new GridPane();
		for (int i = 1; i < temp.length; i++) {
			for (int j = 2; j < temp[0].length; j++) {
				Label aux = new Label(temp[i][j]);
				placeInMatrix(aux, matrixIntermediate, j, i);
			}
		}
		for (int j = 0; j < temp[0].length; j++) {
			Label aux = new Label(temp[0][j]);
			placeInMatrix(aux, matrixIntermediate, j, 0);
		}

		for (int i = 1; i < temp.length; i++) {
			placeInMatrix(new Label(temp[i][0]), matrixIntermediate, 0, i);
			placeInMatrix(new Label(temp[i][1]), matrixIntermediate, 1, i);
		}
		tSpace.getChildren().remove(1);
		tSpace.getChildren().add(1, matrixIntermediate);
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
		if (!Character.isDigit(event.getCharacter().charAt(0)))
			event.consume();
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
					JFXTextField aux = new JFXTextField("");
					/*
					 * aux.focusedProperty().addListener(new ChangeListener<Boolean>() {
					 * 
					 * @Override
					 * public void changed(ObservableValue<? extends Boolean> arg0, Boolean
					 * oldPropertyValue,
					 * Boolean newPropertyValue) {
					 * if (!newPropertyValue) {
					 * if (aux.getText() != null && aux.getText().length() >= 3
					 * && (',' == aux.getText().charAt(1)) &&
					 * ('0' == aux.getText().charAt(2) || '1' == aux.getText().charAt(2))) {
					 * boolean ok = false;
					 * for (int i = 0; i < n && !ok; i++) {
					 * if (65 + i == aux.getText().charAt(0))
					 * ok = true;
					 * }
					 * if (!ok)
					 * aux.setText(",");
					 * } else {
					 * aux.setText(",");
					 * }
					 * }
					 * }
					 * });
					 */
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
											if (65 + i == aux.getText().charAt(0))
												ok = true;
										}
										if (!ok)
											aux.setText("");
									} else
										aux.setText("");
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
	}

	private <Type extends Control> void placeInMatrix(Type element, GridPane matrix, int column, int row) {
		matrix.add(element, column, row);
		GridPane.setHalignment(element, HPos.CENTER);
		GridPane.setValignment(element, VPos.CENTER);
		element.setStyle("-fx-font-size: 24px; -fx-font-family: Consolas");
		element.setPadding(new Insets(5, 10, 5, 10));
	}
}