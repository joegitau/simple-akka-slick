package com.joegitau.http

import akka.actor.typed.{ActorRef, ActorSystem, Scheduler}
import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.pattern.StatusReply
import akka.util.Timeout
import com.joegitau.model.{PatchPlayer, Player}
import com.joegitau.protocol.PlayerProtocol.PlayerCommand
import com.joegitau.protocol.PlayerProtocol.PlayerCommand._
import com.joegitau.protocol.PlayerProtocol.PlayerResponse._

import scala.concurrent.duration.DurationInt

class PlayerRouter(playerActor: ActorRef[PlayerCommand])(implicit system: ActorSystem[_]) extends JsonMarshaller {
  implicit val scheduler: Scheduler = system.scheduler
  implicit val timeout: Timeout     = 3.seconds

  def createPlayer: Route = {
    (path("create") & post) {
      entity(as[Player]) { player =>
        val toCreate = playerActor.ask(CreatePlayer(player, _))

        onSuccess(toCreate) {
          case StatusReply.Success(CreatePlayerResponse(player)) =>
            respondWithHeader(Location(s"/api/players/${player.id}")) {
              complete(StatusCodes.Created -> s"Player with id: ${player.id} successfully created!")
            }
          case StatusReply.Error(reason)                         => complete(StatusCodes.InternalServerError -> reason)
        }
      }
    }
  }

  def getPlayer(id: Int): Route = {
    // (path("get" / id) & get) {
      val probablePlayer = playerActor.ask(GetPlayer(id, _))

      onSuccess(probablePlayer) {
        case StatusReply.Success(GetPlayerResponse(maybePlayer)) => complete(StatusCodes.OK -> maybePlayer)
        case StatusReply.Error(reason)                           => complete(StatusCodes.NotFound -> reason)
      }
    // }
  }

  def getAllPlayers: Route = {
    // (path("getAll") & get) {
      val probablePlayers = playerActor.ask(GetAllPlayers)

      onSuccess(probablePlayers) {
        case StatusReply.Success(GetPlayersResponse(players)) => complete(StatusCodes.OK -> players)
        case StatusReply.Error(reason)                        => complete(StatusCodes.NotFound -> reason)
      }
    // }
  }

  def updatePlayer(id: Int): Route = {
    // (path("update" / id) & patch) {
      entity(as[PatchPlayer]) { patchPlayer =>
        val toPatch = playerActor.ask(UpdatePlayer(id, patchPlayer, _))

        onSuccess(toPatch) {
          case StatusReply.Success(UpdatePlayerResponse(patched)) => complete(StatusCodes.OK -> patched)
          case StatusReply.Error(reason)                          => complete(StatusCodes.InternalServerError -> reason)
        }
      }
    // }
  }

  def deletePlayer(id: Int): Route = {
    // (path("delete" / id) & delete) {
      val toDelete = playerActor.ask(DeletePlayer(id, _))

      onSuccess(toDelete) {
        case StatusReply.Success(DeletePlayerResponse(_)) =>
          complete(StatusCodes.OK -> s"Successfully deleted player with id: $id")
        case StatusReply.Error(reason)                    => complete(StatusCodes.InternalServerError -> reason)
      }
    // }
  }

  val routes: Route = pathPrefix("api" / "players") {
    concat(
      createPlayer,
      getAllPlayers,
      get {
        pathPrefix(IntNumber) { id => getPlayer(id) }
      },
      patch {
        pathPrefix(IntNumber) { id => updatePlayer(id) }
      },
      delete {
        pathPrefix(IntNumber) { id => deletePlayer(id) }
      }
    )
  }
}
