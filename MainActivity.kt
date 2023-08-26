```kotlin
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.facedetectionapp.services.FaceDetectionService
import com.example.facedetectionapp.services.TelegramService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check if user has registered their Telegram ID and bot key
        val sharedPref = getSharedPreferences("FaceDetectionApp", MODE_PRIVATE)
        val telegramId = sharedPref.getString("telegramId", null)
        val botKey = sharedPref.getString("botKey", null)

        if (telegramId == null || botKey == null) {
            // If not registered, redirect to RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // If registered, start the FaceDetectionService and TelegramService
            val faceDetectionServiceIntent = Intent(this, FaceDetectionService::class.java)
            startService(faceDetectionServiceIntent)

            val telegramServiceIntent = Intent(this, TelegramService::class.java)
            startService(telegramServiceIntent)
        }
    }

    fun stopServices(view: View) {
        // Stop the FaceDetectionService and TelegramService when the stop button is clicked
        val faceDetectionServiceIntent = Intent(this, FaceDetectionService::class.java)
        stopService(faceDetectionServiceIntent)

        val telegramServiceIntent = Intent(this, TelegramService::class.java)
        stopService(telegramServiceIntent)
    }
}
```
