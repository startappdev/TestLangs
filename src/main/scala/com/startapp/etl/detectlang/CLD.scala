package com.startapp.etl.detectlang

import com.mzsanford.cld._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

/**
  * Created by Sidney on 27/02/2017.
  */
object CLD {

  val compactLanguageDetector = new CompactLanguageDetector()

  def detect(text: String): Option[String] = {
    if (text == null) None
    else {
      val result = compactLanguageDetector.detect(text)
      if (result.isReliable()) {
        // getProbableLocale returns a java.util.Locale
        Some(result.getProbableLocale.toLanguageTag)
      } else {
        result.getCandidates.asScala.sortBy(_.getScore).reverse.head.getLocale.toLanguageTag
      }
    }
  }

}
