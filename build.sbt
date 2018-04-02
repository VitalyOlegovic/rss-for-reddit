organization := "java-rss-for-reddit"

name := "java-rss-for-reddit"

version := "0.0.1"

scalaVersion := "2.12.5"

resolvers ++= Seq(
  "Cloudera" at "https://repository.cloudera.com/content/repositories/releases/",
  "Spring plugins" at "http://repo.spring.io/plugins-release/",
  "jcenter" at "https://jcenter.bintray.com"
)

val springVersion = "1.5.10.RELEASE"

libraryDependencies ++= Seq(
  "org.springframework.boot" % "spring-boot-starter-data-jpa" % springVersion,
  "org.springframework.boot" % "spring-boot-starter-test" % springVersion % "test",
  "org.springframework.boot" % "spring-boot-starter-web" % springVersion,
  "net.dean.jraw" % "JRAW" % "0.9.0",
  "com.rometools" % "rome" % "1.7.0",
  "org.apache.commons" % "commons-lang3" % "3.4",
  "commons-io" % "commons-io" % "2.5",
  "org.hibernate.javax.persistence" % "hibernate-jpa-2.1-api" % "1.0.0.Final",
  "mysql" % "mysql-connector-java" % "6.0.6",
  "org.apache.tomcat.embed" % "tomcat-embed-jasper" % "8.5.29",
  "javax.servlet.jsp.jstl" % "jstl" % "1.2",
  "org.jsoup" % "jsoup" % "1.10.2",
  "org.scalatest"     %% "scalatest"   % "3.0.3" % Test withSources(),
  "junit"             %  "junit"       % "4.12"  % Test
)

mainClass in Compile := Some("mreddit_bot.SpringBootStart")
