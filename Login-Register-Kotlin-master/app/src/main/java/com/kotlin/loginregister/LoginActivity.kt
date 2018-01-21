package com.kotlin.loginregister

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener({
            doLogin()
        })

        tv_register.setOnClickListener({
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        })
    }

    private fun loginValidation(): Boolean {
        if (et_email.text.isEmpty()) {
            et_email.error = getString(R.string.required_email)
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(et_email.text).matches()) {
            et_email.error = getString(R.string.invalid_email)
            return false
        } else if (et_password.text.isEmpty()) {
            et_password.error = getString(R.string.required_password)
            return false
        } else {
            return true
        }
    }

    private fun doLogin() {
        if (loginValidation()) {
            val email = et_email.text.toString()
            val password = et_password.text.toString()
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage(getString(R.string.please_wait))
            progressDialog.show()

            val stringRequest = object : StringRequest(Request.Method.POST, getString(R.string.api) + "login.php",
                    Response.Listener<String> { response ->
                        // Jika Sukses Mendapat Data Dari Server
                        progressDialog.dismiss()
                        Log.d("response", response.toString())
                        try {
                            val jsonObject = JSONObject(response)
                            Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
                            if (jsonObject.getString("status") == "ok") {
                                val jsonData = jsonObject.getJSONObject("data")
                                val i = Intent(this, ProfileActivity::class.java)
                                i.putExtra("full_name", jsonData.getString("full_name"))
                                i.putExtra("email", jsonData.getString("email"))
                                startActivity(i)
                                finish()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener { error ->
                        // Jika Gagal Mendapat Data Dari Server
                        progressDialog.dismiss()
                        Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
                    }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params.put("email", email)
                    params.put("password", password)
                    return params
                }
            }
            val requestQueue = Volley.newRequestQueue(this)
            requestQueue.add(stringRequest)
        }
    }
}