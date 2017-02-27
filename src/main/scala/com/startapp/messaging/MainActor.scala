package com.startapp.messaging

import java.util.concurrent.TimeUnit

import akka.actor.{ActorRef, ActorSystem, PoisonPill, Props}
import akka.routing.SmallestMailboxPool

import scala.collection.mutable
import scala.concurrent.duration._

/**
  * Created by Sidney on 01/12/2016.
  */

object MainActor extends App {

    val initialInstanceAmount: Int = 1
    val fillAmount = Integer.parseInt(args(0))
    val intervalInMinutes = Integer.parseInt(args(1))

    val host = "0.0.0.0"
    val port = 9002

  val ts = System.currentTimeMillis()

    implicit val system = ActorSystem("actorMain")
    implicit val executionContext = scala.concurrent.ExecutionContext.Implicits.global

    var managerActor: ActorRef = system.actorOf(Props(classOf[ActorsManager], initialInstanceAmount, fillAmount, intervalInMinutes), name = "manager")

    managerActor ! "start"

}