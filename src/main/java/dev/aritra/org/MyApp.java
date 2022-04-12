package dev.aritra.org;

import java.util.HashMap;
import java.util.Map;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

public class MyApp {

    static Environment makeEnv(String account, String region) {

        account = (account == null) ? System.getenv("CDK_DEFAULT_ACCOUNT") : account;
        region = (region == null) ? System.getenv("CDK_DEFAULT_REGION") : region;

        return Environment.builder()
                .account(account)
                .region(region)
                .build();
    }

  public static void main(final String[] args) {
    
    App app = new App();
    IBackendStackProps props = FetchExistingCFValues.SetPropsValue(checkifContextValue(app));
    new MyStack(app, "aritra-cdk-workflow", new StackProps.Builder()
    .env(makeEnv("<<Account number>>", "eu-central-1")).build(),props);
    app.synth();
  }
  static Map<String, Object> checkifContextValue(App app) {
    Map<String, Object> initialProps = new HashMap<String, Object>();
    initialProps.put("ecsClusterStackName",
            app.getNode().tryGetContext("ecsClusterStackName") != null
                    ? app.getNode().tryGetContext("ecsClusterStackName").toString()
                    : "EcsClusterCDKApp");
    initialProps.put("accountId",
            app.getNode().tryGetContext("accountId") != null ? app.getNode().tryGetContext("accountId").toString()
                    : "<<Account number>>");
    initialProps.put("region",
            app.getNode().tryGetContext("region") != null ? app.getNode().tryGetContext("region").toString()
                    : "eu-central-1");
    initialProps.put("distributionId",
            app.getNode().tryGetContext("distributionId") != null
                    ? app.getNode().tryGetContext("distributionId").toString()
                    : "<<Distribution Id>>");
    initialProps.put("imageName",
            app.getNode().tryGetContext("imageName") != null ? app.getNode().tryGetContext("imageName").toString()
                    : "../app");
    initialProps.put("serviceName",
            app.getNode().tryGetContext("serviceName") != null
                    ? app.getNode().tryGetContext("serviceName").toString()
                    : "ec2service-cdk-java");
    initialProps.put("albListernerPriority",
            app.getNode().tryGetContext("albListernerPriority") != null
                    ? app.getNode().tryGetContext("albListernerPriority")
                    : 8);
    initialProps.put("desiredCount",
            app.getNode().tryGetContext("desiredCount") != null ? app.getNode().tryGetContext("desiredCount") : 1);
    initialProps.put("autoscalingTargetValue",
            app.getNode().tryGetContext("autoscalingTargetValue") != null
                    ? app.getNode().tryGetContext("autoscalingTargetValue").toString()
                    : 50);
    initialProps.put("repoName",
            app.getNode().tryGetContext("repoName") != null ? app.getNode().tryGetContext("repoName").toString()
                    : "sample-rest-api");
    initialProps.put("dockerTag",
            app.getNode().tryGetContext("dockerTag") != null ? app.getNode().tryGetContext("dockerTag").toString()
                    : "latest");
    return initialProps;
}
}