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
        val distance = points[fromIndex].distance(points[toIndex])
        // Calculate the amount of the force to apply
        val force = (distance - this.length).absoluteValue * springConstant

        // Calculate the direction of the force
        val dir = (points[toIndex].position() - points[fromIndex].position()) /
                (points[toIndex].position() - points[fromIndex].position()).length()

        return dir * force
    }

    private fun getDistance(): Double {
        return points[0].distance(points[1])
    }

    fun deltaS(): Double {
        return (this.getDistance() - this.length).absoluteValue
    }

    /**
     * Updates the velocities of all points in the spring system based on the forces exerted by neighboring points.
     *
     * For each point in the `points` list, this function calculates and applies the spring force from its adjacent points
     * (previous and next, if they exist). The force is computed using the `getForce` method, and then applied to the point
     * using its `applyForce` method. The time step `dtMs` is used to scale the velocity update.
     *
     * The update process is as follows:
     * - For each point:
     *   - If it is not the last point, apply the force from the next point.
     *   - If it is not the first point, apply the force from the previous point.
     *   - The magnitude of the force applied from the previous point is printed for debugging.
     *
     * @param dtMs The time step in milliseconds over which to update the velocities.
     */
    fun updatePointVelocities(dtMs: Double) {
        for (i in points.indices) {

            // apply the force to the following point
            if(i != points.lastIndex) {
                points[i].applyForce(
                    this.getForce(i, i + 1), dtMs
                )
            }
            // apply the force to the previous point
            if(i != 0) {
                points[i].applyForce(
                    this.getForce(i, i - 1), dtMs
                )
//                print("Force: ${this.getForce(i, i - 1).length()}\n")
            }
        }
    }
}