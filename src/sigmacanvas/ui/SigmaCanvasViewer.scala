package sigmacanvas.ui

import java.io.ByteArrayInputStream

object SigmaCanvasViewer {
  
  def main(args:Array[String]):Unit = {
    val src = XML2Dot.load(args(0))
    val pane = new SigmaCanvasGraphPane("/opt/local/bin/dot")
    pane.view(new ByteArrayInputStream(src.getBytes()))
  }
  
}