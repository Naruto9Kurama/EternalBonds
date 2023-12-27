import android.os.Handler
import android.os.Looper

object MainThreadExecutor {
    private val handler = Handler(Looper.getMainLooper())

    fun runOnUiThread(task: () -> Unit) {
        handler.post { task() }
    }
}
