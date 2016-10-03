package cn.com.ddhj;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;

import cn.com.ddhj.base.BaseTest;
import cn.com.ddhj.model.TLandedProperty;
import cn.com.ddhj.service.ITLandedPropertyService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring.xml", "classpath:spring/spring-mybatis.xml" })
public class TLandedPropertyTest extends BaseTest {

	@Autowired
	private ITLandedPropertyService service;

	public void insert() {
		service.insertDataFromAPI();
	}

	public void select() {
		TLandedProperty model = service.selectByCode("LP161003100031");
		System.out.println(JSONObject.toJSON(model));
	}
	
	@Test
	public void ii(){
		Double afforest = Double.valueOf("33.12%".substring(0,"33.12%".indexOf("%")));
		System.out.println(afforest);
	}
}
