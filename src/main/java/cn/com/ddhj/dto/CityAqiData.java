package cn.com.ddhj.dto;

public class CityAqiData {
	private String date;
	private String city;
	private String AQI;
	private String quality;
	
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAQI() {
		return AQI;
	}
	public void setAQI(String aQI) {
		AQI = aQI;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
}
