package wasps

import wasps.WaspType.{Drone, Queen, Worker}

case class Wasp(waspType : WaspType,  currentPoints : Int){
  // add condition for dead bug
  def hit : Wasp = {
    println(waspType match
      case Queen => "The Queen was hit"
      case Worker => "A worker was hit"
      case Drone => "A drone was hit")
    Wasp(waspType, currentPoints - waspType.pointsLost)
  }
  def isAlive : Boolean = currentPoints>0
  override def toString: String = {
    waspType match
      case Queen => s"Queen $currentPoints"
      case Worker => s"Worker $currentPoints"
      case Drone => s"Drone $currentPoints"
  }
  def this(waspType: WaspType) = {
    this(waspType, waspType.initialPoints)
  }
}
