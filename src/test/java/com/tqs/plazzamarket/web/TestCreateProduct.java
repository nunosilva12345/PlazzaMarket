package com.tqs.plazzamarket.web;

import java.util.concurrent.TimeUnit;

import com.tqs.plazzamarket.entities.Category;
import com.tqs.plazzamarket.repositories.CategoryRepository;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.bonigarcia.wdm.WebDriverManager;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class TestCreateProduct {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @Autowired
    private CategoryRepository categoryRepository;

    @Before
    public void setUp() throws Exception {
        Category category = new Category("Flowers");
        categoryRepository.saveAndFlush(category);

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");

        try {
            newChromeSession(chromeOptions);
        } catch (SessionNotCreatedException e) {
            WebDriverManager.chromedriver().setup();
            newChromeSession(chromeOptions);
        }
    }

    @Test
    public void testCreateProduct() throws Exception {
        String url = String.format("http://localhost:%d/createproduct", port);
        driver.get(url);
        driver.findElement(By.id("name")).clear();
        driver.findElement(By.id("name")).sendKeys("Batata");
        new Select(driver.findElement(By.id("category"))).selectByVisibleText("Flowers");
        driver.findElement(By.id("price")).clear();
        driver.findElement(By.id("price")).sendKeys("8");
        driver.findElement(By.id("quantity")).clear();
        driver.findElement(By.id("quantity")).sendKeys("4");
        driver.findElement(By.id("description")).clear();
        driver.findElement(By.id("description")).sendKeys("test");
        driver.findElement(By.id("submit")).click();
        assertEquals("Product added with success!", closeAlertAndGetItsText());
        // WebDriverWait wait = new WebDriverWait(driver, 60);
        // Assert.assertTrue(wait.until(ExpectedConditions.attributeContains(By.id("success-div"), "class", "d-block")));
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
            WebDriverWait wait = new WebDriverWait(driver, 60);
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }

    private void newChromeSession(ChromeOptions chromeOptions) throws Exception {
        driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }
}