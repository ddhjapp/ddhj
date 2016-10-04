package cn.com.ddhj.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.ddhj.dto.CityAqi;
import cn.com.ddhj.dto.CityAqiData;
import cn.com.ddhj.result.estateInfo.EData;
import cn.com.ddhj.service.ICityAirService;
import cn.com.ddhj.service.IEstateInfoService;
import cn.com.ddhj.service.IWaterQualityService;
import cn.com.ddhj.service.IWeatherAreaSupportService;
import cn.com.ddhj.util.PureNetUtil;


/**
 * @descriptions 环境信息相关接口 
 *
 * @date 2016年10月4日 下午5:13:23
 * @author Yangcl 
 * @version 1.0.1
 */
@Controller
@RequestMapping("env")
public class EnvironmentController {
	
	private static Logger logger=Logger.getLogger(EnvironmentController.class);
	
	@Autowired
	private IWeatherAreaSupportService weatherService;  // aqi相关信息
	
	/**
	 * 生态状况:绿化率指数 	0.5或1  【地产检索接口->"greeningRate":"50%"】
	 * 生态状况:容积率指数 	0~9之间 【地产检索接口->"volumeRate":"0.46"】
	 */
	@Autowired
	private IEstateInfoService estateService;  // 地产信息相关接口 
	
	@Autowired
	private IWaterQualityService waterService; // 水环境:水质分类
	
	@Autowired
	private ICityAirService cityAirService;
	
	/**
	 * @descriptions 环境综合评分接口|1032
	 *
	 * @param position|经纬度逗号分隔
	 * @param title | 地产名称
	 * @return
	 * @date 2016年10月4日 下午5:30:13
	 * @author Yangcl 
	 * @version 1.0.0.1
	 */
	@RequestMapping(value = "1032", produces = { "application/json;charset=utf-8" })
	@ResponseBody
	public JSONObject envScore(String position , String title , String city){
		String[] arr = position.split(",");
		String lng = arr[0];
		String lat = arr[1];
		
		JSONObject result = new JSONObject();
		
		String greeningRate = "1";  // 如下条件不满足则用默认值
		String volumeRate = "1";	   // 如下条件不满足则用默认值
		JSONObject estate = this.estateList(position, "1"); // 获取楼盘信息
		if(estate.getString("code").equals("1")) {
			List<EData> estateList = JSONArray.parseArray(estate.getString("list"), EData.class);
			if(estateList != null && estateList.size() > 0){
				for(EData e : estateList){
					if(e.getTitle().equals(title)){
						if(e.getGreeningRate().equals("50%")){
							greeningRate = "0.5"; 
						}
						volumeRate = e.getVolumeRate();			
					}
				}
			}
		} 
		CityAqi aqi = cityAirService.getCityAqi(city);
		String hourAqi = aqi.getEntity().getAQI();
		String dayAqi = "";
		for(CityAqiData d : aqi.getList()){
			dayAqi += d.getAQI() + ",";
		}
		dayAqi = dayAqi.substring(0 , dayAqi.length()-1);
		
		result.put("score", this.getDoctorScore(hourAqi, hourAqi, greeningRate, volumeRate)); 
		result.put("AQIList", this.initAqiList(aqi.getList()));  
		
		JSONObject weather = cityAirService.getWeatherInfo(city);
		
		List<EnvInfo> envList = new ArrayList<>();
		EnvInfo air = new EnvInfo();
		air.setName("空气");
		air.setMemo(aqi.getEntity().getAQI());
		air.setLevel(aqi.getEntity().getQuality()); 
		envList.add(air);
		EnvInfo wea = new EnvInfo();
		wea.setName("天气");
		wea.setMemo(weather.getString("info"));
		wea.setLevel(weather.getString("quality")); 
		envList.add(wea);
		result.put("detailList", envList);  // 环境明细
		
		
		
		result.put("tiptitle", weather.getString("des"));  // 提示标题
		return  result;
	}
	
	
	
