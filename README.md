## Code for Paper: _An Approach to Construction of a Deterministic Parallel Programming System Based on Monotonic Objects_

This is full Kotlin code of the example in Figures 2 and 3 in the paper (in Russian):

* Алексей И. Адамович, Андрей В. Климов.
__Подход к построению системы детерминированного параллельного программирования на основе монотонных объектов__ // [_Вестник СибГУТИ._](http://vestnik.sibsutis.ru/) — 2019. — № 3. — 13 с. — ISSN 1998-6920. —
(Accepted, publication pending). — 
([PDF of authors' version](https://pat.keldysh.ru/~anklimov/papers/2019-Adamovich_Klimov--An_Approach_to_Construction_of_a_Deterministic_Parallel_Programming_System--Vestnik_SibGUTI--submitted-revised.pdf))

The notion of _monotonic_ class and object is motivated and defined in the paper and illustrated by this example.
The beginning and end of the code used in figures are marked with comment lines ///////:

* Fig. 2 in [`LongM.kt`](src/LongM.kt)
* Fig. 3 in [`Fib.kt`](src/Fib.kt)

The monotonic class `longM` implements the notion of _I-structure_ [[Arvind _et al_ 1989]](https://doi.org/10.1145/69558.69562), that is an object with a `Long` field that changes monotonically from the initial "undefined", "unready" state to a state "defined" with a `Long` value by method `set` and then possibly to the "overdefined", "contradictory" state by another invocation of `set` with an unequal value, implemented as an exception.
The `get` method _suspends_ the current coroutine if the object is in the "unready" state, until it becomes "defined" after an invocation of `set`.

The implementation is based on Kotlin [_coroutines_](https://kotlinlang.org/docs/reference/coroutines/coroutines-guide.html), that are lightweight parallel threads.
A coroutine is a stack of functions (methods) marked with `suspend` modifiers and possibly regular functions (methods), which the last `suspend` function have called.
The first stack frame of a coroutine is the argument of the `launch` or `runBlocking` library functions.
The `launch` function creates a new coroutine, which will be started in parallel later when a free core thread is available.

Each `LongM` object uses an own private object of class `Suspender` in the `unready` field. 
This object keeps a queue of the coroutines that have been suspended by an invocation of the `suspend` method (in the statement `unready?.suspend()`).
The suspended coroutines are released by the `releaseAll` method.
Notice that `suspend` and `releasedAll` are analogous to the `wait` and `notifyAll` standard methods in Java.

The file `Fib.kt` demonstrates the use of `LongM` monotonic objects for computation of Fibonacci numbers in linear time due to memoization of the numbers in the `LongM` objects.