/* package dev.aritra.org;

import org.junit.jupiter.api.Test;
import dev.aritra.org.MyStack;
import software.amazon.awscdk.App;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.assertions.Template;

public class MyStackTest {
  @Test
  public void testStack() {
    App app = new App();
    MyStack stack = new MyStack(app, "unittest", new StackProps.Builder().env(MyApp.makeEnv("dev", "eu-central-1")).build());

    Template template = Template.fromStack(stack);
    template.resourceCountIs("AWS::S3::Bucket", 1);
  }
} */