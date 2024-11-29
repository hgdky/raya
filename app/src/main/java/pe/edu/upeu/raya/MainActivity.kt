

package pe.edu.upeu.raya

import android.app.AlertDialog
import android.widget.LinearLayout
import android.os.Bundle
import android.widget.ImageView
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upeu.raya.ui.theme.RayaTheme


class MainActivity : ComponentActivity() {

     private val gameController = GameController()
     private lateinit var tablero: Array<Array<Char>>
     private var gameOver = false
     private var turnoJugador1 = true
     private lateinit var tableroLayout: LinearLayout
     private lateinit var reiniciarPartida: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_main, menu)

        tableroLayout = findViewById(R.id.tableroLayout)
        reiniciarPartida = findViewById(R.id.reiniciarPartida)

        inicializarPartida()
        inicializarListeners()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                // Lógica para manejar la opción de menú "Configuración"
                return true
            }
            android.R.id.home -> {
                // Maneja la acción de retroceso en la barra de acción si está habilitada
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun inicializarPartida(){
        tablero = gameController.nuevoTablero()
        gameOver = false
        turnoJugador1 = true
        for(i in 0 until tableroLayout.childCount){
            val fila = tableroLayout.getChildAt(i) as LinearLayout
            for (j in 0  until fila.childCount){
                val ficha = fila.getChildAt(j) as ImageView
                ficha.setImageDrawable(getDrawable(R.drawable.cuadro_vacio))
            }
        }
    }


        private fun inicializarListeners(){
            reiniciarPartida.setOnClickListener{ // esto reinicia la partida
                inicializarPartida()
            }

            for(i in 0 until tableroLayout.childCount){
               // val fila = tableroLayout[i] as LinearLayout //comentario
                val fila = tableroLayout.getChildAt(i) as LinearLayout
                for(j in 0 until fila.childCount){
                    //val ficha = fila[j] as ImageView // comentario
                    val ficha = fila.getChildAt(j) as ImageView
                    ficha.setOnClickListener{
                        if(!gameOver && tablero [i][j]== '-'){ // el guion significa z
                            setFicha(ficha, i, j)
                            val estadoPartida = gameController.estadoPartida(turnoJugador1)
                            if(turnoJugador1 && estadoPartida == GameController.EstadoPartida.JUGADOR1_GANO){
                                showGameOverDialog("EL jugador 1 ganó!")
                                gameOver = true
                            }else if (!turnoJugador1 && estadoPartida == GameController.EstadoPartida.JUGADOR2_GANO){
                                showGameOverDialog("EL jugador 2 ganó!")
                                gameOver = true
                            }else if(estadoPartida == GameController.EstadoPartida.EMPATE) {
                                showGameOverDialog("Empate")
                                gameOver = true
                            }else {
                                turnoJugador1 = !turnoJugador1
                            }
                           // turnoJugador1 = !turnoJugador1 //se le dara el valor opuesto
                        }

                     //  ficha.setImageDrawable(getDrawable(R.drawable.circulo))
                    }


            }
        }

    }


    private fun showGameOverDialog(mensaje:String){
        AlertDialog.Builder(this)
            .setTitle(mensaje)
            .setPositiveButton("Jugar de nuevo",{ dialog, which -> inicializarPartida()})
            .setNegativeButton("cancelar", {dialog, which -> dialog.dismiss()})
            .show()
    }

    private fun setFicha(view: ImageView, posicionFila: Int, posicionColumna: Int){
        if(turnoJugador1){
            tablero[posicionFila][posicionColumna] = 'O'
            view.setImageDrawable(getDrawable(R.drawable.circulo))
        } else{
            tablero[posicionFila][posicionColumna] = 'X'
            view.setImageDrawable(getDrawable(R.drawable.cruz))
        }
    }
}