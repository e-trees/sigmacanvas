package sigmacanvas.tools

import scala.collection.mutable.Queue
import scala.collection.mutable.SynchronizedQueue
import sigmacanvas.base.SigmaCanvasItem
import sigmacanvas.utils.PacketUtils
import sigmacanvas.utils.SysUtils
import sigmacanvas.ui.SigmaCanvasMainWindow
import sigmacanvas.base.SigmaCanvasMessage
import sigmacanvas.base.SigmaCanvasMessage
import sigmacanvas.base.SigmaCanvasMessage

class ConvByteToShort extends SigmaCanvasItem{
  
  private var result:Array[Short] = _
  private var offset:Int = 0
  private var size:Int = 512
  
  setParam("size", 512.toString)
  setParam("offset", 0.toString)
    
  def init():Unit = {
    size = getParam("size") match{
      case Some(v) => v.toInt
      case _ => 512
    }
    result = new Array[Short](size)
    
    offset = getParam("offset") match{
      case Some(v) => v.toInt
      case _ => 0
    }
  }
  
  def wakeup():Unit = {}
  
  def run(m:SigmaCanvasMessage):Unit = {
    val s = m.obj.data
    for(i <- 0 until size; j = 2 * i + offset){
    	result(i) = PacketUtils.ntohs(m.obj.data.asInstanceOf[Seq[Byte]], j)
    }
    forward_to_all()
  }
  
  def data:Seq[AnyVal] = result

}

class ConvByteToInt extends SigmaCanvasItem{
  
  private var source:Seq[Byte] = _
  private var result:Array[Int] = _
  private var offset:Int = 0
  private var size:Int = 256
  
  setParam("size", size.toString)
  setParam("offset", offset.toString)
  
  def init():Unit = {
    size = getParam("size") match{
      case Some(v) => v.toInt
      case _ => size
    }
    result = new Array[Int](size)
    
    offset = getParam("offset") match{
      case Some(v) => v.toInt
      case _ => offset
    }
  }
  
  def run(m:SigmaCanvasMessage):Unit = {
    val a = m.obj.data
    for(i <- 0 until size; j = 4 * i + offset){
      result(i) = PacketUtils.ntoh(source, j)
    }
    forward_to_all()
  }
  
  def wakeup():Unit = {}
  
  def data():Seq[AnyVal] = result

}

class ConvAnyValueToDouble extends SigmaCanvasItem{
  
  private val result:Queue[Double] = new SynchronizedQueue[Double]()
  
  private var conv:AnyVal=>Double = _
  private var cond:Int=>Boolean = _
  private var bufsize:Int = 600
  
  setParam("bufsize", bufsize.toString)

  def init():Unit = {
    getParam("conv") match{
      case Some(s) => conv = SysUtils.apply[(AnyVal => Double)](s) 
      case _ => conv = null
    }
    getParam("cond") match{
      case Some(s) => cond = SysUtils.apply[(Int => Boolean)](s) 
      case _ => cond = null
    }
    bufsize = getParam("bufsize") match{
      case Some(s) => s.toInt
      case _ => bufsize
    }
  }
    
  def run(m:SigmaCanvasMessage):Unit = {
    val a = m.obj.data
    if(m.obj.data == null || conv == null || cond == null) return
    for{
    	(d, i) <- a.zipWithIndex
    	if cond(i)
    } result.enqueue(conv(d))
    while(result.size > bufsize) result.dequeue();
    forward_to_all()
  }

  def wakeup():Unit = {}
  
  def data:Seq[AnyVal] = result

}
