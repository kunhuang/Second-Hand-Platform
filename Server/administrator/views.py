from django.shortcuts import render
from django.http import HttpResponse

from administrator.models import *

import json
import time
from django.forms.models import model_to_dict
from django.template import RequestContext, loader
from django.db.models import Q

from json_api.models import Account_Info, Goods_Info, Log_Info, Message_Info, Comment_Info, Wish_List

# Create your views here.
def index(request):
    return HttpResponse(request.session['login'])

def login(request):
    #admin_id = request.POST['admin_id']
    #password = request.POST['password']

    request.session['login'] = True
    return HttpResponse("login")

def logout(request):
    request.session['login'] = False
    return HttpResponse("login")

def statistic(request):
    template = loader.get_template('statistic.html')
    context = RequestContext(request, {
        'test': 1,
    })
    return HttpResponse(template.render(context))

