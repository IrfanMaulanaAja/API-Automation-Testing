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

public class localGlobal {
	public static String emailAlodokter = "autouserfofize@mail.com"
	public static String passwordAlodokter = "123456"
	
	public static String hostMmobile = "https://staging-mapp.alodokter.com/api/v"+GlobalVariable.AlodokterVersion.toString().replace(".", "")
	public static String deviceToken
	public static String AlodokterAppToken
	public static String AlodokterAppUID	
}

postLogin()

//=== login group ===
private postLogin() {
	String host = localGlobal.hostMmobile
	String endpoint = "/alodokter/sessions/login.json"
	String url = host+endpoint
	String devicetoken = "d2WV7SUCT7K8i3qeIQZOXo:APA91bGOvnpIcy7B99Nf_VvLUveGAeDRBtCDXaLOTMYo4d15z-gXJtdzsgxNyvaPNm-d5BdQbkllB_UqQcR0cpNdZUY3BVjQ4KekNpF2qxE9cQJsMyg0_0r9B0ggJbdqT_FmIpwAsdgv"
	String email = localGlobal.emailAlodokter
	String pass = localGlobal.passwordAlodokter
	
	String body =
	"""
	{
		"device_token": "${devicetoken}",
		"email": "${email}",
		"password": "${pass}",
		"version": "${GlobalVariable.AlodokterVersion}"
	}
	"""
	WS.comment(body)
	
	//	===================== Header Request =====================
	List<TestObjectProperty> parameters = new ArrayList<>()
	parameters.add(new TestObjectProperty("Content-Type", ConditionType.EQUALS, "application/json"))
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
		localGlobal.AlodokterAppToken = object.data.auth_token
		localGlobal.AlodokterAppUID = object.data.id
		println GlobalVariable.BASE_TOKEN = localGlobal.AlodokterAppToken
		println GlobalVariable.BASE_UID = localGlobal.AlodokterAppUID
		localGlobal.deviceToken = object.data.device_token
//		FirebaseAttributes()
		postUpdateDeviceToken()
	}
	//'{"device_token": "'+devicetoken+'","email": "'+email+'","password": "'+pass+'","version": "'+GlobalVariable.AlodokterVersion+'"}'
}

private postUpdateDeviceToken() {
	String endpoint = "/alodokter/users/update_device_token.json"
	String host = localGlobal.hostMmobile
	String url = host+endpoint
	
	String body =
	"""
	{
		"fcm_token": "${localGlobal.deviceToken}",
		"user_id": "${localGlobal.AlodokterAppUID}"
	}
	"""
	WS.comment(body)
	
	//	===================== Header Request =====================
	List<TestObjectProperty> parameters = new ArrayList<>()
	parameters.add(new TestObjectProperty("X-UID", ConditionType.EQUALS, localGlobal.AlodokterAppUID))
	parameters.add(new TestObjectProperty("HTTP-X-UID", ConditionType.EQUALS, localGlobal.AlodokterAppUID))
	parameters.add(new TestObjectProperty("Authorization", ConditionType.EQUALS, "Token token="+ localGlobal.AlodokterAppToken))
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
		println object
	}
}

private postFirebaseAttributes() {
	String endpoint = "/alodokter/users/update_firebase_attributes.json"
	String host = localGlobal.hostMmobile
	String url = host+endpoint
	
	//variable
	String strLogiceName = "feed_cluster_ctr"
	String strPriceSegment = "B"
	String strShopSegment = "B"
	
	String body = '{"logic_name": "'+strLogiceName+'","price_segmentation": "'+strPriceSegment+'","shop_tab_segmentation": "'+strShopSegment+'"}'
	WS.comment(body)
	//	===================== Header Request =====================
	List<TestObjectProperty> parameters = new ArrayList<>()
	parameters.add(new TestObjectProperty("X-UID", ConditionType.EQUALS, localGlobal.AlodokterAppUID))
	parameters.add(new TestObjectProperty("HTTP-X-UID", ConditionType.EQUALS, localGlobal.AlodokterAppUID))
	parameters.add(new TestObjectProperty("Authorization", ConditionType.EQUALS, "Token token="+ localGlobal.AlodokterAppToken))
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
		println object
	}
}