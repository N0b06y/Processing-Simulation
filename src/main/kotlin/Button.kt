import processing.core.PApplet
import kotlin.math.roundToLong

class Button (
    val size: Vector = Vector(50.0, 50.0),
    val position: Vector = Vector(0.0, 0.0),
    var text: String = "",
    var r: Double = 0.0,
    var g: Double = 255.0,
    var b: Double = 0.0
    ){
    fun draw(scope: PApplet) {
        scope.fill(this.r.toFloat(), this.g.toFloat(), this.b.toFloat())
        scope.rect(position.x.toFloat(), position.y.toFloat(), size.x.toFloat(), size.y.toFloat())
        // draw text
        scope.fill(0);
//        scope.textAlign(CENTER, CENTER)
        scope.text(this.text, position.x.toFloat(), (position.y + size.y / 2).toFloat())
    }
    fun isClicked(mouseX: Int, mouseY: Int): Boolean {
        return if(mouseX.toDouble() in position.x..position.x + size.x
            && mouseY.toDouble() in position.y..position.y + size.y)
            true
        else
            false
    }

    fun setColorRgb(r: Double, g: Double, b: Double) {
        this.r = r
        this.g = g
        this.b = b
    }

    fun setLabel(text: String) {
        this.text = text
    }
}