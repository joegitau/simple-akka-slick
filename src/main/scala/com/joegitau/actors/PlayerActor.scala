package com.joegitau.actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.pattern.StatusReply
import akka.pattern.StatusReply.ErrorMessage
import com.joegitau.protocol.PlayerProtocol.PlayerCommand
import com.joegitau.protocol.PlayerProtocol.PlayerResponse._
import com.joegitau.services.PlayerService

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

object PlayerActor {
  def apply(playerService: PlayerService): Behavior[PlayerCommand] = Behaviors.receive { (ctx, msg) =>
    ctx.log.info("Player actor initialized...")

    implicit val ec: ExecutionContextExecutor = ctx.system.executionContext

    msg match {
      case PlayerCommand.CreatePlayer(player, replyTo)     =>
        playerService.createPlayer(player).onComplete {
          case Success(createdPlayer) =>
            replyTo ! StatusReply.success(CreatePlayerResponse(createdPlayer))
          case Failure(ex)            =>
            replyTo ! StatusReply.error(ErrorMessage(s"Failed to create player: ${ex.getMessage}"))
        }
        Behaviors.same

      case PlayerCommand.GetPlayer(id, replyTo)            =>
        playerService.getPlayerById(id).onComplete {
          case Success(mayePlayer) => replyTo ! StatusReply.success(GetPlayerResponse(mayePlayer))
          case Failure(ex)         => StatusReply.error(ErrorMessage(s"Failed to query player: ${ex.getMessage}"))
        }
        Behaviors.same

      case PlayerCommand.GetAllPlayers(replyTo)            =>
        playerService.getAllPlayers.onComplete {
          case Success(players) => replyTo ! StatusReply.success(GetPlayersResponse(players.toList))
          case Failure(ex)      => replyTo ! StatusReply.error(ErrorMessage(s"Failed to query players: ${ex.getMessage}"))
        }
        Behaviors.same

      case PlayerCommand.UpdatePlayer(id, player, replyTo) =>
        playerService.updatePlayer(id, player).onComplete {
          case Success(updatedPlayer) => replyTo ! StatusReply.success(UpdatePlayerResponse(updatedPlayer))
          case Failure(ex)            => StatusReply.error(ErrorMessage(s"Failed to update player: ${ex.getMessage}"))
        }
        Behaviors.same

      case PlayerCommand.DeletePlayer(id, replyTo)         =>
        playerService.deletePlayer(id).onComplete {
          case Success(_)  => replyTo ! StatusReply.success(DeletePlayerResponse(id))
          case Failure(ex) => StatusReply.error(ErrorMessage(s"Failed to delete player: ${ex.getMessage}"))
        }
        Behaviors.same
    }
  }
}
