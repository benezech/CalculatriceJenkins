/*
 * File:   test_IO.c
 * Author: didier Coque *
 * Created on 17 février 2017, 11:30
 */
#include <xc.h>
#include "config_bits.h"

#define _XTAL_FREQ 4000000
#define LED_V_ON 0b00001000 //LED verte allumée : pas de dysfonctionnement
#define LED_R_ON 0b00000100 //LED rouge allumée : pas de dysfonctionnement
#define LED_O_ON 0b00000010 //LED orange allumée : pas de dysfonctionnement
#define LED_J_ON 0b00000001 //LED verte allumée : pas de dysfonctionnement

void main(void)

{

    // declaration d'une vaiable 8 bits non signé
    unsigned char Data=0;

    // configuration des ports
    // voir doc 16f877 page 33
    TRISC = 0xF0;  //  Port C en  sortie RC7?RC5 entrées RC3 ? RC0 sorties
    TRISD = 0xFF;  //port D  tout en entree 

   //  le bit 3 du port D est mis à 1 si les bit 0 et 1 du port D sont à 1
   // ajouter les lignes RD0 et RD1 dans les i/o pins du simulateur
   // ajouter PORTC en watch run time ( Debug -> new runtime watch))
    // tester en simulation

    while (1)
    {         
        Data = PORTD;  // lit l'état des broches 0 et 1 du port D  Data <- Rd0 et Rd1
                            // Data = 1 si RD0 =1 et RD1 = 1  sinon Data = 0

        if(Data == 0xAC){
            PORTC = LED_V_ON;
        }else if (Data && 0b00000010 == 0b00000010){
            PORTC = LED_R_ON;
        }else if (Data >> 4 == 0b00000011){
            PORTC = LED_O_ON;
        }else if (Data && 0b00001100){
            PORTC = LED_J_ON;
        }
        __delay_ms(250);
  }            
}

 /*
 // fichier config des bits spécifiques au processeur 16f877
// à utiliser dans les autres projets utilisant ce processeur
#include "config_bits.h"
#define __XTAL_FREQ

void main(void)
{
    // declaration d'une vaiable 8 bits non signé
    unsigned char Data=0;
    // configuration des ports 
    // voir doc 16f877 page 33 
    TRISC = 0xF0;  //  RC7?RC5 entrées RC3 ? RC0 sorties
    TRISD = 0xFF;  //port D  tout en entree  
   //  le bit 3 du port D est mis à 1 si les bit 0 et 1 du port D sont à 1 
   // ajouter les lignes RD0 et RD1 dans les i/o pins du simulateur 
   // ajouter PORTC en watch run time ( Debug -> new runtime watch))
    // tester en simulation 
   
     
   
    while (1)
    {          
        Data = PORTD;  // lit l'état des broches 0 et 1 du port D  Data <- Rd0 et Rd1
         
        if(Data == 0xAC){
            PORTC = 0x08;
        }else if (0x08 & 0x02 == 0x02){
            PORTC = 0x04;
        }else if (0x08 >> 4 == 0x03){
            PORTC = 0x02;
        }else if (0x08 & 0x0C){
            PORTC = 0x01;
        }
        __delay_ms(250);
         
     }            
    
}
*/