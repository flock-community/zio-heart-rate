package community.flock.scalademo

import community.flock.scalademo.Main.AppEnv
import community.flock.scalademo.user.UserService
import sttp.tapir.ztapir._
import zio._

object Routes {

  // corresponds to: GET /hello?name=...
  private val helloWorldEndpoint: ZServerEndpoint[AppEnv, Unit, String, String] = endpoint
    .get
    .in("hello")
    .errorOut(stringBody)
    .out(stringBody)
    .zServerLogic(_ => UIO(s"Please tell me your name first!"))

  private val nameEndpoint: ZServerEndpoint[AppEnv, String, String, String] = endpoint
    .post
    .in("hello" / query[String]("name"))
    .errorOut(stringBody)
    .out(stringBody)
    .zServerLogic(name => UIO(s"Nice to meet you, $name"))

  private val userEndpoint: ZServerEndpoint[AppEnv, String, String, String] = endpoint
    .get
    .in("user" / path[String]("id"))
    .errorOut(stringBody)
    .out(stringBody)
    .zServerLogic(id => UserService.getUser(id).map(it => it.toString))

  val endpoints = List(nameEndpoint, helloWorldEndpoint, userEndpoint)

}
