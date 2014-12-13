import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.text.html.HTMLDocument.HTMLReader.SpecialAction;


public class Segmentor {

	private String coordinateFilePath;
	private String userInfoFilePath;
	ArrayList<Double> speeds = new ArrayList<Double>();

	public Segmentor(String coorFilePath, String userPath)
	{
		this.coordinateFilePath = coorFilePath;
		this.userInfoFilePath = userPath;
		
	}
	
	// this method accept two location points in degree and compute the distance between them
	private double calcDistance(double lat1, double lon1, double lat2, double lon2)
	{
		double R = 6371; // Radius of the earth in km
		double dLat = deg2rad(lat2-lat1);  // deg2rad below
		double dLon = deg2rad(lon2-lon1); 
		 double a = 
		    Math.sin(dLat/2) * Math.sin(dLat/2) +
		    Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * 
		    Math.sin(dLon/2) * Math.sin(dLon/2); 
		  
		 double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		 double d = R * c * 1000; // Distance in m
		 return d;
	}
	
	// degree to radian
	private double deg2rad(double deg) {
		  return deg * (Math.PI/180);
	}
	
	private void performFirstSegmentation() throws IOException
	{
		BufferedReader firstBuffer = null;
		BufferedReader secondBuffer = null;
		
		String firstCurrentline = "";
		String tempLine = "";
		String secondCurrentLine = "";
		String cvsdelimiter = ",";

		double numOfExecution = 0;

		// result file
		File file = new File("firstStep-segmented20%.csv");
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);

