package com.startapp.etl.detectlang


import com.google.common.base.Optional
import com.optimaize.langdetect.{LanguageDetector, LanguageDetectorBuilder}
import com.optimaize.langdetect.ngram.NgramExtractors
import com.optimaize.langdetect.profiles.{LanguageProfile, LanguageProfileReader}
import com.optimaize.langdetect.text.{CommonTextObjectFactories, TextObjectFactory}

/**
  * Created by Sidney on 23/02/2017.
  */
object LanguageDetector {
  //load all languages:
  val languageProfiles: java.util.List[LanguageProfile] = new LanguageProfileReader().readAllBuiltIn()

  //build language detector:
  val languageDetector: LanguageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
    .withProfiles(languageProfiles)
    .build()

  //create a text object factory
  val longTextObjectFactory: TextObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText()
  val shortTextObjectFactory: TextObjectFactory = CommonTextObjectFactories.forDetectingShortCleanText()

  implicit def optionalToOption[T](optional: Optional[T]): Option[T] = {
    optional.isPresent match {
      case true => Some(optional.get)
      case _ => None
    }
  }

  def detectLongText(inputText: String): Option[String] = {
    val textObject = longTextObjectFactory.forText(inputText)
    languageDetector.detect(textObject).map(_.toString)
  }

  def detectShortText(inputText: String): Option[String] = {
    val textObject = shortTextObjectFactory.forText(inputText)
    languageDetector.detect(textObject).map(_.toString)
  }

  def detectAnyLength(inputText: String) = {
    inputText.length > 15 match {
      case true => detectLongText(inputText)
      case _ => detectShortText(inputText)
    }
  }

}