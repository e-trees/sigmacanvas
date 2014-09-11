package sigmacanvas.base

import java.io.File
import scala.collection.mutable.HashMap
import scala.xml.XML
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.actorRef2Scala
import sigmacanvas.utils.SysUtils
import akka.actor.Props

class Base {
  
  private var modules = new HashMap[String,ActorRef]()
  val system = ActorSystem("system")
  //val actor = sys.actorOf(Props[sigmacanvas.tools.UDPReceiver], "r")


  def init():Unit = {
    for((k,m) <- modules) m ! ITEM_INIT
  }
  
  def wakeup():Unit = {
    for((k,m) <- modules) m ! ITEM_WAKEUP
  }
  
  def load(file:String) = {
	val xml = XML.loadFile(new File(file))
    
    for(item <- xml \ "item"){
    	val key = (item \ "@id")(0).text
    	val klass = (item \ "@class")(0).text
    	val cmd = "akka.actor.Props[" + klass + "]"
    	val m = system.actorOf(SysUtils.apply[akka.actor.Props](cmd), key)
    	m ! ("id", key)
    	for(param <- item \ "parameter"){
    	  val k = (param \ "@key")(0).text
    	  val v = (param \ "@value")(0).text
    	  m ! (k, v)
    	}
    	modules.put(key, m)
    }
    
    init();
    
    for(item <- xml \ "connect"){
      val src = modules.get((item \ "@src")(0).text) match{
        case Some(m) => m
        case _ => null
      }
      val dest = modules.get((item \ "@dest")(0).text) match {
        case Some(m) => m
        case _ => null
      }
      if(src != null && dest != null) src ! dest
    }

  }
  
}

object Base {
  
  def main(args:Array[String]) = {
    val b = new Base() 
    b.load(args(0))
    b.wakeup()
  }
  
}