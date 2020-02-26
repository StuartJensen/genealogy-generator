package home.genealogy.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import home.genealogy.configuration.CFGFamily;

public class MarshallUtil
{
	public static final String SCHEMA_PACKAGE = "home.genealogy.schema.all";
	public static final String SCHEMA_URL = "http://www.w3.org/2001/XMLSchema";
	
	public static Unmarshaller createUnMarshaller(String strSchemaFileName)
		throws JAXBException, SAXException
	{
		JAXBContext jc = JAXBContext.newInstance(SCHEMA_PACKAGE);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		SchemaFactory schemaFactory = SchemaFactory.newInstance(SCHEMA_URL);
		Schema schema = schemaFactory.newSchema(new File(strSchemaFileName));
		unmarshaller.setSchema(schema);
		return unmarshaller;
	}
	
	public static Marshaller createMarshaller(String strSchemaFileName, boolean bFormattedOutput)
		throws JAXBException, SAXException
	{
		JAXBContext jc = JAXBContext.newInstance(SCHEMA_PACKAGE);
		Marshaller marshaller = jc.createMarshaller();
		SchemaFactory schemaFactory = SchemaFactory.newInstance(SCHEMA_URL);
		Schema schema = schemaFactory.newSchema(new File(strSchemaFileName));
		marshaller.setSchema(schema);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, bFormattedOutput);
		return marshaller;
	}
	
	public static void marshall(Marshaller marshaller, Object source, String strOutputFile)
	{
		FileOutputStream fos = null;
		try
		{
			try
			{
				fos = new FileOutputStream(new File(strOutputFile));
				marshaller.marshal(source, fos);
			}
			catch (JAXBException jb)
			{
				System.out.println("Exception marshalling object: " + source.getClass().getName() + ": " + jb.toString());
				return;
			}
			catch (IOException ie)
			{
				System.out.println("Exception handling " + strOutputFile + " file: " + ie.toString());
				return;
			}
		}
		finally
		{
			if (null != fos)
			{
				try{fos.close();}catch(IOException ioe) {}
			}
		}
	}


}
