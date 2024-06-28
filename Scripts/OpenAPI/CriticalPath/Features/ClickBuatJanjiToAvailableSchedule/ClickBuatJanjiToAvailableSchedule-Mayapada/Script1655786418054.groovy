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

public class localGlobal {
	//OAPI
	public static String hostHospital = "https://middleware.mayapadahospital.com/uat"
	public static String doctorIDHospital = "MHJSA06326"
	public static String doctorNameHospital = "dr. Wisanti Zarwin, Sp.THT-KL"
	public static String tokenHospital
	
	public static String arrSchedule_id
	public static String arrSchedule_date
	public static String arrTime_from
	public static String arrTime_to
	public static String arrHospital_name
	public static String arrDoctor_name
	
	//chucker
	public static String hostMmobile = "https://staging-mapp.alodokter.com/api/v"+GlobalVariable.AlodokterVersion.toString().replace(".", "")
	
	public static String doctorName = doctorNameHospital
	public static String doctorID
	public static String doctorHospitalScheduleID
	
	public static String arrMobSchedule_id
	public static String arrSchedule_date_raw
	public static String arrHour
	public static String arrMobHospital_name
	public static String arrMobDoctor_name
	
	public static String strSchedule_date_raw
	public static String strHour
}

//=== hospital API ===
postToken()
getHospitalSchedules()

//=== chuker mobile ===
//login proses
WS.callTestCase(findTestCase('Test Cases/OpenAPI/Login/LoginAlodokterNon'), [:])

//get mobile schedule
getSearchDoctor()
getDetailDoctor()

//validate schedule mobile x OAPI
String[] arrSchedule_id = localGlobal.arrSchedule_id.split(",")
String[] arrSchedule_date = localGlobal.arrSchedule_date.split(",")
String[] arrTime_from = localGlobal.arrTime_from.split(",")
String[] arrTime_to = localGlobal.arrTime_to.split(",")
String[] arrHospital_name = localGlobal.arrHospital_name.split(",")
String[] arrDoctor_name = localGlobal.arrDoctor_name.split(";")
String strTime
for (int i in 0..arrTime_from.length-1) {
	strTime = strTime+","+arrTime_from[i]+" - "+arrTime_to[i]
}
String[] arrTime = strTime.replace("null,", "").split(",")

String[] arrMobSchedule_id = localGlobal.arrMobSchedule_id.split(",")
String[] arrSchedule_date_raw = localGlobal.arrSchedule_date_raw.split(",")
String[] arrHour = localGlobal.arrHour.split(",")

localGlobal.strSchedule_date_raw = arrSchedule_date_raw[0]
localGlobal.strHour = arrHour[0]

for (int a in 0..arrMobSchedule_id.length-1) {
	for (int b in 0..arrSchedule_id.length-1) {
		if (arrMobSchedule_id[a].trim() == arrSchedule_id[b].trim()) {
			println arrMobSchedule_id[a]
			WS.verifyMatch(arrMobSchedule_id[a].trim(), arrSchedule_id[b].trim(), false)
			WS.verifyMatch(arrSchedule_date_raw[a].trim(), arrSchedule_date[b].trim(), false)
			WS.verifyMatch(arrHour[a].trim(), arrTime[b].trim(), false)
			WS.verifyMatch(localGlobal.arrMobHospital_name.trim(), arrHospital_name[b].trim(), false)
			WS.verifyMatch(localGlobal.arrMobDoctor_name.trim(), arrDoctor_name[b].trim(), false)
			break
		}
		if (b == arrSchedule_id.length-1) {
			CustomKeywords.'customkey.RFN_Key.forceStopComment'(arrMobSchedule_id[a])
		}
	}
}

postDoctorSchedule()

