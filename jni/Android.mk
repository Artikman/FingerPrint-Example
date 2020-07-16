LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := ndkfingerprint
LOCAL_SRC_FILES := ndkfingerprint.c

include $(BUILD_SHARED_LIBRARY)