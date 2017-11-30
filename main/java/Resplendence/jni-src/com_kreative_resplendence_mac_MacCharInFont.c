#include "com_kreative_resplendence_mac_MacCharInFont.h"
#include <CoreFoundation/CoreFoundation.h>
#include <ApplicationServices/ApplicationServices.h>
#include <string.h>
#include <stdio.h>

JNIEXPORT jboolean JNICALL Java_com_kreative_resplendence_mac_MacCharInFont_inFont__Ljava_lang_String_2I(JNIEnv * env, jclass cl, jstring fontName, jint charToCheck) {
    const char * ntvFontName = (*env)->GetStringUTFChars(env, fontName, 0);
    
    CFStringRef fn = CFStringCreateWithBytes(NULL, (const UInt8 *)ntvFontName, strlen(ntvFontName), kCFStringEncodingUTF8, false);
    CTFontRef f = CTFontCreateWithName(fn, 12.0, NULL);
    CFCharacterSetRef cs = CTFontCopyCharacterSet(f);
    
    jboolean ret = (jboolean)(CFCharacterSetIsLongCharacterMember(cs, charToCheck));
    
    if (fn) CFRelease(fn);
    if (f) CFRelease(f);
    if (cs) CFRelease(cs);
    
    (*env)->ReleaseStringUTFChars(env, fontName, ntvFontName);
    
    return ret;
}

JNIEXPORT jbooleanArray JNICALL Java_com_kreative_resplendence_mac_MacCharInFont_inFont__Ljava_lang_String_2II(JNIEnv * env, jclass cl, jstring fontName, jint startChar, jint endChar) {
	const char * ntvFontName = (*env)->GetStringUTFChars(env, fontName, 0);
    
    CFStringRef fn = CFStringCreateWithBytes(NULL, (const UInt8 *)ntvFontName, strlen(ntvFontName), kCFStringEncodingUTF8, false);
    CTFontRef f = CTFontCreateWithName(fn, 12.0, NULL);
    CFCharacterSetRef cs = CTFontCopyCharacterSet(f);
    
    jsize len = endChar - startChar + 1;
    jbooleanArray ret = (*env)->NewBooleanArray(env, len);
    
    jint charToCheck, idx;
    for (charToCheck = startChar, idx = 0; charToCheck <= endChar && idx < len; charToCheck++, idx++) {
    	jboolean r = (jboolean)(CFCharacterSetIsLongCharacterMember(cs, charToCheck));
    	(*env)->SetBooleanArrayRegion(env, ret, idx, 1, &r);
    }
    
    if (fn) CFRelease(fn);
    if (f) CFRelease(f);
    if (cs) CFRelease(cs);
    
    (*env)->ReleaseStringUTFChars(env, fontName, ntvFontName);
    
    return ret;
}

JNIEXPORT jbooleanArray JNICALL Java_com_kreative_resplendence_mac_MacCharInFont_inFont__Ljava_lang_String_2_3I(JNIEnv * env, jclass cl, jstring fontName, jintArray charsToCheck) {
	const char * ntvFontName = (*env)->GetStringUTFChars(env, fontName, 0);
    
    CFStringRef fn = CFStringCreateWithBytes(NULL, (const UInt8 *)ntvFontName, strlen(ntvFontName), kCFStringEncodingUTF8, false);
    CTFontRef f = CTFontCreateWithName(fn, 12.0, NULL);
    CFCharacterSetRef cs = CTFontCopyCharacterSet(f);
    
    jsize len = (*env)->GetArrayLength(env, charsToCheck);
    jbooleanArray ret = (*env)->NewBooleanArray(env, len);
    
    jint idx;
    for (idx = 0; idx < len; idx++) {
    	jint charToCheck;
    	(*env)->GetIntArrayRegion(env, charsToCheck, idx, 1, &charToCheck);
    	jboolean r = (jboolean)(CFCharacterSetIsLongCharacterMember(cs, charToCheck));
    	(*env)->SetBooleanArrayRegion(env, ret, idx, 1, &r);
    }
    
    if (fn) CFRelease(fn);
    if (f) CFRelease(f);
    if (cs) CFRelease(cs);
    
    (*env)->ReleaseStringUTFChars(env, fontName, ntvFontName);
    
    return ret;
}