//=== booking group ===
private getSearchDoctor() {
	String host = localGlobal.hostMmobile
	String endpoint = "/alodokter/doctor_hospitals/autocomplete_specialities.json?name="+URLEncoder.encode(localGlobal.doctorName.toString())
	String url = host+endpoint
	
	//	===================== Header Request =====================
	List<TestObjectProperty> parameters = new ArrayList<>()
	parameters.add(new TestObjectProperty("X-UID", ConditionType.EQUALS, GlobalVariable.BASE_UID))
	parameters.add(new TestObjectProperty("HTTP-X-UID", ConditionType.EQUALS, GlobalVariable.BASE_UID))
	parameters.add(new TestObjectProperty("Authorization", ConditionType.EQUALS, "Token token="+ GlobalVariable.BASE_TOKEN))
	parameters.add(new TestObjectProperty("Content-Type", ConditionType.EQUALS, "application/json; charset=UTF-8"))
	//	====================== Set & hit API =====================
	ArrayList defaultHeaders = Arrays.asList(parameters)
	RequestObject ro = new RequestObject("objectId")
	ro.setRestUrl(url)
	ro.setHttpHeaderProperties(defaultHeaders)
	ro.setRestRequestMethod("GET")
	
	JsonSlurper Slurper = new JsonSlurper().setType(JsonParserType.INDEX_OVERLAY) //memanggil slurper
	ResponseObject respon = WS.sendRequest(ro)
	String rescode = respon.getStatusCode()
	WS.comment(rescode)
	String bodyResponse = respon.getResponseBodyContent()
	WS.comment(bodyResponse)
	if (WS.verifyMatch(rescode, '200', false, FailureHandling.STOP_ON_FAILURE)) {
		def object = Slurper.parseText(bodyResponse)
		WS.verifyMatch(object.status.toString(), 'success', false, FailureHandling.STOP_ON_FAILURE)
		localGlobal.doctorID = object.data.id[0]
		localGlobal.doctorHospitalScheduleID = object.data.hospital_schedule_id[0]
	}
}

private getDetailDoctor() {
	String host = localGlobal.hostMmobile
	String strLat = "37.4218521"
	String strLong = "-122.0841308"
	String endpoint = "/alodokter/doctor_hospitals/detail_doctor/"+localGlobal.doctorID+"?user_id="+GlobalVariable.BASE_UID+"&lat="+strLat+"&long="+strLong+"&hospital_schedule_id="+localGlobal.doctorHospitalScheduleID+"&loc_type=precise"
	String url = host+endpoint
	
	//	===================== Header Request =====================
	List<TestObjectProperty> parameters = new ArrayList<>()
	parameters.add(new TestObjectProperty("X-UID", ConditionType.EQUALS, GlobalVariable.BASE_UID))
	parameters.add(new TestObjectProperty("HTTP-X-UID", ConditionType.EQUALS, GlobalVariable.BASE_UID))
	parameters.add(new TestObjectProperty("Authorization", ConditionType.EQUALS, "Token token="+ GlobalVariable.BASE_TOKEN))
	parameters.add(new TestObjectProperty("Content-Type", ConditionType.EQUALS, "application/json; charset=UTF-8"))
	//	====================== Set & hit API =====================
	ArrayList defaultHeaders = Arrays.asList(parameters)
	RequestObject ro = new RequestObject("objectId")
	ro.setRestUrl(url)
	ro.setHttpHeaderProperties(defaultHeaders)
	ro.setRestRequestMethod("GET")
	
	JsonSlurper Slurper = new JsonSlurper().setType(JsonParserType.INDEX_OVERLAY) //memanggil slurper
	ResponseObject respon = WS.sendRequest(ro)
	String rescode = respon.getStatusCode()
	WS.comment(rescode)
	String bodyResponse = respon.getResponseBodyContent()
	WS.comment(bodyResponse)
	
	String arrMobSchedule_id
	String arrHour
	String arrSchedule_date_raw
	if (WS.verifyMatch(rescode, '200', false, FailureHandling.STOP_ON_FAILURE)) {
		def object = Slurper.parseText(bodyResponse)
		WS.verifyMatch(object.status.toString(), 'success', false, FailureHandling.STOP_ON_FAILURE)
		String[] getLength = object.data.hospital_schedule.hospital_schedules.schedule_date_raw
		for (int a in 0..getLength.length-1) {
			String[] getLengtha = object.data.hospital_schedule.hospital_schedules.schedule_hour[a].schedule_id
			for (int i in 0..getLengtha.length-1) {
				String[] splSchedule_id = object.data.hospital_schedule.hospital_schedules.schedule_hour[a].schedule_id[i].toString().split("-")
				arrSchedule_date_raw = arrSchedule_date_raw+","+object.data.hospital_schedule.hospital_schedules.schedule_date_raw[a]
				arrMobSchedule_id = arrMobSchedule_id+","+splSchedule_id[0]
				arrHour = arrHour+","+object.data.hospital_schedule.hospital_schedules.schedule_hour[a].hour[i]
			}
		}
		println localGlobal.arrMobSchedule_id = arrMobSchedule_id.replace("null,", "")
		println localGlobal.arrSchedule_date_raw = arrSchedule_date_raw.replace("null,", "")
		println localGlobal.arrHour = arrHour.replace("null,", "")
		println localGlobal.arrMobHospital_name = object.data.hospital_schedule.hospital_name
		println localGlobal.arrMobDoctor_name = object.data.doctor_name
	}
}

