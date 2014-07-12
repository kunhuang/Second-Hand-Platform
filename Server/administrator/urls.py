from django.conf.urls import patterns, url

from administrator import views

urlpatterns = patterns('',
    # ex: /json_api/
    url(r'^$', views.index, name='index'),
    
    #url(r'^(?P<poll_id>\d+)/$', views.detail, name='detail'),
    #url(r'/test/$', views.results, name='results'),
    
)