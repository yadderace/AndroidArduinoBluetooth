#include <SoftwareSerial.h>
char rec;
SoftwareSerial BT1(0,1);/*pines 2 y 3 para conexion rx y tx del modulo bluetooth*/
long distancia1, distancia2, distancia3, distancia4;
long tiempo1, tiempo2, tiempo3, tiempo4;
char leer = 0; //Char para leer
boolean activeUp = false;
boolean activeDown = false;
boolean activeLeft = false;
boolean activeRight = false;

void setup(){
  Serial.begin(9600);
  pinMode(13, OUTPUT); /*activación del pin 13 como salida: para el pulso ultrasónico*/
  pinMode(12, INPUT); /*activación del pin 12 como entrada: tiempo del rebote del ultrasonido*/
  pinMode(11, OUTPUT); /*activación del pin 11 como salida: para el pulso ultrasónico*/
  pinMode(10, INPUT); /*activación del pin 10 como entrada: tiempo del rebote del ultrasonido*/
  pinMode(9, OUTPUT); /*activación del pin 9 como salida: para el pulso ultrasónico*/
  pinMode(8, INPUT); /*activación del pin 8 como entrada: tiempo del rebote del ultrasonido*/
  pinMode(7, OUTPUT); /*activación del pin 7 como salida: para el pulso ultrasónico*/
  pinMode(6, INPUT); /*activación del pin 6 como entrada: tiempo del rebote del ultrasonido*/
  BT1.begin(9600);
}

void loop(){
  
  if (Serial.available())
  {
    leer = Serial.read();
    BT1.print(leer);
  }
  
  digitalWrite(13,LOW); /* Por cuestión de estabilización del sensor*/
  delayMicroseconds(5);  
  digitalWrite(13, HIGH); /* envío del pulso ultrasónico*/
  delayMicroseconds(10);
  tiempo1 = pulseIn(12, HIGH); /* Función para medir la longitud del pulso entrante. Mide el tiempo que transcurrido entre el envío
  del pulso ultrasónico y cuando el sensor recibe el rebote, es decir: desde que el pin 12 empieza a recibir el rebote, HIGH, hasta que
  deja de hacerlo, LOW, la longitud del pulso entrante*/
  
  digitalWrite(11,LOW); /* Por cuestión de estabilización del sensor*/
  delayMicroseconds(5);  
  digitalWrite(11, HIGH); /* envío del pulso ultrasónico*/
  delayMicroseconds(10);
  tiempo2 = pulseIn(10, HIGH); 

  digitalWrite(9,LOW); /* Por cuestión de estabilización del sensor*/
  delayMicroseconds(5);  
  digitalWrite(9, HIGH); /* envío del pulso ultrasónico*/
  delayMicroseconds(10);
  tiempo3 = pulseIn(8, HIGH); 

  digitalWrite(7,LOW); /* Por cuestión de estabilización del sensor*/
  delayMicroseconds(5);  
  digitalWrite(7, HIGH); /* envío del pulso ultrasónico*/
  delayMicroseconds(10);
  tiempo4 = pulseIn(6, HIGH); 

  
  distancia1 = int(0.017*tiempo1); /*fórmula para calcular la distancia obteniendo un valor entero*/
  distancia2 = int(0.017*tiempo2); /*fórmula para calcular la distancia obteniendo un valor entero*/
  distancia3 = int(0.017*tiempo3); /*fórmula para calcular la distancia obteniendo un valor entero*/
  distancia4 = int(0.017*tiempo4); /*fórmula para calcular la distancia obteniendo un valor entero*/
  /*Monitorización en centímetros por el monitor serial*/
  //Serial.print("Distancia1: ");
  //Serial.print(distancia1);
  //Serial.println(" cm");
  //Serial.print("Distancia2: ");
  //Serial.print(distancia2);
  //Serial.println(" cm");
  //Serial.print("Distancia3: ");
  //Serial.print(distancia3);
  //Serial.println(" cm");
  //Serial.print("Distancia4: ");
  //Serial.print(distancia4);
  //Serial.println(" cm");
  
  delay(1000);

  /**
   * verifica si la distancia calculada de los cuatro sensores es menor a 10cm
   * envía el pulso al dispositivo conectado de bluetooth
   */
  if(distancia1 < 10 && distancia1 >= 0){
    if(activeDown == true){
      
      Serial.print("B"); //Para choque abajo
      BT1.println("B");
      activeDown = false;
    }
  }else{
    activeDown = true;
  }

  if(distancia2 < 10 && distancia2 >= 0){
    if(activeRight == true){
      
      Serial.print("D");//Para choque en la derecha
      BT1.println("D");
      activeRight = false;
    }
  }else{
    activeRight = true;
  }
  
  if(distancia3 < 10 && distancia3 >= 0){
    if(activeLeft == true){
      
      Serial.print("C"); //Para choque en la izquierda
      BT1.println("C");
      activeLeft = false;
    }
    
  }else{
    activeLeft = true;
  }
  
  if(distancia4 < 10 && distancia4 >= 0){
    if(activeUp == true){
      
      Serial.print("A");//Para choque arriba
      BT1.println("A");
      activeUp = false;
    }
  }else{
    activeUp = true;
  }
  
}


/*tone(buzzer, 1000, 400);
    delay(600);
    tone(buzzer, 1000, 400);
    delay(600);
    tone(buzzer, 1000, 400);
    delay(600);
    */
