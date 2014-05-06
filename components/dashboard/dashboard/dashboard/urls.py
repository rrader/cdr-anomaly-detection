from django.conf.urls import patterns, include, url

# Uncomment the next two lines to enable the admin:
# from django.contrib import admin
# admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    url(r'^$', 'patterns.views.home', name='home'),
    url(r'^analytics/$', 'patterns.views.analytics', name='analytics'),
    url(r'^trends/$', 'patterns.views.trends', name='trends'),
    url(r'^patterns/', include('patterns.urls')),

    # Uncomment the admin/doc line below to enable admin documentation:
    # url(r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
    # url(r'^admin/', include(admin.site.urls)),
)