	/**
	 * @descriptions 楼盘列表|检索该经纬度附近10Km内的楼盘信息|1033
	 * 								 城市名称|北京，上海，广州，深圳
	 *
	 * @param position|经纬度逗号分隔
	 * @param page | 当前第几页，每页数据20条
	 * @return
	 * @date 2016年10月4日 下午5:30:13
	 * @author Yangcl 
	 * @version 1.0.0.1
	 */
	@RequestMapping(value = "1033", produces = { "application/json;charset=utf-8" })
	@ResponseBody
	public JSONObject estateList(String position  , String page){
		String[] arr = position.split(",");
		String lng = arr[0];
		String lat = arr[1];
		return  estateService.estateInfoList(lng, lat, page); 
	}
	
	
	/**
	 * @descriptions 获取教授数学模型综合评分|噪音和水质暂时默认为2
	 *
	 * @param a hourAQI
	 * @param b dayAQI   @教授的接口文档有问题，暂时放一个参数
	 * @param c  l  生态状况:绿化率指数 0.5或1  【地产检索接口->"greeningRate":"50%"】
	 * @param d  j 生态状况:容积率指数  0~9之间 【地产检索接口->"volumeRate":"0.46"】
	 * @return
	 * @date 2016年10月4日 下午10:18:29
	 * @author Yangcl 
	 * @version 1.0.0.1
	 */
	private String getDoctorScore(String a ,String b ,String c , String d){
		String url = "http://123.56.169.49:8338/environment/servlet/environmentZHInterface";
		JSONObject obj = null;
		Map<String, String> param = new HashMap<String, String>();
		param.put("hourAQI", a);		 
		param.put("dayAQI", b);	 
		param.put("s", "2");						 
		param.put("z1", "2");							 
		param.put("z2", "2");							 
		param.put("l", c);	
		param.put("j", d);	
		param.put("t", "2");
		
		String result = PureNetUtil.get(url, param);
		if (result != null && !"".equals(result)) {
			obj = JSONObject.parseObject(result); 
			if(obj.getString("flag").equals("true")){
				return obj.getString("message");
			}
		}
		return "0";
	}
	
	
	/**
	 * @descriptions 重组日期
	 *
	 * @param date 2014-05-08
	 * @date 2016年10月4日 下午10:43:34
	 * @author Yangcl 
	 * @version 1.0.0.1
	 */
	private String showWeekday(String date) {
		Date d = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String current = formatter.format(d);
		if(date.equals(current)){
			return "今日@" + this.formateDay(date); 
		}
		
		String[] arr = date.split("-");
		Calendar temp = Calendar.getInstance();
		temp.set(Integer.valueOf(arr[0]) , Integer.valueOf(arr[1]) - 1, Integer.valueOf(arr[2]));
		int x = temp.get(Calendar.DAY_OF_WEEK);
		String str = "";
		switch (x){
			case Calendar.SUNDAY:
				str = "周日";
				break;
			case Calendar.MONDAY:
				str = "周一";
				break;
			case Calendar.TUESDAY:
				str = "周二";
				break;
			case Calendar.WEDNESDAY:
				str = "周三";
				break;
			case Calendar.THURSDAY:
				str = "周四";
				break;
			case Calendar.FRIDAY:
				str = "周五";
				break;
			case Calendar.SATURDAY:
				str = "周六";
				break; 
		}
		
		return str + "@" + this.formateDay(date);
	}
	
	/**
	 * @descriptions 格式化月份日期为：8.29
	 *
	 * @param date
	 * @return
	 * @date 2016年10月4日 下午10:57:18
	 * @author Yangcl 
	 * @version 1.0.0.1
	 */
	private String formateDay(String date){
		String[] arr = date.split("-");
		String month = arr[1];
		String day = arr[2];
		if(StringUtils.startsWith(month, "0")){
			month = month.substring(1, 2);
		}
		if(StringUtils.startsWith(day, "0")){
			day = day.substring(1, 2);
		}
		
		return month + "." + day;
	}
	
	
	/**
	 * @descriptions 重组aqi显示信息
	 *
	 * @param list
	 * @return
	 * @date 2016年10月4日 下午11:18:42
	 * @author Yangcl 
	 * @version 1.0.0.1
	 */
	private List<AqiInfo> initAqiList(List<CityAqiData> list){
		List<AqiInfo> list_ = new ArrayList<>();
		for(CityAqiData d : list ){
			String[] arr = this.showWeekday(d.getDate()).split("@");
			AqiInfo i = new AqiInfo();
			i.setWeek(arr[0]);
			i.setDate(arr[1]);
			i.setLevel(d.getQuality());
			list_.add(i);
		}
		return list_;
	}
	
	
	
	// 收录此代码
	public String showWeekday2(String date) {
		String[] arr = date.split("-");
		Calendar temp = Calendar.getInstance();
		temp.set(Integer.valueOf(arr[0]) , Integer.valueOf(arr[1]) - 1, Integer.valueOf(arr[2]));
		int x = temp.get(Calendar.DAY_OF_WEEK);
		String str = "";
		switch (x){
			case Calendar.SUNDAY:
				str = "星期日";
				break;
			case Calendar.MONDAY:
				str = "星期一";
				break;
			case Calendar.TUESDAY:
				str = "星期二";
				break;
			case Calendar.WEDNESDAY:
				str = "星期三";
				break;
			case Calendar.THURSDAY:
				str = "星期四";
				break;
			case Calendar.FRIDAY:
				str = "星期五";
				break;
			case Calendar.SATURDAY:
				str = "星期六";
				break; 
		}
		
		return str;
	}
}


class AqiInfo{
	private String week ;
	private String date;
	private String level;
	
	
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
}


/**
 * @descriptions 环境描述明细
 *
 * @date 2016年10月4日 下午11:31:40
 * @author Yangcl 
 * @version 1.0.1
 */
class EnvInfo{
	private String name;
	private String level;
	private String memo;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
}
























