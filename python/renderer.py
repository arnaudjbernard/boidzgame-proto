from point import Point
import pygame

class Renderer(object):
    instances = []
    def __init__(self, owner):
        self.owner = owner
        Renderer.instances.append(self)
        self.prevPos = Point()
    
    def clear(self, delay, surface):
        pass
    
    def draw(self, delay, surface):
        pos = self.owner.coordinates.position
        self.prevPos.setTo(pos)
    
    def onRemove(self):
        Renderer.instances.remove(self)

class PointRenderer(Renderer):
    def __init__(self, owner, color, bcolor):
        super(PointRenderer, self).__init__(owner)
        self.color = color
        self.bcolor = bcolor
    
    def clear(self, delay, surface):
        surface.set_at((int(self.prevPos.x), int(self.prevPos.y)), self.bcolor)
    
    def draw(self, delay, surface):
        pos = self.owner.coordinates.position
        surface.set_at((int(pos.x), int(pos.y)), self.color)
        self.prevPos.setTo(pos)

class CircleRenderer(PointRenderer):
    def __init__(self, owner, color, bcolor, radius):
        super(CircleRenderer, self).__init__(owner, color, bcolor)
        self.radius = radius
    
    def clear(self, delay, surface):
        surface.fill(self.bcolor, self.prevRect)
    
    def draw(self, delay, surface):
        pos = self.owner.coordinates.position
        self.prevRect = pygame.draw.circle(surface, self.color, (int(pos.x), int(pos.y)), self.radius)

class DuckRenderer(CircleRenderer):
    def clear(self, delay, surface):
        surface.fill(self.bcolor, self.prevRect)
    
    def draw(self, delay, surface):
        pos = self.owner.coordinates.position
        spe = self.owner.coordinates.speed
        mainRect = pygame.draw.circle(surface, self.color, (int(pos.x), int(pos.y)), self.radius)
        headPos = spe.normalized()*self.radius + pos
        headRect = pygame.draw.circle(surface, self.color, (int(headPos.x), int(headPos.y)), self.radius/2)
        self.prevRect = mainRect.union(headRect)

class TruckRenderer(Renderer):
    def __init__(self, owner, color, bcolor, length, width):
        super(TruckRenderer, self).__init__(owner)
        self.color = color
        self.bcolor = bcolor
        self.length = length
        self.breadth = width
        self.prevRect = None
    
    def clear(self, delay, surface):
        surface.fill(self.bcolor, self.prevRect)
    
    def draw(self, delay, surface):
        pos = self.owner.coordinates.position
        spe = self.owner.coordinates.speed
        if spe.y<0:
            rect = pygame.Rect(pos.x, pos.y, self.breadth, self.length)
        elif spe.y>0:
            rect = pygame.Rect(pos.x-self.breadth, pos.y-self.length, self.breadth, self.length)
        elif spe.x<0:
            rect = pygame.Rect(pos.x, pos.y - self.breadth, self.length, self.breadth)
        elif spe.x>0:
            rect = pygame.Rect(pos.x - self.length, pos.y, self.length, self.breadth)
        self.prevRect = pygame.draw.rect(surface, self.color, rect)