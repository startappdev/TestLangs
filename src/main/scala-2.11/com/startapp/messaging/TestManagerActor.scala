package com.startapp.messaging

import akka.actor.{Actor, Props}
import akka.routing.{RoundRobinPool, SmallestMailboxPool}

/**
  * Created by Sidney on 01/12/2016.
  */
class TestManagerActor (val instancesAmount: Int) extends Actor {

  val cpuActor = context.actorOf(SmallestMailboxPool(instancesAmount).props(Props[CPUActorRoute]), name = s"cpuRoute-$instancesAmount")
  val ts = System.currentTimeMillis()

  def receive = {
    case x: SodaMsgArray => cpuActor ! (x, instancesAmount)
    case x => println("Unknown " + x)
  }
}
