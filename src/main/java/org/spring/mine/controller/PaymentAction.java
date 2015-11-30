//package org.spring.mine.controller;
//
//import java.math.BigDecimal;
//import java.sql.Timestamp;
//import java.util.Date;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.lang.time.DateFormatUtils;
//import org.apache.commons.lang.time.DateUtils;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.alibaba.druid.util.StringUtils;
//import com.allinpay.ets.client.RequestOrder;
//import com.allinpay.ets.client.StringUtil;
//import com.team.mine.common.ClientRequestUtil;
//import com.team.mine.common.PageBean;
//import com.team.mine.common.PaymentUtil;
//import com.team.mine.common.ResultInfo;
//import com.team.mine.common.SysConstants;
//import com.team.mine.domain.Payment;
//import com.team.mine.domain.UserAccount;
//import com.team.mine.util.CacheUtil;
//
//@Controller
//@RequestMapping("/PaymentAction")
//public class PaymentAction extends BaseAction{
//
//
//	/***
//	 * 支付
//	 */
//	@RequestMapping("paying")
//	public ModelAndView paying(HttpServletRequest request) {
//		ResultInfo info = new ResultInfo();
//		UserAccount user = CacheUtil.getCurrUserAccount(request);
//
//		String now = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
//		
//		String serverUrl = setting.getServerUrl(); // 测试支付网关 ； 正式网关
//													// service.allinpay.com
//		String payerTelephone = request.getParameter("payerTelephone");
//		String payerEmail = request.getParameter("payerEmail");
//		String payerName = request.getParameter("payerName");
//		String itemId = request.getParameter("itemId");
//
//		ModelAndView mav = getModelAndView();
//		if (null == serverUrl || StringUtil.isEmpty(serverUrl) || null == itemId || StringUtils.isEmpty(itemId)) {
//			info.set(0, "参数异常");
//			mav.addObject(info);
//			return mav;
//		}
//		if (null == now || StringUtil.isEmpty(now)) {
//			info.set(-1, "支付超时");
//			mav.addObject(info);
//			return mav;
//		}
//
//		ItemMember item = itemMemberService.queryItemMemberById(Long.valueOf(itemId));
//		if (null == item) {
//			info.set(-1, "您购买的商品不存在，请重新选择");
//			mav.addObject(info);
//			return mav;
//		}
//
//		String key = setting.getKey(); // 用于计算signMsg的key值:
//		String version = setting.getVersion();
//		String language = setting.getLanguage();
//		String inputCharset = setting.getInputCharset(); // 字符集：1代表utf-8
//		String merchantId = setting.getMerchantId(); // 商户测试 账户
//		String pickupUrl = "http://" + setting.getReceiveHost() + setting.getPickupUrl(); // 取货(支付结果展示)地址
//		String receiveUrl = "http://" + setting.getReceiveUrl() + setting.getReceiveUrl(); // 商户系统通知地址:
//		String payType = setting.getPayType(); // 支付方式
//		String signType = setting.getSignType(); // 签名类型
//		String tradeNature = setting.getTradeNature(); // GOODS表示实物类型，SERVICES表示服务类型
//		String orderNo = "No" + now;
//		String orderAmount = new BigDecimal(item.price.doubleValue() * 100).intValue() + "";
//		String orderDatetime = now;
//		String orderCurrency = setting.getOrderCurrency(); // 默认为人民币;0,156表示人民币;344表示港币;840表示美元
//		String orderExpireDatetime = setting.getOrderExpireDatetime(); // 订单过期时间
//		String payerIDCard = request.getParameter("payerIDCard");
//		String pid = request.getParameter("pid");
//		String productName = item.name;
//		String productId = "prd:" + item.itemId;
//		String productNum = "1";
//		String productPrice = item.price.longValue() * 100 + "";
//		String productDesc = item.descript;
//		String ext1 = request.getParameter("ext1");
//		String ext2 = request.getParameter("ext2");
//		String extTL = request.getParameter("extTL");// 通联商户拓展业务字段，在v2.2.0版本之后才使用到的，用于开通分账等业务
//		String issuerId = request.getParameter("issuerId");
//		String pan = request.getParameter("pan");
//		String certPath = setting.getCertPath();
//
//		RequestOrder requestOrder = PaymentUtil.setRequestOrder(now, serverUrl, payerTelephone, payerEmail, payerName,
//				itemId, key, version, language, inputCharset, merchantId, pickupUrl, receiveUrl, payType, signType,
//				tradeNature, orderNo, orderAmount, orderDatetime, orderCurrency, orderExpireDatetime, payerIDCard, pid,
//				productName, productId, productNum, productPrice, productDesc, ext1, ext2, extTL, issuerId, pan,
//				certPath);
//
//		String strSrcMsg = requestOrder.getSrc(); // 此方法用于debug，测试通过后可注释。
//		String strSignMsg = requestOrder.doSign(); // 签名，设为signMsg字段值。
//		requestOrder.setSignMsg(strSignMsg);
//		serverUrl = "http://" + serverUrl + "/gateway/index.do?";
//
//		// 插入一笔订单
//		Payment pay = new Payment(requestOrder);
//		pay.merchantId = requestOrder.getMerchantId();
//		pay.signType = requestOrder.getSignType();
//
//		pay.accountId = user.getAccountId();
//		pay.itemId = item.itemId;
//		int result = paymentService.insert(pay);
//		mav = getModelAndView("paying");
//		mav.addObject("result", result);
//		mav.addObject("strSrcMsg", strSrcMsg);
//		mav.addObject("strSignMsg", strSignMsg);
//		mav.addObject("serverUrl", serverUrl);
//		return mav;
//	}
//
//	/**
//	 * 询问结果
//	 * 
//	 * @param requestOrder
//	 * @param strSrcMsg
//	 * @param strSignMsg
//	 * @throws Throwable
//	 */
//	@RequestMapping("getAnsyPayResult")
//	public  ModelAndView getAnsyPayResult() {
//
//		List<PaymentItemBo> payList = paymentService.queryNoPaymentItemHalfHour();
//		String serverUrl = setting.getServerUrl(); // 测试支付网关 ；
//																	// 正式网关
//		String resultUrl = "http://" + serverUrl + "/gateway/index.do?"; // 取货(支付结果展示)地址
//		for (PaymentItemBo paymentBo : payList) {
//			String parameterData = paymentBo.getPay().strSrcMsg + "&signMsg=" + paymentBo.getPay().signMsg;
//			parameterData += "&queryDatetime=" + paymentBo.getPay().orderNo.substring(2);
//			// service.allinpay.com
//			// System.out.println("getAnsyPayResult=-=======>>" +
//			// parameterData);
//			String result = "";
//			try {
//				ItemMember item = paymentBo.getItem();
//
//				result = ClientRequestUtil.postRequest(resultUrl, parameterData);
//				System.out.println("response:" + result);
//				if (result.contains("支付成功") || result.contains("payResult：1")) {
//					// pay.paymentOrderId =
//					// Long.valueOf(map.get("paymentOrderId") + "");
//					Payment payment = paymentBo.getPay();
//					payment.payTime = new Timestamp(System.currentTimeMillis());
//					if (paymentBo.getItem().gender.gender.equals(ItemGender.MALE.gender)) {
//						payment.payResult = SysConstants.PAY_SUCC_MALE + "";
//					} else if (paymentBo.getItem().gender.gender.equals(ItemGender.FEMALE.gender + "")) {
//						payment.payResult = SysConstants.PAY_SUCC_FEMALE + "";
//					} else {
//						payment.payResult = SysConstants.PAY_SUCC_ALL + "";
//					}
//					paymentService.updateOrderPay(payment);
//
//					payment.payTime = new Timestamp(System.currentTimeMillis());
//					payment.payResult = "1";
//					int upResult = paymentService.updateOrderPay(payment);
//
//					if (upResult > 0) {
//						UserVipLevel vip = new UserVipLevel();
//						UserVipLevel getVip = userVipService.queryByAccountItemPower(payment.accountId, item.gender.name(),
//								item.power);
//						if (null != getVip) {
//							vip = getVip;
//							// 多次购买同一会员的时长 累加
//							vip.invalidTime = new Timestamp(DateUtils.addMonths(vip.invalidTime, item.longTime).getTime());
//						} else {
//							vip.invalidTime = new Timestamp(DateUtils.addMonths(new Date(), item.longTime).getTime());
//						}
//						vip.accountId = payment.accountId;
//						vip.itemId = item.itemId;
//						vip.level = item.power;
//						vip.area = item.gender;
//						vip.description = item.name;
//						vip.orderNo = payment.orderNo;
//						vip.insertTime = new Timestamp(System.currentTimeMillis());
//						userVipService.insert(vip);
//					}
//				}
//			} catch (Throwable e) {
//				e.printStackTrace();
//			}
//		}
//		return getModelAndView();
//	}
//
//	/***
//	 * 显示购买结果
//	 */
//	@RequestMapping("showPayResult")
//	public ModelAndView showPayResult(String orderNo) {
//		ModelAndView mav = getModelAndView("showPayResult");
//		Payment pay = paymentService.queryPaymentById(orderNo);
//		mav.addObject("pay", pay);
//		return mav;
//	}
//
//	/**
//	 * 支付页初始
//	 */
//	@RequestMapping("payinit")
//	public ModelAndView payinit(HttpServletRequest request, long itemId) {
//		ModelAndView mav = getModelAndView("payinit");
//		// String buyCount=params.get("buyCount");
//		ResultInfo info = new ResultInfo();
//		if (itemId <= 0) {
//			info.set(0, "商品参数错误");
//			mav.addObject("info", info);
//			return mav;
//		}
//		UserAccount user = CacheUtil.getCurrUserAccount(request);
//		ItemMember item = itemMemberService.queryItemMemberById(itemId);
//		if (null == item) {
//			info.set(-1, "您购买的商品不存在，请重新选择");
//			mav = getModelAndView();
//			mav.addObject(info);
//			return mav;
//		}
//		String productName = item.name;
//		String productPrice = item.price.toString();
//		String productNum = "1";
//		String productId = itemId + "";
//		String productDesc = item.descript;
//		String orderAmount = new BigDecimal(item.price.doubleValue() * 100).intValue() + ""; // 付款金额（单位：分）
//		mav = getModelAndView("payinit");
//		mav.addObject("user", user);
//		mav.addObject("item", item);
//		mav.addObject("productName", productName);
//		mav.addObject("productPrice", productPrice);
//		mav.addObject("productNum", productNum);
//		mav.addObject("productId", productId);
//		mav.addObject("productDesc", productDesc);
//		mav.addObject("orderAmount", orderAmount);
//		return mav;
//	}
//
//	/**
//	 * 购买选项页
//	 */
//	@RequestMapping("selectItem")
//	public ModelAndView selectItem(HttpServletRequest request) {
//	// 加载购买商品的选项
//		String rurl = request.getParameter("rurl");
//		String curr = request.getParameter("curr");
//		String size = request.getParameter("size");
//		int currPage = curr != null && curr.matches("^\\d+") ? Integer.valueOf(curr) : 1;
//		int Pagesize = size != null && size.matches("^\\d+") ? Integer.valueOf(size) : 15;
//		ModelAndView mav=getModelAndView();
//		UserAccount user = CacheUtil.getCurrUserAccount(request);
//		if(user==null){
//			mav=getModelAndView("/UserAction/loginit");
//			mav.addObject("rurl", rurl);
//			return mav;
//		}
//		PageBean<PaymentItemVipBo> page = paymentService.queryPaymentPage(user.getAccountId(), currPage, Pagesize);
//		
//		mav.addObject("page", page);
//		mav.addObject("rurl", rurl);
//		return mav;
//	}
//
//	/***
//	 * 支付结果验签回调
//	 * 
//	 * @param orderNo
//	 * @param payAmount
//	 * @param paymentOrderId
//	 * @param payResult
//	 */
//	@RequestMapping("payReceive")
//	public ModelAndView payReceive(HttpServletRequest request) {
//		String merchantId = request.getParameter("merchantId");
//		String version = request.getParameter("version");
//		String language = request.getParameter("language");
//		String signType = request.getParameter("signType");
//		String payType = request.getParameter("payType");
//		String issuerId = request.getParameter("issuerId");
//		String paymentOrderId = request.getParameter("paymentOrderId");
//		String orderNo = request.getParameter("orderNo");
//		String orderDatetime = request.getParameter("orderDatetime");
//		String orderAmount = request.getParameter("orderAmount");
//		String payDatetime = request.getParameter("payDatetime");
//		String payAmount = request.getParameter("payAmount");
//		String ext1 = request.getParameter("ext1");
//		String ext2 = request.getParameter("ext2");
//		String payResult = request.getParameter("payResult");
//		String errorCode = request.getParameter("errorCode");
//		String returnDatetime = request.getParameter("returnDatetime");
//		String signMsg = request.getParameter("signMsg");
//		if (null == orderNo || null == paymentOrderId || null == payResult) {
//			this.selectItem(request);
//		}
//
//		// 验签是商户为了验证接收到的报文数据确实是支付网关发送的。
//		// 构造订单结果对象，验证签名。
//		com.allinpay.ets.client.PaymentResult paymentResult = new com.allinpay.ets.client.PaymentResult();
//		paymentResult.setMerchantId(merchantId);
//		paymentResult.setVersion(version);
//		paymentResult.setLanguage(language);
//		paymentResult.setSignType(signType);
//		paymentResult.setPayType(payType);
//		paymentResult.setIssuerId(issuerId);
//		paymentResult.setPaymentOrderId(paymentOrderId);
//		paymentResult.setOrderNo(orderNo);
//		paymentResult.setOrderDatetime(orderDatetime);
//		paymentResult.setOrderAmount(orderAmount);
//		paymentResult.setPayDatetime(payDatetime);
//		paymentResult.setPayAmount(payAmount);
//		paymentResult.setExt1(ext1);
//		paymentResult.setExt2(ext2);
//		paymentResult.setPayResult(payResult);
//		paymentResult.setErrorCode(errorCode);
//		paymentResult.setReturnDatetime(returnDatetime);
//		// signMsg为服务器端返回的签名值。
//		paymentResult.setSignMsg(signMsg);
//		// signType为"1"时，必须设置证书路径。
//		if ("1".equals(signType)) {
//			paymentResult.setCertPath(setting.getCertPath());
//		}
//		// 验证签名：返回true代表验签成功；否则验签失败。
//		boolean verifyResult = paymentResult.verify();
//
//		// 验签成功，还需要判断订单状态，为"1"表示支付成功。
//		boolean paySuccess = verifyResult && payResult.equals("1");
//
//		PaymentItemBo paymentBo = paymentService.queryPaymentItemBoById(orderNo);
//		ModelAndView mav = getModelAndView("payReceive");
//		if (paymentBo == null) {
//			payResult = "-1";
//			mav.addObject("payResult", payResult);
//			return mav;
//
//		}
//		ItemMember item = paymentBo.getItem();
//		Payment payment = paymentBo.getPay();
//
//		if (!paySuccess) {
//			payResult = "-9";
//			mav.addObject("payResult", payResult);
//			return mav;
//		}
//
//		payment.orderNo = orderNo;
//		payment.paymentOrderId = Long.valueOf(paymentOrderId);
//		payment.payTime = new Timestamp(System.currentTimeMillis());
//		payment.payResult = "1";
//		int result = paymentService.updateOrderPay(payment);
//
//		if (result > 0) {
//			UserVipLevel vip = new UserVipLevel();
//			UserVipLevel getVip = userVipService.queryByAccountItemPower(payment.accountId, item.gender.name(), item.power);
//			if (null != getVip) {
//				vip = getVip;
//				// 多次购买同一会员的时长 累加
//				vip.invalidTime = new Timestamp(DateUtils.addMonths(vip.invalidTime, item.longTime).getTime());
//			} else {
//				vip.invalidTime = new Timestamp(DateUtils.addMonths(new Date(), item.longTime).getTime());
//			}
//			vip.accountId = payment.accountId;
//			vip.itemId = item.itemId;
//			vip.level = item.power;
//			vip.area = item.gender;
//			vip.description = item.name;
//			vip.orderNo = orderNo;
//			vip.insertTime = new Timestamp(System.currentTimeMillis());
//			result = userVipService.insert(vip);
//		}
//
//		BigDecimal amount = new BigDecimal(payAmount);
//		mav.addObject("orderNo", orderNo);
//		mav.addObject("amount", amount);
//		mav.addObject("payResult", payResult);
//		return mav;
//	}
//
//	/***
//	 * 支付结果通知回调
//	 * 
//	 * @param orderNo
//	 * @param payAmount
//	 * @param description
//	 */
//	@RequestMapping("paySucc")
//	public ModelAndView paySucc(String orderNo, String payAmount, String paymentOrderId, String payResult) {
//		ModelAndView mav = getModelAndView("paySucc");
//
//		// 测试开启下列代码
//		// if(null==orderNo || null==paymentOrderId || null==payResult){
//		// PaymentAction.selectItem();
//		// }
//		//
//		// PaymentItemBo paymentBo =
//		// paymentService.queryPaymentItemBoById(orderNo);
//		//
//		// if (!payResult.equals("1") || paymentBo == null) {
//		// payResult = "-1";
//		// render(payResult);
//		// }
//		// ItemMember item = paymentBo.item;
//		// Payment payment = paymentBo.pay;
//		// if("1".equals(payment.payResult)){
//		// payResult="-9";
//		// render(payResult);
//		// }
//		// payment.orderNo = orderNo;
//		// payment.paymentOrderId = Long.valueOf(paymentOrderId);
//		// payment.payTime = new Timestamp(System.currentTimeMillis());
//		// payment.payResult = "1";
//		// int result = paymentService.updateOrderPay(payment);
//		//
//		// if (result > 0) {
//		// UserVipLevel vip = new UserVipLevel();
//		// UserVipLevel getVip =
//		// vipService.queryByAccountItemPower(payment.accountId,
//		// item.gender.name(),item.power);
//		// if (null != getVip) {
//		// vip = getVip;
//		// //多次购买同一会员的时长 累加
//		// vip.invalidTime = new Timestamp(DateUtils.addMonths(vip.invalidTime,
//		// item.longTime).getTime());
//		// }else{
//		// vip.invalidTime = new Timestamp(DateUtils.addMonths(new Date(),
//		// item.longTime).getTime());
//		// }
//		// vip.accountId = payment.accountId;
//		// vip.itemId = item.itemId;
//		// vip.level = item.power;
//		// vip.area=item.gender;
//		// vip.description=item.name;
//		// vip.orderNo=orderNo;
//		// vip.insertTime=new Timestamp(System.currentTimeMillis());
//		// result = vipService.insert(vip);
//		// }
//		BigDecimal amount = new BigDecimal(payAmount);
//		mav.addObject("orderNo", orderNo);
//		mav.addObject("amount", amount);
//		mav.addObject("payResult", payResult);
//		return mav;
//	}
//	
//}
