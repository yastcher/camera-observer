```kotlin
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.facedetectionapp.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var telegramIdEditText: EditText
    private lateinit var botKeyEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        telegramIdEditText = findViewById(R.id.telegram_id_edit_text)
        botKeyEditText = findViewById(R.id.bot_key_edit_text)
        registerButton = findViewById(R.id.register_button)

        registerButton.setOnClickListener {
            val telegramId = telegramIdEditText.text.toString()
            val botKey = botKeyEditText.text.toString()

            if (telegramId.isNotBlank() && botKey.isNotBlank()) {
                val sharedPref = getSharedPreferences("FaceDetectionApp", MODE_PRIVATE)
                with (sharedPref.edit()) {
                    putString("telegramId", telegramId)
                    putString("botKey", botKey)
                    apply()
                }

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Show error message if either field is blank
                if (telegramId.isBlank()) {
                    telegramIdEditText.error = "Telegram ID is required"
                }
                if (botKey.isBlank()) {
                    botKeyEditText.error = "Bot Key is required"
                }
            }
        }
    }
}
```
