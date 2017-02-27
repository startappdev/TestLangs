package com.startapp.messaging

/**
  * Created by Sidney on 01/12/2016.
  */

case class SodaMsg(st: String, num: Int)
case class SodaMsgArray(sodaArray: List[SodaMsg])
case class SuccesMessageSimpleNormal()
case class SuccesMessageSimpleRouter()
case class SuccesMessageSleepNormal()
case class SuccesMessageSleepRouter()
case class SuccessMessageProcess(instanceAmount: Int, fillAmount: Int)
case class PrintInfo(instanceAmount: Int, fillAmount: Int)
case class Reset(instancesAmount: Int, fillAmount: Int)
