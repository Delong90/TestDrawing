package com.example.testdrawing

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.SurfaceHolder
import android.view.SurfaceView

class CustomSurfaceView(context: Context):SurfaceView(context),SurfaceHolder.Callback {

    private val drawingThread = SurfaceDrawingThread(holder)

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        drawingThread.start() //запускаем поток
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        try {
            drawingThread.join()//ждёт пока закончится запущенный поток
        }catch (e:InterruptedException){

        }
    }

    // если обновляется класс serface  и с ним происходят какие то изменения
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }


}

class SurfaceDrawingThread(private val surfaceHolder: SurfaceHolder): Thread(){
    private val circlePainter = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val eyesMouthPainter = Paint().apply {
        color = Color.BLUE
        isAntiAlias = true
        strokeWidth = 30f
    }

    override fun run() {
        val canvas: Canvas? = surfaceHolder.lockCanvas()   //блокировка канваса
        canvas?.apply {
            val centerX = (width/2).toFloat()
            val centerY = (height/2).toFloat()
            drawColor(Color.WHITE)
            drawCircle(centerX,centerY, SMILE_RADIUS,circlePainter)
            drawCircle(centerX-100,centerY-100, EYE_RADIUS,eyesMouthPainter)
            drawCircle(centerX+100,centerY-100, EYE_RADIUS,eyesMouthPainter)
            drawLine(centerX-130,centerY+130,centerX+130,centerY+130,eyesMouthPainter)
        }
        surfaceHolder.unlockCanvasAndPost(canvas)   //разблокировка канваса и пост его в мейн поток
    }
    private companion object{
        private const val SMILE_RADIUS = 300f
        private const val EYE_RADIUS = 35f

    }
}
