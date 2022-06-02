package client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;

import common.Settings;
import common.Transport;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;

public class Program extends Shell {
	private Text text;
	private List list;
	private Client client;
	int nr;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			Program shell = new Program(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * 
	 * @param display
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public Program(Display display) throws UnknownHostException, IOException {
		super(display, SWT.SHELL_TRIM);

		// autogenerat prin EVENTS din interfata grafica pe comp 'shell':
		// [EVENTS shell - closed]
		// daca se inchide fereastra, clientul va fi automat deconectat de la server
		addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				try {
					client.close();

				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});
		setLayout(new GridLayout(1, false));

		list = new List(this, SWT.BORDER);
		GridData gd_list = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_list.widthHint = 123;

		list.setLayoutData(gd_list);

		text = new Text(this, SWT.BORDER);

		// autogenerat prin EVENTS din interfata grafica pe comp 'text':
		// [EVENTS key - released]
		// apasarea tastei 'enter' va trimite automat catre server mesajul
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					if (e.keyCode == SWT.CR && text.getText().trim().length() > 0) {
						client.send(text.getText().trim());

						text.setText("");
					}
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});

		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		createContents();

		// conectez clientul la server din lista de servere
		/*
		 * for (Map.Entry<String, Integer> entry : Settings.list.entrySet()) {
		 * 
		 * System.out.println(entry.getKey() + "/" + entry.getValue());
		 * 
		 * client = new Client(entry.getKey(), entry.getValue(), message -> {
		 * 
		 * Display.getDefault().asyncExec(() -> { list.add(message); list.redraw();
		 * //refresh, executie asincron
		 * 
		 * }); });
		 * 
		 * break; }
		 */

		initialize();

	}

	public Client initialize() {
		/*
		 * Iterator<Map.Entry<String, Integer>> it =
		 * Settings.list.entrySet().iterator(); Boolean isAClient = true;
		 * 
		 * while (it.hasNext() && isAClient == true) { Map.Entry<String, Integer> pair =
		 * it.next();
		 * 
		 * System.out.println(pair.getKey() + "/" + pair.getValue());
		 * 
		 * try { client = new Client(pair.getKey(), pair.getValue(), message -> {
		 * 
		 * Display.getDefault().asyncExec(() -> { list.add(message); list.redraw();
		 * //refresh, executie asincron
		 * 
		 * }); });
		 * 
		 * if(client == null) isAClient = false;
		 * 
		 * return client;
		 * 
		 * } catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } } return null;
		 */

		for (Integer i : Settings.portList) {
			try (Socket socket = new Socket(Settings.HOST1, i)) {
				// if(!socket.isClosed()) {
				nr = 0;

				// Transport.send(i, socket);
				client = new Client(Settings.HOST1, i, message -> {

					Display.getDefault().asyncExec(() -> {
						if(nr == 0) {
							list.add(i.toString());
							list.redraw();
							nr++;
						}
						list.add(message);
						list.redraw(); // refresh, executie asincron

					});
				});
				// }
				System.out.println(i);

			} catch (IOException e) {
				break;
			}

		}
		return client;

	}

	// prin EVENTS din interfata grafica:
	// 'enter' va trimite automat catre server mesajul
	// daca se inchide fereastra, clientul va fi automat deconectat de la server

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("SWT Application");
		setSize(498, 375);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
