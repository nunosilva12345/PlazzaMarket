/*package com.tqs.plazzamarket.web;

import java.util.concurrent.TimeUnit;

import com.tqs.plazzamarket.entities.Category;
import com.tqs.plazzamarket.repositories.CategoryRepository;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class TestCreateProduct {

    @LocalServerPort
	private int port;

    private WebDriver driver;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeClass
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @Before
    public void setUp() throws Exception {
        Category category = new Category("Flowers");
        categoryRepository.saveAndFlush(category);
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testCreateProduct() throws Exception {
        String url = String.format("http://localhost:%d/createproduct", port);
        driver.get(url);
        driver.findElement(By.id("name")).click();
        driver.findElement(By.id("name")).clear();
        driver.findElement(By.id("name")).sendKeys("Batata");
        driver.findElement(By.id("category")).click();
        new Select(driver.findElement(By.id("category"))).selectByVisibleText("Flowers");
        driver.findElement(By.id("category")).click();
        driver.findElement(By.id("price")).clear();
        driver.findElement(By.id("price")).sendKeys("1");
        driver.findElement(By.id("price")).click();
        driver.findElement(By.id("price")).clear();
        driver.findElement(By.id("price")).sendKeys("2");
        driver.findElement(By.id("price")).click();
        // ERROR: Caught exception [ERROR: Unsupported command [doubleClick | id=price | ]]
        driver.findElement(By.id("price")).clear();
        driver.findElement(By.id("price")).sendKeys("7");
        driver.findElement(By.id("price")).click();
        driver.findElement(By.id("price")).clear();
        driver.findElement(By.id("price")).sendKeys("8");
        driver.findElement(By.id("price")).click();
        driver.findElement(By.id("quantity")).clear();
        driver.findElement(By.id("quantity")).sendKeys("1");
        driver.findElement(By.id("quantity")).click();
        driver.findElement(By.id("quantity")).clear();
        driver.findElement(By.id("quantity")).sendKeys("2");
        driver.findElement(By.id("quantity")).click();
        // ERROR: Caught exception [ERROR: Unsupported command [doubleClick | id=quantity | ]]
        driver.findElement(By.id("quantity")).clear();
        driver.findElement(By.id("quantity")).sendKeys("3");
        driver.findElement(By.id("quantity")).click();
        driver.findElement(By.id("quantity")).clear();
        driver.findElement(By.id("quantity")).sendKeys("4");
        driver.findElement(By.id("quantity")).click();
        driver.findElement(By.id("description")).click();
        driver.findElement(By.id("description")).clear();
        driver.findElement(By.id("description")).sendKeys("test");
        driver.findElement(By.id("submit")).click();
        assertEquals("Product added with success!", closeAlertAndGetItsText());
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
        } finally {
            acceptNextAlert = true;
        }
    }
}*/

