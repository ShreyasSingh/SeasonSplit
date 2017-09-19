package com.SeasonSplit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;



public class DBServiceTest {


	private final SessionFactory sessionFactory = Mockito.mock(SessionFactory.class);
	private final Session session =  Mockito.mock(Session.class);
	private final Transaction transaction = Mockito.mock(Transaction.class);
	@SuppressWarnings("unchecked")
	private final Query<Season> query = Mockito.mock(Query.class);
	private LocalDate _1Jan2018 = LocalDate.of(2018, 1, 1);
	private LocalDate _10Jan2018 = LocalDate.of(2018, 1, 10);
	private LocalDate _15Jan2018 = LocalDate.of(2018, 1, 15);
	private LocalDate _25Jan2018 = LocalDate.of(2018, 1, 25);
	private LocalDate _30Jan2018 = LocalDate.of(2018, 1, 30);
	private LocalDate _10Feb2018 = LocalDate.of(2018, 2, 10);
	List<LocalDate> startDatesOfDifferentSeasons = Arrays.asList(_1Jan2018, _15Jan2018, _30Jan2018);
	List<LocalDate> endDatesOfDifferentSeasons = Arrays.asList(_10Jan2018, _25Jan2018, _10Feb2018);
	private LocalDate startDate = LocalDate.of(2017, 12, 25);
	private LocalDate endDate = LocalDate.of(2018, 2, 5);
	private DBService dbService ;
	private List<Integer> HoteMSGIds = Arrays.asList(1, 2, 3, 4);
	private HotelMSG hotelMSG = new HotelMSG(1,  "HILTON1", "MSG1");
	private String seasonBAR = "1, 1, 1, 1, 1, 1, 1";
	private String FETCH_ALL_HOTELMSGS = "From HotelMSG";

	private static final String FETCH_ALL_SEASONS_FOR_PARTICULAR_HOTELMSGS_BETWEEN_START_AND_END_DATE = "Select s from Season s where "
			+ "s.hotelMSG.msgID in ( :hotelMSGIds )"
			+ " AND s.seasonStartDate  <= :endDate AND "
			+ " s.seasonEndDate  >= :startDate"
			+ " order by s.seasonStartDate ASC";
	private static final String DELETE_ALL_SEASONS_FOR_PARTICULAR_HOTELMSGS_BETWEEN_START_AND_END_DATE = "Delete from Season s where "
			+ "s.hotelMSG.msgID in ( :hotelMSGIds )"
			+ " AND s.seasonStartDate  <= :endDate AND "
			+ " s.seasonEndDate  >= :startDate";

	@Before
	public void setup() {
		dbService = new DBService(sessionFactory);
		Mockito.when(sessionFactory.openSession()).thenReturn(session);
		Mockito.when(session.beginTransaction()).thenReturn(transaction);
		Mockito.when(session.getTransaction()).thenReturn(transaction);

	}

	@After
	public void cleanUp(){
		Mockito.verify(transaction).commit();
		Mockito.verify(session).clear();
		Mockito.verify(session).close();

	}

	@Test
	public void fetchAllSeasonsforAllHotelMSGsBetweenStartDateAndEndDate() {
		Set<Season> seasonsForAHotelMSG = createSeasonsForAParticularHotelMSG(startDatesOfDifferentSeasons, endDatesOfDifferentSeasons);
		Mockito.when(session.createQuery(FETCH_ALL_SEASONS_FOR_PARTICULAR_HOTELMSGS_BETWEEN_START_AND_END_DATE)).thenReturn(query);
		Mockito.when(query.setParameter("hotelMSGIds",HoteMSGIds)).thenReturn(query);
		Mockito.when(query.setParameter("startDate",startDate )).thenReturn(query);
		Mockito.when(query.setParameter("endDate",endDate )).thenReturn(query);
		Mockito.when(query.getResultList()).thenReturn(new LinkedList<Season>(seasonsForAHotelMSG));

		Map<HotelMSG, List<Season>> seasonsPerHotelMSG = dbService.fetchAllSeasonsForGivenHotelMSGIDsBetweenStartDateAndEndDate(HoteMSGIds,startDate, endDate);
		Mockito.verify(query).getResultList();

	}

	@Test
	public void deleteAllSeasonsforAllHotelMSGsBetweenStartDateAndEndDate() {
		Mockito.when(session.createQuery(DELETE_ALL_SEASONS_FOR_PARTICULAR_HOTELMSGS_BETWEEN_START_AND_END_DATE)).thenReturn(query);
		Mockito.when(query.setParameter("hotelMSGIds",HoteMSGIds)).thenReturn(query);
		Mockito.when(query.setParameter("startDate",startDate )).thenReturn(query);
		Mockito.when(query.setParameter("endDate",endDate )).thenReturn(query);

		dbService.deleteAllSeasonsForGivenHotelMSGIDsBetweenStartDateAndEndDate(HoteMSGIds,startDate, endDate);
		Mockito.verify(query).executeUpdate();

	}

	@Test
	public void saveAllSeasonsforAHotelMSG() {
		Set<Season> seasonsForAHotelMSG = createSeasonsForAParticularHotelMSG(startDatesOfDifferentSeasons, endDatesOfDifferentSeasons);
		dbService.saveSeasonsForParicularHotelMSG(hotelMSG, new ArrayList<Season>(seasonsForAHotelMSG));
		Mockito.verify(session).merge(hotelMSG);
	}

	@Test
	public void fetchAllHotelMSGs() {
		Mockito.when(session.createQuery(FETCH_ALL_HOTELMSGS )).thenReturn(query);

		Map<Integer, HotelMSG> seasonsPerHotelMSG = dbService.fetchAllHotelMSGsOrderedByMSGIDs();
		Mockito.verify(query).getResultList();

	}

	private Set<Season> createSeasonsForAParticularHotelMSG(final List<LocalDate> startDatesOfDifferentSeasons,
			final List<LocalDate> endDatesOfDifferentSeasons) {

		Set<Season> seasons = new TreeSet<>();
		int i = 0;
		for( LocalDate startDate : startDatesOfDifferentSeasons){
			Season season = new Season();
			season.setHotelMSG(hotelMSG );
			season.setSeasonStartDate(startDate);
			season.setSeasonEndDate(endDatesOfDifferentSeasons.get(i)); i++;
			season.setSeasonBAR(seasonBAR );
			seasons.add(season);
		}

		return seasons;
	}
}
