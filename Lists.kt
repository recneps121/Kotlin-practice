// Do not remove or rename the package
package lists

/*
* The following functions are helper functions that I am providing
*/

/*
* Extend the List class with a "tail" getter to get the tail of a list.
* Below is an example of how you would use tail
*    val a = listOf(1,2,3)
*    val t = a.tail
*    println("tail of $a is $t") // prints [2,3]
*/
val <T> List<T>.tail: List<T>
get() = drop(1)

/*
* Extend the List class with a "head" getter to get the head of a list.
* Below is an example of how you would use head
*    val a = listOf(1,2,3)
*    val h = a.head
*    println("head of $a is $h") // prints 1
*/
val <T> List<T>.head: T
get() = first()

/* 
* The isPrime function takes as input an Int
*      x : an Int object to test
* and returns a Boolean
*      true  if x is a prime
*      false if x is not a prime
*/
fun isPrime(x : Int) : Boolean {
    for (i in 2..(x-1)) {
        if (x % i == 0) {
            return false
        }    
    }
    return true
}

/* The compose function takes as input
*     f - A function that takes as input a value of type T and returns a value of type T
*     g - A function that takes as input a value of type T and returns a value of type T
*  and returns as output the composition of the functions
*     f(g(x))
*/
fun<T> compose(f: (T)->T,  g:(T) -> T) : (T) -> T = { f(g(it)) }

/**
 * Creates a list of numbers from 1 to limit.
 * @param limit the number to count up to. The head of the list.
 * @return A list of Ints from 1 to limit. If limit is null, returns null.
 */
fun countingNumbers(limit : Int?) : List<Int>? {
    if (limit is Int) {
        var rList = mutableListOf<Int>() // Create an empty list of Ints.
        //Fill the list from 1 to limit.
        for (index in 1..limit) {
            rList.add(index)
        }
        return rList // Return list.
    }
    return null // If the list is null return null.
}

/**
 * Creates a list of even numbers from 2 up to limit if even, or limit-1 if not.
 * @param n The max value in the list. To count up to.
 * @return A list of even numbers from 2 up to limit. If n is null, returns null.
 */
fun evenNumbers(n : Int?) : List<Int>? {
    if (n is Int) {
        // Return the countingNumbers function, with only the even values.
        return countingNumbers(n)?.filter { x -> x % 2 == 0 }
    }
    return null
}

/**
 * Creates a list of prime numbers up to n.
 * @param n The max value in the list. To count up to.
 * @return A list of prime numbers from 2 to n. If n is null, returns null.
 */
fun primeNumbers(n : Int?) : List<Int>? {
    if (n is Int) {
        // Return the countingNumbers function, with only prime values.
        return countingNumbers(n)?.filter { x -> isPrime(x) && x != 1}
    }
    return null
}

/**
 * Merges two Lists into one.
 * @param a One of the lists to combine.
 * @param b The other list to combine.
 * @return A list composed of the other two lists. If one is null, returns null.
 */
fun<T : Comparable<T>> merge (a : List<T>?, b : List<T>?) : List<T>? {
    if (a != null && b != null) {
        // Returns a concatenated with b, sorted.
        return a.plus(b).sorted()
    }
    return null
}

/**
 * Builds a list of sub-lists, where the nth sub-list is the elements from 1 to n in the list.
 * @param a List of Integers.
 * @return A list of sub-lists, where the nth sub-list is the elements from 1 to n in the list.
 */
fun subLists(a: List<Int>?) : List<List<Int>?>? {
    val listOfLists = mutableListOf<List<Int>?>() // Allocate space for the list being returned.
    when {
        a == null -> {
            return null //Null Check
        }
        a.isEmpty() -> {
            return listOf() // Returns empty list if list is empty.
        }
        else -> {
            val lastItem = a.get(a.size - 1) //Last item in a
            var itr = 1
            while (itr <= lastItem) {
                listOfLists.add(countingNumbers(itr)) //Create sub list up to itr.
                itr++
            }
            return listOfLists
        }
    }
}

/**
 * Counts the total number of elements in a list of lists, that is all the elements in total of each sublist.
 * @param a List of lists to evaluate
 * @return Total number of elements in a.
 */
fun countElements(a: List<List<Int>?>?) : Int {
     if (a.isNullOrEmpty()) {
         return 0 // Null Check
     } else {
         var counter = 0
         for (item in a) {
             if (item != null && item.isNotEmpty()) {
                 counter += item.size
             }
         }
         return counter
     }
}

/**
 * Applies a binary function f to the elements in a list of lists, a
 * @param f Binary function to apply
 * @param a List of Lists to be evaluated
 * @return List of the values from each evaluated sublist.
 */
fun listApply(f: (Int, Int) -> Int, a: List<List<Int>?>?): List<Int>? {
    if (a != null && a.isNotEmpty()) {
        var rList = mutableListOf<Int>() //List to return.
        for (item in a) {
            if (item != null) {
                if (item.size == 1) {
                    rList.add(item.get(0)) //A single item list returns that item.
                } else {
                    var nextElem = 2
                    var itemVal = f(item.get(0), item.get(1)) //Evaluates the first two items in the list.
                    while (nextElem < item.size) {
                        itemVal = f(itemVal, item.get(nextElem)) // Iteratively evaluates each next item in the list.
                        nextElem++
                    }
                    rList.add(itemVal) // Adds the evaluated subsists value to the return list.
                }
            }
        }
        return rList
    }
    return null
}

/**
 * Builds a function that is the composition of the functions in the list a.
 * @param a List of unary functions
 * @return A function composed of each function in the list.
 */
fun composeList(a: List<(Int) -> Int>) : (Int) -> Int {
    if (a.size == 1) {
        return { param -> a.get(0)(param) } //If there is only one function that is the function to return.
    } else {
        var nextFunc = a.size - 3
        var rFunc = compose(a.get(a.size - 1), a.get(a.size - 2)) //Compose the last two functions together.
        while (nextFunc >= 0) {
            rFunc = compose(a.get(nextFunc), rFunc) //Iteratively compose each function into the previous one.
            nextFunc--
        }
        return { param -> rFunc(param) } //Return the new composed function.
    }
}