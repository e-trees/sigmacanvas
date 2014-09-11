package sigmacanvas.ui

import java.awt.BorderLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JToolBar
import sigmacanvas.base.Base

class SigmaCanvasUI(base:Base) {
  
}

class SigmaCanvasToolbar(base:Base) extends JToolBar{

  def makeButton(arg:String, act:ActionListener):JButton = {
    val btn = new JButton(arg)
    btn.addActionListener(act)
    return btn
  } 
  
  add(makeButton("Run", new ActionListener() {
    	def actionPerformed(e:ActionEvent) { base.wakeup() }
    }))
  
}

class SigmaCanvasPanel extends JPanel{

  
  

  
}

object SigmaCanvasUI {
  
  def main(args:Array[String]) = {
    //val base = new Base()
    //val ui = new SigmaCanvasUI(base)
    SigmaCanvasMainWindow.main(null)
  }
  
}