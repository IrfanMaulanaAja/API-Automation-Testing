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
	public static String version_for_path 		= findTestData('Data Files/OpenAPI/LoginAccount/User').getValue('VERSION', 1)
	public static String version_for_endpoint 	= 'v' + version_for_path.replace('.', '')
	public static String prefix_version			= 'v2'
	public static String doctor_name			= 'vivien+pus'
	public static String doctor_id				= '599d47704eb9d841c2dfe20e'
	public static String hospital_schedule_id	= '5ce66bf4767ef20cad929b72'
	public static String schedule_date			= ''
	public static String schedule_hour			= ''
	public static String hospital_id			= '599e31064eb9d841c2dfe615'
	public static String hospital_name			= 'Siloam Hospitals Lippo Village'
	public static String specialisty_id			= ''
	public static String doctor_speciality		= ''
	public static String booking_id				= ''
	
	public static String hospital_id_OpenAPI    = '39764039-37b9-4176-a025-ef7b2e124ba4'
	public static String hospital_name_OpenAPI    = 'Siloam Hospitals Lippo Village'
	public static String doctor_name_OpenAPI	= 'Dr. dr. Vivien Puspitasari, SpS'
	public static String doctor_id_OpenAPI		= '9fe2baf5-ee72-41ca-8d18-eeb945c27758'
	public static String schedule_id_OpenAPI	= ''
	public static String schedule_fromtime_OpenAPI	= ''
	public static String schedule_totime_OpenAPI	= ''
	
}
WS.callTestCase(findTestCase('Test Cases/OpenAPI/Login/LoginAlodokter'),[:], FailureHandling.STOP_ON_FAILURE)



//GET_Doctor_Hospitals_Autocomplete_Specialities()
//GET_Doctor_Hospitals_Detail_doctor()
//GET_Doctor_Hospitals_Resevation()

GET_Hospitals_OpenAPI()
GET_Doctors_OpenAPI()
GET_TimeSlot_OpenAPI()

//ValidationOpenAPIandChuckerApp()

private GET_Doctor_Hospitals_Autocomplete_Specialities() {
	//	===================== URL and Methode ====================
		String host 			= findTestData('Data Files/OpenAPI/LoginAccount/Host').getValue('HOST', 1)
		String endpoint 		= '/api/'+ Local_Global_Variable.version_for_endpoint +'/alodokter/doctor_hospitals/autocomplete_specialities.json?name='+ Local_Global_Variable.doctor_name
		String url 				= host+endpoint
		String requestMethod 	= "GET"
		
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
		println tmp_dokterid
		println tmp_hospital_schedule_id
		println Local_Global_Variable.doctor_id 			= tmp_dokterid.substring(1,25).trim()
		println Local_Global_Variable.hospital_schedule_id 	= tmp_hospital_schedule_id.substring(1,25).trim()
		println "HTTP Code = " + codeResponse + " and Status = " + object.status
		if (WS.verifyMatch(codeResponse, '200', false, FailureHandling.STOP_ON_FAILURE)) {
			WS.verifyMatch(object.status, 'success', false, FailureHandling.STOP_ON_FAILURE)
		}
}

private GET_Doctor_Hospitals_Detail_doctor() {
	//	===================== URL and Methode ====================
		String host 			= findTestData('Data Files/OpenAPI/LoginAccount/Host').getValue('HOST', 1)
		String endpoint 		= '/api/'+ Local_Global_Variable.version_for_endpoint +'/alodokter/doctor_hospitals/detail_doctor/'+ Local_Global_Variable.doctor_id +'?user_id='+ GlobalVariable.BASE_UID +'&lat=-7.0206997&long=108.3693598&hospital_schedule_id='+ Local_Global_Variable.hospital_schedule_id
		String url 				= host+endpoint
		String requestMethod 	= "GET"
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
		println Local_Global_Variable.hospital_id 		= object.data.hospital_schedule.hospital_id
		println Local_Global_Variable.hospital_name 	= object.data.hospital_schedule.hospital_name
		println Local_Global_Variable.specialisty_id	= object.data.speciality_id
		println Local_Global_Variable.doctor_speciality	= object.data.doctor_speciality
		println "HTTP Code = " + codeResponse + " and Status = " + object.status
		if (WS.verifyMatch(codeResponse, '200', false, FailureHandling.STOP_ON_FAILURE)) {
			WS.verifyMatch(object.status, 'success', false, FailureHandling.STOP_ON_FAILURE)
		}
}

