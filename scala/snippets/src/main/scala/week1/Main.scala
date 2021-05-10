package recfun

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }

    println(balance("()()()".toList))
    println(balance("()a()".toList))
    println(balance("()()(v".toList))

    println(countChange(4, List(1, 2)))
    println(countChange(5, List(1, 2)))

  }


  /**
    * Exercise 1
    */
  def pascal(c: Int, r: Int): Int = {
    // if the point belongs to a board, return 1
    if (c == 0 || c == r) {
      1
    }
    else {
      pascal(c - 1, r - 1) + pascal(c, r - 1)
    }
  }


  /**
    * Exercise 2
    */
  def balance(chars: List[Char]): Boolean = {
    def balanceCheck(chars: List[Char], numberOfOpens: Int): Boolean = {
      if (chars.isEmpty) numberOfOpens == 0
      else if (chars.head == '(')
        balanceCheck(chars.tail, numberOfOpens + 1)
      else if (chars.head == ')')
        numberOfOpens > 0 && balanceCheck(chars.tail, numberOfOpens - 1)
      else balanceCheck(chars.tail, numberOfOpens)
    }
    balanceCheck(chars, 0)
  }

  /**
    * Exercise 3
    */
  def countChange(money: Int, coins: List[Int]): Int = {
    if (money == 0)
      1
    else if (money > 0 && coins.nonEmpty)
      countChange(money - coins.head, coins) + countChange(money, coins.tail)
    else
      0
  }
}
