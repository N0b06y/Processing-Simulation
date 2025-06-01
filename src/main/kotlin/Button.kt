import processing.core.PApplet
import kotlin.math.roundToLong

class Button (
    val size: Vector = Vector(50f, 50f),
    val position: Vector = Vector(0f, 0f),
    var text: String = "",
    var r: Float = 0f,
    var g: Float = 255f,
    var b: Float = 0f
    ){
    fun draw(scope: PApplet) {
        scope.fill(this.r, this.g, this.b)
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

    fun setColorRgb(r: Float, g: Float, b: Float) {
        this.r = r
        this.g = g
        this.b = b
    }

    fun setLabel(text: String) {
        this.text = text
    }
}