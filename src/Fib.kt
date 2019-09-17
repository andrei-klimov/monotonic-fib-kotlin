import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

///////////////////////////////////////////////////////////// Fig.3

suspend fun fib(n: Int): Long = coroutineScope {

    val v = LongM()
    val w = LongM()
    fibRec(n, v, w)
    w.get()
}

suspend fun fibRec(n: Int, v: LongM, w: LongM): Unit = coroutineScope {

    if (n <= 1) {
        v.set(1)
        w.set(1)
    }
    else {
        val u = LongM()
        launch { w.set(u.get() + v.get()) }
        fibRec(n - 1, u, v)
    }
}

/////////////////////////////////////////////////////////////

fun testFib(n: Int) {
    System.gc()
    println("testFib ${Thread.currentThread().name}   n=$n")
    var f: Long = 0
    val time = measureTimeMillis {
        runBlocking(Dispatchers.Default) {
            f = fib(n)
        }
    }
    println("${String.format("%7d", time)} fib($n) = $f")
}

fun main(args: Array<String>) {
    println("Concurrent Fibonacci on Kotlin coroutines")
    println("Number of processors:      ${Runtime.getRuntime().availableProcessors()}")
    println("Number of running threads: ${Thread.getAllStackTraces().size}")
    println("main    ${Thread.currentThread().name}")
    listOf(
        0
        , 1, 2, 3, 4, 5//, 6, 7, 8, 9
        , 10, 11, 12, 13, 14, 15, 16, 17, 18, 19
        , 20//, 21, 22, 23, 24, 25, 26, 27, 28, 29
        , 30//, 31, 32, 33, 34, 35, 36, 37, 38, 39
        , 40//, 41, 42, 43, 44, 45, 46, 47, 48, 49
        , 50
        , 60
        , 70
        , 80
        , 90, 91, 92
    ).map(::testFib)
}