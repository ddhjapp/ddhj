package cn.com.ddhj.service.impl;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.ddhj.service.ICityAirService;
import cn.com.ddhj.util.PureNetUtil;

/**
 * 
 * 类: CityAirServiceImpl <br>
 * 描述: 城市空气质量相关接口 <br>
 * 作者: zhy<br>
 * 时间: 2016年10月1日 下午8:59:15
 */
@Service
public class CityAirServiceImpl implements ICityAirService {

	private static final String KEY = "2c5b736e4a1674265ae4a83cb864f64d";

	/**
	 * 
	 * 方法: getAirCityData <br>
	 * 描述: TODO
	 * 
	 * @return
	 * @see cn.com.ddhj.service.ICityAirService#getAirCityData()
	 */
	@Override
	public JSONArray getAirCityData() {
		String url = "http://web.juhe.cn:8080/environment/air/airCities";
		JSONObject obj = null;
		JSONArray array = new JSONArray();
		Map<String, String> param = new HashMap<String, String>();
		param.put("key", KEY);
		String result = PureNetUtil.post(url, param);
		if (result != null && !"".equals(result)) {
			obj = JSONObject.parseObject(result);
			// 如果状态=200时，获取城市列表
			if (obj.getInteger("resultCode") == 200) {
				array = JSONArray.parseArray(obj.getString("result"));
			}
		}
		return array;
	}

	/**
	 * 
	 * 方法: getCityAir <br>
	 * 描述: TODO
	 * 
	 * @param cityName
	 * @return
	 * @see cn.com.ddhj.service.ICityAirService#getCityAir(java.lang.String)
	 */
	@Override
	public JSONObject getCityAir(String cityName) {
		String url = "http://web.juhe.cn:8080/environment/air/cityair";
		JSONObject obj = null;
		try {
			Map<String, String> param = new HashMap<String, String>();
			param.put("key", KEY);
			String city = URLEncoder.encode(cityName, "UTF-8");
			param.put("city", city);
			String result = PureNetUtil.post(url, param);
			if (result != null && !"".equals(result)) {
				obj = JSONObject.parseObject(result);
			} else {
				obj = new JSONObject();
				obj.put("code", "-1");
				obj.put("message", "查询数据为空");
			}
		} catch (Exception e) {
			e.printStackTrace();
			obj = new JSONObject();
			obj.put("code", "-1");
			obj.put("message", "查询数据错误");
		}
		return obj;
	}

	/**
	 * 
	 * 方法: getPMCityData <br>
	 * 描述: TODO
	 * 
	 * @return
	 * @see cn.com.ddhj.service.ICityAirService#getPMCityData()
	 */
	@Override
	public JSONArray getPMCityData() {
		String url = "http://web.juhe.cn:8080/environment/air/airCities";
		JSONArray array = new JSONArray();
		JSONObject obj = null;
		Map<String, String> param = new HashMap<String, String>();
		param.put("key", KEY);
		String result = PureNetUtil.post(url, param);
		if (result != null && !"".equals(result)) {
			obj = JSONObject.parseObject(result);
			if (obj.getInteger("resultCode") == 200) {
				array = JSONArray.parseArray(obj.getString("result"));
			}
		}
		return array;
	}

	/**
	 * 
	 * 方法: getCityPM <br>
	 * 描述: TODO
	 * 
	 * @param cityName
	 * @return
	 * @see cn.com.ddhj.service.ICityAirService#getCityPM(java.lang.String)
	 */
	@Override
	public JSONObject getCityPM(String cityName) {
		String url = "http://web.juhe.cn:8080/environment/air/pm";
		JSONObject obj = null;
		if (isPMCity(cityName)) {
			Map<String, String> param = new HashMap<String, String>();
			param.put("key", KEY);
			param.put("city", cityName);
			String result = PureNetUtil.post(url, param);
			if (result != null && !"".equals(result)) {
				obj = JSONObject.parseObject(result);
			} else {
				obj = new JSONObject();
				obj.put("code", "-1");
				obj.put("message", "查询数据为空");
			}
		} else {
			obj = new JSONObject();
			obj.put("code", "-1");
			obj.put("message", "查询城市无法查询pm2.5信息");
		}

		return obj;
	}

	/**
	 * 
	 * 方法: isAirCity <br>
	 * 描述: 判断查询城市是否在空气质量查询城市列表 <br>
	 * 作者: zhy<br>
	 * 时间: 2016年10月1日 下午9:20:29
	 * 
	 * @param cityName
	 * @return
	 */
	public boolean isAirCity(String cityName) {
		boolean flag = false;
		JSONArray array = getAirCityData();
		if (array != null && array.size() > 0) {
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj = array.getJSONObject(i);
				if (cityName.equals(obj.getString("name"))) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * 
	 * 方法: isPMCity <br>
	 * 描述: 判断查询城市是否在PM查询城市列表 <br>
	 * 作者: zhy<br>
	 * 时间: 2016年10月1日 下午9:25:06
	 * 
	 * @param cityName
	 * @return
	 */
	public boolean isPMCity(String cityName) {
		boolean flag = false;
		JSONArray array = getPMCityData();
		if (array != null && array.size() > 0) {
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj = array.getJSONObject(i);
				if (cityName.equals(obj.getString("name"))) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * 
	 * 方法: getAQILevel <br>
	 * 描述: TODO
	 * 
	 * @return
	 * @see cn.com.ddhj.service.ICityAirService#getAQILevel()
	 */
	@Override
	public Integer getAQILevel(String cityName) {
		Integer level = 1;
		JSONObject air = this.getCityAir(cityName);
		if (air != null) {
			JSONArray result = JSONArray.parseArray(air.getString("result"));
			JSONObject citynow = JSONObject.parseObject(result.getJSONObject(0).getString("citynow"));
			Integer aqi = citynow.getInteger("AQI");
			if (aqi >= 50 && aqi <= 100) {
				level = 2;
			} else if (aqi >= 101 && aqi <= 150) {
				level = 3;
			} else if (aqi >= 151 && aqi <= 200) {
				level = 4;
			} else if (aqi >= 201 && aqi <= 300) {
				level = 5;
			} else if (aqi > 301) {
				level = 6;
			}
		}
		return level;
	}
}
