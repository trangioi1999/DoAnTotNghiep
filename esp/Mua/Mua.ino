#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#define FIREBASE_HOST "appthanhlong-34b6d.firebaseio.com"
#define FIREBASE_AUTH ""
#define WIFI_SSID "anhvan"
#define WIFI_PASSWORD "123123456"
int nRainIn = 0;          // khai bao chan ADC
int nRainVal;             // bien de doc gia tri ADC

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
  nRainVal = analogRead(nRainIn);
  Serial.print("\t Moisture Level: ");
  Serial.println(nRainVal);
  Firebase.setInt("ThongSo/Mua",nRainVal);
  delay(5000);
}
