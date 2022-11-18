package com.example.tp3_hci.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.PreferenceManager
import com.example.tp3_hci.R
import com.example.tp3_hci.components.LittleCard
import com.example.tp3_hci.components.RoutineCard
import com.example.tp3_hci.data.model.Routine
import com.example.tp3_hci.ui.home.HomeViewModel
import com.example.tp3_hci.ui.home.canGetFavouriteRoutines
import com.example.tp3_hci.ui.home.canGetRoutines
import com.example.tp3_hci.ui.theme.Grey2
import com.example.tp3_hci.util.getViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onNavigateToRoutineDetails: (id:Int) -> Unit,
    onNavigateToExecution: (id:Int) -> Unit,
    onNavigateToLogin: () -> Unit,
    onPopStack: (route: String) -> Unit,
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = getViewModelFactory())
) {
    val uiState = viewModel.uiState
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    var imgIds by remember { mutableStateOf(listOf(R.drawable.routine1, R.drawable.routine2, R.drawable.routine3, R.drawable.routine4, R.drawable.routine5, R.drawable.routine6, R.drawable.routine7, R.drawable.routine8 )) }

    var preferences = PreferenceManager.getDefaultSharedPreferences(LocalContext.current)

    var suggestionsEnabled by remember { mutableStateOf(preferences.getBoolean("suggestions_enabled",true)) }

    LaunchedEffect(key1 = Unit) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.CREATED) {
            launch {
                //onPopStack("login")
                if (!uiState.isAuthenticated)
                    onNavigateToLogin()
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        launch {
            if (uiState.canGetRoutines)
                viewModel.getRoutines("date")
        }
    }

    LaunchedEffect(key1 = Unit) {
        launch {
            if (uiState.canGetFavouriteRoutines)
                viewModel.getFavouriteRoutines()
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.home_subtitle),
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 20.dp),
            style = MaterialTheme.typography.h6.copy(
                fontWeight = FontWeight(500)
            )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                if (!uiState.isFetching && uiState.routines.orEmpty().isNotEmpty()){
                    var latestRoutine = uiState.routines?.get(uiState.routines.size-1)
                    RoutineCard(
                        name = latestRoutine?.name!!,
                        description = latestRoutine?.detail!!,
                        id = latestRoutine?.id!!,
                        imgId = R.drawable.routine6,
                        onNavigateToRoutineDetails = onNavigateToRoutineDetails,
                        onNavigateToExecution = onNavigateToExecution
                    )
                }

            }
            Text(
                text = stringResource(R.string.home_favourites),
                modifier = Modifier
                    .padding(12.dp),
                style = MaterialTheme.typography.h6.copy(
                    fontWeight = FontWeight(500)
                )
            )

            val favourites = uiState.favourites.orEmpty()
            if (favourites.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    item { Spacer(modifier = Modifier.padding(4.dp)) }
                    items(
                        count = favourites.size ?: 0,
                        key = { index ->
                            favourites.get(index)?.id.toString()
                        }
                    ) { index ->
                        LittleCard(
                            name = favourites.get(index)?.name ?: "Error",
                            id = favourites.get(index)?.id!!,
                            imgId = imgIds[index],
                            onNavigateToRoutineDetails = onNavigateToRoutineDetails,
                        )
                    }
                    item { Spacer(modifier = Modifier.padding(4.dp)) }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                    ){
                    Text(
                        text = stringResource(id = R.string.empty_favourites),
                        modifier = Modifier
                            .padding(12.dp),
                        style = MaterialTheme.typography.h3.copy(
                            fontWeight = FontWeight(300),
                            fontSize = 20.sp,
                            color = Grey2
                        )
                    )
                }
            }
        }
        if (suggestionsEnabled){
            Text(
                text = stringResource(R.string.home_suggested),
                modifier = Modifier
                    .padding(12.dp),
                style = MaterialTheme.typography.h6.copy(
                    fontWeight = FontWeight(500)
                )
            )
            val suggested = uiState.routines.orEmpty().sortedBy { it.date }.reversed().take(3)
            if (suggested.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    item { Spacer(modifier = Modifier.padding(4.dp)) }
                    items(
                        count = suggested.size ?: 0,
                        key = { index ->
                            suggested.get(index)?.id.toString()
                        }
                    ) { index ->
                        LittleCard(
                            name = suggested.get(index)?.name ?: "Error",
                            id = suggested.get(index)?.id!!,
                            imgId = imgIds[(index+4)%8],
                            onNavigateToRoutineDetails = onNavigateToRoutineDetails,
                        )
                    }
                    item { Spacer(modifier = Modifier.padding(4.dp)) }
                }
                Spacer(modifier = Modifier.size(20.dp))
            }
        }
    }
}