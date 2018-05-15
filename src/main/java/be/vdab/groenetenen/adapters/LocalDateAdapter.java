package be.vdab.groenetenen.adapters;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;

// Een class die JAXB helpt data te converteren erft van XmlAdapter.
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
	
	// JAXB roept deze method op als JAXB een String naar een LocalDate moet omzetten.
	@Override
	public LocalDate unmarshal(String string) throws Exception {
		return LocalDate.parse(string);
	}
	
	// JAXB roept deze method op als JAXB een LocalDate naar een String moet omzetten.
	@Override
	public String marshal(LocalDate date) throws Exception {
		return date.toString();
	}
}
