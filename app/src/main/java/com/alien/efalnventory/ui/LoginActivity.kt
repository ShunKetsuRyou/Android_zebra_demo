package com.alien.efaInventory.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.content.SharedPreferences
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.alien.efaInventory.BuildConfig
import com.alien.efaInventory.dataModel.*
import com.alien.efaInventory.dataModel.companyDataApiModel.CompanyApICallback
import com.alien.efaInventory.dataModel.companyDataApiModel.CompanyCallModel
import com.alien.efaInventory.dataModel.companyDataApiModel.CompanyInfoResponse
import com.alien.efaInventory.apiSetting.comapnySortAPI.CompanyInterface
import com.alien.efaInventory.apiSetting.comapnySortAPI.RetrofitManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.alien.efaInventory.R
import com.alien.efaInventory.apiSetting.loginAPI.LoginInterface
import com.alien.efaInventory.apiSetting.loginAPI.LoginRetrofitManager
import com.alien.efaInventory.dataModel.loginApiModel.LoginApICallback
import com.alien.efaInventory.dataModel.loginApiModel.LoginCallModel
import com.alien.efaInventory.dataModel.loginApiModel.User
import com.alien.efaInventory.interfaces.DES
import com.alien.efaInventory.util.ProgressionBar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class LoginActivity : AppCompatActivity() {

    private lateinit var companySpinner: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        setVersion()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        companySpinner = findViewById<View>(R.id.spinner) as Spinner
        val companyRequest = CompanyCallModel()
        val client: CompanyInterface =
            RetrofitManager().getClient().create(CompanyInterface::class.java)
        val call: Call<CompanyApICallback> = client.getCompany(companyRequest)
        call.enqueue(object : Callback<CompanyApICallback> {
            override fun onResponse(
                call: Call<CompanyApICallback>,
                response: Response<CompanyApICallback>,
            ) {
                val companyInfoResponse: CompanyInfoResponse? = response.body()!!.respData?.let {
                    Json.decodeFromString(it)
                }
                val spinnerOptions: MutableList<String> = mutableListOf()
                if (companyInfoResponse != null) {
                    spinnerOptions.add(getString(R.string.公司別))
                    companyInfoResponse.companyList.forEach {
                        spinnerOptions.add(it.name.toString())
                    }
                    val spinnerArrayAdapter = ArrayAdapter(this@LoginActivity,
                        android.R.layout.simple_spinner_item,
                        spinnerOptions)
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    companySpinner.adapter = spinnerArrayAdapter
                }
            }
            override fun onFailure(call: Call<CompanyApICallback>?, t: Throwable?) {
                println("------------------- ${t.toString()}")
            }
        })
    }

    fun login(view: View?) {
        println(getPasswordValue())
        val encodePassword = DES.encryptDES(getPasswordValue(), "pegatron")
        val loginCall =
            LoginCallModel(LoginId = getUserNameValue(),Password = encodePassword,UserKey = "XMY7Dvv5rWvaTCBoZ19bag==")
        val loginClient: LoginInterface =
            LoginRetrofitManager().getLoginClient().create(LoginInterface::class.java)
        val call: Call<LoginApICallback> = loginClient.getLogin(loginCall)
        call.enqueue(object : Callback<LoginApICallback> {
            override fun onResponse(
                call: Call<LoginApICallback>,
                response: Response<LoginApICallback>
            ) {
                if (response.body()?.Succeeded == true) {

                    val loginResponse: User = response.body()!!.Data.User
                    handleInventoryRelatedData(loginResponse)
                    savaDataToPreference()

                  val loading = ProgressionBar(this@LoginActivity)
                    loading.startLoading()
                    toUserDownload()

            } else {
                Toast.makeText(
                    applicationContext, "${response.body()?.Message}",
                    Toast.LENGTH_SHORT
                ).show()
            }}
            override fun onFailure(call: Call<LoginApICallback>, t: Throwable) {
                Toast.makeText(
                    applicationContext, getString(R.string.連線失敗), Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    override fun onBackPressed() {
        return
    }
    private fun handleInventoryRelatedData(response: User) {
        saveInventorIDToPreferences(response)
        saveUserNameToPreferences(response)
    }
    private fun savaDataToPreference() {
        saveUserDataToPreferences()
        saveCompanyDataToPreferences()

    }
    private fun saveUserDataToPreferences() {
        saveDataToReference("name", getUserNameValue())
    }
    private fun saveCompanyDataToPreferences() {
        val selectedText: String = companySpinner.selectedItem.toString()
        saveDataToReference("company", selectedText)
    }
    private fun saveInventorIDToPreferences(response:User) {

        saveDataToReference("inventorId",response.workId.toString())
    }
    private fun saveUserNameToPreferences(response:User) {
        saveDataToReference("displayName",response.displayName.toString())
    }
    private fun getUserNameEditText(): EditText {
        return getEditTextById(R.id.editText_account)
    }
    private fun getVersion(): TextView {
        return getTextViewById(R.id.versionName)
    }
    private fun setVersion() {
        getVersion().text = getString(R.string.version)+ " " + BuildConfig.VERSION_NAME
    }
    private fun getPasswordEditText(): EditText {
        return getEditTextById(R.id.editText_password)
    }
    private fun getUserNameValue(): String {
        return getUserNameEditText().text.toString().trim()
    }
    private fun getPasswordValue(): String {
        return getPasswordEditText().text.toString().trim()
    }
    private fun getEditTextById(id: Int): EditText {
        return findViewById(id)
    }
    private fun getTextViewById(id: Int): TextView {
        return findViewById(id)
    }
    private fun getPreferences(): SharedPreferences {
        return this.getSharedPreferences("Value", Context.MODE_PRIVATE)
    }
    private fun saveDataToReference(key: String, value: String) {
        val sharedPreferencesEditor: SharedPreferences.Editor = getPreferences().edit()
        sharedPreferencesEditor.putString(key, value)
        sharedPreferencesEditor.apply()
    }


    private fun toUserDownload() {
        startActivity(Intent(applicationContext, UserDownloadActivity::class.java))
        finish()
    }
}



