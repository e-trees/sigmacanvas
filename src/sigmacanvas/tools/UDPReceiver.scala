package sigmacanvas.tools

import java.net.DatagramPacket
import java.net.DatagramSocket
import sigmacanvas.base.SigmaCanvasItem

class UDPReceiver() extends SigmaCanvasItem{
  
  params.put("bufsize", 2048.toString)
  params.put("port", 0x4000.toString)

  private var sock:DatagramSocket = _
  private var destination:Array[Byte] = _
  private var packet:DatagramPacket = _
  
  private var init_done = false
  
  def init():Unit = {
    val port = params.get("port") match {
     case Some(v) => v.toInt
     case _ => 0x4000
    }
    val bufsize = params.get("bufsize") match {
     case Some(v) => v.toInt
     case _ => 1500
    }
    sock = new DatagramSocket(port)
    destination = new Array[Byte](bufsize)
    packet = new DatagramPacket(destination, bufsize)
    init_done = true
  }
  
  def run() : Unit = {
    if(sock != null){
      sock.receive(packet)
    }
  }
  
  def close() : Unit = {
    if(sock != null) sock.close()
  }
  
  def setSource(s:Seq[AnyVal]):Unit = {}
  
  def getDestination():Seq[AnyVal] = destination 
  
}