import org.openqa.selenium.WebDriver;

public class SpireonTestBase {
	public static WebDriver getWebDriver() {
		try {
			WebDriver driver = Selenium2Library.getLibraryInstance()
					.getBrowserManagement().getCurrentWebDriver();
			return driver;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void failTestWithMessage(String msg) {
		captureScreenShot();
		throw new VerificationError(msg);
	}

	public static void pause(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void captureScreenShot() {
		try {
			Selenium2Library.getLibraryInstance().getScreenshot()
					.capturePageScreenshot();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
