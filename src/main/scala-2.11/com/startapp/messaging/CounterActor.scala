package com.startapp.messaging

import akka.actor.Actor

import scala.collection.mutable

/**
  * Created by Sidney on 01/12/2016.
  */
class CounterActor(var fillAmount: Int, var instancesAmount: Int) extends Actor {
  // Counter counts only per instance of fillAmount and instancesAmount
  var counter = 0
  var ts = System.currentTimeMillis

  def receive = {

    case SuccessMessageProcess(nInstanceAmount, nFillAmount) => {
      counter += 1
    }
    case PrintInfo => {
      val timeElapsed = System.currentTimeMillis() - ts
        println(s"$fillAmount:$instancesAmount:$timeElapsed:$counter")
    }

    case Reset(nInstancesAmount, nFillAmount) => {
      counter = 0
      ts = System.currentTimeMillis
      fillAmount = nFillAmount
      instancesAmount = nInstancesAmount
    }

    case x => println(s"Unknown - $x")
  }
}
