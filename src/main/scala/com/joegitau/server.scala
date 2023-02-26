package com.joegitau

import akka.NotUsed
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import com.joegitau.actors.PlayerActor
import com.joegitau.dao.PlayerDaoImpl
import com.joegitau.http.PlayerRouter
import com.joegitau.services.PlayerServiceImpl
import com.joegitau.slick.Connection.Db

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

// Entrypoint: used as the root behavior of our actor system
object server {
  lazy val interface = "localhost"
  lazy val port      = 8000

  private def startHttpServer(routes: Route)(implicit system: ActorSystem[_]): Unit = {
    implicit val ec: ExecutionContextExecutor = system.executionContext

    val serverBinding = Http().newServerAt(interface, port).bind(routes)

    serverBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info(s"Server running at http://${address.getHostString}:${address.getPort}/")
      case Failure(ex)      =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }

  val userGuardian: Behavior[NotUsed] = Behaviors.setup[NotUsed] { context =>
    implicit val ec: ExecutionContextExecutor = context.system.executionContext
    implicit val system: ActorSystem[Nothing] = context.system

    val playerDao     = new PlayerDaoImpl(Db)
    val playerService = new PlayerServiceImpl(playerDao)

    val playerActor = context.spawn(PlayerActor(playerService), "player-actor")

    val routes = new PlayerRouter(playerActor).routes
    startHttpServer(routes)

    Behaviors.empty
  }

  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem(userGuardian, "simple-akka-slick-system")

    // optional - terminate after 10 minutes
    // implicit val ec: ExecutionContext = actorSystem.executionContext
    // actorSystem.scheduler.scheduleOnce(10.minutes, () => actorSystem.terminate())
  }
}
