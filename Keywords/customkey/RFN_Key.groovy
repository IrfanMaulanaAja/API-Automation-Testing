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

public class RFN_Key {

	@Keyword
	void forceStopComment(String message) {
		throw new StepErrorException(message)
	}

}
