package sigmacanvas.ui;

import javax.swing.AbstractListModel;


public class SigmaCanvasItemListModel extends AbstractListModel{
	
	String[] strings = {
			"sigmacanvas.tools.ConvByteToShort",
			"sigmacanvas.tools.ConvAnyValueToDouble",
			"sigmacanvas.tools.Simple2DViewer",
			"sigmacanvas.tools.UDPReceiver"
	};

	public int getSize() {
		return strings.length;
	}
	
	
	public Object getElementAt(int i) {
		return strings[i];
	}
}