package com.tqs.plazzamarket.web;

import java.util.regex.Pattern;

import com.tqs.plazzamarket.entities.Category;
import com.tqs.plazzamarket.entities.Consumer;
import com.tqs.plazzamarket.entities.Producer;
import com.tqs.plazzamarket.entities.Product;
import com.tqs.plazzamarket.repositories.CategoryRepository;
import com.tqs.plazzamarket.repositories.ConsumerRepository;
import com.tqs.plazzamarket.repositories.ProducerRepository;
import com.tqs.plazzamarket.repositories.ProductRepository;

import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
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
public class TestSearch {
    @LocalServerPort
    private int port;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private ProductRepository productRepository;

    private WebDriver driver;
    private StringBuffer verificationErrors = new StringBuffer();

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
        Category flowers = categoryRepository.saveAndFlush(new Category("Flowers"));

        Product potato = new Product();
        potato.setName("Potato");
        potato.setQuantity(4);
        potato.setPrice(5);
        potato.setProducer(producer);
        potato.setDescription("test");
        potato.setCategory(bulbs);
        potato = productRepository.saveAndFlush(potato);

        potato = new Product();
        potato.setName("Tomato");
        potato.setQuantity(4);
        potato.setPrice(5);
        potato.setProducer(producer);
        potato.setDescription("test");
        potato.setCategory(flowers);
        potato = productRepository.saveAndFlush(potato);

    }

    @Test
    public void testSearch() throws Exception {
        String url = String.format("http://localhost:%d/", port);
        driver.get(url);
        WebDriverWait wait = new WebDriverWait(driver, 300);

        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("luispaisalves");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("12345678");
        driver.findElement(By.id("submit")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("logout")));
        driver.get(url + "listproduct/Bulbs");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-category='Bulbs']")));

        driver.get(url + "listproduct/Flowers");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[@data-category='Flowers']")));

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
