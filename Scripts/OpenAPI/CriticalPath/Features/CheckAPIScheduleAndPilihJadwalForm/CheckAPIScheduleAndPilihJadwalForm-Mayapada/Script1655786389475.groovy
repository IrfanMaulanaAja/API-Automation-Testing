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
	
	//chucker
	public static String hostMmobile = "https://staging-mapp.alodokter.com/api/v"+GlobalVariable.AlodokterVersion.toString().replace(".", "")
	
	public static String doctorName = doctorNameHospital
	public static String doctorID
	public static String doctorHospitalScheduleID
	
	public static String arrMobSchedule_id
	public static String arrMobSchedule_date
	public static String arrHour
}

//=== hospital API ===
postToken()
getHospitalSchedules()

//=== chuker mobile ===
//login proses
WS.callTestCase(findTestCase('Test Cases/OpenAPI/Login/LoginAlodokterNon'), [:])

//get mobile schedule
getSearchDoctor()
getScheduleDoctor()

//validate schedule mobile x OAPI
String[] arrSchedule_id = localGlobal.arrSchedule_id.split(",")
String[] arrSchedule_date = localGlobal.arrSchedule_date.split(",")
String[] arrTime_from = localGlobal.arrTime_from.split(",")
String[] arrTime_to = localGlobal.arrTime_to.split(",")
String strTime
for (int i in 0..arrTime_from.length-1) {
	strTime = strTime+","+arrTime_from[i]+" - "+arrTime_to[i]
}
String[] arrTime = strTime.replace("null,", "").split(",")

String[] arrMobSchedule_id = localGlobal.arrMobSchedule_id.split(",")
String[] arrSchedule_date_raw = localGlobal.arrMobSchedule_date.split(",")
String[] arrHour = localGlobal.arrHour.split(",")

for (int a in 0..arrMobSchedule_id.length-1) {
	for (int b in 0..arrSchedule_id.length-1) {
		if (arrMobSchedule_id[a].trim() == arrSchedule_id[b].trim()) {
			println arrMobSchedule_id[a]
			WS.verifyMatch(arrMobSchedule_id[a].trim(), arrSchedule_id[b].trim(), false)
			WS.verifyMatch(arrSchedule_date_raw[a].trim(), arrSchedule_date[b].trim(), false)
			WS.verifyMatch(arrHour[a].trim(), arrTime[b].trim(), false)
			break
		}
		if (b == arrSchedule_id.length-1) {
			CustomKeywords.'customkey.RFN_Key.forceStopComment'(arrMobSchedule_id[a])
		}
	}
}

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

private getScheduleDoctor() {
	String host = localGlobal.hostMmobile
	String endpoint = "/alodokter/doctor_hospitals/reservation_dates?id="+localGlobal.doctorHospitalScheduleID+"&schedule_date="
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
		String [] getLengtha = object.data.operation_hours.hours_list.schedule_id.toString().replace("[", "").replace("]", "").split(",")
		for (int i in 0..getLengtha.length-1) {
			String[] spID = getLengtha[i].split("-")
			localGlobal.arrMobSchedule_id = localGlobal.arrMobSchedule_id+","+ spID[0]
		}
		println localGlobal.arrMobSchedule_id = localGlobal.arrMobSchedule_id.replace("null,", "")
		println localGlobal.arrMobSchedule_date = object.data.operation_hours.hours_list.date.toString().replace("[", "").replace("]", "")
		println localGlobal.arrHour = object.data.operation_hours.hours_list.hour.toString().replace("[", "").replace("]", "")
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
	if (WS.verifyMatch(codeResponse, "200", false, FailureHandling.STOP_ON_FAILURE)) {
		String[] getLength = object.data.schedule_id
		for (int i in 0..getLength.length) {
			if (localGlobal.doctorIDHospital == object.data.doctor_code[i].toString()) {
				arrSchedule_id = arrSchedule_id+","+object.data.schedule_id[i]
				arrSchedule_date = arrSchedule_date+","+object.data.schedule_date[i]
				arrTime_from = arrTime_from+","+object.data.time_from[i]
				arrTime_to = arrTime_to+","+object.data.time_to[i]
			}
		}
		println localGlobal.arrSchedule_id = arrSchedule_id.replace("null,", "")
		println localGlobal.arrSchedule_date = arrSchedule_date.replace("null,", "")
		println localGlobal.arrTime_from = arrTime_from.replace("null,", "")
		println localGlobal.arrTime_to = arrTime_to.replace("null,", "")
	}
}