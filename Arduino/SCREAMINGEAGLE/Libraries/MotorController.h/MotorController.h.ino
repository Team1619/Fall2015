#ifndef MotorController_h
#define MotorController_h

#include "Arduino.h"
#include "VictorMotor.h"

class MotorController {
  public:
    MotorController(int motorCount);
    void addMotor(int pin);
    void setMotorSpeed(int pin, int mSpeed);
    int getMotorSpeed(int pin);
    void updateMotors();
  private:
    VictorMotor[] motors;
    VictorMotor[] sort(VictorMotor[] sMotors);
}

#endif
//unfinished
