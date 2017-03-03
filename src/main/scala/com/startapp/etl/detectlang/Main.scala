package com.startapp.etl.detectlang

import java.io.{BufferedWriter, File, FileWriter}
import java.util.Optional

import scala.collection.mutable.ListBuffer
import scala.io.Source

/**
  * Created by Sidney on 23/02/2017.
  */
object Main extends App {
  case class DetectionInput(id: Int, domain: String, text: String)
  case class TestLib(testName: String, func: String => Option[String], lang: String = "scala")

  def splitExt(filePath: String): (String, String) = {
    val parts = filePath.split("\\.")
    (parts.dropRight(1).mkString("."), parts.last)
  }

  def buildOuputPath(filePath: String): String = {
    val parts = splitExt(filePath)
    s"${parts._1}_scala_output.${parts._2}"
  }

  def getInputIterator(pathToFile: String, skipHeader: Boolean = false): List[DetectionInput] = {
    val lines = Source.fromFile(pathToFile, enc = "UTF-8").getLines().toList
    for (line <- if (skipHeader) lines.tail else lines) yield {
      val splitLine = line.split(",").map(_.trim.stripPrefix("\"").stripSuffix("\""))
      DetectionInput(splitLine.head.toInt, splitLine(1), splitLine.drop(2).mkString(","))
    }
  }

  def testLangFunc(linesToTest: List[DetectionInput], outputWriter: BufferedWriter, testLib: TestLib) = {
    var startTime: Long = 0L
    var endTimeMs: Double = 0
    var outputLine: Seq[Any] = Seq.empty[Any]
    linesToTest.foreach { inputLine =>
      startTime = System.nanoTime()
      val lang = testLib.func(inputLine.text)
      endTimeMs = (System.nanoTime() - startTime).toDouble / 1000000
      outputLine = Seq(testLib.lang, lang.getOrElse(""), f"$endTimeMs%.10f", testLib.testName, inputLine.id, inputLine.domain, inputLine.text.replaceAll("[,\"]", "|"))
      outputWriter.write(s"${outputLine.mkString(",")}\n")
    }
  }

  def mainStressTestLanguageDetection(args: Array[String]): Unit = {
    val pathToCsv = scala.io.StdIn.readLine("Enter path to csv with texts: ")
    val testNames = args.map(_.toLowerCase)



    val inputs = getInputIterator(pathToCsv, skipHeader = true)

    val outputPath = buildOuputPath(pathToCsv)

    // FileWriter
    val file = new File(outputPath)
    val bw = new BufferedWriter(new FileWriter(file))

    implicit def javaOptToScala[T](opt: Optional[T]): Option[T] ={
      if (opt.isPresent) Some(opt.get())
      else None
    }

    val tests: Seq[TestLib] = Seq(
      TestLib("JavaCLD", JavaCLD.detect, lang = "java"),
      TestLib("ScalaCLD", CLD.detect),
      TestLib("LangDetection", LangDetection.detect),
      TestLib("LanguageDetector", LanguageDetector.detectAnyLength)
    )
    
    tests.foreach(test => testLangFunc(inputs, bw, test))
    bw.close()
  }

  def mainBorisTest(start: Int, arr: Array[Int], target: Int): Boolean = {
    /**
      * Given an array of ints, is it possible to choose a group of some of the ints, such that the group sums to the given target with this additional constraint: If a value in the array is chosen to be in the group, the value immediately following it in the array must not be chosen. (No loops needed.)
      *
      * groupNoAdj(0, [2, 5, 10, 4], 12) → true
      * groupNoAdj(0, [2, 5, 10, 4], 14) → false
      * groupNoAdj(0, [2, 5, 10, 4], 7) → false
      *
      */

    def createValueFollowers(arr: Array[Int]): collection.mutable.Map[Int, ListBuffer[Int]] = {
      val followingValues = collection.mutable.Map.empty[Int, ListBuffer[Int]]
      for ((v, i) <- arr.zipWithIndex.dropRight(1)) {
        if (followingValues.contains(v)) followingValues(v) += arr(i + 1)
        else followingValues.put(v, ListBuffer[Int](arr(i + 1)))
      }
      followingValues
    }

    def createDiffMap(arr: Array[Int], followingValues: collection.mutable.Map[Int, collection.mutable.ListBuffer[Int]], target: Int): Map[Int, Int] = {
      val localFollowingValues: collection.mutable.Map[Int, collection.mutable.ListBuffer[Int]] = followingValues.map(pair => pair._1 -> pair._2.map(x=>x))
      val pairs = for (i <- arr) yield (i, target - i)
      pairs.filter { pair =>
        if (localFollowingValues(pair._1).contains(pair._2)) {
          localFollowingValues(pair._1) -= pair._2
          false
        } else true
      }.toMap
    }

    def groupNoAdj(arr: Array[Int], followersMap: collection.mutable.Map[Int, ListBuffer[Int]], target: Int, sum: Int=0): Boolean = {
      if (sum == target) true
      else {
        false
        /*arr, arr.size match {
          case ()
        }*/
      }
    }

    val dropped = arr.drop(start)
    val followersMap = createValueFollowers(dropped)

    // Send sorted array
    groupNoAdj(dropped.filter(_ <= target).sorted, followersMap, target)
  }



  override def main(args: Array[String]): Unit = {
    mainStressTestLanguageDetection(args)
  }

}
