#! /usr/bin/env python

import xml.etree.ElementTree as ET

class Recipient:
	''' The class that contains all information
	about a recipient. It contains get and set 
	methods for every information variable '''
	def __init__(self):
		self.init = "true"		
	def get_name(self):
		return self.name	
	def get_zipcode(self):
		return self.zipcode
	def get_city(self):
		return self.city
	def get_address1(self):
		return self.address1
        def get_address2(self):
                return self.address2
	def get_country(self):
		return self.country
	def get_recipientNo(self):
		return self.recipientNo
	def get_phone(self):
		return self.phone
	def get_mobile(self):
		return self.mobile
	def get_fax(self):
		return self.fax
	def get_email(self):
		return self.email
	def get_web(self):
		return self.web
	def get_creditDays(self):
		return self.creditDays
	def get_orgNo(self):
		return self.orgNo
	def get_preferredShipment(self):
		return self.preferredShipment
	def get_attachPdf(self):
		return self.attachPdf

	def set_name(self, localname):
		self.name = localname
	def set_zipcode(self, localzip):
		self.zipcode = localzip
	def set_city(self, localcity):
		self.city = localcity
	def set_address1(self, localaddr1):
		self.address1 = localaddr1
        def set_address2(self, localaddr2):
                self.address2 = localaddr2
	def set_country(self, localcountry):
		self.country = localcountry
	def set_recipientNo(self, localrecipientNo):
		self.recipientNo = localrecipientNo
        def set_phone(self, localphone):
		self.phone = localphone
        def set_mobile(self, localmobile):
		self.mobile = localmobile
        def set_fax(self, localfax):
		self.fax = localfax
        def set_email(self, localemail):
		self.email = localemail
        def set_web(self, localweb):
		self.web = localweb
        def set_creditDays(self, localcredit):
		self.creditDays = localcredit
        def set_orgNo(self, localorgNo):
		self.orgNo = localorgNo
        def set_preferredShipment(self, localpreferred):
		self.preferredShipment = localpreferred
        def set_attachPdf(self, localpdf):
		self.attachPdf = localpdf


				
def parse_recipients(xml):
	'''
	Parsing the xml result from 
	"get all recipients call" and makes
	recipient objects of it.
	'''
	recipients = []
	
	root = ET.fromstring(xml)
	for recipient in root:
		temp_recipient = Recipient()
		print recipient[0].text
		temp_recipient.set_name(recipient[0].text) 
		temp_recipient.set_zipcode(recipient[1].text)
		temp_recipient.set_city(recipient[2].text) 
		temp_recipient.set_address1(recipient[3][0].text) 
 		temp_recipient.set_address2(recipient[3][1].text)		
  		temp_recipient.set_country(recipient[3][2].text)
		temp_recipient.set_recipientNo(recipient[3][3].text)
  		temp_recipient.set_phone(recipient[3][4].text)
  		temp_recipient.set_mobile(recipient[3][5].text)
                temp_recipient.set_fax(recipient[3][6].text)
                temp_recipient.set_email(recipient[3][7].text)
                temp_recipient.set_web(recipient[3][8].text)
		temp_recipient.set_creditDays(recipient[3][10].text)
		temp_recipient.set_orgNo(recipient[3][11].text)
		temp_recipient.set_preferredShipment(recipient[3][11].text)
		temp_recipient.set_attachPdf(recipient[3][12].text)
		recipients.append(temp_recipient)
	return recipients				
