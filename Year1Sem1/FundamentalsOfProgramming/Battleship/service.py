class Service:
    
    def __init__(self,user,computer,validations):
        self.__user = user
        self.__computer = computer
        self.__validations= validations

    def get_user(self):
        return self.__user

    def get_computer(self):
        return self.__computer
    
    def place_ship(self,ship):
        self.__validations.ship_validation(ship)
        self.__user.place_ship(ship)
        
        
    def print_boards(self):
        print("Player's board")
        self.__user.draw()
        print("Computer's board")
        self.__computer.draw()
    
