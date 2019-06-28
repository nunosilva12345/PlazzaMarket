package com.tqs.plazzamarket.web;

import com.tqs.plazzamarket.entities.Category;
import com.tqs.plazzamarket.entities.Consumer;
import com.tqs.plazzamarket.entities.Producer;
import com.tqs.plazzamarket.entities.Product;
import com.tqs.plazzamarket.entities.Sale;
import com.tqs.plazzamarket.repositories.CategoryRepository;
import com.tqs.plazzamarket.repositories.ConsumerRepository;
import com.tqs.plazzamarket.repositories.ProducerRepository;
import com.tqs.plazzamarket.repositories.ProductRepository;
import com.tqs.plazzamarket.repositories.SaleRepository;
import com.tqs.plazzamarket.utils.Status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class TestConfirmSaleProducer {
    @LocalServerPort
    private int port;

    private WebDriver driver;
    private StringBuffer verificationErrors = new StringBuffer();

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SaleRepository saleRepository;

    Product product;
    Sale sale;

    @Before
    public void setUp() throws Exception {

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage",
                "--window-size=1920,1080");

        try {
            newChromeSession(chromeOptions);
        } catch (SessionNotCreatedException e) {
            WebDriverManager.chromedriver().setup();
            newChromeSession(chromeOptions);
        }

        Category category = new Category();
        category.setName("Flower");
        category = categoryRepository.saveAndFlush(category);

        Consumer consumer = new Consumer();
        consumer.setUsername("luispaisalves");
        consumer.setName("Luis Oliveira");
        consumer.setEmail("luis@ua.pt");
        consumer.setPassword("12345678");
        consumer.setAddress("Aveiro");
        consumer.setZipCode("3060-500");
        consumer = consumerRepository.saveAndFlush(consumer);

        Producer producer = new Producer();
        producer.setUsername("luiso");
        producer.setName("Luis Oliveira");
        producer.setEmail("luis@ua.pt");
        producer.setPassword("12345678");
        producer.setAddress("Aveiro");
        producer.setZipCode("3060-500");
        producer.setWebsite("https://www.example.com");
        producer = producerRepository.saveAndFlush(producer);

        product = new Product();
        product.setName("Potato");
        product.setQuantity(4);
        product.setPrice(5);
        product.setProducer(producer);
        product.setDescription("test");
        product.setCategory(category);
        product = productRepository.saveAndFlush(product);

        sale = new Sale();
        sale.setConsumer(consumer);
        sale.setProduct(product);
        sale.setQuantity(2);
        sale.setStatus(Status.PROCESSING);
        sale = saleRepository.saveAndFlush(sale);
    }

    @Test
    public void testAccept() throws Exception {
        driver.get(String.format("http://localhost:%d", port));
        WebDriverWait wait = new WebDriverWait(driver, 300);
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("luiso");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("12345678");
        driver.findElement(By.id("submit")).click();
        driver.findElement(By.linkText("Pending Reservations")).click();
        driver.findElement(By.xpath(String.format(".//*[@class='fas fa-check accept' and @id='%d']", sale.getId()))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//table[@data-count='0']")));
        driver.findElement(By.linkText("History Sales")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//table[@data-count='1']")));
        assertEquals(sale.getProduct().getName(), wait.until(ExpectedConditions.presenceOfElementLocated(
                (By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Consumer'])[1]/following::td[1]"))))
                .getText());
        driver.findElement(By.id("logout")).click();
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        element.clear();
        element.sendKeys("luispaisalves");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("12345678");
        driver.findElement(By.id("submit")).click();
        driver.findElement(By.linkText("Shopping History")).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//table[@data-count='1']")));
        assertEquals(sale.getProduct().getName(), wait.until(ExpectedConditions.presenceOfElementLocated(
                (By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Consumer'])[1]/following::td[1]"))))
                .getText());
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
