package QKART_SANITY_LOGIN.Module1;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;


public class SearchResult {
    WebElement parentElement;

    public SearchResult(WebElement SearchResultElement) {
        this.parentElement = SearchResultElement;
    }

    /*
     * Return title of the parentElement denoting the card content section of a
     * search result
     */
    public String getTitleofResult() {
        String titleOfSearchResult = "";
        // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
        // Find the element containing the title (product name) of the search result and
        // assign the extract title text to titleOfSearchResult

        titleOfSearchResult = parentElement.getText();

        return titleOfSearchResult;
    }

    /*
     * Return Boolean denoting if the open size chart operation was successful
     */
    public Boolean openSizechart(WebDriver driver) {
        
        try {

            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 04: MILESTONE 2
            // Find the link of size chart in the parentElement and click on it
            // String htmlContent = parentElement.getAttribute("innerHTML");
            // System.out.println(htmlContent);
            WebElement ClickSizechart = driver.findElement(By.xpath("//*[@id='root']/div/div/div[3]/div/div[2]/div[1]/div/div[1]/button"));
                        
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ClickSizechart);

            Actions action = new Actions(driver);


            action.moveToElement(ClickSizechart).click();
            action.perform();
            // Thread.sleep(5000);
            // ClickSizechart.click();

            return true;
        } catch (Exception e) {
            System.out.println("Exception while opening Size chart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting if the close size chart operation was successful
     */
    public Boolean closeSizeChart(WebDriver driver) {
        try {
            Thread.sleep(2000);
            Actions action = new Actions(driver);

            // Clicking on "ESC" key closes the size chart modal
            action.sendKeys(Keys.ESCAPE);
            action.perform();
            Thread.sleep(2000);
            return true;
        } catch (Exception e) {
            System.out.println("Exception while closing the size chart: " + e.getMessage());
            return false;
        }
    }
    
    /*
     * Return Boolean based on if the size chart exists
     */
    public Boolean verifySizeChartExists() {
        Boolean status = false;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 04: MILESTONE 2
            /*
             * Check if the size chart element exists. If it exists, check if the text of
             * the element is "SIZE CHART". If the text "SIZE CHART" matches for the
             * element, set status = true , else set to false
             */
            
            String elementText = parentElement.getText();
            // System.out.println(resultElement);
            if (elementText.toUpperCase().contains("SIZE CHART")) {
                
                status = true;
            }
            
            return status;
        } catch (Exception e) {
            return status;
        }
    }

    /*
     * Return Boolean if the table headers and body of the size chart matches the
     * expected values
     */
    public Boolean validateSizeChartContents(List<String> expectedTableHeaders, List<List<String>> expectedTableBody,
            WebDriver driver) {
        Boolean status = true;

        
        try {
            WebElement Table = driver.findElement(By.xpath("//table"));
            
            List<WebElement> Tableheader = Table.findElements(By.xpath("thead/tr/th"));
    
            List<String> actualTableHeaders = new ArrayList<>();
            for (WebElement header : Tableheader) {
                actualTableHeaders.add(header.getText());
                // actualTableHeaders.add(header.getAttribute("innerHTML"));
                // System.out.println(actualTableHeaders); 
            }
    
            if (!actualTableHeaders.equals(expectedTableHeaders)) {
                status = false;
                // System.out.println(actualTableHeaders+" != "+expectedTableHeaders);
            }
    
    
            List<List<String>> actualRowData = new ArrayList<>();
    
            List<WebElement> tableRows = driver.findElements(By.xpath("//tbody/tr"));
            
            for (WebElement row : tableRows) {
                List<String> rowData = new ArrayList<>();
                List<WebElement> tableCells = row.findElements(By.xpath("td"));
                for (WebElement cell : tableCells) {
                    rowData.add(cell.getText());
                }
                actualRowData.add(rowData);
            }
            
            // System.out.println(actualRowData);
            
            if (!actualRowData.equals(expectedTableBody)) {
                status = false;
                // System.out.println(actualRowData+" != "+expectedTableBody);
            }
           
            return status;

        } catch (Exception e) {
            System.out.println("Error while validating chart contents");
            return false;
        }
    }

    /*
     * Return Boolean based on if the Size drop down exists
     */
    public Boolean verifyExistenceofSizeDropdown(WebDriver driver) {
        Boolean status = false;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 04: MILESTONE 2
            // If the size dropdown exists and is displayed return true, else return false

            // Locate the select element inside the parentElement
            WebElement sizeDropdown = parentElement.findElement(By.id("uncontrolled-native"));
            
            // Check if the size dropdown is displayed
            status = sizeDropdown.isDisplayed();

            return status;
        } catch (Exception e) {
            return status;
        }
    }
}