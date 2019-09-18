package dk.martin.luckywheel

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import dk.adaptmobile.amkotlinutil.extensions.rotate
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.atan2

class MainActivity : AppCompatActivity() {

    var viewRotation: Float = 0.toFloat()
    var fingerRotation: Double = 0.toDouble()
    var newFingerRotation: Double = 0.toDouble()
    var currentFingerRotation: Double = 0.toDouble()
    var clockwise = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        imageView.load(R.drawable.lucky_wheel) // using coil (https://coil-kt.github.io/coil/) to load image into image view

        // it is a lot easier to attach the OnTouchListener to a parent view of the view you want to rotate,
        // so that the coordinate of your touch event is not modified by the rotation itself.
        // https://stackoverflow.com/a/20758552/8480777
        parentView.setOnTouchListener { _, event ->

            val touchX = event.x
            val touchY = event.y

            val centerX = parentView.width / 2f
            val centerY = parentView.height / 2f

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.e("ACTION", "ACTION_DOWN")

                    imageView.animate().cancel()

                    viewRotation = imageView.rotation

                    // Set the rotation in degrees
                    fingerRotation =
                        Math.toDegrees(atan2(touchX - centerX, centerY - touchY).toDouble())

                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    Log.e("ACTION", "ACTION_MOVE")

                    newFingerRotation =
                        Math.toDegrees(atan2(touchX - centerX, centerY - touchY).toDouble())

                    // Set the direction of the wheel's rotation
                    clockwise = currentFingerRotation < newFingerRotation

                    // Used for calculating if clockwise or counter clockwise
                    currentFingerRotation = newFingerRotation

                    // Set the rotation from touch
                    imageView.rotation =
                        (viewRotation + newFingerRotation - fingerRotation).toFloat()

                    true
                }

                MotionEvent.ACTION_UP -> {
                    Log.e("ACTION", "ACTION_UP")

                    if (clockwise) {
                        imageView.rotate(
                            rotation = 2000f,
                            animationDuration = 4500,
                            interpolator = DecelerateInterpolator()
                        )
                    } else {
                        imageView.rotate(
                            rotation = -2000f,
                            animationDuration = 4500,
                            interpolator = DecelerateInterpolator()
                        )
                    }

                    true
                }

                else -> true
            }
        }
    }
}
