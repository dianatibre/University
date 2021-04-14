'''
Tibre Diana Andreea, group 917
'''

class Operations:
    
    def __init__(self):
        self.__result=0
        self.__remainder=0
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

    def get_remainder(self):
        return self.__remainder

    def set_remainder(self, value):
        self.__remainder = value

    def get_numbers(self):
        return self.__numbers

    def get_numbers_inv(self):
        return self.__numbers_inv
        
    def get_result(self):
        return self.__result
    
    def set_result(self,value):
        self.__result=value

    def add(self,number1,number2,base):
        #addition
        base=int(base)
        result=''
        carry=0
        while len(number1)>0 or len(number2)>0:                
            if len(number1)>0:
                c1=number1[-1]
                c1=self.get_numbers()[c1]
            else:
                c1=0
            if len(number2)>0:
                c2=number2[-1]
                c2=int(self.get_numbers()[c2])
            else:
                c2=0
            if (c1+c2+carry) >= base:
                if c1+c2+carry-base in self.get_numbers_inv():
                    result=str(self.get_numbers_inv()[c1+c2+carry-base])+result
                else:
                    result=str(c1+c2+carry-base)+result
                carry=1
            else:
                if c1+c2+carry in self.get_numbers_inv():
                    result=str(self.get_numbers_inv()[c1+c2+carry])+result
                else:
                    result=str(c1+c2+carry)+result
                carry=0
            number1=number1[:-1]
            number2=number2[:-1]
        if len(number1)==0 and len(number2)==0:
            if carry!=0:
                result=str(carry)+result
        self.set_result(result)
    
    def sub(self,number1,number2,base):
        #subtraction
        if base<=10:
            result=0
            carry=0
            p=1
            number1=int(number1)
            number2=int(number2)
            while number1>0 or number2>0:
                c1=number1%10
                c2=number2%10
                if c1+carry<c2:
                    c1=c1+base
                    result+=(carry+c1-c2)*p
                    carry=-1
                else:
                    result+=(carry+c1-c2)*p
                    carry=0
                p=p*10
                number1=number1 // 10
                number2=number2 // 10
        else:
            result=''
            carry=0
            p=1
            while len(number1)>0 or len(number2)>0:
                if len(number1)>0:
                    c1=number1[-1]
                    if c1 in self.get_numbers():
                        c1=int(self.get_numbers()[c1])
                    else:
                        c1=int(c1)
                else:
                    c1=0
                if len(number2)>0:
                    c2=number2[-1]
                    if c2 in self.get_numbers():
                        c2=int(self.get_numbers()[c2])
                    else:
                        c2=int(c2)
                else:
                    c2=0
                if (c1+carry)<c2:
                    c1=c1+base
                    if (carry+c1-c2) in self.get_numbers_inv():
                        result=self.get_numbers_inv()[carry+c1-c2]+result
                    else:
                        result=str(carry+c1-c2)+result
                    carry=-1
                else:
                    if (carry+c1-c2) in self.get_numbers_inv():
                        result=self.get_numbers_inv()[carry+c1-c2]+result
                    else:
                        result=str(carry+c1-c2)+result
                    carry=0
                number1=number1[:-1]
                number2=number2[:-1]   
        self.set_result(result)
        
    def mul(self,number1,number2,base):
        #multiplication
        result=''
        carry=0
        number2=int(self.get_numbers()[number2])
        while len(number1)>0:
            c=int(self.get_numbers()[number1[-1]])
            prod=(c*number2)+carry
            if int(prod)>=int(base):
                mod=prod%base
                div=prod//base
                result=str(self.get_numbers_inv()[mod])+result
                carry=int(div)
            else:
                result=str(self.get_numbers_inv()[prod])+result
                carry=0
            number1=number1[:(-1)]
        if carry!=0:
            result=str(self.get_numbers_inv()[carry])+result
        self.set_result(result)
        
    def mul_lower_than_1(self,number1,number2,base):
        #multiplication for numbers lower than 1
        if ',' in number1:
            parts=number1.split(',')
            number1=parts[1]
        result=''
        carry=0
        number2=int(self.get_numbers()[number2])
        while len(number1)>0:
            c=int(self.get_numbers()[number1[-1]])
            prod=(c*number2)+carry
            if int(prod)>=int(base):
                mod=prod%base
                div=prod//base
                result=str(self.get_numbers_inv()[mod])+result
                carry=int(div)
            else:
                result=str(self.get_numbers_inv()[prod])+result
                carry=0
            number1=number1[:(-1)]
        result=str(self.get_numbers_inv()[carry])+','+result
        self.set_result(result)
        
        
    def div(self,number1,number2,base):
        #division
        result=''
        if (len(number1)==len(number2)) and (self.transform_in_decimal(number1, base)<self.transform_in_decimal(number2, base)):
            self.set_result('0')
            self.set_remainder(number1)
        else:
            number2=self.transform_in_decimal(number2, base)
            c=''
            while len(number1)>0:
                c=c+number1[0]
                number1=number1[1:]
                c=self.transform_in_decimal(c, base)
                quotient=c//number2
                result=result+str(self.get_numbers_inv()[quotient])
                c=c%number2
                c=self.get_numbers_inv()[c]
            while result[0]=='0':
                result=result[1:]
            self.set_result(result)
            self.set_remainder(c)
        
        
    def transform_in_decimal(self,number,base):
        #transform a number from any base in decimal
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
    
            

                
                    
        
        
        
    