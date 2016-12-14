package com.startapp.messaging

import akka.actor.{Actor, ActorRef, Props}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by Sidney on 01/12/2016.
  */
class CPUActorRoute(val fillAmount: Int, val instancesAmount: Int) extends Actor {

  implicit val ec =
  var processedMessages: Int = 0

  def receive = {

    case (sodaMsg: SodaMsg, instancesAmount: Int, fillAmount: Int) => {
      val ts = System.currentTimeMillis()
      val sb = new StringBuilder()
      for (j <- 1 to 60) {
        for (i <- 1 to sodaMsg.num) {
          sb.append(sodaMsg.st * i + "   ")
          sb.toArray.toList.mkString("|").toArray
        }
      }
      processedMessages += 1
      if (processedMessages % 500 == 0) println(s"Processing 1 message took ${System.currentTimeMillis() - ts} ms")

      context.system.actorSelection("/manager/counter") ! SuccessMessageProcess(instancesAmount, fillAmount)
    }


  case x => println(s"Unknown - $x")
  }
}
