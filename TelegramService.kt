```kotlin
package com.example.facedetectionapp.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.facedetectionapp.MainActivity
import kotlinx.coroutines.*
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.io.File
import java.util.concurrent.TimeUnit

class TelegramService : Service() {

    private var job: Job? = null
    private var bot: TelegramBot? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val sharedPref = getSharedPreferences("FaceDetectionApp", MODE_PRIVATE)
        val telegramId = sharedPref.getString("telegramId", null)
        val botKey = sharedPref.getString("botKey", null)

        bot = TelegramBot(botKey, telegramId)

        job = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                val photoFile = File(cacheDir, "face.jpg")
                if (photoFile.exists()) {
                    bot?.sendPhoto(photoFile)
                    photoFile.delete()
                    delay(TimeUnit.MINUTES.toMillis(15))
                }
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    private inner class TelegramBot(val botKey: String?, val chatId: String?) : TelegramLongPollingBot() {

        override fun getBotUsername(): String {
            return "FaceDetectionBot"
        }

        override fun getBotToken(): String {
            return botKey ?: ""
        }

        override fun onUpdateReceived(update: Update) {
            // We don't need to handle any updates in this bot
        }

        fun sendPhoto(file: File) {
            val sendPhoto = SendPhoto()
            sendPhoto.setChatId(chatId)
            sendPhoto.setPhoto(file)

            try {
                execute(sendPhoto)
            } catch (e: TelegramApiException) {
                Log.e(MainActivity::class.java.simpleName, "Failed to send photo", e)
            }
        }
    }
}
```
