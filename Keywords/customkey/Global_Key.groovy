package customkey

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import io.appium.java_client.android.nativekey.AndroidKey

import internal.GlobalVariable

import com.sun.jna.platform.FileUtils
import com.sun.org.apache.bcel.internal.generic.IFGE
import com.kms.katalon.core.exception.StepErrorException as StepErrorException
import io.appium.java_client.InteractsWithApps
import io.appium.java_client.android.AndroidDriver

import io.appium.java_client.android.nativekey.KeyEvent

import io.appium.java_client.AppiumDriver as AppiumDriver
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory as MobileDriverFactory
import org.apache.commons.lang.RandomStringUtils
import io.appium.java_client.android.Activity

import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.stringtemplate.v4.compiler.STParser.ifstat_return
import org.apache.poi.hssf.usermodel.HSSFCellStyle
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.util.IOUtils
import org.apache.poi.ss.usermodel.CreationHelper
import org.apache.poi.ss.usermodel.Drawing
import org.apache.poi.ss.usermodel.ClientAnchor
import org.apache.poi.ss.usermodel.Picture
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Font
import org.apache.poi.ss.usermodel.IndexedColors

import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.Date
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.security.Principal
import java.io.InputStream
import java.io.OutputStream
import java.awt.Robot
import java.awt.Toolkit
import java.awt.event.InputEvent
import java.awt.image.BufferedImage
import java.awt.Desktop
import java.util.Scanner
import java.lang.StringBuilder
import javax.imageio.ImageIO
//import javax.swing.text.html.CSS.BorderStyle
import java.lang.Character
import groovy.json.JsonSlurper
import groovy.swing.factory.ScrollPaneFactory
import groovy.json.JsonBuilder
import groovy.json.JsonLexer
import groovy.json.JsonParser
import groovy.json.JsonParserType
import com.kms.katalon.core.testobject.RequestObject as RequestObject
import java.util.Map
import java.util.List
import java.security.*

import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.RestRequestObjectBuilder
import com.kms.katalon.core.testobject.TestObjectProperty

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.testobject.ConditionType

import com.kms.katalon.core.appium.driver.AppiumDriverManager
import org.openqa.selenium.remote.DesiredCapabilities
import com.kms.katalon.core.mobile.driver.MobileDriverType

import org.openqa.selenium.JavascriptExecutor

import com.kms.katalon.core.webui.driver.DriverFactory

public class Global_Key {

	@Keyword
	def getTestData(String strVar) {
		TestData TD = findTestData(GlobalVariable.Environment+'/'+GlobalVariable.SquadName+'/Data/TestData')
		String variable = ''
		String data = ''
		String active = ''
		String description = ''
		for (def i : (1..TD.getRowNumbers())) {
			variable = TD.getValue('VARIABLE', i)
			active = TD.getValue('ACTIVE', i)
			if (variable.toLowerCase() == strVar.toLowerCase() && active.toLowerCase() == "yes") {
				data = TD.getValue('DATA', i)
				break
			} else {
				data = null
			}
		}
		println data
		return data
	}

