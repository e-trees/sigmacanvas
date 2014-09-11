package sigmacanvas.tools

import java.util.Date

import scala.util.Try

import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import sigmacanvas.base.ITEM_INIT
import sigmacanvas.base.SigmaCanvasItem
import sigmacanvas.base.SigmaCanvasMessage

class SendMail extends SigmaCanvasItem{
  
  private var user:String = _
  private var pass:String = _
  private var from:String = _
  private var to:String = _
  private var subject:String = _
  private var body:String = _
  
  private var param_valid = true
  
  def get_parameter(k:String):String = {
    getParam(k) match {
      case Some(s) => return s
      case _ =>
        param_valid = false
        return ""
    }
  }
  
  def init():Unit = {
    user = get_parameter("user")
    pass = get_parameter("pass")
    from = get_parameter("from")
    to = get_parameter("to")
    subject = get_parameter("subject")
    body = get_parameter("body")
  }
  
  def wakeup():Unit = {}
  def data():Seq[AnyVal] = null
  
  def run(m:SigmaCanvasMessage):Unit = {
    if(param_valid  == false) return
    Try{
      val props = System.getProperties
      props.put("mail.smtp.auth", "true")
      props.put("mail.smtp.starttls.enable", "true")
      props.put("mail.smtp.host", "smtp.gmail.com")
      props.put("mail.smtp.port", "465")
      props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
      props.put("mail.smtp.socketFactory.fallback", "false")
      props.put("mail.smtp.debug", "true")
      props.put("mail.smtp.user", user)
      props.put("mail.smtp.password", pass)
      val session = Session.getInstance(props, new Authenticator() {
        override def getPasswordAuthentication() = {
          new PasswordAuthentication(user, pass)
          }})
      val msg = new MimeMessage(session)
      msg.setFrom(new InternetAddress(from))
      msg.setRecipients(Message.RecipientType.TO, to)
      msg.setSubject(subject)
      msg.setText(body)
      msg.setSentDate(new Date())
      Transport.send(msg)
    }
  }

}

object SendMail{
  
  def main(args:Array[String]) = {
    val system = ActorSystem("system")
    val m = system.actorOf(Props[SendMail])
    m ! ("user", "****")
    m ! ("pass", "****")
    m ! ("from", "****")
    m ! ("to", "****")
    m ! ("subject", "Test from SigmaCanvas")
    m ! ("body", "Hello World")
    m ! ITEM_INIT
    m ! new SigmaCanvasMessage(null, null)
  }

}
