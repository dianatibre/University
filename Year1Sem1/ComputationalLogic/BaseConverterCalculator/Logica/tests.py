'''
Tibre Diana Andreea, group 917
'''

from operations import Operations
from conversions import Conversions

class Tests:
    
    def test_addition(self):
        test_operations=Operations()
        test_operations.add('2', '2', 3)
        assert(test_operations.get_result()=='11')
        
    def test_addition2(self):
        test_operations=Operations()
        test_operations.add('11', '11', 2)
        assert(test_operations.get_result()=='110')
        
    def test_addition3(self):
        test_operations=Operations()
        test_operations.add('AB0','F01', 16)
        assert(test_operations.get_result()=='19B1')
        
    def test_subtraction(self):
        test_operations=Operations()
        test_operations.sub('9AB0', 'C3', 16)  
        assert(test_operations.get_result()=='99ED') 
           
    def test_transform_in_decimal(self):
        test_operations=Operations()
        assert(test_operations.transform_in_decimal('34', 7)==25)
        
    def test_transform_in_decimal2(self):
        test_operations=Operations()
        assert(test_operations.transform_in_decimal('3E', 16)==62)
        
    def test_transform_in_decimal3(self):
        test_operations=Operations()
        assert(test_operations.transform_in_decimal('A', 16)==10)
        
    def test_conversion_to_base2(self):
        test_conversions=Conversions()
        test_conversions.conversion_to_base2('123,01', 4)
        assert(test_conversions.get_result()=='11011,0001')
        
    def test_conversion_to_base2_2(self):
        test_conversions=Conversions()
        test_conversions.conversion_to_base2('2017,5603', 8)
        assert(test_conversions.get_result()=='10000001111,101110000011')
        
        
    def run(self):
        self.test_addition()
        self.test_addition2()
        self.test_addition3()
        self.test_subtraction()
        self.test_transform_in_decimal()
        self.test_transform_in_decimal2()
        self.test_transform_in_decimal3()
        self.test_conversion_to_base2()
        self.test_conversion_to_base2_2()
