import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import javax.swing.text.DateFormatter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Driver;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;



public class MyJUnit {
    WebDriver driver;

    @Before
    public void setProperty() {
//        System.setProperty("webdriver.gecko.driver", "./src/test/resources/geckodriver.exe");
//        FirefoxOptions ops = new FirefoxOptions();
//        ops.addArguments("--headed");
//        driver = new FirefoxDriver(ops);//polymorphism - overridding holo childclass firefoxfriver, OPS - ta ekhne parameterized constructor hisae kaz krtese
//        driver.manage().window().maximize();
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

        System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver.exe");
        ChromeOptions ops = new ChromeOptions();
        ops.addArguments("--headed");
        driver = new ChromeDriver(ops);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

    }

    @Test
    public void getTitle() {
        driver.get("https://demoqa.com");
        String title = driver.getTitle();
        System.out.println(title);
        Assert.assertEquals("ToolsQA", title);
    }

    @Test
    public void chekcIfImageExists() {
        driver.get("https://demoqa.com");
        WebElement image1 = driver.findElement(By.cssSelector("img"));
        Assert.assertTrue(String.valueOf(image1.isDisplayed()), true);
    }

    @Test
    public void writeSomthing() {
        driver.get("https://demoqa.com/text-box");
        //WebElement txtUname = driver.findElement(By.id("userName"));
        //txtUname.sendKeys("Hasnat Shoheb");

        List<WebElement> elements = driver.findElements(By.tagName("input"));

        elements.get(0).sendKeys("Hasnat");
        elements.get(1).sendKeys("hasnat@gmail.com");

        driver.findElement(By.id("submit")).click();


    }

    @Test
    public void clickButton() {
        driver.get("https://demoqa.com/buttons");
        List<WebElement> element = driver.findElements(By.tagName("button"));
        element.get(3).click();

        Actions actions = new Actions(driver);
        actions.doubleClick(element.get(1)).perform();

        //Use contetClick for right click
        actions.contextClick(element.get(2)).perform();

        String doubleClick = driver.findElement(By.id("doubleClickMessage")).getText();
        String rightClick = driver.findElement(By.id("rightClickMessage")).getText();
        String dynamicClick = driver.findElement(By.id("dynamicClickMessage")).getText();

        Assert.assertTrue(doubleClick.contains("You have done a double click"));
        Assert.assertTrue(rightClick.contains("You have done a right click"));
        Assert.assertTrue(dynamicClick.contains("You have done a dynamic click"));

    }

