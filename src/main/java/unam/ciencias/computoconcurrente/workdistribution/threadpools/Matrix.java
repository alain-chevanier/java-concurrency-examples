package unam.ciencias.computoconcurrente.workdistribution.threadpools;

public class Matrix {
  int dim;
  double[][] data;
  int rowDisplace;
  int colDisplace;
  /**
   * constructor
   *
   * @param d dimension of zero-filled matrix
   */
  public Matrix(int d) {
    dim = d;
    rowDisplace = colDisplace = 0;
    data = new double[d][d];
  }
  /**
   * constructor
   *
   * @param matrix backing array for matrix
   * @param x offset of x origin
   * @param y offset of y origin
   * @param d dimension
   */
  public Matrix(double[][] matrix, int x, int y, int d) {
    data = matrix;
    rowDisplace = x;
    colDisplace = y;
    dim = d;
  }
  /**
   * return value
   *
   * @param row coordinate
   * @param col coordinate
   * @return value at coordinate
   */
  public double get(int row, int col) {
    return data[row + rowDisplace][col + colDisplace];
  }
  /**
   * set value at coordinate
   *
   * @param row coordinate
   * @param col coordinate
   * @param value new value for position
   */
  public void set(int row, int col, double value) {
    data[row + rowDisplace][col + colDisplace] = value;
  }
  /** @return matrix dimension */
  public int getDim() {
    return dim;
  }
  /** @return array of half-size matrices, backed by original. */
  public Matrix[][] split() {
    Matrix[][] result = new Matrix[2][2];
    int newDim = dim / 2;
    result[0][0] = new Matrix(data, rowDisplace, colDisplace, newDim); // A_00
    result[0][1] = new Matrix(data, rowDisplace, colDisplace + newDim, newDim); // A_01
    result[1][0] = new Matrix(data, rowDisplace + newDim, colDisplace, newDim);
    result[1][1] = new Matrix(data, rowDisplace + newDim, colDisplace + newDim, newDim);
    return result;
  }
}
