package organizeCores;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

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

//\\files\Research\Haab.Lab\Personal folders\_Elliot\JAVA\OrganizeCores\CoreMap.txt
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
	private String coreRefMapLoc; //Store path to core reference map
	private File coreRefMap; //Tab-delimited 
	private File outputFile;
	private FileWriter fw;
	private BufferedWriter bw;
	private Date today;
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
		today = new Date();
		boolean logCreated = false;//Was the log file created?
		try
		{
			outputFile = new File(System.getProperty("user.dir")+"\\organizationLOG.txt");
			if (!outputFile.exists()) 
			{
				outputFile.createNewFile();
			}
			System.out.println(outputFile.getAbsolutePath());
			fw = new FileWriter(outputFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write("Created Log File: " + today.toString() + "\n");
			bw.newLine();
			logCreated = true;
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		createUserInterface(logCreated);
	}
	/**********************************************************************************************
	 * 
	 *********************************************************************************************/
	private void createUserInterface(boolean logCreated)
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

		if(logCreated)
			message = new JLabel("Select the Browse button");
		else
			message = new JLabel("Unable to creat log file");
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
		fileOpen.setCurrentDirectory(new File(System.getProperty("user.dir")));
		int ret = fileOpen.showDialog(null, "Open file");
		if (ret == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				today = new Date();
				f = fileOpen.getSelectedFile();
				bw.write("Selected File: " + f.getAbsolutePath() + " " + today.toString());
				bw.newLine();
			}
			catch (IOException e) 
			{
				try 
				{
					today = new Date();
					bw.write("Could not find file " + today.toString());
					bw.newLine();
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
			}
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
				try 
				{
					today = new Date();
					bw.write("Calculated file line numbers " + today.toString());
					bw.newLine();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
				br.close();
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
			br = new BufferedReader(new FileReader(coreRefMapLoc));
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}
		try 
		{
			if(br != null)
			{
				int lineIndex = 0;
				while ((sCurrentLine = br.readLine()) != null)
				{
					if(lineIndex < 1)
					{
						coreLocation = sCurrentLine.split("\t")[0];
					}
					else
					{
						coresInfo[lineIndex-1] = sCurrentLine.split("\t");

					}
					try 
					{
						today = new Date();
						bw.write("Read Line " + lineIndex + ": " + sCurrentLine + " " + today.toString());
						bw.newLine();
					} 
					catch (IOException e) 
					{
						System.out.println("No log file 1");
					}
					lineIndex++;
				}
			}
		} 
		catch (IOException e) 
		{				
			try 
			{
				today = new Date();
				bw.write("Failed to read file " + today.toString());
				bw.newLine();
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
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
		coreFolder = new File(coreLocation);
		File[] allFiles = coreFolder.listFiles();
		String[] allFileNames = new String[allFiles.length];
		for(int i = 0; i < allFiles.length; i++)
		{
			File f = allFiles[i];
			allFileNames[i] = f.getName();
			//System.out.println(f.toString());
		}
		for(String[] info : coresInfo)
		{
			String core = cleanString(info[0]);
			String scanName = cleanString(info[1]);
			String imgName = "";

			coreFolder = new File(coreLocation + "\\" + core);
			for(int i = 0; i < allFiles.length; i++)
			{
				if(allFileNames[i].contains(scanName))
				{
					imgName = allFileNames[i];
					break;
				}
			}
			coreImage = new File(coreLocation + "\\" + imgName);
			if(!coreFolder.exists())
			{
				coreFolder.mkdir();
				try 
				{
					today = new Date();
					bw.write("Created core folder: " + coreFolder.getName() + " "+ today.toString());
					bw.newLine();
				} 
				catch (IOException e) 
				{
					System.out.println("No log file 2");
				}
			}
			try 
			{
				today = new Date();
				bw.write("Original file location: " + coreImage.getAbsolutePath() + " "+ today.toString());
				bw.newLine();
			} 
			catch (IOException e) 
			{
				System.out.println("No log file 3");
			}
			if(coreImage.renameTo(new File(coreFolder.getAbsolutePath() + "\\" + coreImage.getName())))
			{
				try 
				{
					today = new Date();
					bw.write("Moved file to: " + coreImage.getAbsolutePath() + " "+ today.toString());
					bw.newLine();
				} 
				catch (IOException e) 
				{
					System.out.println("No log file 4");
				}
			}
			else
			{
				try 
				{
					today = new Date();
					bw.write("Move Failed: " + coreImage.getName() + " "+ today.toString());
					bw.newLine();
				} 
				catch (IOException e) 
				{
					System.out.println("No log file 5");
				}
			}

		}
	}

	private String cleanString(String str)
	{
		if(str.substring(0,1).equals("\""))
			str = str.substring(1);
		if(str.substring(str.length()-1).equals("\""))
			str = str.substring(0,str.length()-1);

		return str;
	}

	private class ButtonListener implements ActionListener 
	{ 
		public void actionPerformed(ActionEvent event) 
		{
			if(event.getSource() == browse)
			{
				coreRefMap = getFile();
				textField.setText(coreRefMap.getAbsolutePath());
				try 
				{
					today = new Date();
					bw.write("Browising for file: "+ today.toString());
					bw.newLine();
				} 
				catch (IOException e) 
				{
					System.out.println("No log file 6");
				}
			}
			else if(event.getSource() == organize)
			{
				if(coreRefMap != null)
				{
					try
					{
						today = new Date();
						bw.write("Beginning organization: "+ today.toString());
						bw.newLine();
					} 
					catch (IOException e) 
					{
						System.out.println("No log file 7");
					}
					message.setText("Begin Organization");
					coreRefMapLoc = coreRefMap.toString();
					int lineNum = readFileLines(coreRefMapLoc);
					coresInfo = new String[lineNum-1][2];
					readFile();
					organize();
					message.setText("Finished Organization");
					try 
					{
						bw.write("Finished organization: "+ today.toString());
						bw.close();
					} 
					catch (IOException e) 
					{
						System.out.println("No log file 8");
					}
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
