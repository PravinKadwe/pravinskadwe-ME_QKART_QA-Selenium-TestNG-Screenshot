package QKART_TESTNG;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.openqa.selenium.remote.RemoteWebDriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ListenerClass implements ITestListener {
    // RemoteWebDriver driver;
    // // WebDriver driver = null;

    // public ListenerClass(RemoteWebDriver driver) {
    //     this.driver = driver;
    // }

    @Override
    public void onTestStart(ITestResult result) {
        RemoteWebDriver driver = QKART_Tests.getDriver();
        QKART_Tests.takeScreenshot(driver, "TestStart", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        RemoteWebDriver driver = QKART_Tests.getDriver();
        QKART_Tests.takeScreenshot(driver, "TestSuccess", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        RemoteWebDriver driver = QKART_Tests.getDriver();
        QKART_Tests.takeScreenshot(driver, "TestFailure", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {}

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}

    @Override
    public void onStart(ITestContext context) {}

    @Override
    public void onFinish(ITestContext context) {}

}
