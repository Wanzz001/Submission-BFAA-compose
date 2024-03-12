package org.d3if0080.bfaa_compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import org.d3if0080.bfaa_compose.response.ItemsItem
import org.d3if0080.bfaa_compose.response.ListFollowResponse
import org.d3if0080.bfaa_compose.database.FavoriteEntity

@Composable
fun UserItem(user: ItemsItem, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("detail/${user.login}") },
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter(model = user.avatarUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(
                        CircleShape
                    )
            )
            user.login?.let {
                Text(
                    text = it,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 7.dp)
                )
            }
        }
    }
}

@Composable
fun UserItem(user: ListFollowResponse, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("detail/${user.login}") },
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter(model = user.avatarUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(
                        CircleShape
                    )
            )
            user.login?.let {
                Text(
                    text = it,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 7.dp)
                )
            }
        }
    }
}

@Composable
fun UserItem(user: FavoriteEntity, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("detail/${user.login}") },
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter(model = user.avatarUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(
                        CircleShape
                    )
            )
            user.login?.let {
                Text(
                    text = it,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 7.dp)
                )
            }
        }
    }
}