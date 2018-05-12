package model.windowcard;

import exception.PositionException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class MatrixCell {

    private Cell[][] matrix;
    private int rows;
    private int cols;
    private static String illegalMsg = "Illegal Position";

    private static final Logger logger = Logger.getLogger(MatrixCell.class.getName());

    public MatrixCell(int rows, int cols) {
        matrix = new Cell[rows][cols];
        this.rows = rows;
        this.cols = cols;
    }

    public MatrixCell(MatrixCell matrixCell) {
        this.matrix = matrixCell.getMatrix();
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

    public Cell[][] getMatrix() {
        return matrix;
    }

    public Cell getCell(int row, int col) {
        if (matrix[row][col]!=null)
            return matrix[row][col];
        throw new ArrayIndexOutOfBoundsException("Wrong Coordinates");
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        logger.info("Rows: " + getRows() + " Cols: " + getCols());
    }

    public boolean isBorder(Cell c){
        return c.getCol()==0 || c.getCol()==4 || c.getRow()==0 || c.getRow()==3;
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
            throw new PositionException(illegalMsg);

        List<Cell> ort = new ArrayList<>();
        try {
            if (matrix[row - 1][col] != null)
                ort.add(matrix[row - 1][col]);
        }catch (ArrayIndexOutOfBoundsException e){}

        try {
            if (matrix[row][col-1] != null)
                ort.add(matrix[row][col-1]);
        }catch (ArrayIndexOutOfBoundsException e){}

        try{
            if (matrix[row][col+1] != null)
                ort.add(matrix[row][col+1]);
        }catch (ArrayIndexOutOfBoundsException e){}

        try{
            if (matrix[row + 1][col] != null)
                ort.add(matrix[row + 1][col]);
        }catch (ArrayIndexOutOfBoundsException e){}

        return ort;

        }

    public List<Cell> retDiagonal (int row, int col) throws PositionException {

        if (row < 0 || row > 3 || col < 0 || col > 4)
            throw new PositionException(illegalMsg);

        List<Cell> diag = new ArrayList<>();

        try {
            if (matrix[row - 1][col - 1] != null)
                diag.add(matrix[row - 1][col - 1]);
        }catch (ArrayIndexOutOfBoundsException e){}

        try {
            if (matrix[row - 1][col + 1] != null)
                diag.add(matrix[row - 1][col + 1]);
        }catch (ArrayIndexOutOfBoundsException e){}

        try {
            if (matrix[row + 1][col - 1] != null)
                diag.add(matrix[row + 1][col - 1]);
        }catch (ArrayIndexOutOfBoundsException e){}

        try {
            if (matrix[row + 1][col + 1] != null)
                diag.add(matrix[row + 1][col + 1]);
        }catch (ArrayIndexOutOfBoundsException e){}

        return diag;

    }

    public List<Cell> retNeighbors (int row, int col) throws PositionException {

        if (row < 0 || row > 3 || col < 0 || col > 4)
            throw new PositionException(illegalMsg);

        List<Cell> neig = new ArrayList<>();

        neig.addAll(retOrtogonal(row,col));
        neig.addAll(retDiagonal(row,col));

        return neig;

    }

}
