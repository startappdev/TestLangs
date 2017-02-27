package com.startapp.etl.detectlang

import java.io.File

import com.cybozu.labs.langdetect.{Detector, DetectorFactory, LangDetectException, Language}

import collection.JavaConversions._
import collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer


/**
  * Created by Sidney on 23/02/2017.
  */
object LangDetection {

  /**
    * This library requires that a detection text has some length, almost 10-20 words over.
    * It may return a wrong language for very short text with 1-10 words.
    * https://github.com/shuyo/language-detection/blob/wiki/FrequentlyAskedQuestion.md
    */

  private val profilesDir = System.getProperty("profilesDir") match {
    case null =>
      println("profilesDir environment param not found, choosing `./language-profiles` as default")
      getClass.getResource("profiles").getPath
    case x => x
  }

  DetectorFactory.loadProfile(profilesDir)

  private def getFilledDetector(text: String): Detector = {
    val detector = DetectorFactory.create
    detector.append(text)
    detector
  }

  def detect(text: String): Option[String] = {
    try {
      getFilledDetector(text).detect() match {
        case "unknown" => None
        case x => Some(x)
      }
    } catch {
      case _: LangDetectException =>
        None
      case x: Throwable => throw x
    }
  }

  def detectLangs(text: String): List[Language] = {
    getFilledDetector(text).getProbabilities.toList
  }

}
