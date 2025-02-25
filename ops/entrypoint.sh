#/bin/bash

### java启动脚本
### 说明：pod启动默认传入NAMESPACE以及APP_NAME两个变量，控制不同环境，不同应用名称启动
###       NAMESPACE目前有:prod-tc,prod-ali,gray-ali,
### 新添加环境只需要添加对应的NAMESPACE-APP_NAME 函数即可

set -e

# ************** 开发配置区域 **********
# ==== ai-search-platform ==== #
# # 阿里云生产环境
ai-search-platform-prod-ali() {
    java -Xmx6096m -Xms6096m -XX:-OmitStackTraceInFastThrow -XX:+PrintGC -XX:+PrintGCDateStamps -Xloggc:/data/logs/ai-search-platform/gc-%t.log -XX:HeapDumpPath=/data/logs/ai-search-platform/java.hprof -XX:-UseAdaptiveSizePolicy -XX:+PrintAdaptiveSizePolicy -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseGCLogFileRotation -XX:MetaspaceSize=128M -XX:MaxMetaspaceSize=256m -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=128m -XX:+HeapDumpOnOutOfMemoryError -XX:SurvivorRatio=6 -XX:NewRatio=1 -XX:ReservedCodeCacheSize=512m -XX:InitialCodeCacheSize=512m -XX:CMSInitiatingOccupancyFraction=70 -XX:+UseCMSInitiatingOccupancyOnly -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=0 -XX:+CMSParallelInitialMarkEnabled -XX:PrintCMSStatistics=1 -XX:+CMSScavengeBeforeRemark -XX:+UseConcMarkSweepGC -XX:+PrintGCApplicationStoppedTime -Dcluster=${NAMESPACE}  -Dcloud=ali -Dapp.id=ai-search-platform -jar java_app.jar --spring.profiles.active=prod-ali
}

# # 阿里云灰度环境

# 腾讯云生产环境
ai-search-platform-prod-tc() {
    java -Xmx6096m -Xms6096m -XX:-OmitStackTraceInFastThrow -XX:+PrintGC -XX:+PrintGCDateStamps -Xloggc:/data/logs/ai-search-platform/gc-%t.log -XX:HeapDumpPath=/data/logs/ai-search-platform/java.hprof -XX:-UseAdaptiveSizePolicy -XX:+PrintAdaptiveSizePolicy -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseGCLogFileRotation -XX:MetaspaceSize=128M -XX:MaxMetaspaceSize=256m -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=128m -XX:+HeapDumpOnOutOfMemoryError -XX:SurvivorRatio=6 -XX:NewRatio=1 -XX:ReservedCodeCacheSize=512m -XX:InitialCodeCacheSize=512m -XX:CMSInitiatingOccupancyFraction=70 -XX:+UseCMSInitiatingOccupancyOnly -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=0 -XX:+CMSParallelInitialMarkEnabled -XX:PrintCMSStatistics=1 -XX:+CMSScavengeBeforeRemark -XX:+UseConcMarkSweepGC -XX:+PrintGCApplicationStoppedTime -Dcluster=${NAMESPACE}  -Dcloud=ali -Dapp.id=ai-search-platform -jar java_app.jar --spring.profiles.active=prod-tc
}

