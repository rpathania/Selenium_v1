package hr_processes;

import java.awt.AWTException;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.*;

import com.relevantcodes.extentreports.ExtentReports;

import com.relevantcodes.extentreports.LogStatus;

import hr_processes.Methods;
public class Run {
	public static String[][] InputExcel = null;
	public static String[][] a = null;
	public static String[] tbh_result = null;
	public static String[] position_number = null;
	static ExtentReports extent = new ExtentReports (System.getProperty(Methods.currentDir.toAbsolutePath()+"")+"\\test-output\\HiringReport.html", true);
	static WebDriver driver;
	static int counterforinput = 0;
	static int rowcountExcel = 0;
	
	@BeforeSuite
	public void xmlReader() throws Exception{
		JFrame f = new JFrame();

		String name = JOptionPane.showInputDialog(f,
				"Please select the Instance: \r\n 1>DSERAJ \r\n 2>SERAHTST \r\n 3>DSER1J ");
		/*System.out.println("Please Choose the Test Environment");
		System.out.println("Press 1 for DSERAJ");
		System.out.println("Press 2 for SERAHTST");
		System.out.println("Press 3 for DSER1J");
		Scanner envInput = new Scanner(System.in);*/
		int id = Integer.parseInt(name);
		String TestEnv = "/HiringModule/TestEnvironments/TestEnvironment[@id="+id+"]";
		org.w3c.dom.Document document =  Methods.getDocument(Methods.currentDir.toAbsolutePath()+"\\configuration.xml");                 
		Methods.Navigations = Methods.evaluateXPath(document, Methods.Nav);
		Methods.Environment_Details = Methods.evaluateXPath(document, TestEnv);
		Methods.PayGroup = Methods.evaluateXPath(document, Methods.PayGrp);   
		Methods.TemplateID = Methods.evaluateXPath(document, Methods.TbhTemplate);
		Methods.Reason_Code = Methods.evaluateXPath(document, Methods.ReasonCode);
		Methods.City_State = Methods.evaluateXPath(document, Methods.CityState);
		//System.setProperty("webdriver.gecko.driver",Methods.currentDir.toAbsolutePath()+"\\geckodriver.exe");
		System.setProperty("webdriver.chrome.driver",Methods.currentDir.toAbsolutePath()+"\\chromedriver.exe");
		driver = new ChromeDriver();
		//System.setProperty("webdriver.gecko.driver","d:\\Profiles\\aniarora\\Desktop\\Test Automation\\GeckoDriver\\geckodriver.exe");
		//File pathToBinary = new File("D:\\Profiles\\aniarora\\AppData\\Local\\Mozilla Firefox\\firefox.exe");
		//FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
		//FirefoxProfile firefoxProfile = new FirefoxProfile();       
	//	driver = new FirefoxDriver();
	  //   capa.setCapability("binary", "e");
		//driver = new FirefoxDriver(); 
	}
	@BeforeTest
	public void inputreader() throws IOException 
	{	
		rowcountExcel = Methods.RowCounterExcel();
		driver.manage().window().maximize();
		Methods.zoomInZoomOut("70%",driver);
		driver.get(Methods.Environment_Details.get("URL"));
		Methods.login(Methods.Environment_Details.get("testerId"), Methods.Environment_Details.get("testerPass"), driver);
	}
	@Test
	public void HeadCountReportTest() throws Exception
	{
		for(int x=0;x<rowcountExcel;x++)
		{
		counterforinput++;
		InputExcel = new String[1][4];
		InputExcel = Methods.read(counterforinput);
		Methods.logger = extent.startTest((x+1) + ". Hire "+InputExcel[0][1]+" and validate in Headcount Report");
		a = Methods.QueryRead(driver, InputExcel);
		String[][] TBH =  Methods.QueryResultSplitter("tbh", a);
		position_number = Methods.returnpos(driver, a, InputExcel[0][1]);
		tbh_result = Methods.Template_Based_Hire(TBH, InputExcel, position_number, driver);
		Methods.HeadCountReader(driver, tbh_result, InputExcel[0][1]);
		extent.endTest(Methods.logger);
		//driver.get("https://soprasteria-hris-uat.opc.oracleoutsourcing.com/psp/SERAHTST/EMPLOYEE/HRMS/h/?tab=DEFAULT");
		driver.navigate().back();
		driver.navigate().refresh();
		Methods.navigateToHome(driver);
		}
	}
/*	@Test (priority = 1)
public void QueryReader() throws InterruptedException, AWTException{
		driver.manage().window().maximize();
		driver.get(Methods.Environment_Details.get("URL"));
		Methods.login(Methods.Environment_Details.get("testerId"), Methods.Environment_Details.get("testerPass"), driver);
		Methods.logger = extent.startTest("Running Query");
		a = Methods.QueryRead(driver, InputExcel);
		Methods.logger.log(LogStatus.PASS, "Test Case Passed For Running Query \n");
		extent.endTest(Methods.logger);
		
	
	}
	@Test (priority = 3)
	public void Hire() throws InterruptedException, AWTException, Exception{
		
		
		String[][] TBH =  Methods.QueryResultSplitter("tbh", a);
	//	try {
			Methods.logger = extent.startTest("Hiring Process");
			tbh_result = Methods.Template_Based_Hire(TBH, InputExcel, position_number, driver);
		
		 extent.endTest(Methods.logger);
	
		} 
	@Test (priority = 2)
	public void Position() throws InterruptedException, IOException, AWTException
	{
		Methods.logger = extent.startTest("Position Creation");
		position_number = Methods.returnpos(driver, a);
		extent.endTest(Methods.logger);
	}
	@Test (priority = 4)
	public void HeadCount() throws InterruptedException, IOException
	{
		Methods.logger = extent.startTest("HeadCount Report");
		Methods.HeadCountReader(driver, tbh_result);
		extent.endTest(Methods.logger);
	} 
	*/
	@AfterTest
	public static void endReport()
	{
		extent.flush();
	}
	@AfterSuite
	public static void closeReport()
	{
		driver.close();
		extent.close();
	}

}