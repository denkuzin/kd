//to run class you can use: 
//1. javac Dog.java
//2. java Dog

//create jdk:
//1. create Manifest.txt; 
//2. put there line: "Main-Class: Dog"
//3. jar cfm Dog.jar Manifest.txt Dog.class
//4. java -jar Dog.jar  //run jar file

//extract .jar file
//jar xf MaxClosePrice-1.0.jar

import java.util.*;
import java.util.Scanner;

class Dog {   // class
    
    int age;
    int weight = 1000;
    int num_legs;
  

    public Dog(int dogsAge) {   // constructor
  	age = dogsAge;
        System.out.println("I'm constructor, I have created object dog with\nAge = " + age + "\nWeight = " + weight + "\n");
    Scanner reader = new Scanner(System.in);
    System.out.println("Enter a number of legs:");  
    num_legs = reader.nextInt();
    System.out.println("");
    }



    public void bark() {     // method
        System.out.println("Woof!");
        System.out.println("I'm a Berkut");
    }

    public void run( int feet) {   //method with parameter
        System.out.println("Your dog ran " + feet + " feet!");
    }

    public void print_weight(int x) {      // method
        System.out.println("Dog's weight is " + x);
        }

	public static void main(String[] args) {  // Java's built-in main method
	                                          // When Java runs your program, 
                                                  // the code inside of the main method is executed
   
        Dog spike = new Dog(2014);    // object (created using constructor public Dog(int dogsAge))
        System.out.println("Age = " + spike.age);
        System.out.println("Weight = " + spike.weight);
        System.out.println("Num_legs = " + spike.num_legs);
        
        spike.bark();   //call the method bark (no parameter)
        spike.run(183); // call method run with parameter
        spike.print_weight(1000);
        
        }
}
