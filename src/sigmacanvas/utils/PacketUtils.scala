package sigmacanvas.utils

object PacketUtils {
  
  def ntohs(d:Seq[Byte], i:Int):Short = {
    (((d(i+0).toShort & 0xFF) << 8) + ((d(i+1).toShort & 0xFF) << 0)).toShort 
  }

}