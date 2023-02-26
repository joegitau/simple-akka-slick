package com.joegitau.protocol

import akka.actor.typed.ActorRef
import akka.pattern.StatusReply
import com.joegitau.model.{PatchPlayer, Player}
import com.joegitau.serialization.CborSerializable

object PlayerProtocol {
  sealed trait PlayerCommand extends CborSerializable
  object PlayerCommand {
    case class CreatePlayer(player: Player, replyTo: ActorRef[StatusReply[PlayerResponse]]) extends PlayerCommand
    case class GetPlayer(id: Int, replyTo: ActorRef[StatusReply[PlayerResponse]]) extends PlayerCommand
    case class GetAllPlayers(replyTo: ActorRef[StatusReply[PlayerResponse]]) extends PlayerCommand
    case class UpdatePlayer(id: Int, player: PatchPlayer, replyTo: ActorRef[StatusReply[PlayerResponse]]) extends PlayerCommand
    case class DeletePlayer(id: Int, replyTo: ActorRef[StatusReply[PlayerResponse]]) extends PlayerCommand
  }

  sealed trait PlayerResponse extends CborSerializable
  object PlayerResponse {
    case class CreatePlayerResponse(player: Player) extends PlayerResponse
    case class GetPlayerResponse(maybePlayer: Option[Player]) extends PlayerResponse
    case class GetPlayersResponse(players: List[Player]) extends PlayerResponse
    case class UpdatePlayerResponse(player: Option[Player]) extends PlayerResponse
    case class DeletePlayerResponse(id: Int) extends PlayerResponse
  }
}
