package com.keita.riggs.interfaces;

import org.apache.poi.ss.usermodel.Sheet;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface Excel {

    public ByteArrayInputStream toExcel() throws IOException;

    public Sheet writeExcelHeader();
}
