from errors import InputError
import random


class Square:
    
    def __init__(self):
        self.dictionary={'ship':'#',
                           'free':'•',
                           'hit':'X',
                           'miss':'0'}
        self.already_attacked=False
        self.setting='•'

    def __str__(self):
        return str(self.setting)
    
    def get_setting(self):
        return str(self.setting)
        
    def set_ship(self):
        self.setting=self.dictionary['ship']
        
    def set_hit(self):
        self.setting=self.dictionary['hit']
        
    def set_miss(self):
        self.setting=self.dictionary['miss']
        
    def set_already_attacked(self):
        self.already_attacked=True
        
    def attacked(self):
        if self.already_attacked:
            raise InputError('Square already attacked!')
        if self.setting==self.dictionary['ship']:
            self.set_hit()
            self.set_already_attacked()
            
        elif self.setting==self.dictionary['free']:
            self.set_miss()
            self.set_already_attacked()
            



class Board(Square):
    
    columns={'A':0,
             'B':1,
             'C':2,
             'D':3,
             'E':4,
             'F':5}
    
    def __init__(self,player):
        Square.__init__(self)
        self.board=[[Square() for line in range(6)] for column in range(6)]
        self.__player=player
        
    def __iter__(self):
        return self
    
    def get_board(self):
        return self.board
    
    def draw(self):
        if self.__player=='u':
            for line in self.board:
                for square in line:
                    print(square, end=' ')
                print()
            print()
        elif self.__player=='c':
            for line in self.board:
                for square in line:
                    if square.get_setting()=='#':
                        print('•', end=' ')
                    else:
                        print(square, end=' ')
                print()
            print()
            
    
    
    def get_square(self,line,column):
        return self.get_board()[line][column]
    
    def place_ship(self,ship):
        point1=ship[:2]
        point2=ship[2:4]
        point3=ship[4:]
        points=[point1,point2,point3]
        for point in points:
            self.board[int(self.columns[str(point[0])])][int(point[1])-1].set_ship()
            print (self.board[self.columns[str(point[0])]][int(point[1])-1])
            
    def random_ships(self,ship):
        position=random.choice(['l','c'])
        if position=='l':
            line=random.choice(range(6))
            columnstart=random.choice(range(4))
            ship=str(line)+str(columnstart)+str(line)+str(columnstart+1)+str(line)+str(columnstart+2)
            self.place_ship(ship)
        
        
        
        
        
        
        
        
