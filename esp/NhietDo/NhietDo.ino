#include <ESP8266WiFi.h>
#include <DHT.h>
#include <FirebaseArduino.h>
#define FIREBASE_HOST "appthanhlong-34b6d.firebaseio.com"
#define FIREBASE_AUTH ""
#define WIFI_SSID "khucthuydu"
#define WIFI_PASSWORD "12345678aA"
#define DHTPIN D4
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);
void setup() {
  Serial.begin(115200);
  delay(10);
  dht.begin();
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
  int temperature = dht.readTemperature();
  int humidity = dht.readHumidity();
  Firebase.setInt("ThongSo/NhietDo", temperature);
  Firebase.setInt("ThongSo/DoAm", humidity);
  delay(5000);
}
