package com.SeasonSplit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class SeasonSplitterTest {
	private String bar_1 = "1, 1, 1, 1, 1, 1, 1";
	private HotelMSG hotelMSG = new HotelMSG( 1, "HILTON1", "MSG1");
	private String bar_2 = "2, 2, 2, 2, 2, 2, 2";
	private Set<Season> newSeasons;
	private List<Season> previouslySplittedSeasons;
	private Season season;
	private LocalDate _1Jan2018 = LocalDate.of(2018, 1, 1);
	private LocalDate _30Jan2018 = LocalDate.of(2018, 1, 30);
	private LocalDate _10Jan2018 = LocalDate.of(2018, 1, 10);
	private LocalDate _20Jan2018 = LocalDate.of(2018, 1, 20);
	private LocalDate _25Jan2018 = LocalDate.of(2018, 1, 25);
	private LocalDate _15Jan2018 = LocalDate.of(2018, 1, 15);
	private LocalDate _28Jan2018 = LocalDate.of(2018, 1, 28);
	private LocalDate _7Feb2018 = LocalDate.of(2018, 2, 7);
	private LocalDate _13Jan2018 = LocalDate.of(2018, 1, 13);
	private SeasonSplitter seasonSplitter;


	@Before
	public void setUp(){
		newSeasons = new LinkedHashSet<>();
		previouslySplittedSeasons =  new LinkedList<Season>();
		seasonSplitter = new SeasonSplitter();
	}

	@Test
	public void thereAreNoPreviousSeasons() {
		addNewSeason().withHotelMSG(hotelMSG).startDate(_10Jan2018).endDate(_20Jan2018).andBar(bar_2).addToNewSeasonsList();

		Set<Season> splittedSeasons = seasonSplitter.splitSeason(previouslySplittedSeasons , newSeasons);
		assertEquals(1, splittedSeasons.size());
		assertTrue(checkProperSplit(splittedSeasons));

	}



	@Test
	public void normal3SeasonSplit() {

		addNewSeason().withHotelMSG(hotelMSG).startDate(_1Jan2018).endDate(_30Jan2018).andBar(bar_1).addToPreviousSeasonsList();
		addNewSeason().withHotelMSG(hotelMSG).startDate(_10Jan2018).endDate(_20Jan2018).andBar(bar_2).addToNewSeasonsList();

		Set<Season> splittedSeasons = seasonSplitter.splitSeason(previouslySplittedSeasons , newSeasons);
		assertEquals(3, splittedSeasons.size());
		assertTrue(checkProperSplit(splittedSeasons));

	}


	@Test
	public void currentSeasonDoesnotOverLapWithPreviousSeason() {
		addNewSeason().withHotelMSG(hotelMSG).startDate(_1Jan2018).endDate(_10Jan2018).andBar(bar_2).addToPreviousSeasonsList();
		addNewSeason().withHotelMSG(hotelMSG).startDate(_20Jan2018).endDate(_30Jan2018).andBar(bar_1).addToNewSeasonsList();
		Set<Season> splittedSeasons = seasonSplitter.splitSeason(previouslySplittedSeasons , newSeasons);
		assertEquals(2, splittedSeasons.size());
		assertTrue(checkProperSplit(splittedSeasons));

	}

	@Test
	public void currentSeasonOverLapsAtStartWithPreviousSeason() {
		addNewSeason().withHotelMSG(hotelMSG).startDate(_20Jan2018).endDate(_30Jan2018).andBar(bar_1).addToPreviousSeasonsList();
		addNewSeason().withHotelMSG(hotelMSG).startDate(_1Jan2018).endDate(_25Jan2018).andBar(bar_2).addToNewSeasonsList();

		Set<Season> splittedSeasons = seasonSplitter.splitSeason(previouslySplittedSeasons , newSeasons);
		assertEquals(2, splittedSeasons.size());
		assertTrue(checkProperSplit(splittedSeasons));

	}

	@Test
	public void currentSeasonOverLapsAtEndOfPreviousSeason() {
		addNewSeason().withHotelMSG(hotelMSG).startDate(_1Jan2018).endDate(_25Jan2018).andBar(bar_2).addToPreviousSeasonsList();
		addNewSeason().withHotelMSG(hotelMSG).startDate(_20Jan2018).endDate(_30Jan2018).andBar(bar_1).addToNewSeasonsList();

		Set<Season> splittedSeasons = seasonSplitter.splitSeason(previouslySplittedSeasons , newSeasons);
		assertEquals(2, splittedSeasons.size());
		assertTrue(checkProperSplit(splittedSeasons));

	}

	@Test
	public void currentSeasonCompletelyOverLapsPreviousSeason() {
		addNewSeason().withHotelMSG(hotelMSG).startDate(_10Jan2018).endDate(_20Jan2018).andBar(bar_2).addToPreviousSeasonsList();
		addNewSeason().withHotelMSG(hotelMSG).startDate(_10Jan2018).endDate(_30Jan2018).andBar(bar_1).addToNewSeasonsList();

		Set<Season> splittedSeasons = seasonSplitter.splitSeason(previouslySplittedSeasons , newSeasons);
		assertEquals(1, splittedSeasons.size());
		assertEquals(bar_1, splittedSeasons.iterator().next().getSeasonBAR());

		assertTrue(checkProperSplit(splittedSeasons));

	}

	@Test
	public void currentSeasonCompletelyOverLapsPreviousSeasonWithSameStartDateAndEndDate() {
		addNewSeason().withHotelMSG(hotelMSG).startDate(_10Jan2018).endDate(_20Jan2018).andBar(bar_2).addToPreviousSeasonsList();
		addNewSeason().withHotelMSG(hotelMSG).startDate(_10Jan2018).endDate(_20Jan2018).andBar(bar_1).addToNewSeasonsList();

		Set<Season> splittedSeasons = seasonSplitter.splitSeason(previouslySplittedSeasons , newSeasons);
		assertEquals(1, splittedSeasons.size());
		assertEquals(bar_1, splittedSeasons.iterator().next().getSeasonBAR());
		assertTrue(checkProperSplit(splittedSeasons));

	}

	@Test
	public void currentSeasonHasStartDateEqualToPreviousSeasonEndDate() {
		addNewSeason().withHotelMSG(hotelMSG).startDate(_10Jan2018).endDate(_20Jan2018).andBar(bar_2).addToPreviousSeasonsList();
		addNewSeason().withHotelMSG(hotelMSG).startDate(_20Jan2018).endDate(_30Jan2018).andBar(bar_1).addToNewSeasonsList();

		Set<Season> splittedSeasons = seasonSplitter.splitSeason(previouslySplittedSeasons , newSeasons);
		assertEquals(2, splittedSeasons.size());
		assertEquals(_20Jan2018.minusDays(1), splittedSeasons.iterator().next().getSeasonEndDate());

		assertTrue(checkProperSplit(splittedSeasons));

	}

	@Test
	public void currentSeasonHasEndDateEqualToPreviousSeasonStartDate() {
		addNewSeason().withHotelMSG(hotelMSG).startDate(_10Jan2018).endDate(_20Jan2018).andBar(bar_2).addToPreviousSeasonsList();
		addNewSeason().withHotelMSG(hotelMSG).startDate(_1Jan2018).endDate(_10Jan2018).andBar(bar_1).addToNewSeasonsList();

		Set<Season> splittedSeasons = seasonSplitter.splitSeason(previouslySplittedSeasons , newSeasons);
		assertEquals(2, splittedSeasons.size());
		Season lastSeasonOfHotelMSG = null;

		for(Season each:splittedSeasons){
			lastSeasonOfHotelMSG = each;
		}
		assertEquals(_10Jan2018.plusDays(1),lastSeasonOfHotelMSG.getSeasonStartDate() );

		assertTrue(checkProperSplit(splittedSeasons));

	}


	@Test
	public void currentSeasonPartiallyOverlaps2SeasonsAtBothEndsAndCompletelyOverlapsOneSeason() {
		addNewSeason().withHotelMSG(hotelMSG).startDate(_10Jan2018).endDate(_15Jan2018).andBar(bar_2).addToPreviousSeasonsList();
		addNewSeason().withHotelMSG(hotelMSG).startDate(_20Jan2018).endDate(_25Jan2018).andBar(bar_1).addToPreviousSeasonsList();
		addNewSeason().withHotelMSG(hotelMSG).startDate(_28Jan2018).endDate(_7Feb2018 ).andBar(bar_2).addToPreviousSeasonsList();

		addNewSeason().withHotelMSG(hotelMSG).startDate(_13Jan2018 ).endDate(_30Jan2018 ).andBar(bar_1).addToNewSeasonsList();


		Set<Season> splittedSeasons = seasonSplitter.splitSeason(previouslySplittedSeasons , newSeasons);
		assertEquals(3, splittedSeasons.size());
		assertTrue(checkProperSplit(splittedSeasons));

	}


	@Test
	public void _3CurrentSeasonsOverlapsPreviousSeasonAtStart_InBetween_AtEnd() {
		addNewSeason().withHotelMSG(hotelMSG).startDate(_13Jan2018 ).endDate(_30Jan2018 ).andBar(bar_1).addToPreviousSeasonsList();

		addNewSeason().withHotelMSG(hotelMSG).startDate(_10Jan2018).endDate(_15Jan2018).andBar(bar_2).addToNewSeasonsList();
		addNewSeason().withHotelMSG(hotelMSG).startDate(_20Jan2018).endDate(_25Jan2018).andBar(bar_1).addToNewSeasonsList();
		addNewSeason().withHotelMSG(hotelMSG).startDate(_28Jan2018).endDate(_7Feb2018 ).andBar(bar_2).addToNewSeasonsList();



		Set<Season> splittedSeasons = seasonSplitter.splitSeason(previouslySplittedSeasons , newSeasons);
		assertEquals(5, splittedSeasons.size());
		assertTrue(checkProperSplit(splittedSeasons));

	}



	private boolean checkProperSplit(final Set<Season> splittedSeasons) {
		Season previousSeason = null;
		for(Season currentSeason : splittedSeasons){
			if(previousSeason == null){
				previousSeason = currentSeason;
				continue;
			}

			if(previousSeason.getSeasonEndDate().isAfter(currentSeason.getSeasonStartDate())){
				return false;
			}

		}
		return true;
	}

	private void addToPreviousSeasonsList() {
		this.previouslySplittedSeasons.add(season);
	}

	private void addToNewSeasonsList() {
		this.newSeasons.add(season);
	}
	private SeasonSplitterTest andBar(final String bar) {
		this.season.setSeasonBAR(bar);
		return this;
	}
	private SeasonSplitterTest endDate(final LocalDate endDate) {
		this.season.setSeasonEndDate(endDate);
		return this;
	}
	private SeasonSplitterTest startDate(final LocalDate startDate) {
		this.season.setSeasonStartDate(startDate);
		return this;
	}
	private SeasonSplitterTest withHotelMSG(final HotelMSG hotelMSG) {
		this.season.setHotelMSG(hotelMSG);
		return this;
	}
	private SeasonSplitterTest addNewSeason() {
		this.season = new Season();
		return this;
	}


}
