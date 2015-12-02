#include "Arduino.h"
#include "VictorMotor.h"

VictorMotor::VictorMotor(int pin) {
  pinMode(pin, OUTPUT);
  motorPin = pin;
  motorSpeed = 0;
}

void VictorMotor::setSpeed(int speed) {
  motorSpeed = constrain(speed, -128, 128);
}

int VictorMotor::getSpeed() {
  return motorSpeed;
}

