from django.conf.urls import patterns, url

from json_api import views

urlpatterns = patterns('',
	# ex: /json_api/
    url(r'^$', views.index, name='index'),
    url(r'^test/$', views.test, name='test'),
    url(r'^test2/$', views.test2, name='test2'),
    
    url(r'^add_account/$', views.add_account, name='add_account'),

    #url(r'^(?P<poll_id>\d+)/$', views.detail, name='detail'),
    #url(r'/test/$', views.results, name='results'),
    
)