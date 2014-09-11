package sigmacanvas.base

import scala.collection.mutable.HashMap
import akka.actor.Actor
import scala.collection.mutable.LinkedList
import akka.actor.ActorRef

case object ITEM_INIT
case object ITEM_WAKEUP

abstract class SigmaCanvasItem extends Actor{
  
  protected val params = new HashMap[String, String]()
    
  def init():Unit

  def wakeup():Unit

  def run(m:SigmaCanvasMessage):Unit
  
  def data():Seq[AnyVal]
  
  def setParam(k:String, v:String):Unit = params.put(k, v)
  
  private var destinations = new LinkedList[ActorRef]()
  
  private def add_destinations(a:ActorRef) = {
    destinations = a +: destinations
  }
  
  def forward_to_all(a:Any) = {
    for(d <- destinations){
      d ! new SigmaCanvasMessage(this, a)
    }
  }
  
  def id():String = {
    params.get("id") match{
      case Some(s) => s
      case _ => "anonymous"
    }
  }
  
  def receive = {
    case (k:String, v:String) => setParam(k, v)
    case ITEM_INIT => init()
    case ITEM_WAKEUP => wakeup()
    case a:ActorRef => add_destinations(a)
    case m:SigmaCanvasMessage => run(m)
    case x:Any => println("unknown:" + x)
  }
  
}

class SigmaCanvasMessage(val obj:SigmaCanvasItem, val value:Any) {
  
  override def toString():String = {
    obj.id + ":" + value
  }

}