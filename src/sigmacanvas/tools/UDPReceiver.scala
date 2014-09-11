package sigmacanvas.tools

import java.net.DatagramPacket
import java.net.DatagramSocket

import sigmacanvas.base.SigmaCanvasItem
import sigmacanvas.base.SigmaCanvasMessage

class UDPReceiver() extends SigmaCanvasItem{
  
  params.put("bufsize", 2048.toString)
  params.put("port", 0x4000.toString)

  private var sock:DatagramSocket = _
  private var result:Array[Byte] = _
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
    result = new Array[Byte](bufsize)
    packet = new DatagramPacket(result, bufsize)
    init_done = true
  }
  
  def wakeup() : Unit = {
    while(true) {
      if(sock != null){
        sock.receive(packet)
      }
      forward_to_all()
    }
  }
  
  def run(m:SigmaCanvasMessage) : Unit = {}
  
  def data:Seq[AnyVal] = result
  
  def close() : Unit = {
    if(sock != null) sock.close()
  }
  
}