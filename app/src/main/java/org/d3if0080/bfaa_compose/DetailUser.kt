package org.d3if0080.bfaa_compose

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import org.d3if0080.bfaa_compose.response.DetailUserResponse
import org.d3if0080.bfaa_compose.viewmodel.FollowersViewModel
import org.d3if0080.bfaa_compose.viewmodel.FollowingViewModel
import org.d3if0080.bfaa_compose.database.FavoriteEntity
import org.d3if0080.bfaa_compose.viewmodel.UserDetailViewModel

@Composable
fun DetailUser(
    user: DetailUserResponse,
    navController: NavController,
    detailViewModel: UserDetailViewModel,
    context: Context
) {
    val isFavorite = isFavorite(viewModel = detailViewModel, user = user)
    val followersViewModel = viewModel<FollowersViewModel>()
    val followingsViewModel = viewModel<FollowingViewModel>()
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    TopBarDetail(navController = navController) { modifier ->
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                FloatingActionButton(
                    onClick = {
                        if (isFavorite) {
                            user.id?.let { detailViewModel.delete(it) }
                            Toast.makeText(
                                context,
                                "User has been unfavorited.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val favoriteUser = FavoriteEntity(
                                id = user.id,
                                login = user.login,
                                avatarUrl = user.avatarUrl
                            )
                            detailViewModel.insert(favoriteUser)
                            Toast.makeText(context, "User has been favorited.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    shape = CircleShape, modifier = modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 10.dp)
                ) {
                    if (isFavorite)
                        Icon(imageVector = Icons.Default.Favorite, contentDescription = "favorite")
                    else Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "unfavorite"
                    )
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = rememberAsyncImagePainter(model = user.avatarUrl),
                        contentDescription = "Pictures",
                        modifier = Modifier
                            .size(200.dp)
                            .clip(
                                CircleShape
                            )
                    )
                    user.name?.let {
                        Text(
                            text = it,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(10.dp),
                            fontWeight = FontWeight.Bold
                        )
                    } ?: Text(
                        text = "-",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(10.dp),
                        fontWeight = FontWeight.Bold
                    )
                    user.login?.let { Text(text = it, fontSize = 15.sp) }
                }
            }


            Row(modifier = Modifier.padding(8.dp)) {
                Column(modifier = Modifier.padding(end = 16.dp)) {
                    Text(text = stringResource(id = R.string.company), fontSize = 14.sp)
                    Text(text = stringResource(id = R.string.blog), fontSize = 14.sp)
                    Text(text = stringResource(id = R.string.location), fontSize = 14.sp)
                    Text(text = stringResource(id = R.string.email), fontSize = 14.sp)
                    Text(text = stringResource(id = R.string.repositories), fontSize = 14.sp)
                }
                Column {
                    user.company?.let { Text(text = it, fontSize = 14.sp) } ?: Text(
                        text = "-",
                        fontSize = 14.sp
                    )
                    user.blog?.let { Text(text = it, fontSize = 14.sp) } ?: Text(
                        text = "-",
                        fontSize = 14.sp
                    )
                    user.location?.let { Text(text = it, fontSize = 14.sp) } ?: Text(
                        text = "-",
                        fontSize = 14.sp
                    )
                    user.email?.let { Text(text = it, fontSize = 14.sp) } ?: Text(
                        text = "-",
                        fontSize = 14.sp
                    )
                    user.publicRepos?.toString()?.let { Text(text = it, fontSize = 14.sp) }
                        ?: Text(text = "-", fontSize = 14.sp)
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    user.followers?.toString()?.let { Text(text = it, fontSize = 30.sp) }
                    Text(text = stringResource(R.string.followers), fontSize = 14.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    user.following?.toString()?.let { Text(text = it, fontSize = 30.sp) }
                    Text(text = stringResource(R.string.followings), fontSize = 14.sp)
                }
            }
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {

                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Followers") }
                )

                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Followings") }
                )
            }
            when (selectedTabIndex) {
                0 -> {
                    user.login?.let { followersViewModel.followers(LocalContext.current, it) }
                    FollowersList(listFollower = followersViewModel, navController)
                }

                1 -> {
                    user.login?.let { followingsViewModel.following(LocalContext.current, it) }
                    FollowingsList(followingsViewModel, navController)
                }
            }
        }
    }
}

@Composable
fun FollowersList(listFollower: FollowersViewModel, navController: NavController) {
    val isLoading by listFollower.getIsLoading.observeAsState(initial = false)
    val followers by listFollower.getFollowers.observeAsState(initial = emptyList())

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        } else {
            LazyColumn {
                items(followers) { user ->
                    UserItem(user = user, navController)
                }
            }
        }
    }
}

@Composable
fun FollowingsList(listFollowing: FollowingViewModel, navController: NavController) {
    val isLoading by listFollowing.getIsLoading.observeAsState(initial = false)
    val followers by listFollowing.getFollowing.observeAsState(initial = emptyList())

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        } else {
            LazyColumn {
                items(followers) { user ->
                    UserItem(user = user, navController)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarDetail(
    navController: NavController,
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.detail_user)) },
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

@Composable
fun isFavorite(viewModel: UserDetailViewModel, user: DetailUserResponse): Boolean {
    val favoriteList by viewModel.getAllFavorites().observeAsState()
    var isFavorite = false

    favoriteList?.let { list ->
        for (data in list) {
            if (user.id == data.id) {
                isFavorite = true
                break
            }
        }
    }

    return isFavorite
}




