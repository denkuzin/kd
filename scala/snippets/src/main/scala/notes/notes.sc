//import scala.collection.immutable.List

/* jupyter notebook scala
https://github.com/jupyter-scala/jupyter-scala
*/

/* create template for a new project:
https://www.scala-sbt.org/1.x/docs/sbt-new-and-Templates.html
ex.: `sbt new scala/scala-seed.g8`
*/

/* ADDITIONS */
//
var b0 :Double = 4.4343243    // create Double var:
var c0 :Float = 4.4343243f    // create Float var:
def ??? = throw new Error("Non implemented error")  // helper for the future
//
// blocks in scala:
def function(x:Int): Int = x + 1
val x = 0
val result = {
  val x = function(7)
  x * x}
// important note about blocks: they can change parameters inside, but not outside!
// in the upper example, after execution, x'll be equals 0.
//
// tail recursion:
// https://stackoverflow.com/questions/33923/what-is-tail-recursion
// example
var myList = 1 :: 2 :: 1 :: 4 :: 1 :: 1 :: 7 :: Nil

println(myList)

def count_len(list:List[Int], acc:Int): Int =
  if (list.isEmpty) acc
  else count_len(list.tail, acc+1)
myList.length == count_len(myList, 0)   // test
// or you can use match/case template for better flexibility
def count_len_case(list:List[Int], acc:Int): Int = list match {
  case Nil => acc
  case _ :: _ =>  count_len_case(list.tail, acc+1)
}
myList.length == count_len_case(myList, 0)  // test
//
// the shorter way write functions
// ((x, y) => x * y) is the same as (_ * _)
// ex:
def sum_reduce1(xs: List[Int]) = (0 :: xs) reduceLeft ((a, b) => a + b)
def sum_reduce2(xs: List[Int]) = (0 :: xs) reduceLeft (_ + _)

var nnn = myList.groupBy(identity).take(1)




/* CLASS EXAMPLE */
// class is custom data structure
class Rational (x: Int, y:Int) {

  // pre-defined function, checks passed arguments
  // if False, it throw `IllegalArgumentException`
  require (y != 0, "denominator must be nonzero")

  private def gcd(a:Int, b:Int):Int = if (b == 0) a else gcd(b, a % b)
  def numer = x / gcd(x, y)
  def denom = y / gcd(x, y)

  def less(that: Rational) =
    numer * denom < that.numer * that.denom

  // 'this' object represent the current class (like 'self' in Python)
  def max(that: Rational) :Rational =
    if (this.less(that)) that else this

  override def toString: String = numer + "/" + denom

  def this(x: Int) = this(x, 1) // auxiliary constructor
  // that call previously defined constructor
  // IT WILL BW CALLED IN CASE NUMBER OF ARGUMENTS is
  // differ from number of arguments in the main class
  // usage example: to identify the default values arguments

  def add (that: Rational): Rational =
    new Rational (numer * that.denom + denom * that.numer, denom * that.denom )

  // you can name functions by symbols like +-<> etc.:
  // therefore add can be defined in class Rational as:
  def + (that: Rational): Rational =
    new Rational (numer * that.denom + denom * that.numer, denom * that.denom )

  // also you can use `unary_-` method that can be user as symbol before the class, like:
  // they're called as prefix operators
  def unary_- : Rational = new Rational(-numer, denom)

  def == (that:Rational): Boolean =
    if (numer == that.numer & denom == that.denom) true
    else false
}
var r1 = new Rational(1, 6)
var r2 = new Rational(2, 6)
r1 + r2
r1.add(r2)

// create new class:
class Person (val name: String,
              val age: Int) {
  override def toString: String = name + '-' + age
}

// split people into 2 parts (young and adults):
val people: Array[Person] = Array[Person](
  new Person("Don", 15),
  new Person("Antoni", 25),
  new Person("Vasya", 30))
val (minors1, adults1) = people partition (_.age < 18)
// the same in parallel (!):
val (minors2, adults2) = people.par partition (_.age < 18)


/* EXCEPTIONS */
// `require(a = 7, "message")`  //check input parameters for new class
// there is assert(condition, message) function - it can be
// used in any parts of code and it throw `AssertionError`:
assert(5 > 1, "5 should be more then 1")
//
// the type of the next exception is Nothing (means the programm
// finished incorrectly)
// throw new Error("you should specify 2 parameters")



