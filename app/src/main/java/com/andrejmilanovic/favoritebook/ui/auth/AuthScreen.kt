package com.andrejmilanovic.favoritebook.ui.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.andrejmilanovic.favoritebook.R
import com.andrejmilanovic.favoritebook.ui.component.InputField
import com.andrejmilanovic.favoritebook.util.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun AuthScreen(navigateToHomeScreen: () -> Unit) {
    val auth: FirebaseAuth = Firebase.auth

    // Navigate to home screen if user is already logged in
    LaunchedEffect(Unit) {
        if (auth.currentUser != null) {
            navigateToHomeScreen()
        }
    }

    val showLoginFrom = rememberSaveable { mutableStateOf(true) }
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // This Text element is supposed to be a logo
            Text(
                text = stringResource(id = R.string.app_name),
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 64.dp,
                    end = 16.dp,
                    bottom = 32.dp
                ),
                style = MaterialTheme.typography.h2
            )
            /* Show login form and sign user in when submit button is clicked if correct email
             * and password are entered
             */
            if (showLoginFrom.value) UserForm { email, password ->
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navigateToHomeScreen()
                        } else {
                            showToast(context, R.string.unable_to_login)
                        }
                    }
            }
            else UserForm(isCreateAccount = true) { email, password ->
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navigateToHomeScreen()
                        } else {
                            showToast(context, R.string.unable_to_create_account)
                        }
                    }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Change login/signup text depending on user action
                Text(
                    text = if (showLoginFrom.value) stringResource(id = R.string.new_user)
                    else stringResource(id = R.string.already_have_account)
                )
                Text(
                    text = if (showLoginFrom.value) stringResource(id = R.string.signup)
                    else stringResource(id = R.string.login),
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clickable {
                            showLoginFrom.value = !showLoginFrom.value
                        },
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary
                )
            }
        }
    }
}

@Composable
fun UserForm(
    isCreateAccount: Boolean = false,
    onSubmit: (String, String) -> Unit = { _, _ -> }
) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisible = rememberSaveable { mutableStateOf(false) }
    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }
    val focusManager = LocalFocusManager.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isCreateAccount) Text(
            text = stringResource(id = R.string.enter_valid_email_and_password),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 4.dp)
        )
        // OutlinedTextField used for email input
        InputField(
            valueState = email,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = stringResource(id = R.string.email)
                )
            },
            label = stringResource(id = R.string.email),
            modifier = Modifier,
            enabled = true,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            visualTransformation = VisualTransformation.None,
            trailingIcon = null
        )
        // OutlinedTextField used for password input
        InputField(
            valueState = password,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Password,
                    contentDescription = stringResource(id = R.string.password)
                )
            },
            label = stringResource(id = R.string.password),
            modifier = Modifier,
            enabled = true,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation()
        ) {
            val image = if (passwordVisible.value)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            val description =
                if (passwordVisible.value) stringResource(id = R.string.hide_password) else stringResource(
                    id = R.string.show_password
                )

            IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                Icon(imageVector = image, description)
            }
        }
        SubmitButton(
            text = if (isCreateAccount) stringResource(id = R.string.create_account)
            else stringResource(id = R.string.login),
            validInputs = valid
        ) {
            onSubmit(email.value.trim(), password.value.trim())
        }
    }
}

@Composable
fun SubmitButton(
    text: String,
    validInputs: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        enabled = validInputs,
        shape = CircleShape
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(4.dp)
        )
    }
}