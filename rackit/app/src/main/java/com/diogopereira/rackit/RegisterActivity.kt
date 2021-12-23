package com.diogopereira.rackit

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import com.diogopereira.rackit.v2.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity(),TextWatcher {
    private lateinit var binding: ActivityRegisterBinding

    private lateinit var firebaseAuth : FirebaseAuth

    private lateinit var progressDialog : ProgressDialog


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

    private var nome =""
    private var email =""
    private  var password = ""
    private var confirmarPassword = ""
    private var confirmarTermos = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Um momento")
        progressDialog.setCanceledOnTouchOutside(false)

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

        binding.loginHereTextView.setOnClickListener{
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
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

    private fun validateData() {
        nome = editTextNome.text.toString().trim()
        email = editTextEmail.text.toString().trim()
        password = editTextConfirmPassword.text.toString().trim()
        confirmarTermos = confirmTermos.isChecked
        confirmarPassword = editTextConfirmPassword.text.toString().trim()
        when {
            Patterns.EMAIL_ADDRESS.matcher(email)
                .matches() && passwordconfirmed && nome!="" && password == confirmarPassword && confirmarTermos -> {
                //createAccount(email, password, nome)
                    createContaUtilizador()
            }

            else -> {
                if (nome.isEmpty()) {
                    editTextNome.error = "Por favor introduza o nome"
                }
                if (!confirmTermos.isChecked)
                    confirmTermos.error = "Tem de concordar com os termos"
            }

        }
    }

    private fun createContaUtilizador() {
        progressDialog.setMessage("Criar conta...")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                updateUserInfo()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this , "Erro ao criar conta, ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

    private fun updateUserInfo() {
        progressDialog.setMessage("Guardar dados...")

        val timestamp = System.currentTimeMillis()

        val uid = firebaseAuth.uid

        val hashMap : HashMap<String,Any?> = HashMap()
        hashMap["uid"]=uid
        hashMap["email"]=email
        hashMap["name"]=nome
        hashMap["userType"]="user"
        hashMap["timestamp"]=timestamp


        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                    progressDialog.dismiss()
                Toast.makeText(this, "Conta criada...",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this, "Erro...${e.message}",Toast.LENGTH_SHORT).show()
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


}