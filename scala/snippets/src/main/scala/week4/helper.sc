//package patmat
import common._

// DO NOT FORGET TO COPY THIS SNIPPET TO Huffman.scala module

object Huffman {

  val a: Int = 7
  var b: Int = 10

  abstract class CodeTree

  case class Fork(left: CodeTree, right: CodeTree, chars: List[Char], weight: Int) extends CodeTree

  case class Leaf(char: Char, weight: Int) extends CodeTree {
    override def toString: String = char + "-" + weight
  }

  // Part 1: Basics
  def weight(tree: CodeTree): Int = tree match {
    case Fork(_, _, _, w) => w
    case Leaf(_, w) => w
  }

  def chars(tree: CodeTree): List[Char] = tree match {
    case Fork(_, _, c, _) => c
    case Leaf(c, _) => List(c)
  }

  def makeCodeTree(left: CodeTree, right: CodeTree):CodeTree =
    Fork(left, right, chars(left) ::: chars(right), weight(left) + weight(right))


  // Part 2: Generating Huffman trees

  //just convert string to List of characters
  def string2Chars(str: String): List[Char] = str.toList

  /** This function computes for each unique character in the list `chars` the number of
    * times it occurs. For example, the invocation
    * example: times(List('a', 'b', 'a')) = List(('a', 2), ('b', 1))
    */
  def times(chars: List[Char]): List[(Char, Int)] = {
    var charsSorted = chars.sorted
    def helper(tail: List[Char], prev: Char, counter: Int): List[(Char, Int)] = tail match {
      case Nil => List((prev, counter))
      case x :: xs => {
        if (x == prev) helper(xs, prev, counter + 1)
        else (prev, counter) :: helper(xs,x, 1)
      }
    }
    helper(charsSorted.tail, charsSorted.head, 1)
  }

  times(string2Chars("abcccdd"))

  /**
    * Returns a list of `Leaf` nodes for a given frequency table `freqs`.
    *
    * The returned list should be ordered by ascending weights (i.e. the
    * head of the list should have the smallest weight), where the weight
    * of a leaf is the frequency of the character.
    */


  def makeOrderedLeafList(tuples: List[(Char, Int)]): List[Leaf] =
    freqs.sortWith((a, b) => a._2 < b._2).map(item => Leaf (item._1, item._2))

//  def makeOrderedLeafList(freqs: List[(Char, Int)]): List[Leaf] = freqs match {
//    case Nil => Nil
//    case x :: xs => Leaf(x._1, x._2) :: makeOrderedLeafList(xs)
//  }

  var freqs = times(string2Chars("abcccdd"))
  var leafList = makeOrderedLeafList(freqs)

  /**
    * Checks whether the list `trees` contains only one single code tree.
    */
  def singleton(trees: List[CodeTree]): Boolean = trees.size == 1

  /**
    * The parameter `trees` of this function is a list of code trees ordered
    * by ascending weights.
    *
    * This function takes the first two elements of the list `trees` and combines
    * them into a single `Fork` node. This node is then added back into the
    * remaining elements of `trees` at a position such that the ordering by weights
    * is preserved.
    *
    * If `trees` is a list of less than two elements, that list should be returned
    * unchanged.
    */
  def combine(trees: List[CodeTree]): List[CodeTree] = trees match {
    case left :: right :: xs => {
      var res:List[CodeTree] = makeCodeTree(left, right) :: xs
      res.sortWith((tree1, tree2) => weight(tree1) < weight(tree2))
    }
    case _ => trees
  }


  /**
    * This function will be called in the following way:
    *
    *   until(singleton, combine)(trees)
    *
    * where `trees` is of type `List[CodeTree]`, `singleton` and `combine` refer to
    * the two functions defined above.
    *
    * In such an invocation, `until` should call the two functions until the list of
    * code trees contains only one single tree, and then return that singleton list.
    *
    * Hint: before writing the implementation,
    *  - start by defining the parameter types such that the above example invocation
    *    is valid. The parameter types of `until` should match the argument types of
    *    the example invocation. Also define the return type of the `until` function.
    *  - try to find sensible parameter names for `xxx`, `yyy` and `zzz`.
    */

  def until(stopFunc: List[CodeTree]=>Boolean, shrinkFunc: List[CodeTree]=> List[CodeTree])
           (trees: List[CodeTree]): List[CodeTree] = {
    if (stopFunc(trees)) trees
    else until(stopFunc, shrinkFunc)( shrinkFunc(trees) )
  }

  /**
    * This function creates a code tree which is optimal to encode the text `chars`.
    *
    * The parameter `chars` is an arbitrary text. This function extracts the character
    * frequencies from that text and creates a code tree based on them.
    */
  // main function
  def createCodeTree(chars: List[Char]): CodeTree =
    until(singleton, combine)(makeOrderedLeafList(times(chars))).head


  // Part 3: Decoding

  type Bit = Int

  /**
    * This function decodes the bit sequence `bits` using the code tree `tree` and returns
    * the resulting list of characters.
    */
  def decode(tree: CodeTree, bits: List[Bit]): List[Char] = {
    def traverse(remaining: CodeTree, bits: List[Bit]): List[Char] = remaining match {
      case Leaf(c, _) if bits.isEmpty => List(c)
      case Leaf(c, _) => c :: traverse(tree, bits)
      case Fork(left, right, _, _) if bits.head == 0 => traverse(left, bits.tail)
      case Fork(left, right, _, _) => traverse(right, bits.tail)
    }

    traverse(tree, bits)
  }