/* SYNTAX SUGARS */
//
// function calling
// you can call method without dot:
// `x.add(y)` is the same as `x add y`
r1.add(r2)
r1 add r2
//
// functions definition:
def f2(x: Int) : Int = x*x
def f1 : Int => Int = x => x*x
f1(1) == f2(1) // True
//


/* PRECEDENCE OF OPERATORS */
// let's imagine you've created the new class Rational with methods
// + - * /
// fro expression x*x + y*y you have to do it in special order (multiplications are the first)
// in scala precedence of an operator is determined by its first character in the next order:
// (all letters) // Minimum priority
// |
// ^
// &
// <>
// = !
// :
// + -
// * / %
// (all other special characters) // Maximum priority




/* ANONYMOUS FUNCTIONS */

// Anonymous function such as
(x: Int) => x * x
// is expanded to
var a = {
  class myAnonFunc extends Function1[Int, Int] {
  def apply(x: Int) = x * x
  }
  new myAnonFunc
}
// or shorter
new Function[Int, Int] {
  def apply(x:Int) = x * x
}

// Object Oriented translation of
val f = (x:Int) => x * x
// would be
val ff = new Function1[Int, Int] {
  def apply(x: Int) = x * x
}
// testing:
f(7)
f.apply(7)


/* */
// A <: B means A is subtype of B
/*
relating to example with intsets
NonEmpty is subtype of IntSet
Empty is subtype of IntSet
*/


/***** PATTERN MATCHING *****/
trait Expr {
  def eval: Int = this match {
    case Number(n) => n
    case Sum(e1, e2) => e1.eval + e2.eval
  }
}
case class Number(n: Int) extends Expr
case class Sum(e1:Expr, e2:Expr) extends Expr

/*Pattern Matching is generalisation of switch from C/Java to class hierarchies*/
// if there is no case scala throws no pattern matching Exception
//example
def eval(e: Expr): Int = e match {
  case Number(n) => n
  case Sum(e1, e2) => eval(e1) + eval(e2)
}  // it's better to include the `eval` method to `Expr` trait:
//test
var ex1 = Sum(Number(1), Number(2))
eval(ex1)
ex1.eval

// example from 2nd Course of the Specialization
val myF: String => String = {case "ping" => "pong"}
myF("ping")
// myF("other word")  // => ERROR
// to avoid such error we can check is the function is defined at the argument
val myF2: PartialFunction[String, String] = {case "ping" => "pong"}
myF2.isDefinedAt("ping")
myF2.isDefinedAt("other word")


/* LISTS */
/*
* lists are immutable
* Nil element is used at any List (as the last one)
*/

var xs = List(1, 2, 3)
var ys = List(4, 5, 6)

val fruits: List[String] = List("apple", "orange", "pear")
val ints1: List[Int] = List(1,2,3)
val ints2 = 1 :: (2 :: (3 :: Nil)) // syntax sugar, represent tree structure of the list
val ints3 = 1 :: 2 :: 3 :: Nil     // the same
val diag: List[List[Int]] = List(List(1,0,0), List(0,1,0), List(0,0,1))
val empty1: List[Nothing] = List()
val empty2 = Nil // syntax sugar

/* List methods */
//
// Sublists sliding or elemnts access:
xs.head // O(1) throw new NoSuchElementException("head of an Empty list") if List is Empty
xs.tail   // O(1)
xs.last   // O(n), Exception if list is Empty
xs.init   // return all elements except the last one, O(n), Exception if list is Empty
xs take 2  // the first n elements or list itself if length is shorter n
xs drop 100  // the rest of the list after dropping the first n elemetns or Empty list
xs(1)  // by index  O(n)
xs apply 1 // is the same as `ints1(1)`
//
//creating new lists
xs ++ ys            // union of a two lists
List.concat(xs, ys) // union of a two lists
xs ::: ys           // union of a two lists
0 :: xs             // union if element and list, or append to the head, O(1)
// :: prepends a single item, like `item :: List`
// ::: prepends a complete list, like `List ::: List`
xs.reverse
xs.updated(1, 777) // replace element in position index=1 by element 777
xs splitAt 1 // return pair of 2 lists
//
// findings of elements
ys indexOf 6  // index of the first element equals 6
              // (or -1 if there is no such element in the List)
ys contains 6 // the same as `ys indexOf 6 >= 0`
//
// example of a `remove` method implementation
def removeAt[T](xs:List[T], n: Int): List[T] =
  (xs take n) ::: (xs drop n+1)

