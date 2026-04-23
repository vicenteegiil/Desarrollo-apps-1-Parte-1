package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class MainActivity : ComponentActivity() {

    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = AppDatabase.getInstance(this)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "submenu1") {
                    composable("submenu1") { Menu(navController) }
                    composable("submenu2") { Submenu1(navController) }
                    composable("lista") { Lista(navController,"CRUD Backend") }
                    composable("lista2") { Lista2(navController,"CRUD Local", db) }
                }
            }
        }
    }
}

@Composable
fun Submenu1(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(text = "Acerca de nosotros", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = "App de Estudiantes - Clase 5", modifier = Modifier.padding(bottom = 16.dp))

        val equipo = listOf(
            Pair("Agustin Blanco", R.drawable.agustin),
            Pair("Maximiliano Pagani", R.drawable.maximiliano),
            Pair("Joaquin Molina", R.drawable.joaquin),
            Pair("Lucas Marguets", R.drawable.lucas),
            Pair("Marso", R.drawable.marso),
            Pair("Vicente Gil", R.drawable.vicente)
        )

        equipo.forEach { (nombre, foto) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = foto),
                    contentDescription = nombre,
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = nombre, fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("submenu1") }) {
            Text("Ir al menu")
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Menu(navController: NavHostController) {
    var expanded by remember { mutableStateOf(false)}
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("App Estudiantes") },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                    Box{
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(text = { Text("Menu Principal") },  onClick = { expanded = false; navController.navigate("submenu1") })
                            DropdownMenuItem(text = { Text("Acerca de") },  onClick = { expanded = false; navController.navigate("submenu2") })
                            DropdownMenuItem(text = { Text("Lista Backend") },  onClick = { expanded = false; navController.navigate("lista") })
                            DropdownMenuItem(text = { Text("Lista Local (Room)") },  onClick = { expanded = false; navController.navigate("lista2") })
                        }
                    }
                })
        },
        content = {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Bienvenido al CRUD de Estudiantes", modifier = Modifier.padding(top = 80.dp))
            }
        }
    )
}

@Composable
fun Lista(navController: NavHostController, name: String, modifier: Modifier = Modifier) {
    val tasks = remember { mutableStateListOf<Estudiante>() }
    var nombreInput by remember { mutableStateOf("")}
    var apellidoInput by remember { mutableStateOf("")}
    var emailInput by remember { mutableStateOf("")}
    var editandoId by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) { Text(name, modifier = Modifier.padding(top = 40.dp))
        TextField(
            value = nombreInput,
            onValueChange = { nombreInput = it },
            label = { Text("Nombre") }
        )
        TextField(
            value = apellidoInput,
            onValueChange = { apellidoInput = it },
            label = { Text("Apellido") }
        )
        TextField(
            value = emailInput,
            onValueChange = { emailInput = it },
            label = { Text("Email") }
        )
        Button(onClick = {
                if (editandoId == null) {
                    ApiService.addStudentToApi(Estudiante("", nombreInput, apellidoInput, emailInput))
                    val aux = Estudiante("", nombreInput, apellidoInput, emailInput)
                    tasks.add(aux)
                } else {
                    ApiService.updateStudentInApi(editandoId!!, nombreInput, apellidoInput, emailInput)
                    val index = tasks.indexOfFirst { it.id == editandoId }
                    if (index != -1) {
                        tasks[index] = Estudiante(editandoId!!, nombreInput, apellidoInput, emailInput)
                    }
                    editandoId = null
                }
                nombreInput = ""
                apellidoInput = ""
                emailInput = ""
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text(if (editandoId == null) "Agregar a la lista backend" else "Modificar Estudiante")
        }
        LazyColumn(
            modifier = Modifier.padding(16.dp).weight(1f)
        ) {
            itemsIndexed(tasks) { index, task ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "${task.nombre} ${task.apellido}")
                        Text(text = task.email)
                    }
                    IconButton(onClick = {
                        nombreInput = task.nombre
                        apellidoInput = task.apellido
                        emailInput = task.email
                        editandoId = task.id
                    }) {
                        Icon(androidx.compose.material.icons.Icons.Default.Edit, contentDescription = "Editar estudiante")
                    }
                    IconButton(onClick = {  ApiService.deleteStudentFromApi(task.id) { success ->
                        if (success) {
                            tasks.remove(task)
                        } else {
                            showError("No se pudo eliminar al estudiante")
                        }
                    } }) {
                        Icon(Icons.Default.Delete, contentDescription = "Borrar estudiante")
                    }                
                }
            }
        }
        Button(onClick = {
            tasks.clear()
            ApiService.fetchStudentsFromApi {
                newTasks -> tasks.addAll(newTasks)
            }
        }) {
            Text("Conectar Backend")
        }

        Button(onClick = { navController.navigate("submenu1") }) {
            Text("Ir al menu")
        }

    }
}

@Composable
fun Lista2(navController: NavHostController, name: String, db: AppDatabase, modifier: Modifier = Modifier) {
    val tasks = remember { mutableStateListOf<EstudianteEntity>() }
    var nombreInput by remember { mutableStateOf("")}
    var apellidoInput by remember { mutableStateOf("")}
    var emailInput by remember { mutableStateOf("")}
    var editandoId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        val localTasks = withContext(Dispatchers.IO) {
            db.estudianteDao().getAll()
        }
        tasks.addAll(localTasks)
    }
    
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) { Text(name, modifier = Modifier.padding(top = 40.dp))
        TextField(
            value = nombreInput,
            onValueChange = { nombreInput = it },
            label = { Text("Nombre") }
        )
        TextField(
            value = apellidoInput,
            onValueChange = { apellidoInput = it },
            label = { Text("Apellido") }
        )
        TextField(
            value = emailInput,
            onValueChange = { emailInput = it },
            label = { Text("Email") }
        )
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                if (editandoId == null) {
                    val newTask = EstudianteEntity(nombre = nombreInput, apellido = apellidoInput, email = emailInput)
                    db.estudianteDao().insert(newTask)
                } else {
                    val updatedTask = EstudianteEntity(id = editandoId!!, nombre = nombreInput, apellido = apellidoInput, email = emailInput)
                    db.estudianteDao().update(updatedTask)
                }
                val updatedList = db.estudianteDao().getAll()
                withContext(Dispatchers.Main) {
                    tasks.clear()
                    tasks.addAll(updatedList)
                    editandoId = null
                    nombreInput = ""
                    apellidoInput = ""
                    emailInput = ""
                }
            }
        },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text(if (editandoId == null) "Agregar a BD Local" else "Actualizar Local")
        }
        LazyColumn(
            modifier = Modifier.padding(16.dp).weight(1f)
        ) {
            itemsIndexed(tasks) { index, task ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "${task.nombre} ${task.apellido}")
                        Text(text = task.email)
                    }
                    IconButton(onClick = {
                        nombreInput = task.nombre
                        apellidoInput = task.apellido
                        emailInput = task.email
                        editandoId = task.id
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar estudiante")
                    }
                    IconButton(onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            db.estudianteDao().delete(task)
                            withContext(Dispatchers.Main) {
                                tasks.remove(task)
                            }
                        }
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Borrar estudiante")
                    }
                }
            }
        }

        Button(onClick = { navController.navigate("submenu1") }) {
            Text("ir al menu")
        }

    }
}

private fun showError(message: String) {
    Log.d("Error conexion", message)
}