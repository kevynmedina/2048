#include <jni.h>
#include <stdio.h>
#include "MiClase.h"  // Este archivo se genera automáticamente con javah

JNIEXPORT jint JNICALL Java_MiClase_numAleatorio248(JNIEnv* env, jobject thisObj) {
    // Sumar los dos parámetros
    int numAleatorio=0;
    __asm{
        rdtsc ;marca de tiempo del procesador
        mov edx,0 ;
        mov ecx,3 ; divisor para numero del 0 al 2
        div ecx
        add edx,1

        cmp edx,1
        je opc1
        cmp edx,2
        je opc2
        cmp edx,3
        je opc3

        ; dependiendo del numero que salga se regresa 2,4,8
        opc1:
        MOV EDX,2
        jmp salida
        opc2:
        MOV EDX,4
        jmp salida
        opc3:
        MOV EDX,8
        salida:

        mov numAleatorio,edx
    }
    return numAleatorio;
}
JNIEXPORT jboolean JNICALL Java_MiClase_comprobarSiGana(JNIEnv* env, jobject thisObj, jint a, jint b) {
    // Sumar los dos parámetros

    __asm{
        MOV EAX, a
        MOV EBX, b

        cmp eax,ebx
        je iguales

        MOV a,0
        jmp salida
        iguales:
        MOV a,1

        salida:


    }
    return a;
}

JNIEXPORT jboolean JNICALL Java_MiClase_comprobarSiPierde(JNIEnv* env, jobject thisObj, jint a) {

    __asm{
        MOV EAX, a

        cmp eax,0
        je iguales

        MOV a,0
        jmp salida
        iguales:
        MOV a,1

        salida:


    }
    return a;
}

JNIEXPORT jint JNICALL Java_MiClase_sumarNumeros(JNIEnv* env, jobject thisObj, jint a, jint b) {
    // Sumar los dos parámetros
    int resultado;
    __asm{
        MOV EAX, a
        MOV EBX, b

        ADD EAX,EBX

        MOV resultado,EAX;
    }
    return resultado;
}

JNIEXPORT jint JNICALL Java_MiClase_regresarMayor(JNIEnv* env, jobject thisObj, jint a, jint b) {
    // Sumar los dos parámetros
    int resultado;
    __asm{
        MOV EAX, a
        MOV EBX, b

        cmp eax,ebx
        JA mayor
        MOV resultado,EBX

        jmp salir
        mayor:
        MOV resultado,EAX
        salir:
    }
    return resultado;
}

JNIEXPORT jboolean JNICALL Java_MiClase_compararSiEs0(JNIEnv* env, jobject thisObj, jint a) {

    __asm{
        MOV EAX, a

        cmp eax,0
        je iguales

        MOV a,1
        jmp salida
        iguales:
        MOV a,0

        salida:


    }
    return a;
}

JNIEXPORT jint JNICALL Java_MiClase_incrementarScore(JNIEnv* env, jobject thisObj, jint a, jint b) {
    int score;
    __asm{
        MOV EAX, a
        MOV EBX, b
        ADD EAX,EBX
        MOV score,EAX;
    }
    return score;
}









