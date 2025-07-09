package com.sakhura.formularioregistro.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationForm() {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    var nombreError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Crear Cuenta",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Completa los datos para registrarte",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                nombreError = validateName(it)
            },
            label = { Text("Nombre completo") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            isError = nombreError.isNotEmpty(),
            supportingText = if (nombreError.isNotEmpty()) {
                { Text(nombreError) }
            } else null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = validateEmail(it)
            },
            label = { Text("Correo electrónico") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = emailError.isNotEmpty(),
            supportingText = if (emailError.isNotEmpty()) {
                { Text(emailError) }
            } else null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = validatePassword(it)
                if (confirmPassword.isNotEmpty()) {
                    confirmPasswordError = validateConfirmPassword(password, confirmPassword)
                }
            },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        if (isPasswordVisible) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = passwordError.isNotEmpty(),
            supportingText = if (passwordError.isNotEmpty()) {
                { Text(passwordError) }
            } else null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                confirmPasswordError = validateConfirmPassword(password, it)
            },
            label = { Text("Confirmar contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                    Icon(
                        if (isConfirmPasswordVisible) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = if (isConfirmPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = confirmPasswordError.isNotEmpty(),
            supportingText = if (confirmPasswordError.isNotEmpty()) {
                { Text(confirmPasswordError) }
            } else null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                nombreError = validateName(nombre)
                emailError = validateEmail(email)
                passwordError = validatePassword(password)
                confirmPasswordError = validateConfirmPassword(password, confirmPassword)

                val isValid = listOf(nombreError, emailError, passwordError, confirmPasswordError).all { it.isEmpty() }

                if (isValid) {
                    isLoading = true
                    println("Registrando usuario: $nombre, $email")
                }
            },
            enabled = !isLoading && listOf(nombre, email, password, confirmPassword).all { it.isNotEmpty() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Crear Cuenta", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

// Funciones de validación

private fun validateName(nombre: String): String {
    return when {
        nombre.isBlank() -> "El nombre es requerido"
        nombre.length < 2 -> "El nombre debe tener al menos 2 caracteres"
        !nombre.matches(Regex("^[a-zA-ZÀ-ÿ\\s]+$")) -> "El nombre solo puede contener letras y espacios"
        else -> ""
    }
}

private fun validateEmail(email: String): String {
    return when {
        email.isBlank() -> "El correo electrónico es requerido"
        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Ingresa un correo electrónico válido"
        else -> ""
    }
}

private fun validatePassword(password: String): String {
    return when {
        password.isBlank() -> "La contraseña es requerida"
        password.length < 8 -> "La contraseña debe tener al menos 8 caracteres"
        !password.matches(Regex(".*[A-Z].*")) -> "La contraseña debe contener al menos una letra mayúscula"
        !password.matches(Regex(".*[a-z].*")) -> "La contraseña debe contener al menos una letra minúscula"
        !password.matches(Regex(".*\\d.*")) -> "La contraseña debe contener al menos un número"
        else -> ""
    }
}

private fun validateConfirmPassword(password: String, confirmPassword: String): String {
    return when {
        confirmPassword.isBlank() -> "Confirma tu contraseña"
        password != confirmPassword -> "Las contraseñas no coinciden"
        else -> ""
    }
}
