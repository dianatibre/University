'''
Created on Jan 28, 2020

@author: dianatibre
'''
import unittest
from validations import Validations, ValidationError


class Test(unittest.TestCase):


    def setUp(self):
        self.ship='A1A2A3'
        self.invalid_ship='A0A1A2'
        self.validations=Validations()


    def tearDown(self):
        pass


    def testShipValidation(self):
        self.validations.ship_validation(self.ship)
        try:    
            self.validations.ship_validation(self.invalid_ship)
            assert(False)
        except ValidationError as error:
            self.assertEqual(str(error), 'Point not on board!')
    


if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()