package week1


object DenisKuzin extends App {
  println(Lists.max(List(1,3,2,43,43,43,4)))
  println(Lists.sum(List(1,3,2,43,43,43,4)))
}



// scala find recurse find maximum example using tail recurse
def getMax(l: List[Int]): Int = {
  def loop(list:List[Int], acc:Int): Int = {
    if (list.isEmpty) acc
    else if (list.head > acc) loop(list.tail, list.head)
    else loop(list.tail, acc)
  }

  if (l.isEmpty) throw new NoSuchElementException
  loop(l, l.head)
}

val l = List[Int](-1,-3,-5,1,2,3,4,5,11,100)
getMax(l)

// scala find sum of elements example using tail recurse
def getSum(l:List[Int]): Int = {
  def loop(list:List[Int], acc:Int): Int = {
    if (list.isEmpty) acc
    else loop(list.tail, list.head + acc)
  }

  if (l.isEmpty) throw new NoSuchElementException
  loop(l, l.head)
}

getSum(l)




object Lists {

  /**
   * This method computes the sum of all elements in the list xs. There are
   * multiple techniques that can be used for implementing this method, and
   * you will learn during the class.
   *
   * For this example assignment you can use the following methods in class
   * `List`:
   *
   *  - `xs.isEmpty: Boolean` returns `true` if the list `xs` is empty
   *  - `xs.head: Int` returns the head element of the list `xs`. If the list
   *    is empty an exception is thrown
   *  - `xs.tail: List[Int]` returns the tail of the list `xs`, i.e. the the
   *    list `xs` without its `head` element
   *
   *  ''Hint:'' instead of writing a `for` or `while` loop, think of a recursive
   *  solution.
   *
   * @param xs A list of natural numbers
   * @return The sum of all elements in `xs`
   */
    def sum(xs: List[Int]): Int = {
      var currentSum: Int = 0
      for (item <- xs) {
        currentSum += item
      }
      currentSum
    }
  
  /**
   * This method returns the largest element in a list of integers. If the
   * list `xs` is empty it throws a `java.util.NoSuchElementException`.
   *
   * You can use the same methods of the class `List` as mentioned above.
   *
   * ''Hint:'' Again, think of a recursive solution instead of using looping
   * constructs. You might need to define an auxiliary method.
   *
   * @param xs A list of natural numbers
   * @return The largest element in `xs`
   * @throws java.util.NoSuchElementException if `xs` is an empty list
   */
    def max(xs: List[Int]): Int = {
      if (xs.isEmpty) { throw new java.util.NoSuchElementException("empty list")}
      var currentMax: Int = xs(0)
      for (item <- xs) { if (item > currentMax) currentMax = item }
      currentMax
    }
  }
