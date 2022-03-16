/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * @Authors: Carlos Jimmy Pantoja and Juan Esteban Caicedo
 * @Date: March, 15th 2022
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
*/
package model;

import java.util.ArrayList;

public class Moore {

    // -----------------------------------------------------------------
    // Attributes
    // -----------------------------------------------------------------

    private String[] states, inputSymbols, outputSymbols;
    private String[][] successors;

    // -----------------------------------------------------------------
    // Methods
    // -----------------------------------------------------------------

    /**
     * Name: Moore <br>
     * <br> Constructor method of a connected Moore machine. <br>
     * <b>pre: </b> The machine represented in the Java String matrix is already initialized and isn't empty. <br>
	 * <b>post: </b> A connected Moore machine is created. <br>
     * @param machine - Java String matrix representing a Moore machine - matrix = String[][], matrix != null
    */
    public Moore(String[][] machine) {
        machine = connected(machine);
        fillMoore(machine);
    }

    /**
     * Name: connected <br>
     * <br> Method used to make connected a Moore machine. <br>
     * <b>pre: </b> The machine represented in the Java String matrix is already initialized and isn't empty. <br>
	 * <b>post: </b> A connected Moore machine. <br>
     * @param machine - Java String matrix representing a Moore machine - matrix = String[][], matrix != null
     * @return A String matrix with the connected machine.
    */
    public String[][] connected(String[][] machine) {
        ArrayList<Integer> included = connectedStates(machine);
        return connectedMachine(machine, included);
    }

    /**
     * Name: connectedMachine <br>
     * <br> Private method used to make connected a Moore machine. <br>
     * <b>pre: </b> The machine represented in the Java String matrix is already initialized and isn't empty. <br>
	 * <b>post: </b> A connected Moore machine. <br>
     * @param machine - Java String matrix representing a Moore machine - matrix = String[][], matrix != null
     * @param included - integers list representing the  - included = ArrayList of Integer, matrix != null
     * @return A String matrix with the connected machine.
    */
    private String[][] connectedMachine(String[][] machine, ArrayList<Integer> included) {
        String[][] connected = new String[included.size() + 1][machine[0].length];
        int additional = 0;
        connected[0] = machine[0];
        for (int i = 1; i < machine.length; i++) {
            if (indexOfList(included, i - 1) == -1)
                additional++;
            else
                connected[i - additional] = machine[i];
        }
        return connected;
    }

    /**
     * Name: connectedStates <br>
     * <br> Method used to get the connected states in a Moore machine. <br>
     * <b>pre: </b> The machine represented in the Java String matrix is already initialized and isn't empty. <br>
	 * <b>post: </b> The connected states in the machine in question are found. <br>
     * @param machine - Java String matrix representing a Moore machine - matrix = String[][], matrix != null
     * @return A list of integers representing the connected states in the machine in question.
    */
    private ArrayList<Integer> connectedStates(String[][] machine) {
        ArrayList<Integer> included = new ArrayList<>();
        included.add(0);
        for (int i = 0; i < included.size(); i++) {
            for (int j = 1; j < machine[0].length - 1; j++) {
                int index = machine[included.get(i) + 1][j].charAt(0) - 'A';
                if (indexOfList(included, index) == -1)
                    included.add(index);
            }
        }
        return included;
    }

    /**
     * Name: indexOfList <br>
     * <br> Method used to get the index of an element in a list of integers. <br>
     * <b>pre: </b> The list is already initialized. <br>
	 * <b>post: </b> The index of an element in the list is found. <br>
     * @param list - list of integers - list = ArrayList of Integer, list != null
     * @param num - integer to use for the index search - num = int, num != null
     * @return An integer representing the index of an element in a list of integers.
    */
    private int indexOfList(ArrayList<Integer> list, int num) {
        int index = -1;
        for (int i = 0; i < list.size() && (index == -1); i++)
            index = list.get(i) == num ? i : -1;
        return index;
    }

    /**
     * Name: indexState <br>
     * <br> Method used to get the index of a state in the list of states of the Moore machine. <br>
     * <b>pre: </b> The list of states of the Moore machine is already initialized. <br>
	 * <b>post: </b> The index of a state in the list of states of the Moore machine is found. <br>
     * @param index - the state converted to char (integer format) - list = ArrayList of Integer, list != null
     * @return An integer representing the index of a state in the list of states.
    */
    private int indexState(int index) {
        for (int i = 0; i < states.length; i++) {
            if (states[i].charAt(0) - 'A' == index)
                return i;
        }
        return -1;
    }

