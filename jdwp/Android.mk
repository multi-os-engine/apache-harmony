# -*- mode: makefile -*-

LOCAL_PATH := $(call my-dir)

define all-harmony-test-java-files-under
  $(patsubst ./%,%,$(shell cd $(LOCAL_PATH) && find $(2) -name "*.java" 2> /dev/null))
endef

harmony_jdwp_test_src_files := \
    $(call all-harmony-test-java-files-under,,src/test/java/)

# Common JDWP settings
jdwp_test_timeout_ms := 10000 # 10s.
jdwp_test_target_runtime_common_args :=  \
	-Djpda.settings.verbose=true \
	-Djpda.settings.syncPort=34016 \
	-Djpda.settings.timeout=$(jdwp_test_timeout_ms) \
	-Djpda.settings.waitingTime=$(jdwp_test_timeout_ms)

# CTS configuration
#
# We run in non-debug mode and support running with a forced abi.
cts_jdwp_test_runtime_target := dalvikvm|\#ABI\#| -XXlib:libart.so
cts_jdwp_test_target_runtime_args :=  $(jdwp_test_target_runtime_common_args)
cts_jdwp_test_target_runtime_args += -Djpda.settings.debuggeeJavaPath='$(cts_jdwp_test_runtime_target)'

include $(CLEAR_VARS)
LOCAL_SRC_FILES := $(harmony_jdwp_test_src_files)
LOCAL_JAVA_LIBRARIES := junit-targetdex
LOCAL_MODULE_TAGS := optional
LOCAL_MODULE := CtsJdwp
LOCAL_NO_EMMA_INSTRUMENT := true
LOCAL_NO_EMMA_COMPILE := true
LOCAL_CTS_TEST_PACKAGE := android.jdwp
LOCAL_CTS_TARGET_RUNTIME_ARGS := $(cts_jdwp_test_target_runtime_args)
include $(BUILD_CTS_TARGET_JAVA_LIBRARY)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := $(harmony_jdwp_test_src_files)
LOCAL_JAVA_LIBRARIES := junit-targetdex
LOCAL_MODULE_TAGS := tests
LOCAL_MODULE := apache-harmony-jdwp-tests
LOCAL_NO_EMMA_INSTRUMENT := true
LOCAL_NO_EMMA_COMPILE := true
LOCAL_MODULE_PATH := $(TARGET_OUT_DATA)/jdwp
include $(BUILD_JAVA_LIBRARY)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := $(harmony_jdwp_test_src_files)
LOCAL_JAVA_LIBRARIES := junit
LOCAL_MODULE := apache-harmony-jdwp-tests-host
include $(BUILD_HOST_JAVA_LIBRARY)

ifeq ($(HOST_OS),linux)
include $(CLEAR_VARS)
LOCAL_SRC_FILES := $(harmony_jdwp_test_src_files)
LOCAL_JAVA_LIBRARIES := junit-hostdex
LOCAL_MODULE := apache-harmony-jdwp-tests-hostdex
include $(BUILD_HOST_DALVIK_JAVA_LIBRARY)
endif  # HOST_OS == linux

include $(call all-makefiles-under,$(LOCAL_PATH))

# Debug configuration (development and continuous testing)
#
# We run in debug mode to get more error checking and enable JDWP verbose logs
# to investigate failures during testing.

# Target settings
jdwp_test_classpath_target := /data/jdwp/apache-harmony-jdwp-tests.jar:/data/junit/junit-targetdex.jar
jdwp_test_runtime_bin_target := dalvikvm

# Host settings
jdwp_test_classpath_host := $(HOST_OUT_JAVA_LIBRARIES)/apache-harmony-jdwp-tests-hostdex.jar:$(HOST_OUT_JAVA_LIBRARIES)/junit-hostdex.jar
jdwp_test_runtime_bin_host := $(HOST_OUT_EXECUTABLES)/art

# Common runtime settings for runner and debuggee.
jdwp_test_common_runtime_options := -XXlib:libartd.so

# Debuggee runtime options
jdwp_test_runtime_options := -verbose:jdwp
#jdwp_test_runtime_options += -Xint # interpret-only mode
#jdwp_test_runtime_options += -Xnoimage-dex2oat # imageless mode
#jdwp_test_runtime_options += -Xcompiler-option --compiler-backend=Optimizing # optimizing compiler
#jdwp_test_runtime_options += -verbose:threads

# Test suite class name
jdwp_test_suite_class_name := org.apache.harmony.jpda.tests.share.AllTests

HOST_ABIS :=
TARGET_ABIS :=
LAST_JDWP_HOST_RULE :=
LAST_JDWP_TARGET_RULE :=

ifneq ($(HOST_PREFER_32_BIT),true)
  HOST_ABIS := 64 32
else
  HOST_ABIS := 32
endif

ifdef TARGET_2ND_ARCH
  ifeq ($(TARGET_IS_64_BIT),true)
    TARGET_ABIS := 64 32
  else
    # TODO: ???
    $(error Do not know what to do with this multi-target configuration!)
  endif
else
  ifeq ($(TARGET_IS_64_BIT),true)
    TARGET_ABIS := 64
  else
    TARGET_ABIS := 32
  endif
endif

JDWP_HOST_DEPENDENCIES := \
  $(HOST_OUT_EXECUTABLES)/art \
  $(HOST_OUT_JAVA_LIBRARIES)/apache-harmony-jdwp-tests-hostdex.jar \
  $(HOST_OUT_JAVA_LIBRARIES)/junit-hostdex.jar

