package model;

import java.util.ArrayList;

public class Machine {

    public String[][] minimizeMealy(String[][] tableMealy, int statesNumber) {
        String[][] minimized = null;
        ArrayList<ArrayList<Integer>> groups = new ArrayList<>();
        for (int i = 1; i <= statesNumber; i++) {
            for (int j = i + 1; j <= statesNumber; j++) {
                boolean aprb = true;
                for (int k = 1; k < tableMealy[k].length; k++) {
                    if (!tableMealy[i][k].split(",")[1].equals(tableMealy[j][k].split(",")[1])) {
                        aprb = false;
                    }
                }
                if (aprb) {
                    if (groups.size() == 0) {
                        groups.add(new ArrayList<Integer>());
                        groups.get(0).add(i);
                        groups.get(0).add(j);
                    } else {
                        boolean iExists = false;
                        boolean jExists = false;
                        int index = -1;
                        for (int l = 0; l < groups.size() && (!iExists || !jExists); l++) {
                            for (int s = 0; s < groups.get(l).size() && (!iExists || !jExists); s++) {
                                if (groups.get(l).get(s) == i) {
                                    iExists = true;
                                    index = l;
                                }
                                if (groups.get(l).get(s) == j) {
                                    jExists = true;
                                    index = l;
                                }
                            }
                        }
                        if (!iExists && !jExists) {
                            groups.add(new ArrayList<Integer>());
                            groups.get(groups.size() - 1).add(i);
                            groups.get(groups.size() - 1).add(j);
                        } else {
                            if (iExists && !jExists)
                                groups.get(index).add(j);
                            if (!iExists && jExists)
                                groups.get(index).add(i);
                        }
                    }
                }
            }
        }
        boolean stop = false;
        while (!stop) {
            ArrayList<ArrayList<Integer>> aux = groupingMealy(groups, tableMealy, statesNumber);
            ArrayList<ArrayList<Integer>> temp = groupingMealy(aux, tableMealy, statesNumber);
            if (printArrayList(aux).equals(printArrayList(temp))) {
                stop = true;
            }
			groups = temp;
        }
		System.out.println("Final Matrix");
        System.out.println(printArrayList(groups));
        return minimized;
    }

    public ArrayList<ArrayList<Integer>> groupingMealy(ArrayList<ArrayList<Integer>> groups, String[][] tableMealy,
            int statesNumber) {
        ArrayList<ArrayList<Integer>> groupitos = new ArrayList<>();
        for (int l = 0; l < groups.size(); l++) {
            for (int i = 0; i < groups.get(l).size(); i++) {
                for (int j = i + 1; j < groups.get(l).size(); j++) {
                    boolean aprb = true;
                    for (int k = 1; k < tableMealy[k].length; k++) {
                        int a = tableMealy[groups.get(l).get(i)][k].charAt(0) - 64;
                        int b = tableMealy[groups.get(l).get(j)][k].charAt(0) - 64;
                        if (!(searchIndexInGroups(groups, a) == searchIndexInGroups(groups, b))) {
                            aprb = false;
                        }
                    }
                    if (aprb) {
                        System.out.println("Apruebaaa");
                        System.out.println(
                                tableMealy[groups.get(l).get(i)][0] + " " + tableMealy[groups.get(l).get(j)][0]);
                        System.out.println("");
                        if (groupitos.size() == 0) {
                            groupitos.add(new ArrayList<Integer>());
                            groupitos.get(0).add(groups.get(l).get(i));
                            groupitos.get(0).add(groups.get(l).get(j));
                        } else {
                            if (searchIndexInGroups(groupitos, groups.get(l).get(i)) == -1
                                    && searchIndexInGroups(groupitos, groups.get(l).get(j)) == -1) {
                                groupitos.add(new ArrayList<Integer>());
                                groupitos.get(groupitos.size() - 1).add(groups.get(l).get(i));
                                groupitos.get(groupitos.size() - 1).add(groups.get(l).get(j));
                            }
                        }
                    }
                }
            }
        }
        for (int i = 1; i <= statesNumber; i++) {
            if (searchIndexInGroups(groupitos, i) == -1) {
                groupitos.add(new ArrayList<Integer>());
                groupitos.get(groupitos.size() - 1).add(i);
            }
        }
        return groupitos;
    }

    public int searchIndexInGroups(ArrayList<ArrayList<Integer>> groups, int n) {
        boolean exists = false;
        int index = -1;
        for (int l = 0; l < groups.size() && !exists; l++) {
            for (int s = 0; s < groups.get(l).size() && !exists; s++) {
                if (groups.get(l).get(s) == n) {
                    exists = true;
                    index = l;
                }
            }
        }
        return index;
    }

    public String[][] minimizeMoore(String[][] tableMoore, int statesNumber) {
        String[][] minimized;
        for (int i = 1; i <= statesNumber; i++) {
            for (int j = 1; j <= tableMoore[i].length; j++) {

            }
        }
        return null;
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