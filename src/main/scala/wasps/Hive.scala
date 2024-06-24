package wasps

import wasps.WaspType.{Drone, Queen, Worker}

import scala.util.Random

class Hive (val hive: List[Wasp]) {

  def this() = {
    this( Random.shuffle(
      new Wasp(Queen) :: List.fill(2)(new Wasp(Worker)) ::: List.fill(3)(new Wasp(Drone)) ) )
  }

  def isHiveAlive: Boolean = {
    hive.find(wasp => wasp.waspType == Queen).get.isAlive
  }

  def printHive(): Unit = {
    hive.foreach(println)
  }

  def hitRandomWasp : Hive = {
    val random = Random.nextInt(hive.length)
    if(hive(random).isAlive){
      val newHive = hive.updated(random, hive(random).hit)
      Hive(newHive)
    }else
      hitRandomWasp
  }
}
