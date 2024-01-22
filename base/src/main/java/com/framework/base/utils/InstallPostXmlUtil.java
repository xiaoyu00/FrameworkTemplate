package com.framework.base.utils;

public class InstallPostXmlUtil {

	// 登陆
	public static String installLoginCheckXml(String studentIdentity, String studentPassword) {
		return "<studentIdentity>" + studentIdentity + "</studentIdentity><studentPassword>" + studentPassword
				+ "</studentPassword>";
	}

	// 身份
	public static String installTokenCheckXml(String studentToken) {
		return "<studentToken>" + studentToken + "</studentToken>";
	}

	// 修改密码
	public static String installChangePasswordXml(String studentToken, String password, String newPassword) {
		return "<studentToken>" + studentToken + "</studentToken><password>" + password + "</password><newPassword>"
				+ newPassword + "</newPassword>";
	}

	// 学员信息
	public static String installStudentInfoXml(String studentToken) {
		return "<studentToken>" + studentToken + "</studentToken>";
	}

	// 教练员信息
	public static String installCoachDataXml(String studentToken) {
		return "<studentToken>" + studentToken + "</studentToken>";
	}

	// 教练员休息时间
	public static String installRestDataXml(String studentToken) {
		return "<studentToken>" + studentToken + "</studentToken>";
	}

	// 教练员空闲时间
	public static String installCoachIdleXml(String studentToken, String coachGuid) {
		return "<studentToken>" + studentToken + "</studentToken><coachGuid>" + coachGuid + "</coachGuid>";
	}

	// 某教练员某日期忙碌
	public static String installCoachBusyXml(String studentToken, String coachGuid, String trainDate) {
		return "<studentToken>" + studentToken + "</studentToken><coachGuid>" + coachGuid + "</coachGuid><trainDate>"
				+ trainDate + "</trainDate>";
	}

	// 驾校排班信息
	public static String installDurationDataXml(String studentToken) {
		return "<studentToken>" + studentToken + "</studentToken>";
	}

	// 枚举信息
	public static String installEnumDataXml() {
		return "";
	}

	// 教练员，车辆及培训场地绑定信息
	public static String installBindDataXml(String studentToken) {
		return "<studentToken>" + studentToken + "</studentToken>";
	}

	// 培训场地信息
	public static String installAreaDataXml(String studentToken) {
		return "<studentToken>" + studentToken + "</studentToken>";
	}

	// 培训车辆信息
	public static String installVehicleDataXml(String studentToken) {
		return "<studentToken>" + studentToken + "</studentToken>";
	}

	// 学员提交预约信息
	public static String installAppointmentInputXml(String studentToken, String trainDate, String startH, String startM,
			String endH, String endM, final String coachGuid, String vehicleGuid, String subjectGuid) {
		return "<studentToken>" + studentToken + "</studentToken><trainDate>" + trainDate + "</trainDate><startH>"
				+ startH + "</startH><startM>" + startM + "</startM><endH>" + endH + "</endH><endM>" + endM
				+ "</endM><coachGuid>" + coachGuid + "</coachGuid><vehicleGuid>" + vehicleGuid
				+ "</vehicleGuid><subjectGuid>" + subjectGuid + "</subjectGuid>";
	}

	// 学员某天预约培训信息
	public static String installAppointmentDataXml(String studentToken, String trainDate) {
		return "<studentToken>" + studentToken + "</studentToken><trainDate>" + trainDate + "</trainDate>";
	}

	// 学员取消预约信息
	public static String installAppointmentCancelXml(String studentToken, String appointmentGuid) {
		return "<studentToken>" + studentToken + "</studentToken><appointmentGuid>" + appointmentGuid
				+ "</appointmentGuid>";
	}
}
