import kotlin.math.PI

object Configuration {
    const val INDUCED_AMPLITUDE: Int    = 300
    const val MASS: Double               = .5
    const val POINT_MASS_NUM: Int       = 300
    const val STRING_CONSTANT: Double    = 50.0   // D
    const val TIME_FACTOR: Double        = 1.0


    const val SIM_STEP = 1.0
    const val DRAW_STEP = 20

    const val STIMULATION_FREQUENCY = .1
    const val STIMULATION_PERIOD    = 2 * PI * STIMULATION_FREQUENCY

    // tension of the strings
    private const val TENSION_FACTOR: Double = .7
    const val LENGTH_FACTOR: Double = 1 - TENSION_FACTOR
}