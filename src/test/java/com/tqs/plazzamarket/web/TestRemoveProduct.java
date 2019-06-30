package com.tqs.plazzamarket.web;

import java.util.concurrent.TimeUnit;

import com.tqs.plazzamarket.entities.Category;
import com.tqs.plazzamarket.entities.Producer;
import com.tqs.plazzamarket.entities.Product;
import com.tqs.plazzamarket.repositories.CategoryRepository;
import com.tqs.plazzamarket.repositories.ProducerRepository;
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
public class TestRemoveProduct {
    @LocalServerPort
    private int port;
    
    private WebDriver driver;
    private StringBuffer verificationErrors = new StringBuffer();

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    Product product;

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

        Producer producer = new Producer();
        producer.setUsername("luiso");
        producer.setName("Luis Oliveira");
        producer.setEmail("luis@ua.pt");
        producer.setPassword("12345678");
        producer.setAddress("Aveiro");
        producer.setZipCode("3060-500");
        producer.setWebsite("https://www.example.com");
        producer = producerRepository.saveAndFlush(producer);

        Category bulbs = categoryRepository.saveAndFlush(new Category("Bulbs"));

        product = new Product();
        product.setName("Potato");
        product.setQuantity(4);
        product.setPrice(5);
        product.setProducer(producer);
        product.setDescription("test");
        product.setCategory(bulbs);
        product = productRepository.saveAndFlush(product);
    }

    @Test
    public void testRemoveProduct() throws Exception {
        String url = String.format("http://localhost:%d/", port);
        WebDriverWait wait = new WebDriverWait(driver, 300);
        driver.get(url);
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("luiso");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("12345678");
        driver.findElement(By.id("submit")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(Integer.toString(product.getId())))).click();
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//table[@data-count='0']")));
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

    private void newChromeSession(ChromeOptions chromeOptions) {
        driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }
}
