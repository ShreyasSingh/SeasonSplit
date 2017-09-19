package com.SeasonSplit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

public class SeasonTest {

	private HotelMSG hotelMSG_1 = new HotelMSG(1, "TAJ1", "MSG1");
	private HotelMSG hotelMSG_2 = new HotelMSG(2, "TAJ1", "MSG2");;
	private HotelMSG hotelMSG_3  = new HotelMSG(1, "TAJ1", "MSG1");
	private LocalDate _1Jan2018 = LocalDate.of(2018, 1, 1);
	private LocalDate _10Jan2018 = LocalDate.of(2018, 1, 10);
	private LocalDate _15Jan2018 = LocalDate.of(2018, 1, 15);
	private LocalDate _25Jan2018 = LocalDate.of(2018, 1, 25);
	private String seasonBAR = "1, 1, 1, 1, 1, 1, 1";

	@Test
	public void seasonShouldNotBeNull() {
		Season season = new Season();
		boolean comparisonResult = season.equals(null);

		assertFalse(comparisonResult);
	}

	@Test
	public void seasonShouldBeEqualToItself() {
		Season season = new Season();
		boolean comparisonResult = season.equals(season);

		assertTrue(comparisonResult);
	}

	@Test
	public void twoSeasonWithDifferentAttributeValuesAreNotEqual(){
		Season season1 = new Season(1, hotelMSG_1 , _1Jan2018, _10Jan2018, seasonBAR);
		Season season2 = new Season(1, hotelMSG_2 , _15Jan2018, _25Jan2018, seasonBAR);
		assertFalse(season1.equals(season2));

	}

	@Test
	public void twoSeasonsWithSameAttributeValuesAreNotEqual(){
		Season season1 = new Season(1, hotelMSG_1 , _1Jan2018, _10Jan2018, seasonBAR);
		Season season2 = new Season(1, hotelMSG_3 , _1Jan2018, _10Jan2018, seasonBAR);
		assertTrue(season1.equals(season2));

	}


}
