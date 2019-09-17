import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicReference

class Suspender {

    private val synchronized = Mutex()
    private val mutex: AtomicReference<Mutex?> =
        AtomicReference(Mutex(true))

    suspend fun suspend() {
        synchronized.withLock {
            val m: Mutex = mutex.get()
                ?: return
            m.lock()
        }
    }

    fun resumeAll() {
        val m: Mutex = mutex.getAndSet(null)
            ?: return
        while (m.isLocked) {
            m.unlock()
        }
    }
}