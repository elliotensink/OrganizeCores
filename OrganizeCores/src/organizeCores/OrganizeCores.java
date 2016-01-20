package organizeCores;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class OrganizeCores 
{
	String coreLocation;
	File coreRefMap;
	String[][] coresInfo;
	public OrganizeCores()
	{
		getFile();
		int lineNum = readFileLines(coreLocation);
		coresInfo = new String[lineNum][2];
	}

	public void getFile()
	{
		JFileChooser fileOpen = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter(".txt files", "txt");
		fileOpen.addChoosableFileFilter(filter);

		int ret = fileOpen.showDialog(null, "Open file");

		if (ret == JFileChooser.APPROVE_OPTION)
		{
			coreRefMap = fileOpen.getSelectedFile();
			coreLocation = coreRefMap.toString();
		}
		else if(ret == JFileChooser.CANCEL_OPTION)
		{
			coreRefMap = null;
			coreLocation = "";
		}
	}
	
	public int readFileLines(String filename)
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

	public void readFile()
	{
		System.out.println("Beginning Reading Motifs");
		String sCurrentLine;
		BufferedReader br = null;
		try 
		{
			br = new BufferedReader(new FileReader(coreRefMap.toString()));
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}
		int index = 1;
		int failCount = 0;
		try 
		{
			if(br != null)
			{
				int lineIndex = 0;
				while ((sCurrentLine = br.readLine()) != null) 
				{
					if(lineIndex == 0)
						coreLocation = sCurrentLine.split("\t")[1];
					else
					{
						
					}
				}
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{

	}
}
