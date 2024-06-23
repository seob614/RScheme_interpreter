LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

ifeq ($(TARGET_ARCH),x86)
PLATFORM := androidx86
else
PLATFORM := android
endif

RSCHEME_HOME := $(LOCAL_PATH)/../librs

LOCAL_MODULE    := myrs
#LOCAL_PATH := $(LOCAL_PATH)
LOCAL_SRC_FILES := main.c
LOCAL_CFLAGS += -I$(RSCHEME_HOME)/include/$(TARGET_ARCH)
LOCAL_CFLAGS += -g -Wall -DANDROID  -O -DINLINES -DGC_MACROS
LOCAL_LDLIBS := -lz
LOCAL_LDLIBS += -llog

#LOCAL_LDLIBS += -L/C:/Users/USER/AppData/Local/Android/Sdk/ndk/android-ndk-r10b/toolchains/arm-linux-androideabi-4.6/prebuilt/windows-x86_64/lib/gcc/arm-linux-androideabi/4.6/include-fixed
#LOCAL_CFLAGS += -I/C:/Users/USER/AppData/Local/Android/Sdk/ndk/android-ndk-r10b/platforms/android-14/arch-arm/usr/include
#LOCAL_LDLIBS += -L/C:/Users/USER/AppData/Local/Android/Sdk/ndk/android-ndk-r10b/platforms/android-14/arch-arm/usr/lib

#LOCAL_ALLOW_UNDEFINED_SYMBOLS = true

LOCAL_STATIC_LIBRARIES :=    librs #libpackages liblss
#LOCAL_SHARED_LIBRARIES :=
include $(BUILD_SHARED_LIBRARY)




