# coding=utf-8
from django.shortcuts import render
from django.http import HttpResponse

from json_api.models import Account_Info, Goods_Info, Log_Info, Message_Info, Comment_Info, Wish_List

import json
import time
from django.forms.models import model_to_dict

import helper

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
        email = request.POST['email']
        password = request.POST['password']
        phone = request.POST.get('phone')
        r = Account_Info.add_account(
            name = name,
            email = email,
            password = password,
            phone = phone,
            )
        print r
        if r == True:
            return HttpResponse(json.dumps(success))
        else:
            error['error_type'] = r
            return HttpResponse(json.dumps(error))
    except Exception, e:
        print e
        return HttpResponse(json.dumps(error))

def get_account_id(request):
    try:
        email = request.POST['email']
        password = request.POST['password']

        account_id = Account_Info.validate_email(email = email, password = password)
        if account_id == False:
            error['error_type'] = -1
            return HttpResponse(json.dumps(error))
        else:
            success['id'] = account_id
            return HttpResponse(json.dumps(success))
    except:
        return HttpResponse(json.dumps(error))

def get_account_info(request):
    try:
        email = request.POST['email']
        password = request.POST['password']

        account_id = Account_Info.validate_email(email = email, password = password)
        if account_id == False:
            error['error_type'] = -1
            return HttpResponse(json.dumps(error))
        else:
            account_info_array = Account_Info.objects.filter(id = account_id)
            return HttpResponse(getSuccessJson(account_info_array))
    except Exception, e:
        print e
        return HttpResponse(json.dumps(error))

def edit_account_info(request):
    try:
        email = request.POST['email']
        password = request.POST['password']
        
        phone = request.POST.get('phone')
        new_password = request.POST.get('new_password')
        name = request.POST.get('name')

        account_id = Account_Info.validate_email(email = email, password = password)
        if account_id == False:
            error['error_type'] = -1
            return HttpResponse(json.dumps(error))
        else:
            account_info = Account_Info.objects.get(id = account_id)
            if phone != None:
                account_info.phone = phone
            if new_password != None:
                account_info.password = new_password
            if name != None:
                account_info.name = name
            account_info.save()
            return HttpResponse(json.dumps(success))
    except Exception, e:
        print e
        return HttpResponse(json.dumps(error))

def add_goods(request):
    try:
        seller_id =  request.POST['seller_id']
        password = request.POST['password']
        name = request.POST['name']
        pure_price = request.POST['pure_price']

        description = request.POST.get('description')
        if description == None:
            description = ""
        #photo

        if Account_Info.validate_id(id = seller_id, password = password) == False:
            error['error_type'] = -1
            return HttpResponse(json.dumps(error))

        goods = Goods_Info.objects.create(
            name = name,
            description = description,
            seller_id = seller_id,
            state = 'I',
            pure_price = pure_price,
            buyer_id = 0,
            )
        
        log = Log_Info.objects.create(
            goods_id = goods.id,
            time = int(time.time()),
            type = 'I',
            )
        
        success['goods_id'] = goods.id
        
        return HttpResponse(json.dumps(success))

    except Exception, e:
        print e
        return HttpResponse(json.dumps(error))
    else:
        pass
    finally:
        pass

def get_goods_info(request):
    try:
        seller_id =  request.POST['seller_id']
        password = request.POST['password']
        goods_id = request.POST['goods_id']

        if Account_Info.validate_id(id = seller_id, password = password) == False:
            error['error_type'] = -1
            return HttpResponse(json.dumps(error))

        goods_array = Goods_Info.objects.filter(id = goods_id)
        return HttpResponse(getSuccessJson(goods_array))

    except Exception, e:
        print e
        return HttpResponse(json.dumps(error))
    else:
        pass
    finally:
        pass

