package com.example.llc.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.llc.R

@Composable
fun InitContent(
    count: Int,
    buttonLoading: Boolean,
    onCountChange: (String) -> Unit,
    onGoClick: () -> Unit
) = Column(
    modifier = Modifier.padding(16.dp)
) {
    Text(text = stringResource(R.string.input_text))
    Spacer(Modifier.height(16.dp))
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = count.toString(),
        onValueChange = onCountChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
    Spacer(Modifier.height(16.dp))
    Button(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth(),
        onClick = onGoClick
    ) {
        if (buttonLoading) {
            CircularProgressIndicator(color = MaterialTheme.colors.secondary)
        } else {
            Text(text = stringResource(R.string.button_go))
        }
    }
}