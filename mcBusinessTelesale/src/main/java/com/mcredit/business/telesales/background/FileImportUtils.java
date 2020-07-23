package com.mcredit.business.telesales.background;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.mcredit.model.dto.common.ErrorImportDTO;
import com.mcredit.model.enums.DateFormatTag;
import com.mcredit.util.DateUtil;

public class FileImportUtils {

    final static String         POS = "pos";
    private final static String MIN = "min";
    private final static String MAX = "max";
    private final static String REQUIRE = "require";
    private final static String YREQ = "1";
    private final static String NREQ = "0";
    private final static String DATA_TYPE = "type";
    private final static String INTEGER_TYPE = "integer";
    private final static String LONG_TYPE = "long";
    private final static String DECIMAL_TYPE = "decimal";
    private final static String STRING_TYPE = "String";
    private final static String POSITIVE = "positive";
    private final static String DATE = "date";
    private final static String Y_DATE = "1";
    private final static String MONTH_FIRST = "mothFirst";
    private final static String Y_MONTH_FISRT = "1";
    //
    private final static String VALIDATE_MAXLENGTH = "import.validate.maxlength";
    private final static String VALIDATE_MINLENGTH = "import.validate.minlength";
    private final static String VALIDATE_REQUIRE = "import.validate.require";
    private final static String VALIDATE_DATA_TYPE = "import.validate.data_type";
    private final static String VALIDATE_DATA_DATE = "import.validate.date";

    private static Workbook workbook;

    public static Document readFileImportConfig() throws
            ParserConfigurationException, SAXException, IOException {
        String pathTemplate = "template/importConfig.xml";
        String url = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        File file = new File(url + pathTemplate);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(file);
    }

