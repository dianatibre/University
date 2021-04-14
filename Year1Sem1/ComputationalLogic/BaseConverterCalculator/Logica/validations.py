'''
Tibre Diana Andreea, group 917
'''
class ValidationError(Exception):
    pass

class Validation:
    
    def BaseValidation(self,base):
        if int(base)<2 or int(base)>16:
            raise ValidationError('Invalid base!')
        
    def NumberInBaseValidation(self,number,base):
        numbers={'0':0,
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
        for i in range(len(number)):
            if number[i]!=',':
                if numbers[number[i]]>=base:
                    raise ValidationError('Number not in the specified base!')