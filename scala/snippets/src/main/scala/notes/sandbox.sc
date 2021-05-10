abstract class myBoolean {
  def ifThenElse[T](t: => T, e: => T): T
  def and (x: => myBoolean ): myBoolean  = ifThenElse(x, false)
}


var c = new Int[]{}