private postDoctorSchedule() {
	String host = localGlobal.hostMmobile
	String endpoint = "/alodokter/doctor_hospitals/oapi_schedule_validation"
	String url = host+endpoint
	
	String body =
	"""
	{
		"hospital_schedule_id": "${localGlobal.doctorHospitalScheduleID}",
		"schedule_date": "${localGlobal.strSchedule_date_raw}",
		"schedule_hour": "${localGlobal.strHour}"
	}
	"""
	WS.comment(body)
	
	//	===================== Header Request =====================
	List<TestObjectProperty> parameters = new ArrayList<>()
	parameters.add(new TestObjectProperty("X-UID", ConditionType.EQUALS, GlobalVariable.BASE_UID))
	parameters.add(new TestObjectProperty("HTTP-X-UID", ConditionType.EQUALS, GlobalVariable.BASE_UID))
	parameters.add(new TestObjectProperty("Authorization", ConditionType.EQUALS, "Token token="+ GlobalVariable.BASE_TOKEN))
	parameters.add(new TestObjectProperty("Content-Type", ConditionType.EQUALS, "application/json; charset=UTF-8"))
	//	====================== Set & hit API =====================
	ArrayList defaultHeaders = Arrays.asList(parameters)
	RequestObject ro = new RequestObject("objectId")
	ro.setRestUrl(url)
	ro.setHttpHeaderProperties(defaultHeaders)
	ro.setRestRequestMethod("POST")
	ro.setBodyContent(new HttpTextBodyContent(body))
	
	JsonSlurper Slurper = new JsonSlurper().setType(JsonParserType.INDEX_OVERLAY) //memanggil slurper
	ResponseObject respon = WS.sendRequest(ro)
	String rescode = respon.getStatusCode()
	WS.comment(rescode)
	String bodyResponse = respon.getResponseBodyContent()
	WS.comment(bodyResponse)
	
	if (WS.verifyMatch(rescode, '200', false, FailureHandling.STOP_ON_FAILURE)) {
		def object = Slurper.parseText(bodyResponse)
		WS.verifyMatch(object.status.toString(), 'success', false, FailureHandling.STOP_ON_FAILURE)
		WS.verifyMatch(object.data.is_oapi.toString(), 'true', false, FailureHandling.STOP_ON_FAILURE)	
	}
}

