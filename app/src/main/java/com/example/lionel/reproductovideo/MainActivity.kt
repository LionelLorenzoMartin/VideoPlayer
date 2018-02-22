package com.example.lionel.reproductovideo

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import java.util.*







class MainActivity : AppCompatActivity() {

    var recVi: RecyclerView? = null
    var arrayV: ArrayList<Video>?= null
    var layoutManager: RecyclerView.LayoutManager?= null
    var pos=-1
    var adaptador: Adaptador?= null
    val PERMISSION_REQUEST_CODE = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selector)




        recVi=findViewById<View>(R.id.rv) as RecyclerView
        registerForContextMenu(recVi)

        recVi?.addOnItemTouchListener(RecyclerTouchListener(this,
                recVi!!, object : ClickListener {
            override fun onClick(view: View, position: Int) {
                pos = position
                startSegundoActivity()
            }

            override fun onLongClick(view: View, position: Int) {
                pos = position
            }
        }))

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recVi?.setLayoutManager(layoutManager)
        arrayV = ArrayList<Video>()

        //val filePlace = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)

        if (ContextCompat.checkSelfPermission(applicationContext,android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }else{
            cargarDatos()
        }
    }

    fun startSegundoActivity() {
        val i = Intent(this, Main2Activity::class.java)
        i.putExtra("ruta", arrayV?.get(pos)?.mPath)
        startActivity(i)
    }

    interface ClickListener {
        fun onClick(view: View, position: Int)
        fun onLongClick(view: View, position: Int)
    }

    internal inner class RecyclerTouchListener(context: Context, recycleView: RecyclerView, private val clicklistener: ClickListener?) : RecyclerView.OnItemTouchListener {
        private val gestureDetector: GestureDetector

        init {
            gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val child = recycleView.findChildViewUnder(e.x, e.y)
                    if (child != null && clicklistener != null) {
                        clicklistener!!.onLongClick(child, recycleView.getChildAdapterPosition(child))
                    }
                }
            })
        }

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            val child = rv.findChildViewUnder(e.x, e.y)
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener!!.onClick(child, rv.getChildAdapterPosition(child))
            }

            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        /*var movieCursor: Cursor? = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null)*/
    }

    fun cargarDatos(){
        var movieCursor: Cursor? = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null)
        while (movieCursor!=null && movieCursor.moveToNext()){
            var songName = movieCursor.getString(movieCursor.getColumnIndex(MediaStore.Video.Media.TITLE))
            var songPath = movieCursor.getString(movieCursor.getColumnIndex(MediaStore.Video.Media.DATA))
            arrayV!!.add(Video(songName, songPath))
        }
        adaptador = Adaptador(arrayV!!)
        var layoutManger = LinearLayoutManager(applicationContext)
        recVi!!.layoutManager = layoutManger
        recVi!!.adapter = adaptador
    }


}