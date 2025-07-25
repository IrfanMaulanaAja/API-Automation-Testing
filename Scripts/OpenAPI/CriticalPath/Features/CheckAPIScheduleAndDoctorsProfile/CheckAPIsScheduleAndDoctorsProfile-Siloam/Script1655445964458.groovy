import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.RestRequestObjectBuilder
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.impl.HttpTextBodyContent //for text in body
import com.kms.katalon.core.testobject.impl.HttpFileBodyContent //for file in body
import com.kms.katalon.core.testobject.impl.HttpFormDataBodyContent //for form data body
import com.kms.katalon.core.testobject.impl.HttpUrlEncodedBodyContent //for URL encoded text body
import internal.GlobalVariable as GlobalVariable
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import groovy.json.JsonLexer
import groovy.json.JsonParser
import groovy.json.JsonParserType
import com.kms.katalon.core.testobject.RequestObject as RequestObject
import java.util.Map
import java.util.List
import java.security.*
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import groovy.time.TimeCategory


public class Local_Global_Variable {
	public static String version_for_path 		= findTestData('Data Files/Staging/OpenAPI/LoginAccount/Alodokter/User').getValue('VERSION', 1)
	public static String version_for_endpoint 	= 'v' + version_for_path.replace('.', '')
	public static String prefix_version			= 'v2'
	public static String doctor_name			= 'dr. Vivien Puspitasari,Sp.S'
	public static String doctor_id				= ''
	public static String hospital_schedule_id	= ''
	public static String schedule_date			= ''
	public static String schedule_hour			= ''
	public static String hospital_id			= ''
	public static String hospital_name			= ''
	public static String specialisty_id			= ''
	public static String doctor_speciality		= ''
	public static String booking_id				= ''
	public static String schedule_id			= ''
	public static String schedule_id_appoinment	= ''
	
	public static String hospital_id_OpenAPI    = '39764039-37b9-4176-a025-ef7b2e124ba4'
	public static String hospital_name_OpenAPI    = 'Siloam Hospitals Lippo Village'
	public static String doctor_name_OpenAPI	= doctor_name
	public static String doctor_id_OpenAPI		= '9fe2baf5-ee72-41ca-8d18-eeb945c27758'
	public static String schedule_id_OpenAPI	= ''
	public static String schedule_fromtime_OpenAPI	= ''
	public static String schedule_totime_OpenAPI	= ''
	public static String schedule_appoinmentRangeTime_OpenAPI	= ''
	public static String schedule_appoinmentDate_OpenAPI	= ''
	
}
WS.callTestCase(findTestCase('Test Cases/OpenAPI/Login/LoginAlodokter'),[:], FailureHandling.STOP_ON_FAILURE)

GET_Doctor_Hospitals_Autocomplete_Specialities()
GET_Doctor_Hospitals_Detail_doctor()

GET_Hospitals_OpenAPI()
GET_Doctors_OpenAPI()
GET_TimeSlot_OpenAPI()

ValidationOpenAPIandChuckerApp()

