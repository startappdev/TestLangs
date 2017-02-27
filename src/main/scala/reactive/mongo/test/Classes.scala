package reactive.mongo.test

//import reactivemongo.bson._

/**
  * Created by Sidney on 21/12/2016.
  */

case class Person(name: String, age: Int)
case class Parent(name: String, age: Int, children: List[Person])
