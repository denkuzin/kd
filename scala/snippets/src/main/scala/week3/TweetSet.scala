package objsets
import TweetReader._

/* singelton indexer */
object globalId {
  val nextId = {
    var i = 0; () => { i += 1; i}
  }
}

class Tweet(val user: String, val text: String, val retweets: Int) {
  override def toString: String =
    "User: " + user + "\n" +
      "Text: " + text + " [" + retweets + "]"
  val id: globalId.type = globalId

  def > (that:Tweet): Boolean = this.retweets > that.retweets
  def < (that:Tweet): Boolean = this.retweets < that.retweets
}


abstract class TweetSet {

  /** This method takes a predicate and returns a subset of all the elements
    * in the original set for which the predicate is true.
    * Question: Can we implement this method here, or should it remain abstract
    * and be implemented in the subclasses? */
  def filter(p: Tweet => Boolean): TweetSet = this.filterAcc(p, new Empty)

  /* This is a helper method for `filter` that propagetes the accumulated tweets. */
  def filterAcc(p: Tweet => Boolean, acc: TweetSet): TweetSet

  /* Returns a new `TweetSet` that is the union of `TweetSet`s `this` and `that`. */
  def union(that: TweetSet): TweetSet

  /** Returns the tweet from this set which has the greatest retweet count.
    * Calling `mostRetweeted` on an empty set should throw an exception of
    * type `java.util.NoSuchElementException`.
    * Question: Should we implement this method here, or should it remain abstract
    * and be implemented in the subclasses? */
  def mostRetweeted: Tweet

  def isEmpty: Boolean

  /** Returns a list containing all tweets of this set, sorted by retweet count
    * in descending order. In other words, the head of the resulting list should
    * have the highest retweet count.
    * Hint: the method `remove` on TweetSet will be very useful.
    * Question: Should we implment this method here, or should it remain abstract
    * and be implemented in the subclasses? */
  def descendingByRetweet: TweetList

  /* Returns a new `TweetSet` which contains all elements of this set, and the
   * the new element `tweet` in case it does not already exist in this set.
   * If `this.contains(tweet)`, the current set is returned.*/
  def incl(tweet: Tweet): TweetSet

  /* Returns a new `TweetSet` which excludes `tweet`. */
  def remove(tweet: Tweet): TweetSet

  /* Tests if `tweet` exists in this `TweetSet`. */
  def contains(tweet: Tweet): Boolean

  /* This method takes a function and applies it to every element in the set */
  def foreach(f: Tweet => Unit): Unit
}

class Empty extends TweetSet {

  //var left = throw new java.util.NoSuchElementException()
  //var right = throw new java.util.NoSuchElementException()
  //var elem = throw new java.util.NoSuchElementException()

  def isEmpty: Boolean = true

  def filterAcc(p: Tweet => Boolean, acc: TweetSet): TweetSet = acc

  def union(that:TweetSet): TweetSet = that

  def mostRetweeted: Tweet = throw new java.util.NoSuchElementException()
  def descendingByRetweet: TweetList = throw new java.util.NoSuchElementException()

  def contains(tweet: Tweet): Boolean = false

  def incl(tweet: Tweet): TweetSet = new NonEmpty(tweet, new Empty, new Empty)

  def remove(tweet: Tweet): TweetSet = this

  def foreach(f: Tweet => Unit): Unit = ()

  override def toString: String = "e"
}


class NonEmpty(var elem: Tweet, var left: TweetSet, var right: TweetSet) extends TweetSet {

  def isEmpty: Boolean = false

  def filterAcc(p: Tweet => Boolean, acc: TweetSet): TweetSet = {
    if( p(elem) ) left.filterAcc(p, right.filterAcc(p, acc.incl(elem)))
    else left.filterAcc(
      p, right.filterAcc(p, acc))
  }

