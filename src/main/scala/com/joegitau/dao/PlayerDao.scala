package com.joegitau.dao

import com.joegitau.model.{PatchPlayer, Player}
import com.joegitau.slick.CustomPostgresProfile.api._
import com.joegitau.slick.tables.PlayerTable.Players

import java.sql.Timestamp
import scala.concurrent.{ExecutionContext, Future}

trait PlayerDao {
  def createPlayer(player: Player): Future[Player]
  def getPlayerById(id: Int): Future[Option[Player]]
  def getAllPlayers: Future[Seq[Player]]
  def updatePlayer(id: Int, player: PatchPlayer): Future[Option[Player]]
  def deletePlayer(id: Int): Future[Int]
}

class PlayerDaoImpl(db: Database)(implicit ec: ExecutionContext) extends PlayerDao {
  lazy val queryById = (id: Int) => Compiled(Players.filter(_.id === id))

  override def createPlayer(player: Player): Future[Player] = {
    val insertQuery = (
      Players returning Players.map(_.id) into ((player, projId) => player.copy(id = projId))
      ) += player

    db.run(insertQuery)
  }

  override def getPlayerById(id: Int): Future[Option[Player]] =
    db.run(queryById(id).result.headOption)

  override def getAllPlayers: Future[Seq[Player]] =
    db.run(Players.result)

  override def updatePlayer(id: Int, player: PatchPlayer): Future[Option[Player]] = {
    val query = queryById(id)

    val updateAction = query.result.headOption.flatMap {
      case Some(existing) =>
        val updatedPlayer = existing.copy(
          firstName   = player.firstName.getOrElse(existing.firstName),
          lastName    = player.lastName.getOrElse(existing.lastName),
          nationality = player.nationality.getOrElse(existing.nationality),
          team        = player.team.getOrElse(existing.team),
          modified    = Some(new Timestamp(System.currentTimeMillis()))
        )
        query.update(updatedPlayer) andThen query.result.headOption

      case None           => DBIO.successful(None)
    }

    db.run(updateAction.transactionally)
  }

  override def deletePlayer(id: Int): Future[Int] =
    db.run(queryById(id).delete)
}
