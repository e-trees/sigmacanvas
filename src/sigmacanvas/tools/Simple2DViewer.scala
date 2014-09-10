package sigmacanvas.tools

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import scala.collection.mutable.LinkedList
import scala.collection.mutable.Queue
import javax.swing.JFrame
import javax.swing.JPanel
import sigmacanvas.utils.PacketUtils
import sigmacanvas.base.SigmaCanvasItem

class Simple2DViewer() extends SigmaCanvasItem{
  
  val frame = new JFrame("Simple2DViewer")
  val width = 640
  val height = 480
  
  val canvas = new GraphCanvas()
  
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  frame.setSize(width, height)
  frame.getContentPane().add(canvas)
  frame.pack()
  frame.setVisible(true)
  
  private var source : LinkedList[Seq[Double]] = new LinkedList[Seq[Double]]()
  
  def setSource(s:Seq[AnyVal]):Unit = {
    source = s.asInstanceOf[Seq[Double]] +: source
  }
  
  def getDestination():Seq[AnyVal] = null

  def init():Unit = {}
  
  def run():Unit = {
    canvas.repaint()
  }
  
  class GraphCanvas() extends JPanel {
    
    super.setSize(width, height)
    super.setPreferredSize(new Dimension(width, height))
    
    val colors:Array[Color] = Array(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW)
    
    def paintGraph(g:Graphics, c:Color, data:Seq[Int]):Unit = {
      if(data == null) return
      var prev = 0
      g.setColor(c)
      for((d, i) <- data.zipWithIndex){
        if(i > 0) g.drawLine(i, prev, i+1, d)
        prev = d
      }
    }
    
    def paintGraphAll(g:Graphics):Unit = {
      for((seq, i) <- source.zipWithIndex){
        if(seq != null){
          val data = for(v <- seq; py = height - v * height) yield(py.toInt)
          val c = colors(i)
          paintGraph(g, c, data)
        }
      }
    }
    
    override def paintComponent(g:Graphics) = {
      super.paintComponent(g)
      if (source != null) paintGraphAll(g)
    }
  }
  
}