# 阿里云测试环境
ai-search-platform-staging() {
    	java -Xmx3072m -Xms3072m -XX:-OmitStackTraceInFastThrow -XX:+PrintGC -XX:+PrintGCDateStamps -Xloggc:/data/logs/ai-search-platform/gc-%t.log -XX:HeapDumpPath=/data/logs/ai-search-platform/java.hprof -XX:-UseAdaptiveSizePolicy -XX:+PrintAdaptiveSizePolicy -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseGCLogFileRotation -XX:MetaspaceSize=128M -XX:MaxMetaspaceSize=256m -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=128m -XX:+HeapDumpOnOutOfMemoryError -XX:SurvivorRatio=6 -XX:NewRatio=1 -XX:ReservedCodeCacheSize=512m -XX:InitialCodeCacheSize=512m -XX:CMSInitiatingOccupancyFraction=70 -XX:+UseCMSInitiatingOccupancyOnly -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=0 -XX:+CMSParallelInitialMarkEnabled -XX:PrintCMSStatistics=1 -XX:+CMSScavengeBeforeRemark -XX:+UseConcMarkSweepGC -XX:+PrintGCApplicationStoppedTime -Dcluster=${NAMESPACE}  -Dcloud=ali -Dapp.id=ai-search-platform -jar java_app.jar --spring.profiles.active=staging

}
## 钉钉测试环境 ##
ai-search-platform-staging-dt() {
    	java -Xmx4096m -Xms4096m -XX:-OmitStackTraceInFastThrow -XX:+PrintGC -XX:+PrintGCDateStamps -Xloggc:/data/logs/ai-search-platform/gc-%t.log -XX:HeapDumpPath=/data/logs/ai-search-platform/java.hprof -XX:-UseAdaptiveSizePolicy -XX:+PrintAdaptiveSizePolicy -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseGCLogFileRotation -XX:MetaspaceSize=128M -XX:MaxMetaspaceSize=256m -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=128m -XX:+HeapDumpOnOutOfMemoryError -XX:SurvivorRatio=6 -XX:NewRatio=1 -XX:ReservedCodeCacheSize=512m -XX:InitialCodeCacheSize=512m -XX:CMSInitiatingOccupancyFraction=70 -XX:+UseCMSInitiatingOccupancyOnly -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=0 -XX:+CMSParallelInitialMarkEnabled -XX:PrintCMSStatistics=1 -XX:+CMSScavengeBeforeRemark -XX:+UseConcMarkSweepGC -XX:+PrintGCApplicationStoppedTime -Dcluster=${NAMESPACE}  -Dcloud=dt -Dapp.id=ai-search-platform -jar java_app.jar --spring.profiles.active=staging-dt
}
## 腾讯云测试环境 ## 
ai-search-platform-tc-staging() {
    	java -Xmx4096m -Xms4096m -XX:-OmitStackTraceInFastThrow -XX:+PrintGC -XX:+PrintGCDateStamps -Xloggc:/data/logs/ai-search-platform/gc-%t.log -XX:HeapDumpPath=/data/logs/ai-search-platform/java.hprof -XX:-UseAdaptiveSizePolicy -XX:+PrintAdaptiveSizePolicy -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseGCLogFileRotation -XX:MetaspaceSize=128M -XX:MaxMetaspaceSize=256m -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=128m -XX:+HeapDumpOnOutOfMemoryError -XX:SurvivorRatio=6 -XX:NewRatio=1 -XX:ReservedCodeCacheSize=512m -XX:InitialCodeCacheSize=512m -XX:CMSInitiatingOccupancyFraction=70 -XX:+UseCMSInitiatingOccupancyOnly -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=0 -XX:+CMSParallelInitialMarkEnabled -XX:PrintCMSStatistics=1 -XX:+CMSScavengeBeforeRemark -XX:+UseConcMarkSweepGC -XX:+PrintGCApplicationStoppedTime -Dcluster=${NAMESPACE}  -Dcloud=ali -Dapp.id=ai-search-platform -jar java_app.jar --spring.profiles.active=tc-staging
}
# # 亚马逊云生产环境
ai-search-platform-prod-aws-sg() {
    java -Xmx6096m -Xms6096m -XX:-OmitStackTraceInFastThrow -XX:+PrintGC -XX:+PrintGCDateStamps -Xloggc:/data/logs/ai-search-platform/gc-%t.log -XX:HeapDumpPath=/data/logs/ai-search-platform/java.hprof -XX:-UseAdaptiveSizePolicy -XX:+PrintAdaptiveSizePolicy -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseGCLogFileRotation -XX:MetaspaceSize=128M -XX:MaxMetaspaceSize=256m -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=128m -XX:+HeapDumpOnOutOfMemoryError -XX:SurvivorRatio=6 -XX:NewRatio=1 -XX:ReservedCodeCacheSize=512m -XX:InitialCodeCacheSize=512m -XX:CMSInitiatingOccupancyFraction=70 -XX:+UseCMSInitiatingOccupancyOnly -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=0 -XX:+CMSParallelInitialMarkEnabled -XX:PrintCMSStatistics=1 -XX:+CMSScavengeBeforeRemark -XX:+UseConcMarkSweepGC -XX:+PrintGCApplicationStoppedTime -Dcluster=${NAMESPACE}  -Dcloud=aws -Dapp.id=ai-search -jar java_app.jar -DapolloActive=true -Dapollo.cluster=prod -Dapollo.meta=http://apollo-prod-apollo-configservice:prod-aws-sg:8080 --spring.profiles.active=prod-aws-sg
}
# # 钉钉生产环境
ai-search-platform-prod-dt() {
    java -Xmx6096m -Xms6096m  -XX:-OmitStackTraceInFastThrow -XX:+PrintGC -XX:+PrintGCDateStamps -Xloggc:/data/logs/ai-search-platform/gc-%t.log -XX:HeapDumpPath=/data/logs/ai-search-platform/java.hprof -XX:-UseAdaptiveSizePolicy -XX:+PrintAdaptiveSizePolicy -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseGCLogFileRotation -XX:MetaspaceSize=128M -XX:MaxMetaspaceSize=256m -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=128m -XX:+HeapDumpOnOutOfMemoryError -XX:SurvivorRatio=6 -XX:NewRatio=1 -XX:ReservedCodeCacheSize=512m -XX:InitialCodeCacheSize=512m -XX:CMSInitiatingOccupancyFraction=70 -XX:+UseCMSInitiatingOccupancyOnly -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=0 -XX:+CMSParallelInitialMarkEnabled -XX:PrintCMSStatistics=1 -XX:+CMSScavengeBeforeRemark -XX:+UseConcMarkSweepGC -XX:+PrintGCApplicationStoppedTime -Dcluster=${NAMESPACE}   -Dcloud=ali -Dapp.id=ai-search-platform -jar java_app.jar   --spring.profiles.active=prod-dt
}


