LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE	:= notification
LOCAL_SRC_FILES	:= notification.c
LOCAL_LDLIBS	:= -llog -lGLESv2

include $(BUILD_SHARED_LIBRARY)