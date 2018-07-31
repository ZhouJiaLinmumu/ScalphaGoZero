package org.deeplearning4j.scalphagozero.board

import scala.collection.mutable

/**
  * Main Go board class, represents the board on which Go moves can be played.
  *
  * @author Max Pumperla
  */
class GoBoard(val row: Int, val col: Int) {

  private var grid: mutable.Map[Point, GoString] = mutable.Map.empty
  private var hash = 0 // TODO

  if (!GoBoard.neighborTables.keySet.contains((row, col)))
    GoBoard.initNeighborTable(row, col)
  if (!GoBoard.cornerTables.keySet.contains((row, col)))
    GoBoard.initCornerTable(row, col)

  private var neighborMap = GoBoard.neighborTables.getOrElse((row, col), mutable.Map.empty)
  private var cornerMap = GoBoard.cornerTables.getOrElse((row, col), mutable.Map.empty)

  def neighbors(point: Point): List[Point] = neighborMap.getOrElse(point, List())

  def corners(point: Point): List[Point] = cornerMap.getOrElse(point, List())

  def placeStone(player: Player, point: Point): Unit = {}

  private def replaceString(newString: GoString): Unit =
    for (point <- newString.stones)
      grid += (point -> newString)

  private def removeString(goString: GoString): Unit =
    for (point <- goString.stones) {
      //TODO
    }

  def isSelfCapture(player: Player, point: Point): Boolean = {
    val friendlyStrings: List[GoString] = List.empty
    // TODO
    false
  }

  def willCapture(player: Player, point: Point): Boolean =
    // TODO
    false

  def isOnGrid(point: Point): Boolean = 1 <= point.row && point.row <= row && 1 <= point.col && point.col <= col

  def getColor(point: Point): Option[Int] = grid.get(point).map(_.color)

  def getGoString(point: Point): Option[GoString] = grid.get(point)

  def equals(other: GoBoard): Boolean =
    this.row == other.row && this.col == other.col && this.grid.equals(other.grid)

  def zobristHash: Int = hash

  private def setBoardProperties(grid: mutable.Map[Point, GoString],
                                 hash: Int,
                                 neighborMap: mutable.Map[Point, List[Point]],
                                 cornerMap: mutable.Map[Point, List[Point]]): Unit = {
    this.hash = hash
    this.grid = grid
    this.neighborMap = neighborMap
    this.cornerMap = cornerMap
  }

  override def clone(): GoBoard = {
    val newBoard = new GoBoard(this.row, this.col)
    newBoard.setBoardProperties(this.grid, this.hash, this.neighborMap, this.cornerMap)
    newBoard
  }

}

object GoBoard {

  var neighborTables: mutable.Map[(Int, Int), mutable.Map[Point, List[Point]]] = mutable.Map.empty
  var cornerTables: mutable.Map[(Int, Int), mutable.Map[Point, List[Point]]] = mutable.Map.empty

  def initNeighborTable(row: Int, col: Int): Unit = {
    val neighborMap: mutable.Map[Point, List[Point]] = mutable.Map.empty
    for (r <- 1 to row) {
      for (c <- 1 to col) {
        val point = Point(row, col)
        val allNeighbors = point.neighbors
        val trueNeighbors =
          for (nb <- allNeighbors if 1 <= nb.row && nb.row <= row && 1 <= nb.col && nb.col <= col) yield nb
        neighborMap += (point -> trueNeighbors)
      }
    }
    neighborTables += ((row, col) -> neighborMap)
  }

  def initCornerTable(row: Int, col: Int): Unit = {
    val cornerMap: mutable.Map[Point, List[Point]] = mutable.Map.empty
    for (r <- 1 to row) {
      for (c <- 1 to col) {
        val point = Point(row, col)
        val allCorners = List(
          Point(row - 1, col - 1),
          Point(row + 1, col + 1),
          Point(row - 1, col + 1),
          Point(row + 1, col - 1)
        )
        val trueCorners =
          for (nb <- allCorners if 1 <= nb.row && nb.row <= row && 1 <= nb.col && nb.col <= col) yield nb
        cornerMap += (point -> trueCorners)
      }
    }
    cornerTables += ((row, col) -> cornerMap)
  }

}