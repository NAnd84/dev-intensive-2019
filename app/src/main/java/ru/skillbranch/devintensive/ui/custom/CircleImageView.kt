package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.annotation.Dimension.DP
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.utils.Utils
import kotlin.math.min


class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defstyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defstyleAttr) {

    companion object {
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH = 2
    }

    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = Utils.convertDpToPx(DEFAULT_BORDER_WIDTH, context)

    private var bitmap: Bitmap? = null
    private var bitmapShader: BitmapShader? = null
    private var centerX: Float = 0f
    private var centerY: Float = 0f
    private var radius: Float = 0f
    private val shaderMatrix = Matrix()
    private val drawableRect = RectF()

    private val borderRect = RectF()
    private var bitmapWidth: Int = 0
    private var bitmapHeight: Int = 0

    private val bitmapPaint: Paint = Paint().apply {
        color = Color.BLUE
        isAntiAlias = true
    }
    private val borderPaint: Paint = Paint().apply {
        color = DEFAULT_BORDER_COLOR
        style = Paint.Style.STROKE
        strokeWidth = DEFAULT_BORDER_WIDTH.toFloat()
        isAntiAlias = true
    }


    init {
        if(attrs != null){
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defstyleAttr, 0)
            borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)
            borderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_cv_borderWidth, DEFAULT_BORDER_WIDTH)
            a.recycle()
        }

        initializeBitmap()
    }



    fun setBorderWidth(@Dimension(unit = DP) dp: Int) {
        borderWidth = Utils.convertDpToPx(dp, context)
        invalidate()
    }


    @Dimension(unit = DP)
    fun getBorderWidth(): Int = Utils.convertPxToDp(borderWidth, context)

    fun setBorderColor(hex: String) {
        borderColor = Color.parseColor(hex)
        invalidate()
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        borderColor = ContextCompat.getColor(context, colorId)
        invalidate()
    }

    fun getBorderColor(): Int = borderColor

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateBitmapPaint()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        updateBitmapPaint()
    }

    override fun onDraw(canvas: Canvas) {

        canvas.drawCircle(centerX, centerY, radius, bitmapPaint)
        if(borderWidth > 0)
            canvas.drawCircle(centerX, centerY, radius, borderPaint)
    }

    private fun updateBitmapPaint() {
        if (width == 0 && height == 0) return

        if (bitmap == null) {
            invalidate()
            return
        }

        bitmapShader = BitmapShader(bitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        bitmapPaint.isAntiAlias = true
        bitmapPaint.shader = bitmapShader

        bitmapHeight = bitmap!!.height
        bitmapWidth = bitmap!!.width

        centerX = layoutParams.width / 2f
        centerY = layoutParams.height / 2f
        radius = min(layoutParams.width, layoutParams.height) / 2f

        radius -= borderWidth / 2f

        borderRect.set(calculateBounds())
        drawableRect.set(borderRect)

        updateShaderMatrix()
        invalidate()
    }

    private fun calculateBounds(): RectF {
        val availableWidth = width - paddingLeft - paddingRight
        val availableHeight = height - paddingTop - paddingBottom

        val sideLength = min(availableWidth, availableHeight)

        val left = paddingLeft + (availableWidth - sideLength) / 2f
        val top = paddingTop + (availableHeight - sideLength) / 2f

        return RectF(left, top, left + sideLength, top + sideLength)
    }


    private fun updateShaderMatrix() {
        val scale: Float
        var dx = 0f
        var dy = 0f

        shaderMatrix.set(null)

        if (bitmapWidth * drawableRect.height() > drawableRect.width() * bitmapHeight) {
            scale = drawableRect.height() / bitmapHeight.toFloat()
            dx = (drawableRect.width() - bitmapWidth * scale) * 0.5f
        } else {
            scale = drawableRect.width() / bitmapWidth.toFloat()
            dy = (drawableRect.height() - bitmapHeight * scale) * 0.5f
        }

        shaderMatrix.setScale(scale, scale)
        shaderMatrix.postTranslate((dx + 0.5f).toInt() + drawableRect.left, (dy + 0.5f).toInt() + drawableRect.top)

        bitmapShader!!.setLocalMatrix(shaderMatrix)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        initializeBitmap()
    }

    override fun setImageResource(@DrawableRes resId: Int) {
        super.setImageResource(resId)
        initializeBitmap()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        initializeBitmap()
    }

    private fun initializeBitmap() {
        bitmap = getBitmapFromDrawable(drawable)
        updateBitmapPaint()
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? = when (drawable) {
        null -> null
        is BitmapDrawable -> drawable.bitmap
        else -> {
            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }


    fun drawDefaultAvatar(initials: String, textSize: Float = 72f, textColor: Int = Color.WHITE): Bitmap {

        val textPaint: Paint = Paint().apply {
            isAntiAlias = true
            this.textSize = textSize
            color = textColor
            textAlign = Paint.Align.CENTER
        }

        val textBounds = Rect()
        textPaint.getTextBounds(initials, 0, initials.length, textBounds)
        val backgroundBounds = RectF()
        backgroundBounds.set(0f, 0f, layoutParams.width.toFloat(), layoutParams.height.toFloat())
        val textBottom = backgroundBounds.centerY() - textBounds.exactCenterY()

        val image = Bitmap.createBitmap(layoutParams.width, layoutParams.height, Bitmap.Config.ARGB_8888)
        image.eraseColor(Utils.getThemeAccentColor(context))
        val canvas = Canvas(image)
        canvas.drawText(initials, backgroundBounds.centerX(), textBottom, textPaint)


        return image
    }



}