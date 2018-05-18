package be.vdab.groenetenen.mail;

import be.vdab.groenetenen.entities.Offerte;

public interface MailSender {
	
	void nieuweOfferte(Offerte offerte);
}
