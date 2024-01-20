package cl.nvh.estudio.app_iplabank

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost


import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.nvh.estudio.app_iplabank.ui.ListaRegistrosViewModel
import cl.nvh.estudio.app_iplabank.ui.theme.App_iplabankTheme
import cl.nvh.estudio.app_iplabank.ui.theme.btnColor
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import java.io.File
import java.util.concurrent.Executor
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.nvh.estudio.app_iplabank.dataBase.Registro
import java.time.LocalDate


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App_iplabankTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppInicio()
                }
            }
        }
    }
}

@Composable
fun AppInicio(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = "Login"
    ) {
        composable("Login") {
            LoginUI(
                onButtonSolicitar = {
                    navController.navigate("solicitud")
                }
            )
        }
        composable("solicitud") {
            RegistroUI(
                onBackButtonClicked = {
                    navController.popBackStack()
                },
                onButtonFoto = {
                    navController.navigate("Fotografia")
                }
            )
        }
        composable("Fotografia") {
            FotosUI(
                onBackButtonClicked = {
                    navController.popBackStack()
                }
            )
        }
    }
    UbicacionUI()
}

@Preview(showSystemUi = true, locale = "es")
@Composable
fun LoginUI(
    onButtonSolicitar: () -> Unit = {},
    vm: viewModel = viewModel()
) {

    val usuario: String by vm.usuario.observeAsState(initial = "")
    val password: String by vm.password.observeAsState(initial = "")
    val placeholderUser = "Usuario"
    val placeholderPassword = "Contraseña"

    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "IplaBank", fontWeight = FontWeight.Bold, fontSize = 30.sp
            )
            TextField(
                value = usuario,
                onValueChange = { vm.onLoginChanged(it, password) },
                placeholder = {
                    Text(placeholderUser)
                },
                singleLine = true,
                maxLines = 1
            )
            TextField(
                value = password,
                onValueChange = { vm.onLoginChanged(usuario, it) },
                placeholder = {
                    Text(placeholderPassword)
                },
                singleLine = true,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(15.dp))
            Button(
                onClick = { vm.limpiar() },
                colors = ButtonDefaults.buttonColors(containerColor = btnColor)
            ) {
                Text(text = "Ingresar")
            }
            Button(
                onClick = { onButtonSolicitar() },
                colors = ButtonDefaults.buttonColors(containerColor = btnColor)
            ) {
                Text(text = "Solicitar cuenta")
            }
        }
    }
}

@Preview(showSystemUi = true, locale = "es")
@Composable
fun RegistroUI(
    onBackButtonClicked: () -> Unit = {},
    onButtonFoto: () -> Unit = {},
    vm: viewModel = viewModel()
) {
    val phNombre = "Nombre Completo"
    val phRut = "RUT"
    val phNacimiento = "Fecha Nacimiento"
    val phEmail = "Email"
    val phTelefono = "Teléfono"

    val nombre: String by vm.usuario.observeAsState(initial = "")
    val rut: String by vm.usuario.observeAsState(initial = "")
    val Nacimiento: String by vm.usuario.observeAsState(initial = "")
    val Email: String by vm.usuario.observeAsState(initial = "")
    val Telefono: String by vm.usuario.observeAsState(initial = "")

    val contexto = LocalContext.current

    val (tfNombre, fNombre) = rememberSaveable {
        mutableStateOf("")
    }
    val (tfRut, fRut) = rememberSaveable {
        mutableStateOf("")
    }
    val (tfNacimiento, fNacimiento) = rememberSaveable {
        mutableStateOf("")
    }
    val (tfEmail, fEmail) = rememberSaveable {
        mutableStateOf("")
    }
    val (tfTelefono, fTelefono) = rememberSaveable {
        mutableStateOf("")
    }

    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Solicitud Cuenta", fontWeight = FontWeight.Bold, fontSize = 30.sp
            )
            TextField(value = tfNombre, onValueChange = { fNombre(it) }, placeholder = {
                Text(phNombre)
            })
            TextField(value = tfRut, onValueChange = { fRut(it) }, placeholder = {
                Text(phRut)
            })
            TextField(value = tfNacimiento, onValueChange = { fNacimiento(it) }, placeholder = {
                Text(phNacimiento)
            })
            TextField(value = tfEmail, onValueChange = { fEmail(it) }, placeholder = {
                Text(phEmail)
            })
            TextField(value = tfTelefono, onValueChange = { fTelefono(it) }, placeholder = {
                Text(phTelefono)
            })
            Spacer(modifier = Modifier.height(15.dp))
            Button(
                onClick = { onButtonFoto() },
                colors = ButtonDefaults.buttonColors(containerColor = btnColor)
            ) {
                Icon(
                    painter = painterResource(R.drawable.camera),
                    contentDescription = "Camara", modifier = Modifier
                        .size(20.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Cedula frontal")
            }
            Button(
                onClick = { onButtonFoto()
                            onBackButtonClicked()},
                colors = ButtonDefaults.buttonColors(containerColor = btnColor)
            ) {
                Icon(
                    painter = painterResource(R.drawable.camera),
                    contentDescription = "Camara", modifier = Modifier
                        .size(20.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "Cedula trasera")
            }
            Spacer(modifier = Modifier.height(35.dp))
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = btnColor)
            ) {
                Text(text = "Solicitar")
                LaunchedEffect(Unit) {
                    vm.agregarSolicitud(contexto)
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FotosUI(onBackButtonClicked: () -> Unit) {
    val permissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    val context = LocalContext.current
    val cameraController = remember {
        LifecycleCameraController(context)
    }
    val lifecycle = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }
    Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
        FloatingActionButton(onClick = {
            val executor = ContextCompat.getMainExecutor(context)
            sacarFoto(cameraController, executor)
        }) {
            Text(text = "Camara")
        }
    }) {
        if (permissionState.status.isGranted) {
            CamaraComposable(cameraController, lifecycle, modifier = Modifier.padding(it))
        } else {
            Text(text = "Permiso Denegado", modifier = Modifier.padding(it))
        }
    }
}

