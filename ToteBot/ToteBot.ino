#define OUTPIN 3

// pwm value between 0 and 255 inclusive
int outputVal = 0;

void setup() {
    pinMode(OUTPIN, OUTPUT);

}

void loop() {
    analogWrite(OUTPIN, outputVal);
    
}
