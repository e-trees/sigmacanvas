package sigmacanvas.tools

import sigmacanvas.utils.PacketUtils
import sigmacanvas.base.SigmaCanvasItem
import sigmacanvas.base.SigmaCanvasMessage

class HexDump extends SigmaCanvasItem{
  
  def init():Unit = {}
  
  def run(m:SigmaCanvasMessage):Unit = {
    val a = m.obj.data
    for((b, i) <- a.zipWithIndex){
    	if(i % 8 == 0) printf("%08x:", i)
    	printf(" %02x", b)
    	if(i % 8 == 7) printf("\n")
    }
  }
  
  def data:Seq[AnyVal] = null
  
  def wakeup():Unit = {}

}