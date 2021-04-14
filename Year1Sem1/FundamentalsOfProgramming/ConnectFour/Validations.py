class InputError(Exception):
    pass

class Validations:
    
    def ColumnValidation(self,column):
        #validates if a given column is in range of the board
        #input: column - the number of the column
        #output: error if the column number is not in the range
        error=''
        try:
            column=int(column)
            if (column<1) or (column>7):
                error+='Column out of range!'
        except:
            ValueError
        if len(error)>0:
            raise InputError(error)
                
        
        


