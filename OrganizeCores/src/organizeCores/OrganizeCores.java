package organizeCores;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**************************************************************************************************
 * OrganizeCores is used to rearrange .im3 files into appropriate folders according to tissue cores
 * the images come from.
 * 
 * Images will be sorted by the reference information stored in a tab-delimited file
 * 
 * The first line of the tab-delimited file must contain the Path to the images
 * 
 * The following lines should contain the core number (which will become the folder name) of the 
 * image and the image name to be moved according to the following setup:
 * 
 * Column1: Core Name/Number
 * Column2: Image Name
 *
 * @author Elliot
 * @version 1/21/2016
 *************************************************************************************************/
public class OrganizeCores 
{
	private String coreLocation; //Store path to files to be sorted
	private File coreRefMap; //Tab-delimited 
	private String[][] coresInfo; //Store the core and image name pairs
	public JFrame frame;
	public JPanel pan;
	public JLabel selectFile,message;
	public JButton browse,organize;
	private ButtonListener bl;
	public JTextField textField;
	
	
	public OrganizeCores()
	{
		coreLocation = "";
		coreRefMap = null;
		coresInfo = null;
	}
	/**********************************************************************************************
	 * Run the process of Organizing files
	 *********************************************************************************************/
	private void run()
	{
		createUserInterface();
	}
	/**********************************************************************************************
	 * 
	 *********************************************************************************************/
	private void createUserInterface()
	{
		//Frame holds the panel
		frame = new JFrame("Organize Cores");
		frame.setSize(new Dimension(400, 400));// Sets Default Size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		//Panel holds all the GUI elements
		pan = new JPanel();
		pan.setLayout(new BoxLayout(pan,BoxLayout.Y_AXIS));
		pan.setPreferredSize(new Dimension(400,150));
		
		//Labels to provide information
		selectFile = new JLabel("Core Reference File: ");
		selectFile.setAlignmentX(Box.LEFT_ALIGNMENT);
		
		message = new JLabel("Select the Browse button");
		message.setAlignmentX(Box.LEFT_ALIGNMENT);
		
		//Setup text field which will store the file path to the reference file
		textField = new JTextField();
		textField.setMinimumSize(new Dimension(200,30));
		textField.setMaximumSize(new Dimension(300,30));
		textField.setAlignmentX(Box.LEFT_ALIGNMENT);
		
		//Listens for user to push buttons see private class below
		bl = new ButtonListener();
		
		//Setup browse button for selecting file
		browse = new JButton("Browse");
		browse.setAlignmentX(Box.LEFT_ALIGNMENT);
		browse.addActionListener(bl);
		//Setup organize button for executing the organization process
		organize = new JButton("Organize Files");
		organize.setAlignmentX(Box.LEFT_ALIGNMENT);
		organize.addActionListener(bl);
		
		//Add all elements to the panel
		pan.add(selectFile);
		pan.add(Box.createRigidArea(new Dimension(5, 5)));//Creates space
		pan.add(textField);
		pan.add(Box.createRigidArea(new Dimension(5, 5)));//Creates space
		pan.add(browse);
		pan.add(Box.createRigidArea(new Dimension(5, 5)));//Creates space
		pan.add(organize);
		pan.add(Box.createRigidArea(new Dimension(5, 5)));//Creates space
		pan.add(message);
		
		//Add the panel to the frame
		frame.add(pan,BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
	
	
	/**********************************************************************************************
	 * Prompt the user to select a file
	 * @return File selected by user
	 **********************************************************************************************/
	private File getFile()
	{
		File f = null;
		JFileChooser fileOpen = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter(".txt files", "txt");
		fileOpen.addChoosableFileFilter(filter);

		int ret = fileOpen.showDialog(null, "Open file");
		if (ret == JFileChooser.APPROVE_OPTION)
		{
			f = fileOpen.getSelectedFile();
		}
		return f;
	}
	/**********************************************************************************************
	 * 
	 * @param filename (Name of the file from coreRefMap obtained from getFile())
	 * @return the integer number of lines within filename
	 **********************************************************************************************/
	private int readFileLines(String filename)
	{
		int lineNum = 0;
		try
		{
			File file =new File(filename);
			if(file.exists())
			{
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);

				while (br.readLine() != null)
				{
					lineNum++;
				}
				br.close();
			}
			else
			{
				System.out.println("Cannot find file: " + filename);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return lineNum;
	}
	/**********************************************************************************************
	 * Read in the data stored within coreRefMap
	 **********************************************************************************************/
	private void readFile()
	{
		String sCurrentLine;
		BufferedReader br = null;
		try 
		{
			br = new BufferedReader(new FileReader(coreLocation));
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}
		try 
		{
			if(br != null)
			{
				int lineIndex = -1;
				while ((sCurrentLine = br.readLine()) != null)
				{
					if(lineIndex < 0)
					{
						coreLocation = sCurrentLine.split("\t")[0];
					}
					else
					{
						coresInfo[lineIndex] = sCurrentLine.split("\t");
						
					}
					lineIndex++;
				}
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**********************************************************************************************
	 * Create core folders and move images into the appropriate new folders according to the
	 * reference data from coreRefMap that is stored in coresInfo
	 **********************************************************************************************/
	private void organize()
	{
		File coreFolder;
		File coreImage;
		for(String[] info : coresInfo)
		{
			coreFolder = new File(coreLocation + "\\" + info[0]);
			coreImage = new File(coreLocation + "\\" + info[1]+info[2]);
			if(!coreFolder.exists())
			{
				coreFolder.mkdir();
			}
			if(!coreImage.renameTo(new File(coreFolder.getAbsolutePath() + "\\" + coreImage.getName())))
			{
				System.out.println("Move Failed!");
			}

		}
	}
	
	private class ButtonListener implements ActionListener 
	{ 
		public void actionPerformed(ActionEvent event) 
		{
			if(event.getSource() == browse)
			{
				coreRefMap = getFile();
				textField.setText(coreRefMap.getAbsolutePath());
			}
			else if(event.getSource() == organize)
			{
				if(coreRefMap != null)
				{
					message.setText("Begin Organization");
					coreLocation = coreRefMap.toString();
					int lineNum = readFileLines(coreLocation);
					coresInfo = new String[lineNum-1][3];
					readFile();
					organize();
					message.setText("Finished Organization");
				}
				else
				{
					message.setText("No file selected");
				}
			}
		}
	}

	public static void main(String[] args)
	{
		OrganizeCores oc = new OrganizeCores();
		oc.run();
	}
}