    /**
     * Name: partitioning <br>
     * <br> Method used to do the partitioning algorithm for a Moore machine. <br>
     * <b>pre: </b> The input symbols list is already initialized and filled, the output symbols list and the successors matrix. <br>
	 * <b>post: </b> The partitioning algorithm is done for the Moore machine. <br>
     * @param format - format of the table to be displayed represented by True for intermediate table and False for final table - format = boolean, format != null
     * @return A String matrix representing the correspondent minimized Moore machine.
    */
    public String[][] partitioning(boolean format) {
        ArrayList<ArrayList<Integer>> groups = step2A();
        boolean stop = false;
        while (!stop) {
            ArrayList<ArrayList<Integer>> subGroups1 = step2B(groups), subGroups2 = step2B(subGroups1);
            stop = isTheSame(subGroups1, subGroups2);
            groups = subGroups2;
        }
        String[][] minimize = format(groups, format);
        return minimize;
    }

    /**
     * Name: format <br>
     * <br> Method used to prepare the format of the output table to be displayed. <br>
     * <b>pre: </b> The input symbols list is already initialized and filled, also the output symbols list and the successors matrix. <br>
	 * <b>post: </b> The partitioning algorithm is already done, the format of the output table is given to it. <br>
     * @param groups - final groups obtained after doing the partitioning algorithm - groups = ArrayList of ArrayList of Integers, groups != null
     * @param format - format of the table to be displayed represented by True for intermediate table and False for final table - format = boolean, format != null
     * @return A String matrix representing the minimized Moore machine in the specified format.
    */
    private String[][] format(ArrayList<ArrayList<Integer>> groups, boolean format) {
        String[][] minimize = new String[groups.size() + 1][inputSymbols.length + 2];
        minimize[0][0] = format ? "Blocks" : "New Names";
        minimize[0][minimize[0].length - 1] = "S";
        for (int j = 0; j < inputSymbols.length; j++) {
            minimize[0][j + 1] = inputSymbols[j];
        }
        minimize = format ? intermediateFormat(groups, minimize) : finalFormat(groups, minimize);
        return minimize;
    }

    /**
     * Name: finalFormat <br>
     * <br> Method used to put the Moore table in the final format. <br>
     * <b>pre: </b> The partitioning algorithm is already done, the input symbols list is already initialized and filled, also the output symbols list and the successors matrix. <br>
	 * <b>post: </b> The Moore table is put in the final format. <br>
     * @param groups - final groups obtained after doing the partitioning algorithm - groups = ArrayList of ArrayList of Integers, groups != null
     * @param minimize - Java String matrix representing a Moore machine after partitioning - matrix = String[][], matrix != null
     * @return A String matrix representing the Moore machine in the final format.
    */
    private String[][] finalFormat(ArrayList<ArrayList<Integer>> groups, String[][] minimize) {
        for (int i = 0, j = groups.size() - 1; i < groups.size(); i++, j--)
            minimize[i + 1][0] = (char) ('Z' - j) + "";
        for (int g = 0; g < groups.size(); g++) {
            ArrayList<Integer> group = groups.get(g);
            for (int j = 0; j < inputSymbols.length; j++) {
                int index = indexOfGroup(groups, indexState(successors[group.get(0)][j].charAt(0) - 65));
                minimize[g + 1][j + 1] = (char) ('Z' - (groups.size() - 1) + index) + "";
            }
        }
        for (int i = 0; i < groups.size(); i++) {
            ArrayList<Integer> group = groups.get(i);
            minimize[i + 1][minimize[0].length - 1] = outputSymbols[group.get(0)];
        }
        return minimize;
    }

    /**
     * Name: intermediateFormat <br>
     * <br> Method used to put the Moore table in the intermediate format. <br>
     * <b>pre: </b> The partitioning algorithm is already done, the input symbols list is already initialized and filled, also the output symbols list and the successors matrix. <br>
	 * <b>post: </b> The Moore table is put in the intermediate format. <br>
     * @param groups - final groups obtained after doing the partitioning algorithm - groups = ArrayList of ArrayList of Integers, groups != null
     * @param minimize - Java String matrix representing a Moore machine after partitioning - matrix = String[][], matrix != null
     * @return A String matrix representing the Moore machine in the intermediate format.
    */
    private String[][] intermediateFormat(ArrayList<ArrayList<Integer>> groups, String[][] minimize) {
        for (int i = 0; i < groups.size(); i++) {
            ArrayList<Integer> group = groups.get(i);
            for (int j = 0; j < group.size(); j++) {
                char s = states[group.get(j)].charAt(0);
                if (minimize[i + 1][0] == null)
                    minimize[i + 1][0] = j == group.size() - 1 ? "{" + s + "}" : "{" + s + ", ";
                else
                    minimize[i + 1][0] += j == group.size() - 1 ? s + "}" : s + ", ";
            }
        }
        for (int g = 0; g < groups.size(); g++) {
            ArrayList<Integer> group = groups.get(g);
            for (int s = 0; s < group.size(); s++) {
                for (int j = 0; j < inputSymbols.length; j++) {
                    String sOld = minimize[g + 1][j + 1];
                    String sNew = successors[group.get(s)][j];
                    if (sOld == null)
                        minimize[g + 1][j + 1] = sNew;
                    else
                        minimize[g + 1][j + 1] = sOld.contains(sNew) ? sOld : sOld + "," + sNew;
                }
            }
        }
        for (int i = 0; i < groups.size(); i++) {
            ArrayList<Integer> group = groups.get(i);
            minimize[i + 1][minimize[0].length - 1] = outputSymbols[group.get(0)];
        }
        return minimize;
    }

