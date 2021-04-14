from validation import ValidationError
from service import GameEnded

class Console:

    def __init__(self,service):
        self.__service = service
        
    def get_year(self):
        return self.__service.get_year()
    
    def read_integer(self,mytext):
        number=input(mytext)
        try:
            number=int(number)
            return number
        except ValueError:
            print('Invalid input! ')
            
    
    def read_data(self):
        acres_trade=None
        while acres_trade==None:
            acres_trade=self.read_integer('Acres to buy/sell (+/-) -> ')
        units_feed=None
        while units_feed==None:
            units_feed=self.read_integer('Units to feed the population -> ')
        acres_plant=None
        while acres_plant==None:
            acres_plant=self.read_integer('Acres to plant ->')
            
        self.__service.data_entered(acres_trade,units_feed,acres_plant)
        
    def end_game(self):
        message=self.__service.all_5_years_done()
        if message!='':
            print(message)
            return
        
    def run(self):
        print(self.__service)
        while self.get_year()<5:
            try:
                self.read_data()
                print(self.__service)
                self.end_game()
            except ValueError as error:
                print(error)
            except ValidationError as error:
                print(error)
                
            except GameEnded as reason:
                #print(self.__service)
                print('GAME OVER!')
                print(reason)
                return
                
        


