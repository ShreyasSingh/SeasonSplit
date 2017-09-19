package com.SeasonSplit;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Controller {

	private SpreadSheet spreadSheetReader;
	private DBService dbService;
	private Writer writer;
	private SeasonSplitter seasonSplitter;

	public Controller(final SpreadSheet spreadSheetReader, final SeasonSplitter seasonSplitter, final DBService dbService, final Writer writer) {
		this.spreadSheetReader = spreadSheetReader;
		this.seasonSplitter = seasonSplitter;
		this.dbService = dbService;
		this.writer = writer;
	}

	public void execute() {
		Boolean isLoaded = spreadSheetReader.load();
		if(isLoaded){
			Map<Integer, HotelMSG> hotelMSGsOrderedByMSGIDs = dbService.fetchAllHotelMSGsOrderedByMSGIDs();
			SimpleDateFormat unifiedDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Map<HotelMSG, Set<Season>> newRecords = spreadSheetReader.readAndValidate(unifiedDateFormat , hotelMSGsOrderedByMSGIDs);
			//getSeasonMinStartDateAndSeasonMaxEndDate();
			LocalDate seasonMinimumStartDate = getSeasonMinimumStartDate(newRecords);
			LocalDate seasonMaximumEndDate = getSeasonMaximumEndDate(newRecords);
			List<Integer> uniqueHotelMSGIds = getHotelMSGIds(newRecords);
			Map<HotelMSG, List<Season>> previouslySplittedSeasonsForParticularHotelMSGIds = dbService.fetchAllSeasonsForGivenHotelMSGIDsBetweenStartDateAndEndDate(uniqueHotelMSGIds, seasonMinimumStartDate, seasonMaximumEndDate);


			for(Map.Entry<HotelMSG, Set<Season>> eachRecord : newRecords.entrySet()){
				HotelMSG eachHotelMSG = eachRecord.getKey();
				List<Season> previouslySplittedSeasons = previouslySplittedSeasonsForParticularHotelMSGIds.get(eachHotelMSG );
				Set<Season> splittedSeasons = seasonSplitter.splitSeason(previouslySplittedSeasons, eachRecord.getValue());
				previouslySplittedSeasons.clear();
				previouslySplittedSeasons.addAll(splittedSeasons);
			}

			dbService.deleteAllSeasonsForGivenHotelMSGIDsBetweenStartDateAndEndDate(uniqueHotelMSGIds, seasonMinimumStartDate, seasonMaximumEndDate);
			for(HotelMSG eachHotelMSG : previouslySplittedSeasonsForParticularHotelMSGIds.keySet()){
				dbService.saveSeasonsForParicularHotelMSG(eachHotelMSG, previouslySplittedSeasonsForParticularHotelMSGIds.get(eachHotelMSG));
			}

		}
	}

	private List<Integer> getHotelMSGIds(final Map<HotelMSG, Set<Season>> newRecords) {
		List<Integer> HotelMSGIds = new ArrayList<>();
		for(HotelMSG HotelMSG : newRecords.keySet()){
			HotelMSGIds.add(HotelMSG.getMsgID());

		}
		return HotelMSGIds;
	}

	private LocalDate getSeasonMaximumEndDate(final Map<HotelMSG, Set<Season>> newRecords) {
		LocalDate seasonMaximumEndDate = LocalDate.now();
		for(Map.Entry<HotelMSG, Set<Season>> eachRecord : newRecords.entrySet()){
			for(Season eachSeason : eachRecord.getValue()){
				if(!seasonMaximumEndDate.isAfter(eachSeason.getSeasonEndDate())){
					seasonMaximumEndDate = eachSeason.getSeasonEndDate();
				}
			}

		}
		return seasonMaximumEndDate;
	}

	private LocalDate getSeasonMinimumStartDate(final Map<HotelMSG, Set<Season>> newRecords) {
		LocalDate seasonMinimumStartDate = LocalDate.now();
		for(Map.Entry<HotelMSG, Set<Season>> eachRecord : newRecords.entrySet()){
			for(Season eachSeason : eachRecord.getValue()){
				if(!seasonMinimumStartDate.isBefore(eachSeason.getSeasonStartDate())){
					seasonMinimumStartDate = eachSeason.getSeasonStartDate();
				}
			}

		}
		return seasonMinimumStartDate;
	}

}
