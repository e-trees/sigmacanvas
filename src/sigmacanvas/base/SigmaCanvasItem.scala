package sigmacanvas.base

import scala.collection.mutable.HashMap

abstract class SigmaCanvasItem {
  
  protected val params = new HashMap[String, String]() 
  
  def init():Unit

  def run():Unit
  
  def setParam(k:String, v:String):Unit = params.put(k, v)
  
  def setSource(s:Seq[AnyVal]):Unit
  
  def getDestination():Seq[AnyVal]
  
}