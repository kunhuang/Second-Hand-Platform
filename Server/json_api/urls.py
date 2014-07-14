from django.conf.urls import patterns, url

from json_api import views

urlpatterns = patterns('',
	# ex: /json_api/
    url(r'^$', views.index, name='index'),
    url(r'^test/$', views.test, name='test'),
    url(r'^test2/$', views.test2, name='test2'),
    
    url(r'^add_account?/$', views.add_account, name='add_account'),
    url(r'^get_account_id?/$', views.get_account_id, name='get_account_id'),
    url(r'^get_account_info?/$', views.get_account_info, name='get_account_info'),
    url(r'^edit_account_info?/$', views.edit_account_info, name='edit_account_info'),

    url(r'^add_goods?/$', views.add_goods, name='add_goods'),
    url(r'^get_goods_info?/$', views.get_goods_info, name='get_goods_info'),
    url(r'^get_goods_array?/$', views.get_goods_array, name='get_goods_array'),
    url(r'^get_my_goods_array?/$', views.get_my_goods_array, name='get_my_goods_array'),
    url(r'^edit_goods_info?/$', views.edit_goods_info, name='edit_goods_info'),
    
    
    url(r'^transact_goods_info?/$', views.transact_goods, name='transact_goods'),
    url(r'^get_transaction_array?/$', views.get_transaction_array, name='get_transaction_array'),
    
    
    url(r'^add_comment?/$', views.add_comment, name='add_comment'),
    url(r'^get_comment_array?/$', views.get_comment_array, name='get_comment_array'),
    
    
    url(r'^add_wishlist?/$', views.add_wishlist, name='add_wishlist'),
    url(r'^get_wishlist?/$', views.get_wishlist, name='get_wishlist'),
    url(r'^delete_wishlist?/$', views.delete_wishlist, name='delete_wishlist'),
    
    url(r'^send_message?/$', views.send_message, name='send_message'),
    url(r'^get_message_array?/$', views.get_message_array, name='get_message_array'),
    url(r'^get_message?/$', views.get_message, name='get_message'),
    
    #url(r'^(?P<poll_id>\d+)/$', views.detail, name='detail'),
    #url(r'/test/$', views.results, name='results'),
    
)