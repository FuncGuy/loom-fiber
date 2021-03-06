package fr.umlv.loom;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
class EventContinuationTests {
  enum Command { LEFT, RIGHT, QUIT }
  enum Direction { NORTH, EAST, SOUTH, WEST }
  
  private static final Direction[] DIRECTIONS = Direction.values();
  
  private static Direction turn(Direction direction, int shift) {
    return DIRECTIONS[(direction.ordinal() + shift + DIRECTIONS.length) % DIRECTIONS.length];
  }
  
  @Test
  void robot() {
    EventContinuation<Command, Direction> robot = new EventContinuation<>((yielder, parameter) -> {
      var direction = Direction.NORTH;
      var command = parameter;
      for(;;) {
        switch(command) {
        case LEFT:
          direction = turn(direction, -1);
          break;
        case RIGHT:
          direction = turn(direction, 1);
          break;
        case QUIT:
        default:
          return direction;
        }
        command = yielder.yield(direction);
      }
    });
    
    assertEquals(Direction.EAST, robot.execute(Command.RIGHT));
    assertEquals(Direction.SOUTH, robot.execute(Command.RIGHT));
    assertEquals(Direction.EAST, robot.execute(Command.LEFT));
    assertEquals(Direction.EAST, robot.execute(Command.QUIT));
  }
}
