package com.example.eatbeat.utils

import android.content.Context
import android.graphics.Bitmap
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

private val ftpClient = FTPClient()

private val host = "10.0.0.99"
private val user = "dam06"
private val password = "pepe"
private val path = "/Multimedia/"

// Connect to the FTP server
fun connect() {
    try {
        if (!ftpClient.isConnected) {
            ftpClient.connect(host)
            val login = ftpClient.login(user, password)
            if (!login) {
                println("FTP login failed")
                disconnect() // Ensure clean disconnect on failure
            } else {
                println("FTP login successful")
                ftpClient.enterLocalPassiveMode() // Set passive mode
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE) // Set file type to binary
            }
        } else {
            println("Already connected to the FTP server")
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

// Disconnect from the FTP server
fun disconnect() {
    try {
        if (ftpClient.isConnected) {
            ftpClient.logout()
            ftpClient.disconnect()
            println("Disconnected from FTP server")
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

// Upload file to FTP server
fun uploadFileToFtp(file: File) {
    Thread{
        try {
            if (!ftpClient.isConnected) {
                println("Not connected to FTP. Attempting to reconnect...")
                connect()
            }

            val inputStream: InputStream = file.inputStream()
            val result = ftpClient.storeFile("${path}${file.name}", inputStream)
            inputStream.close()
            if (result) {
                println("File uploaded successfully at path ${path}${file.name}")
            } else {
                println("File upload failed at path ${path}${file.name}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.start()
}

fun downloadFileFromFtp(path: String): String? {
    var jsonResult: String? = null
    val latch = java.util.concurrent.CountDownLatch(1) // To synchronize threads

    val thread = Thread {
        try {
            if (!ftpClient.isConnected) {
                println("Not connected to FTP. Attempting to reconnect...")
                connect()
            }

            println("First Attempting to retrieve file at path: $path")
            val tempFile = File.createTempFile("temp_json", ".json")
            val outputStream = FileOutputStream(tempFile)

            println("Attempting to retrieve file at path: $path")
            val result = ftpClient.retrieveFile(path, outputStream)
            outputStream.close()

            if (result) {
                println("File retrieved successfully: $path")
                jsonResult = tempFile.readText()
            } else {
                println("Failed to retrieve the file at path: $path")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            latch.countDown()
        }
    }

    thread.start()

    try {
        latch.await() // Wait for the worker thread to finish
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }

    return jsonResult
}


// Full process to save Bitmap, convert to PNG, and upload to FTP
fun uploadBitmap(bitmap: Bitmap, context: Context, filename: String) {
    Thread {
        try {
            connect() // Ensure connection before the upload process
            val file = saveBitmapToFile(bitmap, context, filename)
            if (file != null) {
                uploadFileToFtp(file)
            } else {
                println("Failed to save the bitmap as PNG")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }.start()
}

// Convert Bitmap to PNG and save as a file
private fun saveBitmapToFile(bitmap: Bitmap, context: Context, filename: String): File? {
    val file = File(context.cacheDir, filename)
    var outputStream: FileOutputStream? = null
    try {
        outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        return file
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        try {
            outputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return null
}