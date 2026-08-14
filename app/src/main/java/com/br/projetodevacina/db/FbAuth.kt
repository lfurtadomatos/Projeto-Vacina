package com.br.projetodevacina.db

import com.br.projetodevacina.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FBAuth {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val fbDatabase = FBDatabase()

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    fun login(
        email: String,
        pass: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (email.isBlank() || pass.isBlank()) {
            onFailure("Preencha todos os campos!")
            return
        }

        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception ->
                onFailure(exception.localizedMessage ?: "Erro ao realizar login.")
            }
    }

    fun signUp(
        name: String,
        cpf: String,
        email: String,
        pass: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (name.isBlank() || cpf.isBlank() || email.isBlank() || pass.isBlank()) {
            onFailure("Preencha todos os campos!")
            return
        }

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid
                if (uid != null) {
                    val userProfile = User(
                        uid = uid,
                        name = name.trim(),
                        cpf = cpf.trim(),
                        email = email.trim()
                    )

                    fbDatabase.saveUserProfile(userProfile) { success ->
                        if (success) {
                            onSuccess()
                        } else {
                            onFailure("Conta criada, mas houve um erro ao salvar o perfil.")
                        }
                    }
                } else {
                    onSuccess()
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception.localizedMessage ?: "Erro ao cadastrar usuário.")
            }
    }

    fun logout() {
        auth.signOut()
    }
}