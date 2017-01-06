package Logics;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import Models.Expert;
import Models.ResultInfo;

/**
 * Created by sails on 12.12.2016.
 */

public class MethodsCounter {

    String LOG_TAG = "myLogs";

    ResultInfo resultInfo;
    Map<String, Integer> relatStringInt;
    Map<Integer, String> relatIntString;

    public MethodsCounter() {
        this.resultInfo = new ResultInfo();
        relatStringInt = new HashMap<>();
        relatIntString = new HashMap<>();

        Log.d(LOG_TAG, "METhodsCounter Constructor");
//        Integer[][] marks = generateResultsMatrix(experts);
//        for (int i = 0; i < marks.length; i++) {
//            String s = "";
//            for (int j = 0; j < marks[0].length; j++) {
//                s += marks[i][j] + " ";
//            }
//            Log.d(LOG_TAG, s);
//        }
//        System.out.println(relatStringInt.get("A"));
    }

    private boolean areResultsValuesCorrect(ArrayList<Expert> experts) {
        List<String> possibleMarks = experts.get(0).getResultsOrder();
        for (Expert expert : experts) {
            for (String s : expert.getResultsOrder()) {
                if (!possibleMarks.contains(s)) {
                    resultInfo.setErrorMessage("Error in the imput data." +
                            " Expert with id " + expert.getId() + " should not contain mark named '" + s + "'.");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isResultsCountCorrect(ArrayList<Expert> experts) {
        int size = experts.get(0).getResultsOrder().size();
        for (Expert expert : experts) {
            if (expert.getResultsOrder().size() != size) {
                resultInfo.setErrorMessage("Error in the input data. Count of marks of expert " + expert.getId() + " is wrong. Should be " + size + ".");
                return false;
            }
        }
        return true;
    }

    private boolean checkSameRatingsValues(ArrayList<Expert> experts) {

        for (Expert expert : experts) {
            List<String> expertMarks = expert.getResultsOrder();
            for (int i = 0; i < expertMarks.size(); i++) {
                String curMark = expertMarks.get(i);
                for (int j = 0; j < expertMarks.size(); j++) {
                    if (i != j) {
                        String checkingMark = expertMarks.get(j);
                        if (curMark.equals(checkingMark)) {
                            resultInfo.setErrorMessage("Expert with id " + expert.getId() + " has two same marks ('" + curMark + "').");
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    private void fillRelationsArray(ArrayList<Expert> experts) {
        int i = 1;
        for (String s : experts.get(0).getResultsOrder()) {
            System.out.println("put " + s + "  " + i);
            relatStringInt.put(s, i);
            relatIntString.put(i++, s);
        }
    }

    private Integer[][] generateResultsMatrix(ArrayList<Expert> experts) {

        int marksCount = experts.get(0).getResultsOrder().size();

        Integer[][] marks = new Integer[experts.size() + 1][marksCount + 1];

        System.out.println(Arrays.toString(marks[0]));
        int i = 0;
        for (Expert expert : experts) {
            System.out.println(" i  = " + i + " exp count: " + experts.size());
            List<String> results = expert.getResultsOrder();
            int j;
            for (j = 0; j < marksCount; j++) {
                Log.d(LOG_TAG, "i = " + i + " j = " + j + " results.get(j) = " + results.get(j) + " relatStringInt.get(results.get(j)) " + relatStringInt.get(results.get(j)));
                marks[i][j] = Integer.valueOf(relatStringInt.get(results.get(j)));
            }
            marks[i][j] = expert.getImportance();
            i++;
        }

        int m = marksCount - 1;
        for (int k = 0; k < marksCount; k++) {
            marks[marks.length - 1][k] = m--;
        }

        return marks;
    }


    private boolean checkMarksArray(ArrayList<Expert> experts) {
        return areResultsValuesCorrect(experts) && isResultsCountCorrect(experts) && checkSameRatingsValues(experts);
    }

    private void fillExpertsMatrixInroResultInfo(Integer[][] matrix) {
        StringBuilder stringMatrix = new StringBuilder();
        stringMatrix.append("Matrix:\n");
        for (int i = 0; i < matrix.length - 1; i++) {
            int j = 0;
            for (j = 0; j < matrix[0].length - 1; j++) {
                stringMatrix.append(relatIntString.get(matrix[i][j]) + "   ");
            }
            stringMatrix.append(matrix[i][j] + "\n");
        }

        for (int i = 0; i < matrix[0].length - 1; i++) {
            stringMatrix.append(matrix[matrix.length - 1][i] + "   ");
        }
        stringMatrix.append("\n\n");
        resultInfo.appendCountingMessage(stringMatrix.toString());
    }

    public ResultInfo boardMethod(ArrayList<Expert> experts) {
        resultInfo.clearInfo();
        fillRelationsArray(experts);
        if (!checkMarksArray(experts))
            return resultInfo;

        Integer[][] marks = generateResultsMatrix(experts);

        fillExpertsMatrixInroResultInfo(marks);

        int bordCalculations[] = new int[marks[0].length - 1];

        StringBuilder result = new StringBuilder();

        for (int k = 0; k < bordCalculations.length; k++) {
            result.append("\n" + relatIntString.get(k + 1) + ":  ");
            Integer solutionId = k + 1;
            int boardSum = 0;
            for (int i = 0; i < marks.length - 1; i++) {
                for (int j = 0; j < marks[0].length - 1; j++) {
                    if (marks[i][j] == solutionId) {
                        int num1 = marks[marks.length - 1][j];
                        int num2 = marks[i][marks[0].length - 1];
                        boardSum += num1 * num2;
                        result.append(num2 + " * " + num1 + " + ");
                    }
                }
            }
            result.delete(result.lastIndexOf("+ "), result.length());
            result.append(" = " + boardSum);
            bordCalculations[k] = boardSum;
        }

        resultInfo.appendCountingMessage(result.toString());

        int winnerValue = bordCalculations[0];
        int winner = 1;
        for (int i = 0; i < bordCalculations.length; i++) {
            if (winnerValue < bordCalculations[i]) {
                winnerValue = bordCalculations[i];
                winner = i + 1;
            }
        }
        resultInfo.setWinner("Winner: " + relatIntString.get(winner));

        return resultInfo;
    }


    public ResultInfo simpsonsMethod(ArrayList<Expert> experts) {
        resultInfo.clearInfo();
        fillRelationsArray(experts);
        if (!checkMarksArray(experts))
            return resultInfo;

        Integer[][] marks = generateResultsMatrix(experts);

        fillExpertsMatrixInroResultInfo(marks);

        SimpsonsMethod simpsonsMethod = new SimpsonsMethod();
        ResultInfo simpsonResult = simpsonsMethod.calculate(marks, relatStringInt, relatIntString);
        resultInfo.appendCountingMessage(simpsonResult.getCountingMessage());
        resultInfo.setWinner(simpsonResult.getWinner());

        return resultInfo;
    }





}