private GET_Doctor_Hospitals_Autocomplete_Specialities() {
	//	===================== URL and Methode ====================
		String host 			= findTestData('Data Files/Staging/OpenAPI/LoginAccount/HOST/Host').getValue('HOST', 1)
		String endpoint 		= '/api/'+ Local_Global_Variable.version_for_endpoint +'/alodokter/doctor_hospitals/autocomplete_specialities.json?name='+URLEncoder.encode(Local_Global_Variable.doctor_name.toString())
		String url 				= host+endpoint
		String requestMethod 	= "GET"
		println url
		
	//	===================== Header Request =====================
		List<TestObjectProperty> parameters = new ArrayList<>()
		parameters.add(new TestObjectProperty("Content-Type", ConditionType.EQUALS, "application/json"))
		parameters.add(new TestObjectProperty("X-UID", ConditionType.EQUALS, GlobalVariable.BASE_UID))
		parameters.add(new TestObjectProperty("HTTP-X-UID", ConditionType.EQUALS, GlobalVariable.BASE_UID))
		parameters.add(new TestObjectProperty("Authorization", ConditionType.EQUALS, "Token token=" + GlobalVariable.BASE_TOKEN))
	//	====================== Body Request ======================
		String body
	//	====================== Set & hit API =====================
		ArrayList defaultHeaders = Arrays.asList(parameters)
		RequestObject ro = new RequestObject("objectId")
		ro.setRestUrl(url)
		ro.setHttpHeaderProperties(defaultHeaders)
		ro.setRestRequestMethod(requestMethod)
		if (requestMethod == 'POST' || requestMethod == 'PUT' || requestMethod == 'PATCH') {
			ro.setBodyContent(new HttpTextBodyContent(body))
			}
				
		JsonSlurper Slurper = new JsonSlurper().setType(JsonParserType.INDEX_OVERLAY)
		ResponseObject respon = WS.sendRequest(ro)
		String bodyResponse = respon.getResponseBodyContent()
		String codeResponse = respon.getStatusCode()
		def object = Slurper.parseText(bodyResponse)
	//	=================== Manage the response ==================
		println object
		println bodyResponse
		String tmp_dokterid									= object.data.id
		String tmp_hospital_schedule_id 					= object.data.hospital_schedule_id
		String tmp_doktername								= object.data.name
		println tmp_dokterid
		println tmp_hospital_schedule_id
		println Local_Global_Variable.doctor_id 			= tmp_dokterid.substring(1,25).trim()
		println Local_Global_Variable.hospital_schedule_id 	= tmp_hospital_schedule_id.substring(1,25).trim()
		println Local_Global_Variable.doctor_name 	= tmp_doktername.substring(1,23).trim()
		println "HTTP Code = " + codeResponse + " and Status = " + object.status
		if (WS.verifyMatch(codeResponse, '200', false, FailureHandling.STOP_ON_FAILURE)) {
			WS.verifyMatch(object.status, 'success', false, FailureHandling.STOP_ON_FAILURE)
		}	
}

private GET_Doctor_Hospitals_Detail_doctor() {
	String strLat = "37.4218521"
	String strLong = "-122.0841308"
	//	===================== URL and Methode ====================
		String host 			= findTestData('Data Files/Staging/OpenAPI/LoginAccount/HOST/Host').getValue('HOST', 1)
		String endpoint 		= '/api/'+ Local_Global_Variable.version_for_endpoint +'/alodokter/doctor_hospitals/detail_doctor/'+Local_Global_Variable.doctor_id+'?user_id='+GlobalVariable.BASE_UID+'&lat=37.4218521&long=-122.0841308&hospital_schedule_id='+Local_Global_Variable.hospital_schedule_id+'&loc_type=precise'
		String url 				= host+endpoint
		String requestMethod 	= "GET"
		println url
		
	//	===================== Header Request =====================
		List<TestObjectProperty> parameters = new ArrayList<>()
		parameters.add(new TestObjectProperty("Content-Type", ConditionType.EQUALS, "application/json; charset=UTF-8"))
		parameters.add(new TestObjectProperty("X-UID", ConditionType.EQUALS, GlobalVariable.BASE_UID))
		parameters.add(new TestObjectProperty("HTTP-X-UID", ConditionType.EQUALS, GlobalVariable.BASE_UID))
		parameters.add(new TestObjectProperty("Authorization", ConditionType.EQUALS, "Token token=" + GlobalVariable.BASE_TOKEN))
	//	====================== Body Request ======================
		String body
	//	====================== Set & hit API =====================
		ArrayList defaultHeaders = Arrays.asList(parameters)
		RequestObject ro = new RequestObject("objectId")
		ro.setRestUrl(url)
		ro.setHttpHeaderProperties(defaultHeaders)
		ro.setRestRequestMethod(requestMethod)
		if (requestMethod == 'POST' || requestMethod == 'PUT' || requestMethod == 'PATCH') {
			ro.setBodyContent(new HttpTextBodyContent(body))
			}
				
		JsonSlurper Slurper = new JsonSlurper().setType(JsonParserType.INDEX_OVERLAY)
		ResponseObject respon = WS.sendRequest(ro)
		String bodyResponse = respon.getResponseBodyContent()
		String codeResponse = respon.getStatusCode()
		def object = Slurper.parseText(bodyResponse)
	//	=================== Manage the response ==================
		println object
		println bodyResponse
		
		println "HTTP Code = " + codeResponse + " and Status = " + object.status
		String schedule_id, schedule_hour, schedule_date
		if (WS.verifyMatch(codeResponse, '200', false, FailureHandling.STOP_ON_FAILURE)) {	
			WS.verifyMatch(object.status, 'success', false, FailureHandling.STOP_ON_FAILURE)
			String[] ToLength = object.data.hospital_schedule.hospital_schedules.schedule_date_raw
			for (int var1 in 0..ToLength.length-1) {
				String[] GoLength = object.data.hospital_schedule.hospital_schedules.schedule_hour[var1].schedule_id
				println GoLength
				for (int var2 in 0..GoLength.length-1) {
					String[] TmpSch_id = object.data.hospital_schedule.hospital_schedules.schedule_hour[var1].schedule_id[var2]//.toString()//.split("-")
					println TmpSch_id
					schedule_date = schedule_date+","+object.data.hospital_schedule.hospital_schedules.schedule_date_raw[var1]
//					schedule_id = schedule_id+","+TmpSch_id[0]
					schedule_hour = schedule_hour+","+object.data.hospital_schedule.hospital_schedules.schedule_hour[var1].hour[var2]
					
				}
			}
			
			schedule_id = schedule_id+","+object.data.hospital_schedule.hospital_schedules.schedule_hour.schedule_id
			println Local_Global_Variable.schedule_id = schedule_id.replace("null,", "").replace("[", "").replace("]", "")
			println Local_Global_Variable.schedule_date = schedule_date.replace("null,", "")
			println Local_Global_Variable.schedule_hour = schedule_hour.replace("null,", "")
			println Local_Global_Variable.doctor_name 	= object.data.doctor_name
			println Local_Global_Variable.hospital_name 	= object.data.hospital_schedule.hospital_name
			println Local_Global_Variable.hospital_id 	= object.data.hospital_schedule.hospital_id
		}
}


