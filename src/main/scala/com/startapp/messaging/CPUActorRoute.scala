package com.startapp.messaging

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorRef, Props}
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by Sidney on 01/12/2016.
  */
class CPUActorRoute(val fillAmount: Int, val instancesAmount: Int) extends Actor {
  var processedMessages: Int = 0
  implicit val timeout = new FiniteDuration(5, TimeUnit.SECONDS)

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

      //val actorRef = Await.result(context.system.actorSelection("/manager/counterActor").resolveOne(timeout), timeout)
      context.system.actorSelection(s"/user/manager/counter-$fillAmount")! SuccessMessageProcess(instancesAmount, fillAmount)
    }


  case x => println(s"Unknown - $x")
  }
}
