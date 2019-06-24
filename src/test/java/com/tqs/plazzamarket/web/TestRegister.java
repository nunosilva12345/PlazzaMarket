package com.tqs.plazzamarket.web;

import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class TestRegister {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @BeforeClass
    public static void setupClass() {
        // WebDriverManager.chromedriver().setup();
    }

    @Before
    public void setUp() throws Exception {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testRegister() throws Exception {
        String url = String.format("http://localhost:%d/register", port);
        driver.get(url);
        driver.findElement(By.id("firstname")).clear();
        driver.findElement(By.id("firstname")).sendKeys("Pedro");
        driver.findElement(By.id("lastname")).clear();
        driver.findElement(By.id("lastname")).sendKeys("Cavadas");
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("lengors");
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("pedrocavadas@ua.pt");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("pedroxp1");
        driver.findElement(By.id("address")).clear();
        driver.findElement(By.id("address")).sendKeys("1234 Main St");
        driver.findElement(By.id("zipCode")).clear();
        driver.findElement(By.id("zipCode")).sendKeys("3720-400");
        driver.findElement(By.id("submit")).click();
        assertEquals("User registered with success!", closeAlertAndGetItsText());
        driver.get(url);
        driver.findElement(By.id("firstname")).clear();
        driver.findElement(By.id("firstname")).sendKeys("Pedro");
        driver.findElement(By.id("lastname")).clear();
        driver.findElement(By.id("lastname")).sendKeys("Cavadas");
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("lengors");
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("pedrocavadas@ua.pt");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("pedroxp1");
        driver.findElement(By.id("address")).clear();
        driver.findElement(By.id("address")).sendKeys("1234 Main St");
        driver.findElement(By.id("zipCode")).clear();
        driver.findElement(By.id("zipCode")).sendKeys("3720-300");
        driver.findElement(By.id("submit")).click();
        assertThat("User registered with success!", is(not(closeAlertAndGetItsText())));
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } catch (TimeoutException e) {
            return null;
        } finally {
            acceptNextAlert = true;
        }
    }
}