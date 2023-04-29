package lava.core.util

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

/**`
 * Created by svc on 2023/4/27
 */
object CrossUtil {
    const val RESULT_CODE = "result_code"

    fun request(activity: AppCompatActivity, intent: Intent, block: (Intent?) -> Unit) {
        val contract = object: ActivityResultContract<Any, Intent?>() {
            override fun createIntent(context: Context, input: Any) = intent

            override fun parseResult(resultCode: Int, intent: Intent?): Intent? {
                intent?.putExtra(RESULT_CODE, intent)
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

    private fun <I: Any, O> request(activity: AppCompatActivity, input: I, contract: ActivityResultContract<I, O>, callback: ActivityResultCallback<O>) {
        val launcher = activity.registerForActivityResult(contract, callback)
        launcher.launch(input)
    }
}