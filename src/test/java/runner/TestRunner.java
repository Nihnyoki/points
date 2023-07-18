package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;

import static io.cucumber.core.options.Constants.PLUGIN_PROPERTY_NAME;

/*@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME), value="hooks.CustomReportListener")*/
@RunWith(Cucumber.class)
@CucumberOptions(
        features = { "classpath:/features" },
        monochrome = true, dryRun = false,
        glue = { "steps/definitions","hooks" },
        plugin = { "hooks.CustomReportListener"/*,"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"*/},
        publish = false     
)
public class TestRunner {

}