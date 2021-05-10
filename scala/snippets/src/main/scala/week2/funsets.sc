object FunSets {
  type mySet = Int => Boolean

  def contains(s: mySet, elem: Int): Boolean = s(elem)

  def singletonSet(elem: Int): mySet = x => x == elem

  def union(s: mySet, t: mySet): mySet = x => contains(s,x) || contains(t,x)

  def intersect(s: mySet, t: mySet): mySet = x => contains(s,x) && contains(t,x)

  def diff(s: mySet, t: mySet): mySet = x => contains(s,x) && !contains(t,x)

  def filter(s: mySet, p: Int => Boolean): mySet = x => contains(s,x) && p(x)

  val bound = 1000

  def forall(s: mySet, p: Int => Boolean): Boolean = {
    def iter(a: Int): Boolean = {
      if (contains(s,a) && !p(a)) false
      else if(a > 1000) true
      else iter(a + 1)
    }
    iter(-1000)
  }

  def exists(s: mySet, p: Int => Boolean): Boolean = !forall(s, x => !p(x))

  def map(s: mySet, f: Int => Int): mySet = x => exists(s, elem => x==f(elem))

  def convertToString(s: mySet): String = {
    val xs = for (i <- -bound to bound if contains(s, i)) yield i
    xs.mkString("{", ",", "}")
  }

  def printSet(s: mySet) {
    println(convertToString(s))
  }

  val s1 = singletonSet(1)
  val s2 = singletonSet(3)
  val s3 = singletonSet(-7)
  val s_all = union(s1, union(s2, s3))

  convertToString(union(union(s1, s1), s1))
  convertToString(union(s1, s2))
  convertToString(s_all)

  contains(singletonSet(2), 3)
  contains(singletonSet(2), 2)
}