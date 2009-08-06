package graphics;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.*;

public class DrawingOvals extends JFrame implements Runnable {
	/**
	 * This class draws Ovals (Circles) on the main frame and checks whether any
	 * of these Ovals are being clicked on then it performs action according to
	 * the place of the circle
	 * */

	// define chat components
	JTextField sendSpace = new JTextField(55);
	JTextArea recieveSpace = new JTextArea(4, 65);
	JScrollPane sp_recieveSpace = new JScrollPane(recieveSpace);
	JButton send = new JButton("send");

	Socket socket;
	BufferedReader in = null;
	BufferedWriter out = null;

	public void run() {

		while (true) {
			try {
				InetAddress ia = socket.getInetAddress();
				recieveSpace.append(ia.getHostName() + " : " + in.readLine()
						+ "\n");
			} catch (IOException e) {

			}
		}
	}

	public void mainFrame() {

		// Creating the Frame and setting it's properties [visibility, size,
		// resizability and closing]
		DrawingOvals frame = new DrawingOvals();
		frame.setVisible(true);
		frame.setSize(800, 600);
		frame.setTitle("Connect 4");
		frame.setResizable(true);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);

		// Adding the JPanel to the JFrame and edit their properties
		Panel panel = new Panel();
		panel.setOpaque(false);

		frame.add(panel, BorderLayout.NORTH);
		frame.pack();

		// adding chat panel to the frame and add chat components to it
		ChatPanel cp = new ChatPanel();
		cp.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 12));
		cp.add(sp_recieveSpace);
		recieveSpace.setEditable(false);
		cp.add(sendSpace);
		cp.add(send);

		frame.add(cp, BorderLayout.SOUTH);

	}

	// getting the mouse position when clicked
	int x, y;

	public DrawingOvals() {

		// chat client code
		try {

			socket = new Socket("localhost", 8000);
			in = new BufferedReader(new InputStreamReader(socket
					.getInputStream(), "UTF8"));
			out = new BufferedWriter(new OutputStreamWriter(socket
					.getOutputStream(), "UTF8"));

			Thread t = new Thread(this);
			t.start();

		} catch (IOException ioe) {

		}

		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				recieveSpace.append(" you : " + sendSpace.getText() + "\n");
				try {
					out.write(sendSpace.getText());
					out.newLine();
					out.flush();
					sendSpace.setText("");
				} catch (IOException ie) {
				}
			}
		});

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent mouseClick) {
				x = mouseClick.getX();
				y = mouseClick.getY();
				repaint();
			}
		});
	}

	// Creating an ArrayList that will hold the Ovals dimensions
	ArrayList<Integer> positions = new ArrayList<Integer>();

	// Painting the Circles and filling them
	public void paint(Graphics g) {
		super.paint(g);

		// Drawing the Circles
		for (int i = 1; i <= 7; i++) {
			for (int j = 1; j <= 6; j++) {
				g.setColor(Color.white);
				int ovalX = 130 + (i * 60);
				int ovalY = (j * 60);
				g.drawOval(ovalX, ovalY, 50, 50);

				if (x >= ovalX && x <= 50 + ovalX) {
					if (y >= ovalY && y <= 50 + ovalY) {
						positions.add(i); // we have to change this "i" and put
						// the circles dimensions
						g.fillOval(ovalX, ovalY, 50, 50);
						// System.out.println(positions.size());
					}
				}
			}
		}
	}

	public Dimension getPreferredSize() {
		return new Dimension(800, 600);
	}
}