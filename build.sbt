
lazy val commonSettings = Seq()

lazy val itemApi = (project in file("item-api"))

lazy val externalServices = (project in file("external-services")).dependsOn(itemApi)

lazy val mainService = (project in file("main-service")).dependsOn(itemApi)
