#
# Copyright (C) 2015 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Debug configuration (development and continuous testing)
#
# We run in debug mode to get more error checking and enable JDWP verbose logs
# to investigate failures during testing.

# Common runtime options for test runner and debuggee (host and target)
jdwp_test_common_runtime_options := -Xcompiler-option --debuggable

ifeq ($(ART_TEST_RUN_TEST_NDEBUG),true)
  jdwp_test_common_runtime_options += -XXlib:libart.so
else
  jdwp_test_common_runtime_options += -XXlib:libartd.so
endif

art_core_image_name := core
ifeq ($(ART_TEST_OPTIMIZING),true)
  jdwp_test_common_runtime_options += -Xcompiler-option --compiler-backend=Optimizing
  art_core_image_name := $(art_core_image_name)-optimizing
else
  ifeq ($(ART_TEST_INTERPRETER),true)
    jdwp_test_common_runtime_options += -Xint
    jdwp_test_common_runtime_options += -Xcompiler-option --compiler-filter=interpret-only
    art_core_image_name := $(art_core_image_name)-interpreter
  else
    ifeq ($(ART_TEST_JIT),true)
      jdwp_test_common_runtime_options += -Xusejit:true
      jdwp_test_common_runtime_options += -Xcompiler-option --compiler-filter=interpret-only
      art_core_image_name := $(art_core_image_name)-interpreter
    endif
  endif
endif

# Dex2oat (prebuild is not applicable here)
ifeq ($(ART_TEST_RUN_TEST_NO_DEX2OAT),true)
  jdwp_test_common_runtime_options += -Xcompiler:/system/bin/false
endif

# Relocations
ifeq ($(ART_TEST_RUN_TEST_NO_RELOCATE),true)
  jdwp_test_common_runtime_options += -Xnorelocate
else
  jdwp_test_common_runtime_options += -Xrelocate
  jdwp_test_common_runtime_options += -Xcompiler-option --include-patch-information
  ifeq ($(ART_TEST_RUN_TEST_RELOCATE_NO_PATCHOAT),true)
    jdwp_test_common_runtime_options += -Xpatchoat:/system/bin/false
  endif
endif

# PIC
ifeq ($(ART_TEST_PIC_TEST),true)
  jdwp_test_common_runtime_options += -Xcompiler-option --compile-pic
endif
ifeq ($(ART_TEST_PIC_IMAGE),true)
  art_core_image_name := $(art_core_image_name)-pic
endif

# Debuggee runtime options
jdwp_test_debuggee_runtime_options := -verbose:jdwp
#jdwp_test_debuggee_runtime_options += -verbose:threads

# Gc types
art_gc_args := -Xgc:preverify -Xgc:postverify -XX:HspaceCompactForOOMMinIntervalMs=0
ifeq ($(ART_TEST_GC_STRESS),true)
  jdwp_test_debuggee_runtime_options += $(art_gc_args)
  jdwp_test_debuggee_runtime_options += -Xgc:SS -Xms2m -Xmx2m
else
  ifeq ($(ART_TEST_GC_VERIFY),true)
    jdwp_test_debuggee_runtime_options += $(art_gc_args)
    jdwp_test_debuggee_runtime_options += -Xgc:preverify_rosalloc -Xgc:postverify_rosalloc
  endif
endif

# JNI
ifeq ($(ART_TEST_JNI_FORCECOPY),true)
  jdwp_test_debuggee_runtime_options += -Xjniopts:forcecopy
else
  jdwp_test_debuggee_runtime_options += -Xcheck:jni
endif

# Test suite class name
jdwp_test_suite_class_name := org.apache.harmony.jpda.tests.share.AllTests

# The lists of ABIs supported on host and target.
jdwp_tests_host_abis :=
jdwp_tests_target_abis :=

ifneq ($(HOST_PREFER_32_BIT),true)
  jdwp_tests_host_abis := 64 32
else
  jdwp_tests_host_abis := 32
endif

ifdef TARGET_2ND_ARCH
  ifeq ($(TARGET_IS_64_BIT),true)
    jdwp_tests_target_abis := 64 32
  else
    $(error Unsupported multi-target configuration!)
  endif
