
lazy val springBootVersion = "1.2.3.RELEASE"


lazy val commonSettings = Seq(
  organization := "michalz",
  version := "0.1.0",
  scalaVersion := "2.11.6",
  javacOptions ++= Seq("-source", "1.8"),
  libraryDependencies ++= Seq(
    "org.projectlombok" % "lombok" % "1.16.4"
  )
)

lazy val itemApi = (project in file("item-api"))
  .settings(commonSettings: _*)
  .settings(
    name := "Item API"
  )

lazy val externalServices = (project in file("external-services")).dependsOn(itemApi)
  .settings(commonSettings: _*)
  .settings(
    name := "External Services",
    libraryDependencies ++= Seq(
      "org.springframework.boot" % "spring-boot-starter-web" % springBootVersion
    )
  )
  .settings(Revolver.settings: _*)

lazy val mainService = (project in file("main-service")).dependsOn(itemApi)
  .settings(commonSettings: _*)
  .settings(
    name := "Main Service",
    libraryDependencies ++= Seq(
      "org.springframework.boot" % "spring-boot-starter-web" % springBootVersion,
      "org.springframework.boot" % "spring-boot-starter-actuator" % springBootVersion,
      "org.apache.httpcomponents" % "httpasyncclient" % "4.1",
      "com.typesafe.akka" % "akka-actor_2.11" % "2.3.10"
    )
  )
  .settings(Revolver.settings: _*)
