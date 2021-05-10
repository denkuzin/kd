package sqrt


object Session extends App {
        
    val PRECISION :Double = 0.00000001
 
    def sqrtIter (guess: Double, x: Double) : Double = {
        if (isGoodEnough(guess, x)) guess 
        else sqrtIter(improove(guess,x), x)
    }

    def isGoodEnough(guess: Double, x: Double) :Boolean = {
        Math.abs(guess * guess - x) < PRECISION
    }

    def improove(guess: Double, x: Double) =  (guess + x / guess) / 2

    def sqrt(x: Double) = sqrtIter(1.0, x)


    // check module
    printf("precison equals %s\n", PRECISION)

    val checks = List(2,4,10,100,1e-10,1e10)     // for 1e-06 we should recieve 1e-5
    for (item <- checks) {                       // for 1e10 we should recieve 1e5
        println(sqrt(item))
    }

}