else
  ifeq ($(TARGET_IS_64_BIT),true)
    jdwp_tests_target_abis := 64
  else
    jdwp_tests_target_abis := 32
  endif
endif

jdwp_trace_output_filename := jdwp_trace.bin
jdwp_host_tmp_directory := /tmp
ifneq ($(TMPDIR),)
  jdwp_host_tmp_directory := $(TMPDIR)
endif
jdwp_art_host_method_trace_output := $(jdwp_host_tmp_directory)/$(jdwp_trace_output_filename)

jdwp_tests_host_dependencies := \
  $(HOST_OUT_EXECUTABLES)/art \
  $(HOST_OUT_JAVA_LIBRARIES)/apache-harmony-jdwp-tests-hostdex.jar \
  $(HOST_OUT_JAVA_LIBRARIES)/junit-hostdex.jar

jdwp_tests_target_dependencies := \
  $(TARGET_OUT_DATA)/jdwp/apache-harmony-jdwp-tests.jar \
  $(TARGET_OUT_DATA)/junit/junit-targetdex.jar

# Bootclasspath when running without image
art_bootclasspath := core-libart
art_bootclasspath += conscrypt
art_bootclasspath += okhttp
art_bootclasspath += core-junit
art_bootclasspath += bouncycastle

# Function to build a classpath from a list of jar files (separated by ':')
make-bootclasspath = $(subst $(eval) ,:,$(1))

# Define host bootclasspath
art_host_bootclasspath_dir := $(HOST_OUT_EXECUTABLES)/../framework
art_host_bootclasspath := $(addprefix $(art_host_bootclasspath_dir)/,$(art_bootclasspath))
art_host_bootclasspath := $(addsuffix -hostdex.jar,$(art_host_bootclasspath))
art_host_bootclasspath := $(call make-bootclasspath,$(art_host_bootclasspath))

# Define target bootclasspath
art_target_bootclasspath_dir := /system/framework
art_target_bootclasspath := $(addprefix $(art_target_bootclasspath_dir)/,$(art_bootclasspath))
art_target_bootclasspath := $(addsuffix .jar,$(art_target_bootclasspath))
art_target_bootclasspath := $(call make-bootclasspath,$(art_target_bootclasspath))

