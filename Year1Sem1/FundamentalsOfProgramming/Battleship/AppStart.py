from Board import Board,Square
from Console import Console
from service import Service
from validations import Validations

user=Board('u')
computer=Board('c')


validations=Validations()
service=Service(user,computer,validations)
console=Console(service)

'''
mysquare=Square()
print(mysquare)
'''

console.run()

