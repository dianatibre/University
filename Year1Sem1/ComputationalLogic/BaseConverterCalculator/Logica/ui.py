'''
Tibre Diana Andreea, group 917
'''
from validations import ValidationError

class UI:
    
    def __init__(self,service):
        self.__service=service
        
    def read_numerical(self,text,numericaltype):
        number=input(text)
        while True:
            try:
                number=numericaltype(number)
                return number
            except ValueError:
                number=input('Invalid numerical type! Try again:')    
    
    def __ui_addition(self):
        number1=input('Enter the first number: ').upper()
        number2=input('Enter the second number: ').upper()
        base=self.read_numerical('Enter base: ', int)
        self.__service.add(number1,number2,base)
        rez=self.__service.get_result()
        print(str(number1.upper())+'+'+str(number2.upper())+'='+str(rez)+' in base '+str(base))
        
    def __ui_subtraction(self):
        number1=input('Enter the first number: ').upper()
        number2=input('Enter the second number: ').upper()
        base=self.read_numerical('Enter base: ', int)
        self.__service.sub(number1,number2,base)
        rez=self.__service.get_result()
        print(str(number1.upper())+'-'+str(number2.upper())+'='+str(rez)+' in base '+str(base))
        
    def __ui_multiplication(self):
        number1=input('Enter the first number: ').upper()
        number2=input('Enter the second number: ').upper()
        base=self.read_numerical('Enter base: ', int)
        self.__service.mul(number1,number2,base)
        rez=self.__service.get_result()
        print(str(number1.upper())+'*'+str(number2.upper())+'='+str(rez)+' in base '+str(base))
        
    def __ui_division(self):
        number1=input('Enter the first number: ').upper()
        number2=input('Enter the second number: ').upper()
        base=self.read_numerical('Enter base: ', int)
        self.__service.div(number1,number2,base)
        rez=self.__service.get_result()
        rem=self.__service.get_remainder()
        print(str(number1.upper())+'÷'+str(number2.upper())+'='+str(rez)+' r. ' + str(rem) + ' in base '+str(base))
        
    def __ui_succ_divs_muls(self):
        number=input('Enter the number: ').upper()
        base=self.read_numerical('Enter the initial base: ', int)
        dest_base=self.read_numerical('Enter the destination base: ', int)
        no_digits=self.read_numerical('Enter the number of digits at the fractional part: ', int)
        self.__service.succ_divs_muls(number,base,dest_base,no_digits)
        print(str(number)+' converted from base '+str(base)+' to '+str(dest_base)+' is ' +self.__service.get_result_conversions())
        
    def __ui_substitution(self):
        number=input('Enter the number: ').upper()
        base=self.read_numerical('Enter the initial base: ', int)
        dest_base=self.read_numerical('Enter the destination base: ', int)
        no_digits=self.read_numerical('Enter the number of digits at the fractional part: ', int)
        self.__service.subst(number,base,dest_base,no_digits)
        print(str(number)+' converted from base '+str(base)+' to '+str(dest_base)+' is ' +self.__service.get_result_conversions())
        
    def __ui_rapid_conversions(self):
        number=input('Enter the number: ').upper()
        base=self.read_numerical('Enter the initial base: ', int)
        dest_base=self.read_numerical('Enter the destination base: ', int)
        self.__service.rapid_convs(number,base,dest_base)
        print(str(number)+' converted from base '+str(base)+' to '+str(dest_base)+' is ' +self.__service.get_result_conversions())
        
        
    def menu(self):
        print('  Menu: ')
        print(' Operations:')
        print('1. Addition \n2. Subtraction \n3. Multiplication \n4. Division')
        print(' Conversions:')
        print('5. Method of successive divisions and multiplications \n6. Substitution method \n7. Rapid conversions')
        print('8. Exit')
         
        
    def run(self):
        print('Project author: Tibre Diana Andreea')
        print('group 917')
        self.menu()
        commands={'1':self.__ui_addition,
                  '2':self.__ui_subtraction,
                  '3':self.__ui_multiplication,
                  '4':self.__ui_division,
                  '5':self.__ui_succ_divs_muls,
                  '6':self.__ui_substitution,
                  '7':self.__ui_rapid_conversions
                  }
        while True:
            command=input('Enter command: ')
            if command in commands:
                try:
                    commands[command]()
                except ValidationError as ve:
                    print(ve)
            elif command=='8':
                return


