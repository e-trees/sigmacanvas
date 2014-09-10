package sigmacanvas.ui

import javax.swing._
import java.awt.event.ActionListener
import java.awt.event.ActionEvent

class SigmaCanvasMenuBar extends JMenuBar{
  

  def menuItem(s:String, a:ActionListener):JMenuItem = {
    val item = new JMenuItem(s)
    item.addActionListener(a)
    return item
  }
  
  val fileMenu = new JMenu("File")
  
  fileMenu.add(menuItem("Open", new ActionListener() {
    	def actionPerformed(e:ActionEvent) { println("open") }
  }))
  
  fileMenu.add(menuItem("Save", new ActionListener() {
    	def actionPerformed(e:ActionEvent) { println("save") }
  }))
  
  fileMenu.addSeparator()

  fileMenu.add(menuItem("Quit", new ActionListener() {
    	def actionPerformed(e:ActionEvent) { System.exit(0) }
  }))
  
  add(fileMenu)
  
  val editMenu = new JMenu("Edit")
  
  editMenu.add(menuItem("Undo", new ActionListener() {
    	def actionPerformed(e:ActionEvent) { println("undo") }
  }))
  
  editMenu.add(menuItem("Redo", new ActionListener() {
    	def actionPerformed(e:ActionEvent) { println("redo") }
  }))
  
  editMenu.addSeparator()

  val procMenu = new JMenu("Process")
  
  procMenu.add(menuItem("Run", new ActionListener() {
    	def actionPerformed(e:ActionEvent) { println("run") }
  }))
  
  procMenu.add(menuItem("Stop", new ActionListener() {
    	def actionPerformed(e:ActionEvent) { println("stop") }
  }))
  
  add(procMenu)
  
  editMenu.add(menuItem("View as XML", new ActionListener() {
    	def actionPerformed(e:ActionEvent) { println("xml") }
  }))
  
  add(editMenu)
  
  
  val helpMenu = new JMenu("Help")
  
  helpMenu.add(menuItem("About", new ActionListener() {
    	def actionPerformed(e:ActionEvent) { println("about") }
  }))
    
  helpMenu.add(menuItem("Check update...", new ActionListener() {
    	def actionPerformed(e:ActionEvent) { println("check update") }
  }))

  add(helpMenu)
  
}