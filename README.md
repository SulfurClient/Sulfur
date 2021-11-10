# Sulfur niger hake


![lol](https://i.imgur.com/TWmfhKX.png)

Removed source leaker

shotbow like ruining others

current pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.strezz</groupId>
    <artifactId>MavenMCP</artifactId>
    <version>1.0-1.8.8</version>

    <properties>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <repositories>

        <repository>
            <id>dv8tion</id>
            <name>m2-dv8tion</name>
            <url>https://m2.dv8tion.net/releases</url>
        </repository>

        <repository>
            <name>strezz-central</name>
            <id>strezz</id>
            <url>https://github.com/Strezzed/strezz-central/raw/repository</url>
        </repository>

    </repositories>

    <dependencies>
        <dependency>
            <groupId>net.minecraft</groupId>
            <artifactId>minecraft</artifactId>
            <version>1.8.8</version>
        </dependency>

        <dependency>
            <groupId>oshi-project</groupId>
            <artifactId>oshi-core</artifactId>
            <version>1.1</version>
        </dependency>

        <dependency>
            <groupId>net.java.dev.jna</groupId>
            <artifactId>jna</artifactId>
            <version>3.4.0</version>
        </dependency>

        <dependency>
            <groupId>net.java.dev.jna</groupId>
            <artifactId>platform</artifactId>
            <version>3.4.0</version>
        </dependency>

        <dependency>
            <groupId>com.ibm.icu</groupId>
            <artifactId>icu4j</artifactId>
            <version>51.2</version>
        </dependency>

        <dependency>
            <groupId>net.sf.jopt-simple</groupId>
            <artifactId>jopt-simple</artifactId>
            <version>4.6</version>
        </dependency>

        <dependency>
            <groupId>com.paulscode</groupId>
            <artifactId>codecjorbis</artifactId>
            <version>20101023</version>
        </dependency>

        <dependency>
            <groupId>com.paulscode</groupId>
            <artifactId>codecwav</artifactId>
            <version>20101023</version>
        </dependency>

        <dependency>
            <groupId>com.paulscode</groupId>
            <artifactId>libraryjavasound</artifactId>
            <version>20101123</version>
        </dependency>

        <dependency>
            <groupId>com.paulscode</groupId>
            <artifactId>librarylwjglopenal</artifactId>
            <version>20100824</version>
        </dependency>

        <dependency>
            <groupId>com.paulscode</groupId>
            <artifactId>soundsystem</artifactId>
            <version>20120107</version>
        </dependency>

        <dependency>
            <groupId>io.netty</groupId>
            <artifactId>netty-all</artifactId>
            <version>4.0.23.Final</version>
        </dependency>

        <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
            <version>17.0</version>
        </dependency>

        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>3.3.2</version>
        </dependency>

        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.4</version>
        </dependency>

        <dependency>
            <groupId>commons-codec</groupId>
            <artifactId>commons-codec</artifactId>
            <version>1.9</version>
        </dependency>

        <dependency>
            <groupId>net.java.jinput</groupId>
            <artifactId>jinput</artifactId>
            <version>2.0.5</version>
        </dependency>

        <dependency>
            <groupId>net.java.jutils</groupId>
            <artifactId>jutils</artifactId>
            <version>1.0.0</version>
        </dependency>

        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.2.4</version>
        </dependency>

        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-compress</artifactId>
            <version>1.8.1</version>
        </dependency>



        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
            <version>4.3.3</version>
        </dependency>

        <dependency>
            <groupId>commons-logging</groupId>
            <artifactId>commons-logging</artifactId>
            <version>1.1.3</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/net.dv8tion/JDA -->
        <!-- <dependency>
            <groupId>net.dv8tion</groupId>
            <artifactId>JDA</artifactId>
            <version>3.3.1_303</version>
        </dependency> -->


        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpcore</artifactId>
            <version>4.3.2</version>
        </dependency>

        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-api</artifactId>
            <version>2.0-beta9</version>
        </dependency>

        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-core</artifactId>
            <version>2.0-beta9</version>
        </dependency>

        <dependency>
            <groupId>org.lwjgl</groupId>
            <artifactId>lwjgl</artifactId>
            <version>2.9.4-nightly-20150209</version>
        </dependency>

        <dependency>
            <groupId>org.lwjgl</groupId>
            <artifactId>lwjgl_util</artifactId>
            <version>2.9.4-nightly-20150209</version>
        </dependency>

        <dependency>
            <groupId>com.mojang</groupId>
            <artifactId>realms</artifactId>
            <version>1.7.39</version>
        </dependency>

        <dependency>
            <groupId>com.mojang</groupId>
            <artifactId>authlib</artifactId>
            <version>1.5.21</version>
        </dependency>

        <dependency>
            <groupId>tv.twitch</groupId>
            <artifactId>twitch</artifactId>
            <version>6.5</version>
        </dependency>

        <dependency>
            <groupId>javax.vecmath</groupId>
            <artifactId>vecmath</artifactId>
            <version>1.5.2</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.20</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>


</project>
```

rip my fucking sleep schedule

