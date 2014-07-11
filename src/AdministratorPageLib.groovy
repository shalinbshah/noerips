import java.util.concurrent.TimeUnit

import javax.script.ScriptException;

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions


public class AdministratorPageLib {



	// https://groups.google.com/forum/#!topic/robotframework-users/kZDz52mw8JU
	// http://stackoverflow.com/questions/18220386/robotframework-selenium2library-and-external-libraries-pass-webdriver
	// http://www.valeriobruno.it/blog3/2013/04/28/writing-acceptance-test-with-robot-framework-in-java/
	public static String dataColLocFormat = "//div[@id='adminUsersGrid-body']//table[contains(@class,'x-grid-table x-grid-table-resizer')]//tr/td[%d]";
	public static String headerColsLoc = "//div[@id='adminUsersGrid']//div[contains(@class,'x-column-header') and contains(@id,'gridcolumn') and not(contains(@class,'inner'))and not(contains(@class,'x-column-header-trigger'))]";;
	public static String headerColsNameLocFormat = "//div[@id='adminUsersGrid']//div[contains(@class,'x-column-header') and contains(@id,'gridcolumn') and not(contains(@class,'inner'))and not(contains(@class,'x-column-header-trigger')) and contains(.,'%s')]";
	public static String headerColsIndexLocFormat = "//div[@id='adminUsersGrid']//div[contains(@class,'x-column-header') and contains(@id,'gridcolumn') and not(contains(@class,'inner'))and not(contains(@class,'x-column-header-trigger'))][%d]";
	public static String rowAndColFormat = "//div[@id='adminUsersGrid-body']//table[contains(@class,'x-grid-table x-grid-table-resizer')]//tr[./td[%d][contains(.,'%s')]][%d]";
	public static String rowFormat = "//div[@id='adminUsersGrid-body']//table[contains(@class,'x-grid-table x-grid-table-resizer')]//tr[contains(.,'%s')]";
	public static String rowEditLoc = ".//div[contains(@id,'adminUsersGrid')]//button[contains(@id,'button') and contains(.,'Edit')]";

	public static final boolean ROBOT_CONTINUE_ON_FAILURE = true;
	public static final boolean ROBOT_EXIT_ON_FAILURE = true;

