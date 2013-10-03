#! /usr/bin/env python

import requests
import recipient.recipient
import recipient.parser

class SwsClient:
	def __init__(self,username,password):
		self.username = username;
		self.password = password;

	def getAllRecipients(self):
		'''
		Gets an xml from using the sendregning api
		and sends it to the parsing function
		'''
		result = requests.get('https://www.sendregning.no/ws/butler.do?action=select&type=recipient', auth=(self.username, self.password))
		if(result.status_code != 200):
			return -1
		return recipient.parser.parse_recipients(result.text)


	def getRecipient(self,recipientNo):
		'''
		Gets one recipient based on the recipient number
		and sends it to the parsing function
		'''
		result = requests.get('https://www.sendregning.no/ws/butler.do?action=select&type=recipient&recipientNo=%s' % recipientNo, auth=(self.username, self.password))
		if (result.status_code != 200):
			return -1
		return recipient.parser.parse_recipients(result.text)