	@Keyword
	def startFirstApp(String apps_name) {
		if (GlobalVariable.RunningOnBrowserstack.toString().toLowerCase() == "yes") {
			//get timestamp for test running and validate available app in browserstack
			if(GlobalVariable.DateTimeStamp == '')  {
				Date todaysDate = new Date()
				GlobalVariable.DateTimeStamp = todaysDate.format("yyyyMMdd" + "_"+ "hhmmss")

				//get BSID app
				autoGetAppBSID("Alodokter")
				//				autoGetAppBSID("Alomedika")
				if (GlobalVariable.BSIDApp_Alodokter == "" && GlobalVariable.BSIDApp_Alomedika == "") {
					String errmes = "Apps are not availabe in Browserstack and local. Please place the app in your local destination and rerun"
					forceStopComment(errmes)
				}
			} else {
				if (apps_name == "Alodokter") {
					String errmes = apps_name+" "+GlobalVariable.AlodokterVersion+" is not availabe in Browserstack. Please place the app in your local destination and rerun"
					if (GlobalVariable.BSIDApp_Alodokter == "") {
						forceStopComment(errmes)
					}
				}
				if (apps_name == "Alomedika") {
					String errmes = apps_name+" "+GlobalVariable.AlomedikaVersion+" is not availabe in Browserstack. Please place the app in your local destination and rerun"
					if (GlobalVariable.BSIDApp_Alomedika == "") {
						forceStopComment(errmes)
					}
				}
			}
		}

		//variable
		String appAlodokter = GlobalVariable.BSIDApp_Alodokter //"bs://029c4d402ca062ce3aa2247229264114f698cd45"
		String appAlomedika = GlobalVariable.BSIDApp_Alomedika //"bs://e03511b7118adfe7161038877ccbf1cf624ee0c6"
		String nameTestCase = RunConfiguration.getExecutionSourceName()
		String browserStackServerURL = GlobalVariable.BrowserStackServerURL //"https://aloautokataone_Y3cHSs:45qTV9jdzpRNDmqDXk8n@hub-cloud.browserstack.com/wd/hub"

		//validate if alodokter and alomedika apps is not available in BS
		if (GlobalVariable.RunningOnBrowserstack.toString().toLowerCase() == "yes") {
			if (appAlodokter == "" && appAlomedika == "") {
				String errmes = "Alodokter - "+GlobalVariable.AlodokterVersion+" and Alomedika - "+GlobalVariable.AlomedikaVersion+" not availabe. Please place at your local destination"
				forceStopComment(errmes)
			}
		}

		DesiredCapabilities capabilities = new DesiredCapabilities()
		capabilities.setCapability("device", "Google Pixel 3a")
		capabilities.setCapability("os_version", "9.0")
		capabilities.setCapability("real_mobile", "true")
		capabilities.setCapability("browserstack.debug", "true")
		capabilities.setCapability("browserstack.networkLogs", "true")
		capabilities.setCapability("autoGrantPermissions", "true")
		capabilities.setCapability("browserstack.appium_version", "1.22.0")
		//		capabilities.setCapability("browserstack.appium_version", "1.20.2")
		capabilities.setCapability("locale", "US")
		capabilities.setCapability("language", "US")
		capabilities.setCapability("browserstack.idleTimeout", "300")
		capabilities.setCapability("browserstack.timezone", "Jakarta")

		if(appAlodokter != "" && appAlomedika != "") {
			//			capabilities.setCapability("browserstack.midSessionInstallApps", [appAlodokter, appAlomedika])
		}

		//project details
		capabilities.setCapability("name", GlobalVariable.TestCaseName)

		//if global variable Test Suite is null then set testcasename as session name in browserstack
		//Build Format alodokter/alomedika : Android Feature 4.0.0 Alodokter/TS_AddDeleteButton - 20220128_053053
		if(GlobalVariable.AlodokterVersion != ""){
			if (GlobalVariable.TestSuiteName == '' ) {
				capabilities.setCapability("build", "(" +GlobalVariable.TypeRun+" ) "+ " " + GlobalVariable.AlodokterVersion + " : " +GlobalVariable.SquadName+ "/" +GlobalVariable.TestCaseName+ " - " +GlobalVariable.DateTimeStamp)
			}else{
				capabilities.setCapability("build", "(" +GlobalVariable.TypeRun+" ) "+ " " + GlobalVariable.AlodokterVersion + " : " +GlobalVariable.SquadName+ "/" +GlobalVariable.TestSuiteName+ " - " +GlobalVariable.DateTimeStamp)
			}
			capabilities.setCapability("project", GlobalVariable.TypeRun+" "+GlobalVariable.AlodokterVersion)

		}else{
			if (GlobalVariable.TestSuiteName == '' ) {
				capabilities.setCapability("build", GlobalVariable.TypeRun+" ) "+ " " + GlobalVariable.AlomedikaVersion + " : " +GlobalVariable.SquadName+ "/" +GlobalVariable.TestCaseName+ " - " +GlobalVariable.DateTimeStamp)
			}else{
				capabilities.setCapability("build", GlobalVariable.TypeRun+" ) "+ " " + GlobalVariable.AlomedikaVersion + " : " +GlobalVariable.SquadName+ "/" +GlobalVariable.TestSuiteName+ " - " +GlobalVariable.DateTimeStamp)
			}
			capabilities.setCapability("project", GlobalVariable.TypeRun+" "+GlobalVariable.AlomedikaVersion)
		}

		if(GlobalVariable.RunningOnBrowserstack.toString().toLowerCase() != "yes") {
			if (apps_name == 'Alodokter') {
				Mobile.callTestCase(findTestCase('StartAppUninstall'), [('version') : GlobalVariable.AlodokterVersion, ('app') : apps_name], FailureHandling.STOP_ON_FAILURE)
				GlobalVariable.InstalledAlodokter = "yes"
			}
			if (apps_name == 'Alomedika') {
				Mobile.callTestCase(findTestCase('StartAppUninstall'), [('version') : GlobalVariable.AlomedikaVersion, ('app') : apps_name], FailureHandling.STOP_ON_FAILURE)
				GlobalVariable.InstalledAlomedika = "yes"
			}
			return
		}

		if (apps_name == 'Alodokter') {
			capabilities.setCapability("app", appAlodokter)
			AppiumDriverManager.createMobileDriver(MobileDriverType.ANDROID_DRIVER, capabilities, new URL(browserStackServerURL))
			//Mobile.startApplication(GlobalVariable.BSIDApp_Alodokter, true)
			GlobalVariable.InstalledAlodokter = "yes"
		}
	}

