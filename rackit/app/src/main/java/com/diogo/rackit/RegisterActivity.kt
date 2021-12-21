package com.diogo.rackit

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import com.diogo.rackit.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.app.Application
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.ioweyou.PasswordStrength
import java.lang.Exception



class RegisterActivity : AppCompatActivity(), TextWatcher {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    lateinit var gv: VariaveisGlobais

    private lateinit var botaoRegister: Button
    private lateinit var editTextNome: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText
    private lateinit var confirmTermos: CheckBox
    private lateinit var loginHere: TextView
    private lateinit var strengthView: TextView
    private lateinit var progressBar: ProgressBar
    private var passwordconfirmed = false


    override fun afterTextChanged(s: Editable) {}
    override fun beforeTextChanged(
        s: CharSequence, start: Int, count: Int, after: Int
    ) {
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        updatePasswordStrengthView(s.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        gv = application as VariaveisGlobais

        super.onCreate(savedInstanceState)
        this.binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        botaoRegister = binding.registerButton
        editTextEmail = binding.emailEditText
        editTextNome = binding.usernameEditText
        editTextPassword = binding.passwordEditText
        editTextConfirmPassword = binding.confirmPasswordEditText
        confirmTermos = binding.checkBox
        strengthView = binding.passwordStrength
        progressBar = binding.progressBar
        loginHere = binding.loginHereTextView

        if (editTextPassword.text.toString().isEmpty()) {
            strengthView.text = ""
            progressBar.isVisible = false
        }
        db = Firebase.firestore
        auth = Firebase.auth


        botaoRegister.setOnClickListener {
            validateData()
        }
        editTextPassword.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (!passwordconfirmed && editTextPassword.text.toString().isNotEmpty()) {
                    editTextPassword.error = "Por favor escolha uma password mais segura"
                }

            }
            progressBar.isVisible = true

        }
        editTextConfirmPassword.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (editTextPassword.text.toString() != editTextConfirmPassword.text.toString() && editTextConfirmPassword.text.toString()
                        .isNotEmpty()
                ) {
                    editTextConfirmPassword.error = "As passwords não correspondem"
                }

            }
            progressBar.isVisible = true

        }
        editTextEmail.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (!Patterns.EMAIL_ADDRESS.matcher(editTextEmail.text.toString()).matches()) {
                    editTextEmail.error = "Introduza um email valido"
                }

            }
            progressBar.isVisible = true

        }

        editTextPassword.addTextChangedListener(this)
    }

    fun onClickLoginHere() {
        closeOpenActivity(outraActivity = LoginActivity::class.java)
    }

    private fun validateData() {
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()
        val username = editTextNome.text.toString().trim()
        val confirmarPassword = editTextConfirmPassword.text.toString()
        val confirmarTermos = confirmTermos.isChecked
        //validar dados
        when {
            Patterns.EMAIL_ADDRESS.matcher(email)
                .matches() && passwordconfirmed && username.isNotEmpty() && password == confirmarPassword && confirmarTermos -> {
                createAccount(email, password, username)
            }

            else -> {
                if (username.isEmpty()) {
                    editTextNome.error = "Por favor introduza o nome"
                }
                if (!confirmTermos.isChecked)
                    confirmTermos.error = "Tem de concordar com os termos"
            }

        }
    }


    private fun createAccount(email: String, password: String, nome: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser

                    //crio utilizador e guardo na base de dados se !=null
                    val userInfo = User(nome, email, user?.uid)
                    if (user != null) {
                        gv.user = userInfo
                        db.collection("Users").document(email).set(userInfo)

                    }

                    //closeOpenActivity(outraActivity = MainActivity::class.java)

                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        editTextPassword.error = "A password é demasiado fraca"
                        editTextPassword.requestFocus()
                    } catch (e: FirebaseAuthUserCollisionException) {
                        editTextEmail.error = "Já existe uma conta com este email"
                        editTextEmail.requestFocus()
                    } catch (e: Exception) {
                        Log.e(TAG, e.message!!)
                    }

                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            closeOpenActivity(outraActivity = MainActivity::class.java)
        }

    }


    private fun updatePasswordStrengthView(password: String) {

        if (TextView.VISIBLE != strengthView.visibility)
            return

        if (password == "") {
            strengthView.text = ""
            progressBar.progress = 0
            return
        }

        val str = PasswordStrength.calculateStrength(password)
        strengthView.text = str.getText(this)
        strengthView.setTextColor(str.color)
        passwordconfirmed = str.getText(this) != "Weak"

        progressBar.progressDrawable.setColorFilter(
            str.color,
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        when {
            str.getText(this) == "Weak" -> {
                progressBar.progress = 25
            }
            str.getText(this) == "Medium" -> {
                progressBar.progress = 50
            }
            str.getText(this) == "Strong" -> {
                progressBar.progress = 75
            }
            else -> {
                progressBar.progress = 100
            }
        }
    }

    private fun closeOpenActivity(outraActivity: Class<*>) {
        val x = Intent(this, outraActivity)
        finish()
        startActivity(x)
    }
}