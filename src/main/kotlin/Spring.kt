import kotlin.math.absoluteValue

/**
 * Represents a physical spring connecting two point masses in a simulation.
 *
 * The `Spring` class models a spring according to Hooke's Law, maintaining a rest length between two connected
 * `PointMass` objects. It calculates and applies forces to the connected points to simulate spring dynamics.
 *
 * @property length The rest length of the spring (s₀), i.e., the distance at which the spring is neither compressed nor stretched.
 * @property springConstant The spring constant (D), representing the stiffness of the spring.
 * @constructor Creates a spring connecting two `PointMass` objects with a specified rest length and spring constant.
 * @param length The rest length of the spring.
 * @param point0 The first point mass connected to the spring.
 * @param point1 The second point mass connected to the spring.
 * @param springConstant The spring constant (stiffness).
 *
 * Main features:
 * - Maintains a list of the two connected `PointMass` objects.
 * - Calculates the force exerted by the spring on each point using Hooke's Law.
 * - Updates the velocities of the connected points based on the calculated forces.
 *
 * Usage:
 * - Call [updatePointVelocities] each simulation step to update the velocities of the connected points.
 * - The internal [getForce] method computes the force vector between the two points.
 */
class Spring(
    private val length: Double,          // s_0
    point0: PointMass,
    point1: PointMass,
    val springConstant: Double,  // D
) {
    // points the spring is connected to
    private val points: ArrayList<PointMass> = arrayListOf(point0, point1)

    /**
     * Calculates the spring force vector exerted between two points in the spring system.
     *
     * This function computes the force that should be applied to maintain the spring's rest length
     * between two points, according to Hooke's Law. The force is proportional to the difference
     * between the current distance and the spring's rest length, scaled by the spring constant.
     * The direction of the force is from `fromIndex` to `toIndex`.
     *
     * @param fromIndex The index of the first point in the `points` list.
     * @param toIndex The index of the second point in the `points` list.
     * @return The force vector to be applied from `fromIndex` towards `toIndex`.
     *
     * The calculation steps are:
     * 1. Compute the current distance between the two points.
     * 2. Calculate the magnitude of the force using Hooke's Law: F = k * |x - x₀|,
     *    where `k` is the spring constant, `x` is the current distance, and `x₀` is the rest length.
     * 3. Determine the direction of the force as a unit vector from `fromIndex` to `toIndex`.
     * 4. Return the force vector as the product of the direction and the force magnitude.
     */
    private fun getForce(fromIndex: Int, toIndex: Int): Vector {
        val distance: Vector = points[toIndex].position - points[fromIndex].position
        // Calculate the amount of the force to apply
        val force: Vector = (distance - this.length) * springConstant

        return force
    }

    private fun getDistance(): Double {
        return points[0].distance(points[1])
    }
    fun deltaS(): Double {
        return (this.getDistance() - this.length).absoluteValue
    }

    fun updateVelocities(dtMs: Double) {
        for (point in points) {
            point.updateSpeed(point.force0, dtMs)
        }
    }

    fun updateForce0() {

        // begin force
        val force = this.getForce(0, 1)
        this.points.first().force0 += force
        this.points.last().force0  -= force
    }
}