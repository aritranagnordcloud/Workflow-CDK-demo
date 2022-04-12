import java.util.Collections;

import io.github.cdklabs.projen.awscdk.AwsCdkJavaApp;
import io.github.cdklabs.projen.awscdk.AwsCdkJavaAppOptions;

public class projenrc {
    public static void main(String[] args) {
        AwsCdkJavaApp project = new AwsCdkJavaApp(AwsCdkJavaAppOptions.builder()
            .artifactId("my-app")
            .cdkVersion("2.19.0")
            .groupId("dev.aritra.org")
            .mainClass("dev.aritra.org.MyApp")
            .name("end-end-demo")
            .version("0.1.0")            
            .build());
            
        project.synth();
    }
}