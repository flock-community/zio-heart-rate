package community.flock.scalademo

import community.flock.scalademo.Main.AppEnvironment
import org.http4s._
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import sttp.tapir.ztapir._
import zio._
import zio.clock.Clock
import zio.interop.catz._


object Routes {
  var name: String = null
  // corresponds to: GET /hello?name=...
  private val helloWorldEndpoint: ZServerEndpoint[AppEnvironment, Unit, String, String] = endpoint
    .get
    .in("hello")
    .errorOut(stringBody)
    .out(stringBody)
    .zServerLogic(_ => name match {
      case null => UIO(s"Please tell me your name first!")
      case _ => UIO(s"Hello, $name")
    })

  private val nameEndpoint: ZServerEndpoint[AppEnvironment, String, String, String] = endpoint
    .post
    .in("hello" / query[String]("name"))
    .errorOut(stringBody)
    .out(stringBody)
    .zServerLogic(name => {
      this.name = name;
      UIO(s"Nice to meet you, $name")
    })


  val routes: HttpRoutes[ZIO[AppEnvironment with Has[Clock.Service], Throwable, *]] = ZHttp4sServerInterpreter
    .from(List(nameEndpoint, helloWorldEndpoint))
    .toRoutes
}