assert(removeAt(List('a','b','c','d'), 2) == List('a','b','d'),
  "the test of `removeAt` function FAILED")

// other methods
xs.isEmpty
xs.nonEmpty

// high order Lists functions
xs.map(x => x*x*x)
xs.filter(x => x >= 2)
xs.filterNot(x => x < 2)
xs.partition(x => x > 2)  // split list by condition into 2 sub-lists
xs takeWhile(x => x <= 2)  // return the List until the first triggering of the condition
xs dropWhile(x => x <= 2)  // return the List that after the first triggering of the condition
xs span (x => x >= 2)      // split list into `takeWhile` and `dropWhile` Lists
// pack sorted List:
def pack[T](xs:List[T]): List[List[T]] = xs match {
  case Nil => Nil
  case x :: _ =>
    val (first, rest) = xs span (y => y == x)
    first :: pack(rest)
}
pack(List(1, 1, 1, 2, 2, 3, 4, 4))
// encode to element - number of elements
def encode[T](xs: List[T]) : List[(T, Int)] = pack(xs) map (x => (x.head, x.length))
encode(List(1, 1, 1, 2, 2, 3, 4, 4))

def sum_recursive(xs: List[Int]): Int = xs match {
  case Nil => 0
  case y :: ys => y + sum_recursive(ys)
}

def sum_tail_recursion(xs: List[Int]): Int = {
  def helper(l:List[Int], accum: Int): Int =
    if (l.isEmpty) accum else helper(l.tail, accum + l.head)
  helper(xs, 0)
}

// `reduceLeft` is generic method (like `reduce` in Python)
// it's best practices for the such case
def sum(xs:List[Int]) = (0 :: xs) reduceLeft ((a, b)=> a + b)
//or more shorter:
def sum2(xs:List[Int]) = (0 :: xs) reduceLeft (_ + _)
def product(xs:List[Int]): Int = (0 :: xs) reduceLeft ((a, b) => a * b)

// `foldLeft` is more general way for `reduceLeft`
// `reduceLeft` is a wrapper of `foldLeft`
def sum3(xs:List[Int]) = (xs foldLeft 0 ) (_ + _) // here 0 - is init value of the accumulator
def product3(xs:List[Int]) = (xs foldLeft 1 ) (_ * _) // here 1 - is init value of the accum.

/* List patterns
Nil // the Nil constant
List() // the same
p :: ps //the list that matching with a p is head and ps is tail
//Examples:
1 :: 2 :: xs // match to (1,2,any_tail)
x :: Nil // match List a length of 1
List(x) // the same
*/


// MUTABLE ARRAYS / LISTS:
// https://docs.scala-lang.org/overviews/collections/concrete-mutable-collection-classes.html
// scala.collection.mutable.*
var arr = scala.collection.mutable.ArrayBuffer[Int]()
var list = scala.collection.mutable.ArrayBuffer[Double]()
//add element to the mutable list/array:
arr += 7  // append to the end, O(1)
arr ++= List[Int](4, 7, 1, 5) //append pack of elements to the end of List



//convert mutable list/array to immutable (save memory etc):
//var c = a.toList  // a.toArray


val positions = List(300, 20, 10, 300, 30)
val resultsss = positions.sorted


/* MERGE SORT EXAMPLE */
def msort(xs: List[Int]): List[Int] = {
  val n = xs.length / 2
  if (n == 0) xs // xs.length = 0 or 1 case, in this case the xs is already sorted
  else {
    def merge(xs: List[Int], ys: List[Int]) : List[Int] = (xs, ys) match {
      case (Nil, ys) => ys
      case (xs, Nil) => xs
      case (h1 :: t1, h2 :: t2) =>
        if (h1 < h2) h1 :: merge(t1, ys)
        else h2 :: merge(xs, t2)
    }
    val (fst, snd) = xs splitAt n
    merge(msort(fst), msort(snd))
  }
}


// pairs / tripels and so on
val triple = (1,2,3)
// unbreak it
val k1, k2, k3 = triple
// or the same:
val ka = triple._1
val kb = triple._2
val kc = triple._3



/* INSERTION SORT EXAMPLE */
def insert(x: Int, xs: List[Int]): List[Int] = xs match {
  case Nil => List(x)
  case y :: ys => if (x <= y ) x :: xs else y :: insert(x, ys)
}

