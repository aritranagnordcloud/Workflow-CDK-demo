package dev.aritra.org;


enum ILaunchType {
    /** Launch Type for the ECS Service */
    EC2,
    FARGATE
}

enum Itoggle {
    /** Launch Type for the ECS Service */
    DISABLED,
    ENABLED
}

enum Isubnet {
    /** Launch Type for the ECS Service */
    PrivateSubnets,
    PublicSubnets,
}


public class IBackendStackProps {

    /**
     * Image name to push the Docker File taken from the local Docker
     */

    private String imageName;

    /**
     * Repo name to push the Docker File
     */
    private String repoName;

    /**
     * Cluster Name to deploy the service
     */
    private String clusterName;

    /**
     * Service name to push the Docker File
     */
    private String serviceName;

    /**
     * VPC name for the cluster
     */
    private String vpcId;

    /***
     * Network stack name to push the Docker File
     */
    private String networkName;

    /***
     * Env name to push the Docker File
     */
    private String environment;

    /**
     * Number priority value of ALB Listner rule
     */
    private Number albListnerPriority;

    /** Launch type for the ECS service */
    private ILaunchType launchType;

    /** Count for the ECS task */
    private Number desiredCount;

    /** Public IP Toggle */
    private Itoggle publicIPToggle;

    /** Subnets to be Deployed */
    private Isubnet subnets;

    /** Region for the ECS task */
    private String region;

    /** Count for the ECS task */
    private Number maxContainer = 1;

    /** Count for the ECS task */
    private Number minContainer = 1;;

    /** Auto Scaling Target Value */
    private Number autoScalingTargetValue = 50;

    /** Security Group Name in VPC */
    private String eCSInstanceSecurityGroup;

    /** Common ALB Zone Hosted Id */
    private String commonAlbHostedZoneId;

    /** Common ALB DNS Record */
    private String commonAlbDnsName;

    /** Common Task Execution Role */
    private String eCSTaskExecutionRole;

    /** Created DNS Name from previously created Stack */
    private String dnsDomain;

    /** Global Distribution Zone Id from Route53 */
    private String distributionZoneId;

    /** Listner ARN from the previously created ALB */
    private String ecsListnerArn;

    /** Lookup Location for the Existing Stack Outputs */
    private String lookupToggle;

    /** Docker Tag to be injected in the pipeline */
    private String dockerTag;

    public String getImageName() {
        return this.imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getRepoName() {
        return this.repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getClusterName() {
        return this.clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getVpcId() {
        return this.vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    public String getNetworkName() {
        return this.networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public String getEnvironment() {
        return this.environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public Number getAlbListnerPriority() {
        return this.albListnerPriority;
    }

    public void setAlbListnerPriority(Number albListnerPriority) {
        this.albListnerPriority = albListnerPriority;
    }

    public ILaunchType getLaunchType() {
        return this.launchType;
    }

    public void setLaunchType(ILaunchType launchType) {
        this.launchType = launchType;
    }

    public Number getDesiredCount() {
        return this.desiredCount;
    }

    public void setDesiredCount(Number desiredCount) {
        this.desiredCount = desiredCount;
    }

    public Itoggle getPublicIPToggle() {
        return this.publicIPToggle;
    }

    public void setPublicIPToggle(Itoggle publicIPToggle) {
        this.publicIPToggle = publicIPToggle;
    }

    public Isubnet getSubnets() {
        return this.subnets;
    }

    public void setSubnets(Isubnet subnets) {
        this.subnets = subnets;
    }

    public String getRegion() {
        return this.region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Number getMaxContainer() {
        return this.maxContainer;
    }

    public void setMaxContainer(Number maxContainer) {
        this.maxContainer = maxContainer;
    }

    public Number getMinContainer() {
        return this.minContainer;
    }

    public void setMinContainer(Number minContainer) {
        this.minContainer = minContainer;
    }

    public Number getAutoScalingTargetValue() {
        return this.autoScalingTargetValue;
    }

    public void setAutoScalingTargetValue(Number autoScalingTargetValue) {
        this.autoScalingTargetValue = autoScalingTargetValue;
    }

    public String getECSInstanceSecurityGroup() {
        return this.eCSInstanceSecurityGroup;
    }

    public void setECSInstanceSecurityGroup(String eCSInstanceSecurityGroup) {
        this.eCSInstanceSecurityGroup = eCSInstanceSecurityGroup;
    }

    public String getCommonAlbHostedZoneId() {
        return this.commonAlbHostedZoneId;
    }

    public void setCommonAlbHostedZoneId(String commonAlbHostedZoneId) {
        this.commonAlbHostedZoneId = commonAlbHostedZoneId;
    }

    public String getCommonAlbDnsName() {
        return this.commonAlbDnsName;
    }

    public void setCommonAlbDnsName(String commonAlbDnsName) {
        this.commonAlbDnsName = commonAlbDnsName;
    }

    public String getECSTaskExecutionRole() {
        return this.eCSTaskExecutionRole;
    }

    public void setECSTaskExecutionRole(String eCSTaskExecutionRole) {
        this.eCSTaskExecutionRole = eCSTaskExecutionRole;
    }

    public String getDnsDomain() {
        return this.dnsDomain;
    }

    public void setDnsDomain(String dnsDomain) {
        this.dnsDomain = dnsDomain;
    }

    public String getDistributionZoneId() {
        return this.distributionZoneId;
    }

    public void setDistributionZoneId(String distributionZoneId) {
        this.distributionZoneId = distributionZoneId;
    }

    public String getEcsListnerArn() {
        return this.ecsListnerArn;
    }

    public void setEcsListnerArn(String ecsListnerArn) {
        this.ecsListnerArn = ecsListnerArn;
    }

    public String getLookupToggle() {
        return this.lookupToggle;
    }

    public void setLookupToggle(String lookupToggle) {
        this.lookupToggle = lookupToggle;
    }

    public String getDockerTag() {
        return this.dockerTag;
    }

    public void setDockerTag(String dockerTag) {
        this.dockerTag = dockerTag;
    }

}
