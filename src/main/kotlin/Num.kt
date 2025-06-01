import kotlin.math.pow

object Num {
    fun sqrt(x: Float): Float {
        return 0.0265f * x.pow(3) - 0.3125f * x.pow(2) + 0.9375f * x + 0.3125f
//        return Math.sqrt(x)
    }
}