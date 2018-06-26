package it.polimi.ingsw.server.model.windowcard;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.server.model.dicebag.Dice;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.*;

public class MatrixCell implements Serializable {

    private static final int MIN = 0;

    private static final String ILLEGAL_MSG = "Illegal Position";
    private static final String DUMP_ROW_MSG = "Rows: ";
    private static final String DUMP_COL_MSG = " Cols: ";

    private Cell[][] matrix;
    private final int maxRow;
    private final int maxCol;

    /**
     * Constructor
     * @param maxRow != null
     * @param maxCol != null
     */
    public MatrixCell(int maxRow, int maxCol) {
        matrix = new Cell[maxRow][maxCol];
        this.maxRow = maxRow;
        this.maxCol = maxCol;
    }

    /**
     * Set pos of Cells
     * @param cellList != null
     */
    public void loadMatrixCell(List<Cell> cellList){
        int pos = MIN;
        for (int i = MIN; i < maxRow; i++)
            for (int j = MIN; j < maxCol; j++) {
                matrix[i][j] = cellList.get(pos);
                pos++;
            }
    }

    /**
     * @return number of columns
     */
    public int getMaxCol() {
        return maxCol;
    }

    /**
     * @return number of rows
     */
    public int getMaxRow() {
        return maxRow;
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
     * @return cell searched with passed coordinates, null else
     */
    public Cell getCell(int row, int col) {
        if (row < MIN || row >= maxRow || col < MIN || col >= maxCol)
            return null;

        return matrix[row][col];
    }

    /**
     * @param d Dice of the cell needed
     * @return Cell that contains Dice d, null else
     */
    public Cell getCell(Dice d) {
        for (int i = MIN; i < maxRow; i++)
            for (int j = MIN; j < maxCol; j++)
                if (matrix[i][j].isOccupied() && matrix[i][j].getDice().getID() == d.getID())
                    return matrix[i][j];

        return null;
    }

    /**
     * Check if the Dice is in the windowCard
     * @param d != null
     * @return true if window card contains d, false else
     */
    public boolean containsDice(Dice d) {
        for (Cell[] c1 : matrix)
            for (Cell c2 : c1)
                if (c2.isOccupied() && c2.getDice().isEqual(d))
                    return true;

        return false;
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        out.println(DUMP_ROW_MSG + getMaxRow() + DUMP_COL_MSG + getMaxCol());
    }

    /**
     * Used to verify if cell is on border
     * @param c Cell to be verified
     * @return true if the cell is on the border, false else
     */
    public boolean isBorder(Cell c) {
        if (c == null)
            return false;

        return c.getCol()== MIN || c.getCol()== maxCol-1  || c.getRow()== MIN || c.getRow()== maxRow-1;
    }

    /**
     * Used to get an iterator that moves horizontally through the cells' matrix
     * @return an iterator over the matrix of cells
     */
    Iterator<Cell> itrHorizontal() {
        List<Cell> cellList = new ArrayList<>();

        for (int i = MIN; i < maxRow; i++)
            cellList.addAll(Arrays.asList(matrix[i]).subList(MIN, maxCol));

        return cellList.iterator();
    }

    /**
     * Used to get a cell's orthogonal cells
     * @param row of the cell
     * @param col of the cell
     * @return list of cells that have 1 side adjacent to the corresponding cell identified by passed parameters
     * @throws PositionException when wrong cell coordinates are passed
     */
    public List<Cell> retOrthogonal(int row, int col) throws PositionException {
        if (row < MIN || row >= maxRow || col < MIN || col >= maxCol)
            throw new PositionException(ILLEGAL_MSG);

        List<Cell> ort = new ArrayList<>();

        if (row != MIN)
            ort.add(matrix[row-1][col]);

        if (col != MIN)
            ort.add(matrix[row][col-1]);

        if (col != maxCol-1)
            ort.add(matrix[row][col+1]);

        if (row != maxRow-1)
            ort.add(matrix[row+1][col]);

        return ort;
    }

    /**
     * Used to get a cell's diagonally adjacent cells
     * @param row of the cell
     * @param col of the cell
     * @return list of cells that are diagonally adjacent to the corresponding cell identified by passed parameters
     * @throws PositionException when wrong cell coordinates are passed
     */
    public List<Cell> retDiagonal (int row, int col) throws PositionException {
        if (row < MIN || row >= maxRow || col < MIN || col >= maxCol)
            throw new PositionException(ILLEGAL_MSG);

        List<Cell> diagonal = new ArrayList<>();

        if (row != MIN && col != MIN)
            diagonal.add(matrix[row-1][col-1]);

        if (row != MIN && col != maxCol-1)
            diagonal.add(matrix[row-1][col+1]);

        if (row != maxRow-1 && col != MIN)
            diagonal.add(matrix[row+1][col-1]);

        if (row != maxRow-1 && col != maxCol-1)
            diagonal.add(matrix[row+1][col+1]);

        return diagonal;
    }

    /**
     * Used to get all neighbors of a cell, both orthogonal and diagonal
     * @param row of the cell
     * @param col of the cell
     * @return list of cells that are diagonally adjacent or have 1 side adjacent to the corresponding cell identified by passed parameters
     * @throws PositionException when wrong cell coordinates are passed
     */
    public List<Cell> retNeighbors (int row, int col) throws PositionException {
        if (row < MIN || row >= maxRow || col < MIN || col >= maxCol)
            throw new PositionException(ILLEGAL_MSG);

        List<Cell> neighbors = new ArrayList<>();

        neighbors.addAll(retOrthogonal(row,col));
        neighbors.addAll(retDiagonal(row,col));

        return neighbors;
    }

}
