package com.abir.androidbasicpart1.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.abir.androidbasicpart1.composables.DataValueField
import com.abir.androidbasicpart1.composables.ExpandableSection
import com.abir.androidbasicpart1.data.apiDataMap
import com.abir.androidbasicpart1.viewmodels.UserViewModel

@Composable
fun ApiIntegrationScreen(userViewModel: UserViewModel = viewModel()) {
    var selectedMethod by remember { mutableStateOf("Default") }
    var expandedRequest by remember { mutableStateOf(false) }
    var expandedResponse by remember { mutableStateOf(false) }

    // Fetch the data for the selected method
    val apiData = apiDataMap[selectedMethod] ?: return
    val userData by remember { mutableStateOf(apiData) }

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp, vertical = 60.dp)) {

        // HTTP Method Tabs
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("GET", "POST", "PUT", "DELETE").forEach { method ->
                    Button(
                        onClick = {
                            selectedMethod = method
                            // Make API call on button click, and store the response
                            when (method) {
                                "GET" -> userViewModel.getUsers()
                                "POST" -> userViewModel.newUserConversation("1003")
                                "PUT" -> userViewModel.updateConversation("1002")
                                "DELETE" -> userViewModel.deleteConversation("1003")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedMethod == method) Color.Blue else Color.LightGray,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = method)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (selectedMethod != "Default") {
            userData.status = userViewModel.code.toString()
            userData.responseBody = userViewModel.responseBody.toString()
            userData.responseHeaders = userViewModel.responseHeaders.toString()
            userData.cookies = userViewModel.cookies.toString()
        }
        // Expandable Request Section
        item {
            ExpandableSection(
                title = "Request",
                isExpanded = expandedRequest,
                onExpandChange = { expandedRequest = !expandedRequest }
            ) {
                DataValueField(label = "URL", value = apiData.url)
                DataValueField(label = "Params", value = apiData.params)
                DataValueField(label = "Header", value = apiData.headers)
                DataValueField(label = "Body", value = apiData.body)
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Expandable Response Section
        item {
            ExpandableSection(
                title = "Response",
                isExpanded = expandedResponse,
                onExpandChange = { expandedResponse = !expandedResponse }
            ) {
                DataValueField(label = "Status", value = userData.status)
                DataValueField(label = "Body", value = userData.responseBody)
                DataValueField(label = "Header", value = userData.responseHeaders)
                DataValueField(label = "Cookies", value = userData.cookies)
            }
        }
    }
}

