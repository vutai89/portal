package com.mcredit.model.dto.common;

public class ErrorImportDTO {

    private int row;
    private int column;
    //ten cot :A,B,C........
    private String columName;
    private String descrption;
    private String cellErr;
    private String valueDupExel;
    private String valueDupDB;

    public String getValueDupExel() {
        return valueDupExel;
    }

    public void setValueDupExel(String valueDupExel) {
        this.valueDupExel = valueDupExel;
    }

    public String getValueDupDB() {
        return valueDupDB;
    }

    public void setValueDupDB(String valueDupDB) {
        this.valueDupDB = valueDupDB;
    }

    public String getCellErr() {
        return cellErr;
    }

    public void setCellErr(String cellErr) {
        this.cellErr = cellErr;
    }

    public String getColumName() {
        return columName;
    }

    public void setColumName(String columName) {
        this.columName = columName;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public ErrorImportDTO() {
    }

    public ErrorImportDTO(int row, int column, String descrption) {
        this.row = row;
        this.column = column;
        this.descrption = descrption;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ErrorImportDTO other = (ErrorImportDTO) obj;
        if (this.row != other.row) {
            return false;
        }
        if (this.column != other.column) {
            return false;
        }
        if (this.descrption != other.descrption && (this.descrption == null || !this.descrption.equals(other.descrption))) {
            return false;
        }
        return true;
    }
}
