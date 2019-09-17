import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

//import kotlinx.coroutines.*

///////////////////////////////////////////////////////////// Fig.2

class LongM {
    private var unready: Suspender? = Suspender()
    private var value: Long = 0;

    suspend fun get(): Long {

        unready?.suspend()
        return value
    }

    fun set(x: Long) {
        val u = unready
        if (u != null) {
            value = x
            unready = null
            u.resumeAll()
        }
        else if (x != value)
            throw Exception()
    }
}

/////////////////////////////////////////////////////////////

fun main(args: Array<String>) {
    println("LongM test")
    runBlocking(Dispatchers.Default) {
        val i1 = LongM()
        val i2 = LongM()
        val i3 = LongM()
        val i4 = LongM()
        val i5 = LongM()
        println("i5.set")
        launch { i5.set(i3.get() + i4.get()) }
        println("i4.set")
        launch { i4.set(i2.get() + i3.get()) }
        println("i3.set")
        launch { i3.set(i1.get() + i2.get()) }
        println("i2.set")
        launch { i2.set(2) }
        println("i1.set")
        launch { i1.set(1) }
        println("i5 = ${i5.get()}")
        println("i4 = ${i4.get()}")
        println("i3 = ${i3.get()}")
        println("i2 = ${i2.get()}")
        println("i1 = ${i1.get()}")
    }
}