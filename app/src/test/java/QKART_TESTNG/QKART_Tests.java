package QKART_TESTNG;

import QKART_TESTNG.pages.Checkout;
import QKART_TESTNG.pages.Home;
import QKART_TESTNG.pages.Login;
import QKART_TESTNG.pages.Register;
import QKART_TESTNG.pages.SearchResult;

import static org.testng.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import org.testng.annotations.Test;
import org.testng.annotations.Parameters;
import org.testng.annotations.Listeners;

import java.lang.Integer;

@Listeners(QKART_TESTNG.ListenerClass.class)
public class QKART_Tests {

    static RemoteWebDriver driver;
    public static String lastGeneratedUserName;

    @BeforeSuite(alwaysRun=true)
    public static void createDriver() throws MalformedURLException {
        // Launch Browser using Zalenium
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(BrowserType.CHROME);
        driver = new RemoteWebDriver(new URL("http://localhost:8082/wd/hub"), capabilities);
        driver.manage().window().maximize();
        System.out.println("createDriver()");
    }

    public static RemoteWebDriver getDriver() {
        return driver;
    }

    public static void logStatus(String type, String message, String status) {

        System.out.println(String.format("%s |  %s  |  %s | %s", String.valueOf(java.time.LocalDateTime.now()), type,
                message, status));
    }

