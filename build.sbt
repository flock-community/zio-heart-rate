val ZioVersion = "1.0.9"
val TapirVersion = "0.17.20"
val DoobieVersion = "0.13.4"
val MunitVersion = "0.7.20"
val MunitCatsEffectVersion = "0.13.0"
val LogbackVersion = "1.2.3"

lazy val root = (project in file("."))
  .settings(
    organization := "community.flock",
    name := "scala-demo",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.6",
    assembly / mainClass := Some("community.flock.scalademo.Main"),
    assembly / assemblyJarName := "scala-demo.jar",
    libraryDependencies ++= Seq(
      "dev.zio"                     %% "zio"                      % ZioVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-core"               % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-sttp-client"        % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-zio"                % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-zio-http4s-server"  % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server"      % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-http4s"  % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"       % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % TapirVersion,
      "org.tpolecat"                %% "doobie-core"              % DoobieVersion,
      "org.tpolecat"                %% "doobie-postgres"          % DoobieVersion,
      "org.scalameta"               %% "munit"                    % MunitVersion           % Test,
      "org.typelevel"               %% "munit-cats-effect-2"      % MunitCatsEffectVersion % Test,
      "ch.qos.logback"              %  "logback-classic"          % LogbackVersion,
      "org.scalameta"               %% "svm-subs"                 % "20.2.0"
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.13.0" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    testFrameworks += new TestFramework("munit.Framework")
  )