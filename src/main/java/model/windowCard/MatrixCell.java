package model.windowCard;

import exception.IDNotFoundException;
import exception.PositionException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class MatrixCell {

    private Cell[][] matrix = null;
    private int rows;
    private int cols;

    private static final Logger logger = Logger.getLogger(MatrixCell.class.getName());

    public MatrixCell(int rows, int cols) {
        matrix = new Cell[rows][cols];
        this.rows = rows;
        this.cols = cols;
    }

    public MatrixCell(Cell[][] matrix) {
        this.matrix = matrix.clone();
        rows = this.matrix.length;
        cols = this.matrix[0].length;
    }

    public void loadMatrixCell(List<Cell> cellList){
        int pos = 0;
        for (int i=0; i<rows; i++){
            for (int j=0; j<cols; j++){
                matrix[i][j] = cellList.get(pos);
                pos++;
            }
        }
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public Cell getCell(int row, int col) throws IDNotFoundException {
        if (matrix[row][col]!=null)
            return matrix[row][col];
        throw new IDNotFoundException("Id was not found");
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        logger.info("Rows: " + getRows() + " Cols: " + getCols());
    }

    public boolean isBorder(Cell c){
        if(c.getCol()==0 || c.getCol()==4 || c.getRow()==0 || c.getRow()==3)
            return true;
        return false;
    }

    public Iterator<Cell> itrOrizz(){
        List<Cell> cellList = new ArrayList<>();
        for (int i=0; i<rows; i++) {
            for (int j = 0; j < cols; j++) {
                cellList.add(matrix[i][j]);
            }
        }
        return cellList.iterator();
    }

    public Iterator<Cell> itrVert(){
        List<Cell> cellList = new ArrayList<>();
        for (int j=0; j<cols; j++) {
            for (int i = 0; i < rows; i++) {
                cellList.add(matrix[i][j]);
            }
        }
        return cellList.iterator();
    }

    public List<Cell> retOrtogonal (int row, int col) throws PositionException {

        if (row < 0 || row > 3 || col < 0 || col > 4)
            throw new PositionException("Illegal Position");

        List<Cell> ort = new ArrayList<>();

        if(matrix[row-1][col]!=null)
            ort.add(matrix[row-1][col]);

        if(matrix[row][col-1]!=null)
            ort.add(matrix[row][col-1]);

        if(matrix[row][col+1]!=null)
            ort.add(matrix[row][col+1]);

        if(matrix[row+1][col]!=null)
            ort.add(matrix[row+1][col]);

        return ort;

        }

    public List<Cell> retDiagonal (int row, int col) throws PositionException {

        if (row < 0 || row > 3 || col < 0 || col > 4)
            throw new PositionException("Illegal Position");

        List<Cell> diag = new ArrayList<>();


        if(matrix[row-1][col-1]!=null)
            diag.add(matrix[row-1][col-1]);

        if(matrix[row-1][col+1]!=null)
            diag.add(matrix[row-1][col+1]);

        if(matrix[row+1][col-1]!=null)
            diag.add(matrix[row+1][col-1]);

        if(matrix[row+1][col+1]!=null)
            diag.add(matrix[row+1][col+1]);

        return diag;

    }

    public List<Cell> retNeighbors (int row, int col) throws PositionException {

        if (row < 0 || row > 3 || col < 0 || col > 4)
            throw new PositionException("Illegal Position");

        List<Cell> neig = new ArrayList<>();

        neig.addAll(retOrtogonal(row,col));
        neig.addAll(retDiagonal(row,col));

        return neig;

    }

}