	@Keyword
	def uploadApp(String apps_name) {
		String version = ""
		if (apps_name == "Alodokter") {
			version = GlobalVariable.AlodokterVersion
		} else {
			version = GlobalVariable.AlomedikaVersion
		}
		String namaos = System.getProperty("os.name")
		println namaos //Mac OS X, Windows 10
		String path = ''
		if (namaos.toLowerCase().contains('mac')) {
			path = '/Users/Apps/Android/'
		} else if (namaos.toLowerCase().contains('win')) {
			path = 'C:\\Apps\\'
		} else {
			path = '/home/grumpycat/Apps/'
		}
		println path

		File folder = new File(path)
		File[] listOfFiles = folder.listFiles()
		String namafile = ''
		String appname = ""

		for (int i = 0; i < listOfFiles.length; i++) {
			namafile = listOfFiles[i].getName()
			if (namafile.contains(apps_name) && namafile.contains(version) && namafile.contains('.apk') && namafile.toLowerCase().contains(GlobalVariable.Environment.toString().toLowerCase())) {
				appname = listOfFiles[i].getName()
				Mobile.comment(appname)
				break
			}
		}
		if (appname == "") {
			return
			//			String errmes = apps_name+" versi "+version+" tidak tersedia di local. Harap letakkan di "+path
			//			forceStopComment(errmes)
		}
		String appLoc = path+appname
		println "uploading "+appLoc
		String strCustomID = apps_name+"-"+version+"-"+GlobalVariable.DateTimeStamp

		JsonSlurper Slurper = new JsonSlurper().setType(JsonParserType.INDEX_OVERLAY) //memanggil slurper
		ResponseObject respon = WS.sendRequest(findTestObject('Global/API/BS_UploadApp', [('file'):appLoc, ('custom'):strCustomID]))
		String rescode = respon.getStatusCode()
		WS.comment(rescode)
		String bodyResponse = respon.getResponseBodyContent()
		WS.comment(bodyResponse)
		if (WS.verifyMatch(rescode, '200', false, FailureHandling.STOP_ON_FAILURE)) {
			def object = Slurper.parseText(bodyResponse)
			if (apps_name == "Alodokter") {
				GlobalVariable.BSIDApp_Alodokter = object.app_url
			} else {
				GlobalVariable.BSIDApp_Alomedika = object.app_url
			}
			println "use new uploaded app from local"
		}
	}

	@Keyword
	def autoGetAppBSIDGroup(String apps_name) {
		JsonSlurper Slurper = new JsonSlurper().setType(JsonParserType.INDEX_OVERLAY) //memanggil slurper
		ResponseObject respon = WS.sendRequest(findTestObject('Global/API/BS_ListAppGroup', [:]))
		String rescode = respon.getStatusCode()
		WS.comment(rescode)
		String bodyResponse = respon.getResponseBodyContent()
		WS.comment(bodyResponse)
		if (WS.verifyMatch(rescode, '200', false, FailureHandling.STOP_ON_FAILURE)) {
			def object = Slurper.parseText(bodyResponse)
			if (object.message == "No results found") {
				uploadApp(apps_name)
				return
			}
			String[] getLength = object.app_name
			for (int i in 0..getLength.length-1) {
				String strAppName = object.app_name[i]
				String strAppVersion = object.app_version[i]
				String strAppUrl = object.app_url[i]
				String strUploadDate = object.uploaded_at[i]
				if (strAppName.contains(apps_name) && strAppVersion.contains(GlobalVariable.AlodokterVersion) && strAppName.contains(".apk") && strAppName.toLowerCase().contains(GlobalVariable.Environment.toString().toLowerCase())) {
					println strAppName+"-"+strAppVersion+"-"+strAppUrl+"-"+strUploadDate
					if (apps_name == "Alodokter") {
						GlobalVariable.BSIDApp_Alodokter = strAppUrl
					} else {
						GlobalVariable.BSIDApp_Alomedika = strAppUrl
					}
					return
				}
			}
			uploadApp(apps_name)
		}
	}

