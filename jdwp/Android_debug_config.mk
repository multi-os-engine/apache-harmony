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

# TODO rename
FALSE_BIN="/system/bin/false"

# ART configuration
art_core_image_name := core

# TODO support these configurations

# Common runtime settings for runner and debuggee.
jdwp_test_common_runtime_options := -Xcompiler-option --debuggable

ifeq ($(ART_TEST_RUN_TEST_NDEBUG),true)
  jdwp_test_common_runtime_options += -XXlib:libart.so
else
  jdwp_test_common_runtime_options += -XXlib:libartd.so
endif

# Dex2oat
ifeq ($(ART_TEST_RUN_TEST_PREBUILD),true)
  $(info "ART_TEST_RUN_TEST_PREBUILD: ignoring for JDWP tests")
endif
ifeq ($(ART_TEST_RUN_TEST_NO_PREBUILD),true)
  $(info "ART_TEST_RUN_TEST_NO_PREBUILD: ignoring for JDWP tests")
endif
ifeq ($(ART_TEST_RUN_TEST_NO_DEX2OAT),true)
  $(info ART_TEST_RUN_TEST_NO_DEX2OAT)
  jdwp_test_common_runtime_options += -Xcompiler:${FALSE_BIN}
endif


ifeq ($(ART_TEST_OPTIMIZING),true)
  $(info ART_TEST_OPTIMIZING)
  # Select optimizing compiler
  jdwp_test_common_runtime_options += -Xcompiler-option --compiler-backend=Optimizing
  art_core_image_name := $(art_core_image_name)-optimizing
else
  ifeq ($(ART_TEST_INTERPRETER),true)
    $(info ART_TEST_INTERPRETER)
    # Select optimizing compiler
    jdwp_test_common_runtime_options += -Xcompiler-option --compiler-filter=interpret-only
    art_core_image_name := $(art_core_image_name)-interpreter
  else
    ifeq ($(ART_TEST_JIT),true)
      $(info ART_TEST_JIT)
      jdwp_test_common_runtime_options += -Xusejit:true -Xcompiler-option --compiler-filter=interpret-only
      art_core_image_name := $(art_core_image_name)-interpreter
    endif
  endif
endif

# Relocations
ifeq ($(ART_TEST_RUN_TEST_NO_RELOCATE),true)
  $(info ART_TEST_RUN_TEST_NO_RELOCATE)
  jdwp_test_common_runtime_options += -Xnorelocate
else
  jdwp_test_common_runtime_options += -Xrelocate -Xcompiler-option --include-patch-information
  ifeq ($(ART_TEST_RUN_TEST_RELOCATE_NO_PATCHOAT),true)
    $(info ART_TEST_RUN_TEST_RELOCATE_NO_PATCHOAT)
    jdwp_test_common_runtime_options += -Xpatchoat:${FALSE_BIN}
  endif
endif

# TODO Tracing
ifeq ($(ART_TEST_TRACE),true)
  $(info ART_TEST_TRACE)
endif

# Gc types
art_gc_args := -Xgc:preverify -Xgc:postverify -XX:HspaceCompactForOOMMinIntervalMs=0
ifeq ($(ART_TEST_GC_STRESS),true)
  $(info ART_TEST_GC_STRESS)
  jdwp_test_common_runtime_options += $(art_gc_args) -Xgc:SS -Xms2m -Xmx2m
else
  ifeq ($(ART_TEST_GC_VERIFY),true)
    $(info ART_TEST_GC_VERIFY)
    jdwp_test_common_runtime_options += $(art_gc_args) -Xgc:preverify_rosalloc -Xgc:postverify_rosalloc
  endif
endif

# JNI
ifeq ($(ART_TEST_JNI_FORCECOPY),true)
  $(info ART_TEST_JNI_FORCECOPY)
  jdwp_test_common_runtime_options += -Xjniopts:forcecopy
else
  jdwp_test_common_runtime_options += -Xcheck:jni
endif

# PIC
ifeq ($(ART_TEST_PIC_TEST),true)
  $(info ART_TEST_PIC_TEST)
  jdwp_test_common_runtime_options += -Xcompiler-option --compile-pic
endif

# TODO Image
ifeq ($(ART_TEST_RUN_TEST_NO_IMAGE),true)
  $(info ART_TEST_RUN_TEST_NO_IMAGE)
  art_core_image_name := non-existant-core
else
  ifeq ($(ART_TEST_PIC_IMAGE),true)
    $(info ART_TEST_PIC_IMAGE)
    art_core_image_name := $(art_core_image_name)-pic
  endif
endif

# Target settings
jdwp_test_classpath_target := /data/jdwp/apache-harmony-jdwp-tests.jar:/data/junit/junit-targetdex.jar
jdwp_test_runtime_bin_target := dalvikvm

# Host settings
jdwp_test_classpath_host := $(HOST_OUT_JAVA_LIBRARIES)/apache-harmony-jdwp-tests-hostdex.jar:$(HOST_OUT_JAVA_LIBRARIES)/junit-hostdex.jar
jdwp_test_runtime_bin_host := $(HOST_OUT_EXECUTABLES)/art

jdwp_test_host_runtime_options := $(jdwp_test_common_runtime_options)
jdwp_test_host_runtime_options += -Ximage:$(abspath $(HOST_OUT_EXECUTABLES)/../framework/$(art_core_image_name).art)

# Debuggee runtime options
jdwp_test_runtime_options := -Xcompiler-option --debuggable
#jdwp_test_runtime_options += -verbose:jdwp
#jdwp_test_runtime_options += -Xint # interpret-only mode
#jdwp_test_runtime_options += -Xnoimage-dex2oat # imageless mode
#jdwp_test_runtime_options += -Xcompiler-option --compiler-backend=Optimizing # optimizing compiler
#jdwp_test_runtime_options += -verbose:threads

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

jdwp_tests_host_dependencies := \
  $(HOST_OUT_EXECUTABLES)/art \
  $(HOST_OUT_JAVA_LIBRARIES)/apache-harmony-jdwp-tests-hostdex.jar \
  $(HOST_OUT_JAVA_LIBRARIES)/junit-hostdex.jar

jdwp_tests_target_dependencies := \
  $(TARGET_OUT_DATA)/jdwp/apache-harmony-jdwp-tests.jar \
  $(TARGET_OUT_DATA)/junit/junit-targetdex.jar

# Define a JDWP host rule
#
# $(1) ABI (32 or 64)
# $(2) rule name (ex: run-jdwp-tests-host32)
# $(3) extra dependency rule (ex: run-jdwp-tests-host-all64)
define define-jdwp-host-rule
.PHONY: $(2)
$(2): jdwp_test_runtime_host := $(jdwp_test_runtime_bin_host) --$(1) $(jdwp_test_host_runtime_options)
$(2): $(jdwp_tests_host_dependencies) $(3)
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
$(2): jdwp_test_runtime_target := $(jdwp_test_runtime_bin_target)$(1) $(jdwp_test_common_runtime_options)
$(2): $(jdwp_tests_target_dependencies) $(3)
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

