from point import Point
import pygame
import random

class Ticker(object):
    instances = []
    def __init__(self, owner):
        self.owner = owner
        Ticker.instances.append(self)
    
    def onTick(self, delay):
        pass
    
    def onRemove(self):
        Ticker.instances.remove(self)

class ParticleTicker(Ticker):
    
    def onTick(self, delay):
        pos = self.owner.coordinates.position
        spe = self.owner.coordinates.speed
        acc = self.owner.coordinates.acceleration
        acc.reset()
        # attraction power of the mouse
        p = 10.
        # amplitude of the Brownian motion
        r = 1000.
        # viscuous friction
        fv = 0.01
        # solid friction
        fs = 0.1
        # mass of the particle
        mass = 1.

        if pygame.mouse.get_pressed()[0]:
            # mouse position
            mx, my = pygame.mouse.get_pos()
            mPos = Point(mx, my)
            acc += (mPos - pos)*p
        acc -= (spe*abs(spe))*fv + spe*fs
        acc /= mass
        acc += Point(random.uniform(-r, r), random.uniform(-r, r))
        
        spe += acc*delay/1000
        pos += spe*delay/1000

class MumDuckTicker(Ticker):
    def __init__(self, owner, babys):
        super(MumDuckTicker, self).__init__(owner)
        self.babys = babys
    
    def onTick(self, delay):
        pos = self.owner.coordinates.position
        spe = self.owner.coordinates.speed
        acc = self.owner.coordinates.acceleration
        
        # order babys
        self.babys.sort(key=lambda b: (pos - b.coordinates.position).norm())
        
        acc.reset()
        
        # attraction power of the mouse
        p = 10.
        # viscuous friction
        fv = 0.05
        # solid friction
        fs = 0.5
        # mass of the particle
        mass = 0.7
        # maximum speed
        maxspeed = 60
        
        if pygame.mouse.get_pressed()[0]:
            # mouse position
            mx, my = pygame.mouse.get_pos()
            mPos = Point(mx, my)
            acc += (mPos - pos)*p
        acc -= (spe*abs(spe))*fv + spe*fs
        acc /= mass
        spe += acc*delay/1000
        if spe.norm()>maxspeed:
            spe.setTo(spe.normalized()*maxspeed)
        pos += spe*delay/1000

class BabyDuckTicker(Ticker):
    def __init__(self, owner, mum, others):
        super(BabyDuckTicker, self).__init__(owner)
        self.mum = mum
        self.others = others
    
    def onTick(self, delay):
        pos = self.owner.coordinates.position
        spe = self.owner.coordinates.speed
        acc = self.owner.coordinates.acceleration
        acc *= 0.1
        
        # attraction power of the mum
        p = 2.
        # attraction power of the previous
        t = 15.
        # amplitude of the Brownian motion
        r = 30.
        # viscuous friction
        fv = 0.04
        # solid friction
        fs = 0.4
        # mass of the particle
        mass = 0.2
        # maximum speed
        maxspeed = 65
        # rest distance from previous
        restdist = 8
        
        
        i = self.others.index(self.owner)
        target = self.mum if i == 0 else self.others[i-1]
        tPos = target.coordinates.position
        dist = tPos - pos
        acc += dist.normalized()*(dist.norm() - restdist)
        
        mPos = self.mum.coordinates.position
        acc += (mPos - pos)*p/(i+1)
        
        acc -= (spe*abs(spe))*fv + spe*fs
        acc += Point(random.uniform(-r, r), random.uniform(-r, r))
        acc /= mass
        spe += acc*delay/1000
        if spe.norm()>maxspeed:
            spe.setTo(spe.normalized()*maxspeed)
        pos += spe*delay/1000

class RoadTicker(Ticker):
    def onTick(self, delay):
        pos = self.owner.coordinates.position
        spe = self.owner.coordinates.speed
        acc = self.owner.coordinates.acceleration
        
        # order babys
        self.babys.sort(key=lambda b: (pos - b.coordinates.position).norm())
        
        acc.reset()
        
        # attraction power of the mouse
        p = 5.
        # viscuous friction
        fv = 0.01
        # solid friction
        fs = 0.2
        # mass of the particle
        mass = 1.
        # maximum speed
        maxspeed = 50
        
        if pygame.mouse.get_pressed()[0]:
            # mouse position
            mx, my = pygame.mouse.get_pos()
            mPos = Point(mx, my)
            acc += (mPos - pos)*p
        acc -= (spe*abs(spe))*fv + spe*fs
        acc /= mass
        spe += acc*delay/1000
        if spe.norm()>maxspeed:
            spe.setTo(spe.normalized()*maxspeed)
        pos += spe*delay/1000

class RoadTicker(Ticker):
    def __init__(self, owner):
        super(RoadTicker, self).__init__(owner)
        self.trucks = []
    def onTick(self, delay):
        from gameplay import Truck
        if len(self.trucks) <15 and random.random()<0.005*(15-len(self.trucks)):
            truck = Truck()
            truck.coordinates.speed = Point(random.random()*40 + 40, 0)
            truck.coordinates.position = Point(0, random.randrange(100, 380))
            self.trucks.append(truck)
        for truck in self.trucks:
            if truck.coordinates.position.x > 640+truck.renderer.length:
                self.trucks.remove(truck)
                truck.remove()
        

class TruckTicker(Ticker):
    """trucks are stupid and will probably smash some baby ducks"""
    def __init__(self, owner, mum, babys):
        super(TruckTicker, self).__init__(owner)
        self.mum = mum
        self.babys = babys
    def onTick(self, delay):
        pos = self.owner.coordinates.position
        spe = self.owner.coordinates.speed
        pos += spe*delay/1000
        for duck in self.babys:
            if self.owner.renderer.prevRect:
                if self.owner.renderer.prevRect.inflate(duck.renderer.radius, duck.renderer.radius).collidepoint(duck.coordinates.position.x, duck.coordinates.position.y):
                    duck.remove()
                    print 'SMASH'