    /**
     * Name: step2A <br>
     * <br> Method used to get the groups of the first partition (step 2a of the partitioning algorithm). <br>
     * <b>pre: </b> The output symbols list is already initialized and filled. <br>
	 * <b>post: </b> The groups of the first partition are obtained. <br>
     * @return An ArrayList of ArrayList of Integers representing the groups of the first partition of a Moore machine.
    */
    private ArrayList<ArrayList<Integer>> step2A() {
        ArrayList<ArrayList<Integer>> groups = new ArrayList<>();
        for (int i = 0; i < outputSymbols.length; i++) {
            for (int j = i + 1; j < outputSymbols.length; j++) {
                boolean stop = outputSymbols[i].equals(outputSymbols[j]) ? false : true;
                if (!stop) {
                    int iGroup = indexOfGroup(groups, i), jGroup = indexOfGroup(groups, j);
                    if (groups.size() == 0 || (iGroup == -1 && jGroup == -1)) {
                        ArrayList<Integer> group = new ArrayList<Integer>();
                        group.add(i);
                        group.add(j);
                        groups.add(group);
                    } else {
                        if (iGroup != -1 && jGroup == -1)
                            groups.get(iGroup).add(j);
                        if (iGroup == -1 && jGroup != -1)
                            groups.get(jGroup).add(i);
                    }
                }
            }
            if (indexOfGroup(groups, i) == -1) {
                ArrayList<Integer> group = new ArrayList<>();
                group.add(i);
                groups.add(group);
            }
        }
        return groups;
    }

    /**
     * Name: step2B <br>
     * <br> Method used to get the groups of the following partitions (step 2b of the partitioning algorithm). <br>
     * <b>pre: </b> The successors matrix is already initialized and filled. <br>
	 * <b>post: </b> The groups of the following partitions are obtained. <br>
     * @param groups - groups of the first partition - groups = ArrayList of ArrayList of Integers, groups != null
     * @return An ArrayList of ArrayList of Integers representing the groups of the following partitions of a Moore machine.
    */
    private ArrayList<ArrayList<Integer>> step2B(ArrayList<ArrayList<Integer>> groups) {
        ArrayList<ArrayList<Integer>> subGroups = new ArrayList<>();
        for (int g = 0; g < groups.size(); g++) {
            ArrayList<Integer> group = groups.get(g);
            for (int i = 0; i < group.size(); i++) {
                for (int j = i + 1; j < group.size(); j++) {
                    boolean stop = false;
                    for (int k = 0; k < successors[0].length && !stop; k++) {
                        int i1 = indexState(successors[group.get(i)][k].charAt(0) - 65);
                        int i2 = indexState(successors[group.get(j)][k].charAt(0) - 65);
                        if (!(indexOfGroup(groups, i1) == indexOfGroup(groups, i2)))
                            stop = true;
                    }
                    int iGroup = indexOfGroup(subGroups, group.get(i)), jGroup = indexOfGroup(subGroups, group.get(j));
                    if (!stop) {
                        if (iGroup == -1 && jGroup == -1) {
                            ArrayList<Integer> subGroup = new ArrayList<>();
                            subGroup.add(group.get(i));
                            subGroup.add(group.get(j));
                            subGroups.add(subGroup);
                        } else {
                            if (iGroup == -1 && jGroup != -1)
                                subGroups.get(jGroup).add(group.get(i));
                            if (iGroup != -1 && jGroup == -1)
                                subGroups.get(iGroup).add(group.get(j));
                        }
                    }
                }
                if (indexOfGroup(subGroups, group.get(i)) == -1) {
                    ArrayList<Integer> subGroup = new ArrayList<>();
                    subGroup.add(group.get(i));
                    subGroups.add(subGroup);
                }
            }
        }
        return subGroups;
    }

