# coding=utf-8
from django.shortcuts import render, redirect
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
    return redirect('login')
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

def manage_account(request):
    if 'login' not in request.session or request.session['login'] == False:
        return HttpResponse('401 Error')

    account_array = Account_Info.objects.all()
    template = loader.get_template('manage_account.html')
    context = RequestContext(request, {
        'account_array': account_array,
    })
    return HttpResponse(template.render(context))
def edit_account(request):
    try:
        if 'login' not in request.session or request.session['login'] == False:
            return HttpResponse('401 Error')

        account = Account_Info.objects.get(id = request.GET['account_id'])

        if 'name' in request.POST:
            account.name = request.POST['name']
            account.email = request.POST['email']
            #account.password = request.POST['password']
            account.sell_exp = request.POST['sell_exp']
            account.buy_exp = request.POST['buy_exp']
            account.phone = request.POST['phone']
            account.bank_card = request.POST['bank_card']
            account.save()

            #return redirect('edit_account?account_id=%s' % request.GET['account_id'])
            return redirect('manage_account')
        else:
            template = loader.get_template('account_detail.html')
            context = RequestContext(request, {
                'account': account,
                'account_dict': model_to_dict(account),
                'account_dict_key': model_to_dict(account).keys()
            })
            return HttpResponse(template.render(context))

    except Exception, e:
        print e
        return HttpResponse('')
    else:
        pass
    finally:
        pass

def send_message(request):
    try:
        if 'login' not in request.session or request.session['login'] == False:
            return HttpResponse('401 Error')

        account = Account_Info.objects.get(id = request.GET['account_id'])

        if 'subject' in request.POST:
            Message_Info.objects.create(
                recv_account_id = account,
                subject = request.POST['subject'],
                content = request.POST['content'],
                time = int(time.time()),
                state = 0,
                send_account_id = 0, #表示系统管理员
            )
            return redirect('manage_account')

        else:
            template = loader.get_template('send_message.html')
            context = RequestContext(request, {
                'account': account,
            })
            return HttpResponse(template.render(context))
    except Exception, e:
        print e
        return HttpResponse('')
    else:
        pass
    finally:
        pass