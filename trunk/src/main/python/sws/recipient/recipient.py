#! /usr/bin/env python

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
	def get_comment(self):
		return self.comment
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
	def set_comment(self, localcomment):
		self.comment = localcomment
        def set_creditDays(self, localcredit):
		self.creditDays = localcredit
        def set_orgNo(self, localorgNo):
		self.orgNo = localorgNo
        def set_preferredShipment(self, localpreferred):
		self.preferredShipment = localpreferred
        def set_attachPdf(self, localpdf):
		self.attachPdf = localpdf
			   				
