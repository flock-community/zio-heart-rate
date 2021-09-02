package community.flock.scalademo

import community.flock.scalademo.Main.AppEnvironment
import org.http4s._
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import sttp.tapir.ztapir._
import zio._
import zio.interop.catz._


object Routes {

  // corresponds to: GET /hello?name=...
  private val helloWorldEndpoint: ZServerEndpoint[AppEnvironment, Unit, String, String] = endpoint
    .get
    .in("hello")
    .errorOut(stringBody)
    .out(stringBody)
    .zServerLogic(_ => UIO(s"Please tell me your name first!"))

  private val nameEndpoint: ZServerEndpoint[AppEnvironment, String, String, String] = endpoint
    .post
    .in("hello" / query[String]("name"))
    .errorOut(stringBody)
    .out(stringBody)
    .zServerLogic(name => UIO(s"Nice to meet you, $name"))


  val routes: HttpRoutes[ZIO[AppEnvironment, Throwable, *]] = ZHttp4sServerInterpreter
    .from(List(nameEndpoint, helloWorldEndpoint))
    .toRoutes
}