		try {

			firstBuffer = new BufferedReader(new FileReader(coordinateFilePath));
			
			// add the column names as the first record
			firstCurrentline = firstBuffer.readLine();
			bw.write(firstCurrentline);
			bw.write("\n");
			
			// read first recrod
			firstCurrentline = firstBuffer.readLine();
			bw.write(firstCurrentline);
			bw.write("\n");
			String[] firstParams = firstCurrentline.split(cvsdelimiter);
	
			String userId = firstParams[1];
			String firstTime = firstParams[9];
			String firstDay = (firstTime.split(" "))[0];
			
			// read forth record
			secondBuffer = new BufferedReader(new FileReader(coordinateFilePath));
			secondCurrentLine = secondBuffer.readLine();
			secondCurrentLine = secondBuffer.readLine();
			secondCurrentLine = secondBuffer.readLine();
			secondCurrentLine = secondBuffer.readLine();
			String[] secondParams = secondCurrentLine.split(cvsdelimiter);

			String userIdSecond = secondParams[1];
			String secondTime = secondParams[9];
			String secondDay = (secondTime.split(" "))[0];

			// read fifth record and compare it with the first, the step is five for comparison
			while ((secondCurrentLine = secondBuffer.readLine()) != null) 
			{

				numOfExecution++;
				System.out.println(numOfExecution);
				
				secondParams = secondCurrentLine.split(cvsdelimiter);

				userIdSecond = secondParams[1];
				secondTime = secondParams[9];
				secondDay = (secondTime.split(" "))[0];


				if(secondDay.equals(firstDay) && userId.equals(userIdSecond))
				{

					double distance = calcDistance(Double.parseDouble(firstParams[2]), 
							Double.parseDouble(firstParams[3]), Double.parseDouble(secondParams[2]), 
							Double.parseDouble(secondParams[3]));
					if(distance < 5)
					{
						bw.write("*,*,*,*,*,*,*,*,*,*,*");
						bw.write("\n");
					}
					
				}
				
				tempLine = firstBuffer.readLine();
				String[] tempParams = tempLine.split(cvsdelimiter);
				String tempTime = tempParams[9];
				String tempDay = (tempTime.split(" "))[0];
				
				if(!(tempParams[1].equals(userId)) || !tempDay.equals(firstDay))
				{
					bw.write("*,*,*,*,*,*,*,*,*,*,*");
					bw.write("\n");
					
				}

				firstCurrentline = tempLine;
				bw.write(firstCurrentline);
				bw.write("\n");
				firstParams = firstCurrentline.split(cvsdelimiter);
		
				userId = firstParams[1];
				firstTime = tempTime;
				firstDay = tempDay;


			}
			
			while ((tempLine = firstBuffer.readLine()) != null) 
			{
				String[] tempParams = tempLine.split(cvsdelimiter);
				String tempTime = tempParams[9];
				String tempDay = (tempTime.split(" "))[0];
				
				System.out.println("here in the final last" + numOfExecution);
				
				if(!(tempParams[1].equals(userId)) || !tempDay.equals(firstDay))
				{
					bw.write("*,*,*,*,*,*,*,*,*,*,*");
					bw.write("\n");
					
				}

				firstCurrentline = tempLine;
				bw.write(firstCurrentline);
				bw.write("\n");
				firstParams = firstCurrentline.split(cvsdelimiter);
		
				userId = firstParams[1];
				firstTime = tempTime;
				firstDay = tempDay;
				bw.write(firstCurrentline);
				bw.write("\n");

			}
			bw.close();
			System.out.println("Finished - Check the results!");
			
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	
	private boolean IsSegmented(String dirStr1, String dirStr2)
	{
		double direction1 = Double.parseDouble(dirStr1);
		double direction2 = Double.parseDouble(dirStr2);
		
		double max = 0;
		double min = 0;
		
		if(direction1 > direction2)
		{
			max = direction1;
			min = direction2;
		}else
		{
			max = direction2;
			min = direction1;
		}
			
		
		double delta = max - min;
		
		if(delta < 180)
		{
			if( delta > 100)
				return true;
		}
		else
		{
			if ( ((360 - max) + min) > 100)
				return true;
		}
		
		return false;
	}
	
	
	private void performSecondSegmentation(String firstSegmentFilePath) throws IOException
	{
		BufferedReader bufferr = null;
		String line = "";
		String cvsdelimiter = ",";
		boolean starSeen = false;
		
		double numberOfExecution = 0;

		String starString = "*,*,*,*,*,*,*,*,*,*,*";

		// result file
		File file = new File("secondStep-segmented20%.csv");
		FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bufferWriter = new BufferedWriter(fileWriter);

		
		try {
			 
			bufferr = new BufferedReader(new FileReader(firstSegmentFilePath));
			line = bufferr.readLine();
			
			numberOfExecution++;
			System.out.println(numberOfExecution);
			
			bufferWriter.write(line);
			bufferWriter.write('\n');
			
			line = bufferr.readLine();
			String[] startParams = line.split(cvsdelimiter);

			numberOfExecution++;
			System.out.println(numberOfExecution);

			String direction1 = startParams[6];
			bufferWriter.write(line);
			bufferWriter.write('\n');

			while ((line = bufferr.readLine()) != null) {

				numberOfExecution++;
				System.out.println(numberOfExecution);

				// use comma as separator
				String[] parameters = line.split(cvsdelimiter);

				/*String parametersInLine = new String("");
				for(int i = 0; i < parameters.length; i++)
				{
					parametersInLine = parametersInLine + "     " + parameters[i]; 
				}
				System.out.println(parametersInLine);*/

				if(starSeen)
				{
					bufferWriter.write(line);
					bufferWriter.write('\n');
					
					if(!parameters[0].equals("*"))
					{
						startParams = parameters;
						direction1 =  parameters[6];
						starSeen = false;
					}  
						
					
				} else
				{
				
					if(parameters[0].equals("*"))
					{
						bufferWriter.write(line);
						bufferWriter.write('\n');
						starSeen = true;
					}
					else{	
						String direction2 = parameters[6];
						
						if( IsSegmented(direction1, direction2) )
						{
							bufferWriter.write(starString);
							bufferWriter.write('\n');
						} 
		
						startParams = parameters;
						direction1 = startParams[6]; 
						bufferWriter.write(line);
						bufferWriter.write('\n');
					}
				}
			}
			
			bufferWriter.close();
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferr != null) {
				try {
					bufferr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
	private void mergeSegment(String secondSegmentedFilePath) throws IOException
	{
		double numOfExecution = 0;
		BufferedReader bufferr = null;
		String line = "";
		String cvsdelimiter = ",";
		boolean startSeen = false;
		
		// result file
		File file = new File("merged-segmented20%.csv");
		FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bufferWriter = new BufferedWriter(fileWriter);

		
		
		try {
			 
			bufferr = new BufferedReader(new FileReader(secondSegmentedFilePath));
			line = bufferr.readLine();
			
			// print heading of the table
			bufferWriter.write("user_id, start_lat,	end_lat, start_long, end_long, start_alt, end_alt,	min_speed,"
					+ 	"max_speed,	avg_speed, std_speed, timestamp" );

			bufferWriter.write('\n');
			
			line = bufferr.readLine();
			String[] startParams = line.split(cvsdelimiter);

			//String id = startParams[0];
			String userId = startParams[1];
			
			double startLat = Double.parseDouble(startParams[2]);
			double endLat = Double.parseDouble(startParams[2]);
			
			double startLong = Double.parseDouble(startParams[3]);
			double endLong = Double.parseDouble(startParams[3]);
			
			double startAlt = Double.parseDouble(startParams[4]);
			double endAlt = Double.parseDouble(startParams[4]);
			
			double minSpeed = Double.parseDouble(startParams[5]);
			double maxSpeed = Double.parseDouble(startParams[5]);
			double avgSpeed = Double.parseDouble(startParams[5]);
			
			speeds.add(Double.parseDouble(startParams[5]));
			
			String timeStamp = startParams[9];
			//String method = startParams[10];
			
			double numOfRecords = 1;

			while ((line = bufferr.readLine()) != null) {
	 
				numOfExecution++;
				System.out.println(numOfExecution);
				
				
				// use comma as separator
				String[] parameters = line.split(cvsdelimiter);

					/*String parametersInLine = new String("");
					for(int i = 0; i < parameters.length; i++)
					{
						parametersInLine = parametersInLine + "     " + parameters[i]; 
					}
					System.out.println(parametersInLine);*/
					if(startSeen)
					{
						if(!parameters[0].equals("*")){
							
							numOfRecords++;

							startLat = Double.parseDouble(parameters[2]);
							endLat = Double.parseDouble(parameters[2]);
							
							startLong = Double.parseDouble(parameters[3]);
							endLong = Double.parseDouble(parameters[3]);
							
							startAlt = Double.parseDouble(parameters[4]);
							endAlt = Double.parseDouble(parameters[4]);
							
							minSpeed = Double.parseDouble(parameters[5]);
							maxSpeed = Double.parseDouble(parameters[5]);
							avgSpeed = Double.parseDouble(parameters[5]);
							speeds.add(Double.parseDouble(parameters[5]));
							
							timeStamp = parameters[9];
							//method = parameters[10];
							
							speeds.add(Double.parseDouble(parameters[5]));

							
							/*bufferWriter.write(id + "," + userId + "," +
									String.valueOf(startLat) + "," + String.valueOf(endLat) + "," + String.valueOf(startLong) + "," 
									+ String.valueOf(endLong) + "," + String.valueOf(startAlt) + "," + String.valueOf(endAlt) + "," +
									String.valueOf(minSpeed) + "," + String.valueOf(maxSpeed) + "," + String.valueOf(avgSpeed) + "," ); 
									// + timeStamp + "," + method );
							bufferWriter.write('\n');*/
							startSeen = false;
						}
					} else {
					
						if(parameters[0].equals("*"))
						{
							avgSpeed = avgSpeed / numOfRecords;
							
							//calculate the std
							double speedStd = calcStDev(avgSpeed);
							
							// empty the speeds list
							speeds.clear();
							
								bufferWriter.write(userId + "," +
										String.valueOf(startLat) + "," + String.valueOf(endLat) + "," + String.valueOf(startLong) + "," 
										+ String.valueOf(endLong) + "," + String.valueOf(startAlt) + "," + String.valueOf(endAlt) + "," +
										String.valueOf(minSpeed) + "," + String.valueOf(maxSpeed) + "," + String.valueOf(avgSpeed) + "," + 
										String.valueOf(speedStd) + "," + timeStamp );
								//+ "," + method );
								bufferWriter.write('\n');
							
								numOfRecords = 0;
								avgSpeed = 0;
							startSeen = true;	
							
						} else 
						{
							numOfRecords++;

							//id = parameters[0];
							userId = parameters[1];
							timeStamp = parameters[9];
							//method = parameters[10];
							
							// check for latitude
							if(Double.parseDouble(parameters[2]) < startLat)
								startLat = Double.parseDouble(parameters[2]);
		
							if(Double.parseDouble(parameters[2]) > endLat)
								endLat = Double.parseDouble(parameters[2]);
							
							if(Double.parseDouble(parameters[3]) < startLong)
								startLong = Double.parseDouble(parameters[3]);
		
							if(Double.parseDouble(parameters[3]) > endLong)
								endLong = Double.parseDouble(parameters[3]);
							
							if(Double.parseDouble(parameters[4]) < startAlt)
								startAlt = Double.parseDouble(parameters[4]);
							
							if(Double.parseDouble(parameters[4]) > endAlt)
								endAlt = Double.parseDouble(parameters[4]);
							
							if(Double.parseDouble(parameters[5]) < minSpeed)
								minSpeed = Double.parseDouble(parameters[5]);
		
							if(Double.parseDouble(parameters[5]) > maxSpeed)
								maxSpeed = Double.parseDouble(parameters[5]);
							
							avgSpeed = avgSpeed + Double.parseDouble(parameters[5]);
							
							speeds.add(Double.parseDouble(parameters[5]));
						}
						
					}
			}
			bufferWriter.close();
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferr != null) {
				try {
					bufferr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
	private double calcStDev(double average)
	{
		int num = speeds.size();
		
		double totalSquares = 0;
		
		if(num > 1)
		{
			for(int i = 0; i < num - 1; i++)
			{
				totalSquares = totalSquares + Math.pow( (speeds.get(i) - average) , 2);
			}
			
			double variance = totalSquares / (num -1);
			
			return Math.sqrt(variance);

		} else
			return 0;
		
	}
	
	private void addTransportMethod(String mergeFilePath) throws IOException
	{
		BufferedReader bufferrSegFile = null;
		String line = "";
		String cvsdelimiter = ",";
		
		File file = new File("transport-method.txt");

		FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		
		double numOfExecution = 0;
		
		try {
			 
			bufferrSegFile = new BufferedReader(new FileReader(mergeFilePath));
			
			line = bufferrSegFile.readLine();
			while ((line = bufferrSegFile.readLine()) != null) {
	 
				numOfExecution++;
				// use comma as separator
				String[] parameters = line.split(cvsdelimiter);
				//bw.write(parameters.toString());

					/*String parametersInLine = new String("");
					for(int i = 0; i < parameters.length; i++)
					{
						parametersInLine = parametersInLine + "     " + parameters[i]; 
					}
					System.out.println(parametersInLine); */
				
				if(parameters.length > 3)
				{
					String method = extractTransportMethod(parameters);
					//System.out.println(method + "------------");
					if(!method.isEmpty())
						bufferedWriter.write(method);
					else
						bufferedWriter.write("Not available");
						
					bufferedWriter.write('\n');
				}
				System.out.println(numOfExecution);
			}
			bufferedWriter.close();
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferrSegFile != null) {
				try {
					bufferrSegFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		
	}

	private String extractTransportMethod(String[] parameters)
	{
		String line = "";
		String result = "";
	
		String cvsdelimiter = ",";
		BufferedReader bufferrCoorFile = null;
		
		try {
			bufferrCoorFile = new BufferedReader(new FileReader(userInfoFilePath));
			
			line = bufferrCoorFile.readLine();
			
			//System.out.println("reading coordinate file");
			while ((line = bufferrCoorFile.readLine()) != null) {
			    // use comma as separator
				String[] userParameters = line.split(cvsdelimiter);

				if(userParameters[0].equals(parameters[0]))
				{
					result = userParameters[3]; 
					return result;
					
				}
			}
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (bufferrCoorFile != null) {
				try {
					bufferrCoorFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}


	
	public static void main(String[] args) throws IOException {
		
		String coorPath = "C://Users/Zohreh/workspace/Segmentor/Data/50%/coordinates_50pct.csv";
		String userInfoPath = "C://Users/Zohreh/workspace/Segmentor/Data/50%/users_50pct.csv"; 
		
		Segmentor zoeSegmentor = new Segmentor(coorPath, userInfoPath);
		
		// perform first step of segmentation
		zoeSegmentor.performFirstSegmentation();
		
		//perform second step of segmentation
		zoeSegmentor.performSecondSegmentation("C://Users/Zohreh/workspace/Segmentor/firstStep-segmented20%.csv");
		
		//merge segments
		zoeSegmentor.mergeSegment("C://Users/Zohreh/workspace/Segmentor/secondStep-segmented20%.csv");
		
		//add method of transport from user's data
		zoeSegmentor.addTransportMethod("merged-segmented20%.csv");
		
	}

}
