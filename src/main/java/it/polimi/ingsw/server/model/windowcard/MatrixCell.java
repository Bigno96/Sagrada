package it.polimi.ingsw.server.model.windowcard;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.server.model.dicebag.Dice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.*;

public class MatrixCell implements Serializable {

    private static final String ILLEGAL_MSG = "Illegal Position";
    private static final String DUMP_ROW_MSG = "Rows: ";
    private static final String DUMP_COL_MSG = " Cols: ";

    private Cell[][] matrix;
    private int rows;
    private int cols;

    /**
     * Constructor
     * @param rows != null
     * @param cols != null
     */
    public MatrixCell(int rows, int cols) {
        matrix = new Cell[rows][cols];
        this.rows = rows;
        this.cols = cols;
    }

    /**
     * Set pos of Cells
     * @param cellList != null
     */
    public void loadMatrixCell(List<Cell> cellList){
        int pos = 0;
        for (int i=0; i<rows; i++){
            for (int j=0; j<cols; j++){
                matrix[i][j] = cellList.get(pos);
                pos++;
            }
        }
    }

    /**
     * @return number of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * @return number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * @return matrix of cells
     */
    public Cell[][] getMatrix() {
        return matrix;
    }

    /**
     * @param row of cell to find
     * @param col of cell to find
     * @return cell searched with passed coordinates
     */
    public Cell getCell(int row, int col) {
        if (matrix[row][col] != null)
            return matrix[row][col];

        return null;
    }

    /**
     * @param d Dice of the cell needed
     * @return Cell that contains Dice d
     * @throws IDNotFoundException when trouble finding dice's id
     */
    public Cell getCell(Dice d) throws IDNotFoundException {
        for (int i=0; i<rows; i++){
            for (int j=0; j<cols; j++){
                if (matrix[i][j].isOccupied())
                    if (matrix[i][j].getDice().getID() == d.getID())
                        return matrix[i][j];
            }
        }

        return null;
    }

    /**
     * Check if the Dice is in the windowCard
     * @param d != null
     * @throws IDNotFoundException when Dice has an illegal id
     */

    public boolean containsDice(Dice d) throws IDNotFoundException {
        for (Cell[] c1 : matrix) {
            for (Cell c2 : c1) {
                if (c2.isOccupied()) {
                    if (c2.getDice().isEqual(d)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        out.println(DUMP_ROW_MSG + getRows() + DUMP_COL_MSG + getCols());
    }

    /**
     * Used to verify if cell is on border
     * @param c Cell to be verified
     * @return true if the cell is on the border, false else
     */
    public boolean isBorder(Cell c){
        if (c == null)
            return false;
        return c.getCol()==0 || c.getCol()==4 || c.getRow()==0 || c.getRow()==3;
    }

    public Iterator<Cell> itrOrizz(){
        List<Cell> cellList = new ArrayList<>();
        for (int i=0; i<rows; i++)
            for (int j = 0; j < cols; j++)
                cellList.add(matrix[i][j]);

        return cellList.iterator();
    }

    public List<Cell> retOrtogonal (int row, int col) throws PositionException {
        if (row < 0 || row > 3 || col < 0 || col > 4)
            throw new PositionException(ILLEGAL_MSG);

        List<Cell> ort = new ArrayList<>();

        if (row-1 >= 0)
            ort.add(matrix[row-1][col]);

        if (col-1 >= 0)
            ort.add(matrix[row][col-1]);

        if (col+1 <= 4)
            ort.add(matrix[row][col+1]);

        if (row+1 <= 3)
            ort.add(matrix[row + 1][col]);

        return ort;

        }

    public List<Cell> retDiagonal (int row, int col) throws PositionException {
        if (row < 0 || row > 3 || col < 0 || col > 4)
            throw new PositionException(ILLEGAL_MSG);

        List<Cell> diag = new ArrayList<>();

        if (row-1 >= 0 && col-1 >= 0)
            diag.add(matrix[row - 1][col - 1]);

        if (row-1 >= 0 && col+1 <= 4)
            diag.add(matrix[row - 1][col + 1]);

        if (row+1 <= 3 && col-1 >= 0)
            diag.add(matrix[row + 1][col - 1]);

        if (row+1 <= 3 && col+1 <= 4)
            diag.add(matrix[row + 1][col + 1]);


        return diag;

    }

    public List<Cell> retNeighbors (int row, int col) throws PositionException {
        if (row < 0 || row > 3 || col < 0 || col > 4)
            throw new PositionException(ILLEGAL_MSG);

        List<Cell> neig = new ArrayList<>();

        neig.addAll(retOrtogonal(row,col));
        neig.addAll(retDiagonal(row,col));

        return neig;

    }

}
