package com.example.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.MyApplication
import com.example.myapplication.ui.screens.AboutUsScreen
import com.example.myapplication.ui.screens.CrudListScreen
import com.example.myapplication.ui.screens.MenuScreen
import com.example.myapplication.ui.viewmodel.StudentViewModel
import com.example.myapplication.ui.viewmodel.StudentViewModelFactory

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val application = context.applicationContext as MyApplication
    val viewModel: StudentViewModel = viewModel(
        factory = StudentViewModelFactory(application.repository)
    )

    NavHost(navController = navController, startDestination = "menu") {
        composable("menu") {
            MenuScreen(
                onNavigateToAboutUs = { navController.navigate("about_us") },
                onNavigateToCrud = { navController.navigate("crud_list") }
            )
        }
        composable("about_us") {
            AboutUsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("crud_list") {
            CrudListScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
