from django.urls import path, re_path

from . import views

urlpatterns = [
    re_path(r'^(?P<task_id>[0-9a-z]{32})$', views.index, name='index'),
]