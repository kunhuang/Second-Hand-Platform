from django.contrib import admin

from json_api.models import Account_Info, Goods_Info, Log_Info, Message_Info, Comment_Info, Wish_List

# Register your models here.
admin.site.register(Account_Info)
admin.site.register(Goods_Info)
admin.site.register(Log_Info)
admin.site.register(Message_Info)
admin.site.register(Comment_Info)
admin.site.register(Wish_List)

