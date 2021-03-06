package com.dbs.sg.DTE12.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileOperator {

	/**
	 * Logger
	 */
//	private static Logger logger ;

	/**
	 * Write bytes into file.
	 * @param filename
	 * @param input
	 * @param append	true:write bytes to end of file;false:write bytes to beginning.
	 * @throws IOException 
	 */
	public static void Write(String filename, byte[] input, boolean append) throws IOException {

		File file = new File(filename);

		if (file.exists() && append == false) {
			file.delete();
		}

		FileOutputStream fw = null;
		fw = new FileOutputStream(filename, true);

		fw.write(input);
		// logger.info("Write File, the name is: " + filename);
		if (fw != null) {
			fw.close();
		}

	}
	
	/**
	 * Read all bytes from file.
	 * @param filename
	 * @return
	 * @throws IOException 
	 */
	public static byte[] Read(String filename) throws IOException {

		// check if is existed
		File file = new File(filename);
		if (!file.isFile()) {
			// logger.error("File is not exist or not a file: " + filename);
			return null;
		}

		BufferedReader reader = null;

		StringBuffer result = new StringBuffer();
		String temp;
		reader = new BufferedReader(new FileReader(file));
		while ((temp = reader.readLine()) != null) {
			result.append(temp);
		}
		// logger.error("File read wrong, the name is: " + filename);
		reader.close();

		return (result.toString()).getBytes();
	}
	
	/**
	 * Read line by line from file.
	 * @param filename
	 * @return
	 * @throws IOException 
	 */
	public static ArrayList ReadLines(String filename) throws IOException {

		// check if is existed
		File file = new File(filename);
		if (!file.isFile()) {
			// logger.error("File is not exist or not a file: " + filename);
			return null;
		}

		BufferedReader reader = null;

		ArrayList output = new ArrayList();

		String temp;
		reader = new BufferedReader(new FileReader(file));
		while ((temp = reader.readLine()) != null) {
			output.add(temp);
		}
		reader.close();

		return output;
	}

	/**
	 * Returns TRUE if Copy file successful.
	 */
	public Boolean copyFile(File sourceFile, File targetFile)
	{
		BufferedInputStream bufferedInputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		int size;

		try
		{
			if (!sourceFile.isFile())
			{
//				logger.warn("File " + sourceFile + " is not a file.");
				return Boolean.FALSE;
			}

			if (targetFile == null)
				return Boolean.FALSE;

			byte[] buffer = new byte[1024];
			bufferedInputStream = new BufferedInputStream(new FileInputStream(sourceFile.toString()));
			bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(targetFile.toString()));

			while ((size = bufferedInputStream.read(buffer)) > -1)
			{
				bufferedOutputStream.write(buffer, 0, size);
				Thread.yield();
			}

			bufferedInputStream.close();
			bufferedOutputStream.close();

//			logger.info("File " + sourceFile + " is copied " + " to " + targetFile + " successfully.");
			return Boolean.TRUE;
		}
		catch (FileNotFoundException fileNotFoundException)
		{
//			logger.warn("File " + sourceFile + " is not found", fileNotFoundException);
			return Boolean.FALSE;
		}
		catch (IOException ioException)
		{
//			logger.warn("File " + sourceFile + " ioException", ioException);
			return Boolean.FALSE;
		}
	}

	/**
	 * Returns TRUE if Copy files successful.
	 */
	public Boolean copyAllFiles(File sourceDirectory, File targetDirectory)
	{
		Boolean result = null;
		Boolean overallResult = Boolean.TRUE;

		if (!sourceDirectory.isDirectory())
			return Boolean.FALSE;

		if (!targetDirectory.isDirectory())
			targetDirectory.mkdir();

		File[] list = sourceDirectory.listFiles();

		if (list != null)
		{
			for (int i = 0; i < list.length; i++)
			{
				if (list[i].isFile())
				{
					String fileName = list[i].getName();
					File sourceFilePath = new File(sourceDirectory, fileName);
					File targetFilePath = new File(targetDirectory, fileName);

					result = copyFile(sourceFilePath, targetFilePath);
					if (overallResult.booleanValue())
					{
						overallResult = result;
					}
				}
			}
		}

		return overallResult;
	}

	/**
	 * Returns TRUE if delete file successful.
	 */
	public Boolean deleteFile(File file)
	{
		if (!file.isFile())
		{
//			logger.warn("File " + file + " is not a file.");
			return Boolean.FALSE;
		}

		if (file.delete())
		{
//			logger.info("File " + file + " is deleted successfully.");
			return Boolean.TRUE;
		}
		else
		{
//			logger.info("File " + file + " cannot be deleted successfully.");
			return Boolean.FALSE;
		}
	}

	/**
	 * Returns TRUE if delete all files successful.
	 */
	public Boolean deleteAllFiles(File directory)
	{
		Boolean result = null;
		Boolean overallResult = Boolean.TRUE;

		if (!directory.isDirectory())
			return Boolean.FALSE;

		File[] list = directory.listFiles();

		if (list != null)
		{
			for (int i = 0; i < list.length; i++)
			{
				if (list[i].isFile())
				{
					String fileName = list[i].getName();
					File filePath = new File(directory, fileName);

					result = deleteFile(filePath);
					if (overallResult.booleanValue())
					{
						overallResult = result;
					}
				}
			}
		}

		return overallResult;
	}

	/**
	 * Returns TRUE if delete all files successful.
	 */
	public Boolean deleteFolder(File directory)
	{
		if (deleteAllFiles(directory).booleanValue())
		{
			if (directory.delete())
			{
//				logger.info("Folder " + directory + " is deleted successfully.");
				return Boolean.TRUE;
			}
			else
			{
//				logger.info("Folder " + directory + " cannot deleted successfully.");
				return Boolean.FALSE;
			}
		}
		
		return Boolean.FALSE;
	}

	/**
	 * Returns TRUE if backup files successful.
	 */
	public Boolean backUpFile(File sourceFilePath, File targetDirectory)
	{
		if (!sourceFilePath.isFile())
		{
//			logger.warn("File " + sourceFilePath + " is not a file.");
			return Boolean.FALSE;
		}
		if (!targetDirectory.isDirectory())
		{
			targetDirectory.mkdir();
		}

		String fileName = sourceFilePath.getName();
		File targetFilePath = new File(targetDirectory, fileName);

		if (copyFile(sourceFilePath, targetFilePath).booleanValue())
		{
			deleteFile(sourceFilePath);
		}

		return Boolean.TRUE;
	}

	/**
	 * Returns TRUE if backup files successful.
	 */
	public Boolean backUpAllFiles(File sourceDirectory, File targetDirectory)
	{
		if (copyAllFiles(sourceDirectory, targetDirectory).booleanValue())
		{
			return deleteAllFiles(sourceDirectory);
		}

		return Boolean.FALSE;
	}

	/**
	 * Returns TRUE if backup files successful.
	 */
	public Boolean backUpFolder(File sourceDirectory, File targetDirectory)
	{
		if (copyAllFiles(sourceDirectory, targetDirectory).booleanValue())
		{
			return deleteFolder(sourceDirectory);
		}

		return Boolean.FALSE;
	}


	/**
	 * Returns TRUE if folder is created successful.
	 */
	/*public File createFolder(File folderPath)
	{
		if (!folderPath.isDirectory())
		{
			folderPath.mkdir();
			chmod(folderPath, "777");
		}

		return folderPath;
	}*/

	/**
	 * Returns TRUE if folder is created successful.
	 */
	/*public File createFolder(File directory, String folderName)
	{
		File folderPath = new File(directory, folderName);

		createFolder(folderPath);

		return folderPath;
	}*/

	/*public boolean chmod(File file, String mode)
	{
		String os = System.getProperty("os.name");
		if (os.indexOf("Windows") != -1)
		{
			return true;
		}

		String cmd = "chmod " + mode + " " + file.getAbsolutePath();
		try
		{

//			logger.info("Executing chmod to : " + cmd);
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(cmd);

			// any error message?
			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERR");

			// any output?
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUT");

			// kick them off
			errorGobbler.start();
			outputGobbler.start();

			int exitVal = proc.waitFor();

			return exitVal == 0;
		}
		catch (Throwable t)
		{
//			logger.warn("Chmod command : " + cmd + " fail", t);
			return false;
		}
	}*/
}