    @Test
    public void alertHandle() throws InterruptedException {
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id("alertButton")).click();
        Thread.sleep(2000);
        driver.switchTo().alert().accept();

    }

    //5 Sec Delay Alert Handle //Not Work
    @Test
    public void alertHandleWithDelay() throws InterruptedException {
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id("timerAlertButton")).click();
        Thread.sleep(7000);
        driver.switchTo().alert().accept();
    }

    @Test
    public void dialogBoxHandle() {
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id("confirmButton")).click();
        //**Dialog box will tap click on OK option
        //driver.switchTo().alert().accept();

        //Dialog box will tap on cancel option
        driver.switchTo().alert().dismiss();


    }

    @Test
    public void promtHandle() {
        driver.get("https://demoqa.com/alerts");
        driver.findElement(By.id("promtButton")).click();
        driver.switchTo().alert().sendKeys("Hasnat");
        driver.switchTo().alert().accept();

    }

    @Test
    public void selectDate() {
        driver.get("https://demoqa.com/date-picker");
        WebElement date = driver.findElement(By.id("datePickerMonthYearInput"));
        date.click();
        date.clear();
        date.sendKeys("04/28/2022");
        date.sendKeys(Keys.ENTER);

    }

    @Test
    public void selectDropDown() {
        driver.get("https:demoqa.com/select-menu");
        Select select = new Select(driver.findElement(By.id("oldSelectMenu")));
        select.selectByValue("3");

    }

    @Test
    public void selectMultipleDropDown() {
        driver.get("https://demoqa.com/select-menu");
        Select select = new Select(driver.findElement(By.id("cars")));
        if (select.isMultiple()) {
            select.selectByValue("volvo");
            select.selectByValue("audi");
        }
    }

    @Test
    public void handleMultipleTab() throws InterruptedException {
        driver.get("https://demoqa.com/browser-windows");
        driver.findElement(By.id("tabButton")).click();
        Thread.sleep(5000);

        //These two lines for open a new Tab on the Browser
        ArrayList<String> w = new ArrayList(driver.getWindowHandles());
        //Switch to open Tab
        driver.switchTo().window(w.get(1));

        System.out.println("New Tab title" + driver.getTitle());
        String Text = driver.findElement(By.id("sampleHeading")).getText();
        Assert.assertEquals(Text, "This is a sample page");
        driver.close();
        driver.switchTo().window(w.get(0));
    }

    @Test
    public void handleWindow() {
        driver.get("https://demoqa.com/browser-windows");
        driver.findElement(By.id("windowButton")).click();
        String mainWindowHandle = driver.getWindowHandle();
        Set<String> allWindowHandles = driver.getWindowHandles();
        Iterator<String> iterator = allWindowHandles.iterator();

        while (iterator.hasNext()) {
            String childWindow = iterator.next();
            if (!mainWindowHandle.equalsIgnoreCase(childWindow)) {
                driver.switchTo().window(childWindow);
                String Text = driver.findElement(By.id("sampleHeading")).getText();
                Assert.assertTrue(Text.contains("This is a sample page"));
            }

        }

    }
    @Test
    public void modalDialog()
    {
        driver.get("https://demoqa.com/modal-dialogs");
        driver.findElement(By.id("showSmallModal")).click();
        String text = driver.findElement(By.className("modal-body")).getText();
        System.out.println(text);
        driver.findElement(By.id("closeSmallModal"));

    }
    @Test
    public void scrapData()
    {
        driver.get("https://demoqa.com/webtables");
        WebElement table = driver.findElement(By.className("rt-table"));
        List<WebElement> allRows = table.findElements(By.className("rt-tr"));
        int i=0;
        for(WebElement row : allRows)
        {
            List<WebElement> cells = row.findElements(By.id("rt-td"));
            for (WebElement cell : cells)
            {
                i++;
                System.out.println("num["+i+"]" +cell.getText());
            }
        }

    }
    @Test
    public void uploadImage()
    {
        driver.get("https://demoqa.com/upload-download");
        driver.findElement(By.id("uploadFile")).sendKeys("C:\\Users\\Hasnat\\Downloads");


    }
    @Test
    public void handleIFrame()
    {
        driver.get("https://demoqa.com/frames");
        driver.switchTo().frame("frame1");
        String text = driver.findElement(By.id("sampleHeading")).getText();
        System.out.println(text);
        driver.switchTo().defaultContent();
    }
    @Test
    public void mouseHover()
    {
        driver.get("https://green.edu.bd");
        //WebElement aboutUs =  driver.findElement(By.xpath("//header/nav[@id='primary-navbar']/div[1]/ul[1]/li[1]/a[1]"));

        List<WebElement> aboutUs = driver.findElements(By.xpath("//a[contains(text(),'About Us')]"));
        Actions actions = new Actions(driver);


        actions.moveToElement(aboutUs.get(2)).perform();

    }
    @Test
    public  void keyboardEvent()
    {
        driver.get("https://google.com");
        WebElement search  = driver.findElement(By.name("q"));
        Actions actions = new Actions(driver);
//        actions.moveToElement(search).perform();
//        actions.keyDown(Keys.SHIFT).perform();
//        actions.sendKeys("Selenium webdriver").perform();
//        actions.keyUp(Keys.SHIFT).perform();
//        actions.doubleClick(search).perform();
//        actions.contextClick(search).perform();

        //Chain in System

        actions.moveToElement(search).
                keyDown(Keys.SHIFT)
                .sendKeys("Selenium webdriver")
                .keyUp(Keys.SHIFT)
                .doubleClick(search)
                .contextClick(search).perform();



    }
    @Test
    public void takeScreenshot() throws IOException {
        driver.get("https://demoqa.com");
        File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String time = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss-aa").format(new Date());
        String fileWithPath = "./src/test/resources/screenshots/" + time + ".png";
        File DestFile = new File(fileWithPath);
        FileUtils.copyFile(screenshotFile, DestFile);


    }


    @Test
    public void readExcelFile() throws IOException {
        String filepath = ".\\src\\test\\resources";
        Utils.readFromExcel(filepath, "Hasnat.xls","Sheet1");

    }





    @After
    public void closeDriver()
    {
       driver.close();
       driver.quit();
    }
}
