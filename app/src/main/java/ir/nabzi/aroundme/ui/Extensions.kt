package ir.nabzi.aroundme.ir.nabzi.aroundme.ui

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showError(message: String?) {
    Toast.makeText(requireContext(), message ?: "error", Toast.LENGTH_SHORT).show()
}