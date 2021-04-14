class ValidationError(Exception):
    pass

class Validations:
    
    letters=['A','B','C','D','E','F']
    numbers=['1','2','3','4','5','6']
    
    def point_validation(self,point):
        if point[0] not in self.letters or point[1] not in self.numbers:
            raise ValidationError('Point not on board!')
    
    def ship_validation(self,ship):
        point1=ship[:2]
        point2=ship[:4]
        point3=ship[4:]
        self.point_validation(point1)
        self.point_validation(point2)
        self.point_validation(point3)        
        
        