def get_goods_array(request):
    try:
        seller_id =  request.POST['seller_id']
        password = request.POST['password']
        
        if Account_Info.validate_id(id = seller_id, password = password) == False:
            error['error_type'] = -1
            return HttpResponse(json.dumps(error))

        goods_array = Goods_Info.objects.filter(seller_id = seller_id)
        return HttpResponse(getSuccessJson(goods_array))
    except Exception, e:
        print e
        return HttpResponse(error)
    else:
        pass
    finally:
        pass

def edit_goods_info(request):
    try:
        seller_id =  request.POST['seller_id']
        password = request.POST['password']
        goods_id = request.POST['goods_id']

        description = request.POST.get('description')
        pure_price = request.POST.get('pure_price')
        #photo

        if Account_Info.validate_id(id = seller_id, password = password) == False:
            error['error_type'] = -1
            return HttpResponse(json.dumps(error))

        goods = Goods_Info.objects.filter(id = goods_id, seller_id = seller_id)
        if goods.exists() == False:
            error['error_type'] = -4
            return HttpResponse(json.dumps(error))

        if pure_price != None and goods.state != 'I':
            error['error_type'] = -4
            return HttpResponse(json.dumps(error))

        if pure_price != None:
            goods.pure_price = pure_price
        if description != None:
            goods.description = description
        goods.save()

        log = Goods_Info.objects.create(
            goods_id = goods.id,
            time = int(time.time()),
            type = 'U'
            )
        return HttpResponse(json.dumps(success))
    except Exception, e:
        print e
        return HttpResponse(json.dumps(error_type))
    else:
        pass
    finally:
        pass

def transact_goods(request):
    try:
        account_id =  request.POST['account_id']
        password = request.POST['password']
        account_type = request.POST['account_type']
        goods_id = request.POST['goods_id']
        type = request.POST['type']

        if Account_Info.validate_id(id = account_id, password = password) == False:
            error['error_type'] = -1
            return HttpResponse(json.dumps(error))

        if account_type == 0:#seller
            goods = Goods_Info.objects.filter(id = goods_id, seller_id = seller_id)
        
            if goods.exists() == False:
                error['error_type'] = -4
                return HttpResponse(json.dumps(error))

            if (type == 'O' or type == 'C') and goods.type == 'I':
                goods.type = type
                log = Log_Info.objects.create(
                    goods_id = goods.id,
                    time = int(time.time()),
                    type = type,
                    )
                return HttpResponse(json.dumps(success))
            error['error_type'] = -4
            return HttpResponse(json.dumps(error))
        elif account_type == 1:#buyer
            goods = Goods_Info.objects.filter(seller_id = seller_id)
        
            if goods.exists() == False:
                error['error_type'] = -3
                return HttpResponse(json.dumps(error))

            if (type == 'B') and goods.type == 'O':
                goods.type = type
                log = Log_Info.objects.create(
                    goods_id = goods.id,
                    time = int(time.time()),
                    type = type,
                    )
                goods.buyer_id = account_id
                goods.save()
                return HttpResponse(json.dumps(success))
            error['error_type'] = -4
            return HttpResponse(json.dumps(error))
        else:
            error['error_type'] = -4
            return HttpResponse(json.dumps(error)) 
    except Exception, e:
        print e
        return HttpResponse(json.dumps(error))
    else:
        pass
    finally:
        pass
'''
def get_transaction_array(request):
    try:
        account_id =  request.POST['account_id']
        password = request.POST['password']
        account_type = request.POST['account_type']

        if Account_Info.validate_id(id = account_id, password = password) == False:
            error['error_type'] = -1
            return HttpResponse(json.dumps(error))

        log = Log_Info.objects.filter()
        #TODO
    except Exception, e:
        print e
        return HttpResponse(json.dumps(error))
    else:
        pass
    finally:
        pass
'''

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
        email = request.POST['email']
        password = request.POST['password']
        return HttpResponse(Account_Info.validate_email(email = email, password = password))
    except:
        return HttpResponse(False)