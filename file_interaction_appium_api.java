import io.appium.java_client.android.AndroidDriver;
import java.io.File;
import java.net.URL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

public class File_Interaction {
    private static final String APPIUM = "http://localhost:4723/wd/hub";
    private static final String ANDROID_PHOTO_PATH = "/mnt/sdcard/Pictures";

    private AndroidDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "Android Emulator");
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("appPackage", "com.google.android.apps.photos");
        caps.setCapability("appActivity", ".home.HomeActivity");
        caps.setCapability("noReset", true);
        driver = new AndroidDriver(new URL(APPIUM), caps);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testPhotos() throws Exception {
        File image = new File("src/test/resources/image.jpg").getAbsoluteFile();
        driver.pushFile(ANDROID_PHOTO_PATH + "/" + image.getName(), image);
    }
}
