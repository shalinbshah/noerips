
import org.openqa.selenium.By
import org.openqa.selenium.WebElement


public class UserForm {

	WebElement formComponent;

	public static String compContainerLoc = "//div[contains(@id,'UserEditWindow')]";
	public static String firstNameLoc = "//input[@name='firstName']";
	public static String lastNameLoc = "//input[@name='lastName']";
	public static String emailLoc = "//input[@name='email']";
	public static String phoneLoc = "//input[@name='phone']";
	public static String roleLoc = "//input[@name='permissionRole']";
	public static String buttonSaveLoc = "//button[contains(.,'Save')]";
	public static String buttonCancelLoc = "//button[contains(.,'Cancel')]";


	public  UserForm(String loc){
		formComponent=
				SpireonTestBase.getWebDriver().findElement(By.xpath(loc));
	}

	public  UserForm(){
	}

	public String getFirstName(){
		String firstName = SpireonTestBase.getWebDriver().findElement(By.xpath(compContainerLoc+firstNameLoc)).getAttribute("value");
		return firstName;
	}
	public String getLastName(){
		String lastName=	SpireonTestBase.getWebDriver().findElement(By.xpath(compContainerLoc+lastNameLoc)).getAttribute("value");
		return lastName;
	}
	public String getEmail(){
		String email=SpireonTestBase.getWebDriver().findElement(By.xpath(compContainerLoc+emailLoc)).getAttribute("value");
		return email;
	}

	public String getPhone(){
		String phone=	SpireonTestBase.getWebDriver().findElement(By.xpath(compContainerLoc+phoneLoc)).getAttribute("value");
		return phone;
	}

	public String getRole(){
		String phone=	SpireonTestBase.getWebDriver().findElement(By.xpath(compContainerLoc+roleLoc)).getAttribute("value");
		return phone;
	}

	public void closeInfoPopup(){
		SpireonTestBase.getWebDriver().findElement(By.xpath(compContainerLoc+buttonCancelLoc)).click();
		SpireonTestBase.pause(10000);
	}
}