private GET_Hospitals_OpenAPI() {
	//	===================== URL and Methode ====================
	String host 			= findTestData('Data Files/Staging/OpenAPI/LoginAccount/HOST/Host').getValue('HOST', 6)
	String endpoint 		= '/api/'+ Local_Global_Variable.prefix_version +'/hospitals'
	String url 				= host+endpoint
	String requestMethod 	= "GET"
	println url
	//	===================== Header Request =====================
	List<TestObjectProperty> parameters = new ArrayList<>()
	parameters.add(new TestObjectProperty("X-UID", ConditionType.EQUALS, ""))
	parameters.add(new TestObjectProperty("HTTP-X-UID", ConditionType.EQUALS, ""))
	parameters.add(new TestObjectProperty("Authorization", ConditionType.EQUALS, ""))
	parameters.add(new TestObjectProperty("Content-Type", ConditionType.EQUALS, "application/json; charset=UTF-8"))
	//	====================== Body Request ======================
	String body
	//	====================== Set & hit API =====================
	ArrayList defaultHeaders = Arrays.asList(parameters)
	RequestObject ro = new RequestObject("objectId")
	ro.setRestUrl(url)
	ro.setHttpHeaderProperties(defaultHeaders)
	ro.setRestRequestMethod(requestMethod)
	if (requestMethod == 'POST' || requestMethod == 'PUT' || requestMethod == 'PATCH') {
		ro.setBodyContent(new HttpTextBodyContent(body))
		}
			
	JsonSlurper Slurper = new JsonSlurper().setType(JsonParserType.INDEX_OVERLAY)
	ResponseObject respon = WS.sendRequest(ro)
	String bodyResponse = respon.getResponseBodyContent()
	println bodyResponse
	String codeResponse = respon.getStatusCode()
	def object = Slurper.parseText(bodyResponse)
	//	=================== Manage the response ==================
	println object
	println bodyResponse
	
//	println Local_Global_Variable.schedule_date = object.data.schedule_date_raw[1]
	println Local_Global_Variable.hospital_name_OpenAPI = object.data.name[24]

	println "HTTP Code = " + codeResponse + " and Status = " + object.status
	if (WS.verifyMatch(codeResponse, '200', false, FailureHandling.STOP_ON_FAILURE)) {
		WS.verifyMatch(object.status, 'OK', false, FailureHandling.STOP_ON_FAILURE)
	}
}