  /**
    * A Huffman coding tree for the French language.
    * Generated from the data given at
    *   http://fr.wikipedia.org/wiki/Fr%C3%A9quence_d%27apparition_des_lettres_en_fran%C3%A7ais
    */
  val frenchCode: CodeTree = Fork(Fork(Fork(Leaf('s',121895),Fork(Leaf('d',56269),Fork(Fork(Fork(Leaf('x',5928),Leaf('j',8351),List('x','j'),14279),Leaf('f',16351),List('x','j','f'),30630),Fork(Fork(Fork(Fork(Leaf('z',2093),Fork(Leaf('k',745),Leaf('w',1747),List('k','w'),2492),List('z','k','w'),4585),Leaf('y',4725),List('z','k','w','y'),9310),Leaf('h',11298),List('z','k','w','y','h'),20608),Leaf('q',20889),List('z','k','w','y','h','q'),41497),List('x','j','f','z','k','w','y','h','q'),72127),List('d','x','j','f','z','k','w','y','h','q'),128396),List('s','d','x','j','f','z','k','w','y','h','q'),250291),Fork(Fork(Leaf('o',82762),Leaf('l',83668),List('o','l'),166430),Fork(Fork(Leaf('m',45521),Leaf('p',46335),List('m','p'),91856),Leaf('u',96785),List('m','p','u'),188641),List('o','l','m','p','u'),355071),List('s','d','x','j','f','z','k','w','y','h','q','o','l','m','p','u'),605362),Fork(Fork(Fork(Leaf('r',100500),Fork(Leaf('c',50003),Fork(Leaf('v',24975),Fork(Leaf('g',13288),Leaf('b',13822),List('g','b'),27110),List('v','g','b'),52085),List('c','v','g','b'),102088),List('r','c','v','g','b'),202588),Fork(Leaf('n',108812),Leaf('t',111103),List('n','t'),219915),List('r','c','v','g','b','n','t'),422503),Fork(Leaf('e',225947),Fork(Leaf('i',115465),Leaf('a',117110),List('i','a'),232575),List('e','i','a'),458522),List('r','c','v','g','b','n','t','e','i','a'),881025),List('s','d','x','j','f','z','k','w','y','h','q','o','l','m','p','u','r','c','v','g','b','n','t','e','i','a'),1486387)

  /**
    * What does the secret message say? Can you decode it?
    * For the decoding use the `frenchCode' Huffman tree defined above.
    */
  val secret: List[Bit] = List(0,0,1,1,1,0,1,0,1,1,1,0,0,1,1,0,1,0,0,1,1,0,1,0,1,1,0,0,1,1,1,1,
    1,0,1,0,1,1,0,0,0,0,1,0,1,1,1,0,0,1,0,0,1,0,0,0,1,0,0,0,1,0,1)

  /**
    * Write a function that returns the decoded secret
    */
  def decodedSecret: List[Char] = decode(frenchCode, secret)


  // Part 4a: Encoding using Huffman tree

  /**
    * This function encodes `text` using the code tree `tree`
    * into a sequence of bits.
    */
  def encode(tree: CodeTree)(text: List[Char]): List[Bit] = {
    def lookup(tree:  CodeTree)(c: Char): List[Bit] = tree match {
      case Leaf(_, _) => List()
      case Fork(left, right, _, _) if chars(left).contains(c) => 0 :: lookup(left)(c)
      case Fork(left, right, _, _) => 1 :: lookup(right)(c)
    }
    text flatMap lookup(tree)
  }
  // Part 4b: Encoding using code table

  type CodeTable = List[(Char, List[Bit])]
  type Code = (Char, List[Bit])

  /**
    * This function returns the bit sequence that represents the character `char` in
    * the code table `table`.
    */
  def codeBits(table: CodeTable)(char: Char): List[Bit] =
    table.filter( code => code._1 == char ).head._2
  /**
    * Given a code tree, create a code table which contains, for every character in the
    * code tree, the sequence of bits representing that character.
    *
    * Hint: think of a recursive solution: every sub-tree of the code tree `tree` is itself
    * a valid code tree that can be represented as a code table. Using the code tables of the
    * sub-trees, think of how to build the code table for the entire tree.
    */
  def convert(tree: CodeTree): CodeTable = tree match {
    case Leaf(c, w) => List( (c, List()) )
    case Fork(left, right, cs, w) => mergeCodeTables(convert(left), convert(right))
  }
  /**
    * This function takes two code tables and merges them into one. Depending on how you
    * use it in the `convert` method above, this merge method might also do some transformations
    * on the two parameter code tables.
    */
  def mergeCodeTables(a: CodeTable, b: CodeTable): CodeTable = {
    def prepend(b: Bit)(code: Code): Code = (code._1, b :: code._2)

    a.map(prepend(0)) ::: b.map(prepend(1))
  }
  /**
    * This function encodes `text` according to the code tree `tree`.
    *
    * To speed up the encoding process, it first converts the code tree to a code table
    * and then uses it to perform the actual encoding.
    */
  def quickEncode(tree: CodeTree)(text: List[Char]): List[Bit] =
    text flatMap codeBits(convert(tree))
}