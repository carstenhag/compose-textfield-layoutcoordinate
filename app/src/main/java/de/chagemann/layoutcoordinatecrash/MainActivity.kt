package de.chagemann.layoutcoordinatecrash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import de.chagemann.layoutcoordinatecrash.ui.theme.LayoutCoordinateCrashTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                android.graphics.Color.WHITE,
                android.graphics.Color.BLACK,
                detectDarkMode = { false }
            ),
            navigationBarStyle = SystemBarStyle.auto(
                getColor(R.color.white),
                getColor(R.color.black),
                detectDarkMode = { false }
            )
        )

        setContent {
            LayoutCoordinateCrashTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.statusBarsPadding(),
                        verticalArrangement = Arrangement.spacedBy(50.dp)
                    ) {
                        Text(text = "1. Type in something in the text field\n2. Delete all input\n3.Lose focus of the view\n4. Focus it again - 💥")
                        // more or less copied from our code
                        PhoneInputTextField()


                        val secondTextFieldString = remember { mutableStateOf("") }
                        // very basic example that has the same issue
                        BasicTextField(
                            value = secondTextFieldString.value,
                            onValueChange = {
                                secondTextFieldString.value = it
                            },
                            modifier = Modifier.background(Color.Gray),
                            decorationBox = { innerTextField ->
                                if (secondTextFieldString.value.isEmpty()) {
                                    Text(text = "placeholder 2", color = Color.Black.copy(alpha = 0.4f))
                                } else {
                                    innerTextField()
                                }
                            }
                        )

                    }
                }
            }
        }
    }
}

@Composable
fun PhoneInputTextField() {
    var formattedNumber by remember { mutableStateOf("") }
    var textSelectionRange by remember { mutableStateOf(TextRange(formattedNumber.length)) }

    BasicTextField(
        value = TextFieldValue(
            text = formattedNumber,
            selection = textSelectionRange
        ),
        onValueChange = { textFieldValue: TextFieldValue ->
            val input = textFieldValue.text

            if (input.isDigitsOnly()) {
                formattedNumber = input
                textSelectionRange = textFieldValue.selection
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone
        ),
        singleLine = true,
        decorationBox = { innerTextField ->
            if (formattedNumber.isEmpty()) {
                Text(text = "placeholder 1", color = Color.Black.copy(alpha = 0.4f))
            } else {
                innerTextField()
            }
        }
    )
}

