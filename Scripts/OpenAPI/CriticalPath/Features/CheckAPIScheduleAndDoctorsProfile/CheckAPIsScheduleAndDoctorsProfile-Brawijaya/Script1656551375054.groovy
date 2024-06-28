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
import com.kms.katalon.core.testobject.FormDataBodyParameter
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

public class Local_Global_Variable {
	public static String version_for_path 		= findTestData('Data Files/Staging/OpenAPI/LoginAccount/Alodokter/User').getValue('VERSION', 1)
	public static String version_for_endpoint 	= 'v' + version_for_path.replace('.', '')
	public static String prefix_version			= 'v2'
	public static String doctor_name			= 'Harsha Aulia'
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
	
	public static String hospital_id_OpenAPI    = '19'
	public static String hospital_name_OpenAPI    = ''
	public static String doctor_name_OpenAPI	= doctor_name
	public static String doctor_id_OpenAPI		= ''
	public static String schedule_id_OpenAPI	= ''
	public static String schedule_fromtime_OpenAPI	= ''
	public static String schedule_totime_OpenAPI	= ''
	public static String schedule_appoinmentRangeTime_OpenAPI	= ''
	public static String schedule_appoinmentDate_OpenAPI	= ''
	public static String user_name_OpenAPI	= findTestData('Data Files/Staging/OpenAPI/LoginAccount/OAPI/User').getValue('EMAIL', 1)
	public static String password_OpenAPI	= findTestData('Data Files/Staging/OpenAPI/LoginAccount/OAPI/User').getValue('PASSWORD', 1)
	public static String token_OpenAPI	= ''
	public static String pid_OpenAPI	= '4517'
	public static String starthour_OpenAPI	= ''
	public static String startminute_OpenAPI	= ''
	public static String endhour_OpenAPI	= ''
	public static String endminute_OpenAPI	= ''
	public static String weekday_OpenAPI	= ''
}

WS.callTestCase(findTestCase('Test Cases/OpenAPI/Login/LoginAlodokter'),[:], FailureHandling.STOP_ON_FAILURE)


GET_Doctor_Hospitals_Autocomplete_Specialities()
GET_Doctor_Hospitals_Detail_doctor()

POST_LoginOpenAPI()
GET_RS_OpenAPI()
GET_Doctors_OpenAPI()
GET_ScheduleByPID_OpenAPI()

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
	String strLat = "37.4219141"
	String strLong = "-122.0840414"
	//	===================== URL and Methode ====================
		String host 			= findTestData('Data Files/Staging/OpenAPI/LoginAccount/HOST/Host').getValue('HOST', 1)
		String endpoint 		= '/api/'+ Local_Global_Variable.version_for_endpoint +'/alodokter/doctor_hospitals/detail_doctor/'+Local_Global_Variable.doctor_id+'?user_id='+GlobalVariable.BASE_UID+'&lat='+strLat+'='+strLong+'&hospital_schedule_id='+Local_Global_Variable.hospital_schedule_id+'&loc_type=precise'
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
				
				for (int var2 in 0..GoLength.length-1) {
					String[] TmpSch_id = object.data.hospital_schedule.hospital_schedules.schedule_hour[var1].schedule_id[var2].toString().split("-")
					println TmpSch_id
					schedule_date = schedule_date+","+object.data.hospital_schedule.hospital_schedules.schedule_date_raw[var1]
					schedule_id = schedule_id+","+TmpSch_id[0]
					schedule_hour = schedule_hour+","+object.data.hospital_schedule.hospital_schedules.schedule_hour[var1].hour[var2]
					
				}
			}
			
			println Local_Global_Variable.schedule_id = schedule_id.replace("null,", "")
			println Local_Global_Variable.schedule_date = schedule_date.replace("null,", "")
			println Local_Global_Variable.schedule_hour = schedule_hour.replace("null,", "")
			println Local_Global_Variable.hospital_name 	= object.data.hospital_schedule.hospital_name
			println Local_Global_Variable.doctor_name 	= object.data.doctor_name
			println Local_Global_Variable.hospital_schedule_id = object.data.hospital_schedule.hospital_schedule_id.replace("null,", "")
		}
}


