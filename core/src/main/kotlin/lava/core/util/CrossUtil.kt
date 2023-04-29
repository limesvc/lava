package lava.core.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.elvishew.xlog.XLog
import kotlinx.coroutines.launch

/**`
 * Created by svc on 2023/4/27
 */
object CrossUtil {
    const val RESULT_CODE = "result_code"

    fun request(activity: AppCompatActivity, intent: Intent, block: (Intent?) -> Unit) {
        val contract = object : ActivityResultContract<Any, Intent?>() {
            override fun createIntent(context: Context, input: Any) = intent

            override fun parseResult(resultCode: Int, intent: Intent?): Intent? {
                intent?.putExtra(RESULT_CODE, resultCode)
                return intent
            }
        }

        val callback = ActivityResultCallback<Intent?> { block.invoke(it) }

        request(activity, Unit, contract, callback)
    }

    fun requestPermission(activity: AppCompatActivity, permission: String, block: (Boolean) -> Unit) {
        val callback = ActivityResultCallback<Boolean> { block.invoke(it) }
        request(activity, permission, ActivityResultContracts.RequestPermission(), callback)
    }

    fun requestPermissions(activity: AppCompatActivity, permissions: Array<String>, block: (Map<String, Boolean>) -> Unit) {
        val callback = ActivityResultCallback<Map<String, Boolean>> { block.invoke(it) }
        request(activity, permissions, ActivityResultContracts.RequestMultiplePermissions(), callback)
    }

    private fun <I : Any, O> request(
        activity: AppCompatActivity,
        input: I,
        contract: ActivityResultContract<I, O>,
        callback: ActivityResultCallback<O>
    ) {
        val crossFragment = CrossFragment(input, contract, callback)
        activity.supportFragmentManager.beginTransaction().add(crossFragment, "cross").commitAllowingStateLoss()
    }
}

internal class CrossFragment<I : Any, O>(
    private val input: I,
    private val contract: ActivityResultContract<I, O>,
    private val callback: ActivityResultCallback<O>
) : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val launcher = registerForActivityResult(contract) { result ->
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    requireActivity().supportFragmentManager.beginTransaction().remove(this@CrossFragment).commitAllowingStateLoss()
                }
            }
            callback.onActivityResult(result)
        }
        launcher.launch(input)
    }
}
