package com.usj.midtermrachadsouaiby

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen(
                onTransactionHistoryClick = {
                    startActivity(Intent(this, TransactionHistoryActivity::class.java))
                },
                onSpendingAnalysisClick = {
                    startActivity(Intent(this, SpendingAnalysisActivity::class.java))
                }
            )
        }
    }
}

@Composable
fun MainScreen(
    onTransactionHistoryClick: () -> Unit,
    onSpendingAnalysisClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = onTransactionHistoryClick, modifier = Modifier.padding(8.dp)) {
            Text("View Transaction History", fontSize = 18.sp)
        }
        Button(onClick = onSpendingAnalysisClick, modifier = Modifier.padding(8.dp)) {
            Text("View Spending Analysis", fontSize = 18.sp)
        }
    }
}