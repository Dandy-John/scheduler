package scheduler.io;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import scheduler.model.ListenerStep;

public class XmlTransfer {
	public static void toXML(ListenerStep listenerStep, String path) {
		try {
			File file = new File(path);
			JAXBContext jaxbContext = JAXBContext.newInstance(ListenerStep.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(listenerStep, file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public static ListenerStep fromXML(String path) {
		ListenerStep listenerStep;
		try {
			File file = new File(path);
			JAXBContext jaxbContext = JAXBContext.newInstance(ListenerStep.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			listenerStep = (ListenerStep)jaxbUnmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			listenerStep = null;
		}
		return listenerStep;
	}
}
