# coding=utf-8
from django.shortcuts import render
from django.http import HttpResponse

from json_api.models import Account_Info, Goods_Info, Log_Info, Message_Info, Comment_Info, Wish_List

import json
from django.forms.models import model_to_dict

error = {
    'success': 0,
    'error_type': 0, #Unknown Error
}
success = {
    'success': 1,
}

def getSuccessJson(models):
    data = {
              'total':len(models),
              'rows': [model_to_dict(item) for item in models ]
              }
    success['data'] = data
    json_data = json.dumps(success)
    return json_data

def index(request):
	return HttpResponse('Hello World')

def add_account(request):
    try:
        name = request.POST['name']
        email = request.POST['password']
        password = request.POST['password']
        phone = reques.POST.get('phone')
        r = Account_Info.add_account(
            name = name,
            email = email,
            password = password,
            phone = phone,
            )
        if r == True:
            return HttpResponse(json.dumps(success))
        else:
            error['error_type'] = r
            return HttpResponse(json.dumps(error))

    except:
        return HttpResponse(json.dumps(error))

def get_account_info(request):
    try:
        email = request.POST['email']
        password = request.POST['password']

        account_id = Account_Info.validate(email = email, password = password)
        if account_id == False:
            error['error_type'] = 1
            return HttpResponse(json.dumps(error))
        else:
            account_info = Account_Info.objects.get(email = email)
            return HttpResponse(getSuccessJson(account_info))
    except:
        return HttpResponse(json.dumps(error))

def edit_account(request):
    try:
        email = request.POST['password']
        password = request.POST['password']
        
        phone = request.POST.get('phone')
        new_password = request.POST.get('new_password')
        name = request.POST.get('name')

        account_id = Account_Info.validate(email = email, password = password)
        if account_id == False:
            error['error_type'] = 1
            return HttpResponse(json.dumps(error))
        else:
            account_info = Account_Info.objects.get(id = account_id)
            if phone != None:
                account_info['phone'] = phone
            if new_password != None:
                account_info['new_password'] = new_password
            if name != None:
                account_info['name'] = name
            account_info.save()
    except:
        return HttpResponse(json.dumps(error))

def test(request):
    
    '''goods = Goods_Info.objects.create(
        name = '苹果手机',
        description = '半新品',
        seller_id = 1,
        state = 'I',
        pure_price = 8000,
        )
    '''
    return HttpResponse(json.dumps(success))
    return HttpResponse(getJson(goods_info))

def test2(request):
    try:
        email = request.GET['email']
        password = request.GET['password']
        return HttpResponse(Account_Info.validate(email = email, password = password))
    except:
        return HttpResponse(False)