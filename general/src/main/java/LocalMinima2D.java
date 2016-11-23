package main.java;

/**
 * Created by nvijayv on 11/22/16.
 */
public class LocalMinima2D {

    public Position2D getLocalMinima(int[][] numbers) {
        int numRows = numbers.length;
        int numCols = numbers[0].length;

        int rstart = 0, rend = numRows-1, rmid;
        int cstart = 0, cend = numCols-1, cmid;

        while (rstart < rend || cstart < cend) {
            if (rend-rstart >= cend-cstart) {
                rmid = rstart + (rend - rstart)/2;
                int rowMinCol = cstart;
                int rowMin = numbers[rmid][rowMinCol];
                for (int col = cstart+1; col <= cend; col++) {
                    if (numbers[rmid][col] < rowMin) {
                        rowMin = numbers[rmid][col];
                        rowMinCol = col;
                    }
                }
                boolean rowsShrinked = false;
                if (rmid > rstart) {
                    if (numbers[rmid-1][rowMinCol] < rowMin) {
                        rend = rmid-1;
                        rowsShrinked = true;
                    }
                }
                if (!rowsShrinked && rmid < rend) {
                    if (numbers[rmid+1][rowMinCol] < rowMin) {
                        rstart = rmid+1;
                        rowsShrinked = true;
                    }
                }
                if (!rowsShrinked) {
                    return new Position2D(rmid, rowMinCol);
                }
            } else {
                cmid = cstart + (cend - cstart)/2;
                int colMinRow = rstart;
                int colMin = numbers[colMinRow][cmid];
                for (int row = rstart+1; row <= rend; row++) {
                    if (numbers[row][cmid] < colMin) {
                        colMin = numbers[row][cmid];
                        colMinRow = row;
                    }
                }
                boolean colsShrinked = false;
                if (cmid > cstart) {
                    if (numbers[colMinRow][cmid-1] < colMin) {
                        cend = cmid-1;
                        colsShrinked = true;
                    }
                }
                if (!colsShrinked && cmid < cend) {
                    if (numbers[colMinRow][cmid + 1] < colMin) {
                        cstart = cmid+1;
                        colsShrinked = true;
                    }
                }
                if (!colsShrinked) {
                    return new Position2D(colMinRow, cmid);
                }
            }
        }
        return new Position2D(rstart, cstart);
    }

    public static void main(String[] args) {
        LocalMinima2D lm2d = new LocalMinima2D();

        System.out.println(lm2d.getLocalMinima(new int[][]{
                {1}
        })); // 0, 0

        System.out.println(lm2d.getLocalMinima(new int[][]{
                {1, 0, 10},
                {12, 5, 2}
        })); // 0, 1

        System.out.println(lm2d.getLocalMinima(new int[][]{
                {20, 100, 18, 11, 10, 100, 17},
                {19, 100, 13, 100, 9, 100, 15},
                {18, 100, 14, 100, 8, 10, 14},
                {17, 16, 15, 100, 17, 16, 18}
        }));    // 2, 4
    }
}

class Position2D {
    int row;
    int col;

    Position2D (int _row, int _col) {
        row = _row;
        col = _col;
    }

    public String toString() {
        return "row: " + row + ", col: " + col;
    }
}