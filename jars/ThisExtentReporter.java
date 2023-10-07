package hooks;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.cucumber.java.AfterAll;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.messages.types.TestStepFinished;
import io.cucumber.messages.types.TestStepResult;
import io.cucumber.messages.types.TestStepResultStatus;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ThisExtentReporter {
    public ExtentSparkReporter spark;
    public static ExtentReports extent;
    public ExtentTest testlogger;
    public TestStepFinished stepFinished;
    public TestStepResult stepResult;

    public ThisExtentReporter(TestStepFinished stepFinished, TestStepResult stepResult) {
        this.stepFinished = stepFinished;
        this.stepResult = stepResult;
    }

    @Before
    public void urlConfig(){
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.basePath = "/person";
        RestAssured.defaultParser = Parser.JSON;
    }

    @Before("@extent_setup")
    public void beforeFeature(){
        String dateString = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        /*DateTimeFormatter.ofPattern();*/

        spark = new ExtentSparkReporter(
                System.getProperty("user.dir") + "reports" + File.separator + dateString + "_report.html" );
        spark.config().setDocumentTitle("Point Application");
        spark.config().setReportName("Point Test Suite");
        spark.config().setTheme(Theme.DARK);
        spark.config().setEncoding("utf-8");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    public void afterStep(){
        testlogger = extent.createTest(stepFinished.getTestStepId());

        if (stepFinished.getTestStepResult().getStatus().equals(TestStepResultStatus.PASSED)){
            Markup m = MarkupHelper.createLabel( stepFinished.getTestStepId() +" PASSED", ExtentColor.GREEN);
            testlogger.log(Status.PASS,m);
        }
        if (stepFinished.getTestStepResult().getStatus().equals(TestStepResultStatus.FAILED)){
            Markup m = MarkupHelper.createLabel( stepFinished.getTestStepId() +" FAILED", ExtentColor.RED);
            testlogger.log(Status.PASS,m);
        }
        if (stepFinished.getTestStepResult().getStatus().equals(TestStepResultStatus.SKIPPED)){
            Markup m = MarkupHelper.createLabel( stepFinished.getTestStepId() +" SKIPPED", ExtentColor.ORANGE);
            testlogger.log(Status.SKIP,m);
        }
    }
    
    @AfterAll
    public static void after_all(){
        extent.flush();
    }
}