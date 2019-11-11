package hr_processes;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import hr_processes.Objects;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.server.handler.SendKeys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.NodeList;

import com.google.common.util.concurrent.Service.State;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Methods {
	public static HashMap<String, Integer> HeaderMap = new HashMap<String, Integer>();
	public static HashMap<String, String> TemplateID = new HashMap<String, String>();
	public static HashMap<String, String> Environment_Details = new HashMap<String, String>();
	public static HashMap<String, String> Navigations = new HashMap<String, String>();
	public static HashMap<String, String> Reason_Code = new HashMap<String, String>();
	public static HashMap<String, String> City_State = new HashMap<String, String>();
	public static HashMap<String, String> PayGroup = new HashMap<String, String>();
	public static HashMap<Integer, String> EmployeeIds = new HashMap<Integer, String>();
	public static HashMap<String, String> Queryname = new HashMap<String, String>();
	static Path currentDir = Paths.get("");
	public static String Query = "/HiringModule/Query";

	public static String TbhTemplate = "/HiringModule/TemplateID";
	public static String Nav = "/HiringModule/Navigations/Navigation/path/text()";
	public static String ReasonCode = "/HiringModule/ReasonCode";
	public static String CityState = "/HiringModule/CityState";
	public static String PayGrp = "/HiringModule/PayGroup";
	public static String[] States = { "AP", "DL", "MH", "KA", "TN" };
	public static String DeptLocation = "PUN";
	static ExtentReports extent = new ExtentReports(
			System.getProperty(currentDir.toAbsolutePath() + "") + "\\test-output\\HiringReport.html", true);
	// static String localDirPath =
	// "d:\\Profiles\\aniarora\\eclipse-workspace\\HrTasks\\";
	static Path localDirPath = currentDir.toAbsolutePath();
	static String outLogFolder = "Log_files\\";
	static String fileName = "\\Hire.xlsx";
	//static String fileName = "\\Hire_copy.xlsx";
	// static XSSFWorkbook Workbook;
	static ExtentTest logger;

	public static void login(String id, String pass, WebDriver driver) {
		WebDriverWait wait_login = new WebDriverWait(driver, 10);
		wait_login.until(ExpectedConditions.visibilityOfElementLocated(Objects.txt_username)).sendKeys(id);
		driver.findElement(Objects.txt_password).sendKeys(pass);
		driver.findElement(Objects.btn_login).click();
	}

	public static void mynavigation(String b, WebDriver driver) throws InterruptedException {
		String[] a = b.split(" > ");
		driver.switchTo().defaultContent();
		for (int i = 0; i < a.length; i++) {
			WebDriverWait wait_navigation = new WebDriverWait(driver, 10,5);
			if ((i > 0) && (a[i].equals(a[i - 1]))) {
				Thread.sleep(1000);
				List<WebElement> li = wait_navigation
						.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.linkText(a[i]), 1));
				li.get(1).click();
			} else {
				if(i==1)
		   		 {
		   			/*new Actions(driver).moveToElement(driver.findElement(By.xpath("//*[@id=\"pthnavfly_PORTAL_ROOT_OBJECT\"]/div[2]/div[5]"))).click().build().perform();
		   			Thread.sleep(1000);*/
		   			driver.findElement(By.xpath("//*[@id=\"pthnavfly_PORTAL_ROOT_OBJECT\"]/div[2]/div[5]")).click();
		   		 } 
				 wait_navigation
						.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(a[i]))).click();
				//WebElement myElement = driver.findElement(By.linkText(a[i]));
				//myElement.click();
			}
		}
	}

	public static String[][] QueryRead(WebDriver driver, String[][] InputExcel)
			throws InterruptedException, AWTException {

		String[][] Record = null;
		int HiringCount = 0;
		int rowcounter = 1;

		// System.out.println(InputExcel.length);
		// System.out.println(InputExcel[x][3]);
		// System.out.println(HiringCount);
		HiringCount = Integer.parseInt(InputExcel[0][3]);
		try {
			mynavigation(Navigations.get("Query Manager"), driver);
			driver.switchTo().frame(Objects.frame);
			driver.findElement(Objects.txt_query_search).sendKeys("Z_hire_testing");
			driver.findElement(Objects.btn_query_search).click();
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.visibilityOfElementLocated(Objects.btn_query_edit)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(Objects.lbl_query_name));
			String[] ColumnNumber = wait.until(ExpectedConditions.visibilityOfElementLocated(Objects.lbl_column_number))
					.getText().split(" of ");
			int Column_Number = Integer.parseInt(ColumnNumber[1]);
			Record = new String[Column_Number + 2][HiringCount + 1];
			wait.until(ExpectedConditions.visibilityOfElementLocated(Objects.btn_run_query)).click();
			// Thread.sleep(3000);
			driver.switchTo().defaultContent();
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("ptModFrame_0"));
			wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("/html/body/form/div[4]/table/tbody/tr/td[2]/div/input")))
					.sendKeys(InputExcel[0][2]);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"#ICOK\"]"))).click();
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(Objects.frame));
			// for(int k=0;k<InputExcel.length;k++)
			// {
			// System.out.println(InputExcel.length);
			// System.out.println(k);
			for (int i = 0; i < Integer.parseInt(InputExcel[0][3]); i++) {
				int counter = 0;
				int headercounter = 0;
				Random rand = new Random();
				for (int j = 2; j < Column_Number + 2; j++) {

					int randomNumber = rand.nextInt(101);
					if (randomNumber == 1 || randomNumber == 0) {
						randomNumber += 2;
					}
					if (i == 0) {
						WebElement Header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
								"//*[@id=\"win0divQRY_VIEWER_WRK_HTMLAREA\"]/div/table[2]/tbody/tr/td/table/tbody/tr[1]/th["
										+ j + "]")));
						System.out.println(Header.getText());
						if (Header.getText().contains("&"))
						// Record[j-2][i-1] = Header.getText();
						{
							String r = Header.getText();
							String[] SplitResult = r.split(" & ");
							System.out.println(SplitResult[0]);
							System.out.println(SplitResult[1]);
							Record[headercounter][0] = SplitResult[0];
							HeaderMap.put(Record[headercounter][i], headercounter);
							headercounter++;
							Record[headercounter][0] = SplitResult[1];
							HeaderMap.put(Record[headercounter][i], headercounter);
							headercounter++;

						} else {
							Record[headercounter][0] = Header.getText();
							HeaderMap.put(Header.getText(), headercounter);
							headercounter++;

						}

					}
					// else
					// {
					WebElement value = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
							"//*[@id=\"win0divQRY_VIEWER_WRK_HTMLAREA\"]/div/table[2]/tbody/tr/td/table/tbody/tr["
									+ randomNumber + "]/td[" + j + "]")));
					System.out.println(value.getText());
					if (value.getText().contains("&")) {
						String r = value.getText();
						String[] SplitResult = r.split("&");
						System.out.println(SplitResult[0]);
						System.out.println(SplitResult[1]);
						Record[counter][rowcounter] = SplitResult[0];
						counter++;
						Record[counter][rowcounter] = SplitResult[1];
						counter++;
					} else {
						Record[counter][rowcounter] = value.getText();
						counter++;
					}

				}
				rowcounter++;

			}
			logger.log(LogStatus.PASS, "Creation of random data for Hiring \n");

		} catch (Exception ex) {
			logger.log(LogStatus.FAIL, "Creation of random data for Hiring \n");

		}
		// if(k==InputExcel.length-1)
		// {
		// continue;
		// }
		// else
		// {
		// driver.findElement(By.linkText("Rerun Query")).click();
		// driver.switchTo().defaultContent();
		// wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("ptModFrame_"+(k+1)));
		// wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/form/div[4]/table/tbody/tr/td[2]/div/input"))).sendKeys(InputExcel[k+1][2]);
		// wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"#ICOK\"]"))).click();
		// wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(Objects.frame));
		// Thread.sleep(2000);
		// }

		// }
		return Record;
	}

	public static org.w3c.dom.Document getDocument(String fileName) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		org.w3c.dom.Document doc = builder.parse(fileName);
		return doc;
	}

	public static HashMap<String, String> evaluateXPath(org.w3c.dom.Document document, String xpathExpression)
			throws Exception {
		// Create XPathFactory object
		XPathFactory xpathFactory = XPathFactory.newInstance();

		// Create XPath object
		XPath xpath = xpathFactory.newXPath();
		HashMap<String, String> SearchValue = new HashMap<String, String>();
		try {
			// Create XPathExpression object
			XPathExpression expr = xpath.compile(xpathExpression);

			// Evaluate expression result on XML document
			NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
			if (xpathExpression.contains("Navigation")) {
				for (int i = 0; i < nodes.getLength(); i++) {
					String[] a = nodes.item(i).getNodeValue().split(" > ");
					String b = a[a.length - 1].trim();
					SearchValue.put(b, (nodes.item(i).getNodeValue()));
				}
			} else {
				for (int i = 0; i < nodes.getLength(); i++) {
					// System.out.println(SearchValue);

					NodeList ChildNodes = nodes.item(i).getChildNodes();
					for (int j = 0; j < ChildNodes.getLength(); j++) {
						if (nodes.getLength() > 1) {
							SearchValue.put(ChildNodes.item(j).getNodeName() + i,
									(ChildNodes.item(j).getTextContent()));
							SearchValue.remove("#text" + i);
						} else {
							SearchValue.put(ChildNodes.item(j).getNodeName(), (ChildNodes.item(j).getTextContent()));
							SearchValue.remove("#text");
						}
					}
				}
			}

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return SearchValue;
	}

	public static String[] Template_Based_Hire(String[][] a, String[][] InputExcel, String[] position_number,
			WebDriver driver) throws InterruptedException, AWTException, Exception {
		String[] TBH_Result = new String[a[0].length - 1];
		WebDriverWait wait_tbh = new WebDriverWait(driver, 40, 100);
		// Row count for Query Result Array
		int Rowcount = 0;
		for (int i = 0; i < position_number.length - 1; i++) {
			// System.out.println(InputExcel.length);
			// System.out.println(Integer.parseInt(InputExcel[i][3]));
			String SearchTemplateId = InputExcel[0][2] + InputExcel[0][1];
			Rowcount++;

			try {
				if(i == 1) // uncomment to make a test case fail4
				{
					throw new Exception();
				}

				String[] pos_deptloc_sal = position_number[Rowcount].split("&&");

				// logger.log(LogStatus.INFO, "Hiring of "+InputExcel[i][0]+"
				// "+InputExcel[i][1]+" "+j+" in "+InputExcel[i][2]+" : "+a[HeaderMap.get("First
				// Name")][Rowcount]);
				// logger = extent.startTest("Hiring of "+InputExcel[i][0]+"
				// "+InputExcel[i][1]+" "+j+" in "+InputExcel[i][2]+" : "+a[HeaderMap.get("First
				// Name")][Rowcount]);
				navigateToHome(driver);
				mynavigation(Navigations.get("Template-Based Hire"), driver);
				driver.switchTo().frame(Objects.frame);
				wait_tbh.until(ExpectedConditions.visibilityOfElementLocated(Objects.txt_template_id))
						.sendKeys(TemplateID.get(SearchTemplateId));
				driver.findElement(Objects.btn_template_continue).click();

				WebElement reason_code = wait_tbh
						.until(ExpectedConditions.visibilityOfElementLocated(Objects.txt_reason_code));
				Select reason = new Select(reason_code);
				if (InputExcel[0][1].equals("3PC")) {
					reason.selectByVisibleText(Reason_Code.get("TPC"));
				} else {
					reason.selectByVisibleText(Reason_Code.get(InputExcel[0][1]));
				}
				reason_code.submit();
				WebElement Continue_reason_code = wait_tbh
						.until(ExpectedConditions.visibilityOfElementLocated(Objects.btn_reason_code_continue));
				Continue_reason_code.click();
				Continue_reason_code.submit();
				wait_tbh.until(ExpectedConditions.visibilityOfElementLocated(By.id("HR_TBH_WRK_TBH_TMPL_ID")));
				for (int k = 0; k < a.length; k++) {
					if (a[k][0].contains("Gender")) {
						WebElement Gender = driver.findElement(By.xpath(LabelxpathSelect(a[k][0])));
						Select gender = new Select(Gender);
						System.out.println(a[k][Rowcount]);
						gender.selectByValue(a[k][Rowcount]);
						Gender.submit();

						wait_tbh.until(ExpectedConditions.invisibilityOfElementLocated(By.id("processing")));
						WebElement NamePrefix = driver.findElement(By.xpath(LabelxpathSelect("Prefix")));
						Select Prefix = new Select(NamePrefix);
						if (a[k][Rowcount].equals("F")) {
							Prefix.selectByValue("Miss");
							NamePrefix.submit();
						} else {
							Prefix.selectByValue("Mr");
							NamePrefix.submit();
						}

					} else {
						if (wait_tbh.until(ExpectedConditions
								.visibilityOfElementLocated(By.xpath(LabelxpathInput(a[k][0])))) != null) {
							// if(wait_tbh.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id=(//label[contains(text(),
							// '"+a[k][0]+"')]/@for)]"))) != null){
							// wait_tbh.ignoring(TimeoutException.class, NoSuchElementException.class)
							// .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id=(//label[text()=\"*"+a[k][0]+":
							// \"]/@for)]")));

							// List<WebElement> tbh_list =
							// driver.findElements(By.xpath("//input[@id=(//label[text()=\"*"+a[k][0]+":
							// \"]/@for)]"));
							// int count = tbh_list.size();

							for (int l = 0; l < 2; l++) {
								List<WebElement> tbh_list = driver.findElements(By.xpath(LabelxpathInput(a[k][0])));
								// List<WebElement> tbh_list =
								// driver.findElements(By.xpath("//input[@id=(//label[contains(text(),
								// '"+a[k][0]+"')]/@for)]"));
								// List<WebElement> tbh_list =
								// driver.findElements(By.xpath("//input[@id=(//label(contains[text()=\""+a[k][0]+"\"])/@for)]"));
								int count = tbh_list.size();
								System.out.println(count);
								if (count == 1) {
									tbh_list.get(0).sendKeys(a[k][Rowcount]);
									tbh_list.get(0).submit();
									continue;
								} else {
									tbh_list.get(l).sendKeys(a[k][Rowcount]);
									tbh_list.get(1).submit();
								}
							}

						}

					}
				}
				WebElement MaritalStatus = driver.findElement(By.xpath(LabelxpathSelect(Objects.Marital)));
				Select marital = new Select(MaritalStatus);
				marital.selectByVisibleText("Single");
				MaritalStatus.submit();

				String Date = driver.findElement(By.id("HR_TBH_WRK_JOB_EFFDT")).getText();
				String[] probation = Date.split("/");
				int dt = (Integer.parseInt(probation[0]));

				int month = Integer.parseInt(probation[1]);
				if (dt == 31 && month != 8) {
					dt = 30;
				} else if (dt > 27 && month == 8) {
					dt = 27;
				}

				if (month == 6) {
					month = 12;
				} else {
					month = (month + 6) % 12;

					if (month <= 6) {
						int year = Integer.parseInt(probation[2]);
						year++;
						probation[2] = String.valueOf(year);
					}
				}
				probation[1] = String.valueOf(month);
				probation[0] = String.valueOf(dt);
				List<WebElement> NationalIdType = driver
						.findElements(By.xpath(LabelxpathInput(Objects.NationalIDType)));
				if (!TemplateID.get(SearchTemplateId).contains("CON")) {
					NationalIdType.get(0).clear();
					NationalIdType.get(0).sendKeys("AADHAR");
					NationalIdType.get(0).submit();
					List<WebElement> NationalIdType1 = driver
							.findElements(By.xpath(LabelxpathInput(Objects.NationalIDType)));
					NationalIdType1.get(1).clear();
					NationalIdType1.get(1).sendKeys("PAN");
					NationalIdType1.get(1).submit();
					if (TemplateID.get(SearchTemplateId).contains("FTC")) {
						WebElement ExpectedEndDate = driver.findElement(By.id("HR_TBH_SCR_WRK_TBH_DATE$23"));
						ExpectedEndDate.sendKeys(probation[0] + "/" + probation[1] + "/" + probation[2]);
						ExpectedEndDate.submit();
					} else {
						WebElement ProbationDate = driver.findElement(By.xpath(LabelxpathInput(Objects.Probation)));
						ProbationDate.sendKeys(probation[0] + "/" + probation[1] + "/" + probation[2]);
						ProbationDate.submit();
					}

				} else {
					NationalIdType.get(0).clear();
					NationalIdType.get(0).sendKeys("AADHAR");
					NationalIdType.get(0).submit();
					WebElement EndDate = driver.findElement(By.id("HR_TBH_SCR_WRK_TBH_DATE$22"));
					EndDate.sendKeys(probation[0] + "/" + probation[1] + "/" + probation[2]);
					EndDate.submit();
				}
				Random tbh = new Random();
				int AadharNumber = tbh.nextInt(1999999000);
				int balancingfactor = 12 - String.valueOf(AadharNumber).length();
				List<WebElement> NationalId = driver.findElements(By.xpath(LabelxpathInput(Objects.NationalID)));
				NationalId.get(0).sendKeys((long) AadharNumber * (int) Math.pow(10, balancingfactor) + "");
				NationalId.get(0).submit();
				WebElement Education = driver.findElement(By.xpath(LabelxpathInput(Objects.Education)));
				Education.sendKeys("G" + Keys.TAB);
				Education.submit();
				List<WebElement> State = driver.findElements(By.xpath(LabelxpathInput(Objects.State)));
				System.out.println(State.size());
				int nextInt = tbh.nextInt(City_State.size());
				System.out.println(City_State.size());
				System.out.println(nextInt);
				State.get(1).sendKeys(States[nextInt]);
				State.get(1).submit();

				List<WebElement> State1 = driver.findElements(By.xpath(LabelxpathInput(Objects.State)));
				State1.get(2).sendKeys(States[nextInt]);
				State1.get(2).submit();

				List<WebElement> City = driver.findElements(By.xpath(LabelxpathInput(Objects.City)));
				City.get(0).sendKeys(City_State.get(States[nextInt]));
				City.get(0).submit();

				List<WebElement> City1 = driver.findElements(By.xpath(LabelxpathInput(Objects.City)));
				City1.get(1).sendKeys(City_State.get(States[nextInt]));
				City1.get(1).submit();

				// String Date = a[HeaderMap.get("Date of Birth")][Rowcount];

				int Telephone = tbh.nextInt(999999999);
				int balFactor = 10 - String.valueOf(Telephone).length();
				System.out.println(Telephone + " & " + balFactor);
				WebElement ContactNumber = driver.findElement(By.xpath(LabelxpathInput(Objects.Telephone)));
				ContactNumber.sendKeys((long) Telephone * (int) Math.pow(10, balFactor) + "");
				ContactNumber.submit();
				WebElement Email = driver.findElement(By.xpath(LabelxpathInput(Objects.Email)));
				Email.sendKeys(a[HeaderMap.get("First Name")][Rowcount] + tbh.nextInt(999) + "@dummy.com");
				Email.submit();
				WebElement Classification = driver.findElement(By.xpath(LabelxpathSelect(Objects.Classification)));
				Select empclass = new Select(Classification);
				if (InputExcel[0][1].equals("Trainee") || InputExcel[0][1].equals("Intern")) {
					empclass.selectByValue(InputExcel[0][1].substring(0, 1));
				} else {
					empclass.selectByValue(InputExcel[0][1]);
				}
				Classification.submit();
				if (InputExcel[0][2].equals("SI")) {
					WebElement HolidaySch = driver.findElement(By.xpath(LabelxpathInput(Objects.Schedule)));
					HolidaySch.sendKeys(InputExcel[0][0] + pos_deptloc_sal[1]);
					HolidaySch.submit();

					WebElement Paygroup = driver.findElement(By.xpath(LabelxpathInput(Objects.PayGroup)));

					if (InputExcel[0][1].equals("3PC")) {
						Paygroup.sendKeys(PayGroup.get("TPC"));
					} else {
						System.out.println(PayGroup.get(InputExcel[0][1]));
						Paygroup.sendKeys(PayGroup.get(InputExcel[0][1]));
					}
					Paygroup.submit();
				}
				WebElement Salarygrade = driver.findElement(By.xpath(LabelxpathInput(Objects.SalaryGrade)));
				Salarygrade.sendKeys(pos_deptloc_sal[2]);
				Salarygrade.submit();
				WebElement PositionNumber = driver.findElement(By.xpath(LabelxpathInput(Objects.PositionNumber)));
				PositionNumber.sendKeys(pos_deptloc_sal[0]);
				PositionNumber.submit();
				driver.findElement(By.id("HR_TBH_WRK_TBH_SAVE")).click();
				if (TemplateID.get(SearchTemplateId).equals("SBS_IN_HIRE_CON")) {
					wait_tbh.until(ExpectedConditions.visibilityOfElementLocated(By.id("HR_TBH_WRK_TBH_NO_SM_MATCH")))
							.click();
				}
				wait_tbh.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("//*[@id=\"win0divHR_TBH_WRK_HTMLAREA$6$\"]/div")));
				String Success = driver.findElement(By.xpath("//*[@id=\"win0divHR_TBH_WRK_HTMLAREA$6$\"]/div"))
						.getText();
				String[] emplidseprator = Success.split("is ");
				String[] dotremoval = emplidseprator[1].split(" .");
				int EmplID = Integer.parseInt(dotremoval[0]);
				System.out.println(EmplID);
				EmployeeIds.put(EmplID,
						a[HeaderMap.get("First Name")][Rowcount] + " " + a[HeaderMap.get("Last Name")][Rowcount]);
				System.out.println(Success);

				Assert.assertTrue(true);
				// To generate the log when the test case is passed

				// logger.log(LogStatus.PASS, "Hiring of "+InputExcel[0][0]+"
				// "+InputExcel[0][1]+" "+Rowcount+" in "+InputExcel[0][2]+" :
				// "+a[HeaderMap.get("First Name")][Rowcount]+" Successful! ID Generated
				// "+EmplID);
				logger.log(LogStatus.PASS, "Hiring of  " + InputExcel[0][1] + " " + Rowcount
						+ " from Template Based Hire page. New Employee Id: " + EmplID);
				// extent.endTest(logger);
				TBH_Result[Rowcount - 1] = EmplID + "&&" + pos_deptloc_sal[0] + "&&" + InputExcel[0][1] + "&&"
						+ InputExcel[0][2];
			} catch (Exception e) {
				if (driver.findElements(By.id("#ICOK")).size() > 0) {
					wait_tbh.until(ExpectedConditions.visibilityOfElementLocated(By.id("#ICOK"))).click();
				}
				e.printStackTrace();
				Assert.assertFalse(false);
				// To generate the log when the test case is passed
				// logger.log(LogStatus.FAIL, "Hiring of "+InputExcel[0][0]+"
				// "+InputExcel[0][1]+" "+Rowcount+" in "+InputExcel[0][2]+" :
				// "+a[HeaderMap.get("First Name")][Rowcount]+" Failed! "+e);
				logger.log(LogStatus.FAIL,
						"Hiring of  " + InputExcel[0][1] + " " + Rowcount + " from Template Based Hire page.");
				// extent.endTest(logger);
			}

		}

		return TBH_Result;
	}

	public static String[][] headerspace(String[][] a) {
		for (int i = 0; i < a[0].length; i++) {
			a[0][i] = a[i][0] + " ";
		}
		return a;
	}

	public static String LabelxpathInput(String a) {
		String xpath = "//input[@id=(//label[contains(text(), '" + a + "')]/@for)]";
		return xpath;
	}

	public static String LabelxpathSelect(String a) {
		String xpath = "//select[@id=(//label[contains(text(), '" + a + "')]/@for)]";
		return xpath;
	}

	public static String[][] QueryResultSplitter(String x, String[][] a) {
		String[][] output = null;
		int count = 0;

		for (int i = 0; i < a.length; i++) {
			System.out.println(a.length + " " + a[0].length + " " + a[i][0]);
			if (a[i][0].equalsIgnoreCase("dummy")) {
				count = i;
			} else {
				continue;
			}
		}
		if (x.equalsIgnoreCase("tbh")) {
			output = new String[count][a[0].length];
			for (int i = 0; i < count; i++) {
				for (int j = 0; j < a[0].length; j++) {
					output[i][j] = a[i][j];
				}
			}
		} else {
			System.out.println(a.length + " &&&& " + a[0].length);
			output = new String[a.length - count - 1][a[0].length];

			for (int i = 0; i < a.length - count - 1; i++) {
				for (int j = 0; j < a[0].length; j++) {
					output[i][j] = a[count + 1 + i][j];
				}
			}
		}

		return output;
	}

	/*
	 * public static void HeadcountReport(WebDriver driver) throws
	 * InterruptedException, IOException { WebDriverWait wait_headcount = new
	 * WebDriverWait(driver, 40, 100);
	 * mynavigation(Navigations.get("India Headcount Details"), driver);
	 * driver.switchTo().frame(Objects.frame);
	 * wait_headcount.until(ExpectedConditions.visibilityOfElementLocated(Objects.
	 * txt_headcount_run_cnrl_id)).sendKeys("HireAutomation");
	 * driver.findElement(Objects.btn_headcount_run_cnrl_id_search).click(); /*
	 * LocalDate date = LocalDate.now(); DateTimeFormatter formatter =
	 * DateTimeFormatter.ofPattern("dd/MM/yyyy"); //
	 * System.out.println(date.format(formatter)); WebElement asofdate =
	 * wait_headcount.until(ExpectedConditions.visibilityOfElementLocated(Objects.
	 * txt_headcount_asofdate)); asofdate.sendKeys(date.format(formatter));
	 * asofdate.submit(); WebElement run_btn =
	 * wait_headcount.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
	 * "//*[@id=\"PRCSRQSTDLG_WRK_LOADPRCSRQSTDLGPB\"]"))); run_btn.click();
	 * run_btn.submit(); WebElement okay_btn =
	 * wait_headcount.until(ExpectedConditions.visibilityOfElementLocated(Objects.
	 * btn_headcount_ok)); okay_btn.click(); String PrcInstance =
	 * wait_headcount.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
	 * "//*[@id=\"PRCSRQSTDLG_WRK_DESCR100\"]"))).getText(); String[] instanceNumber
	 * = PrcInstance.split(":"); Thread.sleep(3000); WebElement ProcessMonitor =
	 * wait_headcount.until(ExpectedConditions.visibilityOfElementLocated(By.
	 * linkText("Process Monitor"))); ProcessMonitor.click();
	 * 
	 * WebElement Instance =
	 * wait_headcount.until(ExpectedConditions.visibilityOfElementLocated(By.id(
	 * "PMN_DERIVED_PRCSINSTANCE"))); String[] instanceNumber = {"asdas", "907570"};
	 * //instanceNumber[1] = "907570"; Instance.sendKeys(instanceNumber[1]);
	 * 
	 * WebElement refresh_btn = driver.findElement(By.id("REFRESH_BTN"));
	 * refresh_btn.click(); // boolean flag = true; /*do { WebElement Status =
	 * driver.findElement(By.id("PMN_PRCSLIST_RUNSTATUSDESCR$0"));
	 * System.out.println(Status.getText()); String stat = Status.getText();
	 * 
	 * if (stat.equals("Processing") || stat.equals("Queued") ||
	 * stat.equals("Initiated") || stat.equals("Pending")) { Thread.sleep(10000);
	 * WebElement Rfrsh = driver.findElement(By.id("REFRESH_BTN")); Rfrsh.click(); }
	 * else if (stat.equals("Success") || stat.equals("No Success") ||
	 * stat.equals("Error")) { System.out.println("The process ended with " + stat);
	 * WebElement DistStatus =
	 * driver.findElement(By.id("PMN_PRCSLIST_DISTSTATUS$0"));
	 * System.out.println(DistStatus.getText());
	 * 
	 * 
	 * if (!DistStatus.getText().equals("Posted")) { WebElement Rfrsh =
	 * driver.findElement(By.id("REFRESH_BTN")); Rfrsh.click(); Thread.sleep(3000);
	 * flag = false; break; }
	 * 
	 * flag = false; }
	 * 
	 * else if (stat.equals("Cancelled")) {
	 * 
	 * System.out.println("Process is cancelled"); // break; // System.exit(1); }
	 * 
	 * } while (flag); Thread.sleep(3000);
	 * driver.findElement(By.xpath("//*[@id=\"PRCSDETAIL_BTN$0\"]")).click();
	 * 
	 * wait_headcount.until(ExpectedConditions.visibilityOfElementLocated(By.
	 * linkText("View Log/Trace"))).click();
	 * wait_headcount.until(ExpectedConditions.visibilityOfElementLocated(By.
	 * linkText("Z_HEADCNT_"+instanceNumber[1]+".xls"))).click(); int filefound = 0;
	 * while(filefound==0) { try { File file = new
	 * File("D:\\Profiles\\aniarora\\Downloads\\Z_HEADCNT_"+instanceNumber[1]+".xls"
	 * ); //FileInputStream fis = new FileInputStream(file); // POIFSFileSystem fs =
	 * new POIFSFileSystem(fis); filefound = 1; System.out.println("File Found");
	 * Desktop desktop = Desktop.getDesktop(); desktop.open(file); // workbook = new
	 * HSSFWorkbook(fs); // HSSFSheet sheet = workbook.getSheetAt(0); //
	 * System.out.println(sheet.getRow(5).getCell(7).getStringCellValue());
	 * //desktop.open(fis); } catch(FileNotFoundException ex) { filefound = 0; } } }
	 * /* @AfterMethod public void getResult(ITestResult result) throws Exception{
	 * if(result.getStatus() == ITestResult.FAILURE){ logger.log(LogStatus.FAIL,
	 * "Test Case Failed is "+result.getName()); logger.log(LogStatus.FAIL,
	 * "Test Case Failed is "+result.getThrowable()); //To capture screenshot path
	 * and store the path of the screenshot in the string "screenshotPath" //We do
	 * pass the path captured by this mehtod in to the extent reports using
	 * "logger.addScreenCapture" method.
	 * 
	 * //To add it in the extent report }else if(result.getStatus() ==
	 * ITestResult.SKIP){ logger.log(LogStatus.SKIP,
	 * "Test Case Skipped is "+result.getName()); } // ending test //endTest(logger)
	 * : It ends the current test and prepares to create HTML report
	 * extent.endTest(logger); }
	 */
	public static void endReport() {
		// writing everything to document
		// flush() - to write or update test information to your report.

		extent.flush();
		// Call close() at the very end of your session to clear all resources.
		// If any of your test ended abruptly causing any side-affects (not all logs
		// sent to ExtentReports, information missing), this method will ensure that the
		// test is still appended to the report with a warning message.
		// You should call close() only once, at the very end (in @AfterSuite for
		// example) as it closes the underlying stream.
		// Once this method is called, calling any Extent method will throw an error.
		// close() - To close all the operation

	}

	public static String[] returnpos(WebDriver driver, String[][] a, String empl_class)
			throws AWTException, InterruptedException {

		WebDriverWait wait = new WebDriverWait(driver, 25);
		mynavigation(Navigations.get("Position Management - PMD"), driver);
		String[][] position_data = QueryResultSplitter("Position", a);
		String[] position_nbr = new String[position_data[0].length];
		for (int i = 1; i < position_data[0].length; i++) {
			try {

				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("ptifrmtgtframe"));
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("PSPUSHBUTTONTBADD"))).click();
				// addbtn.sendKeys(runCntrlId);
				// wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Z_PMD_TBL_Z_POS_REQUEST_TYPE$0"))).click();
				Select reqType = new Select(wait.until(
						ExpectedConditions.visibilityOfElementLocated(By.name("Z_PMD_TBL_Z_POS_REQUEST_TYPE$0"))));
				// reqType.selectByValue("D");
				reqType.selectByValue("");
				Thread.sleep(2000);

				reqType.selectByValue("N");
				// reqType.submit();
				// new Robot().keyPress(KeyEvent.VK_TAB);
				Thread.sleep(2000);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Z_PMD_TBL_Z_POS_REQUEST_TYPE$0")))
						.click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Z_PMD_TBL_ASSIGNMENT_ID$0")))
						.sendKeys(new Random().nextInt(99999) + "");
				WebElement RegRegion = wait
						.until(ExpectedConditions.visibilityOfElementLocated(By.name("Z_PMD_TBL_REG_REGION$0")));
				RegRegion.sendKeys("IND");
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("processing")));
				// RegRegion.submit();
				WebElement businessunit = wait
						.until(ExpectedConditions.visibilityOfElementLocated(By.name("Z_PMD_TBL_BUSINESS_UNIT$0")));
				businessunit.sendKeys(position_data[0][i]);
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("processing")));
				// Thread.sleep(2000);

				WebElement dept = wait
						.until(ExpectedConditions.visibilityOfElementLocated(By.name("Z_PMD_TBL_DEPTID$0")));
				dept.sendKeys(position_data[1][i] + Keys.TAB);

				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("processing")));

				/*
				 * new Robot().keyPress(KeyEvent.VK_TAB); Thread.sleep(2000); new
				 * Robot().keyPress(KeyEvent.VK_TAB);
				 */
				/*
				 * WebElement company = wait.until(ExpectedConditions
				 * .visibilityOfElementLocated(By.xpath(
				 * "//div[@id='win0divZ_PMD_TBL_COMPANY$0']/span")));
				 * wait.until(ExpectedConditions.attributeContains(company, "class",
				 * "PSEDITBOX_DISPONLY"));
				 */

				// WebElement Manager =
				// wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Z_PMD_TBL_ADVISEE_ID$0")));
				// Manager.sendKeys("00000636151");
				// Manager.submit();
				WebElement jobcode = wait
						.until(ExpectedConditions.visibilityOfElementLocated(By.name("Z_PMD_TBL_JOBCODE$0")));
				jobcode.sendKeys(position_data[2][i] + Keys.TAB);
				// jobcode.submit();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("processing")));
				// WebElement dept =
				// wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Z_PMD_TBL_DEPTID$0")));
				// dept.sendKeys("DPB763");
				// wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("processing")));
				// dept.submit();
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("processing")));
				WebElement abs = wait
						.until(ExpectedConditions.visibilityOfElementLocated(By.name("Z_PMD_TBL_REG_TEMP$0")));
				new Select(abs).selectByValue("R");
				// abs.submit();
				WebElement ab = wait
						.until(ExpectedConditions.visibilityOfElementLocated(By.name("Z_PMD_TBL_FULL_PART_TIME$0")));
				new Select(ab).selectByValue("F");
				// ab.submit();
				WebElement postitle = wait
						.until(ExpectedConditions.visibilityOfElementLocated(By.name("Z_PMD_TBL_POSN_DESCR$0")));
				postitle.sendKeys(position_data[3][i]);
				// postitle.submit();

				// Manager.submit();

				wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Z_PMD_TBL_EMPLID_END$0")))
						.sendKeys(position_data[4][i]);

				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("processing")));
				WebElement Manager = wait
						.until(ExpectedConditions.visibilityOfElementLocated(By.name("Z_PMD_TBL_ADVISEE_ID$0")));
				Manager.sendKeys(position_data[4][i] + Keys.TAB);

				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("processing")));

				WebElement ID1 = wait
						.until(ExpectedConditions.visibilityOfElementLocated(By.name("Z_PMD_TBL_EMPLID_FROM$0")));
				ID1.sendKeys(position_data[4][i] + Keys.TAB);
				ID1.submit();
				WebElement ID2 = wait
						.until(ExpectedConditions.visibilityOfElementLocated(By.name("Z_PMD_TBL_EMPLID_NEW$0")));
				ID2.sendKeys(position_data[4][i] + Keys.TAB);
				ID2.submit();
				WebElement HRBPID = wait
						.until(ExpectedConditions.visibilityOfElementLocated(By.name("Z_PMD_TBL_AV_EMPLID$77$$0")));
				HRBPID.sendKeys(position_data[4][i] + Keys.TAB);
				HRBPID.submit();
				Thread.sleep(3000);
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("processing")));
				String dept_location = wait
						.until(ExpectedConditions
								.visibilityOfElementLocated(By.xpath("//*[@id=\"LOCATION_TBL_DESCR$0\"]")))
						.getText().substring(0, 3);
				dept_location.toUpperCase();
				String sal_grade = wait.until(
						ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"Z_PMD_TBL_GRADE$0\"]")))
						.getText();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("PSPUSHBUTTON"))).click();

				driver.switchTo().defaultContent();
				Thread.sleep(5000);

				String pos = wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("/html/body/div[8]/div[2]/div/div[2]/div[1]/span")))
						.getText();

				pos = pos.substring(pos.indexOf("-") + 1, pos.indexOf("-") + 10);
				Assert.assertTrue(true);
				// To generate the log when the test case is passed
				// logger.log(LogStatus.PASS, "Creation of Position: "+pos+" for JobCode:
				// "+position_data[2][i]+" and DeptID: "+position_data[1][i]);

				if (dept_location != "PUN" || dept_location != "CHE" || dept_location != "BAN") {
					dept_location = "NOI";
				}
				String pos1 =pos; 
				pos = pos + "&&" + dept_location + "&&" + sal_grade;
				driver.findElement(By.cssSelector("input[title = 'Ok (Enter)']")).click();
				// wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"#ICOK\"]"))).click();
				// *[@id="#ICOK"]
				position_nbr[i] = pos;
				if (i != position_data[0].length - 1) {
					System.out.println("PMD - i ="+i);
					navigateToHome(driver);
					mynavigation(Navigations.get("Position Management - PMD"), driver);
				}
				logger.log(LogStatus.PASS,
						"Creation of Position for " + empl_class + " " + i + ". New Position Number: " + pos1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// driver.findElement(By.id("#ICOK")).click();
				e.printStackTrace();
				Assert.assertFalse(false);
				// To generate the log when the test case is passed
				// logger.log(LogStatus.FAIL, "Creation of Position for JobCode:
				// "+position_data[2][i]+" and DeptID: "+position_data[1][i]+" "+e);
				logger.log(LogStatus.FAIL, "Creation of Position for " + empl_class + " " + i + ".");
				if (i != position_data[0].length - 1) {
					navigateToHome(driver);
					mynavigation(Navigations.get("Position Management - PMD"), driver);
				}
				driver.findElement(By.id("ptpopupmsgbtn2")).click();
			}
		}
		return position_nbr;

	}

	public static String[][] read(int i) throws IOException {

		String[][] obj = null;
		File file = new File(localDirPath + fileName);
		FileInputStream inputStream = null;
		Workbook readWorkbook = null;
		try {
			inputStream = new FileInputStream(file);

			readWorkbook = new XSSFWorkbook(inputStream);

			Sheet readSheet = readWorkbook.getSheet("Hire");
			int rowCount = readSheet.getLastRowNum() - readSheet.getFirstRowNum();
			System.out.println("rowcount=" + readSheet.getLastRowNum());
			Row row1 = readSheet.getRow(readSheet.getFirstRowNum());
			int cellCount = row1.getLastCellNum();
			obj = new String[rowCount][cellCount];

			// for (int i = readSheet.getFirstRowNum()+1; i <=readSheet.getLastRowNum();
			// i++) {
			Row row = readSheet.getRow(i);
			System.out.println("rowcount=" + i);
			for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
				System.out.println("j=" + row.getLastCellNum());
				obj[0][j] = row.getCell(j).getStringCellValue();
				System.out.println(obj[0][j]);
			}
			// }

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			// inputStream.close();
			// readWorkbook.close();
		}

		return obj;

	}

	public Sheet openFile(String sheetname) {
		File file = new File(localDirPath + fileName);
		FileInputStream inputStream;

		Workbook readWorkbook;
		Sheet sheet = null;
		try {
			inputStream = new FileInputStream(file);

			readWorkbook = new XSSFWorkbook(inputStream);

			sheet = readWorkbook.getSheet(sheetname);

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		}
		return sheet;
	}

	/*
	 * public static void HeadCountReader(WebDriver driver,String[] tbhresult)
	 * throws InterruptedException { int rownum = 0;
	 * mynavigation(Navigations.get("Query Manager"),driver);
	 * driver.switchTo().frame(Objects.frame);
	 * driver.findElement(Objects.txt_query_search).sendKeys(
	 * "Z_IND_HEADCOUNT_MGR_PB");
	 * driver.findElement(Objects.btn_query_search).click(); WebDriverWait wait =
	 * new WebDriverWait(driver, 60);
	 * wait.until(ExpectedConditions.visibilityOfElementLocated(Objects.
	 * btn_query_edit)).click();
	 * wait.until(ExpectedConditions.visibilityOfElementLocated(Objects.
	 * lbl_query_name));
	 * wait.until(ExpectedConditions.visibilityOfElementLocated(Objects.
	 * btn_run_query)).click(); driver.switchTo().defaultContent();
	 * wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("ptModFrame_0")
	 * ); wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
	 * "/html/body/form/div[4]/table/tbody/tr/td[2]/div/input"))).sendKeys(
	 * InputExcel[0][2]);
	 * wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
	 * "//*[@id=\"#ICOK\"]"))).click();
	 * wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(Objects.frame))
	 * ; wait.until(ExpectedConditions.visibilityOfElementLocated(By.
	 * linkText("View All"))).click(); String Count =
	 * wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
	 * "//*[@id=\"win0divQRY_VIEWER_WRK_HTMLAREA\"]/div/table[1]/tbody/tr/td[2]/span[2]"
	 * ))).getText(); String[] resultCount = Count.split("of ");
	 * System.out.println(resultCount[1]);
	 * 
	 * for(int i=0;i<tbhresult.length;i++) { String[] TBHValues =
	 * tbhresult[i].split("&&");
	 * System.out.println(wait.until(ExpectedConditions.visibilityOfElementLocated(
	 * By.xpath("//*[contains(text(), '"+"00000"+TBHValues[0]+"')]"))).getTagName())
	 * ; for(int j=1;j<=Integer.parseInt(resultCount[1]);j++) {
	 * 
	 * if(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
	 * "//*[@id=\"win0divQRY_VIEWER_WRK_HTMLAREA\"]/div/table[2]/tbody/tr/td/table/tbody/tr["
	 * +rownum+"]/td[3]"))).getText().contains(TBHValues[2]) &&
	 * wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
	 * "//*[@id=\"win0divQRY_VIEWER_WRK_HTMLAREA\"]/div/table[2]/tbody/tr/td/table/tbody/tr["
	 * +rownum+"]/td[5]"))).getText()==TBHValues[1]) { System.out.println("Pass");
	 * logger.log(LogStatus.PASS,
	 * "Test Case Passed For Headcount Report for ID "+TBHValues[0]+" Name "
	 * +TBHValues[2]); } else { System.out.println("Fail");
	 * logger.log(LogStatus.FAIL,
	 * "Test Case Failed For Headcount Report for ID "+TBHValues[0]+" Name "
	 * +TBHValues[2]); }
	 * 
	 * 
	 * } }
	 */
	public static void HeadCountReader(WebDriver driver, String[] tbhresult, String a) throws InterruptedException {

		// driver.get("https://soprasteria-hris-uat.opc.oracleoutsourcing.com/psc/SERAHTST/EMPLOYEE/HRMS/q/?ICAction=ICQryNameURL=PUBLIC.Z_IND_HEADCOUNT_MGR_PB");
		driver.get(Methods.Environment_Details.get("URLHC"));

		WebDriverWait wait = new WebDriverWait(driver, 60);
	
		for (int i = 0; i < tbhresult.length; i++) {
			try {
			System.out.println(i);
			System.out.println(tbhresult[i]);
			String[] TBHValues = new String[4];
			TBHValues = tbhresult[i].split("&&");
			System.out.println(TBHValues[0]);
			WebElement QueryInput = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("InputKeys_EMPLID")));
			QueryInput.clear();
			QueryInput.sendKeys("00000" + TBHValues[0]);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"#ICOK\"]"))).click();
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("processing")));
			

				Assert.assertEquals(wait
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.xpath("//*[@id=\"win0divQUERYRESULT\"]/table/tbody/tr[2]/td/table/tbody/tr[2]/td[5]")))
						.getText(), TBHValues[1].trim());
				Assert.assertEquals(wait
						.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
								"//*[@id=\"win0divQUERYRESULT\"]/table/tbody/tr[2]/td/table/tbody/tr[2]/td[13]")))
						.getText(), TBHValues[3]);
				if (TBHValues[2].equals("Trainee") || TBHValues[2].equals("Intern")) {
					Assert.assertEquals(wait
							.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
									"//*[@id=\"win0divQUERYRESULT\"]/table/tbody/tr[2]/td/table/tbody/tr[2]/td[14]")))
							.getText(), TBHValues[2].substring(0, 1));
				} else {
					Assert.assertEquals(wait
							.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
									"//*[@id=\"win0divQUERYRESULT\"]/table/tbody/tr[2]/td/table/tbody/tr[2]/td[14]")))
							.getText(), TBHValues[2]);
				}
				// logger.log(LogStatus.PASS, "Headcount Report Validation for ID
				// "+TBHValues[0]+" Postion Number "+TBHValues[1]);
				logger.log(LogStatus.PASS, "Validation of Headcount report for "+TBHValues[2] + " " + (i+1)+ ". Employee ID: " + TBHValues[0]
						+ " and Postion Number: " + TBHValues[1] );
			} catch (Exception ex) {
				// logger.log(LogStatus.FAIL, "Headcount Report Validation for ID
				// "+TBHValues[0]+" Postion Number "+TBHValues[1]);
				logger.log(LogStatus.FAIL, "Validation of Headcount report for "+a +" " + (i+1));
			}
		}
	}

	public static int RowCounterExcel() throws IOException {

		File file = new File(localDirPath + fileName);
		FileInputStream inputStream = null;

		inputStream = new FileInputStream(file);

		Workbook readWorkbook = new XSSFWorkbook(inputStream);

		Sheet readSheet = readWorkbook.getSheet("Hire");
		int rowCount = readSheet.getLastRowNum() - readSheet.getFirstRowNum();
		return rowCount;
	}

public static void navigateToHome(WebDriver driver) {
	
	driver.findElement(By.linkText("Home")).click();	
}
static public void zoomInZoomOut(String value,WebDriver driver){
JavascriptExecutor js = (JavascriptExecutor) driver;
js.executeScript("document.body.style.zoom='" + value +"'");
}

}