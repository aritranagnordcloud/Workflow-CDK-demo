package dev.aritra.org;

import java.util.Map;

public class FetchExistingCFValues {


    static IBackendStackProps SetPropsValue(Map<String, Object> initialProps) {
        IBackendStackProps stackProps = new IBackendStackProps();
        stackProps.setAlbListnerPriority((Integer) initialProps.get("albListernerPriority"));
        stackProps.setImageName(initialProps.get("imageName").toString());
        stackProps.setServiceName(initialProps.get("serviceName").toString());
        stackProps.setDistributionZoneId("<<Distribution Id>>");
        stackProps.setRepoName(initialProps.get("repoName").toString());
        stackProps.setEnvironment(initialProps.get("accountId").toString());
        stackProps.setRegion(initialProps.get("region").toString());
        stackProps.setDesiredCount((int) initialProps.get("desiredCount"));
        stackProps.setAutoScalingTargetValue((int) initialProps.get("autoscalingTargetValue"));
        stackProps.setDockerTag(initialProps.get("dockerTag").toString());
            stackProps.setDnsDomain("<<DNS Domain>>");
            stackProps.setVpcId("<<VPC ID>>");
            stackProps.setClusterName("<<Cluster Name>>");
            stackProps.setECSInstanceSecurityGroup("<<Security Group>>");
            stackProps.setCommonAlbDnsName("<<ALB DNS NAME>>");
            stackProps.setECSTaskExecutionRole("<<ECS Task Execution Role>>");
            stackProps.setCommonAlbHostedZoneId("Z215JYRZR1TBD5");
            stackProps.setEcsListnerArn("<<ECS Listener ARN >>");

      
        return stackProps;
    }

}
