package com.adventofcode.adventofcode2023;

import java.util.List;

public class MatrixUtils {

    public static char[][] getMatrix(List<String> lines) {
        char[][] matrix = new char[lines.size()][lines.getFirst().length()];
        for (int y = 0; y < lines.size(); y++) {
            matrix[y] = lines.get(y).toCharArray();
        }
        return matrix;
    }

    public static char[][] transposeMatrix(char[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;

        char[][] transposedMatrix = new char[n][m];

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                transposedMatrix[x][y] = matrix[y][x];
            }
        }
        return transposedMatrix;
    }

    public static char[][] mirrorY(char[][] inputArray) {
        int numRows = inputArray.length;
        int numCols = inputArray[0].length;

        char[][] mirrored = new char[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                // invert elements
                mirrored[i][j] = inputArray[i][numCols - 1 - j];
            }
        }
        return mirrored;
    }

    public static char[][] mirrorX(char[][] inputArray) {
        int numRows = inputArray.length;
        int numCols = inputArray[0].length;

        char[][] mirrored = new char[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                // invert elements
                mirrored[i][j] = inputArray[numRows -1 - i][j];
            }
        }
        return mirrored;
    }
}