	@Keyword
	def autoGetAppBSID(String apps_name) {
		JsonSlurper Slurper = new JsonSlurper().setType(JsonParserType.INDEX_OVERLAY) //memanggil slurper
		ResponseObject respon = WS.sendRequest(findTestObject('Global/API/BS_ListAppRecent', [:]))
		String rescode = respon.getStatusCode()
		WS.comment(rescode)
		String bodyResponse = respon.getResponseBodyContent()
		WS.comment(bodyResponse)
		if (WS.verifyMatch(rescode, '200', false, FailureHandling.STOP_ON_FAILURE)) {
			def object = Slurper.parseText(bodyResponse)
			if (object.message == "No results found") {
				uploadApp(apps_name)
				return
			}
			String[] getLength = object.app_name
			for (int i in 0..getLength.length-1) {
				String strAppName = object.app_name[i]
				String strAppVersion = object.app_version[i]
				String strAppUrl = object.app_url[i]
				String strUploadDate = object.uploaded_at[i]
				if (strAppName.contains(apps_name) && strAppVersion.contains(GlobalVariable.AlodokterVersion) && strAppName.contains(".apk") && strAppName.toLowerCase().contains(GlobalVariable.Environment.toString().toLowerCase())) {
					println strAppName+"-"+strAppVersion+"-"+strAppUrl+"-"+strUploadDate
					if (apps_name == "Alodokter") {
						GlobalVariable.BSIDApp_Alodokter = strAppUrl
					} else {
						GlobalVariable.BSIDApp_Alomedika = strAppUrl
					}
					println "use existing app in browserstack"
					return
				}
			}
			uploadApp(apps_name)
		}
	}

	@Keyword
	def getIDOnly() {
		JsonSlurper Slurper = new JsonSlurper().setType(JsonParserType.INDEX_OVERLAY) //memanggil slurper
		ResponseObject respon = WS.sendRequest(findTestObject('Global/API/BS_ListAppRecent', [:]))
		String rescode = respon.getStatusCode()
		WS.comment(rescode)
		String bodyResponse = respon.getResponseBodyContent()
		WS.comment(bodyResponse)
		if (WS.verifyMatch(rescode, '200', false, FailureHandling.STOP_ON_FAILURE)) {
			def object = Slurper.parseText(bodyResponse)
			println object
		}
	}

	@Keyword
	def deleteAllAppBSAllVersion() {
		JsonSlurper Slurper = new JsonSlurper().setType(JsonParserType.INDEX_OVERLAY) //memanggil slurper
		ResponseObject respon = WS.sendRequest(findTestObject('Global/API/BS_ListAppRecent', [:]))
		String rescode = respon.getStatusCode()
		WS.comment(rescode)
		String bodyResponse = respon.getResponseBodyContent()
		WS.comment(bodyResponse)
		if (WS.verifyMatch(rescode, '200', false, FailureHandling.STOP_ON_FAILURE)) {
			def object = Slurper.parseText(bodyResponse)
			if (object.message == "No results found") {
				return
			}
			String[] getLength = object.app_name
			for (int i in 0..getLength.length-1) {
				String strAppID = object.app_id[i]
				String strAppName = object.app_name[i]
				if (strAppName.contains(".apk") && strAppName.toLowerCase().contains(GlobalVariable.Environment.toString().toLowerCase())) {
					deleteApp(strAppID)
				}
			}
		}
	}

	@Keyword
	def deleteAllAppBSCurrentVersion() {
		JsonSlurper Slurper = new JsonSlurper().setType(JsonParserType.INDEX_OVERLAY) //memanggil slurper
		ResponseObject respon = WS.sendRequest(findTestObject('Global/API/BS_ListAppRecent', [:]))
		String rescode = respon.getStatusCode()
		WS.comment(rescode)
		String bodyResponse = respon.getResponseBodyContent()
		WS.comment(bodyResponse)
		if (WS.verifyMatch(rescode, '200', false, FailureHandling.STOP_ON_FAILURE)) {
			def object = Slurper.parseText(bodyResponse)
			if (object.message == "No results found") {
				return
			}
			String[] getLength = object.app_name
			for (int i in 0..getLength.length-1) {
				String strAppID = object.app_id[i]
				String strAppName = object.app_name[i]
				String strAppVersion = object.app_version[i]
				if (strAppName.contains(".apk") && strAppVersion == GlobalVariable.AlodokterVersion && strAppName.toLowerCase().contains(GlobalVariable.Environment.toString().toLowerCase())) {
					deleteApp(strAppID)
				}
			}
		}
	}

