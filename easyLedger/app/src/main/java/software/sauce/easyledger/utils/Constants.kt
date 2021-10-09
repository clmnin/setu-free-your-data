package software.sauce.easyledger.utils

class Constants {
    companion object {
        private const val server = "192.168.1.8"
        const val BASE_URL = "http://$server/"
        const val TAG = "AppDebug"
        val acceptedPhones = listOf("9999999999")
        const val acceptedOTP = "123456"
        const val SQLITE_DATE_TIMEFORMAT = "yyyy-MM-dd'T'HH:mm:sssZ"
    }
}