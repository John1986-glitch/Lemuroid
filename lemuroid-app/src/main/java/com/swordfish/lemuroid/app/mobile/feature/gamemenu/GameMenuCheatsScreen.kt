
package com.swordfish.lemuroid.app.mobile.feature.gamemenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GameMenuCheatsScreen(viewModel: GameMenuCheatsViewModel) {
    val cheats by viewModel.cheats.collectAsState()

    var cheatName by remember { mutableStateOf("") }
    var cheatCode by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Game Cheats")

        OutlinedTextField(
            value = cheatName,
            onValueChange = { cheatName = it },
            label = { Text("Cheat name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )

        OutlinedTextField(
            value = cheatCode,
            onValueChange = { cheatCode = it },
            label = { Text("Cheat code") },
            modifier = Modifier.fillMaxWidth(),
        )

        Button(
            onClick = {
                viewModel.addCheat(
                    cheatName,
                    cheatCode,
                )
                cheatName = ""
                cheatCode = ""
            },
            enabled = (
                cheatName.isNotBlank() &&
                    cheatCode.isNotBlank()
                ),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Add Cheat")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Saved Cheats (${cheats.size})")

        if (cheats.isEmpty()) {
            Text("No cheats added yet.")
        }

        cheats.forEachIndexed { index, cheat ->
            Card(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(cheat.name)
                    Text(cheat.code)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text("Enabled")
                            Switch(
                                checked = cheat.enabled,
                                onCheckedChange = {
                                    viewModel.setCheatEnabled(index, it)
                                },
                            )
                        }

                        TextButton(
                            onClick = {
                                viewModel.deleteCheat(index)
                            },
                        ) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}
