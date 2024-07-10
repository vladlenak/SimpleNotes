package akhtemov.vladlen.simplenotes.utility

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission

object PermissionHelper {
    private const val POST_NOTIFICATIONS_REQUEST_CODE = 1

    fun checkPermission(context: Context, activity: Activity) {
        if (checkSelfPermission(context, POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions(
                    activity,
                    arrayOf(POST_NOTIFICATIONS),
                    POST_NOTIFICATIONS_REQUEST_CODE
                )
            }
        }
    }
}