'''
Created on Jan 29, 2020

@author: dianatibre
'''
import unittest
from service import Service
from validation import Validation, ValidationError

class Test(unittest.TestCase):
    
    def setUp(self):
        unittest.TestCase.setUp(self)

    def test_NewGrainstock_YearOne_CorrectGrainstock(self):
        my_validation=Validation()
        my_service=Service(my_validation)
        self.assertEqual(my_service.get_grain_stocks(), 2800)
        
    def test_NewYear_YearOne_YearIncreasing(self):
        my_validation=Validation()
        my_service=Service(my_validation)
        self.assertEqual(my_service.get_year(),1)
        my_service.set_units_feed(2000)
        my_service.new_year()
        self.assertEqual(my_service.get_year(),2)
        
    def test_Validation_LandBuy_Error(self):
        my_validation=Validation()
        #my_service=Service(my_validation)
        try:
            my_validation.land_buy(100,10,900)
            assert(False)
        except ValidationError as myerror:
            self.assertEqual(str(myerror),'Not enough grain to buy acres!')
            
    def test_Validation_LandSell_Error(self):
        my_validation=Validation()
        #my_service=Service(my_validation)
        try:
            my_validation.land_sell(-1000,900)
            assert(False)
        except ValidationError as myerror:
            self.assertEqual(str(myerror),'Not enough land to sell!')
            
    def test_Validation_PlantAcres_Error(self):
        my_validation=Validation()
        #my_service=Service(my_validation)
        try:
            my_validation.plant_acres(1000,900)
            assert(False)
        except ValidationError as myerror:
            self.assertEqual(str(myerror),'Not enough acres!')
            
    def test_NewPeopleCome_PeopleStarved_NoPeopleCame(self):
        my_validation=Validation()
        my_service=Service(my_validation)
        my_service.set_people_starved(10)
        my_service.new_people_come()
        self.assertEqual(my_service.get_people_came(), 0)
        
    def test_GrainstocksAfterRatInfestation_RatInfestation_NewGrainstocks(self):
        my_validation=Validation()
        my_service=Service(my_validation)
        previous_grainstock=my_service.get_grain_stocks()
        my_service.set_rats_ate(100)
        my_service.grainstocks_after_rat_infestation()
        self.assertLess(my_service.get_grain_stocks(), previous_grainstock)
        
    def test_NewHarvest_Random_NumberInInterval(self):
        my_validation=Validation()
        my_service=Service(my_validation)
        my_service.new_harvest()
        self.assertIn(my_service.get_harvest(), range(1,7))
        
    def test_NewLandPrice_Random_NumberInInterval(self):
        my_validation=Validation()
        my_service=Service(my_validation)
        my_service.new_land_price()
        self.assertIn(my_service.get_land_price(), range(15,26))
        
    def test_All5Years_Message(self):
        my_validation=Validation()
        my_service=Service(my_validation)
        my_service.set_year(5)
        self.assertTrue(my_service.all_5_years_done()!='')
        
    def test_NewLandTrade_LandBought_CorrectAmount(self):
        my_validation=Validation()
        my_service=Service(my_validation)
        my_service.set_acres_owned(0)
        my_service.set_acres_trade(1)
        my_service.new_land_trade()
        self.assertEqual(my_service.get_acres_owned(), 1)
        
    def test_NewLandTrade_LandSold_CorrectAmount(self):
        my_validation=Validation()
        my_service=Service(my_validation)
        my_service.set_acres_owned(1)
        my_service.set_acres_trade(-1)
        my_service.new_land_trade()
        self.assertEqual(my_service.get_acres_owned(), 0)
        
    
    
        
        
        
        
        
        
        

if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.test']
    unittest.main()