  def incl(x: Tweet): TweetSet = {
    if (x.text < elem.text) new NonEmpty(elem, left.incl(x), right)
    else if (x.text > elem.text) new NonEmpty(elem, left, right.incl(x))
    else this
  }

  def union(that: TweetSet): TweetSet = {
    left.union(right.union(that)).incl(elem)
  }

  // TODO rewrite it using accumulator
  def mostRetweeted: Tweet = {
    val leftward: Tweet = if (!left.isEmpty) left.mostRetweeted else new Tweet(
      "", "", scala.Int.MinValue)
    val rightward: Tweet = if (!right.isEmpty) right.mostRetweeted else new Tweet(
      "", "", scala.Int.MinValue)

    if (elem > leftward && elem > rightward ) elem
    else if (leftward > rightward) leftward
    else rightward
  }

  /*
 //def dfs(ts: TweetSet, acc: Tweet): Tweet = {
   // compare with node's value
   //acc =
   //if (ts.elem > acc) var acc = ts.elem

   // compare with the left branch
   //val left_tweet:Tweet = if (!ts.left.isEmpty) dfs(ts.left, acc) else acc
   //val acc_new = if (left_tweet > acc) left_tweet else acc

   // compare with the right branch
   //val right_tweet:Tweet = if (!ts.right.isEmpty) dfs(ts.right, acc_new) else acc_new
   //val acc = if (right_tweet > acc_new) right_tweet else acc_new
   //acc_new
 //}
 */


  def descendingByRetweetAcc(ts: TweetSet, acc: TweetList): TweetList = {
    if (ts.isEmpty) acc else {
      val biggest = ts.mostRetweeted
      val newAcc = descendingByRetweetAcc(ts.remove(biggest), acc)
      new Cons(biggest, newAcc)
    }
  }

  override def descendingByRetweet: TweetList = {
    descendingByRetweetAcc(this, Nil)
  }


  def contains(x: Tweet): Boolean =
    if (x.text < elem.text) left.contains(x)
    else if (x.text > elem.text) right.contains(x)
    else true

  def remove(tw: Tweet): TweetSet =
    if (tw.text < elem.text) new NonEmpty(elem, left.remove(tw), right)
    else if (tw.text > elem.text) new NonEmpty(elem, left, right.remove(tw))
    else left.union(right)  //tw is root

  def foreach(f: Tweet => Unit): Unit = {
    f(elem)
    left.foreach(f)
    right.foreach(f)
  }
  override def toString: String = "(" + left + "," + elem.text + "," + right + ")"
}


trait TweetList {
  def head: Tweet
  def tail: TweetList
  def isEmpty: Boolean
  def foreach(f: Tweet => Unit): Unit =
    if (!isEmpty) {
      f(head)
      tail.foreach(f)
    }
}

object Nil extends TweetList {
  def head = throw new java.util.NoSuchElementException("head of EmptyList")
  def tail = throw new java.util.NoSuchElementException("tail of EmptyList")
  def isEmpty = true
}

class Cons(val head: Tweet, val tail: TweetList) extends TweetList {
  def isEmpty = false
}

object GoogleVsApple {
  val google = List("android", "Android", "galaxy", "Galaxy", "nexus", "Nexus")
  val apple = List("ios", "iOS", "iphone", "iPhone", "ipad", "iPad")

  lazy val googleTweets: TweetSet =
    TweetReader.allTweets.filter(p => google.find(x=>(p.text.contains(x))).isDefined)
  lazy val appleTweets: TweetSet = TweetReader.allTweets.filter(p => apple.find(x=>(p.text.contains(x))).isDefined)

  /**
    * A list of all tweets mentioning a keyword from either apple or google,
    * sorted by the number of retweets.
    */
  lazy val trending: TweetList = {
    val unionTweets = appleTweets.union(googleTweets)
    unionTweets.descendingByRetweet
  }
}

object Main extends App {
  // Print the trending tweets
  GoogleVsApple.trending foreach println
}