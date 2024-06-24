import Game.Controller

object Playground extends App {
  
  if(Controller.welcome()) {
      Controller.timerRunning = true
      Controller.timer()
      Controller.gameLoop()
  }
  
}