### ****** 运维配置，禁止修改 ***** ###

# 判断环境变量是否存在 #

if [ ! -n "${NAMESPACE}" ] ;then
    echo "无对应的namespace信息"
    exit 1
elif [ ! -n "${APP_NAME}" ] ;then
    echo "无对应的APP_NAME应用信息"
    exit 1
fi

# 启动应用 #

if [[ ${NAMESPACE} =~ ^staging* ]];then # 判断阿里测试环境,如果没有定义单独测试环境则走通用启动

        if [ "$(type -t ${APP_NAME}-${NAMESPACE})" = "function" ];then
                echo "开始启动: ${NAMESPACE}环境 ${APP_NAME}应用"
            ${APP_NAME}-${NAMESPACE}
        else
                echo "开始启动: ${NAMESPACE}环境 ${APP_NAME}应用" 
        ${APP_NAME}-staging
    fi

elif [[ ${NAMESPACE} =~ ^tc-staging* ]];then # 判断腾讯测试环境,如果没有定义单独测试环境则走通用启动
        echo "1111"
        if [ "$(type -t ${APP_NAME}-${NAMESPACE})" = "function" ];then
                echo "开始启动: ${NAMESPACE}环境 ${APP_NAME}应用"
            ${APP_NAME}-${NAMESPACE}
        else
                echo "开始启动: ${NAMESPACE}环境 ${APP_NAME}应用" 
        ${APP_NAME}-tc-staging
    fi

else
        echo "开始启动: ${NAMESPACE}环境 ${APP_NAME}应用"
    ${APP_NAME}-${NAMESPACE}
fi

exec "$@"

