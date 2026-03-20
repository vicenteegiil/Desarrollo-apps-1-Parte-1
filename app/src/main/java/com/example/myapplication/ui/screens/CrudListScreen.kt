package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.data.model.Student
import com.example.myapplication.ui.viewmodel.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrudListScreen(
    viewModel: StudentViewModel,
    onNavigateBack: () -> Unit
) {
    val students by viewModel.students.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    var editingStudent by remember { mutableStateOf<Student?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Lista de Estudiantes",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    editingStudent = null
                    showDialog = true
                },
                icon = { Icon(Icons.Filled.Add, contentDescription = "Añadir") },
                text = { Text("Nuevo Estudiante") },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
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
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        ) {
            if (students.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "No hay estudiantes registrados.",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 88.dp)
                ) {
                    items(students) { student ->
                        StudentItem(
                            student = student,
                            onEdit = {
                                editingStudent = it
                                showDialog = true
                            },
                            onDelete = { viewModel.deleteStudent(it) }
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        StudentDialog(
            student = editingStudent,
            onDismiss = { showDialog = false },
            onSave = { name, email, course ->
                if (editingStudent == null) {
                    viewModel.addStudent(name, email, course)
                } else {
                    viewModel.updateStudent(editingStudent!!.copy(name = name, email = email, course = course))
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun StudentItem(student: Student, onEdit: (Student) -> Unit, onDelete: (Student) -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circle Avatar
            Surface(
                shape = androidx.compose.foundation.shape.CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = student.name.take(1).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = student.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = student.course, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                Text(text = student.email, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            IconButton(onClick = { onEdit(student) }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.secondary)
            }
            IconButton(onClick = { onDelete(student) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDialog(
    student: Student?,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(student?.name ?: "") }
    var email by remember { mutableStateOf(student?.email ?: "") }
    var course by remember { mutableStateOf(student?.course ?: "") }

    var emailError by remember { mutableStateOf(false) }
    
    val courses = listOf("Matemática", "Programación", "Física", "Química", "Historia", "Inglés")
    var expanded by remember { mutableStateOf(false) }

    val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isFormValid = name.isNotBlank() && isEmailValid && course.isNotBlank() && courses.contains(course)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (student == null) "Añadir Estudiante" else "Editar Estudiante", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre Completo") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { 
                        email = it
                        emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()
                    },
                    label = { Text("Correo Electrónico") },
                    isError = emailError,
                    supportingText = { if (emailError) Text("Correo inválido") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = course,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Curso / Materia") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        courses.forEach { selectedCourse ->
                            DropdownMenuItem(
                                text = { Text(selectedCourse) },
                                onClick = {
                                    course = selectedCourse
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isFormValid) {
                        onSave(name, email, course)
                    }
                },
                enabled = isFormValid,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
