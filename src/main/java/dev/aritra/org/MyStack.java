package dev.aritra.org;

import java.util.Arrays;

import org.jetbrains.annotations.NotNull;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.CfnOutputProps;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.applicationautoscaling.ServiceNamespace;
import software.amazon.awscdk.services.ec2.IVpc;
import software.amazon.awscdk.services.ec2.SecurityGroup;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ec2.VpcLookupOptions;
import software.amazon.awscdk.services.ecr.Repository;
import software.amazon.awscdk.services.ecs.AwsLogDriverProps;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.ClusterAttributes;
import software.amazon.awscdk.services.ecs.ContainerDefinitionOptions;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.CpuUtilizationScalingProps;
import software.amazon.awscdk.services.ecs.Ec2Service;
import software.amazon.awscdk.services.ecs.Ec2ServiceProps;
import software.amazon.awscdk.services.ecs.Ec2TaskDefinition;
import software.amazon.awscdk.services.ecs.Ec2TaskDefinitionProps;
import software.amazon.awscdk.services.ecs.ICluster;
import software.amazon.awscdk.services.ecs.LoadBalancerTargetOptions;
import software.amazon.awscdk.services.ecs.LogDrivers;
import software.amazon.awscdk.services.ecs.PortMapping;
import software.amazon.awscdk.services.ecs.ScalableTaskCount;
import software.amazon.awscdk.services.ecs.ScalableTaskCountProps;
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationListener;
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationListenerLookupOptions;
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationListenerRule;
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationListenerRuleProps;
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationProtocol;
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationTargetGroup;
import software.amazon.awscdk.services.elasticloadbalancingv2.ApplicationTargetGroupProps;
import software.amazon.awscdk.services.elasticloadbalancingv2.HealthCheck;
import software.amazon.awscdk.services.elasticloadbalancingv2.IApplicationTargetGroup;
import software.amazon.awscdk.services.elasticloadbalancingv2.ListenerAction;
import software.amazon.awscdk.services.elasticloadbalancingv2.ListenerCondition;
import software.amazon.awscdk.services.elasticloadbalancingv2.Protocol;
import software.amazon.awscdk.services.elasticloadbalancingv2.TargetType;
import software.amazon.awscdk.services.iam.ManagedPolicy;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.iam.RoleProps;
import software.amazon.awscdk.services.iam.ServicePrincipal;
import software.amazon.awscdk.services.logs.LogGroup;
import software.amazon.awscdk.services.logs.RetentionDays;
import software.amazon.awscdk.services.route53.AliasRecordTargetConfig;
import software.amazon.awscdk.services.route53.HostedZone;
import software.amazon.awscdk.services.route53.HostedZoneAttributes;
import software.amazon.awscdk.services.route53.IAliasRecordTarget;
import software.amazon.awscdk.services.route53.IHostedZone;
import software.amazon.awscdk.services.route53.IRecordSet;
import software.amazon.awscdk.services.route53.RecordSet;
import software.amazon.awscdk.services.route53.RecordSetProps;
import software.amazon.awscdk.services.route53.RecordTarget;
import software.amazon.awscdk.services.route53.RecordType;
import software.amazon.awscdk.services.ssm.StringParameter;
import software.constructs.Construct;

public class MyStack extends Stack {


    public MyStack() {
        super();
    }

    public MyStack(final Construct scope, final String id, StackProps props, IBackendStackProps stackProps) {
        super(scope, id, props);

     
        /**
         * Look up the Existing VPC from the Props input
         */
        final IVpc ivPC = lookupExistingVpc(stackProps);
        /**
         * Look up the Existing Cluster from the Props input
         */
        final ICluster cluster = lookupExistingEcsCluster(stackProps, ivPC);
        /**
         * Create the Task Defination and add it to the ECS Service from the Props input
         */
        Ec2TaskDefinition taskDefinition = produceTaskDefination(id, stackProps);

        Ec2Service service = produceEc2Service(id, stackProps, cluster, taskDefinition);
        /**
         * Create the private Record set for LoadBalancer
         */
        RecordSet recordSet = producePrivateRecordSet(id, stackProps);
        /**
         * Generate the Load balacer settings for the existing LB
         */
        configureLoadBalancerRules(stackProps, ivPC, service, recordSet);

        configureAutoScalingEcS(stackProps, service);

        /** Generate the Output from Record Set to the private API endpoint */
        createExportValues(id, recordSet);

    }

    

