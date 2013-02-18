import pygame
from pygame.locals import *
from ticker import Ticker
from renderer import Renderer
from gameplay import *


class Game(object):
    def __init__(self):
        Game.instance = self
        pygame.init()
        self.clock = pygame.time.Clock()
        
        WINSIZE = [640, 480]
        self.screen = pygame.display.set_mode(WINSIZE)
        pygame.display.set_caption('Bli bli bli')
        black = 0, 0, 0
        self.screen.fill(black)
        
        self.framedelay = 40
        
        play()
    
    def start(self):
        
        for renderer in Renderer.instances:
            renderer.draw(self.framedelay, self.screen)
        
        running = True
        while running:
            for ticker in Ticker.instances:
                ticker.onTick(self.framedelay)
            
            for renderer in Renderer.instances:
                renderer.clear(self.framedelay, self.screen)
            for renderer in Renderer.instances:
                renderer.draw(self.framedelay, self.screen)
            pygame.display.update()
            
            for e in pygame.event.get():
                if e.type == QUIT or (e.type == KEYUP and e.key == K_ESCAPE):
                    running = False
            self.clock.tick(self.framedelay)
        
        pygame.display.quit()

if __name__ == '__main__':
    Game().start()