    /**
     * Name: isTheSame <br>
     * <br> Method used to determine if two matrices are equal. <br>
     * <b>pre: </b> The matrices of integers used for the comparison are already initialized and filled. <br>
	 * <b>post: </b> The true value of verifying if two matrices are equal is determined. <br>
     * @param subGroups1 - first matrix of groups to be compared - subGroups1 = ArrayList of ArrayList of Integers, subGroups1 != null
     * @param subGroups2 - second matrix of groups to be compared - subGroups2 = ArrayList of ArrayList of Integers, subGroups2 != null
     * @return An boolean with true if the two matrices are equal, or with false if not.
    */
    private boolean isTheSame(ArrayList<ArrayList<Integer>> subGroups1, ArrayList<ArrayList<Integer>> subGroups2) {
        boolean stop = subGroups1.size() == subGroups2.size() ? false : true;
        for (int i = 0; i < subGroups1.size() && !stop; i++) {
            ArrayList<Integer> subGroup1 = subGroups1.get(i), subGroup2 = subGroups2.get(i);
            stop = subGroup1.size() == subGroup2.size() ? false : true;
            for (int j = 0; j < subGroup1.size() && !stop; j++)
                stop = subGroup1.get(j) == subGroup2.get(j) ? false : true;
        }
        return !stop;
    }

    /**
     * Name: indexOfGroup <br>
     * <br> Method used to get the index of a group in a matrix of groups given. <br>
     * <b>pre: </b> The matrix of groups is already initialized and filled. <br>
	 * <b>post: </b> The index of a group in the matrix of groups is obtained. <br>
     * @param groups - matrix group to be compared - groups = ArrayList of ArrayList of Integers, groups != null
     * @param num - integer representing a group in the matrix of groups - num = int, num != null
     * @return An integer representing the index of the specified group in the matrix of groups.
    */
    private int indexOfGroup(ArrayList<ArrayList<Integer>> groups, int num) {
        int index = -1;
        boolean exists = false;
        for (int i = 0; i < groups.size() && !exists; i++) {
            ArrayList<Integer> group = groups.get(i);
            for (int j = 0; j < group.size() && !exists; j++) {
                if (group.get(j) == num) {
                    exists = true;
                    index = i;
                }
            }
        }
        return index;
    }

    /**
     * Name: fillMoore <br>
     * <br> Method used to fill the input and output symbols list, and the successors matrix of a Moore machine. <br>
     * <b>pre: </b> The Java String matrix representing the Moore machine is already initialized and filled. <br>
	 * <b>post: </b> The input and output symbols list, and the successors matrix of the Mealy machine are filled. <br>
     * @param machine - Java String matrix representing a Moore machine - matrix = String[][], matrix != null
    */
    private void fillMoore(String[][] machine) {
        inputSymbols(machine);
        states(machine);
        int rows = machine.length;
        int columns = machine[0].length - 1;
        String[][] successors = new String[rows - 1][columns - 1];
        String[] outputSymbols = new String[rows - 1];
        for (int i = 1; i < rows; i++) {
            outputSymbols[i - 1] = machine[i][columns];
            for (int j = 1; j < columns; j++)
                successors[i - 1][j - 1] = machine[i][j];
        }
        this.successors = successors;
        this.outputSymbols = outputSymbols;
    }

    /**
     * Name: inputSymbols <br>
     * <br> Method used to fill the input list of a Moore machine. <br>
     * <b>pre: </b> The Java String matrix representing the Moore machine is already initialized and filled. <br>
	 * <b>post: </b> The input symbols list of the Moore machine is filled. <br>
     * @param machine - Java String matrix representing a Moore machine - matrix = String[][], matrix != null
    */
    private void inputSymbols(String[][] machine) {
        int length = machine[0].length - 2;
        String[] inputSymbols = new String[length];
        for (int i = 0; i < length; i++) {
            inputSymbols[i] = machine[0][i + 1];
        }
        this.inputSymbols = inputSymbols;
    }

    /**
     * Name: states <br>
     * <br> Method used to fill the states list of a Moore machine. <br>
     * <b>pre: </b> The Java String matrix representing the Moore machine is already initialized and filled. <br>
	 * <b>post: </b> The states list of the Moore machine is filled. <br>
     * @param machine - Java String matrix representing a Moore machine - matrix = String[][], matrix != null
    */
    private void states(String[][] machine) {
        int length = machine.length;
        String[] states = new String[length - 1];
        for (int i = 1; i < length; i++) {
            states[i - 1] = machine[i][0];
        }
        this.states = states;
    }
}