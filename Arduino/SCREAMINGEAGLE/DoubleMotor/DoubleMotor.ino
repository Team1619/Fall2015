const int leftPin = 3;
const int rightPin = 5;
//const int resolution = 201;

int leftDutyCycle = 0;
int rightDutyCycle = 0;
int test = 0;

void setup() {
  pinMode(leftPin, OUTPUT);
  pinMode(rightPin, OUTPUT);
  digitalWrite(leftPin, LOW);
  digitalWrite(rightPin, LOW);
  Serial.begin(9600);
}

void loop() {
  test++;
  runMotors(leftDutyCycle, rightDutyCycle);
  if(Serial.available() > 0) {
    leftDutyCycle = Serial.parseInt();
  }
}

void runMotors(int leftSpeed, int rightSpeed) {
  digitalWrite(leftPin, HIGH);
  digitalWrite(rightPin, HIGH);
  int leftDelay = 5*constrain(leftSpeed, -100, 100) + 1450;
  int rightDelay = 5*constrain(rightSpeed, -100, 100) + 1450;
  if(leftDelay > rightDelay) {
    delayMicroseconds(rightDelay);
    digitalWrite(rightPin, LOW);
    delayMicroseconds(leftDelay - rightDelay);
    digitalWrite(leftPin, LOW);
    delayMicroseconds(5000 - leftDelay);
  }
  else {
    delayMicroseconds(leftDelay);
    digitalWrite(leftPin, LOW);
    delayMicroseconds(rightDelay - leftDelay);
    digitalWrite(rightPin, LOW);
    delayMicroseconds(5000 - rightDelay);
  }
}

