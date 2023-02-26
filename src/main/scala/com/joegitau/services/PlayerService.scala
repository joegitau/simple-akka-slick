package com.joegitau.services

import com.joegitau.dao.PlayerDao
import com.joegitau.model.{PatchPlayer, Player}

import scala.concurrent.{ExecutionContext, Future}

trait PlayerService {
  def createPlayer(player: Player): Future[Player]
  def getPlayerById(id: Int): Future[Option[Player]]
  def getAllPlayers: Future[Seq[Player]]
  def updatePlayer(id: Int, player: PatchPlayer): Future[Option[Player]]
  def deletePlayer(id: Int): Future[Int]
}

class PlayerServiceImpl(playerDao: PlayerDao)(implicit ec: ExecutionContext) extends PlayerService {
  override def createPlayer(player: Player): Future[Player] =
    playerDao.createPlayer(player)

  override def getPlayerById(id: Int): Future[Option[Player]] =
    playerDao.getPlayerById(id)

  override def getAllPlayers: Future[Seq[Player]] =
    playerDao.getAllPlayers

  override def updatePlayer(id: Int, player: PatchPlayer): Future[Option[Player]] =
    playerDao.updatePlayer(id, player)

  override def deletePlayer(id: Int): Future[Int] =
    playerDao.deletePlayer(id)
}
