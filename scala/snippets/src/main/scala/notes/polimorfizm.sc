import java.util.NoSuchElementException

// polimorphism means "in many forms"

// why do we use here strange parameter T?
//     because we would like to create custom class of Linked List data structure
//     for all data types (Int, Float, Double)

object checker {
  // example of a function that flexible to data type
  def singelton[T](elem: T) = new Cons[T](elem, new Nil[T])
  singelton[Int](1)
  singelton[Boolean](true)
  singelton(true) // also works

  def nth[T](n: Int, xs: List[T]): T = {
    if (xs.isEmpty) throw new IndexOutOfBoundsException
    else if (n == 0) xs.head
    else nth(n - 1, xs.tail)
  }

  val list = new Cons(1, new Cons(2, new Cons(3, new Nil)))
  nth(0, list)
  nth(2, list)
  nth(100, list)
}

trait List[T] {
  def isEmpty: Boolean
  def head: T
  def tail: List[T]
}

class Cons[T](val head: T, val tail: List[T]) extends List[T] {
  def isEmpty: Boolean = false
}

class Nil[T] extends List[T] {
  def isEmpty: Boolean = true
  def head: Nothing = throw new NoSuchElementException("Nil.head")
  def tail: Nothing = throw new NoSuchElementException("Nil.tail")
}
