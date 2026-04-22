package com.example.circuitosescalera

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val toolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)

// --- AÑADE ESTO PARA DESBUGUEAR ---
        toolbar.setNavigationOnClickListener {
            // Esto es por si tienes la flechita de "atrás"
            onBackPressedDispatcher.onBackPressed()
        }

// Forzar que el título sea visible (esto confirma si el código llega aquí)
        supportActionBar?.title = "Mi App de Circuitos"


        val etR1 = findViewById<EditText>(R.id.etR1)
        val etR2 = findViewById<EditText>(R.id.etR2)
        val etR3 = findViewById<EditText>(R.id.etR3)
        val etR4 = findViewById<EditText>(R.id.etR4)
        val etR5 = findViewById<EditText>(R.id.etR5)
        val etR6 = findViewById<EditText>(R.id.etR6)
        val etVoltaje = findViewById<EditText>(R.id.etVoltaje)
        val btnSiguiente = findViewById<Button>(R.id.btnSiguiente)

        btnSiguiente.setOnClickListener {
            val r1 = etR1.text.toString().toDoubleOrNull()
            val r2 = etR2.text.toString().toDoubleOrNull()
            val r3 = etR3.text.toString().toDoubleOrNull()
            val r4 = etR4.text.toString().toDoubleOrNull()
            val r5 = etR5.text.toString().toDoubleOrNull()
            val r6 = etR6.text.toString().toDoubleOrNull()
            val vT = etVoltaje.text.toString().toDoubleOrNull()

            if (r1 == null || r2 == null || r3 == null || r4 == null || r5 == null || r6 == null || vT == null) {
                Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Enviar datos a la siguiente Activity
                val intent = Intent(this, ResultadosActivity::class.java)
                intent.putExtra("R1", r1)
                intent.putExtra("R2", r2)
                intent.putExtra("R3", r3)
                intent.putExtra("R4", r4)
                intent.putExtra("R5", r5)
                intent.putExtra("R6", r6)
                intent.putExtra("VT", vT)
                startActivity(intent)
            }
        }
    }
    // 1. Este método "infla" (muestra) el menú en la Toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return try {
            menuInflater.inflate(R.menu.main_menu, menu)
            true
        } catch (e: Exception) {
            // Si hay un error, lo verás en el Logcat
            e.printStackTrace()
            false
        }
    }

    // 2. Este método maneja qué pasa cuando tocas una opción
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.activity_main -> {
                // Si ya estamos en Main, no hacemos nada, si no, vamos allá
                if (this !is MainActivity) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                true
            }
            R.id.activity_creador -> {
                startActivity(Intent(this, creador::class.java))
                true
            }
            R.id.activity_contacto -> {
                startActivity(Intent(this, contacto::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}