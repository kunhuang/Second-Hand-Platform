from django.conf.urls import patterns, url

from json_api import views

urlpatterns = patterns('',
	# ex: /json_api/
    url(r'^$', views.index, name='index'),
)