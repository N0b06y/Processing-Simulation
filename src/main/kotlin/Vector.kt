import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

open class Vector(
    var x: Double,
    var y: Double
) {

    fun angle(): Double {
        return atan2(
            y,
            x
        )
    }

    fun compareAngles(angle1: Double, angle2: Double): Double {

        var diff = angle1 - angle2

        while (diff < -2 * Math.PI) {
            diff += 2 * Math.PI
        }
        while (diff > 2 * Math.PI) {
            diff -= 2 * Math.PI
        }
        return diff
    }

    fun getLength(): Double {
        return sqrt(x.pow(2.0) + y.pow(2.0))
    }

    fun length(): Double {
        return sqrt(x.pow(2.0) + y.pow(2.0))
    }

    fun normalize(): Vector {
        return this / this.length()
    }

    // Operators
    operator fun plus(other: Vector): Vector {
        return Vector(
            this.x + other.x,
            this.y + other.y
        )
    }

    operator fun minus(gegFSum: Vector): Vector {
        return Vector(x - gegFSum.x, y - gegFSum.y)
    }

    /**
     * Decrease the length by k
     * @return new Direction
     */
    operator fun minus(k: Double): Vector {
        val newLen = this.length() - k

        val normalized = this / newLen
        // Reset the length
        val newX = normalized.x * newLen
        val newY = normalized.y * newLen

        return Vector(newX, newY)
    }

    operator fun minusAssign(direction: Vector) {
        x -= direction.x
        y -= direction.y
    }

    operator fun times(scalar: Double): Vector {
        return Vector(x * scalar, y * scalar)
    }

//    operator fun timesAssign(scalar: Double) {
//        this.x *= scalar
//        this.y *= scalar
//    }

    operator fun div(scalar: Double): Vector {
        return Vector(x / scalar, y / scalar)
    }
}