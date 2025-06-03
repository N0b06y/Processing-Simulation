import processing.core.PApplet
import kotlin.math.pow
import kotlin.math.sqrt

class PointMass(
    val mass: Double,        // kg
    var position: Vector,   // m
    var velocity: Vector    // m/s
    ) {

    private var xLocked = true

    fun updatePosition(dtMs: Double) {
        val dt: Double = dtMs / 1000
        if(!this.xLocked)
            position.x += velocity.x * dt
        position.y += velocity.y * dt
    }

    fun position(): Vector {
        return position
    }

    /**
     * @param force Newton
     */
    fun applyForce(force: Vector, dtMs: Double) {
        val acceleration = force / this.mass
        val deltaVelocity = acceleration * dtMs / 1000.0
        val newVelocity = this.velocity + deltaVelocity
        newVelocity.x = .0
        this.velocity = newVelocity
    }

    /**
     * Decrease velocity by factor
     */
    fun applyFrictionFactor(factor: Double) {
        velocity *= (1-factor)
    }

    fun lockX(){ xLocked = true }
    fun unlockX(){ xLocked = false }
    /**
     * Decrease velocity by constant acceleration
     */
    fun applyFrictionKonstant(constant: Double) {

        if( (velocity - constant).normalize().x == velocity.normalize().x
            || (velocity - constant).normalize().y == velocity.normalize().y )
            velocity -= constant
        else
            velocity = Vector(0.0, 0.0)
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
}