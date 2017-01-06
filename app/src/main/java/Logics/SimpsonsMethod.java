package Logics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import Models.Expert;
import Models.ResultInfo;

/**
 * Created by sails on 14.12.2016.
 */

class Pair {
    private int memberA;
    private int memberB;
    private int mark;

    public Pair() {
    }

    public Pair(int memberA, int memberB, int mark) {
        this.memberA = memberA;
        this.memberB = memberB;
        this.mark = mark;
    }


    public int getMemberA() {
        return memberA;
    }

    public int getMemberB() {
        return memberB;
    }

    public int getMark() {
        return mark;
    }

    @Override
    public String toString() {
        return memberA + " - " + memberB + " = " + mark;
    }

}


public class SimpsonsMethod {

    private int countAlt;
    private int countExp;

    private Integer[][] initMatrix;
    private Integer[] amountOfExp;
    private ResultInfo resultInfo;
    private Map<String, Integer> relatStringInt;
    private Map<Integer, String> relatIntString;


    private void readMatrix(Integer[][] experts) {

        Integer[][] values = new Integer[experts[0].length][experts.length];
        for (int i = 0; i < experts.length; i++) {
            for (int j = 0; j < experts[0].length; j++) {
                values[j][i] = experts[i][j];
            }
        }

        System.out.println("VALUES");
        for (Integer[] mas : values) {
            System.out.println(Arrays.toString(mas));
        }

        countAlt = values.length - 1;
        countExp = values[0].length - 1;
        System.out.println("countexp:: " + countExp);
        initMatrix = new Integer[countAlt][countExp];
        amountOfExp = new Integer[countExp];

        System.out.println(countAlt);
        System.out.println(countExp);

        for (int i = 0; i < countAlt; i++) {
            for (int j = 0; j < countExp; j++) {
                initMatrix[i][j] = experts[i][j];
            }
        }

        System.out.println("initMatrix");
        for (Integer[] mas : initMatrix) {
            System.out.println(Arrays.toString(mas));
        }

        for (int i = 0; i < countExp; i++) {
            Integer value = values[values.length - 1][i];
            amountOfExp[i] = value;
        }

        System.out.println("amount of exp");
        System.out.println(Arrays.toString(amountOfExp));


    }

    private int findMarkOfPair(int a, int b) {
        int result = 0;
        int indexA = 0;
        int indexB = 0;
        //a--; b--;
        for (int i = 0; i < countExp; i++) {
            for (int j = 0; j < countAlt; j++)
                if (initMatrix[i][j] == a) indexA = j;
                else if (initMatrix[i][j] == b) indexB = j;

            if (indexA < indexB) result += amountOfExp[i];
        }

        System.out.println("pair " + a + " " + b + " = " + result);
        return result;
    }

    private int coplandMethod(ArrayList<Pair> pairs) {
        int mark;
        int[] marks = new int[countAlt];

        resultInfo.appendCountingMessage("\n\nCopland method:");
        for (int i = 0; i < countAlt; i++) {
            resultInfo.appendCountingMessage("\nRating for " + relatIntString.get(i + 1) + " = ");
            mark = 0;
            for (int j = 0; j < countExp; j++) {
                if (i != j) {
                    if (getMarkByPair(i + 1, j + 1, pairs) > getMarkByPair(j + 1, i + 1, pairs)) {
                        mark++;
                        resultInfo.appendCountingMessage(" + ");
                    } else {
                        mark--;
                        resultInfo.appendCountingMessage(" - ");
                    }
                    resultInfo.appendCountingMessage(" 1 ");
                }
            }
            marks[i] = mark;
            resultInfo.appendCountingMessage(" = " + mark);
        }

        int maxIndex = 0;
        for (int i = 1; i < marks.length; i++) {
            int newnumber = marks[i];
            if ((newnumber > marks[maxIndex])) {
                maxIndex = i;
            }
        }
        return (maxIndex + 1);
    }

    private int simpsonMethod(ArrayList<Pair> pairs) {
        Set<Pair> marks = new HashSet<>();
        ArrayList<String> printed = new ArrayList<>();
        resultInfo.appendCountingMessage("\n\nSimpson`s Method:");
        for (int i = 0; i < countAlt; i++) {
            for (int j = 0; j < countExp; j++) {
                if (i != j) {
                    int pairAB = getMarkByPair(i + 1, j + 1, pairs);
                    int pairBA = getMarkByPair(j + 1, i + 1, pairs);
                    Pair p;
                    if (pairAB < pairBA) {
                        p = new Pair(i + 1, j + 1, pairAB);
                        marks.add(p);
                        resultInfo.appendCountingMessage("\n" + relatIntString.get(p.getMemberA()) + "" +
                                relatIntString.get(p.getMemberB()) + " = " + p.getMark());
                    } else {
                        p = new Pair(j + 1, i + 1, pairBA);
                        marks.add(p);

                        String strPair = relatIntString.get(p.getMemberA()) + "" + relatIntString.get(p.getMemberB());

                        System.out.println(strPair);
                        if(!printed.contains(strPair))
                            continue;
                        printed.add(strPair);
                        resultInfo.appendCountingMessage("\n" + strPair + " = " + p.getMark());
                    }

                }
            }
        }

        Pair maxMark = new Pair();
        int max = 0;
        for (Pair item : marks) {
            if (item.getMark() > max) {
                max = item.getMark();
                maxMark = new Pair(item.getMemberA(), item.getMemberB(), item.getMark());
            }
        }
        return maxMark.getMemberA();
    }

    private int getMarkByPair(int a, int b, ArrayList<Pair> pairs) {
        for (Pair item : pairs) {
            if (item.getMemberA() == a && item.getMemberB() == b) {
                return item.getMark();
            }
        }
        return 0;
    }

    public ResultInfo calculate(Integer[][] experts, Map<String, Integer> relatStringInt, Map<Integer, String> relatIntString) {

        this.relatStringInt = relatStringInt;
        this.relatIntString = relatIntString;

        readMatrix(experts);

        resultInfo = new ResultInfo();
        resultInfo.clearInfo();

        ArrayList<Pair> pairs = new ArrayList<>();
        for (int i = 0; i < countAlt; i++) {
            for (int j = 0; j < countExp; j++) {
                if (i != j)
                    pairs.add(new Pair(i + 1, j + 1, findMarkOfPair(i + 1, j + 1)));
            }
        }
        resultInfo.appendCountingMessage("Pairs:");
        for (Pair pair : pairs) {
            String strPair = relatIntString.get(pair.getMemberA()) + "" + relatIntString.get(pair.getMemberB());

            resultInfo.appendCountingMessage("\n" + strPair + " = " + pair.getMark());
        }

        resultInfo.setWinner("Complend`s method winner: " + relatIntString.get(coplandMethod(pairs)) + "\n" +
                "Simpson`s method winner: " + relatIntString.get(simpsonMethod(pairs)));



        return resultInfo;
    }


}
