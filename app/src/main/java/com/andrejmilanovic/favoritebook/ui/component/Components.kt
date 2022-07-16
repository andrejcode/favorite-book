package com.andrejmilanovic.favoritebook.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InputField(
    valueState: MutableState<String>,
    leadingIcon: @Composable() (() -> Unit)?,
    label: String,
    modifier: Modifier,
    enabled: Boolean,
    singleLine: Boolean,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    visualTransformation: VisualTransformation,
    trailingIcon: @Composable() (() -> Unit)?
) {
    OutlinedTextField(
        value = valueState.value,
        leadingIcon = leadingIcon,
        onValueChange = { valueState.value = it },
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            .fillMaxWidth(),
        enabled = enabled,
        label = { Text(text = label) },
        singleLine = singleLine,
        textStyle = TextStyle(fontSize = 18.sp),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon
    )
}

@Composable
fun RoundedButton(label: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(bottomEndPercent = 20, topStartPercent = 50))
            .clickable { onClick() },
        color = MaterialTheme.colors.primary
    ) {
        Text(text = label, modifier = Modifier.padding(16.dp))
    }
}