import processing.core.PApplet
import java.io.ObjectInputFilter.Config


class Simulatron : PApplet() {
    companion object {
        fun simulate() {            val sim = Simulatron()
            sim.runSketch()
        }
    }

    // Pointmasses
    private val pointMasses: ArrayList<PointMass> = ArrayList()
    private val springs: ArrayList<Spring> = ArrayList()

    private val stimulateFirstButton: Button  = Button(Vector(100f, 50f), Vector(20f, 100f), "stimulate\nfirst")
    private val lockUpMiddleButton: Button = Button(Vector(100f, 50f), Vector(130f, 100f), "stimulate\nmiddle")
    private val lockFirstPointButton: Button  = Button(Vector(100f, 50f), Vector(240f, 100f), "lock\nfirst")
    private val lockFinalPointButton: Button  = Button(Vector(100f, 50f), Vector(350f, 100f), "lock\nlast")

    private var isFinalPointLocked: Boolean = false
    private var isFirstPointLocked: Boolean = false

    private var stimulateFirstFlag: Boolean = false
    private var stimulateFirstMillis: Int = 0
    private var lockUpMiddleFlag: Boolean   = false

    private val sinusGenerator = SinusGenerator(Configuration.STIMULATION_FREQUENCY)

    init {
        val defaultDistance: Float = (Constants.WINDOW_WIDTH - Constants.FIRST_POINT_X).toFloat() / Configuration.POINT_MASS_NUM

        // create point masses
        for( i in 1..Configuration.POINT_MASS_NUM) {
            this.pointMasses.add(
                PointMass(
                    Configuration.MASS,
                    Vector( Constants.FIRST_POINT_X + defaultDistance * i, Constants.DEFAULT_Y),
                    Vector(0f,0f)
                )
            )
        }

        // create springs
        for (i in this.pointMasses.indices) {
            if(i != this.pointMasses.size - 1)
                springs.add(Spring(defaultDistance * Configuration.LENGTH_FACTOR, this.pointMasses[i], this.pointMasses[i + 1], Configuration.STRING_CONSTANT))
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

    override fun draw() {
        fullTimer = millis()

        // reset the background
        background(Constants.bgColor)

        //start stim timer
        stimTimer = millis()

        runtime = millis() - lastMillis
        lastMillis = millis()
        println("runtime: $runtime; curr sin: ${sinusGenerator.sinus(0)}")

        // Stimulate the first mass until its in rest position again
        if(this.stimulateFirstFlag) {
            // set position of first point
            this.pointMasses.first().position.y = 100 * this.sinusGenerator.sinus(Configuration.DT_MS.toInt()) + Constants.DEFAULT_Y

            // terminate
            if(sinusGenerator.sinus(0) <= 0f) {
                this.stimulateFirstFlag = false
                this.pointMasses.first().velocity.y = 0f

            }
        }

        if(this.lockUpMiddleFlag) {
            val midIndex = round(this.pointMasses.lastIndex / 2f )

            this.pointMasses[midIndex].position.y = Configuration.INDUCED_AMPLITUDE + Constants.DEFAULT_Y
            this.pointMasses[midIndex].velocity.y = 0f

            println("mid y: ${this.pointMasses[midIndex].position.y}")
            // reduce velocities
            for(point in pointMasses) {
                point.velocity.y *= .99f
            }
        }

        val simTime = (Configuration.DT_MS * Configuration.TIME_FACTOR).toInt()

        // start spring timer
        this.springTimer = millis()

        for(spring in springs) {
            spring.updatePointVelocities(simTime)
        }

        for (pointMass in pointMasses) {
            // only interact if the point is not locked
            if( ( !isFinalPointLocked || pointMass != pointMasses.last() ) &&
                (!isFirstPointLocked || pointMass != pointMasses.first() ) ) {
                pointMass.updatePosition(simTime.toFloat())
            }
            pointMass.draw(this)
        }

        // start line timer
        this.lineTimer = millis()

        stroke(255)
        // draw lines between two connected points
        for(i in 0..<pointMasses.lastIndex) {
            line(
                pointMasses[i].position.x,
                -pointMasses[i].position.y + Constants.WINDOW_HEIGHT,
                pointMasses[i+1].position.x,
                -pointMasses[i+1].position.y + Constants.WINDOW_HEIGHT,

            )
        }

        this.stimulateFirstButton.draw(this)
        this.lockUpMiddleButton.draw(this)
        this.lockFinalPointButton.draw(this)
        this.lockFirstPointButton.draw(this)

        // reset settings
        fill(255F)
        stroke(0F)
        stroke(0xFF0000)
    }

    override fun mouseClicked() {
//        newY = WINDOW_HEIGHT - mouseY

        if(stimulateFirstButton.isClicked(mouseX, mouseY)) {
//            pointMasses.first().position.y = Configuration.INDUCED_AMPLITUDE.toDouble()
            this.stimulateFirstFlag = true
            this.stimulateFirstMillis = millis()
            this.sinusGenerator.timeMs = 0
        }

        if(lockUpMiddleButton.isClicked(mouseX, mouseY)) {
            this.lockUpMiddleFlag = !this.lockUpMiddleFlag

            // update color
            if(this.lockUpMiddleFlag)
                this.lockUpMiddleButton.setColorRgb(255f, 0f, 0f)
            else
                this.lockUpMiddleButton.setColorRgb(0f, 255f, 0f)
        }


        if(lockFinalPointButton.isClicked(mouseX, mouseY)) {
            isFinalPointLocked = !this.isFinalPointLocked

            pointMasses.last().velocity.y = 0f

            if(isFinalPointLocked) {
                lockFinalPointButton.setColorRgb(255f, 0f, 0f)
            } else {
                lockFinalPointButton.setColorRgb(0f, 255f, 0f)
            }
        }

        if(lockFirstPointButton.isClicked(mouseX, mouseY)) {
            isFirstPointLocked = !this.isFirstPointLocked

            pointMasses.first().velocity.y = 0f

            // update color
            if(isFirstPointLocked)
                lockFirstPointButton.setColorRgb(255f, 0f, 0f)
            else
                lockFirstPointButton.setColorRgb(0f, 255f, 0f)
        }
    }
}