    /**
     * Generate the Export name and values for the stack
     * 
     * @param id
     * @param recordSet
     */
    private void createExportValues(final String id, RecordSet recordSet) {
        new CfnOutput(this, "ApiPrivateDNSEndpoint", CfnOutputProps.builder()
                .exportName(id.concat("-ApiPrivateDNSEndpoint"))
                .value(recordSet.getDomainName())
                .build());
    }

    /**
     * Configure AutoScaling ECS Group for the ECS Serivce
     * 
     * @param stackProps
     * @param service
     */
    private void configureAutoScalingEcS(IBackendStackProps stackProps, Ec2Service service) {

        ScalableTaskCount scaling = new ScalableTaskCount(this, "scalingTaskcount", ScalableTaskCountProps
                .builder()
                .maxCapacity(stackProps.getMaxContainer())
                .minCapacity(stackProps.getMinContainer())
                .resourceId("service"
                        .concat("/")
                        .concat(stackProps.getClusterName())
                        .concat("/")
                        .concat(service.getServiceName()))
                .dimension("ecs:service:DesiredCount")
                .serviceNamespace(ServiceNamespace.ECS)
                .role(new Role(this, "autoscalingRole", RoleProps.builder()
                        .roleName(stackProps.getServiceName().concat("-autoscalingRole"))
                        .assumedBy(new ServicePrincipal("ecs-tasks.amazonaws.com"))
                        .managedPolicies(Arrays.asList(ManagedPolicy.fromManagedPolicyArn(this,
                                "ManagedPolicyArn",
                                "arn:aws:iam::aws:policy/service-role/AmazonEC2ContainerServiceAutoscaleRole")))
                        .build()))
                .build());
        scaling.scaleOnCpuUtilization("CPUSScaling", CpuUtilizationScalingProps.builder()
                .policyName("policy/".concat(service.getServiceName()).concat("/autoscaling"))
                .scaleInCooldown(Duration.seconds(10))
                .scaleOutCooldown(Duration.seconds(10))
                .targetUtilizationPercent(stackProps.getAutoScalingTargetValue())
                .build());

    }

    /**
     * Configure Load Balancing Rules for the existing ARN to set the record set as
     * hostheader and service as TG
     * 
     * @param stackProps
     * @param ivPC
     * @param service
     * @param recordSet
     */

    private void configureLoadBalancerRules(IBackendStackProps stackProps, final IVpc ivPC, Ec2Service service,
            RecordSet recordSet) {
        IApplicationTargetGroup iTargetGroup = new ApplicationTargetGroup(this, "WebAppTargetGroup",
                ApplicationTargetGroupProps.builder()
                        .healthCheck(HealthCheck.builder().enabled(true).path("/fruits")
                                .protocol(Protocol.HTTP).build())
                        .vpc(ivPC)
                        .port(80)
                        .protocol(ApplicationProtocol.HTTP)
                        .targetType(TargetType.INSTANCE)
                        .targets(Arrays.asList(service
                                .loadBalancerTarget(LoadBalancerTargetOptions.builder()
                                        .containerName("backend")
                                        .containerPort(8080)
                                        .build())))
                        .build());

        new ApplicationListenerRule(this, "ListenerRule", ApplicationListenerRuleProps.builder()
                .priority(stackProps.getAlbListnerPriority())
                .action(ListenerAction.forward(Arrays.asList(iTargetGroup)))
                .conditions(Arrays.asList(ListenerCondition
                        .hostHeaders(Arrays.asList(recordSet.getDomainName()))))
                .listener(ApplicationListener.fromLookup(this, "LookupApplicationListener",
                        ApplicationListenerLookupOptions.builder()
                                .listenerArn(stackProps.getEcsListnerArn()).build()))
                .build());
    }

