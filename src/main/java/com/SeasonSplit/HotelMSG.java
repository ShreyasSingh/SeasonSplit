package com.SeasonSplit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hotelmsg")
public class HotelMSG implements Comparable<HotelMSG> {
	private Integer msgID;
	private String hotelID;
	private String MSGName;

	public HotelMSG() {
	}

	public HotelMSG(final Integer msgID, final String HotelID, final String MSGName) {
		this.msgID = msgID;
		this.hotelID = HotelID;
		this.MSGName = MSGName;
	}

	@Id
	@Column(name = "msgId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getMsgID() {
		return msgID;
	}

	public void setMsgID(final Integer msgID) {
		this.msgID = msgID;
	}

	@Column(name = "hotelId")
	public String getHotelID() {
		return hotelID;
	}

	public void setHotelID(final String hotelID) {
		this.hotelID = hotelID;
	}

	@Column(name = "msgName")
	public String getMSGName() {
		return MSGName;
	}

	public void setMSGName(final String MSGName) {
		this.MSGName = MSGName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (MSGName == null ? 0 : MSGName.hashCode());
		result = prime * result + (hotelID == null ? 0 : hotelID.hashCode());
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
		HotelMSG other = (HotelMSG) obj;
		if (MSGName == null) {
			if (other.MSGName != null)
				return false;
		} else if (!MSGName.equals(other.MSGName))
			return false;
		if (hotelID == null) {
			if (other.hotelID != null)
				return false;
		} else if (!hotelID.equals(other.hotelID))
			return false;

		return true;
	}

	@Override
	public int compareTo(final HotelMSG other) {
		if (this.hotelID.equals(other.hotelID) && this.MSGName.equals(other.MSGName))
			return 0;
		return -1;
	}

}
