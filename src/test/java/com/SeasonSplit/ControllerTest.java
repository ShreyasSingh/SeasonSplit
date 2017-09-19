package com.SeasonSplit;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

public class ControllerTest {

	private static final String VALID_EXCEL_FILE = "C:\\Users\\idnsys\\Documents\\Eclipse Project Archive\\ProjectSeasonSplit\\src\\test\\resources\\ValidExcelSheet.xlsx";
	private SpreadSheet spreadSheetReader;
	private DBService dbService;
	private Writer spreadSheetWriter;
	private SimpleDateFormat unifiedDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private Map<Integer, HotelMSG> validHotelMSGs;
	private String HOTELID_1 = "HILTON1";
	private String HOTELID_2 = "TAJ1";
	private HotelMSG hotelMSG_1 = new HotelMSG(1, HOTELID_1, "MSG1");
	private HotelMSG hotelMSG_2 = new HotelMSG(2, HOTELID_1 , "MSG2");
	private HotelMSG hotelMSG_3 = new HotelMSG(3, HOTELID_2 , "MSG3");
	private SeasonSplitter seasonSplitter = new SeasonSplitter();

	@Before
	public void init(){
		validHotelMSGs = new TreeMap<>();
		validHotelMSGs.put(1, hotelMSG_1);
		validHotelMSGs.put(2, hotelMSG_2);
		validHotelMSGs.put(3, hotelMSG_3);
	}
	@Test
	public void readAValidExcelFileAndSplitSeasonsAndStoreIntoDataBase() {
		spreadSheetReader = new SpreadSheet(VALID_EXCEL_FILE);
		dbService = new DBService(HibernateUtils.getSessionFactory());
		Controller controller = new Controller(spreadSheetReader, seasonSplitter , dbService, spreadSheetWriter);
		controller.execute();

	}

}
