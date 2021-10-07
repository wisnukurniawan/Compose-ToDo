package com.wisnu.kurniawan.composetodolist.foundation.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.guide
import com.wisnu.kurniawan.composetodolist.foundation.uiextension.guide2

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
                View2(navController = navController1, navController2)
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
                View4(navController = navController2)
            }
            composable(route = "root2a") {
                View5(navController = navController2)
            }
        }

    }
}

@Composable
fun View2(navController: NavController, navController2: NavController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("View 2")
        Button(
            {
                // navController.navigate("root1a")
                navController2.navigate("root2a")
            }
        ) {
            Text("Button")
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
fun View4(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("View 4")
        Button(
            {
                navController.navigate("root2a")
            }
        ) {
            Text("Button")
        }
    }
}

@Composable
fun View5(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("View 5")
        Button(
            {

            }
        ) {
            Text("Button")
        }
    }
}
