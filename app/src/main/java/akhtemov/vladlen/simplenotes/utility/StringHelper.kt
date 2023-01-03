package akhtemov.vladlen.simplenotes.utility

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*



class StringHelper {

    companion object {
        private const val TAG = "StringHelper"

        fun replaceSlashWithHyphen(str: String): String {
            val strCharArray = str.toCharArray()
            var newStr = ""
            var i = 0

            while (i != strCharArray.size) {
                val ch = strCharArray[i]

                newStr += if (ch == '/') {
                    '-'
                } else {
                    ch
                }

                i++
            }

            return newStr
        }


    }
}