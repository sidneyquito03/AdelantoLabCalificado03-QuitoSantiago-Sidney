import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel(
    val context: Context
): ViewModel() {

    val inputsError = MutableLiveData<Boolean>()
    val registerError = MutableLiveData<Boolean>()
    val authError = MutableLiveData<Boolean>()
    val registerSuccess = MutableLiveData<Boolean>()

    private var sharedPreferencesRepository: SharedPreferencesRepository =
        SharedPreferencesRepository().also {
            it.setSharedPreference(context)
        }

    fun validateInputs(email: String, password: String, password2: String) {
        // Empty es para que pueda ver si el input o el espacio está vacío o no
        if (isEmptyInputs(email, password, password2)) {
            inputsError.postValue(true)
            return
        }

        if (password != password2) {
            Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }
        val emailSharedPreferences = sharedPreferencesRepository.getUserEmail()

        if (emailSharedPreferences.isNotEmpty()) {
            registerError.postValue(true)
            return
        }

        sharedPreferencesRepository.saveUserEmail(email)
        sharedPreferencesRepository.saveUserPassword(password)

        // Si la validación fue exitosa
        registerSuccess.postValue(true)
    }

    private fun isEmptyInputs(email: String, password: String, password2: String) = email.isEmpty() || password.isEmpty() || password2.isEmpty()
}
