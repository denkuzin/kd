// Final example: create T9 converter (digits to words)

import scala.io.Source

object Converter {

  val in = Source.fromFile("/home/dkuzin/Dropbox/kd/scala/snippets/" +
                           "src/main/scala/notes/linuxwords.txt")

  // load dictionary of possible words and filter words that contains non letter characters
  val words = in.getLines.toList filter (word => word forall (chr => chr.isLetter))

  val mnem = Map( '2' -> "ABC", '3' -> "DEF", '4' -> "GHI", '5' -> "JKL",
    '6' -> "MNO", '7' -> "PQRS", '8' -> "TUV", '9' -> "WXYZ")

  // create char to digit mapping (inverse `mnem` variable)
  val charCode: Map[Char, Char] =
    for ((digit, str) <- mnem; ltr <- str) yield ltr -> digit

  // word to digits converter
  def wordCode(word: String): String =
    word.toUpperCase map charCode

  wordCode("AAAaaa")
  wordCode("Scala")

  // digits to words
  val wordsForNum: Map[String, Seq[String]] =
    words groupBy wordCode withDefaultValue Seq()  //empty Sequence

  // numbers to Set of possible words Lists (sort of advance things)
  def encode(number: String): Set[List[String]] =
    if (number.isEmpty) Set(List())
    else {
      for {
        split <- 1 to number.length
        word <- wordsForNum(number take split)  // if empty go to the next iteration
        rest <- encode(number drop split)
      } yield word :: rest }.toSet

  encode("7225247")

  // just every List to String
  def translate (number: String): Set[String] =
    encode(number) map (_ mkString " ")

  translate("7225247")
}