## Code for Paper: _An Approach to Construction of a Deterministic Parallel Programming System Based on Monotonic Objects_

This is Kotlin code for Figures 2 and 3 in paper (in Russian):

* Алексей И. Адамович, Андрей В. Климов.
__Подход к построению системы детерминированного параллельного программирования на основе монотонных объектов__ // [_Вестник СибГУТИ._](http://vestnik.sibsutis.ru/) — 2019. — № 3. — 13 с. — ISSN 1998-6920. —
(Accepted, publication pending). — 
([PDF of authors' version](https://pat.keldysh.ru/~anklimov/papers/2019-Adamovich_Klimov--An_Approach_to_Construction_of_a_Deterministic_Parallel_Programming_System--Vestnik_SibGUTI--submitted-revised.pdf))

The beginning and end of the code used in figures is marked with comment lines ///////:

* Fig. 2 in [`LongM.kt`](src/LongM.kt)
* Fig. 3 in [`Fib.kt`](src/Fib.kt)

The code is an example of a monotonic class `longM` that implements I_Structure [Arvind _et al_ 1989], that is an object with a `Long` field that changes monotonically from the initial "undefined", "unready" state on object creation to a state "defined" with a `Long` value by method `set` and then possibly to the "overdefined", "contradictory" state by another invocation of `set` with an unequal value, implemented as an exception.

The implementation is based on Kotlin [_coroutines_](https://kotlinlang.org/docs/reference/coroutines/coroutines-guide.html) — lightweight parallel threads.
A coroutine is a stack of functions (methods) with `suspend` modifiers and regular functions (methods), which they call.
The top `suspend` function of a coroutine is called inside of the argument`launch` or `runBlocking` library functions.
The `launch` library function creates a new coroutine, which will be started in parallel later when a free core thread is available.

Each `LongM` object uses an own private object `unready` of class `Suspender` that keeps a queue of coroutines that have been suspended by an invocation of the `suspend` method.
The suspended coroutines are released by the `releaseAll` method.
Notice that `suspend` and `releasedAll` are analogous to the `wait` and `notifyAll` standard methods in Java.

The file `Fib.kt` demonstrates the use of `LongM` monotonic objects for computation of Fibonacci numbers in linear time due to memoization of the numbers in `LongM` objects.