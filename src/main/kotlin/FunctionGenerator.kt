import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Funktionsgenerator um die erste Punktmasse anzuregen
 */

class SinusGenerator(
    private val frequency: Double,
    var timeMs: Double = 0.0
) {
    fun sinus(dtMs: Double): Double {
        this.timeMs += dtMs
        return sin((2 * PI * this.frequency) * this.timeMs / 1000f)
    }

    fun cosinus(dtMs: Int): Double {
        this.timeMs += dtMs
        return cos((2 * PI * this.frequency) * this.timeMs / 1000f)
    }
}