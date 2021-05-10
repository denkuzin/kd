// -----------------I. IMPORTING OF PACKAGES-----------------------------
// import progfun.Rational
// import progfun._ //import all classes from the progfun package
import notes.{Rational, Author} // import a few classes from the project
/*
in every programm scala automatically import:
- all members of package Scala
- all members of package java.lang
- all members of singelton object scala.Predef
examples:
Int is scala.Int
String is scala.String
Object is java.lang.Object
require is scala.Predef.require
assert is scala.Predef.assert
full list: https://www.scala-lang.org/api/current/
*/

// object means you are creating the Singelton object
object scratch {
  //IMPORTING EXAMPLE
  new Rational(1, 2)
  new Author("Nelson", "Mandella")

  // ABSTRACT CLASSES
  var t1 = new NonEmpty(3, new Empty, new Empty)
  var t2 = t1 incl 4
  var t3 = t2 incl 5 incl 6 incl 2 incl 12 incl -122 incl 123
}

// ---------------II. TRAITS---------------------------------------
/*
it's like abstract class, but

Classes, objects and traits can inherit from at most one class but arbitrary many traits, ex:
`class Square extends Shape with Planar with Movable` (Planar & Movable are traits)

Difference between abstract classes and traits:
- abstract classes can contains only abstract methods
- traits can contains abstract methods and concrete methods,

- traits cannot have values parameters (unlike classes)
*/
trait Planar {
  def height: Int
  def width: Int
  def surface: Int = height * width
}


// ---------------III. ABSTRACT CLASS EXAMPLE-----------------------
// is SUPERCLASS of Empty and NonEmpty classes
// abstract classs means the functions are not defined and yo ushould define
// them later in classes that will inherite this abstract class
abstract class IntSet {
  def incl(x: Int) : IntSet
  def contains(x: Int) : Boolean
}

// is subclass of IntSet
class Empty extends IntSet {
  def contains(x: Int): Boolean = false
  def incl(x: Int): IntSet = new NonEmpty(x, new Empty, new Empty)
  override def toString: String = "."
}

// is subclass of IntSet
class NonEmpty (elem: Int, left: IntSet, right: IntSet) extends IntSet {
  def contains(x: Int): Boolean = {
    if (x < elem) left contains x
    else if (x > elem) right contains x
    else true
  }
  def incl(x: Int) : IntSet = {
    if (x < elem) new NonEmpty(elem, left incl x, right)
    else if (x > elem) new NonEmpty(elem, left, right incl x)
    else this
  }
  override def toString: String = "{" + left + elem + right + "}"
}