    public static void takeScreenshot(RemoteWebDriver driver, String screenshotType, String description) {
        try {
            File theDir = new File("app/screenshots");
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            String timestamp = String.valueOf(java.time.LocalDateTime.now());
            String fileName = String.format("screenshot_%s_%s_%s.png", timestamp, screenshotType, description);
            // File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            
            TakesScreenshot scrShot = ((TakesScreenshot) driver);
            File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
            File DestFile = new File("app/screenshots/" + fileName);
            FileUtils.copyFile(SrcFile, DestFile);
            // FileUtils.copyFile(screenshotFile, DestFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Testcase01: Verify a new user can successfully register
     */
    @Test(/*/*priority = 1,*/ description = "Verify registration happens correctly", groups = { "Sanity_test"})
    @Parameters({"TC1_username", "TC1_password"})
    public void TestCase01(String TC1_username, String TC1_password) throws InterruptedException {
        Boolean status;

        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/register"));

         status = registration.registerUser(TC1_username, TC1_password, true);
        assertTrue(status, "Failed to register new user");

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the login page and login with the previuosly registered user
        Login login = new Login(driver);
        login.navigateToLoginPage();

        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/login"));

        status = login.PerformLogin(lastGeneratedUserName, TC1_password);

        assertTrue(status, "Failed to login with registered user");

        // Visit the home page and log out the logged in user
        Home home = new Home(driver);
        status = home.PerformLogout();

    }


    /*
     * Verify that an existing user is not allowed to re-register on QKart
     */
    @Test(/*priority = 2,*/ description = "Verify re-registering an already registered user fails", groups = { "Sanity_test"})
    @Parameters({"TC2_username", "TC2_password"})
    public void TestCase02(String TC2_username, String TC2_password) throws InterruptedException {
        Boolean status;

        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/register"));

        status = registration.registerUser(TC2_username, TC2_password, true);
        assertTrue(status, "Failed to register new user");


        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the Registration page and try to register using the previously
        // registered user's credentials
        registration.navigateToRegisterPage();

        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/register"));

        status = registration.registerUser(lastGeneratedUserName, TC2_password, false);
        assertTrue(!status, "Failed to this case existing user registration succeeded");

    }

    /*
     * Verify the functionality of the search text box
     */
    @Test(/*priority = 3,*/ description = "Verify the functionality of search text box", groups = { "Sanity_test"})
    @Parameters({"TC3_productName"})
    public void TestCase03(String productName) throws InterruptedException {
        boolean status;
    
        // Visit the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();
    
        // Search for the "yonex" product
        status = homePage.searchForProduct(productName);
        assertTrue(status, "Unable to search for the given product");
    
        // Fetch the search results
        List<WebElement> searchResults = homePage.getSearchResults();
        assertTrue(searchResults.size() != 0, "There were no results for the given search string");
    
        // Verify that all results contain the searched text
        for (WebElement webElement : searchResults) {
            SearchResult resultelement = new SearchResult(webElement);
            String elementText = resultelement.getTitleofResult();
            assertTrue(elementText.toUpperCase().contains(productName),
                    "Test Results contains unexpected values: " + elementText);
        }
    
        // Search for product with invalid keyword
        status = homePage.searchForProduct("Gesundheit");
        assertFalse(!status, "Invalid keyword returned results");
    
        // Verify no search results are found
        searchResults = homePage.getSearchResults();
        if (searchResults.size() == 0) {
            assertTrue(homePage.isNoResultFound(), "No products found message is not displayed");
        } else {
            fail("Expected: no results, actual: results were available");
        }
    }
    
    /*
     * Verify the presence of size chart and check if the size chart content is as
     * expected
     */
    @Test(/*priority = 4,*/ description = "Verify the existence of size chart for certain items and validate contents of size chart", groups = { "Regression_Test"})
    @Parameters({"TC4_productName"})
    public void TestCase04(String productName) throws InterruptedException {
        boolean status;

        // Visit home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Search for product and get card content element of search results
        status = homePage.searchForProduct(productName);
        assertTrue(homePage.searchForProduct(productName), "Test Case Fail. Failure to search Product");

        List<WebElement> searchResults = homePage.getSearchResults();

        // Create expected values
        List<String> expectedTableHeaders = Arrays.asList("Size", "UK/INDIA", "EU", "HEEL TO TOE");
        List<List<String>> expectedTableBody = Arrays.asList(Arrays.asList("6", "6", "40", "9.8"),
                Arrays.asList("7", "7", "41", "10.2"), Arrays.asList("8", "8", "42", "10.6"),
                Arrays.asList("9", "9", "43", "11"), Arrays.asList("10", "10", "44", "11.5"),
                Arrays.asList("11", "11", "45", "12.2"), Arrays.asList("12", "12", "46", "12.6"));

        // Verify size chart presence and content matching for each search result
        for (WebElement webElement : searchResults) {
            SearchResult result = new SearchResult(webElement);

            // Verify if the size chart exists for the search result
            if (result.verifySizeChartExists()) {
                // Verify if size dropdown exists
                status = result.verifyExistenceofSizeDropdown(driver);
                assertTrue(status, "Failed to Validate presence of drop down");

                // Open the size chart
                if (result.openSizechart()) {
                    // Verify if the size chart contents matches the expected values
                    status = result.validateSizeChartContents(expectedTableHeaders, expectedTableBody, driver);
                    assertTrue(status, "Failure while validating contents of Size Chart");

                    // Close the size chart modal
                    result.closeSizeChart(driver);

                } else {
                    fail("Test Case Fail. Failure to open Size Chart");
                }

            } else {
                fail("Test Case Fail. Size Chart Link does not exist");
            }
        }
    }


    /*
     * Verify the complete flow of checking out and placing order for products is
     * working correctly
     */
    @Test(/*priority = 5,*/ description = "Verify that a new user can add multiple products in to the cart and Checkout", groups = { "Sanity_test"})
    @Parameters({"TC5_product1", "TC5_product2", "TC5_addressLine"})
    public void TestCase05(String TC5_product1, String TC5_product2, String TC5_addressLine) throws InterruptedException {
        Boolean status;

        // Go to the Register page
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();

        // Register a new user
        status = registration.registerUser("testUser", "abc@123", true);
        assertTrue(status, "Failed TestCase05 Happy Flow Test Failed");

        // Save the username of the newly registered user
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Go to the login page
        Login login = new Login(driver);
        login.navigateToLoginPage();

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/login"));

        // Login with the newly registered user's credentials
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        assertTrue(status, "Failed TestCase05 Test Case User Perform Login Failed");

        // Go to the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/"));

        // Find required products by searching and add them to the user's cart
        status = homePage.searchForProduct("YONEX");
        assertTrue(status, "Failed TestCase05 To Find required products 1 by searching");
        homePage.addProductToCart(TC5_product1);
        status = homePage.searchForProduct("Tan");
        assertTrue(status, "Failed TestCase05 To Find required products 2 by searching");
        homePage.addProductToCart(TC5_product2);

        // Click on the checkout button
        homePage.clickCheckout();

        // Add a new address on the Checkout page and select it
        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress(TC5_addressLine);
        checkoutPage.selectAddress(TC5_addressLine);

        // Place the order
        checkoutPage.placeOrder();

        // WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));

        // Check if placing order redirected to the Thansk page
        status = driver.getCurrentUrl().endsWith("/thanks");
        assertTrue(status, "Failed TestCase05 To redirected to the Thansk page");

        // Go to the home page
        homePage.navigateToHome();

        // Log out the user
        homePage.PerformLogout();

    }


    /*
     * Verify the quantity of items in cart can be updated
     */
    @Test(/*priority = 6,*/ description = "Verify that the contents of the cart can be edited", groups = { "Regression_Test"})
    @Parameters({"TC6_product1","TC6_product2"})
    public void TestCase06(String product1, String product2) throws InterruptedException {
        Boolean status;

        Home homePage = new Home(driver);
        Register registration = new Register(driver);
        Login login = new Login(driver);

        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        assertTrue(status, "Failed TestCase06 To User Perform Register Failed");

        lastGeneratedUserName = registration.lastGeneratedUsername;

        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        assertTrue(status, "Failed TestCase06 To User Perform Login Failed");

        homePage.navigateToHome();
        status = homePage.searchForProduct("Xtend");
        assertTrue(status, "Failed TestCase06 To Find required products 1 by searching");
        homePage.addProductToCart(product1);

        status = homePage.searchForProduct("Yarine");
        assertTrue(status, "Failed TestCase06 To Find required products 2 by searching");
        homePage.addProductToCart(product2);

        // update watch quantity to 2
        homePage.changeProductQuantityinCart(product1, 2);

        // update table lamp quantity to 0
        homePage.changeProductQuantityinCart(product2, 0);

        // update watch quantity again to 1
        homePage.changeProductQuantityinCart(product1, 1);

        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        checkoutPage.placeOrder();


        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));


        status = driver.getCurrentUrl().endsWith("/thanks");
        assertTrue(status, "Failed TestCase06 To redirected to the Thansk page");

        homePage.navigateToHome();
        homePage.PerformLogout();

    }

