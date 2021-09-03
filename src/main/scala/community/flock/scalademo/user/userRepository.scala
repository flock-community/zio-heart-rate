package community.flock.scalademo

import community.flock.scalademo.user.User
import zio.{Has, Layer, UIO, URIO, ZIO, ZLayer}

package object userRepository {

  type UserRepository = Has[UserRepository.Service]

  object UserRepository extends Serializable {
    trait Service extends Serializable {
      def getUser(id: String, currentTime: Long): UIO[User]
    }

    object Service {
      val live: Service = (id: String, currentTime: Long) => UIO(User(id, "UserName", currentTime))
    }

    val live: Layer[Nothing, UserRepository] =
      ZLayer.succeed(Service.live)
  }

  def getUser(id: String, currentTime: Long): URIO[UserRepository, User] =
    ZIO.accessM(_.get.getUser(id, currentTime))

}
