from django.conf.urls import patterns, include, url

from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'Server.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),
    url(r'^json_api/', include('json_api.urls')),
    url(r'^administrator/', include('administrator.urls')),
    url(r'^admin/', include(admin.site.urls)),
)
