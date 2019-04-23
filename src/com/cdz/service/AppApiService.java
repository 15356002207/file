/**
 * 
 */
package service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.cloopen.rest.sdk.CCPRestSmsSDK;

import aos.framework.core.cache.CacheMasterDataService;
import aos.framework.core.id.AOSId;
import aos.framework.core.service.CDZBaseController;
import aos.framework.core.typewrap.Dto;
import aos.framework.core.typewrap.Dtos;
import aos.framework.core.utils.AOSCodec;
import aos.framework.core.utils.AOSJson;
import aos.framework.core.utils.AOSUtils;
import aos.framework.dao.AosParamsDao;
import aos.framework.dao.AosUserDao;
import aos.framework.web.router.HttpModel;
import aos.system.common.utils.SystemCons;
import aos.system.modules.cache.CacheUserDataService;
import dao.ActivityDao;
import dao.ActivityRuleDao;
import dao.AdvertDao;
import dao.AdvertTrafficDao;
import dao.Alarm_logDao;
import dao.ArticleDao;
import dao.Basic_userDao;
import dao.ChargingOrdersDao;
import dao.ChargingPileDao;
import dao.CommonCommentDao;
import dao.DepositListDao;
import dao.DeviceDao;
import dao.MembersDao;
import dao.MessagesDao;
import dao.OrdersPayDao;
import dao.RegionDao;
import dao.Repair_logDao;
import dao.SubscribeDao;
import dao.VoucherDao;
import po.Alarm_logPO;
import po.Basic_userPO;
import po.DevicePO;
import po.Repair_logPO;


/**
 * @author Administrator
 *cacheUserDataService.getDealerDTO(juid)
 */
@Service
public class AppApiService extends CDZBaseController {
	@Autowired
	private AdvertDao advertDao;
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private VoucherDao voucherDao;
	
	@Autowired
	private RegionDao regionDao;
	@Autowired
	private ActivityDao activityDao;
	
	@Autowired
	private CacheMasterDataService cacheMasterDataService;
	
	@Autowired
	private CacheUserDataService cacheUserDataService;
	@Autowired
	AosUserDao aosUserDao;
	
	@Autowired
	ActivityRuleDao activityRuleDao;
	@Autowired
	AdvertTrafficDao advertTrafficDao;
	
	@Autowired
	MembersDao membersDao;
	
	@Autowired
	AosParamsDao aosParamsDao;
	
	@Autowired
	ChargingPileDao chargingPileDao;
	@Autowired
	ChargingOrdersDao chargingOrdersDao;
	@Autowired
	MessagesDao messagesDao;
	@Autowired
	CommonCommentDao commonCommentDao;
	@Autowired
	OrdersPayDao ordersPayDao;
	@Autowired
	Basic_userDao basic_userDao;
	@Autowired
	DepositListDao depositListDao;
	@Autowired
	SubscribeDao subscribeDao;
	@Autowired
	DeviceDao deviceDao;
	@Autowired
	Repair_logDao repair_logDao;
	@Autowired
	Alarm_logDao alarm_logDao;

	public void init(HttpModel httpModel) {
		httpModel.setViewPath("myproject/map.jsp");
	}

	private static CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
	/*
	 * ##########################################################################
	 * 推送功能
	 */
	
	static String Alias = "18392888103";

