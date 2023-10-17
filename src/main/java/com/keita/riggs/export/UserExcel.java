package com.keita.riggs.export;

import com.keita.riggs.interfaces.Excel;
import com.keita.riggs.model.Address;
import com.keita.riggs.model.Authenticate;
import com.keita.riggs.model.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class UserExcel implements Excel {
    private final List<User> userList;
    private final Workbook workbook;

    public static String[] HEADER = {
            "UserID",
            "Name",
            "Phone#",
            "Email",
            "Street",
            "City",
            "State",
            "Zip Code"
    };

    public UserExcel(List<User> userList) {
        this.userList = userList;
        workbook = new XSSFWorkbook();
    }

    @Override
    public ByteArrayInputStream toExcel() throws IOException {

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = writeExcelHeader();

            int rowIndex = 1;
            for (User user : userList) {
                Address address = user.getAddress();
                Row rowData = sheet.createRow(rowIndex);
                rowData.createCell(0).setCellValue(user.getUserID());
                rowData.createCell(1).setCellValue(user.getFirstName() + " " + user.getLastName());
                rowData.createCell(2).setCellValue(user.getPhoneNum());
                rowData.createCell(3).setCellValue(user.getAuth().getEmail());
                rowData.createCell(4).setCellValue(address.getStreet());
                rowData.createCell(5).setCellValue(address.getCity());
                rowData.createCell(6).setCellValue(address.getState());
                rowData.createCell(7).setCellValue(address.getZipcode());

                rowIndex++;
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            workbook.close();
        }
    }

    @Override
    public Sheet writeExcelHeader(){
        Sheet sheet = workbook.createSheet("User_data");
        Row row = sheet.createRow(0);
        for (int i = 0; i < HEADER.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(HEADER[i]);
        }
        return sheet;
    }
}
