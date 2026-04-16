package com.example.circuitosescalera

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    // Componentes de la Interfaz
    private lateinit var etR1: EditText; private lateinit var etR2: EditText; private lateinit var etR3: EditText
    private lateinit var etR4: EditText; private lateinit var etR5: EditText; private lateinit var etR6: EditText
    private lateinit var etVoltaje: EditText; private lateinit var tvExplicacion: TextView
    private lateinit var tvResultado: TextView; private lateinit var btnSiguiente: Button
    private lateinit var btnAnterior: Button; private lateinit var btnReset: Button
    private lateinit var imgCircuito: ImageView

    // Listas para manejar el contenido dinámico del tutorial
    private var pasosProcedimiento = mutableListOf<String>()
    private var imagenesPasos = mutableListOf<Int>()
    private var pasoActual = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar componentes vinculándolos al XML
        imgCircuito = findViewById(R.id.imgCircuito)
        etR1 = findViewById(R.id.etR1); etR2 = findViewById(R.id.etR2); etR3 = findViewById(R.id.etR3)
        etR4 = findViewById(R.id.etR4); etR5 = findViewById(R.id.etR5); etR6 = findViewById(R.id.etR6)
        etVoltaje = findViewById(R.id.etVoltaje); tvExplicacion = findViewById(R.id.tvExplicacion)
        tvResultado = findViewById(R.id.tvResultado); btnSiguiente = findViewById(R.id.btnSiguiente)
        btnAnterior = findViewById(R.id.btnAnterior); btnReset = findViewById(R.id.btnReset)

        // Listeners de los botones
        btnSiguiente.setOnClickListener {
            if (pasosProcedimiento.isEmpty()) iniciarCalculo() else avanzarPaso()
        }
        btnAnterior.setOnClickListener { retrocederPaso() }
        btnReset.setOnClickListener { limpiarTodo() }
    }

    private fun iniciarCalculo() {
        // 1. VALIDACIÓN: Usando Toast en lugar de .error
        val camposR = listOf(
            etR1 to "R1", etR2 to "R2", etR3 to "R3",
            etR4 to "R4", etR5 to "R5", etR6 to "R6"
        )

        for (par in camposR) {
            val valor = par.first.text.toString().toDoubleOrNull()
            if (valor == null || valor <= 0) {
                Toast.makeText(this, "La resistencia ${par.second} debe ser mayor a 0", Toast.LENGTH_SHORT).show()
                tvExplicacion.text = "Error: La resistencia ${par.second} debe ser mayor a 0."
                return
            }
        }

        val vT = etVoltaje.text.toString().toDoubleOrNull() ?: 0.0
        if (vT <= 0) {
            Toast.makeText(this, "Ingresa un voltaje positivo", Toast.LENGTH_SHORT).show()
            tvExplicacion.text = "Error: Ingresa un voltaje positivo."
            return
        }

        // 2. OBTENER VALORES
        val r1 = etR1.text.toString().toDouble()
        val r2 = etR2.text.toString().toDouble()
        val r3 = etR3.text.toString().toDouble()
        val r4 = etR4.text.toString().toDouble()
        val r5 = etR5.text.toString().toDouble()
        val r6 = etR6.text.toString().toDouble()

        // 3. CÁLCULOS DE REDUCCIÓN
        val rA = r5 + r6
        val rB = (r4 * rA) / (r4 + rA)
        val rC = rB + r3
        val rD = (r2 * rC) / (r2 + rC)
        val rEq = r1 + rD
        val iT = vT / rEq
        val pTotal = vT * iT

        // 4. ANÁLISIS DE VOLTAJES Y CORRIENTES
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

        // 5. GENERAR PASOS + IMÁGENES (Tal cual tu lógica original)
        pasosProcedimiento.clear()
        imagenesPasos.clear()

        pasosProcedimiento.add("INICIO\nPresiona '->' para ver cómo se resuelve este circuito escalera.")
        imagenesPasos.add(R.drawable.circuito1)

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

        pasosProcedimiento.add("ANÁLISIS R1\nV1 = It * R1 = ${"%.3f".format(iT)} * ${"%.1f".format(r1)} = ${"%.2f".format(vR1)}V\nI1 = It = ${"%.3f".format(iT)}A\nP1 = V1 * I1 = ${"%.2f".format(vR1 * iT)}W")
        imagenesPasos.add(R.drawable.circuito1)

        pasosProcedimiento.add("ANÁLISIS R2\nV2 = Vt - V1 = ${"%.2f".format(vD)}V\nI2 = V2 / R2 = ${"%.2f".format(vD)} / ${"%.1f".format(r2)} = ${"%.3f".format(iR2)}A\nP2 = V2 * I2 = ${"%.2f".format(vD * iR2)}W")
        imagenesPasos.add(R.drawable.circuito1)

        pasosProcedimiento.add("ANÁLISIS R3\nI3 = It - I2 = ${"%.3f".format(iR3)}A\nV3 = I3 * R3 = ${"%.3f".format(iR3)} * ${"%.1f".format(r3)} = ${"%.2f".format(vR3)}V\nP3 = V3 * I3 = ${"%.2f".format(vR3 * iR3)}W")
        imagenesPasos.add(R.drawable.circuito1)

        pasosProcedimiento.add("ANÁLISIS R4\nV4 = V2 - V3 = ${"%.2f".format(vB)}V\nI4 = V4 / R4 = ${"%.2f".format(vB)} / ${"%.1f".format(r4)} = ${"%.3f".format(iR4)}A\nP4 = V4 * I4 = ${"%.2f".format(vB * iR4)}W")
        imagenesPasos.add(R.drawable.circuito1)

        pasosProcedimiento.add("ANÁLISIS R5\nI5 = I3 - I4 = ${"%.3f".format(iR56)}A\nV5 = I5 * R5 = ${"%.3f".format(iR56)} * ${"%.1f".format(r5)} = ${"%.2f".format(vR5)}V\nP5 = V5 * I5 = ${"%.2f".format(vR5 * iR56)}W")
        imagenesPasos.add(R.drawable.circuito1)

        pasosProcedimiento.add("ANÁLISIS R6\nI6 = I5 = ${"%.3f".format(iR56)}A\nV6 = I6 * R6 = ${"%.2f".format(vR6)}V\nP6 = V6 * I6 = ${"%.2f".format(vR6 * iR56)}W")
        imagenesPasos.add(R.drawable.circuito1)

        pasosProcedimiento.add("POTENCIA TOTAL\nP = Vt * It = ${"%.2f".format(vT)} * ${"%.3f".format(iT)} = ${"%.2f".format(pTotal)} W")
        imagenesPasos.add(R.drawable.circuito1)

        pasoActual = 0
        mostrarPaso()
        tvResultado.text = "Res: ${"%.2f".format(rEq)} Ω"
    }

    private fun mostrarPaso() {
        if (pasosProcedimiento.isNotEmpty()) {
            tvExplicacion.text = pasosProcedimiento[pasoActual]
            if (pasoActual < imagenesPasos.size) {
                imgCircuito.setImageResource(imagenesPasos[pasoActual])
            }
            tvExplicacion.textSize = if (pasosProcedimiento[pasoActual].length > 160) 14f else 16f
        }
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

    private fun limpiarTodo() {
        val campos = listOf(etR1, etR2, etR3, etR4, etR5, etR6, etVoltaje)
        campos.forEach {
            it.text.clear()
            it.error = null // Esto limpia cualquier error previo si existía
        }
        pasosProcedimiento.clear()
        imagenesPasos.clear()
        pasoActual = 0
        imgCircuito.setImageResource(R.drawable.circuito1)
        tvExplicacion.text = "Aquí aparecerá el procedimiento paso a paso..."
        tvResultado.text = "Res: 0.00 Ω"
    }
}