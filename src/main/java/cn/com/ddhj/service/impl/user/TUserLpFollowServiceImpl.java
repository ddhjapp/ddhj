package cn.com.ddhj.service.impl.user;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.ddhj.base.BaseResult;
import cn.com.ddhj.dto.TLandedPropertyDto;
import cn.com.ddhj.dto.user.TUserLpFollowDto;
import cn.com.ddhj.mapper.TLandedPropertyMapper;
import cn.com.ddhj.mapper.user.TUserLoginMapper;
import cn.com.ddhj.mapper.user.TUserLpFollowMapper;
import cn.com.ddhj.mapper.user.TUserMapper;
import cn.com.ddhj.model.TLandedProperty;
import cn.com.ddhj.model.user.TUser;
import cn.com.ddhj.model.user.TUserLogin;
import cn.com.ddhj.model.user.TUserLpFollow;
import cn.com.ddhj.result.tuser.FollowResult;
import cn.com.ddhj.service.impl.BaseServiceImpl;
import cn.com.ddhj.service.user.ITUserLpFollowService;
import cn.com.ddhj.util.CommonUtil;
import cn.com.ddhj.util.DateUtil;

/**
 * 
 * 类: TUserLpFollowServiceImpl <br>
 * 描述: 用户楼盘关注表业务逻辑处理接口实现类 <br>
 * 作者: zhy<br>
 * 时间: 2016年10月11日 下午3:00:18
 */
@Service
public class TUserLpFollowServiceImpl extends BaseServiceImpl<TUserLpFollow, TUserLpFollowMapper, TUserLpFollowDto>
		implements ITUserLpFollowService {

	@Autowired
	private TUserLpFollowMapper mapper;
	@Autowired
	private TUserLoginMapper loginMapper;
	@Autowired
	private TUserMapper userMapper;
	@Autowired
	private TLandedPropertyMapper lpMapper;

	/**
	 * 
	 * 方法: delFollow <br>
	 * 
	 * @param dto
	 * @return
	 * @see cn.com.ddhj.service.user.ITUserLpFollowService#delFollow(cn.com.ddhj.dto.user.TUserLpFollowDto)
	 */
	@Override
	public BaseResult delFollow(TUserLpFollowDto dto, String userToken) {
		BaseResult result = new BaseResult();
		TUserLogin login = loginMapper.findLoginByUuid(userToken);
		if (login != null) {
			TUser user = userMapper.findTUserByUuid(login.getUserToken());
			if (StringUtils.isNotBlank(dto.getIdList())) {
				String[] codes = dto.getIdList().split(",");
				List<String> list = Arrays.asList(codes);
				int flag = mapper.deleteByLpCode(list);
				if (flag >= 0) {
					result.setResultCode(0);
					result.setResultMessage("删除成功");
				} else {
					result.setResultCode(-1);
					result.setResultMessage("删除失败");
				}
			} else {
				int flag = mapper.deleteByUserCode(user.getUserCode());
				if (flag >= 0) {
					result.setResultCode(0);
					result.setResultMessage("删除成功");
				} else {
					result.setResultCode(-1);
					result.setResultMessage("删除失败");
				}
			}
		} else {
			result.setResultCode(-1);
			result.setResultMessage("用户未登录");
		}
		return result;
	}

	/**
	 * 
	 * 方法: insert <br>
	 * 
	 * @param lpCode
	 * @param userToken
	 * @return
	 * @see cn.com.ddhj.service.user.ITUserLpFollowService#insert(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public BaseResult insert(String lpCode, String userToken) {
		BaseResult result = new BaseResult();
		TUserLogin login = loginMapper.findLoginByUuid(userToken);
		if (login != null) {
			TUser user = userMapper.findTUserByUuid(login.getUserToken());
			TUserLpFollow entity = new TUserLpFollow();
			entity.setLpCode(lpCode);
			entity.setUserCode(user.getUserCode());
			entity.setCreateTime(DateUtil.getSysDateTime());
			int flag = mapper.insertSelective(entity);
			if (flag > 0) {
				result.setResultCode(0);
				result.setResultMessage("楼盘关注成功");
			} else {
				result.setResultCode(-1);
				result.setResultMessage("楼盘关注失败");
			}
		} else {
			result.setResultCode(-1);
			result.setResultMessage("用户未登录");
		}
		return result;
	}

	/**
	 * 
	 * 方法: findFollowLpData <br>
	 * 
	 * @param list
	 * @return
	 * @see cn.com.ddhj.service.user.ITUserLpFollowService#findFollowLpData(java.util.List)
	 */
	@Override
	public FollowResult findFollowLpData(TUserLpFollowDto dto, String userToken) {
		FollowResult result = new FollowResult();
		TUserLogin login = loginMapper.findLoginByUuid(userToken);
		if (login != null) {
			TUser user = userMapper.findTUserByUuid(login.getUserToken());
			if (user != null) {
				// 查询关注楼盘列表
				List<String> lpCodes = mapper.findLpCodeAll(user.getUserCode());
				if (lpCodes != null && lpCodes.size() > 0) {
					TLandedPropertyDto lpDto = new TLandedPropertyDto();
					lpDto.setCodes(lpCodes);
					lpDto.setStart(dto.getPageIndex() * dto.getPageSize());
					lpDto.setPageSize(dto.getPageSize());
					List<TLandedProperty> list = lpMapper.findLpForUser(lpDto);

					if (list != null && list.size() > 0) {
						for (TLandedProperty lp : list) {
							Double distance = CommonUtil.getDistanceFromLL(Double.parseDouble(dto.getLat()),
									Double.parseDouble(dto.getLng()), Double.parseDouble(lp.getLat()),
									Double.parseDouble(lp.getLng())) / 1000;
							lp.setDistance(distance.intValue());
						}
						result.setList(list);
						result.setTotal(lpMapper.findLpForUserCount(lpDto));
						result.setResultCode(0);
						result.setResultMessage("获取关注楼盘列表成功");
					} else {
						result.setResultCode(-1);
						result.setResultMessage("暂无关注楼盘");
					}
				} else {
					result.setResultCode(-1);
					result.setResultMessage("暂无关注楼盘");
				}
			} else {
				result.setResultCode(-1);
				result.setResultMessage("用户不存在");
			}
		} else {
			result.setResultCode(-1);
			result.setResultMessage("用户未登录");
		}
		return result;
	}
}
