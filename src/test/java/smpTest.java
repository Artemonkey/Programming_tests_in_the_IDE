import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class smpTest
{
    static WebDriver driver;
    String Btn_Add_Fav = "//*[@title='Добавить в избранное']";
    String Btn_Apply = "//*[@id='gwt-debug-apply']";
    String Btn_Fav = "//*[@title='Избранное']";
    String Btn_Parameters = "//span[contains(@_code, 'parameters')]";
    String Btn_Delete = "//span[contains(@class, 'del fontIcon')]";
    String Btn_Yes = "//*[@id='gwt-debug-YES']";

    @BeforeAll
    public static void setUp()
    {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().setSize(new Dimension(1400, 1200));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    @Test
    @Order(1)
    public void addObject() throws InterruptedException {
        login();

        click(Btn_Add_Fav);
        click(Btn_Apply);
        Thread.sleep(2000);
        click(Btn_Fav);

        WebElement element = driver.findElement(By.xpath("//div[contains(text(), 'employee1 \"Ямбушев Артём\"/Карточка сотрудника')]"));
        String textElement = element.getText();
        String msg = String.format("Название объекта не совпало. Ожидалось: %s, Получили: %s", "employee1 \"Ямбушев Артём\"/Карточка сотрудника", textElement);
        Assertions.assertEquals("employee1 \"Ямбушев Артём\"/Карточка сотрудника", textElement);
    }

    @Test
    @Order(2)
    public void deleteObject()
    {
        login();

        click(Btn_Fav);
        click(Btn_Parameters);
        click(Btn_Delete);
        click(Btn_Yes);
        click(Btn_Apply);

        List<WebElement> element = driver.findElements(By.xpath("//div[contains(text(), 'employee1 \"Ямбушев Артём\"/Карточка сотрудника')]"));
        Assertions.assertFalse(element.isEmpty(), "Объект не удалился");
    }

    private void login()
    {
        driver.get("https://test-m.sd.nau.run/sd/");

        driver.findElement(By.id("username")).sendKeys("yambushev_artem");
        driver.findElement(By.id("password")).sendKeys("123");
        driver.findElement(By.id("submit-button")).click();
    }

    public static WebElement waitElement(String xpath)
    {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        return element;
    }

    public void click(String xpath)
    {
        waitElement(xpath).click();
    }

    @AfterEach
    public void clean()
    {
        waitElement("//*[contains(@_code, 'hideMenu')]");
        driver.findElement(By.xpath("//*[contains(@_code, 'hideMenu')]")).click();
        driver.findElement(By.xpath("//*[@title='Выйти']")).click();
    }

    @AfterAll
    public static void close()
    {
        driver.close();
    }
}