    @Test(/*priority = 7,*/ description = "Verify that insufficient balance error is thrown when the wallet balance is not enough", groups = { "Sanity_test"})
    @Parameters({"TC7_product", "TC7_quantity"})
    public void TestCase07(String TC7_product, int TC7_quantity) throws InterruptedException {
        Boolean status;

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        assertTrue(status, "Failed TestCase07 To User Perform Register Failed");

        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/login"));

        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        assertTrue(status, "Failed TestCase07 To User Perform Login Failed");


        Home homePage = new Home(driver);
        homePage.navigateToHome();
        status = homePage.searchForProduct("Tan");
        assertTrue(status, "Failed TestCase07 To Find required products by searching");
        homePage.addProductToCart(TC7_product);

        homePage.changeProductQuantityinCart(TC7_product, TC7_quantity);

        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        checkoutPage.placeOrder();
        Thread.sleep(3000);

        status = checkoutPage.verifyInsufficientBalanceMessage();
        assertTrue(status, "Failed TestCase07 To Verify that insufficient balance error is thrown when the wallet balance is not enough");

    }   
    
    @Test(/*priority = 8,*/ description = "Verify that a product added to a cart is available when a new tab is added", groups = { "Regression_Test"})
    public void TestCase08() throws InterruptedException {
        Boolean status = false;


        Register registration = new Register(driver);
        registration.navigateToRegisterPage();

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/register"));

        status = registration.registerUser("testUser", "abc@123", true);
        assertTrue(status, "Failed TestCase08 To User Perform Register Failed");

        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();

        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/login"));

        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        assertTrue(status, "Failed TestCase08 To User Perform Login Failed");


        Home homePage = new Home(driver);
        homePage.navigateToHome();

        status = homePage.searchForProduct("YONEX");
        assertTrue(status, "Failed TestCase08 To Find required products by searching");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");

        String currentURL = driver.getCurrentUrl();

        driver.findElement(By.linkText("Privacy policy")).click();
        Set<String> handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);

        driver.get(currentURL);
        Thread.sleep(2000);

        List<String> expectedResult = Arrays.asList("YONEX Smash Badminton Racquet");
        status = homePage.verifyCartContents(expectedResult);

