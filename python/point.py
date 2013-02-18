
class Point(object):
    def __init__(self, x=0., y=0.):
        self.x = x
        self.y = y
    
    def __iadd__(self, other):
        if isinstance(other, Point):
            self.x += other.x
            self.y += other.y
            return self
    def __add__(self, other):
        if isinstance(other, Point):
            return Point(self.x + other.x, self.y + other.y)
    __radd__ = __add__
    
    def __isub__(self, other):
        if isinstance(other, Point):
            self.x -= other.x
            self.y -= other.y
            return self
    def __sub__(self, other):
        if isinstance(other, Point):
            return Point(self.x - other.x, self.y - other.y)
    __rsub__ = __sub__
    
    def __imul__(self, other):
        if isinstance(other, int) or isinstance(other, float):
            self.x *= other
            self.y *= other
            return self
        elif isinstance(other, Point):
            self.x *= other.x
            self.y *= other.y
            return self
    def __mul__(self, other):
        if isinstance(other, int) or isinstance(other, float):
            return Point(self.x * other, self.y * other)
        elif isinstance(other, Point):
            return Point(self.x * other.x, self.y * other.y)
    __rmul__ = __mul__
    
    def __idiv__(self, other):
        if isinstance(other, int) or isinstance(other, float):
            self.x /= other
            self.y /= other
            return self
    def __div__(self, other):
        if isinstance(other, int) or isinstance(other, float):
            return Point(self.x / other, self.y / other)
        else:
            return NotImplemented
    
    def __abs__(self):
        return Point(abs(self.x), abs(self.y))
    
    def copy(self):
        return Point(self.x, self.y)

    def setTo(self, point):
        self.x = point.x
        self.y = point.y
        return self
    
    def reset(self):
        self.x = 0.
        self.y = 0.
        return self
    
    def norm(self):
        from math import sqrt
        return sqrt(self.x**2+self.y**2)
    
    def normalized(self, length = 1):
        norm = self.norm()
        if norm == 0: norm = 1
        return Point(self.x*length/norm, self.y*length/norm)

if __name__ == '__main__':
    p = Point()
    p += Point(1, 2) *3. - Point(4, 6)/2.
    p *= 2.
    print p.x, p.y, p.normalized().x, p.normalized().y
    p.reset()
    print p.x, p.y
