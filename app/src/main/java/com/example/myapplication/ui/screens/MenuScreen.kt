package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    onNavigateToAboutUs: () -> Unit,
    onNavigateToCrud: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Gestor de Estudiantes", 
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Header Icon or Text
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    Text(
                        text = "¡Bienvenido al Gestor!",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)
                    )
                }

                ElevatedButton(
                    onClick = onNavigateToCrud,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Students",
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Gestiónar estudiantes",
                        fontSize = 18.sp, 
                        fontWeight = FontWeight.Bold
                    )
                }
                
                ElevatedButton(
                    onClick = onNavigateToAboutUs,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Acerca de Nosotros", 
                        fontSize = 18.sp, 
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