def insertSort(xs: List[Int]): List[Int] = xs match {
  case Nil => Nil
  case y :: ys => insert(y, insertSort(ys))
}

var testL = 2 :: 1 :: 4 :: 3 :: Nil
insertSort(testL) // mmm... cool language
msort(testL)



/* VECTORS */
/*
vectors are like lists in Python (constant random access by index and so on)
vectors are mutable

Vector implemented as 32 pointers array that links to other 32 pointers arrays
and so on,
the last layer refers to the values

 */
val nums = Vector(1,2,3,4)
// most of operations are the same as for lists
// except `::`, instead of `::` we can use
4 +: nums // add to the left `x +: xs`, return partly(!) copy
          // the previous Vector and the new vector use some of the same objects :-)
nums :+ 5 // add to the right `xs :+ 5`

// initialise Vector
Vector.fill(4)("*")


// Vectors and List and Strings are subclasses of `Seq`
// it means that for strings you can apply the same methods as for Lists/Vector


// Range
var r: Range = 1 until 5
r.toList
val s1: Range = 1 to 5
s1.toList
1 to 10 by 2
6 to 1 by -2

/* other methods that apllicable for all `Seq`*/

val s: String = "Hello World"
s filter (c => c.isUpper) //
s exists (c => c.isUpper) // is there are an element where some condition is True
s forall (c => c.isUpper)  // True if it's True for every element in s
val pairs = "Deni" zip s
val (uno, kvadro) = pairs.unzip
s.take(1).flatMap (c => List('.', c, '.'))



// example 1: generate all possible pairs combinations from (1 to m) and (1 to n)
var m = 2
var n = 3
var solution = (1 to m) flatMap ( x => (1 to n) map (y => (x, y))) take 1
// example 2: scalar product of 2 vectors
xs
ys
def scalarProduct(xs: Vector[Double], ys: Vector[Double]) : Double =
   xs.zip(ys).map(xy => xy._1 * xy._2).sum


/* for loop for `Seqs` */
for {
  item <- xs
  if item > 1} yield item * 2
// is the same as
xs filter (x => x > 1) map (x => x * 2)
// in reality, compile translate for expresiions into combination of `map`,
// `flatMap`, lazy `filter`


// in terms of for loop notations:
// this is bewtter becouse it works with generators(!) without creating temporary big arrays
def scalarProduct2(xs: Vector[Double], ys: Vector[Double]) : Double =
  (for ((x,y) <- xs zip ys ) yield x*y).sum


/* SETS */
// `Set()` is implemented as a hash table
// `TreeSet()` is implemented as a red black tree
val froits = Set("banana", "apple", "pear")
val ex2 = (1 to 5).toSet
froits filter (x => x.startsWith("app"))
froits.isEmpty
froits.nonEmpty
froits contains "pineapple"


scala.collection.immutable.TreeSet(1, 3)



/* MAPS */
// `Map()` is implemented as hash table
// TreeMap is implemented as a red black tree
var digits = Map(1 -> "one", 3 -> "three")
scala.collection.immutable.TreeMap(1 -> "one", 3 -> "three")
digits(1)
digits get 10
var digitsTunes = digits withDefaultValue "<unknown>"  // add default value to the Map
digitsTunes(10)



// Functional Random Generators
import java.util.Random

import scala.collection.mutable
val rand = new Random
rand.nextInt()
rand.nextFloat()
rand.nextBoolean()

// how to get random values for other domains (Lists, Strings etc.)
trait Generator[+T] {
  def generate: T
}

val integers = new Generator[Int] {
  val rand = new java.util.Random
  def generate = rand.nextInt()
}

val booleans = new Generator[Boolean] {
  def generate = integers.generate > 0
}

val random_pairs = new Generator[(Int, Int)] {
  def generate = (integers.generate, integers.generate)
}

random_pairs.generate

/////////////
/* STREAMS */
/* in python Streams are similar to generators */
/////////////
println("STREAMS MODULE")
(1 to 1000) toStream

def isPrime(n: Int) = {
  // it's a bad implementation because in this case scala create array length n
  // and then check forall-condition for each element
  require(n > 1, "the number must be greater 1")
  (2 to scala.math.sqrt(n).toInt) forall (n % _ != 0)
}

def isPrimeTail(n: Int):Boolean = {
  require(n > 1, "the number must be greater 1")
  def iteration(current: Int, n: Int): Boolean =
    if (current == scala.math.sqrt(n).toInt + 1) true
    else if (n % current == 0) false
    else iteration(current + 1, n)
  iteration(2, n)
}

