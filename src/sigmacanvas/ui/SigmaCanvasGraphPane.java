package sigmacanvas.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import att.grappa.Graph;
import att.grappa.GrappaAdapter;
import att.grappa.GrappaConstants;
import att.grappa.GrappaPanel;
import att.grappa.Parser;

public class SigmaCanvasGraphPane implements GrappaConstants {

	public ViewFrame frame = null;

	public static final String GRAPHVIZ_URL = "http://www.research.att.com/~john/cgi-bin/format-graph";
	
	private final boolean useCGI;
	private final String dotCmd;
	
	public SigmaCanvasGraphPane(boolean useCGI){
		this.useCGI = useCGI;
		dotCmd = "";
	}
	
	public SigmaCanvasGraphPane(String cmd){
		this.dotCmd = cmd;
		this.useCGI = false;
	}
			
	private String replaceAttribute(String str, String prefix){
		String regex = prefix + "=(\\d*\\.\\d*)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		return m.replaceAll(prefix+"=\"$1\"");
	}
	
	private String getResultDot(InputStream source, OutputStream out, InputStream in) throws IOException{
		int DEFAULT_BUFFER_SIZE = 1024 * 4;
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int size;
		while (-1 != (size = source.read(buffer))) {
			out.write(buffer, 0, size);
		}
		out.flush();
		out.close();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuffer buf = new StringBuffer();
		String s = "";
		try{
			while(null != (s = reader.readLine())){
				buf.append(s + "\n");
			}
			s = buf.toString();
			s = replaceAttribute(s, "height");
			s = replaceAttribute(s, "width");
		}catch(Exception e){
			e.printStackTrace();
		}
		return s;
	}
	
	// experimental implementation, this cannot receive result well.
	private String doGraphvizWithCGI(InputStream source){
		String result = "";
		try {
			URLConnection conn = new URL(GRAPHVIZ_URL).openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			result = getResultDot(source, conn.getOutputStream(), conn.getInputStream());
		} catch (Exception ex) {
			System.err.println("failed to connect the network.");
			System.err.println("(" + ex.getMessage() + ")");
		}
		return result;
	}
	
	private String doGraphvizWithLocal(InputStream source){
		String result = "";
		try {
			Process proc = Runtime.getRuntime().exec(dotCmd);
			result = getResultDot(source, proc.getOutputStream(), proc.getInputStream());
			proc.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void view(InputStream input) {
		
		Graph graph = null;
		Parser parser = null;
		
		String dot = "";
		if(useCGI){
			dot = doGraphvizWithCGI(input);
		}else{
			dot = doGraphvizWithLocal(input);
		}
		
		InputStream in = new ByteArrayInputStream(dot.getBytes());
		parser = new Parser(in, System.err);
		
		try{
			parser.parse();
		}catch(Exception e){
			e.printStackTrace();
		}

		graph = parser.getGraph();
		graph.setEditable(true);
		graph.setMenuable(true);
		graph.setErrorWriter(new PrintWriter(System.err, true));
		//graph.printGraph(new PrintWriter(System.out));

		frame = new ViewFrame(graph);
		frame.setVisible(true);
		graph.repaint();
	}

	class ViewFrame extends JFrame implements ActionListener {
		private GrappaPanel gp;
		private Graph graph = null;

		public ViewFrame(Graph graph) {
			super("SigmaCanvas Design Graph");
			this.graph = graph;

			setSize(600, 400);
			setLocation(100, 100);

			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			JScrollPane jsp = new JScrollPane();
			jsp.getViewport().setBackingStoreEnabled(true);

			gp = new GrappaPanel(graph);
			gp.addGrappaListener(new GrappaAdapter());
			gp.setScaleToFit(false);

			getContentPane().add("Center", jsp);

			setVisible(true);
			jsp.setViewportView(gp);
		}

		public void actionPerformed(ActionEvent evt) {

		}

	}
}