private GET_Doctors_OpenAPI() {
	//	===================== URL and Methode ====================
	String host 			= findTestData('Data Files/Staging/OpenAPI/LoginAccount/HOST/Host').getValue('HOST', 6)
	String endpoint 		= '/api/'+ Local_Global_Variable.prefix_version +'/mobile/doctors?hospitalId='+ Local_Global_Variable.hospital_id_OpenAPI +''
	String url 				= host+endpoint
	String requestMethod 	= "GET"
	println url
	//	===================== Header Request =====================
	List<TestObjectProperty> parameters = new ArrayList<>()
	parameters.add(new TestObjectProperty("X-UID", ConditionType.EQUALS, ""))
	parameters.add(new TestObjectProperty("HTTP-X-UID", ConditionType.EQUALS, ""))
	parameters.add(new TestObjectProperty("Authorization", ConditionType.EQUALS, ""))
	parameters.add(new TestObjectProperty("Content-Type", ConditionType.EQUALS, "application/json; charset=UTF-8"))
	//	====================== Body Request ======================
	String body
	//	====================== Set & hit API =====================
	ArrayList defaultHeaders = Arrays.asList(parameters)
	RequestObject ro = new RequestObject("objectId")
	ro.setRestUrl(url)
	ro.setHttpHeaderProperties(defaultHeaders)
	ro.setRestRequestMethod(requestMethod)
	if (requestMethod == 'POST' || requestMethod == 'PUT' || requestMethod == 'PATCH') {
		ro.setBodyContent(new HttpTextBodyContent(body))
		}
			
	JsonSlurper Slurper = new JsonSlurper().setType(JsonParserType.INDEX_OVERLAY)
	ResponseObject respon = WS.sendRequest(ro)
	String bodyResponse = respon.getResponseBodyContent()
	println bodyResponse
	String codeResponse = respon.getStatusCode()
	def object = Slurper.parseText(bodyResponse)
	//	=================== Manage the response ==================
	println object
	println bodyResponse
//	String tmp_doctorNameOpenAPI = object.data.name[29]
//	println Local_Global_Variable.doctor_name_OpenAPI 	= tmp_doctorNameOpenAPI.substring(4,26).trim()
	println "HTTP Code = " + codeResponse + " and Status = " + object.status
	if (WS.verifyMatch(codeResponse, '200', false, FailureHandling.STOP_ON_FAILURE)) {
		WS.verifyMatch(object.status, 'OK', false, FailureHandling.STOP_ON_FAILURE)
	}
}

private GET_TimeSlot_OpenAPI() {
	
	Date todaysDate = new Date()
	String fromDate = todaysDate.format("YYY-MM-dd")
	todaysDate = todaysDate+7
	String toDate = todaysDate.format("YYY-MM-dd")
	
	//	===================== URL and Methode ====================
	String host 			= findTestData('Data Files/Staging/OpenAPI/LoginAccount/HOST/Host').getValue('HOST', 6)
	String endpoint 		= '/api/'+ Local_Global_Variable.prefix_version +'/schedules/time-slot?isTeleconsultation=false&to='+toDate+'&hospitalId='+Local_Global_Variable.hospital_id_OpenAPI+'&doctorId='+Local_Global_Variable.doctor_id_OpenAPI+'&from='+fromDate+''
	String url 				= host+endpoint
	String requestMethod 	= "GET"
	println url
	//	===================== Header Request =====================
	List<TestObjectProperty> parameters = new ArrayList<>()
	parameters.add(new TestObjectProperty("X-UID", ConditionType.EQUALS, ""))
	parameters.add(new TestObjectProperty("HTTP-X-UID", ConditionType.EQUALS, ""))
	parameters.add(new TestObjectProperty("Authorization", ConditionType.EQUALS, ""))
	parameters.add(new TestObjectProperty("Content-Type", ConditionType.EQUALS, "application/json; charset=UTF-8"))
	//	====================== Body Request ======================
	String body
	//	====================== Set & hit API =====================
	ArrayList defaultHeaders = Arrays.asList(parameters)
	RequestObject ro = new RequestObject("objectId")
	ro.setRestUrl(url)
	ro.setHttpHeaderProperties(defaultHeaders)
	ro.setRestRequestMethod(requestMethod)
	if (requestMethod == 'POST' || requestMethod == 'PUT' || requestMethod == 'PATCH') {
		ro.setBodyContent(new HttpTextBodyContent(body))
		}
			
	JsonSlurper Slurper = new JsonSlurper().setType(JsonParserType.INDEX_OVERLAY)
	ResponseObject respon = WS.sendRequest(ro)
	String bodyResponse = respon.getResponseBodyContent()
	println bodyResponse
	String codeResponse = respon.getStatusCode()
	def object = Slurper.parseText(bodyResponse)
	//	=================== Manage the response ==================
	println object
	println bodyResponse

	String schedule_id_OpenAPI, schedule_appoinmentDate_OpenAPI, schedule_fromtime_OpenAPI
	String hospital_name_OpenAPI, doctor_name_OpenAPI, schedule_totime_OpenAPI
	println "HTTP Code = " + codeResponse + " and Status = " + object.status
	if (WS.verifyMatch(codeResponse, '200', false, FailureHandling.STOP_ON_FAILURE)) {
		WS.verifyMatch(object.status, 'OK', false, FailureHandling.STOP_ON_FAILURE)
		String[] ToLength = object.data.time_slot.hospital_id
		println ToLength
		for (int VarA in 0..ToLength.length) {
			
//			if (Local_Global_Variable.hospital_id_OpenAPI ==  object.data.time_slot.hospital_id[VarA].toString()) {
			
				String[] TmpSchOpenAPI = object.data.time_slot.schedule_id[VarA].toString().split("-")
				schedule_id_OpenAPI = schedule_id_OpenAPI+","+TmpSchOpenAPI[0]
				schedule_appoinmentDate_OpenAPI = schedule_appoinmentDate_OpenAPI+","+object.data.time_slot.appointment_date[VarA]
				schedule_fromtime_OpenAPI = schedule_fromtime_OpenAPI+","+object.data.time_slot.schedule_from_time[VarA]
				schedule_totime_OpenAPI = schedule_totime_OpenAPI+","+object.data.time_slot.schedule_to_time[VarA]
//				hospital_name_OpenAPI = hospital_name_OpenAPI+","+object.data.hospital_name[VarA]
//				doctor_name_OpenAPI = doctor_name_OpenAPI+","+object.data.name[VarA]
//			}
			
		}
		
		println Local_Global_Variable.schedule_id_OpenAPI = schedule_id_OpenAPI.replace("null,", "")//.replace("[", "").replace("]", "")
		println Local_Global_Variable.schedule_appoinmentDate_OpenAPI = schedule_appoinmentDate_OpenAPI.replace("null,", "").replace("[", "").replace("]", "")
		println Local_Global_Variable.schedule_fromtime_OpenAPI = schedule_fromtime_OpenAPI.replace("null,", "").replace("[", "").replace("]", "")
		println Local_Global_Variable.schedule_totime_OpenAPI = schedule_totime_OpenAPI.replace("null,", "").replace("[", "").replace("]", "")
//		println Local_Global_Variable.hospital_name_OpenAPI = hospital_name_OpenAPI.replace("null,", "")
//		println Local_Global_Variable.doctor_name_OpenAPI = doctor_name_OpenAPI.replace("null,", "")
	}
}


