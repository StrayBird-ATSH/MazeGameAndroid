package fdu.ss.wangchen.mazeGame;

/**
 * The abstract class for character, monsters and treasure.
 * Created by StrayBird_ATSH on 08-Jan-18.
 */

abstract class Element {
    private int row, column, status;

    int getRow() {
        return row;
    }

    void setRow(int row) {
        this.row = row;
    }

    void setColumn(int column) {
        this.column = column;
    }

    final boolean moveUp(int mapNumber) {
        switch (mapNumber) {
            case 1:
                if (Constants.getMapOne()[this.row - 1][this.column] == 0) {
                    row--;
                    return true;
                }
                break;
            case 2:
                if (Constants.getMapTwo()[this.row - 1][this.column] == 0) {
                    row--;
                    return true;
                }
                break;
            case 3:
                if (Constants.getMapThree()[this.row - 1][this.column] == 0) {
                    row--;
                    return true;
                }
                break;
            case 4:
                if (Constants.getMapFour()[this.row - 1][this.column] == 0) {
                    row--;
                    return true;
                }
                break;
        }
        return false;
    }

    int getColumn() {
        return column;
    }

    final boolean moveDown(int mapNumber) {
        switch (mapNumber) {
            case 1:
                if (Constants.getMapOne()[this.row + 1][this.column] == 0) {
                    row++;
                    return true;
                }
                break;
            case 2:
                if (Constants.getMapTwo()[this.row + 1][this.column] == 0) {
                    row++;
                    return true;
                }
                break;
            case 3:
                if (Constants.getMapThree()[this.row + 1][this.column] == 0) {
                    row++;
                    return true;
                }
                break;
            case 4:
                if (Constants.getMapFour()[this.row + 1][this.column] == 0) {
                    row++;
                    return true;
                }
                break;
        }
        return false;
    }

    final boolean moveLeft(int mapNumber) {
        switch (mapNumber) {
            case 1:
                if (Constants.getMapOne()[this.row][this.column - 1] == 0) {
                    column--;
                    return true;
                }
                break;
            case 2:
                if (Constants.getMapTwo()[this.row][this.column - 1] == 0) {
                    column--;
                    return true;
                }
                break;
            case 3:
                if (Constants.getMapThree()[this.row][this.column - 1] == 0) {
                    column--;
                    return true;
                }
                break;
            case 4:
                if (Constants.getMapFour()[this.row][this.column - 1] == 0) {
                    column--;
                    return true;
                }
                break;
        }
        return false;
    }

    final boolean moveRight(int mapNumber) {
        switch (mapNumber) {
            case 1:
                if (Constants.getMapOne()[this.row][this.column + 1] == 0) {
                    column++;
                    return true;
                }
                break;
            case 2:
                if (Constants.getMapTwo()[this.row][this.column + 1] == 0) {
                    column++;
                    return true;
                }
                break;
            case 3:
                if (Constants.getMapThree()[this.row][this.column + 1] == 0) {
                    column++;
                    return true;
                }
                break;
            case 4:
                if (Constants.getMapFour()[this.row][this.column + 1] == 0) {
                    column++;
                    return true;
                }
                break;
        }
        return false;
    }

    int getStatus() {
        return status;
    }

    void setStatus(int status) {
        this.status = status;
    }

    Element() {
    }

    Element(int row, int column) {
        this.row = row;
        this.column = column;
        this.status = 1;
    }
}
