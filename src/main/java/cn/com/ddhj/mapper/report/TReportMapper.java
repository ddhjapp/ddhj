package cn.com.ddhj.mapper.report;

import java.util.List;

import cn.com.ddhj.dto.BaseDto;
import cn.com.ddhj.mapper.BaseMapper;
import cn.com.ddhj.model.report.TReport;

/**
 * 
 * 类: TReportMapper <br>
 * 描述: 环境报告数据库访问接口 <br>
 * 作者: zhy<br>
 * 时间: 2016年10月3日 下午1:57:48
 */
public interface TReportMapper extends BaseMapper<TReport, BaseDto> {

	/**
	 * 
	 * 方法: findRreportByChart <br>
	 * 描述: 根据编码集合获取报告信息 <br>
	 * 作者: zhy<br>
	 * 时间: 2016年10月7日 下午4:25:04
	 * 
	 * @param list
	 * @return
	 */
	List<TReport> findRreportByChart(List<String> list);
}