#define PINOUT 3

int dutyCycle = 0; 
int highTime = 1500;
int lowTime = 1000;

void setup() {
 pinMode(pin, OUTPUT); 
 digitalWrite(pin, LOW);
}

void loop() {
  digitalWrite(pin, HIGH);
  delayMicroseconds(highus);
  digitalWrite(pin, LOW);
  delayMicroseconds(lowus);
  
  highTime = 8*dutyCycle + 1600;
  lowTime = 5000-highTime;
}
