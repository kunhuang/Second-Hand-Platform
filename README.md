#校园自助跳蚤平台

##Server目录下为服务端代码

###json_api

*error_type:
0->未知错误
-1->身份验证失败（账号名或密码错误） 
-2->账号已存在（邮箱重复）
-3->找不到goods
-4->没有权限操作的goods（如别人的goods，只能在未上架的情况下修改价格）
-5->已经加入心愿单
-6->不在心愿单中
-7->账户ID不存在
-8->站内信不存在

*注册账号
URL: POST /json_api/add_account
DATA:
    name,
    email, 
    password, 
    (phone), 
RESPONSE:
    成功:{"success": 1, }
    失败:{"success": 0, "error_type": -1}

*登录账号（根据email和password返回id）
URL: POST /json_api/get_account_id
DATA:
    email,
    pasword,
RESPONSE:
    成功:{"success": 1, "id": }
    失败:{"success": 0, "error_type": -1}

*返回账户信息
URL: POST /json_api/get_account_info
DATA:
    account_id,
    password
RESPONSE
    成功:{"success": 1, }
    失败:{"success": 0, "error_type": "-1"}

*修改账户信息
URL: POST /json_api/edit_account_info
DATA:
    account_id = 
    password = 
    (name) = 
    (new_password) = 
    (phone) = 
RESPONSE
    成功:{"success": 1, }
    失败:{"success": 0, "error_type": "-1"}

*添加商品
URL: POST /json_api/add_goods
DATA:
    seller_id,
    password,
    name,
    (description),
    pure_price,
    (photo),
RESPONSE:

*返回单个商品信息
URL: POST /json_api/get_goods_info
DATA:
    goods_id,
RESPONSE:

*查看商品（兼容seller端和buyer端，返回多个商品信息，对于seller，相当于返回“我的物品”）
URL: POST /json_api/get_my_goods_array
DATA:
    account_id,
    password,
    account_type,(0 for seller, 1 for buyer)
    (goods_num),
    (option),
RESPONSE:

*首页商品
URL: POST /json_api/get_goods_array
DATA:
    (goods_num),
RESPONSE:

*修改商品信息
URL: POST /json_api/edit_goods_info
DATA:
    seller_id,
    password,
    goods_id,
    (description),
    (pure_price),
    (photo),
RESPONSE:

*改变商品状态（包括上架，下架，交易）
URL: POST /json_api/transact_goods
DATA:
    account_id,
    password,
    account_type, (0 for seller, 1 for buyer)
    goods_id,
    type,('O' for on_sale, 'B' for bought, 'C' for close)
RESPONSE:

*查看、搜索交易记录
URL: POST /json_api/get_transaction_array
DATA:
    account_id,
    password,
    account_type, (0 for seller, 1 for buyer)
    (option),
RESPONSE:

*评论商品
URL: POST /json_api/add_comment
DATA:
    account_id,
    password,
    goods_id,
    content,
RESPONSE:

*查看商品评论
URL: POST /json_api/get_comment_array
DATA:
    goods_id,
    (option)
RESPONSE:

*将商品加入心愿单
URL: POST /json_api/add_wishlist
DATA:
    buyer_id,
    password,
    goods_id,
    (option)
RESPONSE:

*返回心愿单信息
URL: POST /json_api/get_wishlist
DATA:
    buyer_id,
    password,
    (option)
RESPONSE:

*删除心愿单信息
URL: POST /json_api/delete_wishlist
DATA:
    buyer_id,
    password,
    goods_id,
    (option)
RESPONSE:

*发送站内信
URL: POST /json_api/send_message
DATA:
    account_id,
    password,
    recv_account_id,
    subject,
    content,
    (option)
RESPONSE:

*查看站内信（返回多个站内信）
URL: POST /json_api/get_message_array
DATA:
    account_id,
    password,
    (option)
RESPONSE:

*查看单个站内信（返回单个站内信内容）
URL: POST /json_api/get_message
DATA:
    account_id,
    password,
    message_id,
    (option)
RESPONSE:

##Andriod_Client目录下为Andriod客户端代码