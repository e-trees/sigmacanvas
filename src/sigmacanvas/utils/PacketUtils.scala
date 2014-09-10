package sigmacanvas.utils

object PacketUtils {
  
  def ntohs(d:Seq[Byte], i:Int):Short = {
    (((d(i+0).toShort & 0xFF) << 8) + ((d(i+1).toShort & 0xFF) << 0)).toShort 
  }

  def ntoh(d:Seq[Byte], i:Int):Int = {
    (((d(i+0).toInt & 0xFF) << 24) + 
     ((d(i+1).toInt & 0xFF) << 16) + 
     ((d(i+2).toInt & 0xFF) <<  8) + 
     ((d(i+3).toInt & 0xFF) <<  0))  
  }

}