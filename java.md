#### Table of Content

- [code example](#code-example)
- [data types](#data-types)
- [variables](#variables)
- [relational operators](relational-operators)
- [if statement](if-statement)
- [condition statement](condition-statement)
- [for loop](for-loop)
- [arrays](arrays)
- [dict](dict)
- [OOP](oop)
- [other notes](other-notes)



#### Java documentation:  <br>

https://docs.oracle.com/javase/tutorial/java/concepts/index.html

#### Eclipse Che — cloud IDE:

- https://habrahabr.ru/post/310438/
- https://www.eclipse.org/che/getting-started/cloud/




#### code example

``` java
public class YourName {
	public static void main(String[] args) {
		System.out.println("Hello world");
	}
}
```

#### data types
``` java
byte -128 .. 127    (integer)
short  -32,768 .. 32,767
int -2^31 .. 2^31 - 1
long -2^63 .. 2^63 - 1
float
double
boolean true/false
char
java.math.BigDecimal  //very high precision
```

#### variables
``` java
int myLuckyNumber = 7;
int sum = 34 + 113;
int product = 2 * 8;
int quotient = 45 / 3;
int myRemainder = 5 % 3;
char movieRating = 'A';    
boolean isFun = true;
String s = "this is a string"   // note: double quotes!
                                // string objects are immutable
int len = string_name.length()  // lenght of string
string1.concat(string2)
String all = "Hello" + "World"
```

#### relational operators
``` java
<, <=, >, >=, ==, !=
&& - and
|| - or
The precedence of each Boolean operator is as follows:
! is evaluated first
&& is evaluated second
|| is evaluated third
Example: !(false) || true && false)
1. !(false) = true
2. true && false = false
3. true || false = true
++ //increase by 1
-- //decrease by 1 //example: variable++; or variable--;
```

#### if statement
``` java
if (condition) {
    /* code */
} else if (condition) {
    /* code */
} else {
    /*code*/
}
```

####  condition statement
``` java
//ternary condition statement
char gameResult = (pointsScored > 20) ? 'W' : 'L';
//variable = (condition) ? value if true : value if false ;

//switch statement
int Number = 3;
switch (Number) {

    case 1: System.out.println("This is namber 1");
      break;
    
    case 2: System.out.println("This is namber 2");
      break;
    
    case 3: System.out.println("This is namber 3");
      break;
    
    default: System.out.println("It s not 1,2 or 3");
      break;
}
```

#### for loop
``` java
for (int counter = 0; counter < 5; counter++) {
    System.out.println("The counter value is: " + counter);
    }
```

#### arrays
``` java
/*ARRAYS*/
import java.util.ArrayList;
//or
import java.util.*;

//declaring
ArrayList<Integer> name_of_list = new ArrayList<Integer>();
//ArrayList is a pre-defined Java class. In the example above, we create an ArrayList object called name_of_list that will store data types belonging to the <Integer> class (whole numbers).

name_of_list.add(100);
name_of_list.add(75);
name_of_list.add(43);
name_of_list.add(0, 100);  //incert at the first position

//select element
name_of_list.get(0)  //from zero, like python
name_of_list.get(1)

name_of_list.size() //number of elements in the list

//pass though all elements in array
for (int i = 0; i < quizGrades.size(); i++) {
    System.out.println( quizGrades.get(i) );
}

//previous method equals the "for each loop"
for (Integer temperature : weeklyTemperatures) {   // (:) can be read as "in"
    System.out.println(temperature);
}
```

#### dict
``` java
/*DICT*/
import java.util.HashMap;

//declaring
HashMap<String, Integer> myFriends = new HashMap<String, Integer>();

myFriends.put("Mark", 24);
myFriends.put("Cassandra", 25);

myFriends.get("Mark");

myFriends.size()

myFriends.keySet()  //get all keys

for (String name: myFriends.keySet()) {   //go through list
    System.out.println(name + " is age: " + myFriends.get(name));
}
```

### OOP
``` java
//OOP, classes, objects and methods
/*A class is a set of instructions that describe how a data structure should behave*/

class Dog {   // class
  int age;
	public Dog(int dogsAge) {   // constructor
  	age = dogsAge;
  }

    public void bark() {     // method
        System.out.println("Woof!");
    }
    
    public void run( int feet) {   //methof with parameter
        System.out.println("Your dog ran " + feet + " feet!");
    }
    
    public static void main(String[] args) {  // Java's built-in main method
                                              // When Java runs your program, 
                                                  //     the code inside of the main method is executed
       
        Dog spike = new Dog(1995);    // object
    
        spike.bark()   //call the method bark
        spike.run(183); // call method run with parameter
      
        }

}

//methods:
/*The void keyword indicates that no value should be returned
do not use void, if you want method return something and add return command to 
    define what do you want to return and inplace void to type of returned variable (int, char etc)*/
//also method can recieve variables without return something
//example:
    public int numberOfTires() {
        return 4;
    }

    int tires = myFastCar.numberOfTires();  //it should be placed into main method

//extending other classes
class Dog extends Animal {} //means the class Dog uses class Animals 
                            //(all methods of Animals are available in Dog class)

//description of OOP elements:
//  Class: a blueprint for how a data structure should function
//  Constructor: instructs the class to set up the initial state of an object
//  Object: instance of a class that stores the state of a class
//  Method: set of instructions that can be called on an object
//  Parameter: values that can be specified when creating an object or calling a method
//  Return value: specifies the data type that a method will return after it runs
//  Inheritance: allows one class to use functionality defined in another class

//you can use methods to objects
```



#### other notes
``` java
//OTHER NOTES
int is allows values between -2,147,483,648 and 2,147,483,647
//comment
/* comment block */
boolean is a data type that can only be either true or false
the char data type is used to represent single characters
All char values must be enclosed in single quotes, like this: 'G'
/* Java is an object-oriented programming (OOP) language, which means that we can design classes, objects, and methods that can perform certain actions. These behaviors are important in the construction of larger, more powerful Java programs */
```