private ValidationOpenAPIandChuckerApp() {
	
	String[] TmpSchedule_id = Local_Global_Variable.schedule_id_OpenAPI.split(",")
	String[] TmpSchedule_date = Local_Global_Variable.schedule_appoinmentDate_OpenAPI.split(",")
	String[] TmpTime_from = Local_Global_Variable.schedule_fromtime_OpenAPI.split(",")
	String[] TmpTime_to = Local_Global_Variable.schedule_totime_OpenAPI.split(",")
	String strHourOAPI
	println TmpSchedule_id
	
	for (int VarA in 0..TmpTime_from.length-1) {
		strHourOAPI = strHourOAPI+","+TmpTime_from[VarA]+" - "+TmpTime_to[VarA]
	}
	String[] GetTimeOAPI = strHourOAPI.replace("null,", "").split(",")
	println GetTimeOAPI
	String[] TmpMobSchedule_id = Local_Global_Variable.schedule_id.split(",")
	String[] TmpMobSchedule_date_raw = Local_Global_Variable.schedule_date.split(",")
	String[] TmpMobHour = Local_Global_Variable.schedule_hour.split(",")
	println TmpMobSchedule_id
	println TmpMobHour
	for (int VarB in 0..TmpMobSchedule_id.length-1) {
		for (int VarC in 0..TmpSchedule_id.length-1) {
			if (TmpMobSchedule_id[VarB].trim() == TmpSchedule_id[VarC].trim()) {
				println TmpMobSchedule_date_raw[VarB]
				println TmpSchedule_date[VarC]
				WS.verifyMatch(TmpMobSchedule_id[VarB].trim(), TmpSchedule_id[VarC].trim(), false)
				WS.verifyMatch(TmpMobSchedule_date_raw[VarB].trim(), TmpSchedule_date[VarC].trim(), false)
//				WS.verifyMatch(TmpMobHour[VarB].trim(), GetTimeOAPI[VarC].trim(), false)
				break
			}
			
			
		
			if (VarC == TmpSchedule_id.length-1) {
//				CustomKeywords.'customkey.RFN_Key.forceStopComment'(TmpMobSchedule_id[VarB])
				WS.comment(TmpMobSchedule_id[VarB])
			}
		}
	}
	
}
	