JDWP_TARGET_DEPENDENCIES := \
  $(TARGET_OUT_DATA)/jdwp/apache-harmony-jdwp-tests.jar \
  $(TARGET_OUT_DATA)/junit/junit-targetdex.jar

# Define a JDWP host rule
#
# $(1) ABI (32 or 64)
# $(2) rule name (ex: run-jdwp-tests-host32)
# $(3) extra dependency rule (ex: run-jdwp-tests-host-all64)
define define-jdwp-host-rule
.PHONY: $(2)
$(2): jdwp_test_runtime_host := $(jdwp_test_runtime_bin_host) --$(1) $(jdwp_test_common_runtime_options)
$(2): $(JDWP_HOST_DEPENDENCIES) $(3)
	$(hide) echo "Running JDWP $(1)-bit host tests"
	$$(jdwp_test_runtime_host) -cp $(jdwp_test_classpath_host) \
    $(jdwp_test_target_runtime_common_args) \
    -Djpda.settings.debuggeeJavaPath='$$(jdwp_test_runtime_host) $(jdwp_test_runtime_options)' \
    $(jdwp_test_suite_class_name)
endef

# Declare all JDWP host rules
#
# $(1) ABI (32 or 64)
define declare-jdwp-host-rule
  # Declare standalone host rule for the given ABI.
  $(eval $(call define-jdwp-host-rule,$(1),run-jdwp-tests-host$(1),))

  # Declare variant host rule for run-jdwp-tests-host. It depends on the previous abi rule(s)
  # so all ABIs are tested.
  $(eval $(call define-jdwp-host-rule,$(1),run-jdwp-tests-host-all$(1),$(LAST_JDWP_HOST_RULE)))
  LAST_JDWP_HOST_RULE := run-jdwp-tests-host-all$(1)
endef

# Waits for device to boot completely.
define wait-for-boot-complete
$(hide) echo "Wait for boot complete ..."
$(hide) while [ `adb wait-for-device shell getprop dev.bootcomplete | grep -c 1` -eq 0 ]; \
do \
  sleep 1; \
done
$(hide) echo "Boot complete"
endef

# Define a JDWP target rule
#
# $(1) ABI (32 or 64)
# $(2) rule name (ex: run-jdwp-tests-target32)
# $(3) extra dependency rule (ex: run-jdwp-tests-target-all64)
define define-jdwp-target-rule
.PHONY: $(2)
$(2): jdwp_test_runtime_target := $(jdwp_test_runtime_bin_target)$(1) $(jdwp_test_common_runtime_options)
$(2): $(JDWP_TARGET_DEPENDENCIES) $(3)
	$(hide) echo "Running JDWP $(1)-bit target tests"
	adb root
	adb wait-for-device shell stop
	adb remount
	adb sync
	adb reboot
	$$(call wait-for-boot-complete)
	adb shell $$(jdwp_test_runtime_target) -cp $(jdwp_test_classpath_target) \
    $(jdwp_test_target_runtime_common_args) \
    -Djpda.settings.debuggeeJavaPath='$$(jdwp_test_runtime_target) $(jdwp_test_runtime_options)' \
    $(jdwp_test_suite_class_name)
endef

# Declare all JDWP target rules
#
# $(1) ABI (32 or 64)
define declare-jdwp-target-rule
  # Declare standalone target rule for the given ABI.
  $(eval $(call define-jdwp-target-rule,$(1),run-jdwp-tests-target$(1),))

  # Declare variant target rule for run-jdwp-tests-target. It depends on the previous abi rule(s)
  # so all ABIs are tested.
  $(eval $(call define-jdwp-target-rule,$(1),run-jdwp-tests-target-all$(1),$(LAST_JDWP_TARGET_RULE)))
  LAST_JDWP_TARGET_RULE := run-jdwp-tests-target-all$(1)
endef

# Declare host and target rules for each ABI
$(foreach abi,$(HOST_ABIS),$(eval $(call declare-jdwp-host-rule,$(abi))))
$(foreach abi,$(TARGET_ABIS),$(eval $(call declare-jdwp-target-rule,$(abi))))

# High level host and target rules running tests for each ABI.
.PHONY: run-jdwp-tests-host
run-jdwp-tests-host: $(LAST_JDWP_HOST_RULE)

.PHONY: run-jdwp-tests-target
run-jdwp-tests-target: $(LAST_JDWP_TARGET_RULE)

LAST_JDWP_HOST_RULE :=
LAST_JDWP_TARGET_RULE :=
HOST_ABIS :=
TARGET_ABIS :=

.PHONY: run-jdwp-tests-ri
run-jdwp-tests-ri: $(HOST_OUT_JAVA_LIBRARIES)/apache-harmony-jdwp-tests-host.jar $(HOST_OUT_JAVA_LIBRARIES)/junit.jar
	$(hide) echo java -cp $(HOST_OUT_JAVA_LIBRARIES)/apache-harmony-jdwp-tests-host.jar:$(HOST_OUT_JAVA_LIBRARIES)/junit.jar \
          -Djpda.settings.verbose=true \
          -Djpda.settings.syncPort=34016 \
          -Djpda.settings.debuggeeJavaPath=java \
          $(jdwp_test_suite_class_name)
