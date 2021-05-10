object intsets {
  var t1 = new NonEmpty(3, new Empty, new Empty)
  var t2 = t1 incl 4
  var t3 = t2 incl 5 incl 6 incl 2 incl 12 incl -122 incl 123
}


// is SUPERCLASS of Empty and NonEmpty classes
// abstract classs means the functions are not defined and yo ushould define
// them later in classes that will inherite this abstract class
abstract class IntSet {
  def incl(x: Int) : IntSet
  def contains(x: Int) : Boolean
}

// is SUBCLASS of IntSet
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

// is SUBCLASS of IntSet
// object means you are creating the Singelton object
/*
object Empty extends IntSet {
  def contains(x: Int): Boolean = false
  def incl(x: Int): IntSet = new NonEmpty(x, new Empty, new Empty)
  override def toString: String = "."
}
*/