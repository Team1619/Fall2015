/*
 * Library for writing to victor motor controllers using an arduino
 */
#ifndef VictorMotor_h
#define VictorMotor_h

#include "Arduino.h"

class VictorMotor {
  public:
    VictorMotor(int pin);
    void setSpeed(int speed);
    int getSpeed();
  private:
    int motorPin;
    int motorSpeed;
}

#endif
