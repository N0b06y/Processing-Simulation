import Configuration.ADD_MASS_NUM
import Configuration.DRAW_STEP
import Configuration.PERIOD_TIME
import Configuration.SIM_STEP
import Configuration.STIMULATION_PERIOD_NUM
import processing.core.PApplet
import kotlin.math.pow


class Simulatron : PApplet() {
    companion object {
        fun simulate() {
            val sim = Simulatron()
            sim.runSketch()
        }
    }

    // Pointmasses
    private val pointMasses: ArrayList<PointMass> = ArrayList()
    private val springs: ArrayList<Spring> = ArrayList()

    private val stimulateFirstButton: Button = Button(Vector(100.0, 50.0), Vector(20.0, 100.0), "stimulate\nfirst")
    private val lockUpMiddleButton: Button = Button(Vector(100.0, 50.0), Vector(130.0, 100.0), "stimulate\nmiddle")
    private val lockFirstPointButton: Button = Button(Vector(100.0, 50.0), Vector(240.0, 100.0), "lock\nfirst")
    private val lockFinalPointButton: Button = Button(Vector(100.0, 50.0), Vector(350.0, 100.0), "lock\nlast")

    private var isFinalPointLocked: Boolean = false
    private var isFirstPointLocked: Boolean = false

    private var stimulateFirstFlag: Boolean = false
    private var stimulateFirstMillis: Int = 0
    private var lockUpMiddleFlag: Boolean = false

    private val sinusGenerator = SinusGenerator(Configuration.STIMULATION_FREQUENCY)

    init {
        val defaultDistance: Double =
            (Constants.WINDOW_WIDTH - Constants.FIRST_POINT_X).toDouble() / Configuration.POINT_MASS_NUM

        // create point masses
        for (i in 1..Configuration.POINT_MASS_NUM + ADD_MASS_NUM) {
            this.pointMasses.add(
                PointMass(
                    Configuration.MASS,
                    Vector(Constants.FIRST_POINT_X + defaultDistance * i, Constants.DEFAULT_Y),
                    Vector(0.0, 0.0),
                    Vector(0.0, 0.0),
                )
            )
        }

        // create springs
        for (i in this.pointMasses.indices) {
            if (i != this.pointMasses.size - 1)
                springs.add(
                    Spring(
                        defaultDistance * Configuration.LENGTH_FACTOR,
                        this.pointMasses[i],
                        this.pointMasses[i + 1],
                        Configuration.STRING_CONSTANT
                    )
                )
        }
    }

    override fun settings() {
        size(Constants.WINDOW_WIDTH + 50, Constants.WINDOW_HEIGHT)
    }

    override fun setup() {
        stroke(Constants.rStroke, Constants.gStroke, Constants.bStroke)

        fill(255F)
        stroke(0F)
        stroke(0xFF0000)
    }

    private var lastMillis = -1
    private var runtime = -1
    private var newY = -1

    var stimTimer = 0
    var springTimer = 0
    var pointMassTime = 0
    var lineTimer = 0
    var buttonTimer = 0
    var fullTimer = 0

    var energy = 0.0

