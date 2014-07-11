import org.openqa.selenium.remote.server.handler.CaptureScreenshot;

public class VerificationError extends RuntimeException {
	public static final boolean ROBOT_CONTINUE_ON_FAILURE = true;

	public VerificationError(String msg){
		throw new AssertionError(msg);
	}
}