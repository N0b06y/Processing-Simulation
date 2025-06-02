import processing.core.PApplet
import kotlin.math.pow
import kotlin.math.sqrt

class PointMass(
    private val mass: Float,        // kg
    var position: Vector,   // m
    var velocity: Vector    // m/s
    ) {

    private var xLocked = true

    fun updatePosition(dtMs: Float) {
        val dt: Float = dtMs / 1000
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
    fun applyForce(force: Vector, dtMs: Int) {
        val acceleration = force / this.mass
        val deltaVelocity = acceleration * dtMs.toFloat() / 1000f
        val newVelocity = this.velocity + deltaVelocity
        this.velocity = newVelocity
    }

    /**
     * Decrease velocity by factor
     */
    fun applyFrictionFactor(factor: Float) {
        velocity *= (1-factor)
    }

    fun lockX(){ xLocked = true }
    fun unlockX(){ xLocked = false }
    /**
     * Decrease velocity by constant acceleration
     */
    fun applyFrictionKonstant(constant: Float) {

        if( (velocity - constant).normalize().x == velocity.normalize().x
            || (velocity - constant).normalize().y == velocity.normalize().y )
            velocity -= constant
        else
            velocity = Vector(0f, 0f)
    }

    fun distance(other: PointMass): Float {
        return sqrt(
            (this.position.x - other.position.x).pow(2)
            + (this.position.y - other.position.y).pow(2)
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