	public void pushToSingle(HttpModel httpModel) {



		try {
			Push.pushToSingle();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void pushToGroup(HttpModel httpModel) {

		try {
			Push.pushToGroup();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}



	public void pushToApp(HttpModel httpModel) {


		try {
			Push.pushToApp();
			sendSms("18392888103,15356002207", "1", null);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/* ############################################保存头像######################### */

	public void getUserBasicInfo(HttpModel httpModel) {
		Dto qDto = httpModel.getInDto();
		Dto odto = Dtos.newDto();

		String phone = qDto.getString("phone");


		phone = "18392888103";

		Dto pDto = Dtos.newDto("account", phone);
		Basic_userPO basic_userPO = basic_userDao.selectOne(pDto); // 用
		String avatar = basic_userPO.getAvatar();
		if (null != avatar && !avatar.isEmpty()) {
			avatar = "http://118.126.95.215:9090/cdz/myupload" + basic_userPO.getAvatar();

		} else {
			avatar = "";

		}


		odto.put("name", basic_userPO.getName());
		odto.put("is_cert", basic_userPO.getIs_cert());
		odto.put("avatar", avatar);

		odto.put("msg", "查询成功");
		odto.put("status", "1");

		httpModel.setOutMsg(AOSJson.toJson(odto));
	}

	public void uploadUserAvatar(HttpModel httpModel) {
		Dto qDto = httpModel.getInDto();
		Dto odto = Dtos.newDto();

		String phone = qDto.getString("phone");
		String avatar = qDto.getString("avatar");

		/* String id_ = qDto.getString("image"); */
		phone = "18392888103";
		// id_ = "1807231116060914";
		avatar = "data:image/jpeg;base64,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";

		Dto pDto = Dtos.newDto("account", phone);
		Basic_userPO basic_userPO = basic_userDao.selectOne(pDto); // 用于检

		Dto outDto = Dtos.newDto();
		outDto = this.base64ToFile(httpModel.getRequest(), httpModel.getResponse(), avatar, outDto);
		if (SystemCons.SUCCESS.equals(outDto.getAppCode())) {

			basic_userPO.setAvatar(outDto.getAppMsg().replace("\\", "/"));
			avatar = "http://118.126.95.215:9090/cdz/myupload" + outDto.getAppMsg().replace("\\", "/");
			basic_userDao.updateByKey(basic_userPO);

			odto.put("avatar", avatar);

			odto.put("msg", "更新成功");
			odto.put("status", "1");
		}
		else {
			odto.put("avatar", "");

			odto.put("msg", "更新失败");
			odto.put("status", "-1");

		}



		httpModel.setOutMsg(AOSJson.toJson(odto));
	}

	/* ############################################报警######################### */
	public void getAlarmLogList(HttpModel httpModel) {
		Dto qDto = httpModel.getInDto();
		Dto odto = Dtos.newDto();

		String phone = qDto.getString("phone");
		phone = "15356002207";

		Dto pDto = Dtos.newDto();
		pDto.put("user_phone", phone);
		int rows = alarm_logDao.rows(pDto);
		pDto.put("limit", rows);// 默认查询出100个

		pDto.put("start", 0);

		List<Dto> alarmDtos = sqlDao.list("Alarm_log.listAlarm_logsPage", pDto);
		List<Dto> newListDtos = new ArrayList<Dto>();
		if (rows != 0) {

		for (Dto dto : alarmDtos) {
				Dto newDto = Dtos.newDto();

			newDto.put("alarm_time", dto.getString("alarm_time"));
			newDto.put("device_id", dto.getString("device_id"));

			String deviceid = dto.getString("device_id");
			String type = dto.getString("type_");
			String useraddress = "";
			if (type.equals("1")) {
				useraddress = "";

			} else {

				Dto pDto1 = Dtos.newDto("device_id", deviceid);
				DevicePO devicePO1 = deviceDao.selectOne(pDto1);
				useraddress = devicePO1.getUser_address();
			}

			newDto.put("user_address", useraddress);
			newDto.put("reason", dto.getString("reason_"));
			newDto.put("type", dto.getString("type_"));
				newListDtos.add(newDto);
				odto.put("data", newListDtos);
				odto.put("status", "1");
				odto.put("msg", "查询成功");

		}
		} else {
			Dto newDto = Dtos.newDto();
			newDto.put("alarm_time", "");
			newDto.put("device_id", "");
			newDto.put("user_address", "");
			newDto.put("reason", "");
			newDto.put("type", "");
			newListDtos.add(newDto);
			odto.put("data", newListDtos);
			odto.put("status", "-1");
			odto.put("msg", "查询失败");

		}

		httpModel.setOutMsg(AOSJson.toJson(odto));
	}

	public void sos(HttpModel httpModel) {
		Dto qDto = httpModel.getInDto();
		Dto odto = Dtos.newDto();

		String phone = qDto.getString("phone");
		phone = "15356002207";
		Alarm_logPO alarm_logPO = new Alarm_logPO();
		alarm_logPO.setAlarm_id(AOSId.appId(SystemCons.ID.SYSTEM));
		alarm_logPO.setUser_phone(phone);
		alarm_logPO.setAlarm_time(new Date());
		alarm_logPO.setType_("1");
		alarm_logDao.insert(alarm_logPO);



		odto.put("status", "1");
		odto.put("msg", "报警成功");
		httpModel.setOutMsg(AOSJson.toJson(odto));

	}

	/*
	 * 报修 #### ########,同一个手机号，is_complete,必须只有一个为0，正在维修，其他为1表示已完成
	 */

	public void getRepairLogList(HttpModel httpModel) {
		Dto qDto = httpModel.getInDto();
		Dto odto = Dtos.newDto();

		String phone = qDto.getString("phone");
		phone = "15356002208";

		Dto pDto = Dtos.newDto();
		pDto.put("user_phone", phone);
		int rows = repair_logDao.rows(pDto);
		pDto.put("limit", rows);// 默认查询出100个

		pDto.put("start", 0);

		List<Dto> repairDtos = sqlDao.list("Repair_log.listRepair_logsPage", pDto);
		List<Dto> newListDtos = new ArrayList<Dto>();
		if (rows != 0) {

			for (Dto dto : repairDtos) {
				Dto newDto = Dtos.newDto();
				newDto.put("device_id", dto.getString("device_id"));
				newDto.put("repair_id", dto.getString("repair_id"));
				newDto.put("repair_time", dto.getString("repair_time"));
				newDto.put("renovate_time", dto.getString("renovate_time"));
				newDto.put("handler_", dto.getString("handler_"));
				newDto.put("handler_phone", dto.getString("handler_phone"));
				newDto.put("error_reason", dto.getString("error_reason"));
				newDto.put("processing_state", dto.getString("processing_state"));

				newDto.put("repair_content", dto.getString("repair_content"));

				newListDtos.add(newDto);
				odto.put("data", newListDtos);
				odto.put("status", "1");
				odto.put("msg", "查询成功");

			}
		} else {
			Dto newDto = Dtos.newDto();
			newDto.put("device_id", "");
			newDto.put("repair_id", "");
			newDto.put("repair_time", "");
			newDto.put("renovate_time", "");
			newDto.put("handler_", "");
			newDto.put("handler_phone", "");
			newDto.put("error_reason", "");
			newListDtos.add(newDto);
			odto.put("data", newListDtos);
			odto.put("status", "-1");
			odto.put("msg", "查询失败");

		}

		httpModel.setOutMsg(AOSJson.toJson(odto));
	}

	public void deviceFaultReport(HttpModel httpModel) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Dto qDto = httpModel.getInDto();
		Dto odto = Dtos.newDto();

		String phone = qDto.getString("phone");// phone,即为登陆时的用户手机号,15356002207
		String device_id = qDto.getString("device_id");
		String fault_description = qDto.getString("fault_description");
		phone = "15356002207";
		device_id = "4gw1234569";
		Dto pDto = Dtos.newDto("user_phone", phone);
		Dto pDto1 = Dtos.newDto("phone", phone);
		pDto1.put("device_id", device_id);// 默认查询出100个
		// Repair_logPO repair_logPO = repair_logDao.selectOne(pDto);
		// phone不能为空非空，否则有问题，实际不会空，判断一下。 //selectone,只能返回一列值。即pdto唯一性
		int rows = repair_logDao.rows(pDto);
		pDto.put("limit", rows);// 默认查询出100个

		pDto.put("start", 0);
		List<Dto> repairDtos = sqlDao.list("Repair_log.listRepair_logsPage2", pDto);
		List<Dto> repairDtos1 = sqlDao.list("Device.listDevicesPage3", pDto1);
		if (null != repairDtos1 && !repairDtos1.isEmpty()) {

			if (null != repairDtos && !repairDtos.isEmpty()) {

				this.fail(odto, "报修失败，您有正在维修的设备");

		} else {

			Repair_logPO repair_logPO = new Repair_logPO();
			repair_logPO.setRepair_id(AOSId.appId(SystemCons.ID.SYSTEM));
			repair_logPO.setDevice_id(device_id);
			repair_logPO.setUser_phone(phone);
			repair_logPO.setRepair_time(new Date());
			repair_logPO.setRepair_content(fault_description);
				Calendar cal = Calendar.getInstance();
				int year = cal.get(Calendar.YEAR);// 获取年份
				int month = (cal.get(Calendar.MONTH) + 1);// 获取月份
				int day = cal.get(Calendar.DAY_OF_MONTH);// 获取日
				int hour = cal.get(Calendar.HOUR_OF_DAY);// 小时
				int minute = cal.get(Calendar.MINUTE);// 分

				repair_logPO.setState_info("已报修" + "%" + year + "年" + month + "月" + day + "日" + hour + ":" + minute);
			repair_logPO.setProcessing_state("0");
			repair_logPO.setIs_completed("0");
			repair_logDao.insert(repair_logPO);
			

			odto.put("status", "1");
			odto.put("msg", "报修成功");
		}

		} else {
			this.fail(odto, "报修失败，这不是您的设备");

		}
		httpModel.setOutMsg(AOSJson.toJson(odto));
	}


	public void getRepairProgress(HttpModel httpModel) {
		Dto qDto = httpModel.getInDto();
		Dto odto = Dtos.newDto();

		String phone = qDto.getString("phone");
		String repair_id = qDto.getString("repair_id");
		repair_id = "1808102254351862";
		phone = "15356002207";
		if (null != repair_id && !repair_id.isEmpty()) {
			
			Dto pDto = Dtos.newDto("repair_id", repair_id);
			Repair_logPO repair_logPO = repair_logDao.selectOne(pDto);
			
			odto.put("processing_state", repair_logPO.getProcessing_state());
			odto.put("state_info", repair_logPO.getState_info());
			odto.put("repair_content", repair_logPO.getRepair_content());

			odto.put("status", "1");
			odto.put("msg", "查询成功");
			
			

		} else {

		Dto pDto = Dtos.newDto("user_phone", phone);

		int rows = repair_logDao.rows(pDto);
		pDto.put("limit", rows);

		pDto.put("start", 0);


		List<Dto> repairDtos = sqlDao.list("Repair_log.listRepair_logsPage2", pDto);
		if (null != repairDtos && !repairDtos.isEmpty()) {
			for (Dto dto : repairDtos) {
			odto.put("processing_state", dto.getString("processing_state"));
			odto.put("state_info", dto.getString("state_info"));
				odto.put("repair_content", dto.getString("repair_content"));
			odto.put("status", "1");
			odto.put("msg", "查询成功");
			}
		} else {

			odto.put("processing_state", "");
			odto.put("state_info", "");
			odto.put("repair_content", "");
			odto.put("status", "-1");
			odto.put("msg", "查询失败");
		}
		}
		httpModel.setOutMsg(AOSJson.toJson(odto));


	}


	public void getDeviceList(HttpModel httpModel) {
		Dto qDto = httpModel.getInDto();
		Dto odto = Dtos.newDto();
	
		String phone = qDto.getString("phone");
		/* System.out.println(phone); */

		Dto pDto = Dtos.newDto();
		pDto.put("phone", phone);
		int rows = deviceDao.rows(pDto);
		pDto.put("limit", rows);// 默认查询出100个

			pDto.put("start", 0);

		List<Dto> deviceDtos = sqlDao.list("Device.listDevicesPage", pDto);
		List<Dto> newListDtos = new ArrayList<Dto>();
	
		
		for (Dto dto : deviceDtos) {
			Dto newDto = Dtos.newDto();	
				newDto.put("device_id", dto.getString("device_id"));
			newDto.put("user_id", dto.getString("user_id"));
			newDto.put("user_name", dto.getString("user_name"));
			newDto.put("user_address", dto.getString("user_address"));
			newDto.put("status",dto.getString("status"));
				newDto.put("is_alarming", dto.getString("is_alarming"));
			newDto.put("head", dto.getString("head"));
			newDto.put("head_phone", dto.getString("head_phone"));
			newDto.put("police_station", dto.getString("police_station"));
			newListDtos.add(newDto);

			}

			odto.put("data", newListDtos);
			odto.put("status", "1");
		odto.put("msg", "共查到" + rows + "条数据");


		httpModel.setOutMsg(AOSJson.toJson(odto));
	}
	
	
	public void getDeviceDetails(HttpModel httpModel) {
		Dto qDto = httpModel.getInDto();
		Dto odto = Dtos.newDto();

		String device_id = qDto.getString("device_id");

		Dto pDto = Dtos.newDto("device_id", device_id);
		DevicePO devicePO = deviceDao.selectOne(pDto);

			/* List<Dto> newListDtos = new ArrayList<Dto>(); */

			Dto newDto = Dtos.newDto();
			newDto.put("device_id", devicePO.getDevice_id());
			newDto.put("product_type", devicePO.getProduct_type());
			newDto.put("status", devicePO.getStatus());
			newDto.put("is_alarming", devicePO.getIs_alarming());
			newDto.put("production_date", devicePO.getProduction_date());
			newDto.put("install_date", devicePO.getInstall_date());
			newDto.put("guarantee_time", devicePO.getGuarantee_time());

			newDto.put("user_address", devicePO.getUser_address());
			newDto.put("repair_record", devicePO.getRepair_record());
			newDto.put("repair_progress", devicePO.getRepair_progress());

			newDto.put("head", devicePO.getHead());
			newDto.put("head_phone", devicePO.getHead_phone());
			newDto.put("police_station", devicePO.getPolice_station());
			/* newListDtos.add(newDto); */

			odto.put("data", newDto);
			odto.put("status", "1");
			odto.put("msg", "成功");

		
		httpModel.setOutMsg(AOSJson.toJson(odto));

	}

	public void deviceBinding(HttpModel httpModel) {
		Dto qDto = httpModel.getInDto();
		Dto odto = Dtos.newDto();

		String phone = qDto.getString("phone");// phone,即为登陆时的用户手机号,15356002207
		String address = qDto.getString("address");
		String deviceInfo = qDto.getString("deviceInfo");
		String[] info = deviceInfo.split("#");
		Dto pDto=Dtos.newDto("account", phone);
		Basic_userPO basic_userPO=basic_userDao.selectOne(pDto);   //用于检查账户是否存在
		Dto p1Dto = Dtos.newDto("device_id", info[0]);
		DevicePO devicePO1 = deviceDao.selectOne(p1Dto);
		if(null==basic_userPO){
			this.fail(odto, "手机号输入错误。");
		}else {

			if (null != devicePO1) {// 单个的对象判断，可以直接null！。若是数列，则不可以，需要加上empty判断。
				this.fail(odto, "绑定失败，该设备已被绑定");

			} else {
			String info1 = basic_userPO.getDevice_number();
			info1=info1+ "#" + info[0];
			basic_userPO.setDevice_number(info1);//device_number,代表设备编号

				DevicePO devicePO = new DevicePO();
			devicePO.setId_(AOSId.appId(SystemCons.ID.SYSTEM));
				devicePO.setUser_id("");
				devicePO.setPhone(phone);
			devicePO.setUser_address(address);
			devicePO.setUser_type(basic_userPO.getUser_type());
				devicePO.setDevice_id(info[0]);
			devicePO.setProduct_type(info[1]);
			devicePO.setProduction_date(info[2]);
				devicePO.setInstall_date(new Date());
			

			deviceDao.insert(devicePO);
			basic_userDao.updateByKey(basic_userPO);
			// TODO
		
			odto.put("status", "1");
			odto.put("msg", "绑定成功");
			}

		}
		httpModel.setOutMsg(AOSJson.toJson(odto));
	}
	
	
	public void userLogin(HttpModel httpModel) {
		Dto qDto = httpModel.getInDto();
		Dto odto = Dtos.newDto();
		String phone=qDto.getString("phone");
		String type=qDto.getString("type");
		String role = qDto.getString("role");
		
		Dto pDto=Dtos.newDto("account", phone);
		pDto.put("is_del", "0");
		//pDto.put("type_", "2");
		Basic_userPO basic_userPO=basic_userDao.selectOne(pDto);   //用于检查账户是否存在
		if(null==basic_userPO){
			this.fail(odto, "手机号输入错误或已被删除，请重新输入。");
		}else if(type.equals("1")) {
			
			String smsCode=qDto.getString("smsCode");
			String smsSessionId=qDto.getString("smsSessionId");
			
			if(AOSUtils.isEmpty(smsCode)){
				this.fail(odto, "smsCode不能为空");
			}else if(AOSUtils.isEmpty(smsSessionId)){
				this.fail(odto, "smsSessionId不能为空");
			}else{
			String sms_Code = cacheMasterDataService.getSMSCode(smsSessionId);
				if (AOSUtils.isEmpty(sms_Code)) {
					this.fail(odto, "验证码已失效!");
				}else if (!sms_Code.equals(smsCode)) {
					this.fail(odto, "验证码不正确!");
				}else{
			//String smsCode=AOSUtils.createRandomVcode();
			//String sessionId=httpModel.getRequest().getSession().getId();
			//cacheMasterDataService.cacheSMSCode(sessionId, smsCode);
			//发送短信，已实现
			//saveLogs("getSmsCode:smsCode-------------->>"+smsCode+",sessionId---------------->>"+sessionId,null);
			//dto.put(AOSCons.APPCODE_KEY, AOSCons.SUCCESS);
					odto.put("token", "150e18b7fdf84cb18cb2493b0d354f16");
					odto.put("uid", "f84cb18cb2493b0d354f12");
					odto.put("msg", "登录成功");
					odto.put("status", "1");
			//this.sendSms(phone, "1", smsCode);
			//dto.put(AOSCons.APPMSG_KEY, AOSCons.APPMSG_SUCCESS);
					}
				}
			}else if(type.equals("0")) {
				
				String password=qDto.getString("password");
				String password_ = AOSCodec.password(password);
				
				if(!StringUtils.equals(password_, basic_userPO.getPassword())){   //用于检查密码是否正确
				this.fail(odto, "密码不正确，请重新输入");
				}else {
				// this.fail(odto, "密码正确");
					odto.put("token", "150e18b7fdf84cb18cb2493b0d354f16");
					odto.put("uid", "f84cb18cb2493b0d354f12");
					odto.put("msg", "登录成功");
					odto.put("status", "1");
					
				}
			}else 
			this.fail(odto, "type不为1也不为0!");
		
			Basic_userPO basic_userPO1=new Basic_userPO();
			basic_userPO1.setToken("1");
			basic_userPO1.setId_(basic_userPO.getId_());
			basic_userDao.updateByKey(basic_userPO1);
				
			httpModel.setOutMsg(AOSJson.toJson(odto));
	}
	
	
	/**
	 * 获取验证码
	 * @param httpModel
	 */
	public void getSmsCode(HttpModel httpModel) {
		Dto qDto = httpModel.getInDto();
		Dto dto = Dtos.newDto();
		//String mobile=qDto.getString("mobile");
		String phone=qDto.getString("phone");
		String type=qDto.getString("type");
		//if(AOSUtils.isEmpty(mobile)){
		if(AOSUtils.isEmpty(phone)){
			this.fail(dto, "手机号码不能为空");
		}else if(AOSUtils.isEmpty(type)){
			this.fail(dto, "type不能为空");
		}else{
			String smsCode=AOSUtils.createRandomVcode();
			String sessionId=httpModel.getRequest().getSession().getId();
			cacheMasterDataService.cacheSMSCode(sessionId, smsCode);
			//发送短信，已实现
			saveLogs("getSmsCode:smsCode-------------->>"+smsCode+",sessionId---------------->>"+sessionId,null);
			//dto.put(AOSCons.APPCODE_KEY, AOSCons.SUCCESS);
			//dto.put("smsSessionId", sessionId);
			//dto.put("smsCode", smsCode);//暂时未去掉
			//this.sendSms(mobile, "1", smsCode);
			
			dto.put("msg", "验证码已发送");
			//dto.put("smsSessionId", "rj9kq6ghpvwv5n6jy6qtd2ep");
			dto.put("smsSessionId", sessionId);
			dto.put("status", "1");
			this.sendSms(phone, "1", smsCode);
			//dto.put(AOSCons.APPMSG_KEY, AOSCons.APPMSG_SUCCESS);
		}
		httpModel.setOutMsg(AOSJson.toJson(dto));
	}
	
	
	/**
	 * 会员注册
	 */
	public void userRegister(HttpModel httpModel) {
		Dto qDto = httpModel.getInDto();
		Dto odto = Dtos.newDto();
		
		String account=qDto.getString("phone");
		String password=qDto.getString("password");
		String sms_code=qDto.getString("smsCode");
		String smsSessionId=qDto.getString("smsSessionId");
		String role=qDto.getString("role");
		/*
		 * if(!qDto.containsKey("account")||AOSUtils.isEmpty(account)){ this.fail(odto,
		 * "account不能为空"); }else
		 * if(!qDto.containsKey("password")||AOSUtils.isEmpty(password)){
		 * this.fail(odto, "password不能为空"); }else
		 * if(!qDto.containsKey("sms_code")||AOSUtils.isEmpty(sms_code)){
		 * this.fail(odto, "sms_code不能为空"); }else
		 */
		if(!qDto.containsKey("smsSessionId")||AOSUtils.isEmpty(smsSessionId)){
			this.fail(odto, "smsSessionId不能为空");
		}else{
			Dto pDto=Dtos.newDto("account", account);
			pDto.put("is_del_", "0");   //0表示未删除
			//pDto.put("type_", "2");     
			//AosUserPO aosUserPO=aosUserDao.selectOne(pDto);
			Basic_userPO basic_userPO1=basic_userDao.selectOne(pDto);
			String smsCode = cacheMasterDataService.getSMSCode(smsSessionId);
			if(null!=basic_userPO1){
				this.fail(odto, "当前手机号已存在，请重新输入。");
			}else if (AOSUtils.isEmpty(smsCode)) {
				this.fail(odto, "验证码已失效!");
			} else if (!smsCode.equals(sms_code)) {
				this.fail(odto, "验证码不正确!");
			}else{
				String password_ = AOSCodec.password(password);
				Basic_userPO basic_userPO=new Basic_userPO();
				basic_userPO.setUser_type("1");
				basic_userPO.setId_(AOSId.appId(SystemCons.ID.SYSTEM));
				basic_userPO.setAccount(account);
				basic_userPO.setPassword(password_);
				basic_userPO.setCreate_at(AOSUtils.getDateTime());
				basic_userPO.setStatus("0");  //0表示未屏蔽
				basic_userPO.setIs_del("0");   //0表示未删除
				basic_userPO.setUser_type(role);
				basic_userDao.insert(basic_userPO);

				//注册送优惠券
				/*
				Dto pDto_=Dtos.newDto("is_del", "0");//
				pDto_.put("status_", "1");
				pDto_.put("type_", "3");
				List<ActivityPO> list=activityDao.list(pDto_);
				if(null!=list&&list.size()>0){
					ActivityPO activityPO=list.get(0);
					if(null!=activityPO){
						for(int i=0;i<activityPO.getAr_num().intValue();i++){
							VoucherPO voucherPO=new VoucherPO();
							voucherPO.setActivity_id(activityPO.getActivity_id());
							//voucherPO.setOrder_id(order_id);
							//voucherPO.setCond_value(activityRulePO.getCond_value());
							voucherPO.setIs_del("0");
							voucherPO.setDealer_id(membersPO.getId_());
							voucherPO.setDirection("0");
							voucherPO.setVoucher_amt(activityPO.getAmount());
							voucherPO.setVoucher_id(AOSId.appId(SystemCons.ID.SYSTEM));
							voucherPO.setCreate_date(AOSUtils.getDateTime());
							voucherPO.setEffec_date(AOSUtils.dateAdd(AOSUtils.getCurDayTimestamp(), Calendar.MONTH, Integer.valueOf(AOSCxt.getParam("voucherValidity"))));
							voucherDao.insert(voucherPO);
						}
					}
				}
				*/
				//odto.put(AOSCons.APPCODE_KEY, AOSCons.SUCCESS);
				//odto.put("uid", membersPO.getId_());
				//odto.put(AOSCons.APPMSG_KEY, AOSCons.APPMSG_SUCCESS);
				odto.put("uid",basic_userPO.getId_());
				odto.put("msg","注册成功");
				odto.put("status","1");
			}
		}
		httpModel.setOutMsg(AOSJson.toJson(odto));
	}
	
	
	/**
	 * 忘记密码
	 */
	public void forgetPwd(HttpModel httpModel) {
		Dto qDto = httpModel.getInDto();
		Dto odto = Dtos.newDto();
		
		String mobile=qDto.getString("phone");
		String new_pwd=qDto.getString("newPwd");
		String sms_code=qDto.getString("smsCode");
		//String confirm_pwd=qDto.getString("confirmPwd");
		String smsSessionId=qDto.getString("smsSessionId");
		// this.fail(odto, new_pwd);
		/*
		 * if(!qDto.containsKey("mobile")||AOSUtils.isEmpty(mobile)){ this.fail(odto,
		 * "mobile不能为空"); }else
		 * if(!qDto.containsKey("new_pwd")||AOSUtils.isEmpty(new_pwd)){ this.fail(odto,
		 * "new_pwd不能为空"); }else
		 * if(!qDto.containsKey("sms_code")||AOSUtils.isEmpty(sms_code)){
		 * this.fail(odto, "sms_code不能为空"); }else
		 * if(!qDto.containsKey("confirm_pwd")||AOSUtils.isEmpty(confirm_pwd)){
		 * this.fail(odto, "confirm_pwd不能为空"); }else if(!confirm_pwd.equals(new_pwd)){
		 * this.fail(odto, "新密码与确认密码不一致"); }else
		 */
		if(!qDto.containsKey("smsSessionId")||AOSUtils.isEmpty(smsSessionId)){
			this.fail(odto, "smsSessionId不能为空");
		}else{
			Dto pDto=Dtos.newDto("account", mobile);
			pDto.put("is_del", "0");
			//pDto.put("type_", "2");
			Basic_userPO basic_userPO=basic_userDao.selectOne(pDto);
			
			if(null==basic_userPO){
				this.fail(odto, "手机号码不存在，请重新输入。");
			}else{
				String smsCode = cacheMasterDataService.getSMSCode(smsSessionId);
				if (AOSUtils.isEmpty(smsCode)) {
					this.fail(odto, "验证码已失效!");
				} else if (!smsCode.equals(sms_code)) {
					this.fail(odto, "验证码不正确!");
				} else {
					//new_pwd = "1234567890";
					String password = AOSCodec.password(new_pwd);
					
					Basic_userPO basic_userPO_=new Basic_userPO();
					basic_userPO_.setPassword(password);
					basic_userPO_.setId_(basic_userPO.getId_());
					basic_userDao.updateByKey(basic_userPO_);
					//odto.put(AOSCons.APPCODE_KEY, AOSCons.SUCCESS);
					//odto.put(AOSCons.APPMSG_KEY, AOSCons.APPMSG_SUCCESS);
					odto.put("status","1");
					odto.put("msg","密码修改成功");
				}
			}
		}
		httpModel.setOutMsg(AOSJson.toJson(odto));
	}
	
	public void userSignOut(HttpModel httpModel) {
		Dto qDto = httpModel.getInDto();
		Dto odto = Dtos.newDto();
		String token=qDto.getString("token");
		
		Dto pDto=Dtos.newDto("token", token);
		//pDto.put("is_del", "0");
		//pDto.put("type_", "2");
		Basic_userPO basic_userPO=basic_userDao.selectOne(pDto);   //用于检查账户是否存在
		if(null==basic_userPO){
			this.fail(odto, "手机号输入错误或已被删除，请重新输入。");
		}else {
			
			Basic_userPO basic_userPO1=new Basic_userPO();
			basic_userPO1.setQq(AOSUtils.getDateTime().toString());
			basic_userPO1.setId_(basic_userPO.getId_());
			basic_userDao.updateByKey(basic_userPO1);
			
			odto.put("status","1");
			odto.put("msg","退出成功");
			
			}
				
			httpModel.setOutMsg(AOSJson.toJson(odto));
	}
	
	
	
	private void initSmsConfig(){
		//restAPI.init(AOSCxt.getParam("sms_url"), AOSCxt.getParam("sms_port"));
		//restAPI.setAccount(AOSCxt.getParam("account_sid"), AOSCxt.getParam("auth_token"));
		//restAPI.setAppId(AOSCxt.getParam("app_id"));
		restAPI.init("app.cloopen.com", "8883");
		restAPI.setAccount("8aaf07086436008b01643c2a00dd05b7", "bcf4e77d8e0b41c88732e51eeb6139db");
		restAPI.setAppId("8aaf07086436008b01643c2a014105be");
	}

	private void sendSms(String mobile, String templateId, String smsCode) {
		this.initSmsConfig();
		HashMap<String, Object> result= restAPI.sendTemplateSMS(mobile,templateId,new String[]{smsCode,"2"});
		this.printSmsInfo(result, "");
		
	}
	private void printSmsInfo(HashMap<String, Object> result,String mobile){
		saveLogs("printSmsInfo:短信发送 手机号:"+mobile+"  SDKTestGetSubAccounts result=" + result,null);
		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
				System.out.println(key +" = "+object);
			}
		}else{

			//异常返回输出错误码和错误信息
			saveLogs("printSmsInfo:错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"),null);
		}
	}
	


}