//=== OAPI group ===
private postToken() {
	//	===================== URL and Methode ====================
	String host 			= localGlobal.hostHospital
	String endpoint 		= "/backend/account/pub_login"
	String url 				= host+endpoint
	String requestMethod 	= "POST"
	String auth  			= "Basic eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
	//	===================== Header Request =====================
	List<TestObjectProperty> parameters = new ArrayList<>()
	parameters.add(new TestObjectProperty("Authorization", ConditionType.EQUALS, auth))
	parameters.add(new TestObjectProperty("Content-Type", ConditionType.EQUALS, "multipart/form-data"))
	//	====================== Body Request ======================
	String strUsername		= "alodokter"
	String strPassword		= "12345678"
	
	List body = new ArrayList()
	FormDataBodyParameter Username = new FormDataBodyParameter("Username",strUsername,"Text")
	FormDataBodyParameter Password = new FormDataBodyParameter("Password",strPassword,"Text")
	body.add(Username)
	body.add(Password)
	HttpFormDataBodyContent bodyContent = new HttpFormDataBodyContent(body)
	
	//	String body =
	//	"""
	//	{
	//		"Username": "${strUsername}",
	//		"Password": "${strPassword}"
	//	}
	//	"""
	
	//	body = '{"Username": "'+strUsername+'","Password": "'+strPassword+'"}'
	//	println body
	//	====================== Set & hit API =====================
	ArrayList defaultHeaders = Arrays.asList(parameters)
	RequestObject ro = new RequestObject("objectId")
	ro.setRestUrl(url)
	ro.setHttpHeaderProperties(defaultHeaders)
	ro.setRestRequestMethod(requestMethod)
	if (requestMethod == "POST" || requestMethod == "PUT" || requestMethod == "PATCH") {
//		ro.setBodyContent(new HttpTextBodyContent(body))
		ro.setBodyContent(new HttpFormDataBodyContent(body))
	}
			
	JsonSlurper Slurper = new JsonSlurper().setType(JsonParserType.INDEX_OVERLAY)
	ResponseObject respon = WS.sendRequest(ro)
	String bodyResponse = respon.getResponseBodyContent()
	String codeResponse = respon.getStatusCode()
	def object = Slurper.parseText(bodyResponse)
	//	=================== Manage the response ==================
	println object
	//	println "HTTP Code = " + codeResponse + " and Status = " + object.status
	if (WS.verifyMatch(codeResponse, "200", false, FailureHandling.STOP_ON_FAILURE)) {
		localGlobal.tokenHospital = "Bearer "+object.data.Bearer
	}
}

private getHospital() {
	//	===================== URL and Methode ====================
	String host 			= localGlobal.hostHospital
	String endpoint 		= "/master/hospitalunit/refhospitalunit"
	String url 				= host+endpoint
	String requestMethod 	= "GET"
//	===================== Header Request =====================
	List<TestObjectProperty> parameters = new ArrayList<>()
	parameters.add(new TestObjectProperty("Authorization", ConditionType.EQUALS, localGlobal.tokenHospital))
//	====================== Body Request ======================
	String body
//	====================== Set & hit API =====================
	ArrayList defaultHeaders = Arrays.asList(parameters)
	RequestObject ro = new RequestObject("objectId")
	ro.setRestUrl(url)
	ro.setHttpHeaderProperties(defaultHeaders)
	ro.setRestRequestMethod(requestMethod) 
	if (requestMethod == "POST" || requestMethod == "PUT" || requestMethod == "PATCH") {
		ro.setBodyContent(new HttpTextBodyContent(body))
	}
			
	JsonSlurper Slurper = new JsonSlurper().setType(JsonParserType.INDEX_OVERLAY) 
	ResponseObject respon = WS.sendRequest(ro) 
	String bodyResponse = respon.getResponseBodyContent() 
	String codeResponse = respon.getStatusCode() 
	def object = Slurper.parseText(bodyResponse) 
//	=================== Manage the response ==================
	println object
	if (WS.verifyMatch(codeResponse, "200", false, FailureHandling.STOP_ON_FAILURE)) {
		
	}
}