        driver.close();

        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);

    }

    @Test(/*priority = 9,*/ description = "Verify that privacy policy and about us links are working fine", groups = { "Regression_Test"})
    public void TestCase09() throws InterruptedException {
        Boolean status = false;

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/register"));

        status = registration.registerUser("testUser", "abc@123", true);
        assertTrue(status, "Failed TestCase09 To User Perform Register Failed");

        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();

        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/login"));

        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        assertTrue(status, "Failed TestCase09 To User Perform Login Failed");


        Home homePage = new Home(driver);
        homePage.navigateToHome();

        String basePageURL = driver.getCurrentUrl();

        driver.findElement(By.linkText("Privacy policy")).click();
        status = driver.getCurrentUrl().equals(basePageURL);
        assertTrue(status, "Failed TestCase09 To Verifying parent page url didn't change on privacy policy link click failed");


        Set<String> handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);
        WebElement PrivacyPolicyHeading = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/h2"));
        status = PrivacyPolicyHeading.getText().equals("Privacy Policy");
        assertTrue(status, "Failed TestCase09 To Verifying new tab opened has Privacy Policy page heading failed");

        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);
        driver.findElement(By.linkText("Terms of Service")).click();

        handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[2]);
        WebElement TOSHeading = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/h2"));
        status = TOSHeading.getText().equals("Terms of Service");
        assertTrue(status, "Failed TestCase09 To Verifying new tab opened has Terms Of Service page heading failed");

        driver.close();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]).close();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);

    }    

    @Test(/*priority = 10,*/ description = "Verify that the contact us dialog works fine", groups = { "Regression_Test"})
    @Parameters({"TC10_name", "TC10_email", "TC10_message"})
    public void TestCase10(String TC10_name, String TC10_email, String TC10_message) throws InterruptedException {

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        driver.findElement(By.xpath("//*[text()='Contact us']")).click();

        WebElement inputName = driver.findElement(By.xpath("//input[@placeholder='Name']"));
        inputName.sendKeys(TC10_name);
        WebElement inputEmail = driver.findElement(By.xpath("//input[@placeholder='Email']"));
        inputEmail.sendKeys(TC10_email);
        WebElement inputMessage = driver.findElement(By.xpath("//input[@placeholder='Message']"));
        inputMessage.sendKeys(TC10_message);

        WebElement contactUs = driver.findElement(
                By.xpath("/html/body/div[2]/div[3]/div/section/div/div/div/form/div/div/div[4]/div/button"));

        contactUs.click();

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.invisibilityOf(contactUs));

    }

    @Test(/*priority = 11,*/ description = "Ensure that the Advertisement Links on the QKART page are clickable", groups = { "Sanity_test"})
    @Parameters({"TC11_product", "TC11_addressLine"})
    public void TestCase11(String TC11_product, String TC11_addressLine) throws InterruptedException {
        Boolean status = false;

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        assertTrue(status, "Failed TestCase011 To User Perform Register Failed");

        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/login"));

        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        assertTrue(status, "Failed TestCase011 To User Perform Login Failed");

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        status = homePage.searchForProduct(TC11_product);
        assertTrue(status, "Failed TestCase011 To Find required products by searching");
        homePage.addProductToCart(TC11_product);
        // homePage.changeProductQuantityinCart("YONEX Smash Badminton Racquet", 1);
        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress(TC11_addressLine);
        checkoutPage.selectAddress(TC11_addressLine);
        checkoutPage.placeOrder();
        Thread.sleep(3000);

        String currentURL = driver.getCurrentUrl();

        List<WebElement> Advertisements = driver.findElements(By.xpath("//iframe"));

        status = Advertisements.size() == 3;
        assertTrue(status, "Failed TestCase011 To Verify that 3 Advertisements are available");

        WebElement Advertisement1 = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/iframe[1]"));
        driver.switchTo().frame(Advertisement1);
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
        driver.switchTo().parentFrame();

        status = !driver.getCurrentUrl().equals(currentURL);
        assertTrue(status, "Failed TestCase011 To Verify that Advertisement 1 is clickable ");

        driver.get(currentURL);
        Thread.sleep(3000);

        WebElement Advertisement2 = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/iframe[2]"));
        driver.switchTo().frame(Advertisement2);
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
        driver.switchTo().parentFrame();

        status = !driver.getCurrentUrl().equals(currentURL);
        assertTrue(status, "Failed TestCase011 To Verify that Advertisement 2 is clickable ");

    }  


    @AfterSuite
    public static void quitDriver() {
        System.out.println("quit()");
        driver.quit();
    }

}