private GET_Doctor_Hospitals_Resevation() {
	//	===================== URL and Methode ====================
		String host 			= findTestData('Data Files/OpenAPI/LoginAccount/Host').getValue('HOST', 1)
		String endpoint 		= '/api/'+ Local_Global_Variable.version_for_endpoint +'/alodokter/doctor_hospitals/reservation_dates?id='+ Local_Global_Variable.hospital_schedule_id +'&schedule_date='
		String url 				= host+endpoint
		String requestMethod 	= "GET"
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
//		println Local_Global_Variable.schedule_date = object.data.schedule_date_raw[1]
//		String tmp_schedule_hour  = object.data.operation_hours.hours_list.hour[1]
//		println Local_Global_Variable.schedule_hour = tmp_schedule_hour.substring(2,15).trim()
		println "HTTP Code = " + codeResponse + " and Status = " + object.status
		if (WS.verifyMatch(codeResponse, '200', false, FailureHandling.STOP_ON_FAILURE)) {
			WS.verifyMatch(object.status, 'success', false, FailureHandling.STOP_ON_FAILURE)
		}
}




private GET_Hospitals_OpenAPI() {
	//	===================== URL and Methode ====================
	String host 			= findTestData('Data Files/OpenAPI/CriticalPath/Host').getValue('HOST', 1)
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
	
	
	println "HTTP Code = " + codeResponse + " and Status = " + object.status
	if (WS.verifyMatch(codeResponse, '200', false, FailureHandling.STOP_ON_FAILURE)) {
		WS.verifyMatch(object.status, 'OK', false, FailureHandling.STOP_ON_FAILURE)
	}
}

private GET_Doctors_OpenAPI() {
	//	===================== URL and Methode ====================
	String host 			= findTestData('Data Files/OpenAPI/CriticalPath/Host').getValue('HOST', 1)
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
	
//	println Local_Global_Variable.doctor_name_OpenAPI = object.data.name
//	println Local_Global_Variable.doctor_id_OpenAPI = object.data.doctor_id
	
	println "HTTP Code = " + codeResponse + " and Status = " + object.status
	if (WS.verifyMatch(codeResponse, '200', false, FailureHandling.STOP_ON_FAILURE)) {
		WS.verifyMatch(object.status, 'OK', false, FailureHandling.STOP_ON_FAILURE)
	}
}


private GET_TimeSlot_OpenAPI() {
	def today = new Date()
	println ("today -- " + today.format('yyyy-MM-dd'))
	
	//	===================== URL and Methode ====================
	String host 			= findTestData('Data Files/OpenAPI/CriticalPath/Host').getValue('HOST', 1)
	String endpoint 		= '/api/'+ Local_Global_Variable.prefix_version +'/schedules/time-slot?isTeleconsultation=false&to='+"2022-10-30"+'&hospitalId='+Local_Global_Variable.hospital_id_OpenAPI+'&doctorId='+Local_Global_Variable.doctor_id_OpenAPI+'&from='+today.format('yyyy-MM-dd')+''
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
	
	println Local_Global_Variable.schedule_fromtime_OpenAPI = object.data.schedule_from_time[2]
	println Local_Global_Variable.schedule_totime_OpenAPI = object.data.schedule_to_time[2]
	
	println "HTTP Code = " + codeResponse + " and Status = " + object.status
	if (WS.verifyMatch(codeResponse, '200', false, FailureHandling.STOP_ON_FAILURE)) {
		WS.verifyMatch(object.status, 'OK', false, FailureHandling.STOP_ON_FAILURE)
	}
}

private ValidationOpenAPIandChuckerApp() {
	String timefrom = "00:20"
	String timeto = "10:30"
	String HoursOpenAPI = timefrom+"-"+timeto
	
	
	println HoursOpenAPI
}
	