# Define a JDWP host rule
#
# $(1) ABI (32 or 64)
# $(2) rule name (ex: run-jdwp-tests-host32)
# $(3) extra dependency rule (ex: run-jdwp-tests-host-all64)
define define-jdwp-host-rule
.PHONY: $(2)
# Base host command for test and debuggee
$(2): jdwp_test_runtime_host := $(HOST_OUT_EXECUTABLES)/art --$(1) $(jdwp_test_common_runtime_options)
# Host command for running the tests
$(2): jdwp_test_cmd_host := $$(jdwp_test_runtime_host)
$(2): jdwp_test_cmd_host += -Ximage:$(art_host_bootclasspath_dir)/$(art_core_image_name).art
$(2): jdwp_test_classpath_host := $(HOST_OUT_JAVA_LIBRARIES)/apache-harmony-jdwp-tests-hostdex.jar:$(HOST_OUT_JAVA_LIBRARIES)/junit-hostdex.jar
# Host command for running the debuggee
$(2): jdwp_debuggee_cmd_host := $$(jdwp_test_runtime_host) $(jdwp_test_debuggee_runtime_options)
ifeq ($(ART_TEST_RUN_TEST_NO_IMAGE),true)
$(2): jdwp_debuggee_cmd_host += -Xbootclasspath:$(art_host_bootclasspath)
$(2): jdwp_debuggee_cmd_host += -Ximage:/system/non-existant/core.art
else
$(2): jdwp_debuggee_cmd_host += -Ximage:$(art_host_bootclasspath_dir)/$(art_core_image_name).art
endif
ifeq ($(ART_TEST_TRACE),true)
$(2): jdwp_debuggee_cmd_host += -Xmethod-trace
$(2): jdwp_debuggee_cmd_host += -Xmethod-trace-file:$(jdwp_art_host_method_trace_output)
$(2): jdwp_debuggee_cmd_host += -Xmethod-trace-file-size:2000000
endif
$(2): $(jdwp_tests_host_dependencies) $(3)
	$(hide) echo "Running JDWP $(1)-bit host tests"
	$$(jdwp_test_cmd_host) -cp $$(jdwp_test_classpath_host) \
    $(jdwp_test_target_runtime_common_args) \
    -Djpda.settings.debuggeeJavaPath='$$(jdwp_debuggee_cmd_host)' \
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
  $(eval $(call define-jdwp-host-rule,$(1),run-jdwp-tests-host-all$(1),$(jdwp_tests_previous_host_rule)))
  jdwp_tests_previous_host_rule := run-jdwp-tests-host-all$(1)
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
# Base target command for test and debuggee
$(2): jdwp_test_runtime_target := dalvikvm$(1) $(jdwp_test_common_runtime_options)
# Target command for running the tests
$(2): jdwp_test_cmd_target := $$(jdwp_test_runtime_target)
$(2): jdwp_test_cmd_target += -Ximage:$(art_target_bootclasspath_dir)/$(art_core_image_name).art
$(2): jdwp_test_classpath_target := /data/jdwp/apache-harmony-jdwp-tests.jar:/data/junit/junit-targetdex.jar
# Target command for running the debuggee
$(2): jdwp_debuggee_cmd_target := $$(jdwp_test_runtime_target) $(jdwp_test_debuggee_runtime_options)
ifeq ($(ART_TEST_RUN_TEST_NO_IMAGE),true)
$(2): jdwp_debuggee_cmd_target += -Ximage:/system/non-existant/core.art
$(2): jdwp_debuggee_cmd_target += -Xbootclasspath:$(art_target_bootclasspath)
else
$(2): jdwp_debuggee_cmd_target += -Ximage:$(art_target_bootclasspath_dir)/$(art_core_image_name).art
endif
ifeq ($(ART_TEST_TRACE),true)
$(2): jdwp_debuggee_cmd_target += -Xmethod-trace
$(2): jdwp_debuggee_cmd_target += -Xmethod-trace-file:/data/local/tmp/$(jdwp_trace_output_filename)
$(2): jdwp_debuggee_cmd_target += -Xmethod-trace-file-size:2000000
endif
$(2): $(jdwp_tests_target_dependencies) $(3)
	$(hide) echo "Running JDWP $(1)-bit target tests"
	adb root
	adb wait-for-device shell stop
	adb remount
	adb sync
	adb reboot
	$$(call wait-for-boot-complete)
	adb shell $$(jdwp_test_cmd_target) -cp $$(jdwp_test_classpath_target) \
    $(jdwp_test_target_runtime_common_args) \
    -Djpda.settings.debuggeeJavaPath='$$(jdwp_debuggee_cmd_target)' \
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
  $(eval $(call define-jdwp-target-rule,$(1),run-jdwp-tests-target-all$(1),$(jdwp_tests_previous_target_rule)))
  jdwp_tests_previous_target_rule := run-jdwp-tests-target-all$(1)
endef

# Declare host and target rules for each ABI
$(foreach abi,$(jdwp_tests_host_abis),$(eval $(call declare-jdwp-host-rule,$(abi))))
$(foreach abi,$(jdwp_tests_target_abis),$(eval $(call declare-jdwp-target-rule,$(abi))))

# High level host and target rules running tests for each ABI.
.PHONY: run-jdwp-tests-host
run-jdwp-tests-host: $(jdwp_tests_previous_host_rule)

.PHONY: run-jdwp-tests-target
run-jdwp-tests-target: $(jdwp_tests_previous_target_rule)

jdwp_tests_host_abis :=
jdwp_tests_target_abis :=
jdwp_tests_host_dependencies :=
jdwp_tests_target_dependencies :=

.PHONY: run-jdwp-tests-ri
run-jdwp-tests-ri: $(HOST_OUT_JAVA_LIBRARIES)/apache-harmony-jdwp-tests-host.jar $(HOST_OUT_JAVA_LIBRARIES)/junit.jar
	$(hide) java -cp $(HOST_OUT_JAVA_LIBRARIES)/apache-harmony-jdwp-tests-host.jar:$(HOST_OUT_JAVA_LIBRARIES)/junit.jar \
          -Djpda.settings.verbose=true \
          -Djpda.settings.syncPort=34016 \
          -Djpda.settings.debuggeeJavaPath=java \
          $(jdwp_test_suite_class_name)

