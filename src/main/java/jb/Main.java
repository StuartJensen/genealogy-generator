package jb;

import home.genealogy.schema.all.ObjectFactory;
import home.genealogy.schema.all.Reference;
import home.genealogy.schema.all.Title;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
	}
	
	
/*
 * 		try
		{
			JAXBContext jc = JAXBContext.newInstance("home.genealogy.schema.all");
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			
			SchemaFactory schemaFactory=SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			Schema schema = schemaFactory.newSchema(new File("c:\\genealogy\\SchemaV2.0\\all.xsd"));
			unmarshaller.setSchema(schema);

			Reference reference = (Reference)unmarshaller.unmarshal(new File("c:\\temp\\r2.xml"));
			System.out.println(reference);
			if (null != reference)
			{
				System.out.println(reference.getCitation().getPlace().getCountry());
				System.out.println(reference.getCitation().getAuthority().getTitle().getName());
			}
			
			// Now format the XMl and output it!
			ObjectFactory objFactory = new ObjectFactory();
			Title title = objFactory.createTitle();
			title.setName("The bigger and bestest title");
			title.setContent("The content");
			reference.getCitation().getAuthority().setTitle(title);			
			reference.getCitation().getPlace().setCountry("Mars");
			
			Marshaller marshaller = jc.createMarshaller();

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
			marshaller.marshal(reference, new FileOutputStream("c:\\temp\\r2Prime.xml"));

			
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
 */

}
