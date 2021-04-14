class Service:
    
    
    def __init__(self, board, validations):
        self.__board = board
        self.__validations = validations
        
    def get_board(self):
        return self.__board.get_board()
    
    def get_won(self):
        return self.__board.get_won()
    
    def get_winner(self):
        return self.__board.get_winner()
    
    def piece(self,column,player):
        if self.get_won()==False:
            if column!='':
                self.__validations.ColumnValidation(column)
                column=column-1
            self.__board.piece(column,player)
        else:
            return
        
    
    
    



