package org.d3if0080.bfaa_compose

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.d3if0080.bfaa_compose.settings.SettingPreference
import org.d3if0080.bfaa_compose.ui.theme.BFAAcomposeTheme
import org.d3if0080.bfaa_compose.viewmodel.FavoriteViewModel
import org.d3if0080.bfaa_compose.viewmodel.SearchUserViewModel
import org.d3if0080.bfaa_compose.viewmodel.SettingViewModel
import org.d3if0080.bfaa_compose.viewmodel.UserDetailViewModel

class MainActivity : ComponentActivity() {
    private val listViewModel by lazy { ViewModelProvider(this)[SearchUserViewModel::class.java] }
    private val detailViewModel by lazy { ViewModelProvider(this)[UserDetailViewModel::class.java] }
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            BFAAcomposeTheme(darkTheme = isDarkMode(dataStore = dataStore)) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "splashscreen") {
                        composable("splashscreen") {
                            SplashScreen(navController)
                        }
                        composable("home") {
                            if (savedInstanceState != null) {
                                listViewModel.restoreState()
                            } else {
                                listViewModel.searchUser(context, "wanzz")
                            }
                            ListUser(navController = navController, listViewModel)
                        }
                        composable("detail/{username}") { backStackEntry ->
                            val userName = backStackEntry.arguments?.getString("username") ?: ""
                            detailViewModel.detailUser(context, userName)
                            ToDetail(detailViewModel, navController, context)
                        }
                        composable("setting") {
                            SettingScreen(dataStore, navController)
                        }
                        composable("favoriteList") {
                            FavoriteList(
                                navController = navController,
                                favoriteList = favoriteViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopBar(
    listViewModel: SearchUserViewModel,
    navController: NavController,
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        topBar = {
            SearchTopBar(listViewModel, navController)
        }
    ) { padding ->
        content(Modifier.padding(padding))
    }
}


@Composable
fun ToDetail(detailViewModel: UserDetailViewModel, navController: NavController, context: Context) {
    val userDetail = detailViewModel.userDetail
    val isLoading = detailViewModel.isLoading
    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        } else {
            userDetail?.let { user ->
                DetailUser(user = user, navController, detailViewModel, context)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(listViewModel: SearchUserViewModel, navController: NavController) {
    var searchVisible by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    val context = LocalContext.current

    TopAppBar(
        title = {
            if (searchVisible) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { searchVisible = false },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                    TextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                            listViewModel.searchUser(context, it)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            searchVisible = false
                        }),
                        placeholder = { Text(stringResource(R.string.placeholder_search_user)) }
                    )
                }
            } else {
                Text(text = stringResource(R.string.top_bar_name))
            }
        },
        actions = {
            if (!searchVisible) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    IconButton(onClick = { searchVisible = true }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                    IconButton(onClick = { navController.navigate("setting") }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            }
        }
    )
}


@Composable
fun ListUser(navController: NavController, listViewModel: SearchUserViewModel) {
    val isLoading by listViewModel.isLoading.observeAsState(initial = false)
    val searchList by listViewModel.searchList.observeAsState(initial = emptyList())

    DisposableEffect(key1 = listViewModel) {
        listViewModel.restoreState()
        onDispose {
            listViewModel.saveState()
        }
    }

    TopBar(listViewModel, navController) { modifier ->
        Box(modifier = modifier.fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            } else {
                LazyColumn {
                    items(searchList ?: emptyList()) { user ->
                        UserItem(user = user, navController)
                    }
                }
                FloatingActionButton(
                    onClick = { navController.navigate("favoriteList") },
                    modifier
                        .align(Alignment.BottomEnd)
                        .padding(24.dp), shape = CircleShape
                ) {
                    Icon(imageVector = Icons.Default.Favorite, contentDescription = "favorite")
                }
            }
        }
    }
}

@Composable
fun isDarkMode(dataStore: DataStore<Preferences>): Boolean {
    val pref = SettingPreference.getInstance(dataStore)
    val viewModel = SettingViewModel(pref)

    val isDarkModeActive by viewModel.getThemeSettings().observeAsState(isSystemInDarkTheme())
    return isDarkModeActive
}


