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
  
  private var frame:JFrame = _
  
  private var width = 640
  private var height = 480
  private var title = "Simple2DViewer"
    
  params.put("width", width.toString)
  params.put("height", height.toString)
  params.put("title", title)
  
  val canvas = new GraphCanvas()
    
  private var source : LinkedList[Seq[Double]] = new LinkedList[Seq[Double]]()
  
  def setSource(s:Seq[AnyVal]):Unit = {
    source = s.asInstanceOf[Seq[Double]] +: source
  }
  
  def getDestination():Seq[AnyVal] = null

  def set_parameters():Unit = {
    title = params.get("title") match {
      case Some(s) => s
      case _ => title
    }
    width = params.get("width") match {
      case Some(v) => v.toInt
      case _ => width
    }
    height = params.get("height") match {
      case Some(v) => v.toInt
      case _ => height
    }
  }
  
  def init():Unit = {
    set_parameters()    
    frame = new JFrame(title)
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.setSize(width, height)
    frame.getContentPane().add(canvas)
    frame.pack()
    frame.setVisible(true)
  }
  
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
