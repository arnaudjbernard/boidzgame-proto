import random
from ticker import *
from renderer import *
from point import Point

class Coordinates(object):
    def __init__(self, owner):
        self.owner = owner
        self.position = Point()
        self.speed = Point()
        self.acceleration = Point()

class Entity(object):
    def __init__(self):
        for klass in (self.__class__,) + self.__class__.__bases__:
            if hasattr(klass, 'instances'):
                klass.instances.append(self)
    
    def remove(self):
        for klass in (self.__class__,) + self.__class__.__bases__:
            if hasattr(klass, 'instances'):
                klass.instances.remove(self)
        if hasattr(self, 'onRemove') and callable(self.onRemove):
            self.onRemove()
        for attrN in dir(self):
            if attrN.startswith('__'): continue
            attr = getattr(self, attrN)
            if hasattr(attr, 'onRemove') and callable(attr.onRemove):
                attr.onRemove()
        
    
class Particle(Entity):
    instances = []
    def __init__(self):
        super(Particle, self).__init__()
        self.renderer = PointRenderer(self, (random.randrange(122, 255), random.randrange(122, 255), random.randrange(122, 255)), (0, 0, 0))
        self.ticker = ParticleTicker(self)
        self.coordinates = Coordinates(self)

class BigParticle(Entity):
    instances = []
    def __init__(self):
        super(BigParticle, self).__init__()
        self.renderer = CircleRenderer(self, (random.randrange(122, 255), random.randrange(122, 255), random.randrange(122, 255)), (0, 0, 0), 10)
        self.ticker = ParticleTicker(self)
        self.coordinates = Coordinates(self)

class Duck(Entity):
    instances = []
    def __init__(self):
        super(Duck, self).__init__()
        self.renderer = DuckRenderer(self, (random.randrange(122, 255), random.randrange(122, 255), random.randrange(122, 255)), (0, 0, 0), 10)
        self.ticker = ParticleTicker(self)
        self.coordinates = Coordinates(self)

class MumDuck(Entity):
    instance = []
    def __init__(self):
        super(MumDuck, self).__init__()
        MumDuck.instance = self
        self.renderer = DuckRenderer(self, (153, 77, 0), (0, 0, 0), 10)
        self.ticker = MumDuckTicker(self, BabyDuck.instances)
        self.coordinates = Coordinates(self)

class BabyDuck(Entity):
    instances = []
    def __init__(self):
        super(BabyDuck, self).__init__()
        self.renderer = DuckRenderer(self, (255, 255, 60), (0, 0, 0), 5)
        self.ticker = BabyDuckTicker(self, MumDuck.instance, BabyDuck.instances)
        self.coordinates = Coordinates(self)

class Road(Entity):
    def __init__(self):
        super(Road, self).__init__()
        self.ticker = RoadTicker(self)

class Truck(Entity):
    instances = []
    def __init__(self):
        super(Truck, self).__init__()
        self.renderer = TruckRenderer(self, (random.randrange(122, 255), random.randrange(122, 255), random.randrange(122, 255)), (0, 0, 0), 100, 30)
        self.ticker = TruckTicker(self, MumDuck.instance, BabyDuck.instances)
        self.coordinates = Coordinates(self)
    

def simpleparticles():
    for _ in range(1000):
        Particle()

def bigparticles():
    for _ in range(10):
        BigParticle()

def ducks():
    for _ in range(10):
        Duck()

def duckfamily():
    MumDuck()
    for _ in range(10):
        BabyDuck()

def trucks():
    Road()

def ducksmash():
    duckfamily()
    trucks()

def play():
    ducksmash()