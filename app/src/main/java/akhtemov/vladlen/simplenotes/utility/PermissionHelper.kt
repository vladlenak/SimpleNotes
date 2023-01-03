package akhtemov.vladlen.simplenotes.utility

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission

class PermissionHelper {

    companion object {
        private const val TAG = "PermissionHelper"
        private const val POST_NOTIFICATIONS_REQUEST_CODE = 1

        fun checkPermission(context: Context, activity: Activity) {
            if (checkSelfPermission(context, POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(TAG, "checkPermission: ")
                requestPermissions(
                    activity,
                    arrayOf(POST_NOTIFICATIONS),
                    POST_NOTIFICATIONS_REQUEST_CODE
                )
            }
        }
    }
}