package com.SeasonSplit;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "season")
public class Season implements Comparable<Season>{

	private Integer seasonId;
	private HotelMSG hotelMSG;
	private LocalDate seasonStartDate;
	private LocalDate seasonEndDate;
	private String seasonBAR;

	public Season() {}

	public Season(final Integer seasonId, final HotelMSG hotelMSG, final LocalDate seasonStartDate, final LocalDate seasonEndDate, final String seasonBAR) {

		this.seasonId = seasonId;
		this.hotelMSG = hotelMSG;
		this.seasonStartDate = seasonStartDate;
		this.seasonEndDate = seasonEndDate;
		this.seasonBAR = seasonBAR;
	}

	@Id
	@Column(name = "seasonId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(final Integer seasonId) {
		this.seasonId = seasonId;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="msgId")
	public HotelMSG getHotelMSG() {
		return hotelMSG;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (hotelMSG == null ? 0 : hotelMSG.hashCode());
		result = prime * result + (seasonBAR == null ? 0 : seasonBAR.hashCode());
		result = prime * result + (seasonEndDate == null ? 0 : seasonEndDate.hashCode());
		result = prime * result + (seasonId == null ? 0 : seasonId.hashCode());
		result = prime * result + (seasonStartDate == null ? 0 : seasonStartDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Season other = (Season) obj;
		if (hotelMSG == null) {
			if (other.hotelMSG != null)
				return false;
		} else if (!hotelMSG.equals(other.hotelMSG))
			return false;
		if (seasonBAR == null) {
			if (other.seasonBAR != null)
				return false;
		} else if (!seasonBAR.equals(other.seasonBAR))
			return false;
		if (seasonEndDate == null) {
			if (other.seasonEndDate != null)
				return false;
		} else if (!seasonEndDate.equals(other.seasonEndDate))
			return false;
		if (seasonId == null) {
			if (other.seasonId != null)
				return false;
		} else if (!seasonId.equals(other.seasonId))
			return false;
		if (seasonStartDate == null) {
			if (other.seasonStartDate != null)
				return false;
		} else if (!seasonStartDate.equals(other.seasonStartDate))
			return false;
		return true;
	}

	public void setHotelMSG(final HotelMSG hotelMSG) {
		this.hotelMSG = hotelMSG;
	}

	@Column(name = "seasonStartDate")
	public LocalDate getSeasonStartDate() {
		return seasonStartDate;
	}

	public void setSeasonStartDate(final LocalDate startDate) {
		this.seasonStartDate = startDate;
	}

	@Column(name = "seasonEndDate")
	public LocalDate getSeasonEndDate() {
		return seasonEndDate;
	}

	public void setSeasonEndDate(final LocalDate endDate) {
		this.seasonEndDate = endDate;
	}

	@Column(name = "bar")
	public String getSeasonBAR() {
		return seasonBAR;
	}

	public void setSeasonBAR(final String seasonBAR) {
		this.seasonBAR = seasonBAR;
	}

	@Override
	public int compareTo(final Season other) {
		return seasonStartDate.compareTo(other.seasonStartDate);
	}

}
