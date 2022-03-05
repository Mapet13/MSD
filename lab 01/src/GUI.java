import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Class containing GUI: board + buttons
 */
public class GUI extends JPanel implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	private Timer timer;
	private Board board;
	private JButton start;
	private JButton simMode;
	private JButton clear;
	private JSlider pred;
	private JFrame frame;
	private int iterNum = 0;
	private final int maxDelay = 500;
	private final int initDelay = 100;
	private boolean running = false;
	private SimulationMode currentSimMode = SimulationMode.Rain;

	private static final String sym_mode_command_name = "sym mode";

	public GUI(JFrame jf) {
		frame = jf;
		timer = new Timer(initDelay, this);
		timer.stop();
	}

	/**
	 * @param container to which GUI and board is added
	 */
	public void initialize(Container container) {
		container.setLayout(new BorderLayout());
		container.setSize(new Dimension(1024, 768));

		JPanel buttonPanel = new JPanel();

		start = new JButton("Start");
		start.setActionCommand("Start");
		start.setToolTipText("Starts clock");
		start.addActionListener(this);

		simMode = new JButton();
		changeModeTo(SimulationMode.GameOfLife);
		simMode.setActionCommand(sym_mode_command_name);
		simMode.addActionListener(this);

		clear = new JButton("Clear");
		clear.setActionCommand("clear");
		clear.setToolTipText("Clears the board");
		clear.addActionListener(this);

		pred = new JSlider();
		pred.setMinimum(0);
		pred.setMaximum(maxDelay);
		pred.setToolTipText("Time speed");
		pred.addChangeListener(this);
		pred.setValue(maxDelay - timer.getDelay());

		buttonPanel.add(start);
		buttonPanel.add(clear);
		buttonPanel.add(pred);
		buttonPanel.add(simMode);

		board = new Board(1024, 768 - buttonPanel.getHeight(), currentSimMode);
		container.add(board, BorderLayout.CENTER);
		container.add(buttonPanel, BorderLayout.SOUTH);
	}

	/**
	 * handles clicking on each button
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(timer)) {
			iterNum++;
			frame.setTitle(String.format("%s (%d iteration)", currentSimMode.toString(), iterNum));
			board.iteration();
		} else {
			String command = e.getActionCommand();
			switch (command) {
				case "Start" -> {
					if (!running) {
						timer.start();
						start.setText("Pause");
						simMode.setEnabled(false);
					} else {
						timer.stop();
						start.setText("Start");
						simMode.setEnabled(true);
					}
					running = !running;
					clear.setEnabled(true);
				}
				case "clear" -> clearAction();
				case sym_mode_command_name -> {
					if (currentSimMode.equals(SimulationMode.GameOfLife)) {
						changeModeTo(SimulationMode.Rain);
					} else {
						changeModeTo(SimulationMode.GameOfLife);
					}
					clearAction();
					frame.setTitle(currentSimMode.toString());
					board.changeMode(currentSimMode);
				}
			}

		}
	}

	private void changeModeTo(SimulationMode nextMode) {
		simMode.setText(String.format("Change to: %s mode", currentSimMode.toString()));
		currentSimMode = nextMode;
	}

	/**
	 * slider to control simulation speed
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e) {
		timer.setDelay(maxDelay - pred.getValue());
	}

	public void clearAction() {
		iterNum = 0;
		timer.stop();
		start.setEnabled(true);
		board.clear();
	}
}
