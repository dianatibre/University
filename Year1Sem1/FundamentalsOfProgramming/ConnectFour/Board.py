from Validations import InputError

class GameEnded(Exception):
    pass

class Board:
    
    def __init__(self):
        self.__board=[['_' for column in range(7)] for line in range(6)]
        self.__free_space=[5]*7
        self.__players={'user':'X',
                        'computer':'0'}
        self.__won=False
        self.__winner=None

    def get_won(self):
        return self.__won

    def get_winner(self):
        return self.__winner
    
    def set_won(self,value):
        self.__won=value
        
    def set_winner(self,value):
        self.__winner=value

    def set_board(self,line,column,value):
        self.__board[line][column]=value

    def get_board(self):
        return self.__board

    def get_free_space(self):
        return self.__free_space

    def get_free_space_column(self,column):
        return self.__free_space[column]
    
    def consecutive3_column(self):
        #verifies if there exists 3 pieces consecutive on a column and if the next free space after them is available
        #output: None if there weren't 3 consecutive on a column
        #        column_for_return = column number where the computer should place its piece in order to block the user
        column_for_return=None
        for column in range(7):
            for line in range(4):
                if self.__board[line][column]==self.__board[line+1][column] and self.__board[line+1][column]==self.__board[line+2][column] and self.__board[line][column]!='_':
                    if self.get_free_space_column(column)==line-1:
                        column_for_return = column
                        return column_for_return
        if column_for_return==None:
            return column_for_return
                
    def consecutive2_line(self):
        #verifies if there exists 2 pieces consecutive on a line and if the next free space after them of the one before them is available
        #output: None if there weren't 2 consecutive on a line
        #        column_for_return = column number where the computer should place its piece in order to block the user
        column_for_return=None
        for line in range(6):
            for column in range(6):
                if self.__board[line][column]==self.__board[line][column+1] and self.__board[line][column+1]!='_':
                    if column==0 and (self.get_free_space_column(column+2)==line):
                        column_for_return=column+2
                        return column_for_return 
                    elif (column!=0) and (self.get_free_space_column(column-1)==line):
                        column_for_return = column-1
                        return column_for_return
                    elif (column==5) and (self.get_free_space_column(column-1)==line):
                        column_for_return = column+3
                        return column_for_return
                    elif (column!=5) and self.get_free_space_column(column+2)==line:
                        column_for_return=column+2
                        return column_for_return
        if column_for_return==None:
            return column_for_return
        
    def consecutive2_collumn(self):
        #verifies if there exists 3 pieces consecutive on a column and if the next free space after them is available
        #output: None if there weren't 2 consecutive on a column
        #        column_for_return = column number where the computer should place its piece in order to block the user
        column_for_return=None
        for column in range(7):
            for line in range(5):
                if self.__board[line][column]==self.__board[line+1][column] and self.__board[line][column]!='_':
                    if self.get_free_space_column(column)==line-1:
                        column_for_return = column
                        return column_for_return
        if column_for_return==None:
            return column_for_return
        
    def possible_to_win_on_collumn(self,column):
        #verifies if there exists the possibility for the computer to win (if there isn't space for 4 pieces on that column, it is not possible)
        #output: True if it is possible to win on that column
        #        False if it is not possible to win on that column
        if self.get_free_space_column(column)>=0:
            number_of_x=0
            for line in range(6):
                if self.__board[line][column]=='X':
                    number_of_x+=1
            if number_of_x>=4:
                return False
            else:
                return True
            
    def consecutive3_diagonal1(self):
        #verifies if there exists 3 pieces consecutive on the first diagonal and if the next free space after them or the one before them is available
        #output: None if there weren't 3 consecutive on the first diagonal
        #        column_for_return = column number where the computer should place its piece in order to block the user
        column_for_return=None
        for line in range(3):
            for column in range(4):
                if self.__board[line+1][column+1]=='X':
                    if self.__board[line+1][column+1]==self.__board[line+2][column+2]:
                        if self.__board[line+2][column+2]==self.__board[line+3][column+3]:
                            if self.get_free_space_column(column)==line:
                                column_for_return=column
                                return column_for_return
        for line in range(3):
            for column in range(4):
                if self.__board[line][column]=='X':
                    if self.__board[line][column]==self.__board[line+1][column+1]:
                        if self.__board[line+1][column+1]==self.__board[line+2][column+2]:
                            if self.get_free_space_column(column+3)==line+3:
                                column_for_return=column+3
                                return column_for_return
        if column_for_return==None:
            return column_for_return
        
    def consecutive3_diagonal2(self):
        #verifies if there exists 3 pieces consecutive on the second diagonal and if the next free space after them or the one before them is available
        #output: None if there weren't 3 consecutive on the second diagonal
        #        column_for_return = column number where the computer should place its piece in order to block the user
        column_for_return=None
        for line in range(3):
            for column in range(3,7):
                if self.__board[line+1][column-1]=='X':
                    if self.__board[line+1][column-1]==self.__board[line+2][column-2]:
                        if self.__board[line+2][column-2]==self.__board[line+3][column-3]:
                            if self.get_free_space_column(column)==line:
                                column_for_return=column
                                return column_for_return
        for line in range(3):
            for column in range(3,7):
                if self.__board[line][column]=='X':
                    if self.__board[line][column]==self.__board[line+1][column-1]:
                        if self.__board[line+1][column-1]==self.__board[line+2][column-2]:
                            if self.get_free_space_column(column-3)==line+3:
                                column_for_return=column-3
                                return column_for_return
        if column_for_return==None:
            return column_for_return
    
    def choose_column(self):
        #chooses the column where the computer should place its piece
        #the column is chosen to block the user if possible
        #output: column - the place where the user is blocked to win
        column=0
        while self.possible_to_win_on_collumn(column)==False:
            column=column+1
        while self.get_free_space_column(column)==-1:
            column=column+1
        if self.consecutive3_column()!=None:
            return self.consecutive3_column()
        elif self.consecutive3_diagonal1()!=None:
            return self.consecutive3_diagonal1()
        elif self.consecutive3_diagonal2()!=None:
            return self.consecutive3_diagonal2()
        elif self.consecutive2_line()!=None:
            return self.consecutive2_line()
        elif self.consecutive2_collumn()!=None:
            return self.consecutive2_collumn()
        else:
            return column
        
    
    def if_won(self):
        #verifies is the game ends
        #it does verifications on lines, columns and the two possible diagonals
        #if all places are filled and none of the players won, it will be a draw
        for line in range(3):
            for column in range(7):
                if self.__board[line][column]!='_':
                    if self.__board[line][column]==self.__board[line+1][column] and self.__board[line+1][column]==self.__board[line+2][column] and self.__board[line+2][column]==self.__board[line+3][column]:
                        winner=self.__board[line][column]
                        self.set_won(True)
                        self.set_winner(winner)
                        return
        for column in range(4):
            for line in range(6):
                if self.__board[line][column]!='_':
                    if self.__board[line][column]==self.__board[line][column+1] and self.__board[line][column+1]==self.__board[line][column+2] and self.__board[line][column+2]==self.__board[line][column+3]:
                        winner=self.__board[line][column]
                        self.set_won(True)
                        self.set_winner(winner)
                        return
        for line in range(3):
            for column in range(4):
                if self.__board[line][column]!='_':
                    if self.__board[line][column]==self.__board[line+1][column+1] and self.__board[line+1][column+1]==self.__board[line+2][column+2]:
                        if self.__board[line+2][column+2]==self.__board[line+3][column+3]:
                            winner=self.__board[line][column]
                            self.set_won(True)
                            self.set_winner(winner)
                            return
        for line in range(4,6):
            for column in range(4):
                if self.__board[line][column]!='_':
                    if self.__board[line][column]==self.__board[line-1][column+1]:
                        if self.__board[line-1][column+1]==self.__board[line-2][column+2]:
                            if self.__board[line-2][column+2]==self.__board[line-3][column+3]:
                                winner=self.__board[line][column]
                                self.set_won(True)
                                self.set_winner(winner)
                                return
                                
        '''
        for line in range(3):
            for column in range(3,6):
                if self.__board[line][column]!='_':
                    if self.__board[line][column]==self.__board[line+1][column-1] and self.__board[line+1][column-1]==self.__board[line+2][column-2]:
                        if self.__board[line+2][column-2]==self.__board[line+3][column-3]:
                            winner=self.__board[line][column]
                            self.set_won(True)
                            self.set_winner(winner)
                            return
        '''
        draw=True
        for line in range(6):
            for column in range(7):
                if self.__board[line][column]=='_':
                    draw=False
                    return
        if draw==True:
            raise GameEnded('It is a draw!')
                
        #return
                  
    
    def piece(self,column,player):
        #places a piece, specific to the player
        #verifies if the game ended after every piece placed
        #input:     column - the column where the piece should be placed, if the player is the user or '' if the player is the computer
        #             player - which player's round is ('user'/'computer')
        #output :    places the piece
        #            shows the winner or 'draw' if the game ended 
        if self.__won==False:
            if player=='user':
                if int(self.get_free_space_column(column))>=0:
                    if self.__board[self.get_free_space_column(column)][column]=='_':
                        self.set_board(self.get_free_space_column(column), column, self.__players[player])
                        #self.__board[self.get_free_space_column(column)][column]=self.__players[player]
                        self.__free_space[column]=self.__free_space[column]-1
                else:
                    raise InputError('Column is full!')
            elif player=='computer':
                while True:
                    column=self.choose_column()
                    if self.__board[self.get_free_space_column(column)][column]=='_':
                        self.set_board(self.get_free_space_column(column), column, self.__players[player])
                        #self.__board[self.get_free_space_column(column)][column]=self.__players[player]
                        self.__free_space[column]=self.__free_space[column]-1
                        self.if_won()
                        return
            self.if_won()
        elif self.__won==True:
            #print(self.__winner+' won!')
            raise GameEnded(self.__winner+' won!')
            return
        '''
        if self.winning()!=None:
            self.set_won(True)
            self.__winner=self.winning()
        '''
