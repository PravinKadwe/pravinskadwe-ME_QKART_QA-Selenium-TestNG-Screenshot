package QKART_SANITY_LOGIN.Module1;

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

            // Wait for Logout to Complete
            Thread.sleep(3000);

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
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Clear the contents of the search box and Enter the product name in the search
            // box
            
            WebElement SearchBoxInput = driver.findElement(By.xpath("//input[@name='search'][1]"));
            SearchBoxInput.clear();
            SearchBoxInput.sendKeys(product);
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
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Find all webelements corresponding to the card content section of each of
            // search results
            // Thread.sleep(3000);
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
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Check the presence of "No products found" text in the web page. Assign status
            // = true if the element is *displayed* else set status = false
            // Thread.sleep(2000);
            WebElement noProductsFoundElement = driver.findElement(By.xpath("//h4[contains(text(),'No products found')]"));
        
            // Check if the element is displayed
            if (noProductsFoundElement.isDisplayed()) {
                status = true;
            }


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
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Iterate through each product on the page to find the WebElement corresponding
             * to the matching productName
             * 
             * Click on the "ADD TO CART" button for that element
             * 
             * Return true if these operations succeeds
             */
            Thread.sleep(2000);
            WebElement ProductCards = driver.findElement(By.xpath("//*[@id='root']/div/div/div[3]/div"));
            
            List<WebElement> FindProduct = ProductCards.findElements(By.xpath("//*[@id='root']/div/div/div[3]/div/div[2]"));

            for (WebElement CheckCard : FindProduct){
                WebElement getProductName = CheckCard.findElement(By.xpath("//p[contains(text(),'"+productName+"')]"));

                if(getProductName.isDisplayed()){
                    WebElement AddCartProduct = driver.findElement(By.xpath("//button[(contains(text(),'Add to cart'))]"));
                    AddCartProduct.click();
                    return true;
                }

            }

            System.out.println("Unable to find the given product");
            return true;
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
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            // Find and click on the the Checkout button
            Thread.sleep(2000);
            WebElement ClickCheckOut = driver.findElement(By.xpath("//button[contains(text(),'Checkout')]"));

            ClickCheckOut.click();

            return status = true;
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
            //--//div[@class='App']/div/div[3]/div[2]/div[1]/div[contains(@class, 'css-0')]
            // Allow the page to load and the elements to be available
            Thread.sleep(2000);

            // Locate the product list in the cart
            WebElement SelectedProductList = driver.findElement(By.xpath("//div[@class='App']/div/div[3]/div[2]"));

            // Retrieve all products in the cart
            List<WebElement> GetProductList = SelectedProductList.findElements(By.xpath(".//div/div[@class='MuiBox-root css-0']"));
            // System.out.println(" GetProductList = " + GetProductList.size());

            // Iterate through each product to find the matching product name
            for (int i = 1; i <= GetProductList.size(); i++) {
                // Locate the product name element and get its text
                WebElement SingleProductCheck = SelectedProductList.findElement(By.xpath(".//div/div[@class='MuiBox-root css-0'][" + i + "]/div/div[2]/div[1]"));
                String stringProductName = SingleProductCheck.getText();
                // System.out.println("SingleProductCheck  =  //div[@class='App']/div/div[3]/div[2]/div/div/div[@class='MuiBox-root css-0'][" + i + "]/div/div[2]/div[contains(text(),'" + productName + "')]");
                
                // Check if the product name matches
                if (stringProductName.equals(productName)) {
                    // Locate the current quantity element
                    WebElement getQuantity = SelectedProductList.findElement(By.xpath(".//div/div[@class='MuiBox-root css-0'][" + i + "]/div/div[2]/div[2]/div/div"));
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
                        // Wait for the DOM to update
                        Thread.sleep(5000);
                    }

                    // If the desired quantity is 0, remove the item completely
                    if (quantity == 0) {
                        WebElement RemoveButton = SelectedProductList.findElement(By.xpath(".//div/div[@class='MuiBox-root css-0']div/div[@class='MuiBox-root css-0'][" + i + "]/div/div[2]/div[2]/div/button[1]"));
                        while (intQuantity > 0) {
                            RemoveButton.click();
                            intQuantity--;
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
    public Boolean verifyCartContents(List<String> expectedCartContents) {
        try {
            WebElement cartParent = driver.findElement(By.className("cart"));
            List<WebElement> cartContents = cartParent.findElements(By.className("css-zgtx0t"));

            ArrayList<String> actualCartContents = new ArrayList<String>() {
            };
            for (WebElement cartItem : cartContents) {
                actualCartContents.add(cartItem.findElement(By.className("css-1gjj37g")).getText().split("\n")[0]);
            }

            for (String expected : expectedCartContents) {
                if (!actualCartContents.contains(expected)) {
                    return false;
                }
            }

            return true;

        } catch (Exception e) {
            System.out.println("Exception while verifying cart contents: " + e.getMessage());
            return false;
        }
    }
}
