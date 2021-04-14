from Validations import InputError
from Board import GameEnded
class Console:
    
    
    def __init__(self, service):
        self.__service = service
        
    def get_board(self):
        return self.__service.get_board()
    
    def print_board(self):
        for line in range(6):
            for column in range(7):
                print(self.get_board()[line][column], end=' ')
            print()
        print()
            
    def read_numerical(self,text,numericaltype):
        number=input(text)
        while True:
            try:
                number=numericaltype(number)
                return number
            except ValueError:
                number=input('Invalid numerical type! Try again:')
            
    def piece(self):
        column=self.read_numerical('Enter column:',int)
        self.__service.piece(column,'user')
        
    def computer_turn(self):
        self.__service.piece('','computer')
            
        
    def run(self):
        self.print_board()
        while self.__service.get_won()==False:
            try:
                self.piece()
                self.print_board()
                if self.__service.get_won()==False:
                    self.computer_turn()
                    self.print_board()
            except GameEnded as gameended:
                print(str(gameended))
            except InputError as inputerror:
                print (inputerror)
            except ValueError as valuerror:
                print(valuerror)
            #except Exception as exception:
                #print(exception)
        print(self.__service.get_winner()+' won!')
                
            
        
        
    
    



