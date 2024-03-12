package org.d3if0080.bfaa_compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import org.d3if0080.bfaa_compose.viewmodel.FavoriteViewModel

@Composable
fun FavoriteList(navController: NavController, favoriteList: FavoriteViewModel) {
    val favoriteItemsState = favoriteList.getAllFavorites().observeAsState(initial = emptyList())
    val favoriteItems = favoriteItemsState.value

    TopBarFavorite(navController = navController) { modifier ->
        Box(modifier = modifier.fillMaxSize()) {
            LazyColumn {
                items(favoriteItems) { listItem ->
                    UserItem(user = listItem, navController = navController)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarFavorite(
    navController: NavController,
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.favorite_users)) },
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