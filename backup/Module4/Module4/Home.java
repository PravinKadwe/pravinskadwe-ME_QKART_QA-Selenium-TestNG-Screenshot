package QKART_SANITY_LOGIN.Module4;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Home {
    RemoteWebDriver driver;
    String url = "https://crio-qkart-frontend-qa.vercel.app";

    public Home(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public void navigateToHome() {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
        }
    }

    public Boolean PerformLogout() throws InterruptedException {
        try {
            // Find and click on the Logout Button
            WebElement logout_button = driver.findElement(By.className("MuiButton-text"));
            logout_button.click();

            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.invisibilityOfElementWithText(By.className("css-1urhf6j"), "Logout"));

            return true;
        } catch (Exception e) {
            // Error while logout
            return false;
        }
    }

    /*
     * Returns Boolean if searching for the given product name occurs without any
     * errors
     */
    public Boolean searchForProduct(String product) {
        try {
            // Clear the contents of the search box and Enter the product name in the search
            // box
            WebElement searchBox = driver.findElement(By.xpath("//input[@name='search'][1]"));
            searchBox.clear();
            searchBox.sendKeys(product);
            // TODO: CRIO_TASK_MODULE_XPATH - M0 Fix broken Xpath
            // WebDriverWait wait = new WebDriverWait(driver,30);
            // wait.until(ExpectedConditions.or(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//div[@class='MuiGrid-root MuiGrid-item MuiGrid-grid-xs-6 MuiGrid-grid-md-3 css-sycj1h']"), product),
            // ExpectedConditions.presenceOfElementLocated(By.xpath("//h4[contains(text(),'No products found')]"))));
            Thread.sleep(3000);
            return true;
        } catch (Exception e) {
            System.out.println("Error while searching for a product: " + e.getMessage());
            return false;
        }
    }

    /*
     * Returns Array of Web Elements that are search results and return the same
     */
    public List<WebElement> getSearchResults() {
        List<WebElement> searchResults = new ArrayList<WebElement>() {
        };
        try {
            // Find all webelements corresponding to the card content section of each of
            // search results
            searchResults = driver.findElements(By.xpath("//div[@class='MuiGrid-root MuiGrid-item MuiGrid-grid-xs-6 MuiGrid-grid-md-3 css-sycj1h']"));
            return searchResults;
        } catch (Exception e) {
            System.out.println("There were no search results: " + e.getMessage());
            return searchResults;

        }
    }

    /*
     * Returns Boolean based on if the "No products found" text is displayed
     */
    public Boolean isNoResultFound() {
        Boolean status = false;
        try {
            // Check the presence of "No products found" text in the web page. Assign status
            // = true if the element is *displayed* else set status = false
            status = driver.findElementByXPath("//h4[contains(text(),'No products found')]").isDisplayed();
            return status;
        } catch (Exception e) {
            return status;
        }
    }

    /*
     * Return Boolean if add product to cart is successful
     */
    public Boolean addProductToCart(String productName) {
        try {
            /*
             * Iterate through each product on the page to find the WebElement corresponding
             * to the matching productName
             * 
             * Click on the "ADD TO CART" button for that element
             * 
             * Return true if these operations succeeds
             */
            List<WebElement> gridContent = driver.findElements(By.xpath("//*[@id='root']/div/div/div[3]/div/div[2]"));
            for (WebElement cell : gridContent) {
                if (productName.contains(cell.findElement(By.xpath("//p[contains(text(),'"+productName+"')]")).getText())) {
                    cell.findElement(By.tagName("button")).click();

                    WebDriverWait wait = new WebDriverWait(driver, 30);
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                            String.format("//*[@class='MuiBox-root css-1gjj37g']/div[1][text()='%s']", productName))));
                    return true;
                }
            }
            // SLEEP_STMT_12: If product found, wait till the product gets added
            // successfully
            System.out.println("Unable to find the given product: " + productName);
            return false;
        } catch (Exception e) {
            System.out.println("Exception while performing add to cart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting the status of clicking on the checkout button
     */
    public Boolean clickCheckout() {
        Boolean status = false;
        try {
            // Find and click on the the Checkout button
            WebElement checkoutBtn = driver.findElement(By.className("checkout-btn"));
            checkoutBtn.click();

            status = true;
            return status;
        } catch (Exception e) {
            System.out.println("Exception while clicking on Checkout: " + e.getMessage());
            return status;
        }
    }

    /*
     * Return Boolean denoting the status of change quantity of product in cart
     * operation
     */
    public Boolean changeProductQuantityinCart(String productName, int quantity) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 06: MILESTONE 5

            // Find the item on the cart with the matching productName

            // Increment or decrement the quantity of the matching product until the current
            // quantity is reached (Note: Keep a look out when then input quantity is 0,
            // here we need to remove the item completely from the cart)

            //SLEEP_STMT_07 : Allow cart elements to be available
            Thread.sleep(2000);

            // Locate the product list in the cart
            WebElement SelectedProductList = driver.findElement(By.xpath("//div[@class='App']/div/div[3]/div[2]"));

            // Retrieve all products in the cart
            List<WebElement> GetProductList = SelectedProductList.findElements(By.xpath(".//div/div[@class='MuiBox-root css-0']"));

            // Iterate through each product to find the matching product name
            for (int i = 1; i <= GetProductList.size(); i++) {
                // Locate the product name element and get its text
                WebElement SingleProductCheck = SelectedProductList.findElement(By.xpath(".//div/div[@class='MuiBox-root css-0'][" + i + "]/div/div[2]/div[1]"));
                String stringProductName = SingleProductCheck.getText().replace(" ", "");

                // Check if the product name matches
                if (stringProductName.contains(productName.replace(" ", ""))) {

                    // Locate the current quantity element
                    WebElement getQuantity = SelectedProductList.findElement(By.xpath(".//div/div[@class='MuiBox-root css-0'][" + i + "]/div/div[2]/div[2]/div/div[@data-testid=\"item-qty\"]"));
                    String checkQuantity = getQuantity.getText();
                    int intQuantity = Integer.parseInt(checkQuantity);
                    // Adjust the quantity to the desired value
                    while (quantity != intQuantity) {
                        if (quantity > intQuantity) {
                            // Increment the quantity
                            WebElement PlusButton = SelectedProductList.findElement(By.xpath(".//div/div[@class='MuiBox-root css-0'][" + i + "]/div/div[2]/div[2]/div/button[2]"));
                            PlusButton.click();
                            intQuantity++;
                        } else {
                            // Decrement the quantity
                            WebElement MinusButton = SelectedProductList.findElement(By.xpath(".//div/div[@class='MuiBox-root css-0'][" + i + "]/div/div[2]/div[2]/div/button[1]"));
                            MinusButton.click();
                            intQuantity--;
                        }
                        //SLEEP_STMT_08 : Wait for the Product quantity Increment or decrement to update.
                        Thread.sleep(5000);
                    }

                    // If the desired quantity is 0, remove the item completely
                    if (quantity == 0) {
                        WebElement RemoveButton = SelectedProductList.findElement(By.xpath(".//div/div[@class='MuiBox-root css-0']div/div[@class='MuiBox-root css-0'][" + i + "]/div/div[2]/div[2]/div/button[1]"));
                        while (intQuantity > 0) {
                            RemoveButton.click();
                            intQuantity--;
                            //SLEEP_STMT_09 : Wait for the DOM to update, remove the item completely
                            Thread.sleep(2000);
                        }
                    }

                    // Successfully updated the quantity
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            if (quantity == 0)
                return true;
            System.out.println("exception occurred when updating cart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting if the cart contains items as expected
     */
    // TODO: CRIO_TASK_MODULE_XPATH - M1_1 Update locators to use Xpath
    public Boolean verifyCartContents(List<String> expectedCartContents) {
        try {
            // Get all the cart items as an array of webelements

            // Iterate through expectedCartContents and check if item with matching product
            // name is present in the cart

            WebElement cartParent = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div[3]/div[2]/div[contains(@class,'cart')]"));
            List<WebElement> cartContents = cartParent.findElements(By.xpath(".//div[@class='MuiBox-root css-0']"));

            ArrayList<String> actualCartContents = new ArrayList<String>() {
            };
            for (WebElement cartItem : cartContents) {
                actualCartContents.add(cartItem.findElement(By.xpath("//div/div[@class='MuiBox-root css-1gjj37g']")).getText().split("\n")[0]);
            }

            // System.out.println("actualCartContents ::"+actualCartContents);


            for (String expected : expectedCartContents) {
                // To trim as getText() trims cart item title
                // System.out.println("expected ::"+expected);
                for (String cartProductName : actualCartContents){
                    // System.out.println("cartProductName ::"+cartProductName);
                    if (!cartProductName.contains(expected.trim())) {
                        return false;
                    }
                }
            }

            return true;

        } catch (Exception e) {
            System.out.println("Exception while verifying cart contents: " + e.getMessage());
            return false;
        }
    }
}
