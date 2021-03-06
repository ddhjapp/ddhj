package cn.com.ddhj.service.apporderpay.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.com.ddhj.base.BaseClass;
import cn.com.ddhj.helper.PropHelper;
import cn.com.ddhj.model.map.MDataMap;
import cn.com.ddhj.service.apporderpay.sign.MD5;

public class PayUtil extends BaseClass{
	
	
	public String getWechatOpenID(MDataMap map){
		String sign = "";
		
		List<String> list = new ArrayList<String>();
//		list.add("merchantid="+map.get("merchantid"));
//		list.add("tradetype="+map.get("tradetype"));
//		list.add("orderno="+map.get("orderno"));
//		list.add("tradetime="+map.get("tradetime"));
//		list.add("TradeCode="+map.get("tradeCode"));
//		list.add("channelid="+map.get("channelid"));
//		list.add("CallBackURL="+map.get("callBackURL"));
		list.add(map.get("merchantid"));
		list.add(map.get("tradetype"));
		list.add(map.get("orderno"));
		list.add(map.get("tradetime"));
		list.add(map.get("TradeCode"));
		list.add(map.get("channelid"));
		list.add(map.get("CallBackURL"));
		
		Collections.sort(list); // 对List内容进行排序
		
		StringBuffer sb = new StringBuffer();
		for(String str : list){
			sb.append(str);
		}
		if(sb!=null && !"".equals(sb)){
			sb.append(PropHelper.getValue("KEY_NET"));       
			sign =MD5.sign(sb.toString(), "UTF8");
		}
		return sign;
	}
	
	
	public static void main(String[] args) {
		MDataMap map = new MDataMap();
		map.put("merchantid", "13");
		map.put("tradetype", "oauth");
		map.put("orderno", "b");
		map.put("tradetime", "20150817182200");
		map.put("TradeCode", "WeiXin");
		map.put("channelid", "5");
		map.put("CallBackURL", "http://api-family.syapi.ichsy.com/cfamily/wechatWAPNet?");
		
		PayUtil pay = new PayUtil();
		pay.getWechatOpenID(map);
		
	}
}
