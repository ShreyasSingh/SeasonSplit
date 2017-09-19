package com.SeasonSplit;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class DBService {

	private final SessionFactory sessionFactory;
	private Session session;
	private static final String FETCH_ALL_SEASONS_FOR_PARTICULAR_HOTELMSGS_BETWEEN_START_AND_END_DATE = "Select s from Season s where "
			+ "s.hotelMSG.msgID in ( :hotelMSGIds )"
			+ " AND s.seasonStartDate  <= :endDate AND "
			+ " s.seasonEndDate  >= :startDate"
			+ " order by s.seasonStartDate ASC";
	private static final String DELETE_ALL_SEASONS_FOR_PARTICULAR_HOTELMSGS_BETWEEN_START_AND_END_DATE = "Delete from Season s where "
			+ "s.hotelMSG.msgID in ( :hotelMSGIds )"
			+ " AND s.seasonStartDate  <= :endDate AND "
			+ " s.seasonEndDate  >= :startDate";

	public DBService(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Map<HotelMSG, List<Season>> fetchAllSeasonsForGivenHotelMSGIDsBetweenStartDateAndEndDate(
			final List<Integer> hoteMSGIds, final LocalDate startDate, final LocalDate endDate) {
		startSession();
		Query query = session.createQuery(FETCH_ALL_SEASONS_FOR_PARTICULAR_HOTELMSGS_BETWEEN_START_AND_END_DATE);
		query.setParameter("hotelMSGIds",hoteMSGIds);
		query.setParameter("startDate",startDate);
		query.setParameter("endDate",endDate);
		Map<HotelMSG, List<Season>> seasonsPerHotelMSG = (Map<HotelMSG, List<Season>>) query.getResultList().parallelStream().collect(Collectors.groupingBy(Season::getHotelMSG));

		closeSession();
		return seasonsPerHotelMSG;
	}


	public void deleteAllSeasonsForGivenHotelMSGIDsBetweenStartDateAndEndDate(
			final List<Integer> hoteMSGIds, final LocalDate startDate, final LocalDate endDate) {
		startSession();
		Query query = session.createQuery(DELETE_ALL_SEASONS_FOR_PARTICULAR_HOTELMSGS_BETWEEN_START_AND_END_DATE);
		query.setParameter("hotelMSGIds",hoteMSGIds);
		query.setParameter("startDate",startDate);
		query.setParameter("endDate",endDate);
		query.executeUpdate();
		closeSession();
	}

	public void saveSeasonsForParicularHotelMSG(final HotelMSG hotelMSG, final List<Season> seasons) {

		startSession();
		save(hotelMSG);
		saveSeasonsForAHotelMSG(seasons);
		closeSession();
	}


	public Map<Integer, HotelMSG> fetchAllHotelMSGsOrderedByMSGIDs() {
		startSession();
		Query query = session.createQuery("From HotelMSG");
		Map<Integer, HotelMSG> hotelMSGsOrderedByIDs = (Map<Integer, HotelMSG>) query.getResultList().parallelStream().collect(Collectors.toMap(HotelMSG ::getMsgID , HotelMSG -> HotelMSG));
		closeSession();
		return hotelMSGsOrderedByIDs;
	}

	private void saveSeasonsForAHotelMSG(final List<Season> seasonsForAHotelMSG) {
		for(Season each : seasonsForAHotelMSG){
			Season mergedSeason = (Season) session.merge(each);
			session.update(mergedSeason);
		}
		session.flush();
	}

	private void save(final HotelMSG hotelMSG) {
		HotelMSG mergedHotelMSG = (HotelMSG) session.merge(hotelMSG);
		session.update(mergedHotelMSG);
	}
	private void startSession() {
		session = sessionFactory.openSession();
		session.beginTransaction();
	}

	private void closeSession() {
		session.getTransaction().commit();
		session.clear();
		session.close();
	}


}
