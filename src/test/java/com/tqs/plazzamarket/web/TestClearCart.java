package com.tqs.plazzamarket.web;

import java.util.concurrent.TimeUnit;

import com.tqs.plazzamarket.entities.Category;
import com.tqs.plazzamarket.entities.Consumer;
import com.tqs.plazzamarket.entities.Product;
import com.tqs.plazzamarket.repositories.CategoryRepository;
import com.tqs.plazzamarket.repositories.ConsumerRepository;
import com.tqs.plazzamarket.repositories.ProductRepository;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
public class TestClearCart {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private StringBuffer verificationErrors = new StringBuffer();

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private ProductRepository productRepository;

    Product p;

    @Before
    public void setUp() throws Exception {

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");

        try {
            newChromeSession(chromeOptions);
        } catch (SessionNotCreatedException e) {
            WebDriverManager.chromedriver().setup();
            newChromeSession(chromeOptions);
        }

        Consumer consumer = new Consumer();
        consumer.setUsername("luispaisalves");
        consumer.setName("Luis Oliveira");
        consumer.setEmail("luis@ua.pt");
        consumer.setPassword("12345678");
        consumer.setAddress("Aveiro");
        consumer.setZipCode("3060-500");
        consumerRepository.saveAndFlush(consumer);

        Category category = new Category("Flowers");
        category = categoryRepository.saveAndFlush(category);

        p = new Product();
        p.setName("Potato");
        p.setQuantity(4);
        p.setPrice(5);
        p.setDescription("test");
        p.setCategory(category);
        productRepository.saveAndFlush(p);
    }

    @Test
    public void testCleanCart() throws Exception {
        String url = String.format("http://localhost:%d/", port);
        WebDriverWait wait = new WebDriverWait(driver, 300);
        driver.get(url);
        driver.findElement(By.id("username")).click();
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("luispaisalves");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("12345678");
        driver.findElement(By.id("submit")).click();
        driver.findElement(
                By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='View'])[1]/following::button[1]"))
                .click();
        driver.findElement(By.id("quantity")).clear();
        driver.findElement(By.id("quantity")).sendKeys("1");
        driver.findElement(By.id("quantity")).click();
        driver.findElement(By.id("quantity")).clear();
        driver.findElement(By.id("quantity")).sendKeys("2");
        driver.findElement(By.id("quantity")).click();
        // ERROR: Caught exception [ERROR: Unsupported command [doubleClick |
        // id=quantity | ]]
        driver.findElement(By.id("submit")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                "(.//*[normalize-space(text()) and normalize-space(.)='Your Cart' and @data-count='1'])[1]/following::a[1]")))
                .click();
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Your Cart' and @data-count='0'])[1]")));
        assertEquals("0", element.getAttribute("data-count"));
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