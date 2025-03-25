package com.example.cacaaotesouro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.text.font.FontWeight
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Substituindo NavigationComposeTheme por MaterialTheme
            MaterialTheme {
                TreasureHuntApp()
            }
        }
    }
}

@Composable
fun TreasureHuntApp() {
    val navController = rememberNavController()
    val startTime = remember { System.currentTimeMillis() }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("clue1") {
            ClueScreen(
                clueText = "Qual é o animal que late, mas não é um cachorro?",
                correctAnswer = "lobo",
                navController = navController,
                nextRoute = "clue2"
            )
        }
        composable("clue2") {
            ClueScreen(
                clueText = "Sou redondo, amarelo e brilhante. O que sou?",
                correctAnswer = "sol",
                navController = navController,
                nextRoute = "clue3"
            )
        }
        composable("clue3") {
            ClueScreen(
                clueText = "Tenho folhas, mas não sou árvore. Posso ser usado para escrever. O que sou?",
                correctAnswer = "livro",
                navController = navController,
                nextRoute = "treasure"
            )
        }
        composable("treasure") {
            TreasureScreen(
                navController = navController,
                startTime = startTime
            )
        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Caça ao Tesouro",
            fontSize = 36.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { navController.navigate("clue1") },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text("Iniciar Caça ao Tesouro")
        }
    }
}

@Composable
fun ClueScreen(
    clueText: String,
    correctAnswer: String,
    navController: NavController,
    nextRoute: String
) {
    var userAnswer by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Pista:",
            fontSize = 24.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = clueText,
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = userAnswer,
            onValueChange = { userAnswer = it },
            label = { Text("Sua resposta") },
            modifier = Modifier.fillMaxWidth(0.8f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { navController.navigateUp() }
            ) {
                Text("Voltar")
            }
            Button(
                onClick = {
                    if (userAnswer.lowercase().trim() == correctAnswer) {
                        navController.navigate(nextRoute) {
                            popUpTo("home")
                        }
                    } else {
                        errorMessage = "Resposta incorreta. Tente novamente!"
                    }
                }
            ) {
                Text("Próxima Pista")
            }
        }
    }
}

@Composable
fun TreasureScreen(
    navController: NavController,
    startTime: Long
) {
    val totalTime = (System.currentTimeMillis() - startTime) / 1000.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Parabéns! Você encontrou o tesouro!",
            fontSize = 24.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = String.format(
                Locale.getDefault(),
                "Tempo total: %.2f segundos",
                totalTime
            ),
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text("Reiniciar Caça ao Tesouro")
        }
    }
}