private fun sacarFoto(
    cameraController: LifecycleCameraController,
    executor: Executor
) {
    val file = File.createTempFile("img_", ".jpg")
    val outputDirectory = ImageCapture.OutputFileOptions.Builder(file).build()
    cameraController.takePicture(
        outputDirectory,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                println(outputFileResults.savedUri)

                //cerrar camara
                cameraController.unbind()

            }

            override fun onError(exception: ImageCaptureException) {
                println()
            }
        },
    )
    Log.i("test", outputDirectory.toString())
}


@Composable
fun CamaraComposable(
    cameraController: LifecycleCameraController,
    lifecycle: LifecycleOwner,
    modifier: Modifier = Modifier,
) {
    cameraController.bindToLifecycle(lifecycle)
    AndroidView(modifier = modifier, factory = { context ->
        val previewView = PreviewView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
        }
        previewView.controller = cameraController

        previewView
    })
}

@Preview(showSystemUi = true)
@Composable
fun UbicacionUI(vm: viewModel = viewModel()) {
    val contexto = LocalContext.current
    var mensaje by rememberSaveable { mutableStateOf("Ubicacion: ") }

    var latitud: Float by rememberSaveable {
        mutableStateOf(0.0f)
    }
    var longitud: Float by rememberSaveable {
        mutableStateOf(0.0f)
    }
    val launcherPermisos = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {
            if (
                it.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false)
                ||
                it.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false)
            ) {
                val locationService =
                    LocationServices.getFusedLocationProviderClient(contexto)
                val repositorio = LocationRepositorio(locationService)
                repositorio.conseguirUbicacion(
                    cuandoExito = {
                        mensaje = "Lat: ${it.latitude} | Lon: ${it.longitude}"
                        latitud = it.latitude.toFloat()
                        longitud = it.longitude.toFloat()
                    },
                    cuandoError = {
                        mensaje = "No se pudo conseguir la ubicacion"
                        Log.e("UbicacionUI::conseguirUbicacion", it.message ?: "")
                    }
                )
            } else {
                // permisos no concedidos
            }
        }
    )
    Column() {
        Text(mensaje)
        LaunchedEffect(Unit) {
            launcherPermisos.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }
}

@Composable
fun AppRegistrosUI(
    vmListaRegistros: ListaRegistrosViewModel = viewModel(factory = ListaRegistrosViewModel.Factory) // con factory hacemos que el Dao se pase al ViewModel y hacer las operaciones desde ahi
) {
    //se ejecuta 1 vez al iniciar el composable
    LaunchedEffect(Unit) {
        vmListaRegistros.getRegistros()
    }

    LazyColumn {
        items(vmListaRegistros.listRegistros) {
            Text(it.nombre)
        }
    }
}

