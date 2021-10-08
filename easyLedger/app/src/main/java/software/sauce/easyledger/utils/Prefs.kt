package software.sauce.easyledger.utils

import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context)
{

    private val preferences: SharedPreferences = context.getSharedPreferences(ConstantPref.sharedPrefName, Context.MODE_PRIVATE)

    var token: String?
        get() = preferences.getString(ConstantPref.jwtToken, "")
        set(value) = preferences.edit().putString(ConstantPref.jwtToken, value).apply()

    var userProfile: String?
        get() = preferences.getString(ConstantPref.profile, "")
        set(value) = preferences.edit().putString(ConstantPref.profile, value).apply()

    var selectedCompanyUUID: String?
        get() = preferences.getString(ConstantPref.selectedCompany, "")
        set(value) = preferences.edit().putString(ConstantPref.selectedCompany, value).apply()
}