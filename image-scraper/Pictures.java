import java.util.regex.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Pictures 
{	
	//method that takes the image url and file name and saves the image
	public static void saveImage(URL urlImage, String destinationFile) throws IOException 
	{
		InputStream is = urlImage.openStream(); //input (the image url)
		OutputStream os = new FileOutputStream(destinationFile); //output (the file name)
		byte[] b = new byte[2048]; //buffering the stream
		int length;

		//reads in the stream until it returns -1 (and there is nothing else to read)
		while ((length = is.read(b)) != -1) 
		{
			os.write(b, 0, length);
		}

		//closes the streams
		is.close();
		os.close();
	}
		
	public static void main(String[] args) throws IOException
	{
		BufferedReader reader = null;
		String wordListLine = null;
		try
		{
			//reads the first argument - a word from the input file
			reader = new BufferedReader(new FileReader(args[0]));
		}
		catch (Exception e)
		{
			//prints the error message
			System.out.println(e.getMessage());
		}
		
		//declared variables
		URL urlAddress;
		URL urlImage;
		String stringImage;
		String clipartURL = "";
		String destinationFile;
		String inLine;
		
		while ((wordListLine = reader.readLine()) != null) //loops through each line of the file
		{
			//assembles the openclipart URL in a string
			clipartURL = "http://openclipart.org/search/json/?query=" + wordListLine + "&page=1&amount=1";
			
			try 
	        {
	            urlAddress = new URL(clipartURL); //makes the new string a URL

				//loads the webpage 
	            BufferedReader in = new BufferedReader(new InputStreamReader(urlAddress.openStream()));
	            		
	            while ((inLine = in.readLine()) != null) //reads the page in, one line at a time
	            {
	                //process each line
					Pattern p = Pattern.compile("png_thumb.*?: \"(.*?)\""); //wanted image url
					Matcher m = p.matcher(inLine);

					if (m.find()) //if the pattern is found
					{
						//get the image url
		                stringImage = m.group(1);
						urlImage = new URL(stringImage);
						
						destinationFile = wordListLine + ".png"; //set the file name to the noun from the wordlist
						saveImage(urlImage, destinationFile); //call the saveImage method (to save the image)
					}
	            }
				//close the reader
	            in.close(); 
	 
	        } 
	        catch (MalformedURLException me) 
	        {
				//print error message
	            System.out.println(me); 
	 
	        } 
	        catch (IOException ioe) 
	        {
				//print error message
	            System.out.println(ioe);
	        }
		}
		//close the reader
		reader.close();
	}

}
