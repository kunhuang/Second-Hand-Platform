from django.db import models
import time

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
	
	@staticmethod
	def validate(email, password):
		try:
			Account_Info.objects.get(email = email, password = password)
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
			Account_Info.objects.add(account_info)
			return True
	
class Goods_Info(models.Model):
	state_choice = (
		('I', 'initiated'),
		('O', 'on_sale'),
		('B', 'bought'),
		('C', 'closed'),
		)
	name = models.CharField(max_length = 16)
	description = models.TextField()
	seller_id = models.CharField(max_length = 8)
	state = models.CharField(max_length = 1,
		choices = state_choice,
		default = 'I')
	pure_price = models.IntegerField(max_length = 4)
	buyer_id = models.CharField(max_length = 8)
	#photo = models.ImageField(upload_to = '~/image')

class Log_Info(models.Model):
	type_choice = (
		('I', 'initiated'),
		('O', 'on_sale'),
		('B', 'bought'),
		('C', 'closed'),
		('U', 'update')
		)
	goods_id = models.IntegerField(max_length = 16)
	time = models.IntegerField(max_length = 10)
	type = models.CharField(max_length = 1,
		choices = type_choice)

class Message_Info(models.Model):
	send_account_id = models.IntegerField(max_length = 8)
	recv_account_id = models.IntegerField(max_length = 8)
	time = models.IntegerField(max_length = 10)
	subject = models.TextField()
	content = models.TextField()
	state = models.BooleanField(default = 0)

class Comment_Info(models.Model):
	goods_id = models.IntegerField(max_length = 16)
	account_id = models.IntegerField(max_length = 8)
	time = models.IntegerField(max_length = 10)
	content = models.TextField()

class Wish_List(models.Model):
	account_id = models.IntegerField(max_length = 8)
	goods_id = models.IntegerField(max_length = 16)
	time = models.IntegerField(max_length = 10)
	payed = models.BooleanField(default = 0)
