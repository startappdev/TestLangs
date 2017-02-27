package com.startapp.messaging

import akka.actor.Actor

/**
  * Created by Sidney on 01/12/2016.
  */
class SleepActorRoute extends Actor {
  def receive = {
    case x: SodaMsgArray => {
      val ts = System.currentTimeMillis()
      for (sodaMsg <- x.sodaArray)
        Thread.sleep(5000)
      println(s"Done Processing array of ${x.sodaArray.length} elements in ${System.currentTimeMillis() - ts} milliseconds")
    }
  }
}
