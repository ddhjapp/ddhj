package cn.com.ddhj.service.impl.orderpay;

import java.util.Map;

import cn.com.ddhj.service.impl.orderpay.notify.PayGateNotifyPayProcess;
import cn.com.ddhj.service.impl.orderpay.prepare.AlipayPreparePayProcess;
import cn.com.ddhj.service.impl.orderpay.prepare.ApplePayPreparePayProcess;
import cn.com.ddhj.service.impl.orderpay.prepare.WechatPreparePayProcess;

/**
 * 支付模块调用支持类
 * @author zhaojunling
 */
public class PayServiceSupport {

	/**
	 * 支付宝付款（网关渠道）
	 * @param channel 支付渠道
	 * @param bigOrderCode 待支付大订单号
	 * @return
	 */
	public static AlipayPreparePayProcess.PaymentResult aliPayPrepare(PaymentChannel channel, String bigOrderCode){
		AlipayPreparePayProcess.PaymentInput input = new AlipayPreparePayProcess.PaymentInput();
		input.bigOrderCode = bigOrderCode;
		input.payChannel = channel;
		return PayServiceFactory.getInstance().getAlipayPreparePayProcess().process(input);
	}
	
	/**
	 * 微信付款（网关渠道）
	 * @param channel 支付渠道
	 * @param bigOrderCode 待支付大订单号
	 * @return
	 */
	public static WechatPreparePayProcess.PaymentResult wechatPrepare(PaymentChannel channel, String bigOrderCode){
		WechatPreparePayProcess.PaymentInput input = new WechatPreparePayProcess.PaymentInput();
		input.bigOrderCode = bigOrderCode;
		input.payChannel = channel;
		return PayServiceFactory.getInstance().getWechatPreparePayProcess().process(input);
	}
	
	/**
	 * 苹果支付
	 * @param bigOrderCode 待支付大订单号
	 * @return
	 */
	public static ApplePayPreparePayProcess.PaymentResult applePayPrepare(String bigOrderCode){
		ApplePayPreparePayProcess.PaymentInput input = new ApplePayPreparePayProcess.PaymentInput();
		input.bigOrderCode = bigOrderCode;
		input.payChannel = PaymentChannel.APP;
		return PayServiceFactory.getInstance().getApplePayPreparePayProcess().process(input);
	}
	
	/**
	 * 网关支付结果通知
	 * @param notifyParam 通知参数
	 * @return
	 */
	public static PayGateNotifyPayProcess.PaymentResult payGateNotify(Map<String,String> notifyParam){
		PayGateNotifyPayProcess.PaymentInput input = new PayGateNotifyPayProcess.PaymentInput();
		input.dataParam = notifyParam;
		return PayServiceFactory.getInstance().getPayGateNotifyPayProcess().process(input);
	}
//	
//	/**
//	 * 苹果支付结果通知
//	 * @param notifyParam 通知参数
//	 * @return
//	 */
//	public static ApplePayNotifyPayProcess.PaymentResult applePayNotify(Map<String,String> notifyParam){
//		ApplePayNotifyPayProcess.PaymentInput input = new ApplePayNotifyPayProcess.PaymentInput();
//		input.dataParam = notifyParam;
//		return PayServiceFactory.getInstance().getApplePayNotifyPayProcess().process(input);
//	}
}
