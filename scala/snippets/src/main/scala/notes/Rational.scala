package notes

class Rational (x: Int, y:Int) {
  // pre-defined function, checkd passed arguments
  // if False, it throw `IllegalArgumentException`
  require (y != 0, "denominator must be nonzero")

  // also there is assert(condition, message) function - it can be
  // used in any parts of code and it throw `AssertionError`

  def this(x: Int) = this(x, 1) // auxiliary constructor
  // that call previously defined constructor
  // IT WILL BW CALLED IN CASE NUMBER OF ARGUMENTS is
  // differ from number of arguments in the main class
  // usage example: toidentify the default values arguments

  private def gcd(a:Int, b:Int):Int = if (b == 0) a else gcd(b, a % b)

  def numer : Int = x / gcd(x, y)
  def denom : Int = y / gcd(x, y)

  def less(that: Rational) :Boolean =
    numer * denom < that.numer * that.denom

  // the same as `def less`
  def < (that: Rational): Boolean =
    numer * denom < that.numer * that.denom

  def max(that: Rational) :Rational =
    if (this < that) that else this

  def add(that:Rational) :Rational =
  // the method returns new Rational
    new Rational(numer * that.denom + that.numer * denom, denom * that.denom)

  def + (that:Rational) :Rational =
  // the method returns new Rational
    new Rational(numer * that.denom + that.numer * denom, denom * that.denom)

  // override standart method for class representation
  override def toString: String = numer + "/" + denom


  def neg(): Rational = new Rational(-numer, denom)
  def unary_- : Rational = new Rational(-numer, denom) // the same as neg()
  // but you can use it as `-this`

  def sub(that: Rational) : Rational = add(that.neg())
  def - (that: Rational) : Rational = this + -that // elegant way :-)

}


class Author (name: String, surname: String) {
  private var my_name = name.capitalize
  private var my_surname = surname.capitalize
  override def toString: String = my_name + " OMG " + my_surname
}