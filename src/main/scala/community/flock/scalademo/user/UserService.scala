package community.flock.scalademo.user

import community.flock.scalademo.userRepository
import zio.clock

import java.util.concurrent.TimeUnit


object UserService {

  def getUser(id: String) = for {
    now <- clock.currentTime(TimeUnit.MILLISECONDS)
    user <- userRepository.getUser(id, now)
  } yield user

}
