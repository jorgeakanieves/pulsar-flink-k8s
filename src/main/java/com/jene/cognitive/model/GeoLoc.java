package com.jene.cognitive.model;

import java.io.Serializable;

/**
 * @uthor Jorge Nieves
 */

public class GeoLoc implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1011157571367550251L;

	private Double lat;
	private Double lon;

	public GeoLoc()
	{
	}
	
	public GeoLoc(String fullSt)
	{
		if ( fullSt != null )
		{
			int separator = fullSt.indexOf(',');
			String lat = fullSt.substring(0, separator);
			String lon = fullSt.substring(separator+1);
			
			this.lat = new Double (lat);
			this.lon = new Double (lon);
		}
	}
	
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}
	public void setLon(Double lon) {
		this.lon = lon;
	}

//	public String toString() {
//		return lat.toString() + "," + lon.toString();
//	}


	@Override
	public String toString() {
		return "GeoLoc{" +
				"lat=" + lat +
				", lon=" + lon +
				'}';
	}
}