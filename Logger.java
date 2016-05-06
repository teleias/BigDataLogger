import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.contentstream.*;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class Logger {
	public static void main(String[] args) throws IOException
	{
		new Logger();
	}
	public Logger() throws IOException
	{
		//org.apache.pdfbox.contentstream.PDFStreamEngine p;
		//PDFStreamEngine pdcs = new PDFStreamEngine();
		//new PDFParser();
		PDFTextStripper ts = new PDFTextStripper();
		PDDocument d = PDDocument.load(new File("input.pdf"));
		System.out.println("There are "+d.getNumberOfPages()+" pages.");
		Pattern pdate, ptime, pmessage, pname;
		Matcher m;
		pdate 	= Pattern.compile(
				"(?<month>(January)|(February)|(March)|(April)|(May)|(June)|(July)|(August)|(September)|(October)|(November)|(December))"+
				".(?<day>\\d+)(\\w+)."+
				".(?<year>\\d+)"
				);
		ptime	 = Pattern.compile(
				"(?<hour>\\d+):(<minute>\\d+) (?<ampm>am|pm)"
				);
		pmessage = Pattern.compile(
				"(?<message>.+)"
				);
		pname = Pattern.compile(
				"^(?<name>\\w+)\\: (?<message>.+)"
				);
		String sDate, sTime, sName, sMessage;
		sDate = sTime = sName = sMessage = "";
		String[] arr = ts.getText(d).split("\n");
		d.close();
		for(String s : arr)
		//String s = "May 1st, 2016  ";
		//while(true)
		{
//			'\u00A0'
			s = s.replace('\u00A0', ' ').replaceAll("\\s", " ").trim();
			m = pname.matcher(s);
			if(m.find())
			{
				sName = m.group("name");
				sMessage = m.group("message");
				String id;
				Message mes = new Message(id = getId(sName, sDate+"\t"+sTime), sName, sDate+"\t"+sTime, sMessage);
				System.out.println(mes);
				map.put(id, mes);
				continue;
			}
			m = pdate.matcher(s);
			if(m.find())
			{
				sDate = m.group("month")+" "+m.group("day")+" "+m.group("year");
				continue;
			}
			m = ptime.matcher(s);
			if(m.find())
			{
				sTime = m.group("hour")+":"+m.group("minute")+" "+m.group("ampm");
				continue;
			}
			/*
			m = pmessage.matcher(s);
			if(m.find())
			{
				sMessage = m.group("message");
				String id;
				Message mes = new Message(id = getId(sName, sDate+"\t"+sTime), sName, sDate+"\t"+sTime, sMessage);
				System.out.println(mes);
				map.put(id, mes);
				continue;
			}
			*/
		}
	}
	static HashMap<String, Message> map = new HashMap<String, Message>();
	public class Message
	{
		String id;
		String name;
		String date;
		String message;
		public Message(String id_, String name_, String date_, String message_)
		{
			id = id_;
			name = name_;
			date = date_;
			message = message_;
		}
		public String toString()
		{
			return 
					//"id     : ["+id+"]\n"+
					"name   : ["+name+"]\n"+
					"date   : ["+date+"]\n"+
					"message: ["+message+"]\n";
		}
	}
	public static String getId(String n, String d)
	{
		return n + ":"+d;
	}
}
