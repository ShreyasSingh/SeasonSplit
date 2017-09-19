package com.SeasonSplit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
//@Ignore
public class DAOTest {

	private static final String HOTELID_1 = "HILTON3";
	private static final String FIND_ALL_SEASONS_FOR_PARTICULAR_HOTELMSG = "From Season where msgID = :hotelMsgId";
	private static final String FETCH_ALL_SEASONS_FOR_HOTELMSGID_BETWEEN_START_AND_END_DATE = "Select s From Season s where "
			+ "s.hotelMSG.msgID in ( :hotelMSGIds )"
			+ " AND s.seasonStartDate  <= :endDate AND "
			+ " s.seasonEndDate  >= :startDate"
			+ " order by s.seasonStartDate ASC";
	private static SessionFactory sessionFactory;
	private static Session session;
	private String seasonBAR = "1, 1, 1, 1, 1, 1, 1";

	private LocalDate _1Jan2018 = LocalDate.of(2018, 1, 1);
	private LocalDate _10Jan2018 = LocalDate.of(2018, 1, 10);
	private LocalDate _15Jan2018 = LocalDate.of(2018, 1, 15);
	private LocalDate _25Jan2018 = LocalDate.of(2018, 1, 25);
	private LocalDate _30Jan2018 = LocalDate.of(2018, 1, 30);
	private LocalDate _10Feb2018 = LocalDate.of(2018, 2, 10);
	List<LocalDate> startDatesOfDifferentSeasons = Arrays.asList(_1Jan2018, _15Jan2018, _30Jan2018);
	List<LocalDate> endDatesOfDifferentSeasons = Arrays.asList(_10Jan2018, _25Jan2018, _10Feb2018);
	private HotelMSG hotelMSG_1 = new HotelMSG(1, HOTELID_1, "MSG5");

	@BeforeClass
	public static void init() {
		sessionFactory = HibernateUtils.getSessionFactory();
	}


	@Before
	public void setup() {
		session = sessionFactory.openSession();
	}


	@After
	public void clean() {
		session.close();
	}

	@Test
	public void shouldCreateSessionFactory() {
		assertNotNull(sessionFactory);
	}

	@Test
	public void shouldSaveFetchAndDeleteAllSeasonsForParticularHotelMarketSegmentGroup() {

		Season season = new Season();
		season.setHotelMSG(hotelMSG_1);
		season.setSeasonStartDate(_1Jan2018);
		season.setSeasonEndDate(_30Jan2018);
		season.setSeasonBAR(seasonBAR);

		//save hotelmsg and season
		beginTransaction();

		Integer savedHotelMSGID = (Integer) session.save(hotelMSG_1);
		assertNotNull(savedHotelMSGID);
		Integer savedSeasonID = (Integer) session.save(season);
		assertNotNull(savedSeasonID);

		commitTransaction();

		//fetch season for hotel msg
		beginTransaction();
		Query query = session.createQuery(FIND_ALL_SEASONS_FOR_PARTICULAR_HOTELMSG);
		query.setParameter("hotelMsgId",savedHotelMSGID);
		@SuppressWarnings("rawtypes")
		List seasons = query.getResultList();
		assertNotNull(seasons);
		commitTransaction();

		//delete hotelmsg and season
		beginTransaction();
		Season savedSeason = session.find(Season.class, savedSeasonID);
		session.delete(savedSeason);

		HotelMSG savedHotelMSG = session.find(HotelMSG.class, savedHotelMSGID);
		assertNull(savedHotelMSG);

		commitTransaction();

	}

	private void commitTransaction() {
		session.flush();
		session.clear();
		session.getTransaction().commit();

	}


	private void beginTransaction() {
		session.beginTransaction();

	}


	/*@Test
	public void shouldSaveAndDeleteAllSeasonsForParticularHotelMarketSegmentGroup() {

		//create Season Entity
		Season season = new Season();
		season.setHotelMSG(hotelMSG_1);
		season.setSeasonStartDate(_1Jan2018);
		season.setSeasonEndDate(_30Jan2018);
		season.setSeasonBAR(seasonBAR);


		saveSeasonsForParicularHotelMSG();
		executeQueryWith(DELETE_ALL_SEASONS_FOR_PARTICULAR_HOTELMSG);
		//	deleteSeasonForParticularHotelMSG();

	}
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void fetchAllSeasonsforAllHotelMSGIDsBetweenStartDateAndEndDate() {

		Season season = new Season();
		season.setHotelMSG(hotelMSG_1);
		season.setSeasonStartDate(_1Jan2018);
		season.setSeasonEndDate(_30Jan2018);
		season.setSeasonBAR(seasonBAR);

		//save hotelmsg and season
		beginTransaction();

		Integer savedHotelMSGID = (Integer) session.save(hotelMSG_1);
		assertNotNull(savedHotelMSGID);
		Integer savedSeasonID = (Integer) session.save(season);
		assertNotNull(savedSeasonID);

		commitTransaction();

		beginTransaction();
		Query query = session.createQuery(FETCH_ALL_SEASONS_FOR_HOTELMSGID_BETWEEN_START_AND_END_DATE);
		query.setParameter("hotelMSGIds", savedHotelMSGID);
		query.setParameter("startDate",_1Jan2018);
		query.setParameter("endDate",_30Jan2018);
		Map<HotelMSG, List<Season>> seasonsPerHotelMSG = (Map<HotelMSG, List<Season>>) query.getResultList().parallelStream().collect(Collectors.groupingBy(Season::getHotelMSG));
		assertNotNull(seasonsPerHotelMSG);
		commitTransaction();

		beginTransaction();
		Season savedSeason = session.find(Season.class, savedSeasonID);
		session.delete(savedSeason);
		HotelMSG savedHotelMSG = session.find(HotelMSG.class, savedHotelMSGID);
		assertNull(savedHotelMSG);

		commitTransaction();
	}



}
