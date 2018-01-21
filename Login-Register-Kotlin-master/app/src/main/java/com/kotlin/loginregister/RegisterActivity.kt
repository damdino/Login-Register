package com.kotlin.loginregister

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Patterns
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_register.setOnClickListener({
            doRegister()
        })
    }

    private fun registerValidation(): Boolean {
        if (et_full_name.text.isEmpty()) {
            et_full_name.error = getString(R.string.required_full_name)
            return false
        } else if (et_email.text.isEmpty()) {
            et_email.error = getString(R.string.required_email)
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(et_email.text).matches()) {
            et_email.error = getString(R.string.invalid_email)
            return false
        } else if (et_password.text.isEmpty()) {
            et_password.error = getString(R.string.required_password)
            return false
        } else if (et_password.text.length < 6) {
            et_password.error = getString(R.string.password_length_error)
            return false
        } else if (et_confirm_password.text.isEmpty()) {
            et_confirm_password.error = getString(R.string.required_confirm_password)
            return false
        } else if (!et_password.text.equals(et_confirm_password.text)) {
            et_confirm_password.error = getString(R.string.password_not_match)
            return false
        } else {
            return true
        }
    }

    private fun doRegister() {
        if (registerValidation()) {
            val fullName = et_full_name.text.toString()
            val email = et_email.text.toString()
            val password = et_password.text.toString()
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage(getString(R.string.please_wait))
            progressDialog.show()

            val stringRequest = object : StringRequest(Request.Method.POST, getString(R.string.api) + "register.php",
                    Response.Listener<String> { response ->
                        // Jika Sukses Mendapat Data Dari Server
                        progressDialog.dismiss()
                        try {
                            val jsonObject = JSONObject(response)
                            Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
                            if (jsonObject.getString("status") == "ok") {
                                startActivity(Intent(this, LoginActivity::class.java))
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
                    params.put("full_name", fullName)
                    params.put("password", password)
                    return params
                }
            }
            val requestQueue = Volley.newRequestQueue(this)
            requestQueue.add(stringRequest)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}