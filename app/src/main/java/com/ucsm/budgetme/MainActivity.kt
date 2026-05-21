package com.ucsm.budgetme

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ucsm.budgetme.data.Transaccion
import com.ucsm.budgetme.data.TransaccionDao
import com.ucsm.budgetme.databinding.ActivityMainBinding
import com.ucsm.budgetme.data.AppDatabase
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dao: TransaccionDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = AppDatabase.getInstance(this).transaccionDao()

        binding.btnRegistrar.setOnClickListener { registrar() }
        binding.btnBuscar.setOnClickListener { buscar() }
        binding.btnModificar.setOnClickListener { modificar() }
        binding.btnEliminar.setOnClickListener { eliminar() }
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    private fun limpiar() {
        binding.txtId.setText("")
        binding.txtDescripcion.setText("")
        binding.txtMonto.setText("")
        binding.txtCategoria.setText("")
        binding.txtFecha.setText("")
    }

    private fun registrar() {
        val descripcion = binding.txtDescripcion.text.toString()
        val monto = binding.txtMonto.text.toString()
        val categoria = binding.txtCategoria.text.toString()
        val fecha = binding.txtFecha.text.toString()

        if (descripcion.isEmpty() || monto.isEmpty() || categoria.isEmpty() || fecha.isEmpty()) {
            toast("Complete todos los campos")
            return
        }

        val transaccion = Transaccion(
            descripcion = descripcion,
            monto = monto.toDouble(),
            tipo = if (binding.rbIngreso.isChecked) "ingreso" else "gasto",
            categoria = categoria,
            fecha = fecha
        )

        lifecycleScope.launch {
            try {
                dao.insertar(transaccion)
                limpiar()
                toast("Transacción registrada ✓")
            } catch (e: Exception) {
                toast("Error: ${e.message}")
            }
        }
    }

    private fun buscar() {
        val idStr = binding.txtId.text.toString()
        if (idStr.isEmpty()) { toast("Ingrese el ID"); return }

        lifecycleScope.launch {
            val t = dao.buscarPorId(idStr.toInt())
            if (t != null) {
                binding.txtDescripcion.setText(t.descripcion)
                binding.txtMonto.setText(t.monto.toString())
                binding.txtCategoria.setText(t.categoria)
                binding.txtFecha.setText(t.fecha)
                if (t.tipo == "ingreso") binding.rgTipo.check(binding.rbIngreso.id)
                else binding.rgTipo.check(binding.rbGasto.id)
                toast("Encontrado ✓")
            } else {
                toast("No existe esa transacción")
            }
        }
    }

    private fun modificar() {
        val idStr = binding.txtId.text.toString()
        val descripcion = binding.txtDescripcion.text.toString()
        val monto = binding.txtMonto.text.toString()
        val categoria = binding.txtCategoria.text.toString()
        val fecha = binding.txtFecha.text.toString()

        if (idStr.isEmpty() || descripcion.isEmpty() || monto.isEmpty() || categoria.isEmpty() || fecha.isEmpty()) {
            toast("Complete todos los campos incluyendo el ID")
            return
        }

        val transaccion = Transaccion(
            id = idStr.toInt(),
            descripcion = descripcion,
            monto = monto.toDouble(),
            tipo = if (binding.rbIngreso.isChecked) "ingreso" else "gasto",
            categoria = categoria,
            fecha = fecha
        )

        lifecycleScope.launch {
            val filas = dao.actualizar(transaccion)
            if (filas > 0) { limpiar(); toast("Modificado ✓") }
            else toast("No existe esa transacción")
        }
    }

    private fun eliminar() {
        val idStr = binding.txtId.text.toString()
        if (idStr.isEmpty()) { toast("Ingrese el ID"); return }

        lifecycleScope.launch {
            val t = dao.buscarPorId(idStr.toInt())
            if (t != null) {
                dao.eliminar(t)
                limpiar()
                toast("Eliminado ✓")
            } else {
                toast("No existe esa transacción")
            }
        }
    }
}