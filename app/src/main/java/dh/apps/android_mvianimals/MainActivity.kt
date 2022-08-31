package dh.apps.android_mvianimals

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberImagePainter
import dh.apps.android_mvianimals.api.AnimalService
import dh.apps.android_mvianimals.model.Animal
import dh.apps.android_mvianimals.ui.theme.Android_MVIAnimalsTheme
import dh.apps.android_mvianimals.view.MainIntent
import dh.apps.android_mvianimals.view.MainState
import dh.apps.android_mvianimals.view.MainViewModel
import dh.apps.android_mvianimals.view.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, ViewModelFactory(AnimalService.api))
            .get(MainViewModel::class.java)

        val onButtonClick: () -> Unit = {
            lifecycleScope.launch {
                viewModel.userIntent.send(MainIntent.FetchAnimals)
            }
        }

        setContent {
            Android_MVIAnimalsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen(vm = viewModel, onButtonClick = onButtonClick)
                }
            }
        }
    }
}

@Composable
fun MainScreen(vm: MainViewModel, onButtonClick: () -> Unit) {

    val state = vm.state.value

    when (state) {
        is MainState.Idle -> IdleScreen(onButtonClick)
        is MainState.Loading -> LoadingScreen()
        is MainState.Animals -> AnimalsList(animals = state.animals)
        is MainState.Error -> {
            IdleScreen(onButtonClick)
            Toast.makeText(LocalContext.current, state.error, Toast.LENGTH_SHORT).show()
        }
    }

}

@Composable
fun IdleScreen(onButtonClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = onButtonClick) {
            Text(text = " Fetch Animals ")
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun AnimalsList(animals: List<Animal>) {
    LazyColumn {
        items(items = animals) {
            AnimalItem(animal = it)
            Divider(color = Color.Gray, modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}

@Composable
fun AnimalItem(animal: Animal) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        val url = AnimalService.BASE_URL + animal.image
        val painter = rememberImagePainter(data = url)
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.FillHeight
        )
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(start = 4.dp)) {
            Text(text = animal.name, fontWeight = FontWeight.Bold)
            Text(text = animal.location, fontWeight = FontWeight.Light)
        }
    }
}