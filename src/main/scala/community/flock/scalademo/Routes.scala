package community.flock.scalademo

import community.flock.scalademo.Main.AppEnvironment
import org.http4s._
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import sttp.tapir.ztapir._
import zio._
import zio.interop.catz._


object Routes {
  // corresponds to: GET /hello?name=...
  private val helloWorldEndpoint: ZEndpoint[String, String, String] = endpoint
    .get.in("hello" / query[String]("name"))
    .errorOut(stringBody).out(stringBody)

  val routes: HttpRoutes[RIO[AppEnvironment, *]] = ZHttp4sServerInterpreter
    .from(helloWorldEndpoint) { name => UIO(s"Hello, $name") }
    .toRoutes

}
