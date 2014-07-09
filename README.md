#校园自助跳蚤平台

##Server目录下为服务端代码

###json_api

*error_type:
0->未知错误
-1->身份验证失败（账号名或密码错误） 
-2->账号已存在（邮箱重复）

*注册账号
URL: POST /json_api/add_account
DATA:
    name,
    email, 
    password, 
    (phone), 
RESPONSE:
    成功:{"success": 1}
    失败:{"success": 0, "error_type": "abc"}

*返回账户信息
URL: POST /json_api/get_account_info
DATA:
    email,
    password
RESPONSE
    成功:{"success": 1, }
    失败:{"success": 0, "error_type": "abc"}

*修改账户信息
URL: /json_api/edit_account
METHOD: POST
DATA:
    email = 
    password = 
    (name) = 
    (new_password) = 
    (phone) = 

*添加商品
*返回单个商品信息
*查看搜索商品（返回多个商品信息）
*修改商品信息
*改变商品状态（包括上架，下架，交易）

*查看、搜索交易记录

*评论商品
*查看商品评论

*将商品加入心愿单
*返回心愿单信息

*发送站内信
*查看站内信（返回多个站内信）
*查看单个站内信（返回单个站内信内容）

##Andriod_Client目录下为Andriod客户端代码