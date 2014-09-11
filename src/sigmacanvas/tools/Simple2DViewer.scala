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
import scala.collection.mutable.HashMap
import sigmacanvas.base.SigmaCanvasMessage

class Simple2DViewer() extends SigmaCanvasItem{
  
  private var frame:JFrame = _
  
  private var width = 640
  private var height = 480
  private var title = "Simple2DViewer"
    
  setParam("width", width.toString)
  setParam("height", height.toString)
  setParam("title", title)
  
  val canvas = new GraphCanvas()
    
  private var source : HashMap[String, Seq[Double]] = new HashMap[String, Seq[Double]]()
  
  def getDestination():Seq[AnyVal] = null

  def set_parameters():Unit = {
    title = getParam("title") match {
      case Some(s) => s
      case _ => title
    }
    width = getParam("width") match {
      case Some(v) => v.toInt
      case _ => width
    }
    height = getParam("height") match {
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

  def run(m:SigmaCanvasMessage):Unit = {
    source.put(m.obj.id, m.obj.data.asInstanceOf[Seq[Double]])
    canvas.repaint()
  }
  
  def wakeup():Unit = {}
  
  def data():Seq[AnyVal] = null
  
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
      for(((k,seq), i) <- source.zipWithIndex){
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
