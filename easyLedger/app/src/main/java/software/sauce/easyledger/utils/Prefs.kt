package software.sauce.easyledger.utils

import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context)
{
    companion object {
        const val sharedPrefName = "ezLedgerPrefs"
        const val jwtToken = "jwtToken"
        const val profile = "myProfile"

        const val selectedCompany = "companyId"
    }

    private val preferences: SharedPreferences = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE)

    var token: String?
        get() = preferences.getString(jwtToken, "")
        set(value) = preferences.edit().putString(jwtToken, value).apply()

    var userProfile: String?
        get() = preferences.getString(profile, "")
        set(value) = preferences.edit().putString(profile, value).apply()

    var selectedCompanyUUID: String?
        get() = preferences.getString(selectedCompany, "")
        set(value) = preferences.edit().putString(selectedCompany, value).apply()
}