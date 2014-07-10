from django.conf.urls import patterns, url

from json_api import views

urlpatterns = patterns('',
	# ex: /json_api/
    url(r'^$', views.index, name='index'),
    url(r'^test/$', views.test, name='test'),
    url(r'^test2/$', views.test2, name='test2'),
    
    url(r'^add_account/$', views.add_account, name='add_account'),
    url(r'^get_account_id/$', views.get_account_id, name='get_account_id'),
    url(r'^get_account_info/$', views.get_account_info, name='get_account_info'),
    url(r'^edit_account_info/$', views.edit_account_info, name='edit_account_info'),

    url(r'^add_goods/$', views.add_goods, name='add_goods'),

    #url(r'^(?P<poll_id>\d+)/$', views.detail, name='detail'),
    #url(r'/test/$', views.results, name='results'),
    
)