package cn.com.ddhj.model;

/**
 * 
 * 类: TIndustrialPark <br>
 * 描述: 工业园 <br>
 * 作者: zhy<br>
 * 时间: 2016年10月4日 上午10:49:33
 */
public class TIndustrialPark extends BaseModel {

	private String city;

	private String name;

	private String address;

	private String lat;

	private String lng;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

}