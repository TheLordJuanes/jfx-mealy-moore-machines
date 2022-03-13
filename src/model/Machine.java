package model;

import java.util.ArrayList;

public class Machine {

    public String[][] minimizeMealy(String[][] matrix, boolean format) {
        Mealy mealy = new Mealy(matrix);
        return mealy.partitioning(format);
    }

    public String[][] minimizeMoore(String[][] tableMoore, int statesNumber) {
        String[][] minimized = null;
        for (int i = 1; i <= statesNumber; i++) {
            for (int j = 1; j <= tableMoore[i].length; j++) {

            }
        }
        return minimized;
    }

    public void printMatrix(String[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print("[" + matrix[i][j] + "]");
            }
            System.out.println();
        }
    }

    public String printArrayList(ArrayList<ArrayList<Integer>> groups) {
        String msg = "";
        for (int i = 0; i < groups.size(); i++) {
            for (int j = 0; j < groups.get(i).size(); j++) {
                msg += "[" + groups.get(i).get(j) + "]";
            }
            msg += "\n";
        }
        return msg;
    }
}