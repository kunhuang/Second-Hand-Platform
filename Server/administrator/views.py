from django.shortcuts import render
from django.http import HttpResponse

from administrator.models import *

import json
import time
from django.forms.models import model_to_dict
from django.db.models import Q

# Create your views here.
def index(request):
    return HttpResponse("hello  world")