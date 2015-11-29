unsigned int pin = 5;

signed int dutyCycle = -50;
unsigned int highus = 1500;
unsigned int lowus = 1000;

void setup() {
 pinMode(pin, OUTPUT); 
 digitalWrite(pin, LOW);
}

void loop() {
  digitalWrite(pin, HIGH);
  delayMicroseconds(highus);
  digitalWrite(pin, LOW);
  delayMicroseconds(lowus);
  
  highus = 5*dutyCycle + 1500;
  lowus = 1000;
}
