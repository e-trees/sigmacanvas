package sigmacanvas.base

import scala.collection.mutable.LinkedList
import scala.collection.mutable.HashMap
import scala.xml.XML
import java.io.File
import sigmacanvas.utils.SysUtils

class Base {
  
  var modules = new HashMap[String,SigmaCanvasItem]()

  def init():Unit = {
    for((k,m) <- modules) m.init()
  }
  
  def run():Unit = {
    while(true){
      for((k,m) <- modules) m.run()
    }
  }

  def load(file:String) = {
	val xml = XML.loadFile(new File(file))
    
    for(item <- xml \ "item"){
    	val key = (item \ "@id")(0).text
    	val klass = (item \ "@class")(0).text
    	val cmd = "new " + klass + "()"
    	val m = SysUtils.apply[SigmaCanvasItem](cmd)
    	for(param <- item \ "parameter"){
    	  val k = (param \ "@key")(0).text
    	  val v = (param \ "@value")(0).text
    	  m.setParam(k, v)
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
      if(src != null && dest != null) dest.setSource(src.getDestination)
    }

  }
  
}

object Base {
  
  def main(args:Array[String]) = {
    val b = new Base() 
    b.load(args(0))
    b.run()
  }
  
}