package sigmacanvas.ui

import java.io.PipedOutputStream
import java.io.PipedInputStream
import java.io.PrintWriter
import scala.xml.XML
import java.io.File
import java.io.InputStream
import java.io.OutputStreamWriter
import java.io.StringWriter
import scala.collection.mutable.HashMap

object XML2Dot {
   
  def load(file:String):String = {

    val xml = XML.loadFile(new File(file))
    val buf = new StringWriter
    val writer = new PrintWriter(buf)
    
    //writer.println("digraph " + file.replaceAll("\\.", "_") + " {")
    writer.println("digraph design{")
    
    for(item <- xml \ "item"){
    	val key = (item \ "@id")(0).text
    	val klass = (item \ "@class")(0).text
    	
    	var s = ""
    	for(param <- item \ "parameter"){
    	  val k = (param \ "@key")(0).text
    	  val v = (param \ "@value")(0).text
    	  s += "|parameter:" + k + "=" + "\"" + v + "\""
    	}
    	//writer.printf("%s [shape=record, label=\"{%s:%s%s}\"];\n", key, key, klass, s)
    	writer.printf("%s [shape=record, label=\"{%s:%s}\"];\n", key, key, klass)
    }
    
    for(item <- xml \ "connect"){
      val src = (item \ "@src")(0).text
      val dest = (item \ "@dest")(0).text
      if(src != null && dest != null){
    	writer.println(src + " -> " + dest + ";")
      } 
    }

    writer.println("}")
    writer.flush()
    writer.close()
    return buf.toString()
  }    

}