    /**
     * Create the private Record set for LoadBalancer
     * 
     * @param id
     * @param stackProps
     * @return
     */
    private RecordSet producePrivateRecordSet(final String id, IBackendStackProps stackProps) {

        return new RecordSet(this, "PrivateRecord",
        RecordSetProps.builder()
                .recordName(id.concat("-app").concat(".")
                        .concat(stackProps.getDnsDomain()))
                .recordType(RecordType.A)
                .target(RecordTarget.fromAlias(new IAliasRecordTarget() {
                    @Override
                    public @NotNull AliasRecordTargetConfig bind(
                            @NotNull IRecordSet record) {

                        return AliasRecordTargetConfig.builder()
                                .dnsName(stackProps
                                        .getCommonAlbDnsName())
                                .hostedZoneId(stackProps
                                        .getCommonAlbHostedZoneId())
                                .build();
                    }

                    @Override
                    public @NotNull AliasRecordTargetConfig bind(
                            @NotNull IRecordSet record, IHostedZone zone) {
                        return null;
                    }
                }))
                .zone(HostedZone.fromHostedZoneAttributes(this, "ExistingHostedZone",
                        HostedZoneAttributes.builder()
                                .hostedZoneId(stackProps
                                        .getDistributionZoneId())
                                .zoneName(stackProps.getDnsDomain())
                                .build()))
                .build());
    }

    /**
     * Create new service in the existing cluster with newly created task definition
     * 
     * @param id
     * @param stackProps
     * @param cluster
     * @param taskDefinition
     * @return EC2Service
     */
    private Ec2Service produceEc2Service(final String id, IBackendStackProps stackProps, final ICluster cluster,
            Ec2TaskDefinition taskDefinition) {

        Ec2Service service = new Ec2Service(this, "EC2Service", Ec2ServiceProps.builder()
                .cluster(cluster)
                .taskDefinition(taskDefinition)
                .serviceName(id.concat("-app"))
                .desiredCount(stackProps.getDesiredCount())

                .build());
        service.applyRemovalPolicy(RemovalPolicy.DESTROY);
        return service;
    }

    /**
     * Create new Task Definition for producing service in the existing cluster
     * 
     * @param id
     * @param stackProps
     * @return Task Definition
     */

    private Ec2TaskDefinition produceTaskDefination(final String id, IBackendStackProps stackProps) {
        Ec2TaskDefinition taskDefinition = new Ec2TaskDefinition(this, "ECSTaskDefination",
                Ec2TaskDefinitionProps.builder()
                        .executionRole(Role.fromRoleArn(this, "ExecutionRole",
                                stackProps.getECSTaskExecutionRole()))
                        .taskRole(Role.fromRoleArn(this, "TaskRole",
                                stackProps.getECSTaskExecutionRole()))
                        .family(id.concat("-").concat("app"))
                        .build());

        taskDefinition.addContainer("backend", ContainerDefinitionOptions.builder()
                .image(stackProps.getRepoName() != null
                        ? ContainerImage.fromEcrRepository(
                                Repository.fromRepositoryName(this, "existingRepo",
                                        stackProps.getRepoName()),
                                stackProps.getDockerTag())
                        : ContainerImage.fromAsset(stackProps.getImageName()))
                .memoryLimitMiB(256)
                .portMappings(Arrays
                        .asList(PortMapping.builder().containerPort(8080).hostPort(0).build()))
                .logging(LogDrivers.awsLogs(AwsLogDriverProps.builder()
                        .streamPrefix("/")
                        .logGroup(LogGroup.Builder.create(this, "logGroup")
                                .logGroupName("/ecs/"
                                        .concat(stackProps.getServiceName())
                                        .concat("/TaskDefinition"))
                                .retention(RetentionDays.ONE_WEEK)
                                .build())
                        .build()))
                .build());
        return taskDefinition;
    }

    /**
     * Look up the Existing Cluster from the Props input
     * 
     * @param stackProps
     * @param ivPC
     * @return Existing Cluster Details
     */
    private ICluster lookupExistingEcsCluster(IBackendStackProps stackProps, final IVpc ivPC) {
        return Cluster.fromClusterAttributes(this, "existingCluster",
        ClusterAttributes.builder().clusterName(stackProps.getClusterName())
                .vpc(ivPC)
                .securityGroups(Arrays.asList(SecurityGroup.fromSecurityGroupId(this,
                        "existingSecuritGroup",
                        stackProps.getECSInstanceSecurityGroup())))
                .build());
    }

    /**
     * Look up the Existing VPC from the Props input
     * 
     * @param stackProps
     * @return Existing VPC
     */
    IVpc lookupExistingVpc(IBackendStackProps stackProps) {
        return Vpc.fromLookup(this, "existingVPC",
        VpcLookupOptions.builder().vpcId(stackProps.getVpcId()).build());
    }
}
