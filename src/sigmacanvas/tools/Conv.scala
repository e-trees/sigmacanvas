package sigmacanvas.tools

import scala.collection.mutable.Queue
import scala.collection.mutable.SynchronizedQueue

import sigmacanvas.base.SigmaCanvasItem
import sigmacanvas.utils.PacketUtils
import sigmacanvas.utils.SysUtils

class ConvByteToShort extends SigmaCanvasItem{
  
  private var source:Seq[Byte] = _
  private var destination:Array[Short] = _
  private var offset:Int = 0
  private var size:Int = 512
  
  params.put("size", 512.toString)
  params.put("offset", 0.toString)
    
  def init():Unit = {
    size = params.get("size") match{
      case Some(v) => v.toInt
      case _ => 512
    }
    destination = new Array[Short](size)
    
    offset = params.get("offset") match{
      case Some(v) => v.toInt
      case _ => 0
    }
  }
  
  def run():Unit = {
    if(source == null) return
    for(i <- 0 until size; j = 2 * i + offset){
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
