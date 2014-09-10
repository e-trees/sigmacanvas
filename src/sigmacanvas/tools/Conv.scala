package sigmacanvas.tools

import scala.collection.mutable.Queue
import scala.collection.mutable.SynchronizedQueue

import sigmacanvas.base.SigmaCanvasItem
import sigmacanvas.utils.PacketUtils
import sigmacanvas.utils.SysUtils

class ConvByteToShort extends SigmaCanvasItem{
  
  private var source:Seq[Byte] = _
  private val destination:Array[Short] = new Array[Short](128) 
  
  def init():Unit = {
  }
  
  def run():Unit = {
    if(source == null) return
    for(i <- 0 until 128; j = 2 * i + 8){
      destination(i) = PacketUtils.ntohs(source, j)
    }
  }

  def setSource(s:Seq[AnyVal]) = {source = s.asInstanceOf[Seq[Byte]]}
  def getDestination():Seq[AnyVal] = destination

}

class ConvAnyValueToDouble extends SigmaCanvasItem{
  
  private var source:Seq[AnyVal] = _
  private val destination:Queue[Double] = new SynchronizedQueue[Double]()
  
  private var conv:AnyVal=>Double = _
  private var cond:Int=>Boolean = _
  
  private var init_done = false
  
  def setSource(s:Seq[AnyVal]) = {source = s}
  
  def getDestination():Seq[AnyVal] = destination 

  
  def init():Unit = {
    params.get("conv") match{
      case Some(s) => conv = SysUtils.apply[(AnyVal => Double)](s) 
      case _ => conv = null
    }
    params.get("cond") match{
      case Some(s) => cond = SysUtils.apply[(Int => Boolean)](s) 
      case _ => cond = null
    }
    init_done = true
  }
    
  def run():Unit = {
    if(source == null || conv == null || cond == null) return

    for{
      (d, i) <- source.zipWithIndex
      if cond(i)
    } destination.enqueue(conv(d))
    
    while(destination.size > 600) destination.dequeue();
  }
    
}