package hooks;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.gherkin.model.Given;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.cucumber.messages.types.TestStepResultStatus;
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestStepFinished;
import io.cucumber.plugin.event.TestStepStarted;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestRunStarted;
import io.cucumber.plugin.event.TestSourceRead;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.HookTestStep;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class CustomReportListener implements EventListener {

    private ExtentSparkReporter spark;
    private ExtentReports extent;

    Map<String, ExtentTest> feature = new HashMap<String, ExtentTest>();
    ExtentTest scenario;
    ExtentTest step;

    public CustomReportListener() {
    };

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        // TODO Auto-generated method stub

        /*
         * :: is method reference , so this::collecTag means collectTags method in
         * 'this' instance. Here we says runStarted method accepts or listens to
         * TestRunStarted event type
         */
        publisher.registerHandlerFor(TestRunStarted.class, this::runStarted);
        publisher.registerHandlerFor(TestRunFinished.class, this::runFinished);
        publisher.registerHandlerFor(TestSourceRead.class, this::featureRead);
        publisher.registerHandlerFor(TestCaseStarted.class, this::ScenarioStarted);
        publisher.registerHandlerFor(TestStepStarted.class, this::stepStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::stepFinished);
    };

    /*
     * Here we set argument type as TestRunStarted if you set anything else then the
     * corresponding register shows error as it doesn't have a listner method that
     * accepts the type specified in TestRunStarted.class
     */

    // Here we create the reporter
    private void runStarted(TestRunStarted event) {
        System.out.println("***************** runStarted(TestRunStarted event : " + event.toString());
        String dateString = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        spark = new ExtentSparkReporter(System.getProperty("user.dir") + File.separator + "reports" + File.separator + dateString + "_report.html");
        spark.config().setDocumentTitle("Point App");
        spark.config().setReportName("Point test suite");
        spark.config().setTheme(Theme.DARK);
        spark.config().setEncoding("utf-8");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    };

    private void runFinished(TestRunFinished event) {
        extent.flush();
    };

    private void featureRead(TestSourceRead event) {
        String featureSource = event.getUri().toString();
        String featureName = featureSource.split(".*/")[1];

        if (feature.get(featureSource) == null) {
            feature.putIfAbsent(featureSource, extent.createTest(featureName));
        }
    };

    private void ScenarioStarted(TestCaseStarted event) {
        String featureName = event.getTestCase().getUri().toString();

        scenario = feature.get(featureName).createNode(event.getTestCase().getName());
    };

    private void stepStarted(TestStepStarted event) {

        String stepName = " ";
        String keyword = "Triggered the hook :";

        if (event.getTestStep() instanceof PickleStepTestStep) {
            // TestStepStarted event implements PickleStepTestStep interface
            // Which have additional methods to interact with the event object
            // So we have to cast TestCase object to get those methods
            PickleStepTestStep steps = (PickleStepTestStep) event.getTestStep();
            stepName = steps.getStep().getText();
            keyword = steps.getStep().getKeyword();
        } else {
            HookTestStep hoo = (HookTestStep) event.getTestStep();
            stepName = hoo.getHookType().name();
        }
        step = scenario.createNode(Given.class, keyword + " " + stepName);
    };

    private void stepFinished(TestStepFinished event) {
        if (event.getResult().getStatus().toString() == "PASSED") {
            step.log(Status.PASS, "This passed");
        } else if (event.getResult().getStatus().toString() == "SKIPPED") {
            step.log(Status.SKIP, "This step was skipped ");
        } else {
            step.log(Status.FAIL, "This failed");
        }
    };

/*    private void stepFinished(TestStepFinished event) {
        String stepName;
        System.out.println("***************** stepFinished(TestStepFinished event.getResults().toString() : " + event.getResult().toString());
        if (event.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep steps = (PickleStepTestStep) event.getTestStep();
            stepName = steps.getStep().getText();
        } else {
            HookTestStep hoo = (HookTestStep) event.getTestStep();
            stepName = hoo.getHookType().name();
        }

        if (event.getResult().getStatus().equals(TestStepResultStatus.PASSED)){
            Markup m = MarkupHelper.createLabel( stepName +" PASSED", ExtentColor.GREEN);
            step.log(Status.PASS,m);
        }
        if (event.getResult().getStatus().equals(TestStepResultStatus.FAILED)){
            Markup m = MarkupHelper.createLabel( stepName +" FAILED", ExtentColor.RED);
            step.log(Status.PASS,m);
        }
        if (event.getResult().getStatus().equals(TestStepResultStatus.SKIPPED)){
            Markup m = MarkupHelper.createLabel( stepName +" SKIPPED", ExtentColor.ORANGE);
            step.log(Status.SKIP,m);
        }
    };*/
}
