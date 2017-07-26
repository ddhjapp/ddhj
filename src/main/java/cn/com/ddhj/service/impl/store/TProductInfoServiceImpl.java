package cn.com.ddhj.service.impl.store;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.com.ddhj.dto.store.TProductInfoDto;
import cn.com.ddhj.mapper.TProductInfoMapper;
import cn.com.ddhj.model.TProductInfo;
import cn.com.ddhj.result.PageResult;
import cn.com.ddhj.service.impl.BaseServiceImpl;
import cn.com.ddhj.service.store.ITProductInfoService;

@Service
public class TProductInfoServiceImpl extends BaseServiceImpl<TProductInfo, TProductInfoMapper, TProductInfoDto>
		implements ITProductInfoService {
	@Autowired
	private TProductInfoMapper mapper;

	public PageResult findDataPage(TProductInfoDto dto) {
		PageResult result = new PageResult();
		PageHelper.startPage(dto.getPageIndex(), dto.getPageSize());
		List<TProductInfo> list = mapper.findEntityAll(dto);
		if (list != null && list.size() > 0) {
			result.setResultCode(0);
		} else {
			list = new ArrayList<TProductInfo>();
			result.setResultCode(-1);
			result.setResultMessage("查询商品列表为空");
		}
		PageInfo<TProductInfo> page = new PageInfo<TProductInfo>(list);
		result.setPage(page);
		return result;
	}

}
