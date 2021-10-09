package software.sauce.easyledger.utils

class Constants {
    companion object {
        private const val server = "192.168.1.8"
        const val BASE_URL = "http://$server/"
        const val TAG = "AppDebug"
        val acceptedPhones = listOf("9999999999", "9999999998")
        const val acceptedOTP = "123456"
    }
}