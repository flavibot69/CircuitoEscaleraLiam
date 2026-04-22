package com.example.circuitosescalera

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class ResultadosActivity : AppCompatActivity() {

    private lateinit var tvExplicacion: TextView
    private lateinit var tvResultado: TextView
    private lateinit var imgCircuitoRes: ImageView

    private var pasosProcedimiento = mutableListOf<String>()
    private var imagenesPasos = mutableListOf<Int>()
    private var pasoActual = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultados)

        // Configurar Toolbar
        val toolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Vincular componentes
        imgCircuitoRes = findViewById(R.id.imgCircuitoRes)
        tvExplicacion = findViewById(R.id.tvExplicacion)
        tvResultado = findViewById(R.id.tvResultado)
        val btnAnterior = findViewById<Button>(R.id.btnAnterior)
        val btnSiguiente = findViewById<Button>(R.id.btnSiguiente)
        val btnReset = findViewById<Button>(R.id.btnReset)

        // 1. RECUPERAR DATOS DEL MAIN
        val r1 = intent.getDoubleExtra("R1", 0.0)
        val r2 = intent.getDoubleExtra("R2", 0.0)
        val r3 = intent.getDoubleExtra("R3", 0.0)
        val r4 = intent.getDoubleExtra("R4", 0.0)
        val r5 = intent.getDoubleExtra("R5", 0.0)
        val r6 = intent.getDoubleExtra("R6", 0.0)
        val vT = intent.getDoubleExtra("VT", 0.0)

        // 2. REALIZAR TODOS LOS CÁLCULOS (Tu lógica original completa)
        val rA = r5 + r6
        val rB = (r4 * rA) / (r4 + rA)
        val rC = rB + r3
        val rD = (r2 * rC) / (r2 + rC)
        val rEq = r1 + rD
        val iT = vT / rEq
        val pTotal = vT * iT

        // Análisis de voltajes y corrientes
        val vR1 = iT * r1
        val vD = vT - vR1
        val iR2 = vD / r2
        val iR3 = iT - iR2
        val vR3 = iR3 * r3
        val vB = vD - vR3
        val iR4 = vB / r4
        val iR56 = iR3 - iR4
        val vR5 = iR56 * r5
        val vR6 = iR56 * r6

        // 3. GENERAR PASOS (Recuperando lo que "me comí" + Potencia Total)
        pasosProcedimiento.clear()
        imagenesPasos.clear()

        // Paso 0: Inicio
        pasosProcedimiento.add("INICIO\nDatos ingresados:\nVoltaje: $vT V\nResistencias: R1=$r1, R2=$r2, R3=$r3, R4=$r4, R5=$r5, R6=$r6")
        imagenesPasos.add(R.drawable.circuito1)

        // Reducciones
        pasosProcedimiento.add("PASO 1: Reducción Serie (R5+R6)\nRa = ${"%.1f".format(r5)} + ${"%.1f".format(r6)} = ${"%.2f".format(rA)} Ω")
        imagenesPasos.add(R.drawable.circuito2)

        pasosProcedimiento.add("PASO 2: Reducción Paralelo (R4 || Ra)\nRb = (${"%.1f".format(r4)} * ${"%.1f".format(rA)}) / (${"%.1f".format(r4)} + ${"%.1f".format(rA)}) = ${"%.2f".format(rB)} Ω")
        imagenesPasos.add(R.drawable.circuito3)

        pasosProcedimiento.add("PASO 3: Reducción Serie (R3 + Rb)\nRc = ${"%.1f".format(r3)} + ${"%.1f".format(rB)} = ${"%.2f".format(rC)} Ω")
        imagenesPasos.add(R.drawable.circuito4)

        pasosProcedimiento.add("PASO 4: Reducción Paralelo (R2 || Rc)\nRd = (${"%.1f".format(r2)} * ${"%.1f".format(rC)}) / (${"%.1f".format(r2)} + ${"%.1f".format(rC)}) = ${"%.2f".format(rD)} Ω")
        imagenesPasos.add(R.drawable.circuito5)

        pasosProcedimiento.add("RESISTENCIA TOTAL\nReq = R1 + Rd\nReq = ${"%.1f".format(r1)} + ${"%.2f".format(rD)} = ${"%.2f".format(rEq)} Ω\n\nIt = V / Req = ${"%.3f".format(iT)} A")
        imagenesPasos.add(R.drawable.circuito6)

        // Análisis individual (Lo que ya tenías)
        pasosProcedimiento.add("ANÁLISIS R1\nV1 = It * R1 = ${"%.2f".format(vR1)}V\nI1 = It = ${"%.3f".format(iT)}A\nP1 = V1 * I1 = ${"%.2f".format(vR1 * iT)}W")
        imagenesPasos.add(R.drawable.circuito1)

        pasosProcedimiento.add("ANÁLISIS R2\nV2 = Vt - V1 = ${"%.2f".format(vD)}V\nI2 = V2 / R2 = ${"%.3f".format(iR2)}A\nP2 = V2 * I2 = ${"%.2f".format(vD * iR2)}W")
        imagenesPasos.add(R.drawable.circuito1)

        pasosProcedimiento.add("ANÁLISIS R3\nI3 = It - I2 = ${"%.3f".format(iR3)}A\nV3 = I3 * R3 = ${"%.2f".format(vR3)}V\nP3 = V3 * I3 = ${"%.2f".format(vR3 * iR3)}W")
        imagenesPasos.add(R.drawable.circuito1)

        pasosProcedimiento.add("ANÁLISIS R4\nV4 = V2 - V3 = ${"%.2f".format(vB)}V\nI4 = V4 / R4 = ${"%.3f".format(iR4)}A\nP4 = V4 * I4 = ${"%.2f".format(vB * iR4)}W")
        imagenesPasos.add(R.drawable.circuito1)

        pasosProcedimiento.add("ANÁLISIS R5\nI5 = I3 - I4 = ${"%.3f".format(iR56)}A\nV5 = I5 * R5 = ${"%.2f".format(vR5)}V\nP5 = V5 * I5 = ${"%.2f".format(vR5 * iR56)}W")
        imagenesPasos.add(R.drawable.circuito1)

        pasosProcedimiento.add("ANÁLISIS R6\nI6 = I5 = ${"%.3f".format(iR56)}A\nV6 = I6 * R6 = ${"%.2f".format(vR6)}V\nP6 = V6 * I6 = ${"%.2f".format(vR6 * iR56)}W")
        imagenesPasos.add(R.drawable.circuito1)

        // Paso Final: Potencia Total (EL NUEVO)
        pasosProcedimiento.add("POTENCIA TOTAL DEL SISTEMA\nPt = Vt * It\nPt = ${"%.1f".format(vT)}V * ${"%.3f".format(iT)}A\nPt = ${"%.2f".format(pTotal)} W")
        imagenesPasos.add(R.drawable.circuito1)

        // 4. MOSTRAR RESULTADO INICIAL
        mostrarPaso()
        tvResultado.text = "Req: ${"%.2f".format(rEq)} Ω"

        // Botones
        btnSiguiente.setOnClickListener { avanzarPaso() }
        btnAnterior.setOnClickListener { retrocederPaso() }
        btnReset.setOnClickListener { finish() }
    }

    // 1. Este método "infla" (muestra) el menú en la Toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
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

    private fun mostrarPaso() {
        tvExplicacion.text = pasosProcedimiento[pasoActual]
        imgCircuitoRes.setImageResource(imagenesPasos[pasoActual])
        // Ajuste de tamaño de texto dinámico
        tvExplicacion.textSize = if (pasosProcedimiento[pasoActual].length > 160) 14f else 16f
    }

    private fun avanzarPaso() {
        if (pasoActual < pasosProcedimiento.size - 1) {
            pasoActual++
            mostrarPaso()
        }
    }

    private fun retrocederPaso() {
        if (pasoActual > 0) {
            pasoActual--
            mostrarPaso()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}