private POST_LoginOpenAPI() {
	//	===================== URL and Methode ====================
	String host 			= findTestData('Data Files/Staging/OpenAPI/LoginAccount/HOST/Host').getValue('HOST', 7)
	String endpoint 		= '/api/'+ 'v1' +'/login'
	String url 				= "https://dev-api-webservice.teramobile.app/api/v1/login"//host+endpoint
	String requestMethod 	= "POST"
	println url
	
	//	===================== Header Request =====================
	List<TestObjectProperty> parameters = new ArrayList<>()
	parameters.add(new TestObjectProperty("Content-Type", ConditionType.EQUALS, "multipart/form-data"))
	//	====================== Body Request ======================
	String strusername = "alodokter_dev"
	String strpassword = "KrnZ88KKyxzF"
	
	List body = new ArrayList()
	FormDataBodyParameter logusername = new FormDataBodyParameter("username",strusername,"Text")
	FormDataBodyParameter logpassword = new FormDataBodyParameter("password",strpassword,"Text")
	body.add(logusername)
	body.add(logpassword)
	HttpFormDataBodyContent bodyContent = new HttpFormDataBodyContent(body)
	
		
	//	====================== Set & hit API =====================
	ArrayList defaultHeaders = Arrays.asList(parameters)
	RequestObject ro = new RequestObject("objectId")
	ro.setRestUrl(url)
	ro.setHttpHeaderProperties(defaultHeaders)
	ro.setRestRequestMethod(requestMethod)
	if (requestMethod == 'POST' || requestMethod == 'PUT' || requestMethod == 'PATCH') {
//		ro.setBodyContent(new HttpTextBodyContent(bodyContent))
		ro.setBodyContent(new HttpFormDataBodyContent(body))
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
	
	println "HTTP Code = " + codeResponse + " and Status = " + object.status
	if (WS.verifyMatch(codeResponse, '200', false, FailureHandling.STOP_ON_FAILURE)) {
		println Local_Global_Variable.token_OpenAPI = 'Bearer '+object.token
	}
}

private GET_RS_OpenAPI() {
	//	===================== URL and Methode ====================
	String host 			= findTestData('Data Files/Staging/OpenAPI/LoginAccount/HOST/Host').getValue('HOST', 7)
	String endpoint 		= '/api/'+ 'v1' +'/rs'
	String url 				= host+endpoint
	String requestMethod 	= "GET"
	println url
	//	===================== Header Request =====================
	List<TestObjectProperty> parameters = new ArrayList<>()
	parameters.add(new TestObjectProperty("X-UID", ConditionType.EQUALS, ""))
	parameters.add(new TestObjectProperty("HTTP-X-UID", ConditionType.EQUALS, ""))
	parameters.add(new TestObjectProperty("Authorization", ConditionType.EQUALS, Local_Global_Variable.token_OpenAPI))
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
	
	println Local_Global_Variable.hospital_name_OpenAPI = object.nama

	println "HTTP Code = " + codeResponse + " and Status = " + object.Status
	if (WS.verifyMatch(codeResponse, '200', false, FailureHandling.STOP_ON_FAILURE)) {
//		WS.verifyMatch(object.Status, '200 OK', false, FailureHandling.STOP_ON_FAILURE)
	}
}

private GET_Doctors_OpenAPI() {
	//	===================== URL and Methode ====================
	String host 			= findTestData('Data Files/Staging/OpenAPI/LoginAccount/HOST/Host').getValue('HOST', 7)
	String endpoint 		= '/api/'+ 'v1' +'/doctors?page=1'
	String url 				= host+endpoint
	String requestMethod 	= "GET"
	println url
	//	===================== Header Request =====================
	List<TestObjectProperty> parameters = new ArrayList<>()
	parameters.add(new TestObjectProperty("Authorization", ConditionType.EQUALS, Local_Global_Variable.token_OpenAPI))
	parameters.add(new TestObjectProperty("Content-Type", ConditionType.EQUALS, "application/json; charset=UTF-8"))
	println Local_Global_Variable.token_OpenAPI
	//	====================== Body Request ======================
	String body = 
	"""{

		"group": "${"true"}",
		"rsid": "${Local_Global_Variable.hospital_id_OpenAPI}"
	
	}"""
	//	====================== Set & hit API =====================
	ArrayList defaultHeaders = Arrays.asList(parameters)
	RequestObject ro = new RequestObject("objectId")
	ro.setRestUrl(url)
	ro.setHttpHeaderProperties(defaultHeaders)
	ro.setRestRequestMethod(requestMethod)
	if (requestMethod == 'GET' || requestMethod == 'PUT' || requestMethod == 'PATCH') {
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
//	println Local_Global_Variable.pid_OpenAPI 	= object.data.pid
	println "HTTP Code = " + codeResponse + " and Status = " + object.Status
	if (WS.verifyMatch(codeResponse, '200', false, FailureHandling.STOP_ON_FAILURE)) {
//		WS.verifyMatch(object.Status, '200 OK', false, FailureHandling.STOP_ON_FAILURE)
	}
}

private GET_ScheduleByPID_OpenAPI() {
	
	//	===================== URL and Methode ====================
	String host 			= findTestData('Data Files/Staging/OpenAPI/LoginAccount/HOST/Host').getValue('HOST', 7)
	String endpoint 		= '/api/'+ 'v1' +'/schedulebyPid/'+Local_Global_Variable.pid_OpenAPI
	String url 				= host+endpoint
	String requestMethod 	= "GET"
	println url
	//	===================== Header Request =====================
	List<TestObjectProperty> parameters = new ArrayList<>()
	parameters.add(new TestObjectProperty("X-UID", ConditionType.EQUALS, ""))
	parameters.add(new TestObjectProperty("HTTP-X-UID", ConditionType.EQUALS, ""))
	parameters.add(new TestObjectProperty("Authorization", ConditionType.EQUALS, Local_Global_Variable.token_OpenAPI))
	parameters.add(new TestObjectProperty("Content-Type", ConditionType.EQUALS, "application/json; charset=UTF-8"))
	//	====================== Body Request ======================
	String body = 
	"""{

		"group": "${"true"}",
		"rsid": "${Local_Global_Variable.hospital_id_OpenAPI}"
	
	}"""
	//	====================== Set & hit API =====================
	ArrayList defaultHeaders = Arrays.asList(parameters)
	RequestObject ro = new RequestObject("objectId")
	ro.setRestUrl(url)
	ro.setHttpHeaderProperties(defaultHeaders)
	ro.setRestRequestMethod(requestMethod)
	if (requestMethod == 'GET' || requestMethod == 'PUT' || requestMethod == 'PATCH') {
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
	
	String start_hour_OpenAPI, start_minute_OpenAPI, end_hour_OpenAPI, end_minute_OpenAPI, weekday_OpenAPI
	
	println "HTTP Code = " + codeResponse + " and Status = " + object.status
	if (WS.verifyMatch(codeResponse, '200', false, FailureHandling.STOP_ON_FAILURE)) {
//		WS.verifyMatch(object.status, 'OK', false, FailureHandling.STOP_ON_FAILURE)
		String[] ToLength = object.jadwal.weekday
		println ToLength
		for (int VarA in 0..ToLength.length) {
				
				String[] tmpweekday = object.jadwal.weekday.toString()
				weekday_OpenAPI = weekday_OpenAPI+","+tmpweekday
				start_hour_OpenAPI = start_hour_OpenAPI+","+object.jadwal.start_hour[VarA]
				start_minute_OpenAPI = start_minute_OpenAPI+","+object.jadwal.start_minute[VarA]
				end_hour_OpenAPI = end_hour_OpenAPI+","+object.jadwal.end_hour[VarA]
				end_minute_OpenAPI = end_minute_OpenAPI+","+object.jadwal.end_minute[VarA]
				
		}		

				println Local_Global_Variable.weekday_OpenAPI = weekday_OpenAPI.replace("null,", "").replace("[", "").replace("]", "")
				println Local_Global_Variable.starthour_OpenAPI = start_hour_OpenAPI.replace("null,", "").replace("[", "").replace("]", "").replace(",null", "")
				println Local_Global_Variable.startminute_OpenAPI = start_minute_OpenAPI.replace("null,", "").replace("[", "").replace("]", "").replace(",null", "")
				println Local_Global_Variable.endhour_OpenAPI = end_hour_OpenAPI.replace("null,", "").replace("[", "").replace("]", "").replace(",null", "")
				println Local_Global_Variable.endminute_OpenAPI = end_minute_OpenAPI.replace("null,", "").replace("[", "").replace("]", "").replace(",null", "")
	}
}


private ValidationOpenAPIandChuckerApp() {
	
	String[] Tmpweekday = Local_Global_Variable.weekday_OpenAPI.split(",")
	String[] Tmpstarthour = Local_Global_Variable.starthour_OpenAPI.split(",")
	String[] Tmpstartminute = Local_Global_Variable.startminute_OpenAPI.split(",")
	String[] Tmpendhour = Local_Global_Variable.endhour_OpenAPI.split(",")
	String[] Tmpendminute = Local_Global_Variable.endminute_OpenAPI.split(",")
	int firstdigit = 0
	String starthour, startminute, endhour, endminute
	String strHourOAPI
	println Tmpstarthour
	println Tmpstartminute
	println Tmpendhour
	println Tmpendminute
	
	
	
	for (int VarA in 0..Tmpstarthour.length-1) {
					
					if (Tmpstarthour[VarA].size() == 1) {
						Tmpstarthour[VarA] = firstdigit+Tmpstarthour[VarA]
					}
					
					if (Tmpstartminute[VarA].size() == 1) {
						Tmpstartminute[VarA] = firstdigit+Tmpstartminute[VarA]
					}
					
					if (Tmpendhour[VarA].size() == 1) {
						Tmpendhour[VarA] = firstdigit+Tmpendhour[VarA]
					}
					
					if (Tmpendminute[VarA].size() == 1) {
						Tmpendminute[VarA] = firstdigit+Tmpendminute[VarA]
					}
					
					strHourOAPI = strHourOAPI+","+Tmpstarthour[VarA]+":"+Tmpstartminute[VarA]+"-"+Tmpendhour[VarA]+":"+Tmpendminute[VarA]
			
	}
	
	String[] GetTimeOAPI = strHourOAPI.replace("null,", "").split(",")
	
	String[] TmpMobSchedule_id = Local_Global_Variable.schedule_id.split(",")
	String[] TmpMobSchedule_date_raw = Local_Global_Variable.schedule_date.split(",")
	String[] TmpMobHour = Local_Global_Variable.schedule_hour.split(",")

	for (int VarB in 0..TmpMobHour.length-1) {
		for (int VarC in 0..GetTimeOAPI.length-1) {
			if (TmpMobHour[VarB].trim() == GetTimeOAPI[VarC].trim()) {
				println TmpMobHour[VarB]
				println GetTimeOAPI[VarC]
				WS.verifyMatch(TmpMobHour[VarB].trim(), GetTimeOAPI[VarC].trim(), false)
				break
			}
		
			if (VarC == GetTimeOAPI.length-1) {
//				CustomKeywords.'customkey.RFN_Key.forceStopComment'(TmpMobSchedule_id[VarB])
				WS.comment(TmpMobHour[VarB])
			}
		}
	}
	
}
	

