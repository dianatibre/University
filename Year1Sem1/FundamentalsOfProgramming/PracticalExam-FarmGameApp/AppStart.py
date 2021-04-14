from Console import Console
from service import Service
from validation import Validation

validation=Validation()
service=Service(validation)
console=Console(service)


console.run()