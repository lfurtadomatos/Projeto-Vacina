package com.br.projetodevacina.db

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FBAuth {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

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
        email: String,
        pass: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (email.isBlank() || pass.isBlank()) {
            onFailure("Preencha todos os campos!")
            return
        }

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception ->
                onFailure(exception.localizedMessage ?: "Erro ao cadastrar usuário.")
            }
    }

    fun logout() {
        auth.signOut()
    }
}