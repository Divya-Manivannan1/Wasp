package wasps

enum WaspType (val initialPoints: Int, val pointsLost: Int):
  case Queen extends WaspType(80,20)
  case Worker extends WaspType(60, 30)
  case Drone extends WaspType(40, 40)
