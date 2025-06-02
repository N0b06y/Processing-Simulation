import kotlin.math.PI

object Configuration {
    const val INDUCED_AMPLITUDE: Int    = 300
    const val MASS: Float               = .5f
    const val POINT_MASS_NUM: Int       = 1000
    const val STRING_CONSTANT: Float    = 50f   // D
    const val TIME_FACTOR: Float        = 1f

    const val DT_MS = 1f

    const val STIMULATION_FREQUENCY = .1f
    const val STIMULATION_PERIOD    = 2 * PI * STIMULATION_FREQUENCY

    // tension of the strings
    private const val TENSION_FACTOR: Float = .7f
    const val LENGTH_FACTOR: Float = 1f - TENSION_FACTOR
}