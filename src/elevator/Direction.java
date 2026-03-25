package elevator;

/**
 * ELEVATOR SYSTEM LLD
 * ====================
 * Key Concepts:
 *  - State Pattern:     Elevator states (IDLE, MOVING_UP, MOVING_DOWN, DOOR_OPEN)
 *  - Strategy Pattern:  Scheduling algorithms (SCAN, LOOK, FCFS)
 *  - Observer Pattern:  Floor display panels update when elevator moves
 *  - SRP:               Elevator, Door, Floor, Request are separate
 *
 * Core Algorithm:
 *  SCAN (Elevator Algorithm):
 *    - Move in one direction, servicing all requests in that direction
 *    - When no more requests in that direction, reverse
 *    - Like a disk arm scanning across platters
 *
 * Key Interview Points:
 *  - How to handle multiple elevators? (ElevatorController dispatches)
 *  - How to optimize? (Nearest elevator, load balancing)
 *  - Thread safety for concurrent requests
 *  - Internal vs External requests (inside elevator vs hallway buttons)
 */
public enum Direction {
    UP, DOWN, IDLE
}

