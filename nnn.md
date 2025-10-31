```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.example</groupId>
    <artifactId>solace-aspectj-demo</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.0.0</version>
        <relativePath/>
    </parent>
    
    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <java.version>17</java.version>
        <aspectj.version>1.9.19</aspectj.version>
    </properties>
    
    <dependencies>
        <!-- Spring Boot Starter -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <!-- Spring Cloud Stream -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-stream</artifactId>
            <version>4.0.0</version>
        </dependency>
        
        <!-- Solace Binder (根据实际版本调整) -->
        <dependency>
            <groupId>com.solace.spring.cloud</groupId>
            <artifactId>spring-cloud-starter-stream-solace</artifactId>
            <version>3.2.0</version>
        </dependency>
        
        <!-- AspectJ Runtime -->
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjrt</artifactId>
            <version>${aspectj.version}</version>
        </dependency>
        
        <!-- Spring AOP -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
        
        <!-- 测试依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <!-- AspectJ Maven Plugin for JDK 17 -->
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>aspectj-maven-plugin</artifactId>
                <version>1.14.0</version>
                <configuration>
                    <complianceLevel>17</complianceLevel>
                    <source>17</source>
                    <target>17</target>
                    <encoding>UTF-8</encoding>
                    <showWeaveInfo>true</showWeaveInfo>
                    <verbose>true</verbose>
                    <Xlint>ignore</Xlint>
                    <XaddSerialVersionUID>true</XaddSerialVersionUID>
                    <proc>none</proc>
                    <forceAjcCompile>true</forceAjcCompile>
                    
                    <!-- 织入依赖配置 -->
                    <weaveDependencies>
                        <weaveDependency>
                            <groupId>com.solace.spring.cloud</groupId>
                            <artifactId>spring-cloud-starter-stream-solace</artifactId>
                        </weaveDependency>
                    </weaveDependencies>
                    
                    <!-- 排除不需要织入的包 -->
                    <excludes>
                        <exclude>**/module-info.class</exclude>
                        <exclude>**/package-info.class</exclude>
                    </excludes>
                </configuration>
                <executions>
                    <execution>
                        <phase>process-classes</phase>
                        <goals>
                            <goal>compile</goal>
                        </goals>
                    </execution>
                </executions>
                <dependencies>
                    <dependency>
                        <groupId>org.aspectj</groupId>
                        <artifactId>aspectjtools</artifactId>
                        <version>${aspectj.version}</version>
                    </dependency>
                    <dependency>
                        <groupId>org.aspectj</groupId>
                        <artifactId>aspectjweaver</artifactId>
                        <version>${aspectj.version}</version>
                    </dependency>
                </dependencies>
            </plugin>
            
            <!-- Spring Boot Maven Plugin -->
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class SolaceAspectJApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SolaceAspectJApplication.class, args);
    }
```
切面类
```
package com.example.solaceaspectj.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Solace消息监听器切面 - 编译时织入
 * 适用于JDK 17 + Spring Boot 3
 */
@Aspect
@Component
public class SolaceMessageAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(SolaceMessageAspect.class);
    
    /**
     * 切入点1: 拦截所有InboundXMLMessageListener及其子类的handleMessage方法
     */
    @Pointcut("execution(* com.solacesystems.common.InboundXMLMessageListener+.handleMessage(..))")
    public void solaceMessageHandling() {}
    
    /**
     * 切入点2: 拦截Solace相关包中的所有消息处理方法
     */
    @Pointcut("within(com.solacesystems..*) && execution(* handleMessage*(..))")
    public void solacePackageMessageHandling() {}
    
    /**
     * 切入点3: 拦截消息监听相关的类
     */
    @Pointcut("execution(* *..*MessageListener*.handleMessage(..))")
    public void messageListenerHandling() {}
    
    /**
     * 组合切入点
     */
    @Pointcut("solaceMessageHandling() || solacePackageMessageHandling() || messageListenerHandling()")
    public void combinedSolaceHandling() {}
    
    /**
     * 环绕通知：拦截消息处理全过程
     */
    @Around("combinedSolaceHandling()")
    public Object aroundHandleMessage(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        
        long startTime = System.currentTimeMillis();
        String messageId = extractMessageId(args);
        
        // 记录开始日志
        logger.info("""
            🟡 [AspectJ-CTW] 开始处理Solace消息
              类: {}
              方法: {} 
              消息ID: {}
              参数数量: {}
            """, className, methodName, messageId, args.length);
        
        // 记录详细参数信息
        if (logger.isDebugEnabled()) {
            logger.debug("消息参数详情: {}", Arrays.toString(args));
        }
        
        try {
            // 执行原始方法
            Object result = joinPoint.proceed();
            
            long duration = System.currentTimeMillis() - startTime;
            
            // 成功日志
            logger.info("""
                🟢 [AspectJ-CTW] Solace消息处理成功
                  类: {}
                  消息ID: {}
                  耗时: {}ms
                  结果: {}
                """, className, messageId, duration, 
                result != null ? result.getClass().getSimpleName() : "null");
            
            return result;
            
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            
            // 错误日志
            logger.error("""
                🔴 [AspectJ-CTW] Solace消息处理失败
                  类: {}
                  方法: {}
                  消息ID: {}
                  耗时: {}ms
                  异常: {}
                """, className, methodName, messageId, duration, e.getMessage(), e);
            
            throw e;
        }
    }
    
    /**
     * 提取消息ID
     */
    private String extractMessageId(Object[] args) {
        if (args == null || args.length == 0) {
            return "unknown";
        }
        
        for (Object arg : args) {
            if (arg instanceof Message) {
                Message<?> message = (Message<?>) arg;
                Object messageId = message.getHeaders().get("id");
                Object correlationId = message.getHeaders().get("correlationId");
                Object timestamp = message.getHeaders().get("timestamp");
                
                return String.format("id=%s, correlationId=%s, timestamp=%s", 
                    messageId, correlationId, timestamp);
            }
        }
        
        return "no-message-headers";
    }
    
    /**
     * 拦截构造函数 - 监控对象创建
     */
    @Pointcut("execution(com.solacesystems.common.InboundXMLMessageListener+.new(..))")
    public void solaceListenerConstruction() {}
    
    @Around("solaceListenerConstruction()")
    public Object aroundConstructor(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        Object[] args = joinPoint.getArgs();
        
        logger.info("""
            🏗️  [AspectJ-CTW] 创建Solace监听器实例
              类: {}
              参数: {}
            """, className, Arrays.toString(args));
        
        long startTime = System.currentTimeMillis();
        Object instance = joinPoint.proceed();
        long duration = System.currentTimeMillis() - startTime;
        
        logger.info("""
            ✅  [AspectJ-CTW] Solace监听器创建完成
              类: {}
              实例: {}
              耗时: {}ms
            """, className, instance.hashCode(), duration);
        
        return instance;
    }
}
```




# 自定义AspectJ配置
aspectj:
  enabled: true
  weave-info: true
