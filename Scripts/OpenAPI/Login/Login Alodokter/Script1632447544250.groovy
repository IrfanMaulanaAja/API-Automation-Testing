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
import javax.xml.bind.annotation.XmlElementDecl.GLOBAL
import java.util.List

public class Local_Global_Variable {
	public static String version_for_path 		= findTestData('Alodokter/User_Alodokter').getValue('d_version', 4)
	public static String version_for_endpoint 	= 'v' + version_for_path.replace('.', '')
}

POST_Alodokter_Session()
POST_Update_Device_Token()
//GET_Dokter_Speciality()

private POST_Alodokter_Session() {
	//	===================== URL and Methode ====================
	String host 			= findTestData('Alodokter/Host_Alodokter').getValue('d_host', 1)
	String endpoint 		= '/api/'+ Local_Global_Variable.version_for_endpoint +'/alodokter/sessions/login.json'
	String url 				= host+endpoint
	String requestMethod	= 'POST'
	//	===================== Header Request =====================		
	List<TestObjectProperty> parameters = new ArrayList<>() 
	parameters.add(new TestObjectProperty("Content-Type", ConditionType.EQUALS, "application/json"))
	//	====================== Body Request ======================		
	String stremail 		= findTestData('Alodokter/User_Alodokter').getValue('d_email', 4)
	String strpassword 		= findTestData('Alodokter/User_Alodokter').getValue('d_password', 4)
	String strversion 		= findTestData('Alodokter/User_Alodokter').getValue('d_version', 4)
	String strdevice_token	= findTestData('Alodokter/User_Alodokter').getValue('d_device_token', 4)
			
	String body = '{"device_token": "'+strdevice_token+'","email": "'+stremail+'","password": "'+strpassword+'","version": "'+strversion+'"}'
	println body
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
	GlobalVariable.g_ad_auth_token 	= object.data.auth_token
	GlobalVariable.g_ad_userid 		= object.data.id
	GlobalVariable.g_ad_email		= object.data.email
	GlobalVariable.g_ad_firstname	= object.data.firstname
	GlobalVariable.g_ad_lastname	= object.data.lastname
	GlobalVariable.g_ad_phonenumber	= object.data.phone
	
	println "User ID: " + GlobalVariable.g_ad_userid
	println "Auth Token: " + GlobalVariable.g_ad_auth_token
	println object
	println bodyResponse
	println "HTTP Code = " + codeResponse + " and Status = " + object.status
	if (WS.verifyMatch(codeResponse, '200', false, FailureHandling.STOP_ON_FAILURE)) {
		WS.verifyMatch(object.status, 'success', false, FailureHandling.STOP_ON_FAILURE)
	}
}
	
private POST_Update_Device_Token(){
	//	===================== URL and Methode ====================
	String host 			= findTestData('Alodokter/Host_Alodokter').getValue('d_host', 1)
	String endpoint 		= '/api/'+ Local_Global_Variable.version_for_endpoint +'/alodokter/users/update_device_token.json'
	String url 				= host+endpoint
	String requestMethod 	= "POST"
	//	===================== Header Request =====================
	List<TestObjectProperty> parameters = new ArrayList<>() 
	parameters.add(new TestObjectProperty("X-UID", ConditionType.EQUALS, GlobalVariable.g_ad_userid))
	parameters.add(new TestObjectProperty("HTTP-X-UID", ConditionType.EQUALS, GlobalVariable.g_ad_userid))
	parameters.add(new TestObjectProperty("Authorization", ConditionType.EQUALS, "Token token="+ GlobalVariable.g_ad_auth_token))
	parameters.add(new TestObjectProperty("Content-Type", ConditionType.EQUALS, "application/json; charset=UTF-8"))
	//	====================== Body Request ======================
	String strfcm_token = GlobalVariable.g_ad_auth_token
	String struser_id 	= GlobalVariable.g_ad_userid
	
	String body = '{"fcm_token": "'+strfcm_token+'","user_id": "'+struser_id+'"}'
	println body
	return
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
	if (WS.verifyMatch(codeResponse, '200', false, FailureHandling.STOP_ON_FAILURE)) {
		WS.verifyMatch(object.status, 'success', false, FailureHandling.STOP_ON_FAILURE)
	}
}

private GET_Dokter_Speciality(){
	//	===================== URL and Methode ====================
	String host 			= findTestData('Alodokter/Host_Alodokter').getValue('d_host', 1)
	String endpoint 		= '/api/'+ Local_Global_Variable.version_for_endpoint +'/alodokter/doctors?speciality_id=&page=1'
	String url 				= host+endpoint
	String requestMethod 	= "GET"
	//	===================== Header Request =====================
	List<TestObjectProperty> parameters = new ArrayList<>() 
	parameters.add(new TestObjectProperty("Content-Type", ConditionType.EQUALS, "application/json"))
	parameters.add(new TestObjectProperty("X-UID", ConditionType.EQUALS, GlobalVariable.g_ad_userid))
	parameters.add(new TestObjectProperty("HTTP-X-UID", ConditionType.EQUALS, GlobalVariable.g_ad_userid))
	parameters.add(new TestObjectProperty("Authorization", ConditionType.EQUALS, "Token token=" + GlobalVariable.g_ad_auth_token))
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
	if (WS.verifyMatch(codeResponse, '200', false, FailureHandling.STOP_ON_FAILURE)) {
		WS.verifyMatch(object.status, 'success', false, FailureHandling.STOP_ON_FAILURE)
	}
	
}