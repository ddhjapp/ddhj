package cn.com.ddhj.mapper;

import java.util.List;

import cn.com.ddhj.dto.BaseDto;
import cn.com.ddhj.dto.landedProperty.LandLatLngDto;
import cn.com.ddhj.model.TLandedProperty;
import cn.com.ddhj.result.estateInfo.EData;

/**
 * 
 * 类: TLandedPropertyMapper <br>
 * 描述: 地产楼盘列表数据库访问接口 <br>
 * 作者: zhy<br>
 * 时间: 2016年10月3日 下午5:20:21
 */
public interface TLandedPropertyMapper extends BaseMapper<TLandedProperty, BaseDto> {

	/**
	 * 
	 * 方法: batchInsertTLandedProperty <br>
	 * 描述: 从聚合数据导入楼盘信息 <br>
	 * 作者: zhy<br>
	 * 时间: 2016年10月3日 下午6:23:35
	 * 
	 * @param list
	 * @return
	 */
	int batchInsertTLandedProperty(List<TLandedProperty> list);
	
	/**
	 * @descriptions 根据楼盘名称寻找楼盘在数据库里的编号
	 *
	 * @param name
	 * @return
	 * @date 2016年10月6日 下午4:44:17
	 * @author Yangcl 
	 * @version 1.0.0.1
	 */
	public List<TLandedProperty>  findCodeByTitle(String name); 
	
	
	public List<EData> findLandedPropertyAll(LandLatLngDto dto);
}































