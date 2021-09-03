package community.flock.scalademo

import cats.data.Kleisli
import community.flock.scalademo.userRepository.UserRepository
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.{HttpRoutes, Request, Response}
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import sttp.tapir.ztapir.ZServerEndpoint
import zio._
import zio.clock.Clock
import zio.interop.catz._

object Main extends App {

  type AppEnv = ZEnv with UserRepository

  val program: ZIO[AppEnv, Throwable, Unit] =
    for {
      _ <- server(Routes.endpoints)
    } yield ()

  def run(args: List[String]) = {
    program.provideCustomLayer(
      ZEnv.live >+> UserRepository.live
    ).exitCode
  }

  private def server[R](endpoints: List[ZServerEndpoint[R, _, _, _]]): ZIO[ZEnv with R, Throwable, Unit] =
    for {
      _ <- {
        val routes: HttpRoutes[RIO[R with Clock, *]] = ZHttp4sServerInterpreter.from(endpoints).toRoutes
        ZIO.runtime[ZEnv with R].flatMap {
          implicit runtime => {
            val router: Kleisli[RIO[R with Clock, *], Request[RIO[R with Clock, *]], Response[RIO[R with Clock, *]]] = Router("/" -> routes).orNotFound
            BlazeServerBuilder[RIO[R with Clock, *]](runtime.platform.executor.asEC)
              .bindHttp(8080, "0.0.0.0")
              .withHttpApp(router)
              .serve
              .compile
              .drain
          }
        }
      }
    } yield ()

}