	private static WebDriver getWebDriver() {
		try {
			WebDriver driver = SpireonTestBase.getWebDriver();
			return driver;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	private static void mouseOverViaJS(WebElement element) {
		String javaScriptMouseOver = "var evObj = document.createEvent('MouseEvents');evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);arguments[0].dispatchEvent(evObj);";
		JavascriptExecutor executor = (JavascriptExecutor) getWebDriver();
		executor.executeScript(javaScriptMouseOver, element);
	}

	private static void mouseClickViaJS(WebElement element) {
		String javaScriptClick = "var evObj = document.createEvent('MouseEvents');evObj.initMouseEvent(\"click\", true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);arguments[0].dispatchEvent(evObj);";
		JavascriptExecutor executor = (JavascriptExecutor) getWebDriver();
		executor.executeScript(javaScriptClick, element);
	}

	public static void LoginWith(String username, String password) {
		getWebDriver().manage().window().maximize();
		getWebDriver().manage().timeouts()
				.pageLoadTimeout(90, TimeUnit.SECONDS);
		getWebDriver().manage().timeouts()
				.setScriptTimeout(30, TimeUnit.SECONDS);
		getWebDriver().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		getWebDriver().get(
				"http://amw1v2localfleet1.stage.spireon.com/home/signout");
		getWebDriver().findElement(By.id("username")).sendKeys(username);
		getWebDriver().findElement(By.id("password")).sendKeys(password);
		getWebDriver().findElement(By.xpath("//div[@id='login-button']/input"))
				.click();

	}


	public static void NavigateToTab(String tabLabel) {
		getWebDriver().findElement(
				By.xpath("//div[@id='adminCardButton']//button")).click();
	}

	public static void openAdminSection(String name) {
		String loc = String.format("//table//td//span[contains(.,'%s')]", name);
		getWebDriver().findElement(By.xpath(loc)).click();
		pause(5000);
	}

	public static void sortColumnAscending(String columnName) {
		WebElement element = getColumnHeaderEle(columnName);
		element.click();
		pause(3000);
		if (!element.getAttribute("class").contains("x-column-header-sort-ASC")) {
			element.click();
		}
		openColumnSortingPopup(columnName);
		// getWebDriver().findElement(
		// By.xpath("//span[contains(.,'Sort Ascending')]")).click();
	}

	public static void sortColumnDescending(String columnName) {

		WebElement element = getColumnHeaderEle(columnName);
		element.click();
		pause(3000);
		if (!element.getAttribute("class")
		.contains("x-column-header-sort-DESC")) {
			element.click();
		}
		openColumnSortingPopup(columnName);
		// getWebDriver().findElement(
		// By.xpath("//span[contains(.,'Sort Descending')]")).click();
	}

	private static WebElement getColumnHeaderEle(String columnName) {
		String loc = String.format(headerColsNameLocFormat, columnName);
		WebDriver driver = getWebDriver();
		WebElement colheader = driver.findElement(By.xpath(loc));
		return colheader;
	}



	public static void openColumnSortingPopup(String columnName) {
		pause(10000);
		WebElement element = getColumnHeaderEle(columnName);
		WebDriver driver = getWebDriver();
		Actions action = new Actions(driver);
		println ("mouseover element via webdriver")

		action.moveToElement(element).build().perform();
		String loc = String.format(headerColsNameLocFormat, columnName);
		WebElement we = driver.findElement(By
				.xpath(loc+"//div[contains(@id,'triggerEl')]"));
		mouseOverViaJS(element);
		pause(1000);
		String id = we.getAttribute("id");
		we = driver.findElement(By.id(id));
		mouseOverViaJS(we);
		pause(1000);
		mouseClickViaJS(we);
		pause(2000);
	}

	public static int getNumberOfRows(){
		return getColumnData(3).size();
	}

	public static int getNumberOfColumns() {
		pause(2000);

		List<WebElement> elements = getWebDriver().findElements(
				By.xpath(headerColsLoc));
		return elements.size();
	}

	public static WebElement getRow(String uniqueUserInfo) {
		WebElement element = getWebDriver().findElement(
				By.xpath(String.format(rowFormat, uniqueUserInfo)));
		return element;
	}

	public static ArrayList<String> getColumnData(int i) {
		ArrayList<String> texts = new ArrayList<String>();
		String loc = String.format(dataColLocFormat, i);
		List<WebElement> elements = getWebDriver().findElements(By.xpath(loc));
		for (WebElement element : elements) {
			texts.add(element.getText().trim().toLowerCase());
		}
		println("Data in column # " + i + " is " + texts);
		return texts;
	}

	public static ArrayList<String> getColumnData(String columnName) {
		pause(2000);
		int colIndex = getColumnIndex(columnName);
		println("Data in column is as follows" + columnName);
		return getColumnData(colIndex);
	}

	public static int getColumnIndex(String columnName) {
		int colIndex=0;
		String loc = "//div[@id='adminUsersGrid']//div[contains(@class,'x-column-header') and contains(@id,'gridcolumn') and not(contains(@class,'inner'))and not(contains(@class,'x-column-header-trigger'))]";
		List<WebElement> elements = getWebDriver().findElements(By.xpath(loc));
		for (int i = 0; i < elements.size(); i++) {
			if (elements.get(i).getText().trim().equalsIgnoreCase(columnName)) {
				colIndex = i + 1;
				break;
			}

		}
		return colIndex;
	}

	public static void openUserInformation(String uniqueUserInfo) {
		WebElement rowSelector = getRow(uniqueUserInfo).findElement(By.xpath(".//td[1]"));
		rowSelector.click();
		getWebDriver().findElement(By.xpath(rowEditLoc)).click();
		pause(10000);
	}

	public static boolean verifyPagingSize(int size) {
		ArrayList<String> recordsOnPage = getColumnData(2);
		boolean b = recordsOnPage.size() < size;

		if(b)
			println("Paging size is " + recordsOnPage.size());
		else
			failTestWithMessage("Paging size is " + recordsOnPage.size()+", Paging Size Should be 25");
		return b;
	}

	public static void verifySortingFunctionality(String columnName) {
		ArrayList<String> initialColData1 = getColumnData(columnName);
		ArrayList<String> initialColData2 = new ArrayList<String>();
		initialColData2.addAll(initialColData1);

		sortColumnDescending(columnName);
		ArrayList<String> descendingColData = getColumnData(columnName);
		Collections.sort(initialColData1);
		Collections.reverse(initialColData1);
		if(initialColData1.equals(descendingColData))
			println("Column " + columnName+" is Sorted in Descending Order Properly");
		else
			failTestWithMessage("Column " + columnName+" is Sorted in Descending Order Properly");

		sortColumnAscending(columnName);
		ArrayList<String> ascendingColData = getColumnData(columnName);
		Collections.sort(initialColData2);
		if(initialColData2.equals(ascendingColData))
			println("Column " + columnName+" is Sorted in Ascending Order Properly");
		else
			failTestWithMessage("Column " + columnName+" is Sorted in Ascending Order Properly");

	}



	public static void verifyRowWithDataPresentInColumn(String columnName, String data) {
		pause(2000);
		ArrayList<String> datas = getColumnData(columnName);
		boolean isPresent=false;
		for(String dataa:datas){
			if (dataa.equalsIgnoreCase(data))
			{isPresent=true;
				break;}

		}
		if(isPresent)
			println(columnName + " contains " + data);
		else {
			failTestWithMessage(columnName + " does not contains " + data);}
	}

	public static void hideColumnAndVerifyVisibility(String columnName) {
		pause(1000);
		WebDriver driver=getWebDriver();
		WebElement element = driver.findElement(By.xpath("//span[contains(@id,'menuitem') and contains(.,'Columns')]"));
		element.click();
		element = driver.findElement(By.xpath(String.format(".//div[contains(@id,'menucheckitem') and contains(.,'%s')]",columnName)));
		if(element.getAttribute("class").contains("x-menu-item-checked"))
			element.click();
		pause(1000);
		element = getColumnHeaderEle(columnName);
		if(element.displayed)
			failTestWithMessage(columnName + " is visible after setting it unchecked from columns menu" );
		else
			println(columnName + " is invisible after setting it unchecked from columns menu" )
	}

	public static void showColumnAndVerifyVisibility(String columnName) {
		pause(1000);
		WebDriver driver=getWebDriver();
		WebElement element = driver.findElement(By.xpath("//span[contains(@id,'menuitem') and contains(.,'Columns')]"));
		element.click();
		element = driver.findElement(By.xpath(String.format(".//div[contains(@id,'menucheckitem') and contains(.,'%s')]",columnName)));
		if(element.getAttribute("class").contains("x-menu-item-unchecked"))
			element.click();
		pause(2000);
		element = getColumnHeaderEle(columnName);
		if(!element.displayed)
			failTestWithMessage(columnName + " is invisible after setting it checked from columns menu" );
		else
			println(columnName + " is visible after setting it checked from columns menu" )
	}

	public static void verifyUniqueRowsInColumn(String columnName) {
		ArrayList<String>	datas = getColumnData(columnName);
		HashSet<String> set = new HashSet<String>(datas);
		if(datas.size() != set.size())
		{
			failTestWithMessage("Column "+columnName+" values ::: " + datas +" ::: contains duplicate values");
		}
	}



	public static void verifyColumnsInOrder(String columnNamesCommaSeperated) {
		WebDriver webDriver = getWebDriver();
		String[] columns = columnNamesCommaSeperated.split(",");
		int i = 2;
		String columnNamesCommaSeperatedActual=""
		for (String column : columns) {
			String loc = String.format(headerColsIndexLocFormat, i);
			WebElement ele = webDriver.findElement(By.xpath(loc));
			if (ele.getText().trim().equalsIgnoreCase(column)) {
				columnNamesCommaSeperatedActual =columnNamesCommaSeperatedActual+ele.getText().trim();
			} else
				failTestWithMessage("Order of columns should be : " +columnNamesCommaSeperated+ele.getText() + " != " + column
						+ " for index " + i);

			i++;
		}
		if(columnNamesCommaSeperated.equalsIgnoreCase(columnNamesCommaSeperatedActual))
		{
			println("Order of columns should be : " +columnNamesCommaSeperated+"Actual Is " + columnNamesCommaSeperatedActual);
		}
		else{
			failTestWithMessage("Order of columns should be : " +columnNamesCommaSeperated+"Actual Is " + columnNamesCommaSeperatedActual);
		}
	}




	public static void verifyColumnDragAndDrop(String columnNameToMove,String afterColumnName) {
		int initialIndex=getColumnIndex(columnNameToMove);
		WebDriver webdriver= getWebDriver();
		WebElement draggable = getColumnHeaderEle(columnNameToMove);
		WebElement to = getColumnHeaderEle(afterColumnName);
		new Actions(webdriver).dragAndDrop(draggable, to).build().perform();
		int newIndex=getColumnIndex(columnNameToMove);
		if(initialIndex==newIndex)
			failTestWithMessage(columnNameToMove + " is not draggable to put after column "+afterColumnName);
		else
			println (columnNameToMove +" is draggable from index "+initialIndex +" to "+newIndex);
	}



	public static void verifyUserInformation(String uniqueUserInfo,String columnName) {
		UserDataBean dataBean = new UserDataBean();
		UserForm userForm = new UserForm();
		openUserInformation(uniqueUserInfo);

		dataBean.setFirstName(userForm.getFirstName());
		dataBean.setLastName(userForm.getLastName());
		dataBean.setEmail(userForm.getEmail());
		dataBean.setPhone(userForm.getPhone());
		dataBean.setRole(userForm.getRole());
		println "UserInformation from PopUp = "+dataBean.toString();
		userForm.closeInfoPopup();

		if(columnName.equalsIgnoreCase("Name"))
		{
			verifyRowWithDataPresentInColumn(columnName, dataBean.getFirstName()+" " + dataBean.getLastName());
			verifyRowColData(uniqueUserInfo,columnName, dataBean.getFirstName()+" " + dataBean.getLastName());
		}
		else if(columnName.equalsIgnoreCase("Email"))
		{
			verifyRowWithDataPresentInColumn(columnName, dataBean.getEmail());
			verifyRowColData(uniqueUserInfo, columnName, dataBean.getEmail());
		}
		else if(columnName.equalsIgnoreCase("Phone"))
		{
			verifyRowWithDataPresentInColumn(columnName, dataBean.getPhone());
			verifyRowColData(uniqueUserInfo, columnName, dataBean.getPhone());

		}
		else if(columnName.equalsIgnoreCase("User Role"))
		{
			verifyRowWithDataPresentInColumn(columnName, dataBean.getRole());
			verifyRowColData(uniqueUserInfo, columnName, dataBean.getRole());
		}
	}
	public static void verifyUserInformation(String uniqueUserInfo) {
		UserDataBean dataBean = new UserDataBean();
		UserForm userForm = new UserForm();
		openUserInformation(uniqueUserInfo);

		dataBean.setFirstName(userForm.getFirstName());
		dataBean.setLastName(userForm.getLastName());
		dataBean.setEmail(userForm.getEmail());
		dataBean.setPhone(userForm.getPhone());

		println "UserInformation from PopUp = "+dataBean.toString();
		userForm.closeInfoPopup();

		WebElement row = getRow(uniqueUserInfo);
		String rowContent = row.getText();
		if(rowContent.contains(dataBean.getFirstName()))
			println "First Name "+dataBean.getFirstName() +" is proper for user with details : "+uniqueUserInfo+", Details Shown are "+ dataBean.toString();
		else
			failTestWithMessage("First Name "+dataBean.getFirstName()+" is not proper for user with details : "+uniqueUserInfo+"; Actual is "+ dataBean.toString());

		if(rowContent.contains(dataBean.getLastName()))
			println "Last Name "+dataBean.getLastName()+" is proper for user with details : "+uniqueUserInfo+", Details Shown are "+ dataBean.toString();
		else
			failTestWithMessage("Last Name "+dataBean.getLastName()+" is not proper for user with details : "+uniqueUserInfo+"; Actual is "+ dataBean.toString());

		if(rowContent.contains(dataBean.getEmail()))
			println "Email is "+dataBean.getEmail()+" proper for user with details : "+uniqueUserInfo+", Details Shown are "+ dataBean.toString();
		else
			failTestWithMessage("Email is "+dataBean.getEmail()+" not proper for user with details : "+uniqueUserInfo+"; Actual is "+ dataBean.toString());
	}

	public static void verifyRowColData(String rowData,String colName,String dataToVerify) {
		int colIndex=getColumnIndex(colName);
		WebElement rowcoldata = getRow(rowData).findElement(By.xpath(".//td["+colIndex+"]"));
		if(rowcoldata.getText().equalsIgnoreCase(dataToVerify)){
			println ("Row with data "+ rowData+" contains " + dataToVerify+" in column "+ colName);
		}else{
			failTestWithMessage("Row with data "+ rowData+" does not contain " + dataToVerify+" in column "+ colName);
		}
		pause(3000);
	}



	public static void failTestWithMessage(String msg) {
		SpireonTestBase.captureScreenShot();
		throw new VerificationError(msg);
	}

	public static void pause(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
