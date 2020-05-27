package sample.handlers;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sample.model.InspectionResult;
import sample.model.Report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ExcelHandler {

    // ======== MyCell ===========================

    public static class MyCell{
        private String data;
        private int row;
        private int column;

        public MyCell(String data, int row, int column) {
            this.data = data;
            this.row = row;
            this.column = column;
        }

        public String getData() {
            return data;
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }
    }

    public static ArrayList<MyCell> getMyCells(Report report){
        ArrayList<MyCell> myCells = new ArrayList<>();

        // Customer data
        myCells.add(new MyCell(report.getCustomer().getName(), 2, 3));
        myCells.add(new MyCell(report.getCustomer().getTestPlace(), 4,3));

        // Equipment data
        myCells.add(new MyCell(((Double) report.getEquipment().getPoleDistance()).toString() + "mm", 8, 4));
        myCells.add(new MyCell(report.getEquipment().getEquipment(), 9, 4));
        myCells.add(new MyCell(report.getEquipment().getMPCarrierMedium(), 10, 4));
        myCells.add(new MyCell(report.getEquipment().getMagTech(), 11, 4));
        myCells.add(new MyCell(report.getEquipment().getUVLightIntensity(), 12, 4));
        myCells.add(new MyCell(report.getEquipment().getDistanceOfLight(), 13, 4));

        // Inspection result data
        for(int i = 0; i < report.getInspectionResults().size(); i++){
            InspectionResult inspectionResult = report.getInspectionResults().get(i);
            myCells.add(new MyCell(((Integer) inspectionResult.getSerialNo()).toString(), i+24, 0));
            myCells.add(new MyCell(inspectionResult.getWeldPieceNo(), i+24, 1));
            myCells.add(new MyCell(((Double)inspectionResult.getTestLength()).toString(), i+24, 8));
            myCells.add(new MyCell(inspectionResult.getWeldingProcess(), i+24, 11));
            myCells.add(new MyCell(((Double)inspectionResult.getThickness()).toString(), i+24, 17));
            myCells.add(new MyCell(inspectionResult.getDiameter(), i+24, 18));
            myCells.add(new MyCell(inspectionResult.getDefectType(), i+24, 22));
            myCells.add(new MyCell(inspectionResult.getDefectLoc(), i+24,  24));
            myCells.add(new MyCell(inspectionResult.getResult(), i+24, 27));
        }
        // Users data
        myCells.add(new MyCell(report.getOperator().toString(),39, 5));
        myCells.add(new MyCell("Level " +((Integer)report.getOperator().getLevel()).toString(), 40, 5));
        myCells.add(new MyCell(report.getOperatorDate().toString(), 41, 5));

        myCells.add(new MyCell(report.getEvaluator().toString(),39, 15));
        myCells.add(new MyCell("Level " +((Integer)report.getEvaluator().getLevel()).toString(), 40, 15));
        myCells.add(new MyCell(report.getEvaluatorDate().toString(), 41, 15));

        myCells.add(new MyCell(report.getConformer().toString(),39, 20));
        myCells.add(new MyCell("Level " +((Integer)report.getConformer().getLevel()).toString(), 40, 20));
        myCells.add(new MyCell(report.getConformerDate().toString(), 41, 20));

        // Other Customer things
        myCells.add(new MyCell(report.getJobOrderNo().toString(), 5, 26));
        myCells.add(new MyCell(report.getProjectName().toString(), 3, 3));
        myCells.add(new MyCell(report.getOfferNo().toString(), 6, 26));

        // Other Data
        myCells.add(new MyCell(report.getInspectionStandard(), 5, 3));
        myCells.add(new MyCell(report.getEvaluationStandard(), 6, 3));
        myCells.add(new MyCell(report.getInspectionProcedure(), 2, 19));
        myCells.add(new MyCell(((Integer)report.getInspectionScope()).toString() + "%", 3, 19));
        myCells.add(new MyCell(report.getDrawingNo(), 4, 19));
        myCells.add(new MyCell(report.getSurfaceCondition().toString(), 5, 19));
        myCells.add(new MyCell(report.getStageOfExamination().toString(), 6, 19));
        myCells.add(new MyCell(((Integer) report.getNumberOfPages()).toString(), 2, 26));
        myCells.add(new MyCell(report.getReportNo(), 3, 26));
        myCells.add(new MyCell(report.getReportDate().toString(), 4, 26));
        myCells.add(new MyCell(report.getExaminationArea(), 8, 16));
        myCells.add(new MyCell(report.getCurrentType(), 9, 16));
        myCells.add(new MyCell(report.getLuxmeter(), 10, 16));
        myCells.add(new MyCell(report.getTestMedium(), 11, 16));
        myCells.add(new MyCell(report.getDemagnetization(), 12, 16));
        myCells.add(new MyCell(report.getHeatTreatment(), 13, 16));
        myCells.add(new MyCell(((Double)report.getSurfaceTemperature()).toString() + "ºc", 8, 25));
        myCells.add(new MyCell(report.getGaussFieldStrength()+" kA/m", 9, 25));
        myCells.add(new MyCell(report.getEquipmentSurfaceCondition(), 11, 25));
        myCells.add(new MyCell(report.getIdentificationOfLightEquip(), 12, 25));
        myCells.add(new MyCell(report.getLiftingTestDateNumber(), 13, 25));
        myCells.add(new MyCell(report.getStandardDeviations(), 19, 7));
        myCells.add(new MyCell(report.getInspectionDates().toString(), 20, 7));
        myCells.add(new MyCell(report.getDescriptionAndAttachments(), 21, 7));

        return myCells;
    }

    // ======= Excel Functions ===================

    public static void getExcel(Report report, String path) throws IOException{
        XSSFWorkbook xssfWorkbook = getWorkbook();
        ArrayList<MyCell> myCells = getMyCells(report);
        for (MyCell myCell : myCells) {
            writeCell(xssfWorkbook, myCell);
        }

        if(report.isButtWeld()){
            CellStyle cellStyle = xssfWorkbook.getSheetAt(0).getRow(14).getCell(0).getCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.RIGHT);
            cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
            xssfWorkbook.getSheetAt(0).getRow(14).getCell(0).setCellValue("✔");
        }
        if(report.isFilerWeld()){
            CellStyle cellStyle = xssfWorkbook.getSheetAt(0).getRow(14).getCell(7).getCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.RIGHT);
            cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
            xssfWorkbook.getSheetAt(0).getRow(14).getCell(7).setCellValue("✔");
        }

        xssfWorkbook.write(new FileOutputStream(path));
        System.out.println("File is saved");
        xssfWorkbook.close();
    }

    private static XSSFWorkbook getWorkbook() throws IOException {
        FileInputStream inputStream = new FileInputStream(new File("src/assets/reports/report.xlsx"));
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);
        System.out.println("File is opened");
        return xssfWorkbook;
    }

    private static void writeCell(XSSFWorkbook xssfWorkbook, MyCell myCell){
        XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
        XSSFRow row = sheet.getRow(myCell.getRow());
        XSSFCell cell = row.getCell(myCell.getColumn());
        cell.setCellValue(myCell.getData());
    }

}
