package com.SeasonSplit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

public class SpreadSheetTest {

	private static final String INVALID_FORMAT_EXCEL = "C:\\Users\\idnsys\\Documents\\Eclipse Project Archive\\ProjectSeasonSplit\\src\\test\\resources\\InvalidFormatExcel.xlsx";
	private static final String FileDoesNotExistURL = "";
	private static final String VALID_FORMAT_EXCEL = "C:\\Users\\idnsys\\Documents\\Eclipse Project Archive\\ProjectSeasonSplit\\src\\test\\resources\\ValidExcelSheet.xlsx";
	private SimpleDateFormat unifiedDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private String HOTELID_1 = "HILTON1";
	private HotelMSG hotelMSG_1 = new HotelMSG(1, HOTELID_1, "MSG1");
	private String HOTELID_2 = "TAJ1";
	private HotelMSG hotelMSG_2 = new HotelMSG(2, HOTELID_1 , "MSG2");
	private HotelMSG hotelMSG_3 = new HotelMSG(3, HOTELID_2 , "MSG3");
	private Map<Integer, HotelMSG> hotelMSGs;
	private SpreadSheet spreadSheetReader;

	@Before
	public void init(){
		hotelMSGs = new TreeMap<>();
		hotelMSGs.put(1, hotelMSG_1);
		hotelMSGs.put(2, hotelMSG_2);
		hotelMSGs.put(3, hotelMSG_3);
	}
	@Test
	public void FileDoesNotExists() {
		spreadSheetReader = new SpreadSheet(FileDoesNotExistURL);
		Boolean isLoaded = spreadSheetReader.load();
		assertFalse(isLoaded);
	}

	@Test
	public void dataIsInInvalidFormat() {
		spreadSheetReader = new SpreadSheet(INVALID_FORMAT_EXCEL);
		spreadSheetReader.load();
		Map<HotelMSG, Set<Season>> records = spreadSheetReader.readAndValidate(unifiedDateFormat, hotelMSGs);
		assertTrue(records.isEmpty());

	}

	@Test
	public void dataIsInValidFormat() {
		spreadSheetReader = new SpreadSheet(VALID_FORMAT_EXCEL);
		spreadSheetReader.load();
		Map<HotelMSG, Set<Season>> records = spreadSheetReader.readAndValidate(unifiedDateFormat, hotelMSGs);
		assertFalse(records.isEmpty());

	}
}
