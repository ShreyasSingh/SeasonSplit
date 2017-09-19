package com.SeasonSplit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SpreadSheet {

	private String filePath;
	private String checkSumOfPreviousFile;
	private XSSFWorkbook workBook;

	public SpreadSheet(final String filePath) {
		this.filePath = filePath;
	}

	public Map<HotelMSG, Set<Season> > readAndValidate(final SimpleDateFormat unifiedDateFormat, final Map<Integer, HotelMSG> validHotelMSGs) {

		XSSFSheet sheet = workBook.getSheetAt(0);
		Map<HotelMSG, Set<Season>> records = new TreeMap<>();
		Boolean isDataInValidFormat = checkFormatOf(sheet);
		if (isDataInValidFormat) {
			for (Iterator<Row> rowIterator = sheet.rowIterator(); rowIterator.hasNext();) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while(cellIterator.hasNext()){
					try{
						HotelMSG hotelMsg = new HotelMSG();
						makeHotelMSG(cellIterator, hotelMsg);
						Boolean isValidHotelMSG = validateHotelMSG(hotelMsg, validHotelMSGs);

						if (!isValidHotelMSG)
							break;
						Season season = new Season();
						makeSeason(season,hotelMsg, unifiedDateFormat, cellIterator);
						Boolean validSeason = validateSeason(season);
						if(!validSeason){
							break;
						}

						makeRecordsWith(records, hotelMsg, season);

					}
					catch (Exception e) {

					}
				}
			}
		}
		try {
			workBook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return records;
	}

	private Boolean validateHotelMSG(final HotelMSG newHotelMsg, final Map<Integer, HotelMSG> validHotelMSGs) {

		for(Map.Entry<Integer, HotelMSG> entry : validHotelMSGs.entrySet()){
			Integer hotelMSGID = entry.getKey();
			HotelMSG validHotelMSG = entry.getValue();
			if(validHotelMSG.compareTo(newHotelMsg)  == 0 ){
				newHotelMsg.setMsgID(hotelMSGID);
				return true;
			}
		}
		return false;

	}

	private void makeRecordsWith(final Map<HotelMSG, Set<Season>> records, final HotelMSG currentHotelMsg, final Season season) {

		if(records.containsKey(currentHotelMsg)){
			Set<Season> newSeasons = records.get(currentHotelMsg);
			if(!newSeasons.contains(season)){
				newSeasons.add(season);
			}
			newSeasons.add(season);
		}
		if(!records.containsKey(currentHotelMsg)){
			Set<Season> newSeasons = new LinkedHashSet<>();
			newSeasons.add(season);
			records.put(currentHotelMsg, newSeasons);
		}


	}

	private Boolean validateSeason(final Season season) {
		if(!season.getSeasonStartDate().isBefore(season.getSeasonEndDate())){
			return false;
		}

		if(!season.getSeasonEndDate().isAfter(LocalDate.now())){
			return false;
		}

		String[] seasonBar = season.getSeasonBAR().split(",");
		for(String dayOFWeekRate : seasonBar){
			double rate = Double.parseDouble(dayOFWeekRate);
			if(rate < 0){
				return false;
			}
		}

		if(season.getSeasonStartDate().isBefore(LocalDate.now())){
			season.setSeasonStartDate(LocalDate.now());
		}
		return true;


	}

	private void makeSeason (final Season season, final HotelMSG  hotelMSG, final SimpleDateFormat unifiedDateFormat, final Iterator<Cell> cellIterator) {
		season.setHotelMSG(hotelMSG);

		season.setSeasonStartDate(LocalDate.parse(unifiedDateFormat.format(cellIterator.next().getDateCellValue())));
		season.setSeasonEndDate(LocalDate.parse(unifiedDateFormat.format(cellIterator.next().getDateCellValue())));

		StringBuilder seasonBar = new StringBuilder();

		for(int i = 0;i < 7; i++){
			Cell cell = cellIterator.next();
			seasonBar.append(cell.getNumericCellValue());
			seasonBar.append(",");
		}
		season.setSeasonBAR(seasonBar.toString());
	}


	private void makeHotelMSG(final Iterator<Cell> cellIterator, final HotelMSG hotelMsg) {

		hotelMsg.setHotelID(cellIterator.next().getStringCellValue().toUpperCase());
		hotelMsg.setMSGName(cellIterator.next().getStringCellValue().toUpperCase());

	}

	private Boolean checkFormatOf(final XSSFSheet sheet) {
		if (sheet.getRow(0).getPhysicalNumberOfCells() == 11)
			return true;
		return false;
	}

	/*	private String getMD5Checksum(final File file) {
		byte[] checksumInBytes = createChecksum(file);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < checksumInBytes.length; i++) {
			sb.append(Integer.toString((checksumInBytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

	private byte[] createChecksum(final File file) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
			final byte[] buffer = new byte[8192];
			for (int read = 0; (read = is.read(buffer)) != -1;) {
				messageDigest.update(buffer, 0, read);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return messageDigest.digest();
	}

	 */	public Boolean load() {

		 try (FileInputStream excelFileStream = new FileInputStream(filePath)) {
			 workBook = new XSSFWorkbook(excelFileStream);
		 } catch (FileNotFoundException e) {
			 return false;
		 } catch (IOException e) {
			 return false;
		 }
		 return true;
	 }
}
