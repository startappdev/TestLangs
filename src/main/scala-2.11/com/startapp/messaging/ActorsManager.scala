package com.startapp.messaging

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorRef, Props}
import akka.routing.SmallestMailboxPool

import scala.collection.mutable
import scala.concurrent.duration._

/**
  * Created by Sidney on 01/12/2016.
  */
class ActorsManager(val initialInstances: Int, val initialFillAmount: Int, val intervalInMinutes: Int) extends Actor {

  implicit val executionContext = scala.concurrent.ExecutionContext.Implicits.global
  val actors: mutable.HashMap[Int, ActorRef] = new mutable.HashMap[Int, ActorRef]()

  var currentCPUActor: ActorRef = _
  var currentCounterActor: ActorRef = _
  var ts = System.currentTimeMillis()

  var sodaMsgArray: SodaMsgArray = _
  var actorName: String = _

  for (fillAmount <- 1 to 21 by 5) {
    for (instances <- List(1, 3, 5, 10, 20)) {
      actorName = s"cpuActor-$instances-$fillAmount"
      println(s"Creating actor $actorName")

      currentCounterActor = context.actorOf(SmallestMailboxPool(instances).props(Props(classOf[CounterActor], fillAmount, instances)), name = s"counterActor-$instances-$fillAmount")

      val sodaMsg = SodaMsg("Hola", 3)
      //counterActor ! Reset(instances, fillAmount)

      ts = System.currentTimeMillis
      val lst: List[SodaMsg] = List.fill(fillAmount)(sodaMsg)
      while (System.currentTimeMillis - ts < 1000 * 60 * intervalInMinutes) {
        // Run for an hour
        //sodaMsgArray.sodaArray.foreach(x => currentActor ! (x, instances, fillAmount))
        lst.foreach(sodaMsg => currentCPUActor ! (sodaMsg, instances, fillAmount))
        Thread.sleep(10)
      }
      println(s"Killing actor $actorName")
      context.stop(currentCPUActor) // Kill CPU actor
      Thread.sleep(1000 * 3)
    }
  }

  def receive = {
    case x => print("Received")
  }


}
