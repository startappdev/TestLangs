package com.startapp.messaging

import java.util.Date

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorRef, Props}
import akka.routing.{DefaultResizer, OptimalSizeExploringResizer, Resizer, SmallestMailboxPool}

import scala.collection.mutable
import scala.concurrent.duration._

/**
  * Created by Sidney on 01/12/2016.
  */
class ActorsManager(val initialInstances: Int, val initialFillAmount: Int, val intervalInMinutes: Int) extends Actor {

  implicit val executionContext = scala.concurrent.ExecutionContext.Implicits.global
  var currentCPUActor: ActorRef = _
  var currentCounterActor: ActorRef = _
  var ts = System.currentTimeMillis()

  var sodaMsgArray: SodaMsgArray = _
  var actorName: String = _

  for (fillAmount <- 1 to 21 by 5) {
    actorName = s"cpuActor-$fillAmount"
    println(s"Creating actor $actorName")

    val resizer: Resizer = DefaultResizer(1, 10, backoffThreshold = 0.3, backoffRate = 0.1, messagesPerResize = 500)

    currentCPUActor = context.actorOf(
      SmallestMailboxPool(1, resizer = Some(resizer)).props(Props(classOf[CPUActorRoute], fillAmount, 1)), name = s"cpuActor-$fillAmount"
    )
    currentCounterActor = context.actorOf(Props(classOf[CounterActor], fillAmount, 1), name=s"counter-$fillAmount")
    context.system.scheduler.schedule(5 seconds, 2 seconds, currentCounterActor, PrintInfo)
    val sodaMsg = SodaMsg("Hola", 3)
    //counterActor ! Reset(instances, fillAmount)

    ts = System.currentTimeMillis
    val lst: List[SodaMsg] = List.fill(fillAmount)(sodaMsg)
    while (System.currentTimeMillis - ts < 1000 * 60 * intervalInMinutes) {
      // Run for an hour
      lst.foreach(sodaMsg => currentCPUActor ! (sodaMsg, 1, fillAmount))
      Thread.sleep(10)
    }
    println(s"Killing actor $actorName")
    context.stop(currentCPUActor) // Kill CPU actor
    context.stop(currentCounterActor)
    Thread.sleep(1000 * 3)
  }

  def receive = {
    case x => print("Received")
  }


}
