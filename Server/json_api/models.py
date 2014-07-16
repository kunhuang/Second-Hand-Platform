from django.db import models
import time
import PIL
# Create your models here.
class Account_Info(models.Model):
	name = models.CharField(max_length = 16)
	email = models.CharField(max_length = 32)
	password = models.CharField(max_length = 16)
	type_seller = models.BooleanField(default = 1)
	type_buyer = models.BooleanField(default = 1)
	sell_exp = models.IntegerField(max_length = 4)
	buy_exp = models.IntegerField(max_length = 4)
	phone = models.CharField(max_length = 11, default = 0)
	bank_card = models.IntegerField(default = 0)
	#photo = models.ImageField(upload_to = 'account_image', null = True)
	
	@staticmethod
	def validate_email(email, password):
		try:
			account = Account_Info.objects.get(email = email, password = password)
		except Exception, e:
			return False
		else:
			return account.id

	@staticmethod
	def validate_id(id, password):
		try:
			account = Account_Info.objects.get(id = id, password = password)
		except Exception, e:
			return False
		else:
			return True
		
	@staticmethod
	def add_account(name, email, password, phone):
		try:
			Account_Info.objects.get(email = email)
			return -2
		except:
			account_info = Account_Info.objects.create(
				name = name,
				email = email,
				password = password,
				phone = phone,
				sell_exp = 0,
				buy_exp = 0,
				)
			return True
	
class Goods_Info(models.Model):
	'''
	state_choice = (
		('I', 'initiated'),
		('O', 'on_sale'),
		('B', 'bought'),
		('C', 'closed'),
		)
	'''
	name = models.CharField(max_length = 16)
	description = models.TextField()
	seller_id = models.ForeignKey('Account_Info', related_name = 'seller_id')
	state = models.CharField(max_length = 1,
		default = 'I')
	pure_price = models.IntegerField(max_length = 4)
	buyer_id = models.ForeignKey('Account_Info', related_name = 'buyer_id', null = True)
	#photo = models.ImageField(upload_to = 'goods_image', null = True)

class Log_Info(models.Model):
	'''
	type_choice = (
		('I', 'initiated'),
		('O', 'on_sale'),
		('B', 'bought'),
		('C', 'closed'),
		('U', 'update')
		)
	'''
	goods_id = models.ForeignKey('Goods_Info')
	time = models.IntegerField(max_length = 10)
	type = models.CharField(max_length = 1)

class Message_Info(models.Model):
	send_account_id = models.IntegerField(max_length = 8)
	recv_account_id = models.ForeignKey('Account_Info')
	time = models.IntegerField(max_length = 10)
	subject = models.TextField()
	content = models.TextField()
	state = models.BooleanField(default = 0)

class Comment_Info(models.Model):
	goods_id = models.ForeignKey('Goods_Info')
	account_id = models.ForeignKey('Account_Info')
	time = models.IntegerField(max_length = 10)
	content = models.TextField()

class Wish_List(models.Model):
	account_id = models.ForeignKey('Account_Info')
	goods_id = models.ForeignKey('Goods_Info')
	time = models.IntegerField(max_length = 10)
	payed = models.BooleanField(default = 0)