	@Keyword
	def deleteApp(String appBSID) {
		String Url = "https://api-cloud.browserstack.com/app-automate/app/delete/"+appBSID
		JsonSlurper Slurper = new JsonSlurper().setType(JsonParserType.INDEX_OVERLAY) //memanggil slurper
		ResponseObject respon = WS.sendRequest(findTestObject('Global/API/BS_DeleteApp', [('url'):Url]))
		String rescode = respon.getStatusCode()
		WS.comment(rescode)
		String bodyResponse = respon.getResponseBodyContent()
		WS.comment(bodyResponse)
		if (WS.verifyMatch(rescode, '200', false, FailureHandling.STOP_ON_FAILURE)) {
			def object = Slurper.parseText(bodyResponse)
		}
	}

	@Keyword
	def closeApplication() {
		GlobalVariable.InstalledAlodokter = ""
		GlobalVariable.InstalledAlomedika = ""
		Mobile.closeApplication()
	}

	@Keyword
	def statusBS(String status, String message) {
		//status is "passed" or "failed"
		if(GlobalVariable.RunningOnBrowserstack.toString().toLowerCase() == "yes") {
			AndroidDriver<?> driver = MobileDriverFactory.getDriver()
			JavascriptExecutor jse = (JavascriptExecutor)driver
			if(status == "passed" || status == "failed") {
				jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \""+status+"\", \"reason\": \""+message+"\"}}")
			}
			//jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"test reason aja gaes\"}}")
		}
	}

	@Keyword
	def loginEmail(String squad, String app, String version, String type) {
		String runtype = "feature"
		if (GlobalVariable.isCriticalPath.toString().toLowerCase() == "yes") {
			runtype = "critical"
		}
		GlobalVariable.SquadName = squad
		TestData Login = findTestData(GlobalVariable.Environment+'/'+squad+'/LoginAccount/'+app+"/User")
		String vers = ''
		String typ = ''
		String username = ''
		String testtype = ''
		for (def i : (1..Login.getRowNumbers())) {
			vers = Login.getValue('VERSION', i)
			typ = Login.getValue('TYPE', i)
			testtype = Login.getValue('TESTTYPE', i).toLowerCase()
			if (vers.toLowerCase() == version && typ == type && testtype.trim().contains(runtype)) {
				username = Login.getValue('USERNAME', i)
				return username
				break
			}
		}
		//How to :
		//CustomKeywords.'customkey.Global_Key.LoginUsername'(Squad, App, Version (global), Type User)
		//example : CustomKeywords.'customkey.Global_Key.LoginUsername'("EPharmacy", "Alodokter", GlobalVariable.AlodokterVersion,"AXA")
	}

	@Keyword
	def loginPassword(String squad, String app,String version, String type) {
		String runtype = "feature"
		if (GlobalVariable.isCriticalPath.toString().toLowerCase() == "yes") {
			runtype = "critical"
		}
		TestData Login = findTestData(GlobalVariable.Environment+'/'+squad+'/LoginAccount/'+app+"/User")
		String vers = ''
		String typ = ''
		String password = ''
		String testtype = ''
		for (def i : (1..Login.getRowNumbers())) {
			vers = Login.getValue('VERSION', i)
			typ = Login.getValue('TYPE', i)
			testtype = Login.getValue('TESTTYPE', i).toLowerCase()
			if (vers.toLowerCase() == version && typ == type && testtype.trim().contains(runtype)) {
				password = Login.getValue('PASSWORD', i)
				return password
				break
			}
		}
		//How to :
		//CustomKeywords.'customkey.Global_Key.LoginPassword'(Squad, App, Version (global), Type User)
		//example : CustomKeywords.'customkey.Global_Key.LoginPassword'("EPharmacy", "Alodokter", GlobalVariable.AlodokterVersion,"AXA")
	}

	@Keyword
	void UninstallAppV2(String appname) {
		return //non aktifkan dulu saat development
		//requirement : aplikasinya sudah terinstall terlebih dahulu
		//uninstal app alodokter atau alomedika
		if(appname.contains('Alodokter') || appname.contains('alodokter')){
			try {
				Mobile.startExistingApplication('com.alodokter.android')
			} catch (Exception e) {
				return
			}
			InteractsWithApps unins = MobileDriverFactory.getDriver()
			unins.removeApp('com.alodokter.android')
		} else if (appname.contains('Alomedika') || appname.contains('alomedika')) {
			try {
				Mobile.startExistingApplication('com.alodokter.doctor')
			} catch (Exception e) {
				return
			}
			InteractsWithApps unins = MobileDriverFactory.getDriver()
			unins.removeApp('com.alodokter.doctor')
		}
	}

	@Keyword
	def getScriptVersion() {
		String spath = RunConfiguration.getExecutionSourceId()
		String[] pa = spath.split("/")
		println spath+'-'+pa[1]
		String version = pa[2]
		return version
	}

	@Keyword
	def openFile(String namafile) {
		//how to use : CustomKeywords.'customkey.Global_Key.OpenFile'('report.xlsx')
		File myFile = new File(namafile)
		Desktop.getDesktop().open(myFile)
	}

	@Keyword
	void inputExcel(String sReport, String sKet, String sNamaSheet) {
		//how to use : CustomKeywords.'customkey.Global_Key.InputExcel'("report", "ket", "REPORT")
		//		String sReport = 'OKre,port1'
		//		String sKet = 'OKket'

		//def DirExcel = "C:\\Katalon Studio\\Android\\report.xlsx"
		def DirExcel = "report.xlsx"
		FileInputStream file = new FileInputStream (new File(DirExcel))
		XSSFWorkbook workbook = new XSSFWorkbook(file)
		int totalsheet = workbook.getNumberOfSheets()
		String reportexist = ""
		XSSFSheet sheet = workbook.getSheetAt(0)
		for (def iSheet : (1..totalsheet)) {
			String sheetname = workbook.getSheetName(iSheet - 1)
			if (sheetname == sNamaSheet.toUpperCase() ) {
				reportexist = "ada"
				break
			}
		}

		if (reportexist == "") {
			String buatsheetreport = workbook.createSheet(sNamaSheet.toUpperCase())
		}

		//Write data to excel
		XSSFSheet sheetReport = workbook.getSheet(sNamaSheet.toUpperCase())
		sheetReport.createRow(0).createCell(0).setCellValue("create row") //create row untuk memancing isian excelnya

		//auto increment nomor
		int sRow = 0
		int sNo = 0
		sRow = sheetReport.getLastRowNum() + 1
		sNo = sheetReport.getLastRowNum() + 1

		//style pada cell header (allignment, font color, cell color)
		CellStyle styleH = workbook.createCellStyle()
		styleH.setBorderBottom(BorderStyle.THIN)
		styleH.setBorderLeft(BorderStyle.THIN)
		styleH.setBorderRight(BorderStyle.THIN)
		styleH.setBorderTop(BorderStyle.THIN)
		Font font = workbook.createFont()
		//		font.setBoldweight(Font.BOLDWEIGHT_BOLD)
		font.setBold(true)
		styleH.setFont(font)

		//style pada cell body
		CellStyle style = workbook.createCellStyle()
		style.setBorderBottom(BorderStyle.THIN)
		style.setBorderLeft(BorderStyle.THIN)
		style.setBorderRight(BorderStyle.THIN)
		style.setBorderTop(BorderStyle.THIN)
		//	sheetReport.createRow(3).createCell(0).setCellStyle(style) //panggil style pada setiap cell

		sheetReport.getRow(0).createCell(0).setCellStyle(styleH)
		sheetReport.getRow(0).createCell(1).setCellStyle(styleH)
		sheetReport.getRow(0).createCell(2).setCellStyle(styleH)

		//	sheetReport.setColumnWidth(2, 30)
		sheetReport.getRow(0).getCell(0).setCellValue("NO")
		sheetReport.getRow(0).getCell(1).setCellValue("REPORT")
		sheetReport.getRow(0).getCell(2).setCellValue("KETERANGAN")

		sheetReport.createRow(sRow).createCell(0).setCellValue("Create Row") //create row untuk memancing isian excelnya
		sheetReport.getRow(sRow).createCell(0).setCellStyle(style)
		sheetReport.getRow(sRow).createCell(1).setCellStyle(style)
		sheetReport.getRow(sRow).createCell(2).setCellStyle(style)

		sheetReport.getRow(sRow).getCell(0).setCellValue(sNo)
		sheetReport.getRow(sRow).getCell(1).setCellValue(sReport)
		sheetReport.getRow(sRow).getCell(2).setCellValue(sKet)

		sheetReport.autoSizeColumn(2) //untuk merapihkan satu column

		//save file
		file.close()
		FileOutputStream outFile =new FileOutputStream(new File(DirExcel))
		workbook.write(outFile)
		outFile.close()
	}

	@Keyword
	def getDayName(String namahari) {
		if (namahari.toLowerCase() == 'mon' || namahari.toLowerCase() == 'sen' || namahari == '1') {
			namahari = 'Senin'
		} else if (namahari.toLowerCase() == 'tue' || namahari.toLowerCase() == 'sel' || namahari == '2') {
			namahari = 'Selasa'
		} else if (namahari.toLowerCase() == 'wed' || namahari.toLowerCase() == 'rab' || namahari.toLowerCase() == 'rabu' || namahari == '3') {
			namahari = 'Rabu'
		} else if (namahari.toLowerCase() == 'thu' || namahari.toLowerCase() == 'kam' || namahari == '4') {
			namahari = 'Kamis'
		} else if (namahari.toLowerCase() == 'fri' || namahari.toLowerCase() == 'jum' || namahari == '5') {
			namahari = 'Jumat'
		} else if (namahari.toLowerCase() == 'sat' || namahari.toLowerCase() == 'sab' || namahari.toLowerCase() == 'sabt' || namahari == '6') {
			namahari = 'Sabtu'
		} else if (namahari.toLowerCase() == 'sun' || namahari.toLowerCase() == 'ming' || namahari.toLowerCase() == 'min' || namahari == '7' || namahari == '0') {
			namahari = 'Minggu'
		}
		return namahari
	}

	@Keyword
	def getFullMonth(String Bulan) {

		if (Bulan == '01' || Bulan == '1' || Bulan.toLowerCase() == 'jan') {
			Bulan = 'Januari'
		} else if (Bulan == '02' || Bulan == '2' || Bulan.toLowerCase() == 'feb') {
			Bulan = 'Februari'
		} else if (Bulan == '03' || Bulan == '3' || Bulan.toLowerCase() == 'mar') {
			Bulan = 'Maret'
		} else if (Bulan == '04' || Bulan == '4' || Bulan.toLowerCase() == 'apr') {
			Bulan = 'April'
		} else if (Bulan == '05' || Bulan == '5' || Bulan.toLowerCase() == 'may' || Bulan.toLowerCase() == 'mei') {
			Bulan = 'Mei'
		} else if (Bulan == '06' || Bulan == '6' || Bulan.toLowerCase() == 'jun') {
			Bulan = 'Juni'
		} else if (Bulan == '07' || Bulan == '7' || Bulan.toLowerCase() == 'jul') {
			Bulan = 'Juli'
		} else if (Bulan == '08' || Bulan == '8' || Bulan.toLowerCase() == 'aug' || Bulan.toLowerCase() == 'agu' || Bulan.toLowerCase() == 'ags' || Bulan.toLowerCase() == 'agt') {
			Bulan = 'Agustus'
		} else if (Bulan == '09' || Bulan == '9' || Bulan.toLowerCase() == 'sep' || Bulan.toLowerCase() == 'sept') {
			Bulan = 'September'
		} else if (Bulan == '10' || Bulan == '10' || Bulan.toLowerCase() == 'oct' || Bulan.toLowerCase() == 'okt') {
			Bulan = 'Oktober'
		} else if (Bulan == '11' || Bulan == '11' || Bulan.toLowerCase() == 'nov') {
			Bulan = 'November'
		} else if (Bulan == '12' || Bulan == '12' || Bulan.toLowerCase() == 'des') {
			Bulan = 'Desember'
		} else {
			Bulan = 'Bulan Tidak Diketahui'
		}
		return Bulan
	}

	@Keyword
	void removeNotif() {
		Mobile.swipe(650, 330, 650, 1)
	}

	@Keyword
	void swipeLeft() {
		Mobile.swipe(960, 780, 95, 780)
	}

	@Keyword
	void scrollUp() {
		Mobile.swipe(400, 1800, 400, 710)
	}

	@Keyword
	void scrollUpSlight() {
		Mobile.swipe(400, 1800, 400, 1400)
	}

	@Keyword
	void scrollUpSlightHeavy() {
		Mobile.swipe(400, 1800, 400, 1000)
	}

	@Keyword
	def tapBlankAndroid() {
		int hei = Mobile.getDeviceHeight() - 100
		int wid = Mobile.getDeviceWidth() - 100
		Mobile.tapAtPosition(wid, hei)
	}

	@Keyword
	void uninstallApp(String appname) {
		//uninstal app alodokter atau alomedika
		if(appname.contains('Alodokter') || appname.contains('alodokter')){
			InteractsWithApps unins = MobileDriverFactory.getDriver()
			unins.removeApp('com.alodokter.android')
		} else if (appname.contains('Alomedika') || appname.contains('alomedika')) {
			InteractsWithApps unins = MobileDriverFactory.getDriver()
			unins.removeApp('com.alodokter.doctor')
		}
	}

	@Keyword
	void setTxtVarEmail(String var) {
		def delt = new File("VarEmail.txt")
		delt.delete()
		def txt = new File("VarEmail.txt")
		txt.append(var)
	}

	@Keyword
	def getTxtVarEmail() {
		File file = new File("VarEmail.txt")
		String var = file.getText("UTF-8")
		def delt = new File("VarEmail.txt")
		delt.delete()
		return var
	}

	@Keyword
	void setTxtVarPass(String var) {
		def delt = new File("VarPass.txt")
		delt.delete()
		def txt = new File("VarPass.txt")
		txt.append(var)
	}

	@Keyword
	def getTxtVarPass() {
		File file = new File("VarPass.txt")
		String var = file.getText("UTF-8")
		def delt = new File("VarPass.txt")
		delt.delete()
		return var
	}

	@Keyword
	void addGlobalVariable(String name, def value) {
		GroovyShell shell1 = new GroovyShell()
		MetaClass mc = shell1.evaluate("internal.GlobalVariable").metaClass
		String getterName = "get" + name.capitalize()
		mc.'static'."$getterName" = { -> return value }
		mc.'static'."$name" = value
	}

	@Keyword
	void forceStop() {
		throw new StepErrorException("FORCEDSTOP")
	}

	@Keyword
	void forceStopComment(String message) {
		throw new StepErrorException(message)
	}

	@Keyword
	def randomNumber(int digit) {
		//digit adalah jumlat digit yg akan dibuat. contoh digit = 3 -> 892, digit = 5 -> 89210
		String num = RandomStringUtils.random(digit, '1234567890')
		return num
	}

	@Keyword
	def randomAlphabet(int digit) {
		//digit adalah jumlat digit yg akan dibuat. contoh digit = 3 -> jks, digit = 5 -> alkds
		String alp = RandomStringUtils.random(digit, 'abcdefghijklmnopqrstuvwxyz')
		return alp
	}

	@Keyword
	def btnMenuAlomedika() {
		int lef = Mobile.getElementLeftPosition(findTestObject('Alomedika/2.5.2/HomeLanding/RFN_btn_menu'), 0) + 500
		int top = Mobile.getElementTopPosition(findTestObject('Alomedika/2.5.2/HomeLanding/RFN_btn_menu'), 0) + 3
		Mobile.swipe(0, top, lef, top)
		Mobile.delay(1)
	}

	@Keyword
	def randomSentence(int sentence) {
		//sentence adalah jumlat kalimat yg ingin dibuat. sentence = 3 -> zxxc klsz klwq
		String kalimat = ''
		for (def kal : (1..sentence)) {
			kalimat = RandomStringUtils.random(4, 'abcdefghijklmnopqrstuvwxyz') +' '+ kalimat
		}
		return kalimat
	}

	@Keyword
	def enterKeyboard() {
		AndroidDriver<?> driver = MobileDriverFactory.getDriver()
		driver.pressKeyCode(new KeyEvent(AndroidKey.SEARCH))
	}


	@Keyword
	def getDoctor() {
		/*===== pilih dokter dari cari dokter =====*/
		int sum = 5

		for (int i=1 ; i<=sum; i++) {
			String Doctor = '//hierarchy/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/androidx.recyclerview.widget.RecyclerView[1]/android.widget.LinearLayout['+i+']'
			println Doctor

			TestObject to = new TestObject("ClickDoctor")
			to.addProperty("xpath", ConditionType.EQUALS, Doctor)
			Mobile.tap(to, 0)

			if (Mobile.waitForElementPresent(findTestObject('Object Repository/EPharmacy/Chat/IRF_lblOnlineDoctor'), 2)) {
				Mobile.comment('Doctor Online')
				break
			}
			Mobile.pressBack()

		}
	}


}