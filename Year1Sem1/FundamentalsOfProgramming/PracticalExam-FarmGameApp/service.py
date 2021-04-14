import random


class Service:
    
    def __init__(self,validation):
        self.__validation=validation
        self.__year=1
        self.__people_starved=0
        self.__people_came=0
        self.__population=100
        self.__acres_owned=1000
        self.__harvest=3
        self.__rats_ate=200
        self.__land_price=20
        self.__grain_stocks=2800
        self.__acres_trade=0
        self.__units_feed=0
        self.__acres_plant=0

    def get_year(self):
        return self.__year


    def get_people_starved(self):
        return self.__people_starved


    def get_people_came(self):
        return self.__people_came


    def get_population(self):
        return self.__population


    def get_acres_owned(self):
        return self.__acres_owned


    def get_harvest(self):
        return self.__harvest


    def get_rats_ate(self):
        return self.__rats_ate


    def get_land_price(self):
        return self.__land_price


    def get_grain_stocks(self):
        return self.__grain_stocks


    def get_acres_trade(self):
        return self.__acres_trade


    def get_units_feed(self):
        return self.__units_feed


    def get_acres_plant(self):
        return self.__acres_plant


    def set_year(self, value):
        self.__year = value


    def set_people_starved(self, value):
        self.__people_starved = value


    def set_people_came(self, value):
        self.__people_came = value


    def set_population(self, value):
        self.__population = value


    def set_acres_owned(self, value):
        self.__acres_owned = value


    def set_harvest(self, value):
        self.__harvest = value


    def set_rats_ate(self, value):
        self.__rats_ate = value


    def set_land_price(self, value):
        self.__land_price = value


    def set_grain_stocks(self, value):
        self.__grain_stocks = value


    def set_acres_trade(self, value):
        self.__acres_trade = value


    def set_units_feed(self, value):
        self.__units_feed = value


    def set_acres_plant(self, value):
        self.__acres_plant = value

    def __str__(self):
        return ('\nIn year '+str(self.__year)+', '+str(self.__people_starved)+' people starved. \n'
                + str(self.__people_came)+' people came to the city. \n'
                + 'City population is '+str(self.__population)+'. \n'
                + 'City owns '+str(self.__acres_owned)+' acres of land. \n'
                + 'Harvest was '+str(self.__harvest)+' units per acre. \n' 
                + 'Rats ate '+str(self.__rats_ate)+' units. \n'
                + 'Land price is '+str(self.__land_price)+' units per acre. \n \n'
                + 'Grains stocks are '+ str(self.__grain_stocks)+' units. \n' )
        
    def all_5_years_done(self):
        '''
        verifies if 5 years passed
        evaluates the game
        '''
        message=''
        if self.__year==5:
            
            if self.__population>=100 and self.__acres_owned>=1000:
                message+='Congratulations! Your population is over 100 and you have over 1000 acres!'
            else:
                message+='You did not do well...'
            #raise GameEnded(message)
        return message
        
    def new_land_price(self):
        '''
        randomly sets the new land price (between 15 and 25)
        '''
        self.__land_price=random.choice(range(15,26))
        
    def new_harvest(self):
        '''
        randomly sets the new harvest (between 1 and 6)
        '''
        self.__harvest=random.choice(range(1,7))
    
    def new_land_trade(self):
        '''
        calculates the new number of acres owned after the trades were made
        if acres were bought or sold, the grain stock is updated
        
        '''
        self.__acres_owned+=self.__acres_trade
        if self.__acres_trade>0:
            self.__grain_stocks=self.__grain_stocks-self.__acres_trade*self.__land_price
        elif self.__acres_trade<0:
            self.__grain_stocks=self.__grain_stocks+self.__acres_trade*self.__land_price
            
    def new_grainstocks(self):
        '''
        Calculates the grainstocks using the data we have
        Output: grainstocks - new value of Grain Stocks
        '''
        grainstocks=int(self.__acres_owned)*int(self.__harvest)-int(self.__rats_ate)-int(self.__units_feed)
        self.set_grain_stocks(grainstocks)
        
    def grainstocks_after_harving(self):
        '''
        calculates the amount of grains harvested by the population
        one person can harvest at most 10 acres, so if there are more acres that can be harvested, only the maximum number of acres will be harvested
        '''
        if self.get_acres_plant()>self.__population*10:
            self.set_acres_plant(self.__population*10)
        harved_grainstocks=int(self.get_acres_plant())*int(self.__harvest)+self.__grain_stocks
        self.set_grain_stocks(harved_grainstocks)
        
        
    def new_people_starved(self):
        '''
        calculates how many people starved during the year
        Raises GameEnded if more than half the population starved and the game ends
        Else, sets new value for people starved
        '''
        previous_population=self.__population
        people_starved=self.__population-(self.__units_feed//20)
        if people_starved>=previous_population//2:
            raise GameEnded('Too many people starved!')
        if people_starved>=0:
            self.set_people_starved(people_starved)
        current_population=self.__population-people_starved
        self.set_population(current_population)
    
    def new_people_come(self):
        '''
        chooses random how many people came in the city, if no one starved in the previous year
        sets the new population
        '''
        if self.__people_starved==0:
            people_come=random.choice(range(11))
            self.set_people_came(people_come)
            new_population=self.__population+people_come
            self.set_population(new_population)
        else:
            self.set_people_came(0)
            
    def rat_infestation(self):
        '''
        chooses randomly if there was any rat infestation. if yes, it calculates randomly the percentage of grains they ate and how many grains
        '''
        probabilty=random.choice(range(5))
        if probabilty==0:
            percentage=random.choice(range(1,11))
            rats_ate=(self.__grain_stocks*percentage)//100
            self.set_rats_ate(rats_ate)
        else:
            self.set_rats_ate(0)
            
    def grainstocks_after_rat_infestation(self):
        '''
        calculates the grain stock after the rat infestation, if there was one
        sets new grain stock
        '''
        if self.__rats_ate>0:
            new_grainstock=self.__grain_stocks-self.__rats_ate
            self.set_grain_stocks(new_grainstock)
        
    def new_year(self):
        '''
        Does the operations for the new year
        '''
        self.__year+=1
        self.new_land_trade()
        self.new_grainstocks()
        self.new_people_starved()
        self.new_people_come()
        self.new_harvest()
        self.grainstocks_after_harving()
        self.rat_infestation()
        self.grainstocks_after_rat_infestation()
        self.new_land_price()
        #self.all_5_years_done()
        
                
    def data_entered(self,acres_trade,units_feed,acres_plant):
        '''
        Sets new data after being entered by the user
        Calls the function which makes the operations for the new year - new_year()
        '''
        self.__validation.land_buy(acres_trade,self.__land_price,self.__grain_stocks)
        self.__validation.land_sell(acres_trade,self.__acres_owned)
        #self.__validation.plant_grain(acres_plant,self.__grain_stocks,units_feed)
        self.__validation.plant_acres(acres_plant,self.get_acres_owned())
        self.set_acres_trade(acres_trade)
        self.set_units_feed(units_feed)
        self.set_acres_plant(acres_plant)
        self.new_year()
        

class GameEnded(Exception):
    pass
                