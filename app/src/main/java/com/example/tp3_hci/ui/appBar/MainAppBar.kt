package com.example.tp3_hci.ui.appBar

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.tp3_hci.ui.model.TopBarInfo
import com.example.tp3_hci.R
import com.example.tp3_hci.ui.model.OrderBy
import com.example.tp3_hci.util.getViewModelFactory

@Composable
fun MainAppBar(
    navController: NavHostController,
    onNavigateToLogin: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onOrderBy: (String) -> Unit,
    viewModel: MainAppBarViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = getViewModelFactory()),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    var currentRoute = backStackEntry?.destination?.route?: "home"
    if (currentRoute.contains("details")){
        currentRoute = "details"
    }
    var topBarInfoMap by remember { mutableStateOf( hashMapOf<String, TopBarInfo>(
        "home" to TopBarInfo("Home", true, false, false, false),
        "routines" to TopBarInfo("Routines", false, false, false, true),
        "explore" to TopBarInfo("Explore", false, false, false, true),
        "details" to TopBarInfo("Details", false, false, true, false),
        "settings" to TopBarInfo("Settings", false, false, true, false),
    )
    )
    }
    var currentTopBarInfo = topBarInfoMap.get(currentRoute)
    var showPopUp by remember { mutableStateOf(false) }
    var showPopUpOrder by remember { mutableStateOf(false) }
    var context = LocalContext.current

    if(currentTopBarInfo != null){
        TopAppBar(
            title = { Text(text = currentTopBarInfo!!.title, fontSize = 28.sp) },
            backgroundColor = MaterialTheme.colors.primary,
            actions = {
                if (currentTopBarInfo.hasSearch) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    }
                } else if (currentTopBarInfo.hasAvatar) {
                    Box(modifier = Modifier.padding(end=6.dp)){
                        Image(
                            painter = painterResource(R.drawable.bg),
                            contentDescription = "profile picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable { showPopUp = !showPopUp }
                        )
                    }
                    DropdownMenu(
                        expanded = showPopUp,
                        onDismissRequest = { showPopUp = false },
                        modifier = Modifier.background(MaterialTheme.colors.background)
                    ) {
                        DropdownMenuItem(onClick = { onNavigateToSettings() ; showPopUp = false }) {
                            Text(text = "Settings", modifier = Modifier.padding(end = 7.dp))
                            Icon(imageVector = Icons.Default.Settings, contentDescription = "settings")
                        }
                        DropdownMenuItem(onClick = { viewModel.logout() ; onNavigateToLogin() ; showPopUp = false ; Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()  }) {
                            Text(text = "Log out", modifier = Modifier.padding(end = 7.dp))
                            Icon(imageVector = Icons.Default.Logout, contentDescription = "logout", tint = Red)
                        }
                    }
                } else if (currentTopBarInfo.hasOrderBy) {
                    Row(
                        modifier = Modifier
                            .padding(end=6.dp)
                            .clickable { showPopUpOrder = !showPopUpOrder }
                    ){
                        Text(
                            text = "OrderBy",
                            modifier = Modifier
                                .padding(end = 6.dp)
                        )
                        Icon(
                            imageVector = Icons.Filled.SwapVert,
                            contentDescription = "orderBy"
                        )
                    }
                    DropdownMenu(
                        expanded = showPopUpOrder,
                        onDismissRequest = { showPopUpOrder = false },
                        modifier = Modifier.background(MaterialTheme.colors.background)
                    ) {
                        DropdownMenuItem(
                            onClick = { onOrderBy("date") ; showPopUpOrder = false })
                        {
                            Text(text = "Date", modifier = Modifier.padding(end = 5.dp))
                            Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "date")
                        }
                        DropdownMenuItem(
                            onClick = { onOrderBy("difficulty") ; showPopUpOrder = false }
                        ) {
                            Text(text = "Difficulty", modifier = Modifier.padding(end = 5.dp))
                            Icon(imageVector = Icons.Default.SupervisorAccount, contentDescription = "difficulty")
                        }
                        DropdownMenuItem(
                            onClick = { onOrderBy("score") ; showPopUpOrder = false }
                        ) {
                            Text(text = "Score", modifier = Modifier.padding(end = 5.dp))
                            Icon(imageVector = Icons.Default.TrendingUp, contentDescription = "score")
                        }
                        DropdownMenuItem(
                            onClick = { onOrderBy("category") ; showPopUpOrder = false }
                        ) {
                            Text(text = "Category", modifier = Modifier.padding(end = 5.dp))
                            Icon(imageVector = Icons.Default.Category, contentDescription = "category", tint = Black)
                        }
                    }
                }
            },
            navigationIcon = if (topBarInfoMap.get(currentRoute)!!.hasBackArrow) {
                {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            } else {null},
        )
    }
}