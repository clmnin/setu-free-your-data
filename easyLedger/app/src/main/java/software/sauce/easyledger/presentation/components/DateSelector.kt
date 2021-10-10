package software.sauce.easyledger.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DateSelector(
    date: String = "Oct 10",
    onClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = "Today, $date",
            style = MaterialTheme.typography.h2
        )
        Text(
            text = "Change the date to see the app in action!",
            style = MaterialTheme.typography.body1
        )
    }
}