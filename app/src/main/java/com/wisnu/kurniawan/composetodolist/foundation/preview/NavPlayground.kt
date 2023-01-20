package com.wisnu.kurniawan.composetodolist.foundation.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.guide
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.guide2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Composable
fun NavPg() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "root"
    ) {
        composable(route = "root") {
            View1(navController = navController)
        }
    }
}

@Composable
fun View1(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("View 1")

        val navController1 = rememberNavController()
        val navController2 = rememberNavController()

        NavHost(
            navController = navController1,
            startDestination = "root1",
            modifier = Modifier.weight(1f).guide2()
        ) {
            composable(route = "root1") {
                val vm = hiltViewModel<Vm1>()
                View2(navController = navController1, navController2, vm)
            }
            composable(route = "root1a") {
                View3(navController = navController1)
            }
        }

        NavHost(
            navController = navController2,
            startDestination = "root2",
            modifier = Modifier.weight(1f).guide()
        ) {
            composable(route = "root2") {
                val vm = hiltViewModel<Vm2>()
                View4(navController = navController2, vm)
            }
            composable(route = "root2a") {
                val vm = hiltViewModel<Vm2>()

                View5(navController = navController2, vm)
            }
        }

    }
}

@Composable
fun View2(navController: NavController, navController2: NavController, vm1: Vm1) {
    Column(
        modifier = Modifier.fillMaxSize().padding(50.dp)
    ) {
        val state = vm1.data1.state.collectAsStateWithLifecycle()
        Text("View 2 ${state.value}")
        Button(
            {
                // navController.navigate("root1a")
                navController2.navigate("root2a")
            }
        ) {
            Text("Button")
        }

        Button(
            {
                vm1.inc()
            }
        ) {
            Text("Button2")
        }
    }
}

@Composable
fun View3(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("View 3")
        Button(
            {

            }
        ) {
            Text("Button")
        }
    }
}

@Composable
fun View4(navController: NavController, vm2: Vm2) {
    Column(
        modifier = Modifier.fillMaxSize().padding(50.dp)
    ) {
        val state = vm2.data1.state.collectAsStateWithLifecycle()
        Text("View 4 ${state.value}")
        Button(
            {
                navController.navigate("root2a")
            }
        ) {
            Text("Button")
        }

        Button(
            {
                vm2.inc()
            }
        ) {
            Text("Button2")
        }
    }
}

@Composable
fun View5(navController: NavController, vm2: Vm2) {
    val state = vm2.data1.state.collectAsStateWithLifecycle()
    Text("View 5 ${state.value}")

    Button(
        {
            vm2.inc()
        }
    ) {
        Text("Button inc")
    }
}

@HiltViewModel
class Vm1 @Inject constructor(val data1: Data1) : ViewModel() {
    fun inc() {
        viewModelScope.launch {
            data1.inc()
        }
    }
}

@HiltViewModel
class Vm2 @Inject constructor(val data1: Data1) : ViewModel() {

    fun inc() {
        viewModelScope.launch {
            data1.inc()
        }
    }
}

@Singleton
class Data1 @Inject constructor() {
    private val _state = MutableStateFlow(0)
    val state: StateFlow<Int> = _state.asStateFlow()
    suspend fun inc() {
        _state.emit(state.value + 1)
    }

}
