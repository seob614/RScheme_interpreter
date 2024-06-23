BASE_PATH := $(call my-dir)

include $(CLEAR_VARS)

RSCHEME_HOME := $(BASE_PATH)

ifeq ($(TARGET_ARCH),x86)
    PLATFORM := androidx86
    include $(CLEAR_VARS)
    LOCAL_MODULE := librs
    LOCAL_SRC_FILES :=  C:/Users/USER/AndroidStudioProjects/RScheme_interpreter/app/src/main/jni/librs/lib/x86/librs.a
    include $(PREBUILT_STATIC_LIBRARY)
else
    PLATFORM := android
    include $(CLEAR_VARS)
    LOCAL_MODULE := librs
    LOCAL_SRC_FILES :=  C:/Users/USER/AndroidStudioProjects/RScheme_interpreter/app/src/main/jni/librs/lib/armeabi/librs.a
    include $(PREBUILT_STATIC_LIBRARY)

endif
