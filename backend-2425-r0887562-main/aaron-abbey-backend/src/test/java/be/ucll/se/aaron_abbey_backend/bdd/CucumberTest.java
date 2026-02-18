package be.ucll.se.aaron_abbey_backend.bdd;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.SpringFactory;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/java/be/ucll/se/aaron_abbey_backend/resources/features", 
glue = "be.ucll.se.aaron_abbey_backend.bdd.steps",
plugin = {"pretty", "json:target/cucumber-report/cucumber.json", "html:target/cucumber-report/cucumber-pretty.html"},
objectFactory = SpringFactory.class)
public class CucumberTest {

}
