package akhtemov.vladlen.simplenotes.utility

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "DateHelper"

class DateHelper {

    companion object {
        fun getTimeNow(): String {
            val date = Date()
            val sdf = SimpleDateFormat("HH:mm")
            val currentTime = sdf.format(date)
            val result = getTimeWithoutAZeroAtTheBeginning(currentTime)

            Log.d(TAG, "getTimeNow: $result")

            return result
        }

        fun getDateNow(): String {
            val date = Date()
            val sdf = SimpleDateFormat("yyyy/M/dd")
            val currentDate = sdf.format(date)

            return StringHelper.replaceSlashWithHyphen(currentDate)
        }

        private fun getTimeWithoutAZeroAtTheBeginning(time: String): String {
            val timeCharArray = time.toCharArray()
            var newTime = ""
            var i = 0

            while (i != timeCharArray.size) {
                if (i == 0) {
                    if (timeCharArray[i] == '0') {

                    } else {
                        newTime += timeCharArray[i]
                    }
                } else {
                    newTime += timeCharArray[i]
                }

                i++
            }

            return newTime
        }
    }
}