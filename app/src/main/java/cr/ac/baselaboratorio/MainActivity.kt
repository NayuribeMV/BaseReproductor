package cr.ac.baselaboratorio
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private var indice:Int=0
    private var pausa=true

    private lateinit var buttonPlay: Button
    private lateinit var buttonPause: Button

    private lateinit var buttonSig: Button
    private lateinit var buttonAnterior: Button

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var textView: TextView

    companion object{
        var OPEN_DIRECTORY_REQUEST_CODE=1
    }
    private var archivos:MutableList<DocumentFile> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.textView)
       //BOTONES
        buttonAnterior = findViewById(R.id.buttonPrevious)
        buttonPlay = findViewById(R.id.buttonPlay)
        buttonPause = findViewById(R.id.buttonPause)
        buttonSig=findViewById(R.id.buttonNext)


        var intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(intent, OPEN_DIRECTORY_REQUEST_CODE)
        setOnClickListeners(this)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == OPEN_DIRECTORY_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                var directoryUri = data?.data ?:return
                val rootTree = DocumentFile.fromTreeUri(this,directoryUri )
                for(file in rootTree!!.listFiles()){
                    try {
                        file.name?.let { Log.e("Archivo", it) }
                        archivos.add(file)
                    }catch (e: Exception){
                        Log.e("Error", "No pude ejecutar el archivo" + file.uri)
                    }
                }
                mediaPlayer = MediaPlayer.create(
                    this,archivos[indice].uri
                )
            }
        }
    }

    private fun setOnClickListeners(context: Context) {
        //boton PLAY
        buttonPlay.setOnClickListener {
            if(pausa){
                pausa=false
                mediaPlayer.start()
                Toast.makeText(context, "Reproduciendo...", Toast.LENGTH_SHORT).show()
                textView.text = archivos[indice].name.toString()
            }

        }
        //boton PAUSA
        buttonPause.setOnClickListener {
            pausa=true
            mediaPlayer.pause()

            Toast.makeText(context, "Parando...", Toast.LENGTH_SHORT).show()
            mediaPlayer = MediaPlayer.create(
                this,archivos[indice].uri
            )
            textView.text = archivos[indice].name.toString()
        }
        //boton NEXT
        buttonSig.setOnClickListener{
            if(indice+1>archivos.size-1){
                mediaPlayer.stop()
                indice=0


                mediaPlayer = MediaPlayer.create(
                    context,archivos[indice].uri
                )
                mediaPlayer.start()
                textView.text = archivos[indice].name.toString()
            }else{
                mediaPlayer.stop()


                indice++
                mediaPlayer = MediaPlayer.create(
                    context,archivos[indice].uri
                )
                mediaPlayer.start()
                textView.text = archivos[indice].name.toString()
            }


        }
        //boton PREVIOUS
        buttonAnterior.setOnClickListener{
            if(indice-1<0){


                mediaPlayer.stop()

                indice=archivos.size-1

                mediaPlayer = MediaPlayer.create(
                    context,archivos[indice].uri
                )
                mediaPlayer.start()
                textView.text = archivos[indice].name.toString()
            }
            else
            {
                mediaPlayer.stop()


                indice--
                mediaPlayer = MediaPlayer.create(
                    context,archivos[indice].uri
                )
                mediaPlayer.start()
                textView.text = archivos[indice].name.toString()

            }

        }
    }
}