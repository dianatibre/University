class Console:
    
    def __init__(self,service):
        self.__service=service
    
    '''
    def get_user(self):
        return self.__user

    def get_computer(self):
        return self.__computer
    '''
        
    def print_boards(self):
        self.__service.print_boards()
        '''
        print("Player's board")
        self.__user.draw()
        print("Computer's board")
        self.__computer.draw()
        '''
        
    def place_ship(self,parameters):
        ship=parameters[0].upper()
        self.__service.place_ship(ship)
        
    def attack(self,parameters):
        pass
    
        
    def run(self):
        self.print_boards()
        commands={'ship':self.place_ship,
                  'attack':self.attack
                  }
        while True: 
            command=input('>>>')
            parts=command.split(' ')
            if parts[0] in commands:
                parameters=parts[1:]
                try:
                    commands[str(parts[0])](parameters)
                    self.print_boards()
                except:
                    pass
            
        
        
