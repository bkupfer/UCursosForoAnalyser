package Parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/** 
 * Parses the HTML of standard ordinary regular UCursos institutional forum, into a data-set form \tab separated, usable for further analysis. 
 * @author Bernardo <Talento> Kupfer
 */
public class MainParser {

	public static void main(String[] args) throws IOException {

		BufferedReader reader;
		PrintWriter writer = new PrintWriter("DataSet/data-set.txt", "UTF-8");
		writer.println("== Data set - UCursos Forum ==\n@Authors: B.Kupfer & J.Sanz & All the comunity\n");
		writer.println("msg-id\t\tmsg-class\t\tpost-title\t\tresponses\t\tlikes\t\tdislikes\t\tauthor");
						
		String sCurrentLine;
		String msg_id, msg_class, author, post_title = null, amount_of_responses, likes, dislikes, post_content;
		
		// TODO: Loop for every file !
		File file = new File("Pages/page0.html"); // add file name here 
		reader = new BufferedReader(new FileReader(file));
		
		while ((sCurrentLine = reader.readLine()) != null) {
			if (sCurrentLine.contains("id=\"mensaje_")){
				// getting msg_id
				int findex = sCurrentLine.indexOf("id=\"") + "id=\"".length();
				int lindex = sCurrentLine.indexOf("\" ", findex); 
				msg_id = sCurrentLine.substring(findex, lindex);
				
				// getting class
				if (sCurrentLine.contains("raiz"))
					msg_class = "raiz"; 
				else 
					msg_class = "hijo"; 
				
				// there are two kinds of posts: the post itself, and the comment/responses to the post. 
				if (msg_class.equals("raiz")){ // main thread
					// Getting post title
					sCurrentLine = reader.readLine(); 
					findex = sCurrentLine.indexOf(")\">") + ")\">".length();
					lindex = sCurrentLine.indexOf("</a>"); 
					post_title = sCurrentLine.substring(findex, lindex); 
					
					// getting amount of responses
					reader.readLine(); reader.readLine(); 
					sCurrentLine = reader.readLine().trim(); 
					amount_of_responses = sCurrentLine.substring(1, sCurrentLine.length() -1); 
					
					// getting ups 
					reader.readLine(); 
					sCurrentLine = reader.readLine(); 
					likes = "0"; dislikes = "0"; 
					if (sCurrentLine.contains("<a class")){ // a post can or can not have likes and/or dislikes
						// getting likes
						findex = sCurrentLine.indexOf("')\">+") + "')\">+".length(); 
						lindex = sCurrentLine.indexOf(" persona");
						likes = sCurrentLine.substring(findex, lindex); 
						
						// getting dislikes
						reader.readLine(); 
						sCurrentLine = reader.readLine(); 
						if (!sCurrentLine.contains("class=\"metadata\"")){
							dislikes = sCurrentLine.substring(1, sCurrentLine.indexOf(" "));
							sCurrentLine = reader.readLine();
						}
					}
			
					// getting author
					if (sCurrentLine.contains("<span class=\"adhesion\"></span>\t</em>"))
						reader.readLine(); 
					sCurrentLine = reader.readLine(); 					
					findex = sCurrentLine.indexOf("/\">") + "/\">".length();
					lindex = sCurrentLine.indexOf("</a>");
					author = sCurrentLine.substring(findex, lindex); 
			
					/* under the scenario that time-stamp and/or category needed is required, here is where you need to add code */
					
					// TODO: get the first post msg.
				}
				else { // response to the main thread
					// setting null variables
					amount_of_responses = "-";
					likes = "-"; 
					dislikes = "-"; 
					
					// getting author
					reader.readLine(); reader.readLine(); reader.readLine(); 
					sCurrentLine = reader.readLine(); 
					findex = sCurrentLine.indexOf("/\">") + "/\">".length();
					lindex = sCurrentLine.indexOf("</a>", findex);
					author = sCurrentLine.substring(findex, lindex);
					
				}
				
				writer.println(msg_id + "\t\t" + msg_class + "\t\t" + post_title + "\t\t" + amount_of_responses + "\t\t" + likes + "\t\t" + dislikes + "\t\t" + author);
			}
		}

		reader.close();
		writer.close();
		
		System.out.println("Parsing succesfull");
		System.exit(0); 
	}

}
