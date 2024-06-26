package com.alienmantech.azure_nebula.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import com.alienmantech.azure_nebula.ui.theme.DialogBackgroundColor
import com.alienmantech.azure_nebula.ui.theme.InputTextFieldBackgroundColor
import com.alienmantech.azure_nebula.ui.theme.InputTextFieldIndicatorColor
import com.alienmantech.azure_nebula.ui.theme.TextColorDefault

@Composable
fun StandardText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = TextColorDefault,
    fontSize: TextUnit = TextUnit.Unspecified,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        fontSize = fontSize,
        style = style,
    )
}

@Composable
fun StandardTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    labelText: String? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = TextFieldDefaults.shape,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = {
            if (labelText?.isNotEmpty() == true) {
                StandardText(
                    text = labelText,
                    color = TextColorDefault,
                )
            }
        },
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        prefix = prefix,
        suffix = suffix,
        supportingText = supportingText,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = capitalization,
            keyboardType = keyboardType
        ),
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource = interactionSource,
        shape = shape,
        colors = TextFieldDefaults.colors(
            focusedTextColor = TextColorDefault,
            unfocusedTextColor = TextColorDefault,
            focusedContainerColor = InputTextFieldBackgroundColor,
            unfocusedContainerColor = InputTextFieldBackgroundColor,
            cursorColor = TextColorDefault,
            focusedIndicatorColor = InputTextFieldIndicatorColor,
        ),
    )
}

@Composable
fun StandardAlertDialog(
    modifier: Modifier = Modifier,
    title: String,
    text: @Composable (() -> Unit)? = null,
    confirmButtonText: String = "OK",
    cancelButtonText: String = "Cancel",
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        title = { StandardText(text = title) },
        text = text,
        containerColor = DialogBackgroundColor,
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
                onDismissRequest()
            }) {
                StandardText(text = confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onCancel()
                onDismissRequest()
            }) {
                StandardText(text = cancelButtonText)
            }
        },
        onDismissRequest = onDismissRequest,
    )
}