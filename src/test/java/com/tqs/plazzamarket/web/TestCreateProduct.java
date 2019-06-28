package com.tqs.plazzamarket.web;

import java.util.concurrent.TimeUnit;

import com.tqs.plazzamarket.entities.Category;
import com.tqs.plazzamarket.entities.Producer;
import com.tqs.plazzamarket.repositories.CategoryRepository;
import com.tqs.plazzamarket.repositories.ProducerRepository;

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
    private StringBuffer verificationErrors = new StringBuffer();

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @Before
    public void setUp() throws Exception {
        Category category = new Category("Flowers");
        category = categoryRepository.saveAndFlush(category);

        Producer producer = new Producer();
        producer.setUsername("luiso");
        producer.setName("Luis Oliveira");
        producer.setEmail("luis@ua.pt");
        producer.setPassword("12345678");
        producer.setAddress("Aveiro");
        producer.setZipCode("3060-500");
        producer.setWebsite("https://www.example.com");
        producer = producerRepository.saveAndFlush(producer);

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");

        try {
            newChromeSession(chromeOptions);
        } catch (SessionNotCreatedException e) {
            WebDriverManager.chromedriver().setup();
            newChromeSession(chromeOptions);
        }
    }

    @Test
    public void testCreateProduct() throws Exception {
        String url = String.format("http://localhost:%d", port);
        WebDriverWait wait = new WebDriverWait(driver, 300);
        driver.get(url);
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("luiso");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("12345678");
        driver.findElement(By.id("submit")).click();

        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Create Product")));
        element.click();

        element = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));        
        element.clear();
        element.sendKeys("Batata");
        new Select(driver.findElement(By.id("category"))).selectByVisibleText("Flowers");
        driver.findElement(By.id("price")).clear();
        driver.findElement(By.id("price")).sendKeys("8");
        driver.findElement(By.id("quantity")).clear();
        driver.findElement(By.id("quantity")).sendKeys("4");
        driver.findElement(By.id("description")).clear();
        driver.findElement(By.id("description")).sendKeys("test");
        driver.findElement(By.id("submit")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//table[@data-count='1']")));
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private void newChromeSession(ChromeOptions chromeOptions) throws Exception {
        driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }
}