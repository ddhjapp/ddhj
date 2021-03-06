package cn.com.ddhj.service.apporderpay.wechat;

import cn.com.ddhj.base.BaseClass;
import cn.com.ddhj.base.BaseResult;
import cn.com.ddhj.model.apppay.TPayInfo;
import cn.com.ddhj.model.map.MDataMap;

/**
 * 微信支付(只存在去支付方法)
 * 
 * @author wz
 * 
 */
public class WechatProcessRequest extends BaseClass {
	/**
	 * 微信都写在   newCenter中
	 */
	
	
	/**
	 * 微信支付接口
	 * 
	 * @param orderCode
	 * @param ip
	 * @param rootResult
	 * @return
	 */
//	public MDataMap wechatMove(String orderCode, String ip,
//			BaseResult rootResult) {
//		MDataMap mDataMap = new MDataMap();
//		try {
//			ApiWechatProcessService apiWechatProcessService = new ApiWechatProcessService();
//			mDataMap = apiWechatProcessService.wechatMovePayment(orderCode, ip, rootResult);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return mDataMap;
//	}
	

	/**
	 * 微信支付最新
	 * @param orderCode
	 * @param ip
	 * @param rootResult
	 * @return
	 */
	public MDataMap wechatMoveNew(String orderPayCode, TPayInfo payInfo, String ip, BaseResult rootResult) {
		MDataMap mDataMap = new MDataMap();
		try {
			ApiWechatProcessService apiWechatProcessService = new ApiWechatProcessService();
			mDataMap = apiWechatProcessService.wechatMovePaymentNew(orderPayCode, payInfo, ip, rootResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mDataMap;
	}
	
//
//	/**
//	 * 微信支付回调方法
//	 * 
//	 * @return
//	 */
//	public PayResult responseWechatMove() {
//		System.out
//				.println("=============weixinhuidiao    jinru================");
//		
//		AlipayOrderShoppingService orderShoppingService = new AlipayOrderShoppingService();
//		PayResult payResult = new PayResult();
//		HttpServletRequest request = WebSessionHelper.create().upHttpRequest();
//		ApiWechatProcessService apiWechatProcessService = new ApiWechatProcessService();
//		payResult = apiWechatProcessService.responseWechatMoveService(request);
//		
//		if (payResult.upFlagTrue()) {
//			orderShoppingService.deletefamilySkuToShopCart(payResult
//					.getOrderCode()); // 根据订单号删除本订单对应的购物车中的商品 并且 同步支付数据
//		}
//		return payResult;
//	}
//
//	public static void main(String[] args) {
//		WechatProcess wechatProcess = new WechatProcess();
//		RootResult rootResult = new RootResult();
//		wechatProcess.wechatMove("HH20061444", "192.168.1.140", rootResult); // HH20119862
//	}
}