for (i <- 2 to 100 if isPrime(i)) yield i
for (i <- 2 to 100 if isPrimeTail(i)) yield i

((1000 to 10000) filter isPrime)(1) // the second prime number
// there is a problem: we want to get just the second prime number but
// here we evaluating all prime numbers in the range (1000 tp 10000)
// to avoid computing the tail of sequence until it's needed there is class Stream
Stream(1,2,3,4)
(1 to 10).toStream
(1 to 10).toStream.take(3).toList
//Stream is like generator in Python
//((1000 to 1000000000) filter isPrime)(1)  // it doesn't work (!)
((1000 to 1000000000).toStream filter isPrime)(1)  // it works fine
// x :: xs produce a list
// x #:: xs produce a Stream

def isPrimeStream(n: Int) = {
  require(n > 1, "the number must be greater 1")
  !((2 to scala.math.sqrt(n).toInt).toStream exists (n % _ == 0))
}
for (i <- 2 to 100 if isPrimeStream(i)) yield i

isPrime(Int.MaxValue)
isPrimeTail(Int.MaxValue)
isPrimeStream(Int.MaxValue)

// Infinite Streams
def from(n: Int) : Stream[Int] = n #:: from(n+1)
val nats = from(0)  // stream of all natural numbers
val mult4 = nats map (_ * 4) //stream of all multiples of 4
(mult4 take 10).toList

// generator of prime numbers based on The Sieve of Eratosthenes algorithm
// https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes
// seems, this implementation is not memory efficient
def sieve(s: Stream[Int]): Stream[Int] =
  s.head #:: sieve(s.tail filter (_ % s.head != 0))

val primes = sieve(from(2))
(primes take 5).toList



////////////////////////////////////
/* Project: Water Pouring Problem */
////////////////////////////////////
class Pouring(capacity: Vector[Int]) {
  // States - the current state of all glasses
  type State = Vector[Int]
  val initialState = capacity map (_ => 0) // just consist of zeros

  // Moves
  trait Move {
    def change(state: State): State
  }
  case class Empty(glass:Int) extends Move {
    def change(state: State): State = state.updated(glass, 0)
  }
  case class Fill(glass:Int) extends Move {
    def change(state: State): State = state.updated(glass, capacity(glass))
  }
  case class Pour(from:Int, to: Int) extends Move {
    def change(state: State) : State = {
      val freespace = capacity(to) - state(to)  // the current free space in the `to` glass
      val amount = state(from) min freespace
      state.updated(from, state(from) - amount).updated(to, state(to) + amount)
    }
  }

  val glasses = capacity.indices   // assign an index to each glass

  // all possible moves
  val moves =
    (for (g <- glasses) yield Empty(g)) ++
    (for (g <- glasses) yield Fill(g)) ++
    (for (from <- glasses; to <- glasses if from != to) yield Pour(from, to))

  // Paths (is sequence of moves)
  class Path(history: List[Move]) {
    // the end state based on the history
    def endState: State = trackState(history)
    private def trackState(xs: List[Move]): State = xs match {
      case Nil => initialState
      case move :: xs1 => move change trackState(xs1)
    }
    def extend(move: Move) = new Path(move :: history)
    override def toString = (history.reverse mkString " ") + "-->" + endState
  }

  val initialPath = new Path(Nil)

  def from(paths: Set[Path], explored: Set[State]): Stream[Set[Path]] =
    if (paths.isEmpty) Stream.empty
    else {
      val more = for {
        path <- paths
        next <- moves map path.extend
        if !(explored contains next.endState)
      } yield next
      paths #:: from(more, explored ++ (more map (_.endState)))
    }

  val pathSets = from(Set(initialPath), Set(initialState))

  def solutions(target: Int): Stream[Path] = {
    for {
      pathSet <- pathSets
      path <- pathSet
      if path.endState contains target
    } yield path
  }
}

val problem = new Pouring(Vector(7, 6))
problem.initialState
problem.glasses
problem.moves
problem.pathSets.take(10).toList
problem.solutions(1).take(5).toList












class Pos(row: Int, col: Int) {
  def unpack: (Int, Int) = (row, col)
  override def toString: String = "row=" + row + "; col=" + col
}

val q = new Pos(1,2)
val (aa, bb) = q.unpack