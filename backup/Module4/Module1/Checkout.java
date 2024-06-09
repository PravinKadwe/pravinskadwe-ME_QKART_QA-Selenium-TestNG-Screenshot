package QKART_SANITY_LOGIN.Module1;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
// import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.FluentWait;
// import org.openqa.selenium.support.ui.*;

public class Checkout {
    RemoteWebDriver driver;
    String url = "https://crio-qkart-frontend-qa.vercel.app/checkout";

    public Checkout(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public void navigateToCheckout() {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
        }
    }

    /*
     * Return Boolean denoting the status of adding a new address
     */
    public Boolean addNewAddress(String addresString) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Click on the "Add new address" button, enter the addressString in the address
             * text box and click on the "ADD" button to save the address
             */

            // SLEEP_STMT_16: Wait allow to load the page completely 
            Thread.sleep(3000);
            WebElement clickAddAddress = driver.findElement(By.xpath("//button[contains(text(),'Add new address')]"));
            clickAddAddress.click();
            // SLEEP_STMT_17: Wait to load the Textbox elements after Click on the "Add new address" button.
            Thread.sleep(1000);
            WebElement EnterAddress = driver.findElement(By.xpath("//textarea[@placeholder='Enter your complete address']"));
            EnterAddress.sendKeys(addresString);

            WebElement ClickAddButton = driver.findElement(By.xpath("//button[@type='button' and contains(text(),'Add')]"));
            ClickAddButton.click();

            return true;
        } catch (Exception e) {
            System.out.println("Exception occurred while entering address: " + e.getMessage());
            return false;

        }
    }

    /*
     * Return Boolean denoting the status of selecting an available address
     */
    public Boolean selectAddress(String addressToSelect) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Iterate through all the address boxes to find the address box with matching
             * text, addressToSelect and click on it
             */
            // SLEEP_STMT_18: Wait for Entered address in textbox transition into address selection list. 
            Thread.sleep(1000);
            WebElement AddressList = driver.findElement(By.xpath("//*[@id='root']/div/div[2]/div[1]/div/div[@class='MuiBox-root css-0'][1]")); 

            List<WebElement> CheckAddressList = AddressList.findElements(By.xpath("//div[@class='address-item not-selected MuiBox-root css-0']"));

            for (WebElement CheckAddress : CheckAddressList){
                WebElement CheckLabel = CheckAddress.findElement(By.xpath(".//div[1]/p[1]")); // Relative XPath
                String getAddress = CheckLabel.getText();

                getAddress = getAddress.replaceAll("\\s+", "");
                String addressToSelectNormalized = addressToSelect.replaceAll("\\s+", "");

                if(getAddress.equals(addressToSelectNormalized)){
                    // System.out.println(getAddress + " == " + addressToSelectNormalized);
                    WebElement InputRadioButton = CheckAddress.findElement(By.xpath(".//div[1]/span/input[@name='address' and @type='radio']"));
                    InputRadioButton.click();
                    // SLEEP_STMT_19: Wait to Select Specific Address from address selection list.
                    Thread.sleep(2000);
                    return true;
                }
            }


            System.out.println("Unable to find the given address");
            return false;
        } catch (Exception e) {
            System.out.println("Exception Occurred while selecting the given address: " + e.getMessage());
            return false;
        }

    }

    /*
     * Return Boolean denoting the status of place order action
     */
    public Boolean placeOrder() {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            // Find the "PLACE ORDER" button and click on it

            WebElement ButtonPlaceOrder = driver.findElement(By.xpath("//button[contains(text(),'PLACE ORDER')]"));
            ButtonPlaceOrder.click();

            // Wait for the Login button to disappear using Fluent Wait
            Wait<RemoteWebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(30))
                    .pollingEvery(Duration.ofMillis(250))
                    .ignoring(NoSuchElementException.class);

            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("notistack-snackbar")));        


            return true;

        } catch (Exception e) {
            System.out.println("Exception while clicking on PLACE ORDER: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting if the insufficient balance message is displayed
     */
    public Boolean verifyInsufficientBalanceMessage() {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 07: MILESTONE 6
            // Thread.sleep(1000);
            WebElement InsufficientMessage = driver.findElement(By.id("notistack-snackbar"));

            String verifyMessage = InsufficientMessage.getText();

            // System.out.println(verifyMessage);

            if(verifyMessage.contentEquals("You do not have enough balance in your wallet for this purchase")){
                return true;
            }

            return false;
        } catch (Exception e) {
            System.out.println("Exception while verifying insufficient balance message: " + e.getMessage());
            return false;
        }
    }


    public Boolean validateAdvertisements(RemoteWebDriver driver) throws InterruptedException {
        Thread.sleep(3000);
    
        // Locate all iframes on the page
        WebElement iframesContainer = driver.findElement(By.xpath("//div[contains(@class,'css-92t6i8')]"));
        List<WebElement> iframes = iframesContainer.findElements(By.xpath("//iframe[@srcdoc]"));
    
        // There should be exactly 2 iframes
        if (iframes.size() != 2) {
            System.out.println("Expected 2 iframes, but found: " + iframes.size());
            return false;
        }
    
        boolean productAdsValidated = false;
        boolean coronaStatsValidated = false;
    
        for (int i = 0; i < iframes.size(); i++) {
            // System.out.println("**Count** " + (i + 1));
    
            // Switch to the current iframe
            WebElement iframe = iframes.get(i);
            driver.switchTo().frame(iframe);
    
            try {
                WebElement buttonsSet = driver.findElement(By.xpath("//div[@class='action_buttons']"));
                WebElement clickBuyButton = buttonsSet.findElement(By.xpath(".//button[contains(text(),'Buy Now')]"));
    
                if (clickBuyButton.isDisplayed()) {
                    clickBuyButton.click();
                    Thread.sleep(2000);
    
                    // Switch back to the main content
                    driver.switchTo().parentFrame();
                    Thread.sleep(2000);

                    driver.navigate().back();
    
                    // Re-locate the iframes after switching back to the main content
                    iframesContainer = driver.findElement(By.xpath("//div[contains(@class,'css-92t6i8')]"));
                    iframes = iframesContainer.findElements(By.xpath("//iframe[@srcdoc]"));
                } else {
                    System.out.println("Button is not clickable: " + clickBuyButton.getText());
                    driver.switchTo().defaultContent();
                    return false;
                }
    
                productAdsValidated = true;
            } catch (Exception e) {
                System.out.println("Exception during iframe validation: " + e.getMessage());
                driver.switchTo().defaultContent();
                return false;
            }
    
            driver.switchTo().parentFrame();
        }
    
        // Check if it's the Corona stats advertisement by verifying its content
        try {
            WebElement coronaStats = driver.findElement(By.xpath("//iframe[contains(@src,'covid-19')]"));
            if (coronaStats != null) {
                coronaStatsValidated = true;
            }
        } catch (Exception e) {
            System.out.println("Exception while validating corona stats: " + e.getMessage());
        }
    
        // Validate that both product advertisements and corona stats were found
        if (productAdsValidated && coronaStatsValidated) {
            return true;
        } else {
            System.out.println("Advertisements validation failed. Product Ads: " + productAdsValidated + ", Corona Stats: " + coronaStatsValidated);
            return false;
        }
    }
    
    
    

}
