import Configuration.DAMPING_COEFFICIENT
import processing.core.PApplet
import kotlin.math.pow
import kotlin.math.sqrt

class PointMass(
    val mass: Double,        // kg
    var position: Vector,   // m
    var velocity: Vector,    // m/s
    var force0: Vector,         // calculated with values from the previous step
    ) {

    private var xLocked = true

    fun updatePosition(dtMs: Double) {
        val dt: Double = dtMs / 1000
        if(!this.xLocked)
            position.x += velocity.x * dt

        this.position.y += this.velocity.y * dt + .5 * this.force0.y /this.mass * dt.pow(2)
    }

    /**
     * @param forceOld Newton
     */
    fun updateSpeed(forceOld: Vector, dtMs: Double) {
        this.velocity.y += forceOld.y / this.mass * dtMs/1000.0
    }

    fun distance(other: PointMass): Double {
        return sqrt(
            (this.position.x - other.position.x).pow(2.0)
            + (this.position.y - other.position.y).pow(2.0)
        )
    }

    fun draw(scope: PApplet) {
        scope.ellipse(
            position.x.toFloat(),
            -position.y.toFloat() + Constants.WINDOW_HEIGHT,
            Constants.DEFAULT_RADIUS, Constants.DEFAULT_RADIUS,
        )
    }

    fun resetForce() {
        this.force0 = Vector(0.0, 0.0)
    }

    fun applyDampingForce() {
        this.force0 += this.velocity * (-DAMPING_COEFFICIENT)
    }
}