    public static Sheet getExcelSheet(File excelfile, int index) throws Exception {
        try {
            WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("Cp1252");//UTF-8
            ws.setCellValidationDisabled(true);
            workbook = Workbook.getWorkbook(excelfile, ws);
            return workbook.getSheet(index);
        } catch (BiffException biffEx) {
            return null;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static XSSFSheet getSheetXlsxExcel(File excelfile, int index) throws Exception {
    	XSSFWorkbook wb = null;
    	try {
            wb = new XSSFWorkbook(excelfile.getPath());
            return wb.getSheetAt(index);
        } catch (Exception ex) {
            return null;
        } finally {
        	wb.close();
        }
    }

    /**
     * Name: validateImportUser Function: method to validate when import a list
     */
    static boolean checkRowNotBlank(Cell[] cell) {
        for (int i = 0; i < cell.length; i++) {
            if (cell[i].getContents().trim().length() > 0) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("deprecation")
	static boolean checkXlsxRowNotBlank(XSSFRow row) {
        for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
            XSSFCell cell = row.getCell(i);
            if (cell != null) {
                if (cell.getCellType() != XSSFCell.CELL_TYPE_BLANK) {
                    return true;
                }
            }
        }
        return false;
    }

    static void checkRequire(Cell[] cell,
            Node rootTable, List<ErrorImportDTO> lstResult, int row) {
        try {
            ErrorImportDTO err;
            int position;
            if (rootTable.hasChildNodes()) {
                NodeList lstNode = rootTable.getChildNodes();
                for (int i = 0; i < lstNode.getLength(); i++) {
                    if (lstNode.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        Element el = (Element) lstNode.item(i);
                        if (el.getAttribute(REQUIRE).equalsIgnoreCase(YREQ)) {
                            position = Integer.parseInt(el.getAttribute(POS));
                            if (cell.length <= position || (cell.length > position && cell[position].getContents().trim().length() == 0) || (cell.length > position && cell[position].getContents().trim().equals("")) || (cell.length > position && cell[position].getContents().trim() == null)) {
                                err = new ErrorImportDTO();
                                err.setColumn(position + 1);
                                err.setRow(row);
                                System.out.println("----- checkRequire VALIDATE_REQUIRE row/col  " + row + "/" + (position + 1));
                                err.setDescrption(VALIDATE_REQUIRE);
                                err.setCellErr(toNameCellErr(position + 1) + row);
                                lstResult.add(err);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    static void checkXlsxRequire(XSSFRow cell,
            Node rootTable, List<ErrorImportDTO> lstResult, int row) {
        try {
            ErrorImportDTO err;
            int position;
            if (rootTable.hasChildNodes()) {
                NodeList lstNode = rootTable.getChildNodes();
                for (int i = 0; i < lstNode.getLength(); i++) {
                    if (lstNode.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        Element el = (Element) lstNode.item(i);
                        if (el.getAttribute(REQUIRE).equalsIgnoreCase(YREQ)) {
                            position = Integer.parseInt(el.getAttribute(POS));
                            int numCell = cell.getPhysicalNumberOfCells();
                            if (numCell <= position || (numCell > position && cell.getCell(position).getStringCellValue().trim().length() == 0)
                                    || (numCell > position && cell.getCell(position).getStringCellValue().trim().equals(""))
                                    || (numCell > position && cell.getCell(position).getStringCellValue().trim() == null)) {
                                err = new ErrorImportDTO();
                                err.setColumn(position + 1);
                                err.setRow(row);
                                err.setDescrption(VALIDATE_REQUIRE);
                                err.setCellErr(toNameCellErr(position + 1) + row);
                                lstResult.add(err);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    static void checkMaxLength(Cell[] cell,
            Node rootTable, List<ErrorImportDTO> lstResult, int row) {
        try {
            ErrorImportDTO err;
            int position;
            if (rootTable.hasChildNodes()) {
                NodeList lstNode = rootTable.getChildNodes();
                for (int i = 0; i < lstNode.getLength(); i++) {
                    if (lstNode.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        Element el = (Element) lstNode.item(i);
                        //Neu yeu cau kiem tra dieu kien MAX LENGTH
                        if (el.getAttribute(MAX) != null && !"".equals(el.getAttribute(MAX))) {
                            int maxlength = Integer.parseInt(el.getAttribute(MAX));
                            position = Integer.parseInt(el.getAttribute(POS));
                            if (cell.length > position && maxlength
                                    < cell[position].getContents().trim().length()) {
                                err = new ErrorImportDTO();
                                err.setColumn(position + 1);
                                err.setRow(row);
                                err.setDescrption(VALIDATE_MAXLENGTH.replace("?", maxlength + ""));
                                err.setCellErr(toNameCellErr(position + 1) + row);
                                lstResult.add(err);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static void checkXlsxMaxLength(XSSFRow cell,
            Node rootTable, List<ErrorImportDTO> lstResult, int row) {
        try {
            ErrorImportDTO err;
            int position;
            if (rootTable.hasChildNodes()) {
                NodeList lstNode = rootTable.getChildNodes();
                for (int i = 0; i < lstNode.getLength(); i++) {
                    if (lstNode.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        Element el = (Element) lstNode.item(i);
                        //Neu yeu cau kiem tra dieu kien MAX LENGTH
                        if (el.getAttribute(MAX) != null && !"".equals(el.getAttribute(MAX))) {
                            int maxlength = Integer.parseInt(el.getAttribute(MAX));
                            position = Integer.parseInt(el.getAttribute(POS));
                            if (cell.getCell(position) != null) {
                                if (cell.getPhysicalNumberOfCells() > position && maxlength
                                        < cell.getCell(position).getStringCellValue().trim().length()) {
                                    err = new ErrorImportDTO();
                                    err.setColumn(position + 1);
                                    err.setRow(row);
                                    err.setDescrption(VALIDATE_MAXLENGTH.replace("?", maxlength + ""));
                                    err.setCellErr(toNameCellErr(position + 1) + row);
                                    lstResult.add(err);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static void checkMinLength(Cell[] cell,
            Node rootTable, List<ErrorImportDTO> lstResult, int row) {
        try {
            ErrorImportDTO err;
            int position;
            if (rootTable.hasChildNodes()) {
                NodeList lstNode = rootTable.getChildNodes();
                for (int i = 0; i < lstNode.getLength(); i++) {
                    if (lstNode.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        Element el = (Element) lstNode.item(i);
                        if (el.getAttribute(MIN) != null && !"".equals(el.getAttribute(MIN))) {
                            int minlength = Integer.parseInt(el.getAttribute(MIN));
                            position = Integer.parseInt(el.getAttribute(POS));
                            if (cell[position].getContents().trim().length() > 0) {
                                if (minlength
                                        > cell[position].getContents().trim().length()) {
                                    err = new ErrorImportDTO();
                                    err.setColumn(position + 1);
                                    err.setRow(row);
                                    err.setDescrption(VALIDATE_MINLENGTH.replace("?", minlength + ""));
                                    err.setCellErr(toNameCellErr(position + 1) + row);
                                    lstResult.add(err);
                                }
                            }

                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    static void checkXlsxMinLength(XSSFRow cell,
            Node rootTable, List<ErrorImportDTO> lstResult, int row) {
        try {
            ErrorImportDTO err;
            int position;
            if (rootTable.hasChildNodes()) {
                NodeList lstNode = rootTable.getChildNodes();
                for (int i = 0; i < lstNode.getLength(); i++) {
                    if (lstNode.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        Element el = (Element) lstNode.item(i);
                        if (el.getAttribute(MIN) != null && !"".equals(el.getAttribute(MIN))) {
                            int minlength = Integer.parseInt(el.getAttribute(MIN));
                            position = Integer.parseInt(el.getAttribute(POS));
                            if (cell.getCell(position) != null) {
                                if (cell.getCell(position).getStringCellValue().trim().length() > 0) {
                                    if (minlength
                                            > cell.getCell(position).getStringCellValue().trim().length()) {
                                        err = new ErrorImportDTO();
                                        err.setColumn(position + 1);
                                        err.setRow(row);
                                        err.setDescrption(VALIDATE_MINLENGTH.replace("?", minlength + ""));
                                        err.setCellErr(toNameCellErr(position + 1) + row);
                                        lstResult.add(err);
                                    }
                                }
                            }

                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    static void checkDataType(Cell[] cell, Node rootTable, List<ErrorImportDTO> lstResult, int row) {
        try {
            ErrorImportDTO err;
            int position;
            if (rootTable.hasChildNodes()) {
                NodeList lstNode = rootTable.getChildNodes();
                for (int i = 0; i < lstNode.getLength(); i++) {
                    if (lstNode.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        Element el = (Element) lstNode.item(i);
                        position = Integer.parseInt(el.getAttribute(POS));
                        boolean error = false;
                        boolean isTestPositive = false;
                        boolean isTestUnsigned = false;
                        String description = VALIDATE_DATA_TYPE;
                        if (el.getAttribute(DATA_TYPE).equalsIgnoreCase(INTEGER_TYPE)) {
                            if (el.getAttribute(POSITIVE).equalsIgnoreCase(YREQ)) {
                                isTestPositive = true;
                            } else if (el.getAttribute(POSITIVE).equalsIgnoreCase(NREQ)) {
                                isTestUnsigned = true;
                            }
                            if (cell.length > position) {
                                String strValue = cell[position].getContents().trim();
                                if (strValue.equals("")) {
                                    continue; // Ko check neu ko co gia tri
                                }
                                try {
                                    int value = Integer.parseInt(strValue);
                                    if (isTestPositive && value <= 0) {
                                        error = true;
                                        description = description.replace("?", "so nguyen duong");
                                    } else if (isTestUnsigned && value < 0) {
                                        error = true;
                                        description = description.replace("?", "so nguyen khong am");
                                    }
                                } catch (NumberFormatException e) {
                                    error = true;
                                    description = description.replace("?", "so nguyen");
                                }
                            }
                        } else if (el.getAttribute(DATA_TYPE).equalsIgnoreCase(DECIMAL_TYPE)) {
                            if (el.getAttribute(POSITIVE).equalsIgnoreCase(YREQ)) {
                                isTestPositive = true;
                            } else if (el.getAttribute(POSITIVE).equalsIgnoreCase(NREQ)) {
                                isTestUnsigned = true;
                            }
                            if (cell.length > position) {
                                String strValue = cell[position].getContents().trim();
                                if (strValue.equals("")) {
                                    continue; // Ko check neu ko co gia tri
                                }
                                try {
                                    double value = Double.parseDouble(strValue.replaceAll(",", ""));
                                    if (isTestPositive && value <= 0) {
                                        error = true;
                                        description = description.replace("?", "so thap phan duong");
                                    } else if (isTestUnsigned && value < 0) {
                                        error = true;
                                        description = description.replace("?", "so thap phan khong am");
                                    }
                                } catch (NumberFormatException e) {
                                    error = true;
                                    description = description.replace("?", "so thap phan");
                                }
                            }
                        } else if (el.getAttribute(DATA_TYPE).equalsIgnoreCase(LONG_TYPE)) {
                            if (el.getAttribute(POSITIVE).equalsIgnoreCase(YREQ)) {
                                isTestPositive = true;
                            } else if (el.getAttribute(POSITIVE).equalsIgnoreCase(NREQ)) {
                                isTestUnsigned = true;
                            }
                            if (cell.length > position) {
                                String strValue = cell[position].getContents().trim();
                                if (strValue.equals("")) {
                                    continue; // Ko check neu ko co gia tri
                                }
                                try {
                                    long value = Long.parseLong(strValue);
                                    if (isTestPositive && value <= 0) {
                                        error = true;
                                        description = description.replace("?", "so nguyen duong");
                                    } else if (isTestUnsigned && value < 0) {
                                        error = true;
                                        description = description.replace("?", "so nguyen khong am");
                                    }
                                } catch (NumberFormatException e) {
                                    error = true;
                                    description = description.replace("?", "so nguyen");
                                }
                            }
                        }
                        if (error) {
                            err = new ErrorImportDTO();
                            err.setColumn(position + 1);
                            err.setRow(row);
                            err.setDescrption(description);
                            err.setCellErr(toNameCellErr(position + 1) + row);
                            lstResult.add(err);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    static void checkXlsxDataType(XSSFRow cell, Node rootTable, List<ErrorImportDTO> lstResult, int row) {
        try {
            ErrorImportDTO err;
            int position;
            if (rootTable.hasChildNodes()) {
                NodeList lstNode = rootTable.getChildNodes();
                for (int i = 0; i < lstNode.getLength(); i++) {
                    if (lstNode.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        Element el = (Element) lstNode.item(i);
                        position = Integer.parseInt(el.getAttribute(POS));
                        boolean error = false;
                        boolean isTestPositive = false;
                        boolean isTestUnsigned = false;
                        String description = VALIDATE_DATA_TYPE;
                        if (el.getAttribute(DATA_TYPE).equalsIgnoreCase(INTEGER_TYPE)) {
                            if (el.getAttribute(POSITIVE).equalsIgnoreCase(YREQ)) {
                                isTestPositive = true;
                            } else if (el.getAttribute(POSITIVE).equalsIgnoreCase(NREQ)) {
                                isTestUnsigned = true;
                            }
                            if (cell.getPhysicalNumberOfCells() > position) {
                                if (cell.getCell(position) != null) {
                                    String strValue = cell.getCell(position).getStringCellValue().trim();
                                    if (strValue.equals("")) {
                                        continue; // Ko check neu ko co gia tri
                                    }
                                    try {
                                        int value = Integer.parseInt(strValue);
                                        if (isTestPositive && value <= 0) {
                                            error = true;
                                            description = description.replace("?", "so nguyen duong");
                                        } else if (isTestUnsigned && value < 0) {
                                            error = true;
                                            description = description.replace("?", "so nguyen khong am");
                                        }
                                    } catch (NumberFormatException e) {
                                        error = true;
                                        description = description.replace("?", "so nguyen");
                                    }
                                }

                            }
                        } else if (el.getAttribute(DATA_TYPE).equalsIgnoreCase(DECIMAL_TYPE)) {
                            if (el.getAttribute(POSITIVE).equalsIgnoreCase(YREQ)) {
                                isTestPositive = true;
                            } else if (el.getAttribute(POSITIVE).equalsIgnoreCase(NREQ)) {
                                isTestUnsigned = true;
                            }
                            if (cell.getPhysicalNumberOfCells() > position) {
                                if (cell.getCell(position) != null) {
                                    String strValue = cell.getCell(position).getStringCellValue().trim();
                                    if (strValue.equals("")) {
                                        continue; // Ko check neu ko co gia tri
                                    }
                                    try {
                                        double value = Double.parseDouble(strValue.replaceAll(",", ""));
                                        if (isTestPositive && value <= 0) {
                                            error = true;
                                            description = description.replace("?", "so thap phan duong");
                                        } else if (isTestUnsigned && value < 0) {
                                            error = true;
                                            description = description.replace("?", "so thap phan khong am");
                                        }
                                    } catch (NumberFormatException e) {
                                        error = true;
                                        description = description.replace("?", "so thap phan");
                                    }
                                }
                            }
                        } else if (el.getAttribute(DATA_TYPE).equalsIgnoreCase(LONG_TYPE)) {
                            if (el.getAttribute(POSITIVE).equalsIgnoreCase(YREQ)) {
                                isTestPositive = true;
                            } else if (el.getAttribute(POSITIVE).equalsIgnoreCase(NREQ)) {
                                isTestUnsigned = true;
                            }
                            if (cell.getPhysicalNumberOfCells() > position) {
                                if (cell.getCell(position) != null) {
                                    String strValue = cell.getCell(position).getStringCellValue().trim();
                                    if (strValue.equals("")) {
                                        continue; // Ko check neu ko co gia tri
                                    }
                                    try {
                                        long value = Long.parseLong(strValue);
                                        if (isTestPositive && value <= 0) {
                                            error = true;
                                            description = description.replace("?", "so nguyen duong");
                                        } else if (isTestUnsigned && value < 0) {
                                            error = true;
                                            description = description.replace("?", "so nguyen khong am");
                                        }
                                    } catch (NumberFormatException e) {
                                        error = true;
                                        description = description.replace("?", "so nguyen");
                                    }
                                }
                            }
                        }
                        if (error) {
                            err = new ErrorImportDTO();
                            err.setColumn(position + 1);
                            err.setRow(row);
                            err.setDescrption(description);
                            err.setCellErr(toNameCellErr(position + 1) + row);
                            lstResult.add(err);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    static void checkDataDate(Cell[] cell, Node rootTable, List<ErrorImportDTO> lstResult, int row) {
        try {
            ErrorImportDTO err;
            int position;
            if (rootTable.hasChildNodes()) {
                NodeList lstNode = rootTable.getChildNodes();
                for (int i = 0; i < lstNode.getLength(); i++) {
                    if (lstNode.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        Element el = (Element) lstNode.item(i);
                        position = Integer.parseInt(el.getAttribute(POS));
                        boolean error = false;
                        //boolean isDate = false;
                        String description = VALIDATE_DATA_DATE;

                        if (el.getAttribute(DATE).equalsIgnoreCase(Y_DATE)) {
                            if (cell.length > position) {
                                String strDate = cell[position].getContents().trim();
                                if (!strDate.equals("")) {
                                    try {
                                        if (el.getAttribute(MONTH_FIRST).equalsIgnoreCase(Y_MONTH_FISRT)) {
                                            if (!DateUtil.validateFormat(strDate, "dd/MM/yyyy")) {
                                                error = true;
                                            }
                                        } else {
                                            if (!DateUtil.validateFormat(strDate, "yyyy/MM/dd")) {
                                                error = true;
                                            }
                                        }
                                    } catch (Exception e) {
                                        error = true;
                                    }
                                }

                            }
                        }
                        if (error) {
                            err = new ErrorImportDTO();
                            err.setColumn(position + 1);
                            err.setRow(row);
                            err.setDescrption(description);
                            err.setCellErr(toNameCellErr(position + 1) + row);
                            lstResult.add(err);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void checkXlsxDataDate(XSSFRow cell, Node rootTable, List<ErrorImportDTO> lstResult, int row) {
        try {
            ErrorImportDTO err;
            int position;
            if (rootTable.hasChildNodes()) {
                NodeList lstNode = rootTable.getChildNodes();
                for (int i = 0; i < lstNode.getLength(); i++) {
                    if (lstNode.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        Element el = (Element) lstNode.item(i);
                        position = Integer.parseInt(el.getAttribute(POS));
                        boolean error = false;
                        //boolean isDate = false;
                        String description = VALIDATE_DATA_DATE;

                        if (el.getAttribute(DATE).equalsIgnoreCase(Y_DATE)) {
                            if (cell.getPhysicalNumberOfCells() > position) {
                                if (cell.getCell(position) != null) {
                                    String strDate = cell.getCell(position).getStringCellValue().trim();
                                    if (!strDate.equals("")) {
                                        try {
                                            if (el.getAttribute(MONTH_FIRST).equalsIgnoreCase(Y_MONTH_FISRT)) {
                                                if (!DateUtil.validateFormat(strDate, "dd/MM/yyyy")) {
                                                    error = true;
                                                }
                                            } else {
                                                if (!DateUtil.validateFormat(strDate, "yyyy/MM/dd")) {
                                                    error = true;
                                                }
                                            }
                                        } catch (Exception e) {
                                            error = true;
                                        }
                                    }

                                }
                            }
                        }
                        if (error) {
                            err = new ErrorImportDTO();
                            err.setColumn(position + 1);
                            err.setRow(row);
                            err.setDescrption(description);
                            err.setCellErr(toNameCellErr(position + 1) + row);
                            lstResult.add(err);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static boolean checkDuplicateFileExel(Sheet sheet, int row, int col, String stName) throws Exception {
        boolean isDupCol = false;
        String currentCode = stName.toString().trim().toLowerCase();
        for (int j = 1; j < sheet.getRows(); j++) {
            if (sheet.getCell(col, j).getContents().toString().trim().toLowerCase().equals(currentCode)) {
                if (j != row) {
                    isDupCol = true;
                    break;
                }
            }
        }
        return isDupCol;
    }

    /*public static boolean checkDuplicateXlsxFileExel(XSSFSheet sheet, int row, int col, String stName, Node rootTable) throws Exception {
        boolean isDupCol = false;
        for (int j = row; j < sheet.getPhysicalNumberOfRows(); j++) {
            XSSFRow cell = sheet.getRow(j);
            if (FileImportUtils.checkXlsxRowNotBlank(cell)) {
                if (cell.getCell(col) != null) {
                    if (cell.getCell(col).getStringCellValue().trim().equalsIgnoreCase(stName)) {
                        if (j != row) {
                            isDupCol = true;
                            break;
                        }
                    }
                }
            }

        }
        return isDupCol;
    }*/

    static boolean checkDup(Map<Integer, String> mapIdentity, String stName, int row) throws Exception {
        boolean isDupCol = false;
        for (Map.Entry<Integer, String> entry : mapIdentity.entrySet()) {
            if (entry.getValue().trim().equalsIgnoreCase(stName)) {
                if (entry.getKey() != row) {
                    isDupCol = true;
                    break;
                }
            }
        }
        return isDupCol;
    }

    static int getRecordFileExel(Sheet sheet, int row) throws Exception {
        int quantityRecord = 0;
        for (int i = row; i < sheet.getRows(); i++) {
            Cell[] cell = sheet.getRow(i);
            if (checkRowNotBlank(cell)) {
                quantityRecord = quantityRecord + 1;
            }
        }
        return quantityRecord;
    }

    static int getRecordFileXlsxExel(XSSFSheet sheet, int row) throws Exception {
        int quantityRecord = 0;
        for (int i = row; i < sheet.getPhysicalNumberOfRows(); i++) {
            XSSFRow cell = sheet.getRow(i);
            if (checkXlsxRowNotBlank(cell)) {
                quantityRecord = quantityRecord + 1;
            }
        }
        return quantityRecord;
    }

    static String getAttributeChildByParentNode(Node parentNode,
            String childNode, String att) {
        if (parentNode.hasAttributes()) {
            NodeList lstNode = parentNode.getChildNodes();
            for (int i = 0; i < lstNode.getLength(); i++) {
                if (lstNode.item(i).getNodeName().equalsIgnoreCase(childNode.trim()) && lstNode.item(i).
                        getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) lstNode.item(i);
                    return el.getAttribute(att);
                }
            }
        }
        return null;
    }

    static String toNameCellErr(int number) {
        StringBuilder sb = new StringBuilder();
        while (number-- > 0) {
            sb.append((char) ('A' + (number % 26)));
            number /= 26;
        }
        return sb.reverse().toString();
    }

    @SuppressWarnings("deprecation")
	static void setCellType(XSSFRow cellRow, Node rootTable) {
        try {
            int position;
            if (rootTable.hasChildNodes()) {
                NodeList lstNode = rootTable.getChildNodes();
                for (int i = 0; i < lstNode.getLength(); i++) {
                    if (lstNode.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        Element el = (Element) lstNode.item(i);
                        position = Integer.parseInt(el.getAttribute(POS));
                        XSSFCell cell = cellRow.getCell(position);
                        if (cell != null) {
                            if (el.getAttribute(DATA_TYPE).equalsIgnoreCase(INTEGER_TYPE)
                                    || el.getAttribute(DATA_TYPE).equalsIgnoreCase(DECIMAL_TYPE)
                                    || el.getAttribute(DATA_TYPE).equalsIgnoreCase(LONG_TYPE)
                                    || el.getAttribute(DATA_TYPE).equalsIgnoreCase(STRING_TYPE)) {
                                cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                            } else if (el.getAttribute(DATE).equalsIgnoreCase(Y_DATE)) {
                                if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                                    cell.setCellValue(cell.getStringCellValue());
                                } else {
                                    Date strDate = cellRow.getCell(position).getDateCellValue();
                                    try {
                                        String date = null;
                                        if (el.getAttribute(MONTH_FIRST).equalsIgnoreCase(Y_MONTH_FISRT)) {
                                            date = DateFormatTag.DATEFORMAT_dd_MM_yyyy.formatter().format(strDate);
                                        } else {
                                            date = DateFormatTag.DATEFORMAT_yyyy_MM_dd.formatter().format(strDate);
                                        }
                                        if (date != null) {
                                            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                                            cell.setCellValue(date);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }

                    }
                }
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
