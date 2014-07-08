from django.shortcuts import render
from django.http import HttpResponse

from json_api.models import Account_Info, Goods_Info, Log_Info, Message_Info, Comment_Info, Wish_List

import json
from django.forms.models import model_to_dict

# Create your views here.
def index(request):
	return HttpResponse('Hello World')