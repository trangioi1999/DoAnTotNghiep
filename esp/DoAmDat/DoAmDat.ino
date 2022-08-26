#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#define FIREBASE_HOST "appthanhlong-34b6d.firebaseio.com"
#define FIREBASE_AUTH ""
#define WIFI_SSID "khucthuydu"
#define WIFI_PASSWORD "12345678aA"
int value, real_value;

void setup() {
  Serial.begin(115200);
  delay(10);
  pinMode(5, INPUT_PULLUP);
  Serial.println('\n');
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to ");
  Serial.print(WIFI_SSID); Serial.println(" ...");
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }
  int i = 0;
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.print(++i); Serial.print(' ');
  }
  Serial.println('\n');
  Serial.println("Kết Nối Thành Công!");
  Serial.print("Địa Chỉ IP:");
  Serial.println(WiFi.localIP());
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
}

void loop() {

  for (int i = 0; i <= 9; i++) {
    real_value += analogRead(A0);
  }
  value = real_value / 10;
  int percent = map(value, 350, 1023, 0, 100);
  percent = 100 - percent;
  Serial.print(percent);
  Serial.println('%');
  Firebase.setInt("ThongSo/DoAmDat", percent);
  delay(5000);
  real_value = 0;
  
}
