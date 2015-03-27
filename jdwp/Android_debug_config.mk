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

# Target settings
jdwp_test_classpath_target := /data/jdwp/apache-harmony-jdwp-tests.jar:/data/junit/junit-targetdex.jar
jdwp_test_runtime_bin_target := dalvikvm

# Host settings
jdwp_test_classpath_host := $(HOST_OUT_JAVA_LIBRARIES)/apache-harmony-jdwp-tests-hostdex.jar:$(HOST_OUT_JAVA_LIBRARIES)/junit-hostdex.jar
jdwp_test_runtime_bin_host := $(HOST_OUT_EXECUTABLES)/art

# Common runtime settings for runner and debuggee.
jdwp_test_common_runtime_options := -XXlib:libartd.so -Xcompiler-option --debuggable

# Debuggee runtime options
jdwp_test_runtime_options := -verbose:jdwp
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

# Creates a temp directory (used to control test failures)
jdwp_host_test_dir := $(shell mktemp -d --tmpdir jdwp-tests-XXXXX)
jdwp_host_test_passed_dir := $(jdwp_host_test_dir)/passed
jdwp_host_test_failed_dir := $(jdwp_host_test_dir)/failed

# Dump output for all tests in the given directory
#
# $(1) directory
define dump-test-output
  $(hide) test -d $(1) && (ls -1 $(1) | xargs -I {} sh -c "echo -e '\e[93m[OUTPUT OF {}]\e[0m' && cat $(1)/{}") || true
endef

# List passed and failed tests.
#
# $(1) rule name
define check-jdwp
	$(call dump-test-output,$(jdwp_host_test_failed_dir))
	$(hide) echo -e "$(1) \e[32mCOMPLETE\e[0m"
	$(hide) echo ""
	$(hide) test -d $(jdwp_host_test_passed_dir) && (echo -e "\e[92mPASSING TESTS\e[0m" && ls -1 $(jdwp_host_test_passed_dir)) || echo -e "\e[91mNO TESTS PASSED\e[0m"
	$(hide) test -d $(jdwp_host_test_failed_dir) && (echo -e "\e[91mFAILING TESTS\e[0m" && ls -1 $(jdwp_host_test_failed_dir)) || echo -e "\e[92mNO TESTS FAILED\e[0m"
	$(hide) test ! -d $(jdwp_host_test_failed_dir) && rm -rf $(jdwp_host_test_dir) || (rm -rf $(jdwp_host_test_dir) && false)
endef

# Define a JDWP host rule
#
# $(1) ABI (32 or 64)
# $(2) rule name (ex: run-jdwp-tests-host32)
# $(3) extra dependency rule (ex: run-jdwp-tests-host-all64)
# $(4) 'true' to dump output of passed tests, 'false' to only dump output of failed tests
define define-jdwp-host-rule
.PHONY: $(2)
$(2): jdwp_test_runtime_host := $(jdwp_test_runtime_bin_host) --$(1) $(jdwp_test_common_runtime_options)
$(2): jdwp_tmp_output_file := $(jdwp_host_test_dir)/$(2)
$(2): jdwp_success_command := (echo -e "$(2) \e[92mPASSED\e[0m"
  ifeq ($(4),true)
$(2): jdwp_success_command += && echo -e '\e[93m[OUTPUT OF $(2)]\e[0m' && cat $$(jdwp_tmp_output_file)
  endif
$(2): jdwp_success_command += && mkdir -p $(jdwp_host_test_passed_dir) && cp $$(jdwp_tmp_output_file) $(jdwp_host_test_passed_dir)/$(2))
$(2): jdwp_failure_command := (echo -e "$(2) \e[91mFAILED\e[0m" && mkdir -p $(jdwp_host_test_failed_dir) && cp $$(jdwp_tmp_output_file) $(jdwp_host_test_failed_dir)/$(2))
$(2): $(jdwp_tests_host_dependencies) $(3)
	$(hide) echo -e "$(2) \e[95mRUNNING\e[0m"
	$(hide) (($$(jdwp_test_runtime_host) -cp $(jdwp_test_classpath_host) \
    $(jdwp_test_target_runtime_common_args) \
    -Djpda.settings.debuggeeJavaPath='$$(jdwp_test_runtime_host) $(jdwp_test_runtime_options)' \
    $(jdwp_test_suite_class_name) &> $$(jdwp_tmp_output_file)) && $$(jdwp_success_command)) || $$(jdwp_failure_command)
	$(hide) rm $$(jdwp_tmp_output_file)
  ifeq ($(4),true)
	  $(call check-jdwp,$$@)
  endif
endef

# Make sure ART is ready for host testing.
jdwp_tests_previous_host_rule := build-art-host

# Declare all JDWP host rules
#
# $(1) ABI (32 or 64)
define declare-jdwp-host-rule
  # Declare standalone host rule for the given ABI.
  $(eval $(call define-jdwp-host-rule,$(1),run-jdwp-tests-host$(1),,true))

  # Declare variant host rule for run-jdwp-tests-host. It depends on the previous abi rule(s)
  # so all ABIs are tested.
  $(eval $(call define-jdwp-host-rule,$(1),run-jdwp-tests-host-all$(1),$(jdwp_tests_previous_host_rule),false))
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
	$(call check-jdwp,$@)

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

