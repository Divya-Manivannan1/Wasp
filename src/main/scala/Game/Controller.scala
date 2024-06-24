package Game

import wasps.Hive
import scala.concurrent.ExecutionContext.Implicits.global
import scala.annotation.tailrec
import scala.concurrent.Future

object Controller {
  private var gameHive = new Hive()
  private var shotsFired = 0
  private var name : String = ""
  private var time = (0, 0, 0)
  var timerRunning = false

  private def oncePerSecond(callback: () => Unit) : Unit =
    while timerRunning do {
      callback()
      Thread.sleep(1000)
    }

  private def incrementTime():Unit = {
    val (h, m, s) = time
    time = if(s<59)
      (h, m, s + 1)
    else
      if(m<59)
        (h, m+1, 0)
      else
        (h+1, 0, 0)
  }

  def timer(): Unit = Future {
    oncePerSecond(incrementTime)
  }

  def welcome(): Boolean = {
    println("Please enter your name:")
    name = scala.io.StdIn.readLine()
    println("Hello, " + name + " Welcome!")
    askYesOrNo("Would you like to play the wasp game?")
  }

  @tailrec
  def gameLoop(): Unit = {
    println("-".repeat(40))
    gameHive.printHive()
    println("-".repeat(40))
    val command = getCommandInput
    command match
      case "help" =>
        printHelp()
        gameLoop()
      case "fire" =>
        gameHive = gameHive.hitRandomWasp
        shotsFired += 1
        if(gameHive.isHiveAlive)
          gameLoop()
        else{
          timerRunning = false
          val timeString = time match
            case (h, m, s) if h>0 => s"$h hours, $m min and $s secs"
            case (_, m, s) if m>0 => s"$m min and $s secs"
            case (_, _, s) => s"$s secs"
          println("=".repeat(40))
          println (s"Congratulations, $name!! You have won the game!!!! \n Shots fired : $shotsFired \n Time taken : $timeString")
          println("=".repeat(40))
          if(askYesOrNo("Would you like to play the Game again?")) {
            gameReset()
            gameLoop()
          }
        }
      case "restart" =>
        if(askYesOrNo("Would you like to Restart the Game?")) {
          gameReset()
        }
        gameLoop()
      case "quit" =>
        if(askYesOrNo("Would you like to Quit the Game?"))
          timerRunning = false
        else
          gameLoop()
      case _ =>
  }

  private def gameReset():Unit={
    timerRunning = false
    gameHive = new Hive()
    shotsFired = 0
    time = (0, 0, 0)
    timerRunning = true
    timer()
  }

  private def printHelp (): Unit = {
    println("\n"+" ".repeat(10)+"-*".repeat(30)+"-")
    println ("                                 THE QUEEN WASP RULES")
    println(" ".repeat(10)+"-*".repeat(30)+"-\n")
    println("The game involves hitting random wasps from a nest of wasps. " +
      "Each time a command is given to hit a wasp, a random wasp is selected, and its hit points are reduced. " +
      "\nOnce a wasp's hit points reach zero, it dies and cannot be hit again.\nThe state of all the wasps should be displayed after each hit." +
      " The game is over when all wasps have died. The user should be able to start a new game once the game is over.")
    println("\nThe game starts with the following wasps:\n\n" +
      "1 x Queen\n80 Hit Points\nLoses 7 hit points every time it is hit\nAll wasps die if the queen is killed\n\n" +
      "5 x Worker Wasps\nEach one starts with 68 hit points\nEach one loses 10 hit points each time it is hit\n\n" +
      "8 x Drone Wasps\nEach starts with 60 hit points\nEach loses 12 hit points each time it is hit")
    println("\n\nCommands:\nfire: Hits a random wasp.\nrestart: Restarts the game after it has ended.\nquit: Exits the game.\n\n")
  }

  @tailrec
  private def getCommandInput: String = {
    val validCommands = Set("help", "fire", "restart", "quit")
    println("Please enter a valid command")
    val commandInput = scala.io.StdIn.readLine().toLowerCase()
    if (validCommands(commandInput))
      commandInput
    else
      getCommandInput
  }


  @tailrec
  private def askYesOrNo(message: String): Boolean = {
    println(s"$message (Y/N)")
    val yesNo = scala.io.StdIn.readLine().toLowerCase().trim
    if (yesNo.equals("yes") || yesNo.equals("y"))
      true
    else if (yesNo.equals("no") || yesNo.equals("n"))
      false
    else
      println("Please enter a valid response")
      askYesOrNo(message)
  }


}
