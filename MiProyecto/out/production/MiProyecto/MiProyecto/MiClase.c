#include <jni.h>
#include <stdio.h>
#include "MiClase.h"  // Este archivo se genera automáticamente con javah



JNIEXPORT void JNICALL Java_MiClase_regresarTexto(JNIEnv* env, jobject thisObj) {
    printf("Este es el texto prueba 2 ");
}

JNIEXPORT jint JNICALL Java_MiClase_numAleatorio04(JNIEnv* env, jobject thisObj) {
    // Sumar los dos parámetros
    int numAleatorio=0;
    __asm{
        rdtsc ;marca de tiempo del procesador
        mov edx,0 ;
        mov ecx,5 ; divisor para numero del 0 al 4
        div ecx ; edx tendrá el residuo

        mov numAleatorio,edx
    }
    return numAleatorio;
}

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
JNIEXPORT jint JNICALL Java_MiClase_comprobarSiGana(JNIEnv* env, jobject thisObj, jint a, jint b) {
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

JNIEXPORT jint JNICALL Java_MiClase_comprobarSiPierde(JNIEnv* env, jobject thisObj, jint a) {

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



// Implementación de la función nativa que suma dos números
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

#include <jni.h>
#include <stdio.h>

JNIEXPORT void JNICALL Java_MiClase_reverse(JNIEnv *env, jobject obj, jintArray array) {
    // Obtener el tamaño del arreglo
    jsize length = (*env)->GetArrayLength(env, array);

    // Obtener un puntero al arreglo en la memoria de la JVM
    jint *arr = (*env)->GetIntArrayElements(env, array, NULL);

    // Variables para los índices izquierdo y derecho
    int izquierda = 0, derecha = length - 1;

    // Intercambiar los elementos desde los extremos
    while (izquierda < derecha) {
        // Guardar temporalmente el valor de la izquierda
        jint temp = arr[izquierda];

        // Intercambiar los valores de los extremos
        arr[izquierda] = arr[derecha];
        arr[derecha] = temp;

        // Ajustar los índices
        izquierda++;
        derecha--;
    }

    // Sincronizar el arreglo de vuelta a la memoria de la JVM
    (*env)->ReleaseIntArrayElements(env, array, arr, 0);
}


