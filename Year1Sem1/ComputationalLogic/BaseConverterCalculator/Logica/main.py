'''
Tibre Diana Andreea, group 917
'''

from operations import Operations
from conversions import Conversions
from service import Service
from ui import UI
from tests import Tests
from validations import Validation

operations=Operations()
conversions=Conversions()
validations=Validation()
service=Service(operations,conversions,validations)
ui=UI(service)
tests=Tests()

tests.run()
ui.run()