    override fun draw() {
        val preCalculationNum = (DRAW_STEP / SIM_STEP).toInt()
//        println("pre calc num: $preCalculationNum")

        val simTime = if (lockUpMiddleFlag)
            50.0
        else
            SIM_STEP * Configuration.TIME_FACTOR


        // calc not shown steps
        for (i in 0..preCalculationNum) {
            fullTimer = millis()


            //start stim timer
            stimTimer = millis()

            runtime = millis() - lastMillis
            lastMillis = millis()
//            println("runtime: $runtime; curr sin: ${sinusGenerator.sinus(.0)}")

            // Stimulate the first mass until its in rest position again
            if (this.stimulateFirstFlag) {
                // set position of first point
                this.pointMasses.first().position.y =
                    100 * this.sinusGenerator.sinus(SIM_STEP) + Constants.DEFAULT_Y

                // terminate
                if (sinusGenerator.timeMs/1000 >= STIMULATION_PERIOD_NUM * PERIOD_TIME) {
                    this.stimulateFirstFlag = false
                    this.pointMasses.first().velocity.y = 0.0

                }
            }

            if (this.lockUpMiddleFlag) {
                val midIndex = round(this.pointMasses.lastIndex / 2f)

                this.pointMasses[midIndex].position.y = Configuration.INDUCED_AMPLITUDE + Constants.DEFAULT_Y
                this.pointMasses[midIndex].velocity.y = 0.0

                println("mid y: ${this.pointMasses[midIndex].position.y}")
                // reduce velocities
                for (point in pointMasses) {
                    point.velocity.y *= .99f
                }
            }


            // start spring timer
            this.springTimer = millis()

            // velocity verlet
            for (point in pointMasses)
                point.resetForce()

            for (spring in springs)
                spring.updateForce()

            for (point in pointMasses)
                point.applyDampingForce()

            for (pointMass in pointMasses) {
                // only interact if the point is not locked
                if ((!isFinalPointLocked || pointMass != pointMasses.last()) &&
                    (!isFirstPointLocked || pointMass != pointMasses.first())
                ) {
                    pointMass.updatePosition(simTime)
                }
            }

            // update last because position needs to be calculated with old speed and acceleration
            for (spring in springs) {
                spring.updateVelocities(simTime)
            }
        }

        // update ui

        // reset the background
        background(Constants.bgColor)
//        println("update ui")

        for (pointMass in pointMasses) {
            pointMass.draw(this)
        }


        // start line timer
        this.lineTimer = millis()

        stroke(255)
        // draw lines between two connected points
        for (i in 0..<pointMasses.lastIndex) {
            line(
                pointMasses[i].position.x.toFloat(),
                (-pointMasses[i].position.y + Constants.WINDOW_HEIGHT).toFloat(),
                pointMasses[i + 1].position.x.toFloat(),
                (-pointMasses[i + 1].position.y + Constants.WINDOW_HEIGHT).toFloat(),

                )
        }

        this.stimulateFirstButton.draw(this)
        this.lockUpMiddleButton.draw(this)
        this.lockFinalPointButton.draw(this)
        this.lockFirstPointButton.draw(this)

        // calculate energy
        energy = 0.0
        for(point in pointMasses) {
            energy += .5 * point.mass * point.velocity.length().pow(2.0)
        }

        for(spring in springs) {
            energy += .5 * spring.springConstant * spring.deltaS().pow(2.0)
        }

        println("energy2: $energy")

        // reset settings
        fill(255F)
        stroke(0F)
        stroke(0xFF0000)
    }

    override fun mouseClicked() {
//        newY = WINDOW_HEIGHT - mouseY

        if (stimulateFirstButton.isClicked(mouseX, mouseY)) {
//            pointMasses.first().position.y = Configuration.INDUCED_AMPLITUDE.toDouble()
            this.stimulateFirstFlag = true
            this.stimulateFirstMillis = millis()
            this.sinusGenerator.timeMs = 0.0
        }

        if (lockUpMiddleButton.isClicked(mouseX, mouseY)) {
            this.lockUpMiddleFlag = !this.lockUpMiddleFlag

            // update color
            if (this.lockUpMiddleFlag)
                this.lockUpMiddleButton.setColorRgb(255.0, 0.0, 0.0)
            else
                this.lockUpMiddleButton.setColorRgb(0.0, 255.0, 0.0)
        }


        if (lockFinalPointButton.isClicked(mouseX, mouseY)) {
            isFinalPointLocked = !this.isFinalPointLocked

            pointMasses.last().velocity.y = 0.0

            if (isFinalPointLocked) {
                lockFinalPointButton.setColorRgb(255.0, 0.0, 0.0)
            } else {
                lockFinalPointButton.setColorRgb(0.0, 255.0, 0.0)
            }
        }

        if (lockFirstPointButton.isClicked(mouseX, mouseY)) {
            isFirstPointLocked = !this.isFirstPointLocked

            pointMasses.first().velocity.y = 0.0

            // update color
            if (isFirstPointLocked)
                lockFirstPointButton.setColorRgb(255.0, 0.0, 0.0)
            else
                lockFirstPointButton.setColorRgb(0.0, 255.0, 0.0)
        }
    }
}