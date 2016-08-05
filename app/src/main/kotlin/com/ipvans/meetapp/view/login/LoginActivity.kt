package com.ipvans.meetapp.view.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import com.ipvans.meetapp.App
import com.ipvans.meetapp.R
import com.ipvans.meetapp.data.restapi.model.request.SignupRequest
import com.ipvans.meetapp.data.visibleIf
import com.ipvans.meetapp.view.main.MainActivity
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var presenter: LoginPresenter

    lateinit var component: LoginComponent

    val login: TextView by lazy { findViewById(R.id.email) as TextView }
    val password: EditText by lazy { findViewById(R.id.password) as EditText }
    val name: EditText by lazy { findViewById(R.id.name) as EditText }
    val phone: EditText by lazy { findViewById(R.id.phone) as EditText }
    val loginButton: Button by lazy { findViewById(R.id.email_sign_in_button) as Button }
    val switch: TextView by lazy { findViewById(R.id.switch_login) as TextView }
    val progress: ProgressBar by lazy { findViewById(R.id.login_progress) as ProgressBar }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component = LoginDaggerComponent().getComponent((application as App).component, this)
        component.inject(this)

        presenter.checkUserState()

        setContentView(R.layout.activity_login)
        initViews()
    }

    private fun initViews() {
        switch.setOnClickListener { presenter.switchScreenState() }
        loginButton.setOnClickListener { presenter.login(
                SignupRequest(
                        login.text.toString(),
                        password.text.toString(),
                        name.text.toString(),
                        phone.text.toString())
        ) }

        presenter.onAttached()
    }

    fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun showError(error: String?) {
        Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
    }

    fun showProgress() {
        loginButton.visibleIf(false)
        switch.visibleIf(false)
        progress.visibleIf(true)
    }

    fun showLoginForm() {
        name.visibleIf(false)
        phone.visibleIf(false)
        loginButton.visibleIf(true)
        switch.visibleIf(true)
        progress.visibleIf(false)
        loginButton.text = "Sign in"
        switch.text = "Do not have an account?\nRegister"
    }

    fun showSignupForm() {
        name.visibleIf(true)
        phone.visibleIf(true)
        loginButton.visibleIf(true)
        switch.visibleIf(true)
        progress.visibleIf(false)
        loginButton.text = "Sign up"
        switch.text = "Already have an account? Login"
    }

    override fun onStop() {
        super.onStop()
        presenter.onDetached()
    }

}

