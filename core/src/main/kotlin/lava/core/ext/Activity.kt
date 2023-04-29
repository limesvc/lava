package lava.core.ext

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import lava.core.util.CrossUtil

/**
 * Created by svc on 2023/4/27
 */

fun AppCompatActivity.startActivityForResult(intent: Intent, block: (Intent?) -> Unit) {
    CrossUtil.request(this, intent, block)
}

fun AppCompatActivity.requestPermission(permission: String, block: (Boolean) -> Unit) {
    CrossUtil.requestPermission(this, permission, block)
}

fun AppCompatActivity.requestPermission(permissions: Array<String>, block: (Map<String, Boolean>) -> Unit) {
    CrossUtil.requestPermissions(this, permissions, block)
}