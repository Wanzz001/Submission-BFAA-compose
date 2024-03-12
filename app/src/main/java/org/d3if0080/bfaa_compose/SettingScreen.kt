package org.d3if0080.bfaa_compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import org.d3if0080.bfaa_compose.settings.SettingPreference
import org.d3if0080.bfaa_compose.viewmodel.SettingViewModel


@Composable
fun SettingScreen(dataStore: DataStore<Preferences>, navController: NavHostController) {
    val pref = SettingPreference.getInstance(dataStore)
    val viewModel = SettingViewModel(pref)

    val isDarkModeActive by viewModel.getThemeSettings().observeAsState(isSystemInDarkTheme())

    SettingTopBar(navController = navController) {
        Box(
            contentAlignment = Alignment.Center, modifier = it
                .padding(8.dp)
                .fillMaxSize()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Light Mode")
                Switch(
                    checked = isDarkModeActive,
                    onCheckedChange = { viewModel.saveThemeSetting(!isDarkModeActive) },
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
                Text(text = "Dark Mode")
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingTopBar(navController: NavController, content: @Composable (Modifier) -> Unit) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        content(Modifier.padding(padding))
    }
}