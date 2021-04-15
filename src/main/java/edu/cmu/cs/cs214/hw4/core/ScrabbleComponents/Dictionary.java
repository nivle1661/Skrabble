package edu.cmu.cs.cs214.hw4.core.ScrabbleComponents;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a dictionary in Scrabble to check if a word is valid.
 */
public class Dictionary {
  /** The list of available words in scrabble. */
  private List<String> dictionary;

  /** Creates a dictionary of valid words for Scrabble from resource file. */
  public Dictionary() {
    String dicPath = "words.txt";
    ClassLoader classLoader = this.getClass().getClassLoader();
    InputStream in = classLoader.getResourceAsStream(dicPath);
    dictionary = new ArrayList<>();

    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = reader.readLine()) != null) {
        dictionary.add(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Returns whether a string is a valid word in Scrabble.
   * @param toCheck word to check
   * @return whether a string is a valid word in Scrabble
   */
  public boolean isWord(final String toCheck) {
    return dictionary.contains(toCheck);
  }
}
