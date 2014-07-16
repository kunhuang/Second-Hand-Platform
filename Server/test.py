# coding=utf-8
import json
import requests

url_prefix = "http://10.180.26.157:8000/"

add_account_url = "json_api/add_account/"
add_account_data = {
    "name": u"小黄",
    "email": u"21kunhuang110@gmail.com",
    "password": "123",
    "phone": "1234",
}

get_account_id_url = "json_api/get_account_id/"
get_account_id_data = {
    "email": u"kunhuang110@gmail.com",
    "password": "123",
}

get_account_info_url = "json_api/get_account_info/"
get_account_info_data = {
    "account_id": 8,
    "password": "t",
}

edit_account_info_url = "json_api/edit_account_info/"
edit_account_info_data = {
    "account_id": 1,
    "password": "123",
    "name": u"小黄",
    "phone": "1111"
}

add_goods_url = "json_api/add_goods/"
add_goods_data = {
    "seller_id": 1,
    "password": "123",
    "name": u"显示屏",
    "description": u"2013年",
}

get_transaction_array_url = "json_api/get_transaction_array/"
get_transaction_array_data = {
    "account_id": 8,
    "password": "t",
    #"account_type": 0,
    #"photo"
}

get_goods_array_url = "json_api/get_goods_array/"
get_goods_array_data = {
    "account_id": 2,
    "password": "121212",
    "account_type": 1,
    "goods_num": 5,
}

u = "json_api/get_transaction_array/"
d = {
        "account_id": 1,
        "password": "123",
        "account_type": 0,
        "goods_id": 1,
        "type": 'O',
    }

#8修改商品信息
u8=edit_goods_info_url = "json_api/edit_goods_info/"
u82=edit_goods_info_data = {
    "seller_id": 8,
    "password": "123456",
    "goods_id":36,
    "description":u"expensive",
    "pure_price":135000000,
    #"photo"  
}

transact_goods_url = "json_api/transact_goods/"
transact_goods_data = {
    "account_id": 8,
    "password": "123",
    "account_type":"1",
    "goods_id":41,
    "type":"B",
    #"photo"  
}

r = requests.post(url_prefix + transact_goods_url, transact_goods_data)
print(r.content)