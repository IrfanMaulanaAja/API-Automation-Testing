import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject

import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile

import internal.GlobalVariable as GlobalVariable

import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.annotation.AfterTestCase
import com.kms.katalon.core.annotation.AfterTestSuite
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext

import com.kms.katalon.core.configuration.RunConfiguration

class GlobalTL {
	/**
	 * Executes before every test case starts.
	 * @param testCaseContext related information of the executed test case.
	 */
	@BeforeTestCase
	def beforeTestCase(TestCaseContext testCaseContext) {
		if (GlobalVariable.Environment.toString().toLowerCase().trim() == "staging") {
			GlobalVariable.Environment = "Staging"
		} else if (GlobalVariable.Environment.toString().toLowerCase().trim() == "appimage") {
			GlobalVariable.Environment = "AppImage"
		} else {
			CustomKeywords.'customkey.Global_Key.forceStopComment'("Environment : '"+GlobalVariable.Environment+"' is unknown")
		}
		Mobile.comment("using environment : "+GlobalVariable.Environment)
		GlobalVariable.TestCaseName = ''
		String testCaseId = testCaseContext.getTestCaseId()
		String namaos = System.getProperty("os.name")
		if (testCaseId.contains("/CriticalPath/")) {
			GlobalVariable.isCriticalPath = "yes"
		} else { 
			GlobalVariable.isCriticalPath = ""
		}
		String path = ''
		if (namaos.toLowerCase().contains('mac')) {
			GlobalVariable.TestCaseName = testCaseId.substring((testCaseId.lastIndexOf("/").toInteger()) + 1)
		} else if (namaos.toLowerCase().contains('win')) {
			GlobalVariable.TestCaseName = testCaseId.substring((testCaseId.lastIndexOf("/").toInteger()) + 1)
//			GlobalVariable.TestCaseName = testCaseId.substring((testCaseId.lastIndexOf("\\").toInteger()) + 1)
		} else {
			GlobalVariable.TestCaseName = testCaseId.substring((testCaseId.lastIndexOf("/").toInteger()) + 1)
		}
		String[] nameTC = testCaseId.split("/")
		GlobalVariable.SquadName = nameTC[1]
//		GlobalVariable.SquadName = "OpenAPI"
		if (GlobalVariable.RunningOnBrowserstack.toString().toLowerCase().trim() == "yes") {
			CustomKeywords.'customkey.Global_Key.startFirstApp'("Alodokter")
		}
	}
	
	/**
	 *  * Executes before every test suite starts.
	 * @param testSuiteContext related information of the executed test suite.
	 */
	@BeforeTestSuite
	def beforeTestSuite(TestSuiteContext testSuiteContext) {
		GlobalVariable.TestSuiteName = ''
		String testSuiteId = testSuiteContext.getTestSuiteId()
		String namaos = System.getProperty("os.name")

		String path = ''
		if (namaos.toLowerCase().contains('mac')) {
			GlobalVariable.TestSuiteName = testSuiteId.substring((testSuiteId.lastIndexOf("/").toInteger()) + 1)
		} else if (namaos.toLowerCase().contains('win')) {
			GlobalVariable.TestSuiteName = testSuiteId.substring((testSuiteId.lastIndexOf("/").toInteger()) + 1)
//			GlobalVariable.TestSuiteName = testSuiteId.substring((testSuiteId.lastIndexOf("\\").toInteger()) + 1)
		} else {
			GlobalVariable.TestSuiteName = testSuiteId.substring((testSuiteId.lastIndexOf("/").toInteger()) + 1)
		}
	}
	
	/**
	 * Executes after every test case ends.
	 * @param testCaseContext related information of the executed test case.
	 */
	@AfterTestCase
	def afterTestCase(TestCaseContext testCaseContext) {
		//=== V2 ===
		String TCStatus = testCaseContext.getTestCaseStatus() //PASSED or FAILED or ERROR
		GlobalVariable.isCriticalPath = ""
		
		println TCStatus
		if(TCStatus.trim() == "PASSED") {
			CustomKeywords.'customkey.Global_Key.statusBS'('passed', "This Test Case is PASSED")
		} else {
			String strMessage = testCaseContext.getMessage()
			String conMessage = ""
			int sx = strMessage.indexOf("Reason")
			int sy = strMessage.indexOf("at ")
			if (sx != -1) {
				conMessage = strMessage.substring(sx, sy).trim().replace("\n", "").replace("\r", "")
				sx = strMessage.indexOf("Caused by")
				if (sx != -1) {
					sy = strMessage.indexOf("at ", sx)
					conMessage = conMessage+" || "+strMessage.substring(sx, sy).trim().replace("\n", "").replace("\r", "")
				}
				conMessage = conMessage.replace("com.kms.katalon.core.exception.StepFailedException:", "")
				conMessage = conMessage.replace("com.kms.katalon.core.exception.StepErrorException:", "")
			} else {
				conMessage = "This Test Case is Failed"
			}
			conMessage = conMessage.replace('"', "")
			println conMessage
			CustomKeywords.'customkey.Global_Key.statusBS'('failed', conMessage)
		}
		CustomKeywords.'customkey.Global_Key.closeApplication'()
		
		//=== V1 ===
//		String TCStatus = testCaseContext.getTestCaseStatus() //PASSED or FAILED or ERROR
//		String TCMessage = "This Test Case is Failed"//testCaseContext.getMessage().
//		println TCStatus
//		if(TCStatus.trim() == "PASSED") {
//			CustomKeywords.'customkey.Global_Key.statusBS'('passed', "This Test Case is Passed")			
//		} else {
//			CustomKeywords.'customkey.Global_Key.statusBS'('failed', TCMessage)
//		}
//		CustomKeywords.'customkey.Global_Key.closeApplication'()
	}
}