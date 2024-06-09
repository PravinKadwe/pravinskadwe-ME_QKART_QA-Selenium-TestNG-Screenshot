package QKART_SANITY_LOGIN.Module1;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
            Thread.sleep(3000);
            WebElement clickAddAddress = driver.findElement(By.xpath("//button[contains(text(),'Add new address')]"));
            clickAddAddress.click();
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
}
