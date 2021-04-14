'''
Tibre Diana Andreea, group 917
'''

class Service:
    
    def __init__(self,operations,conversions,validations):
        self.__operations=operations
        self.__conversions=conversions
        self.__validations=validations
        
    def get_result(self):
        return self.__operations.get_result()
    
    def get_result_conversions(self):
        return self.__conversions.get_result()
    
    def get_remainder(self):
        return self.__operations.get_remainder()
        
    def add(self,number1,number2,base):
        self.__validations.BaseValidation(base)
        self.__validations.NumberInBaseValidation(number1,base)
        self.__validations.NumberInBaseValidation(number2,base)
        self.__operations.add(number1,number2,base)

    def sub(self,number1,number2,base):
        self.__validations.BaseValidation(base)
        self.__validations.NumberInBaseValidation(number1,base)
        self.__validations.NumberInBaseValidation(number2,base)
        self.__operations.sub(number1,number2,base)
        
    def mul(self,number1,number2,base):
        self.__validations.BaseValidation(base)
        self.__validations.NumberInBaseValidation(number1,base)
        self.__validations.NumberInBaseValidation(number2,base)
        self.__operations.mul(number1,number2,base)
        
    def div(self,number1,number2,base):
        self.__validations.BaseValidation(base)
        self.__validations.NumberInBaseValidation(number1,base)
        self.__validations.NumberInBaseValidation(number2,base)
        self.__operations.div(number1,number2,base)
        
    def succ_divs_muls(self,number,base,destination_base,no_digits_fractional_part):
        self.__validations.BaseValidation(base)
        self.__validations.BaseValidation(destination_base)
        self.__validations.NumberInBaseValidation(number,base)
        self.__conversions.successive_divs_muls(number,base,destination_base,no_digits_fractional_part)

    def subst(self,number,base,destination_base,no_digits_fractional_part):
        self.__validations.BaseValidation(base)
        self.__validations.BaseValidation(destination_base)
        self.__validations.NumberInBaseValidation(number,base)
        self.__conversions.substitution(number,base,destination_base,no_digits_fractional_part)
        
    def rapid_convs(self,number,base,destination_base):
        self.__validations.BaseValidation(base)
        self.__validations.BaseValidation(destination_base)
        self.__validations.NumberInBaseValidation(number,base)
        self.__conversions.rapid_conversions(number,base,destination_base)
    
