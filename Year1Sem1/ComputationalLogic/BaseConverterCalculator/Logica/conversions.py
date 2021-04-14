'''
Tibre Diana Andreea, group 917
'''
import copy
from operations import Operations

class Conversions:
    
    def __init__(self):
        self.__numbers={'0':0,
                        '1':1,
                        '2':2,
                        '3':3,
                        '4':4,
                        '5':5,
                        '6':6,
                        '7':7,
                        '8':8,
                        '9':9,
                        'A':10,
                        'B':11,
                        'C':12,
                        'D':13,
                        'E':14,
                        'F':15}
        self.__numbers_inv={0:'0',
                            1:'1',
                            2:'2',
                            3:'3',
                            4:'4',
                            5:'5',
                            6:'6',
                            7:'7',
                            8:'8',
                            9:'9',
                            10:'A',
                            11:'B',
                            12:'C',
                            13:'D',
                            14:'E',
                            15:'F'}
        self.__result=''

    def get_numbers(self):
        return self.__numbers

    def get_numbers_inv(self):
        return self.__numbers_inv

    def set_result(self, value):
        self.__result = value

    def get_result(self):
        return self.__result

    def transform_in_decimal(self,number,base):
        #transforms a number in any base to decimal
    
        result=0
        p=0
        while len(number)>0:
            c=self.get_numbers()[number[-1]]
            if p==0:
                result=result+c
            else:
                for i in range(p):
                    c=c*base
                result=result+c
            p=p+1
            number=number[:-1]
        return int(result)
    
    def successive_divs_muls(self,number,base,dest_base,no_digits_fractional_part):
        if ',' in number:
            parts=number.split(',')
            integer_part=parts[0]
            decimal_part='0,'+parts[1][:2]
        else:
            integer_part=number
        divisor=dest_base
        operations=Operations()
        #transformation for integer part
        result_int=''
        while integer_part!='0':
            operations.div(str(integer_part), self.get_numbers_inv()[divisor], base)
            integer_part=copy.deepcopy(operations.get_result())
            result_int=str(operations.get_remainder())+result_int  

        #transformation for decimal part
        result_dec=''
        for i in list(range(no_digits_fractional_part)):
            if decimal_part=='0,00':
                result_dec=result_dec+'0'
            else:
                operations.mul_lower_than_1(str(decimal_part),self.get_numbers_inv()[divisor],base)
                parts=(operations.get_result()).split(',')
                result_dec=result_dec+str(parts[0])
                decimal_part='0,'+parts[1][:2]
        if no_digits_fractional_part!=0:        
            self.set_result(str(result_int+','+result_dec))
        else:
            self.set_result(result_int)

    def substitution(self,number,base,dest_base,no_digits_fractional_part):
        operations=Operations()
        if base<dest_base:
            if ',' in number:
                parts=number.split(',')
                integer_part=parts[0]
                decimal_part=parts[1]
            else:
                integer_part=number
                decimal_part=''
            #transform integer part
            p=0
            integer_result=''
            result=''
            while len(integer_part)>0:
                if p==0:
                    result=integer_part[-1]
                    integer_result=result
                else:
                    result=integer_part[-1]
                    for i in list(range(p)):
                        operations.mul(result, self.get_numbers_inv()[base], dest_base)
                        result=operations.get_result()
                    operations.add(str(integer_result),str(result),dest_base)
                    integer_result=operations.get_result()
                p=p+1
                integer_part=integer_part[:-1]
                
            #transforms decimal part 
            p=1
            decimal_result='0'
            result=''
            while len(decimal_part)>0:
                result=decimal_part[0]
                for i in range(no_digits_fractional_part):
                    result=result+'0'
                for i in range(p):
                    operations.div(result, self.get_numbers_inv()[base],dest_base)
                    result=operations.get_result()
                operations.add(decimal_result,result,dest_base)
                decimal_result=operations.get_result()
                p=p+1
                decimal_part=decimal_part[1:]
            if decimal_result=='0':
                self.set_result(str(integer_result))
            elif len(decimal_result)==no_digits_fractional_part:
            #if decimal_result[0]=='0':
                self.set_result(str(integer_result+','+decimal_result))
            else:
                current_decimal_result=decimal_result[-no_digits_fractional_part:]
                decimal_result=decimal_result[:-no_digits_fractional_part]
                operations.add(decimal_result,integer_result,dest_base)
                integer_result=operations.get_result()
                self.set_result(str(integer_result+','+current_decimal_result[1:]))
                
                
    def rapid_conversions(self,number,base,dest_base):
        if ',' in number:
            parts=number.split(',')
            integer_part=parts[0]
            decimal_part=parts[1]
        else:
            integer_part=number
            decimal_part=''
        if base==2:
            convert=Conversions()
            convert.conversion_from_base2(number, dest_base)
            self.set_result(convert.get_result())
        elif dest_base==2:
            convert=Conversions()
            convert.conversion_to_base2(number, base)
            self.set_result(convert.get_result())
        else:
            convert=Conversions()
            convert.conversion_to_base2(number, base)
            number=copy.deepcopy(convert.get_result())
            convert.conversion_from_base2(number, dest_base)
            self.set_result(convert.get_result())
        self.set_result(convert.get_result())
            
    def conversion_from_base2(self,number,dest_base):
        #converts from binary to any base
        if ',' in number:
            parts=number.split(',')
            integer_part=parts[0]
            decimal_part=parts[1]
        else:
            integer_part=number
            decimal_part=''
        digits={4:2,
                8:3,
                16:4}
        for i in range(digits[dest_base]-len(integer_part)%digits[dest_base]):
            integer_part='0'+integer_part
        for i in range(digits[dest_base]-len(decimal_part)%digits[dest_base]):
            decimal_part=decimal_part+'0'
        integer_result=''
        decimal_result=''
        len_integer=len(integer_part)
        len_decimal=len(decimal_part)
        for i in range(len_integer//digits[dest_base]):
            digit=integer_part[-digits[dest_base]:]
            integer_part=integer_part[:-digits[dest_base]]
            self.substitution(str(digit), 2, dest_base, 0)
            integer_result=str(self.get_result())+integer_result
        for i in range(len_decimal//digits[dest_base]):
            digit=decimal_part[:digits[dest_base]]
            decimal_part=decimal_part[digits[dest_base]:]
            self.substitution(digit, 2, dest_base, 0)
            decimal_result=decimal_result+self.get_result()
        while integer_result[0]=='0':
            integer_result=integer_result[1:]
        while decimal_result[-1]=='0':                
            decimal_result=decimal_result[:-1]
        if decimal_result=='':
            self.set_result(integer_result)
        else:
            self.set_result(integer_result+','+decimal_result)
        
    def conversion_to_base2(self,number,base):
        #converts from bases 4,8 or 16 to binary
        if base==4:
            digits={'0':'00',
                    '1':'01',
                    '2':'10',
                    '3':'11'}
        if base==8:
            digits={'0':'000',
                    '1':'001',
                    '2':'010',
                    '3':'011',
                    '4':'100',
                    '5':'101',
                    '6':'110',
                    '7':'111'}      
               
        if base==16:
            digits={'0':'0000',
                    '1':'0001',
                    '2':'0010',
                    '3':'0011',
                    '4':'0100',
                    '5':'0101',
                    '6':'0110',
                    '7':'0111',
                    '8':'1000',
                    '9':'1001',
                    'A':'1010',
                    'B':'1011',
                    'C':'1100',
                    'D':'1101',
                    'E':'1110',
                    'F':'1111'}
        result=''
        while len(number)>0:
            digit=number[0]
            if digit!=',':
                result=result+str(digits[digit])
            else:
                result=result+digit
            number=number[1:]
        while result[0]=='0':
            result=result[1:]
        if ',' in result:
            while result[-1]=='0':
                result=result[:-1]  
        self.set_result(result)
    