from Console import Console
from Board import Board
from Validations import Validations
from Service import Service

board=Board()
validations=Validations()
service=Service(board,validations)
console=Console(service)

console.run()