private getHospitalDoctors() {
	//	===================== URL and Methode ====================
	String host 			= localGlobal.hostHospital
	String endpoint 		= "/master/doctor/search_doctor_unit_get?hospital_id=1"
	String url 				= host+endpoint
	String requestMethod 	= "GET"
//	===================== Header Request =====================
	List<TestObjectProperty> parameters = new ArrayList<>()
	parameters.add(new TestObjectProperty("Authorization", ConditionType.EQUALS, localGlobal.tokenHospital))
//	====================== Body Request ======================
	String body
//	====================== Set & hit API =====================
	ArrayList defaultHeaders = Arrays.asList(parameters)
	RequestObject ro = new RequestObject("objectId")
	ro.setRestUrl(url)
	ro.setHttpHeaderProperties(defaultHeaders)
	ro.setRestRequestMethod(requestMethod)
	if (requestMethod == "POST" || requestMethod == "PUT" || requestMethod == "PATCH") {
		ro.setBodyContent(new HttpTextBodyContent(body))
	}
			
	JsonSlurper Slurper = new JsonSlurper().setType(JsonParserType.INDEX_OVERLAY)
	ResponseObject respon = WS.sendRequest(ro)
	String bodyResponse = respon.getResponseBodyContent()
	String codeResponse = respon.getStatusCode()
	def object = Slurper.parseText(bodyResponse)
//	=================== Manage the response ==================
	println object
	if (WS.verifyMatch(codeResponse, "200", false, FailureHandling.STOP_ON_FAILURE)) {
		
	}
}

private getHospitalSchedules() {
	//	===================== URL and Methode ====================
	Date todaysDate = new Date()
	String fromDate = todaysDate.format("YYY-MM-dd")
	todaysDate = todaysDate+7
	String toDate = todaysDate.format("YYY-MM-dd")
	
	String host 			= localGlobal.hostHospital
	String endpoint 		= "/publics/doctor/schedule_doctor_alodoc?to="+toDate+"&hospital_id=1&from="+fromDate///publics/doctor/schedule_doctor_alodoc?to=2022-06-22&hospital_id=2&from=2022-06-15
	String url 				= host+endpoint
	String requestMethod 	= "GET"
//	===================== Header Request =====================
	List<TestObjectProperty> parameters = new ArrayList<>()
	parameters.add(new TestObjectProperty("Authorization", ConditionType.EQUALS, localGlobal.tokenHospital))
//	====================== Body Request ======================
	String body
//	====================== Set & hit API =====================
	ArrayList defaultHeaders = Arrays.asList(parameters)
	RequestObject ro = new RequestObject("objectId")
	ro.setRestUrl(url)
	ro.setHttpHeaderProperties(defaultHeaders)
	ro.setRestRequestMethod(requestMethod)
	if (requestMethod == "POST" || requestMethod == "PUT" || requestMethod == "PATCH") {
		ro.setBodyContent(new HttpTextBodyContent(body))
	}

	JsonSlurper Slurper = new JsonSlurper().setType(JsonParserType.INDEX_OVERLAY)
	ResponseObject respon = WS.sendRequest(ro)
	String bodyResponse = respon.getResponseBodyContent()
	String codeResponse = respon.getStatusCode()
	def object = Slurper.parseText(bodyResponse)
//	=================== Manage the response ==================
	println object
	String arrSchedule_id
	String arrSchedule_date
	String arrTime_from
	String arrTime_to
	String arrHospital_name
	String arrDoctor_name
	if (WS.verifyMatch(codeResponse, "200", false, FailureHandling.STOP_ON_FAILURE)) {
		String[] getLength = object.data.schedule_id
		for (int i in 0..getLength.length) {
			if (localGlobal.doctorIDHospital == object.data.doctor_code[i].toString()) {
				arrSchedule_id = arrSchedule_id+","+object.data.schedule_id[i]
				arrSchedule_date = arrSchedule_date+","+object.data.schedule_date[i]
				arrTime_from = arrTime_from+","+object.data.time_from[i]
				arrTime_to = arrTime_to+","+object.data.time_to[i]
				arrHospital_name = arrHospital_name+","+object.data.hospital_name[i]
				arrDoctor_name = arrDoctor_name+";"+object.data.doctor_name[i]
			}
		}
		println localGlobal.arrSchedule_id = arrSchedule_id.replace("null,", "")
		println localGlobal.arrSchedule_date = arrSchedule_date.replace("null,", "")
		println localGlobal.arrTime_from = arrTime_from.replace("null,", "")
		println localGlobal.arrTime_to = arrTime_to.replace("null,", "")
		println localGlobal.arrHospital_name = arrHospital_name.replace("null,", "")
		println localGlobal.arrDoctor_name = arrDoctor_name.replace("null;", "")
	}
}