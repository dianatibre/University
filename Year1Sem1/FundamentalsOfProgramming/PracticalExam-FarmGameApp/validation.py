class ValidationError(Exception):
    pass

class Validation:
    
    def land_buy(self,acres,land_price,stocks):
        if acres*land_price>stocks:
            raise ValidationError('Not enough grain to buy acres!')
        
    def land_sell(self,acres,land):
        if acres<0:
            if acres+land<0:
                raise ValidationError('Not enough land to sell!')
            
    def plant_grain(self,grain,stocks,units_to_feed):
        if grain+units_to_feed>stocks:
            raise ValidationError('Not enough grain!')
        
    def plant_acres(self,acres_plant,acres_owned):
        if acres_plant>acres_owned:
            raise ValidationError('Not enough acres!')
        
    
        
        
    
        
        