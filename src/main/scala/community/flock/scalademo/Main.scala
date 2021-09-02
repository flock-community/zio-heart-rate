package community.flock.scalademo

import community.flock.scalademo.Routes.routes
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import zio._
import zio.blocking.Blocking
import zio.clock.Clock
import zio.interop.catz._

object Main extends App {

  type AppEnvironment = Clock

  private val appEnvironment = Blocking.live


  // starting the server
  val serve: ZIO[ZEnv, Throwable, Unit] =
    ZIO.runtime[ZEnv].flatMap { implicit runtime => // This is needed to derive cats-effect instances for that are needed by http4s
      BlazeServerBuilder[RIO[Clock, *]](runtime.platform.executor.asEC)
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(Router("/" -> routes).orNotFound)
        .serve
        .compile
        .drain
    }

  override def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] = serve
    .provideSomeLayer[ZEnv](appEnvironment)
    .exitCode

}
