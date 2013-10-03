#! /usr/bin/env python

import xml.etree.ElementTree as ET
import recipient as rm

def parse_recipients(xml):
    '''
    Parsing the xml result from 
    "get all recipients call" and makes
    recipient objects of it.
    '''
    recipients = []
    
    root = ET.fromstring(xml)
    for recipient in root:
        temp_recipient = rm.Recipient()
	for elementInRecipient in recipient:
	    if elementInRecipient.tag == "name":	
		temp_recipient.set_name(elementInRecipient.text)
	    if elementInRecipient.tag == "zip":
		temp_recipient.set_zipcode(elementInRecipient.text)
	    if elementInRecipient.tag == "city":
		temp_recipient.set_city(elementInRecipient.text) 
	    if elementInRecipient.tag == "optional":
		for elementInOptional in elementInRecipient:
		    if elementInOptional.tag == "address1":	
                        temp_recipient.set_address1(elementInOptional.text)
		    if elementInOptional.tag == "address2":
                        temp_recipient.set_address2(elementInOptional.text)		
		    if elementInOptional.tag == "country":
  		        temp_recipient.set_country(elementInOptional.text)
		    if elementInOptional.tag == "recipientNo":
		        temp_recipient.set_recipientNo(elementInOptional.text)
		    if elementInOptional.tag == "phone":
                        temp_recipient.set_phone(elementInOptional.text)
		    if elementInOptional.tag == "mobile":
  		        temp_recipient.set_mobile(elementInOptional.text)
		    if elementInOptional.tag == "fax":
			temp_recipient.set_fax(elementInOptional.text)
                    if elementInOptional.tag == "email":
			temp_recipient.set_email(elementInOptional.text)
                    if elementInOptional.tag == "web":
			temp_recipient.set_web(elementInOptional.text)
		    if elementInOptional.tag == "comment":
			temp_recipient.set_comment(elementInOptional.text)
		    if elementInOptional.tag == "creditDays":
			temp_recipient.set_creditDays(elementInOptional.text)
		    if elementInOptional.tag == "orgNo":
			temp_recipient.set_orgNo(elementInOptional.text)
		    if elementInOptional.tag == "preferredShipment":
			temp_recipient.set_preferredShipment(elementInOptional.text)
		    if elementInOptional.tag == "attachPdf":
			temp_recipient.set_attachPdf(elementInOptional.text)
        recipients.append(temp_recipient)
    return recipients				
