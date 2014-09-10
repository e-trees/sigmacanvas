package sigmacanvas.tools

import sigmacanvas.utils.PacketUtils
import sigmacanvas.base.SigmaCanvasItem

class HexDump extends SigmaCanvasItem{
  
  private var source:Seq[Byte] = _
  
  def init():Unit = {
  }
  
  def run():Unit = {
    if(source == null) return
    for((b, i) <- source.zipWithIndex){
      if(i % 8 == 0) printf("%08x:", i)
      printf(" %02x", b)
      if(i % 8 == 7) printf("\n")
    }
  }

  def setSource(s:Seq[AnyVal]) = {source = s.asInstanceOf[Seq[Byte]]}
  def getDestination():Seq[AnyVal] = null

}