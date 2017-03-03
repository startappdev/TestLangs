package com.startapp.etl.detectlang;

import com.mzsanford.cld.*;

import java.util.*;

/**
 *
 * Created by Sidney on 03/03/2017.
 *
 */
public class JavaCLD {

    private static CompactLanguageDetector compactLanguageDetector = new CompactLanguageDetector();

    public static Optional<String> detect(String text) {
        System.out.println(text);
        if (text == null) return Optional.empty();
        LanguageDetectionResult result = compactLanguageDetector.detect(text);
        System.out.println(result);
        System.out.println("-----------------------------");
        if (result.isReliable()) return Optional.of(result.getProbableLocale().toLanguageTag());
        else {
            List<LanguageDetectionCandidate> candidates = result.getCandidates();
            Collections.sort(candidates, Comparator.comparingDouble(x -> x.getScore()));
            return Optional.of(candidates.get(candidates.size() - 1).getLocale().toLanguageTag());
        }
    }
}
