/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.harmony.jpda.tests.jdwp.Events;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

import org.apache.harmony.jpda.tests.share.JPDADebuggeeSynchronizer;
import org.apache.harmony.jpda.tests.share.SyncDebuggee;

/**
 * Debuggee for JDWP NewInstanceTest unit test which
 * exercises ArrayType.NewInstance command.
 */
public class AnnotationDebuggee extends SyncDebuggee {

	public int stressTest() {
		logWriter.println("started stress test");
		int ann = 0;
		Method[] methods = AnnotatedClass.class.getDeclaredMethods();
		for (Method method : methods) {
			Annotation[] annotations = method.getAnnotations();
			for (@SuppressWarnings("unused") Annotation annotation : annotations) {
				ann++;
			}
		}
		logWriter.println("finished stress test");
		return ann;
	}

	public void doCall() {
		stressTest();
	}

    public void run() {
        synchronizer.sendMessage(JPDADebuggeeSynchronizer.SGNL_READY);

        logWriter.println("AnnotationDebuggee started");

        synchronizer.receiveMessage(JPDADebuggeeSynchronizer.SGNL_CONTINUE);

        doCall();

        logWriter.println("AnnotationDebuggee finished");
    }

    /**
     * Starts AnnotationDebuggee with help of runDebuggee() method
     * from <A HREF="../../share/Debuggee.html">Debuggee</A> super class.
     *
     */
    public static void main(String [] args) {
        runDebuggee(AnnotationDebuggee.class);
    }

    public static class AnnotatedClass {

       @Retention(RetentionPolicy.RUNTIME)
		public static @interface Ann0 {}

		@Ann0
		public static void method0000() {}

		@Ann0
		public static void method0001() {}

		@Ann0
		public static void method0002() {}

		@Ann0
		public static void method0003() {}

		@Ann0
		public static void method0004() {}

		@Ann0
		public static void method0005() {}

		@Ann0
		public static void method0006() {}

		@Ann0
		public static void method0007() {}

		@Ann0
		public static void method0008() {}

		@Ann0
		public static void method0009() {}

		@Ann0
		public static void method0010() {}

		@Ann0
		public static void method0011() {}

		@Ann0
		public static void method0012() {}

		@Ann0
		public static void method0013() {}

		@Ann0
		public static void method0014() {}

		@Ann0
		public static void method0015() {}

		@Ann0
		public static void method0016() {}

		@Ann0
		public static void method0017() {}

		@Ann0
		public static void method0018() {}

		@Ann0
		public static void method0019() {}

		@Ann0
		public static void method0020() {}

		@Ann0
		public static void method0021() {}

		@Ann0
		public static void method0022() {}

		@Ann0
		public static void method0023() {}

		@Ann0
		public static void method0024() {}

		@Ann0
		public static void method0025() {}

		@Ann0
		public static void method0026() {}

		@Ann0
		public static void method0027() {}

		@Ann0
		public static void method0028() {}

		@Ann0
		public static void method0029() {}

		@Ann0
		public static void method0030() {}

		@Ann0
		public static void method0031() {}

		@Ann0
		public static void method0032() {}

		@Ann0
		public static void method0033() {}

		@Ann0
		public static void method0034() {}

		@Ann0
		public static void method0035() {}

		@Ann0
		public static void method0036() {}

		@Ann0
		public static void method0037() {}

		@Ann0
		public static void method0038() {}

		@Ann0
		public static void method0039() {}

		@Ann0
		public static void method0040() {}

		@Ann0
		public static void method0041() {}

		@Ann0
		public static void method0042() {}

		@Ann0
		public static void method0043() {}

		@Ann0
		public static void method0044() {}

		@Ann0
		public static void method0045() {}

		@Ann0
		public static void method0046() {}

		@Ann0
		public static void method0047() {}

		@Ann0
		public static void method0048() {}

		@Ann0
		public static void method0049() {}

		@Ann0
		public static void method0050() {}

		@Ann0
		public static void method0051() {}

		@Ann0
		public static void method0052() {}

		@Ann0
		public static void method0053() {}

		@Ann0
		public static void method0054() {}

		@Ann0
		public static void method0055() {}

		@Ann0
		public static void method0056() {}

		@Ann0
		public static void method0057() {}

		@Ann0
		public static void method0058() {}

		@Ann0
		public static void method0059() {}

		@Ann0
		public static void method0060() {}

		@Ann0
		public static void method0061() {}

		@Ann0
		public static void method0062() {}

		@Ann0
		public static void method0063() {}

		@Ann0
		public static void method0064() {}

		@Ann0
		public static void method0065() {}

		@Ann0
		public static void method0066() {}

		@Ann0
		public static void method0067() {}

		@Ann0
		public static void method0068() {}

		@Ann0
		public static void method0069() {}

		@Ann0
		public static void method0070() {}

		@Ann0
		public static void method0071() {}

		@Ann0
		public static void method0072() {}

		@Ann0
		public static void method0073() {}

		@Ann0
		public static void method0074() {}

		@Ann0
		public static void method0075() {}

		@Ann0
		public static void method0076() {}

		@Ann0
		public static void method0077() {}

		@Ann0
		public static void method0078() {}

		@Ann0
		public static void method0079() {}

		@Ann0
		public static void method0080() {}

		@Ann0
		public static void method0081() {}

		@Ann0
		public static void method0082() {}

		@Ann0
		public static void method0083() {}

		@Ann0
		public static void method0084() {}

		@Ann0
		public static void method0085() {}

		@Ann0
		public static void method0086() {}

		@Ann0
		public static void method0087() {}

		@Ann0
		public static void method0088() {}

		@Ann0
		public static void method0089() {}

		@Ann0
		public static void method0090() {}

		@Ann0
		public static void method0091() {}

		@Ann0
		public static void method0092() {}

		@Ann0
		public static void method0093() {}

		@Ann0
		public static void method0094() {}

		@Ann0
		public static void method0095() {}

		@Ann0
		public static void method0096() {}

		@Ann0
		public static void method0097() {}

		@Ann0
		public static void method0098() {}

		@Ann0
		public static void method0099() {}

		@Ann0
		public static void method0100() {}

		@Ann0
		public static void method0101() {}

		@Ann0
		public static void method0102() {}

		@Ann0
		public static void method0103() {}

		@Ann0
		public static void method0104() {}

		@Ann0
		public static void method0105() {}

		@Ann0
		public static void method0106() {}

		@Ann0
		public static void method0107() {}

		@Ann0
		public static void method0108() {}

		@Ann0
		public static void method0109() {}

		@Ann0
		public static void method0110() {}

		@Ann0
		public static void method0111() {}

		@Ann0
		public static void method0112() {}

		@Ann0
		public static void method0113() {}

		@Ann0
		public static void method0114() {}

		@Ann0
		public static void method0115() {}

		@Ann0
		public static void method0116() {}

		@Ann0
		public static void method0117() {}

		@Ann0
		public static void method0118() {}

		@Ann0
		public static void method0119() {}

		@Ann0
		public static void method0120() {}

		@Ann0
		public static void method0121() {}

		@Ann0
		public static void method0122() {}

		@Ann0
		public static void method0123() {}

		@Ann0
		public static void method0124() {}

		@Ann0
		public static void method0125() {}

		@Ann0
		public static void method0126() {}

		@Ann0
		public static void method0127() {}

		@Ann0
		public static void method0128() {}

		@Ann0
		public static void method0129() {}

		@Ann0
		public static void method0130() {}

		@Ann0
		public static void method0131() {}

		@Ann0
		public static void method0132() {}

		@Ann0
		public static void method0133() {}

		@Ann0
		public static void method0134() {}

		@Ann0
		public static void method0135() {}

		@Ann0
		public static void method0136() {}

		@Ann0
		public static void method0137() {}

		@Ann0
		public static void method0138() {}

		@Ann0
		public static void method0139() {}

		@Ann0
		public static void method0140() {}

		@Ann0
		public static void method0141() {}

		@Ann0
		public static void method0142() {}

		@Ann0
		public static void method0143() {}

		@Ann0
		public static void method0144() {}

		@Ann0
		public static void method0145() {}

		@Ann0
		public static void method0146() {}

		@Ann0
		public static void method0147() {}

		@Ann0
		public static void method0148() {}

		@Ann0
		public static void method0149() {}

		@Ann0
		public static void method0150() {}

		@Ann0
		public static void method0151() {}

		@Ann0
		public static void method0152() {}

		@Ann0
		public static void method0153() {}

		@Ann0
		public static void method0154() {}

		@Ann0
		public static void method0155() {}

		@Ann0
		public static void method0156() {}

		@Ann0
		public static void method0157() {}

		@Ann0
		public static void method0158() {}

		@Ann0
		public static void method0159() {}

		@Ann0
		public static void method0160() {}

		@Ann0
		public static void method0161() {}

		@Ann0
		public static void method0162() {}

		@Ann0
		public static void method0163() {}

		@Ann0
		public static void method0164() {}

		@Ann0
		public static void method0165() {}

		@Ann0
		public static void method0166() {}

		@Ann0
		public static void method0167() {}

		@Ann0
		public static void method0168() {}

		@Ann0
		public static void method0169() {}

		@Ann0
		public static void method0170() {}

		@Ann0
		public static void method0171() {}

		@Ann0
		public static void method0172() {}

		@Ann0
		public static void method0173() {}

		@Ann0
		public static void method0174() {}

		@Ann0
		public static void method0175() {}

		@Ann0
		public static void method0176() {}

		@Ann0
		public static void method0177() {}

		@Ann0
		public static void method0178() {}

		@Ann0
		public static void method0179() {}

		@Ann0
		public static void method0180() {}

		@Ann0
		public static void method0181() {}

		@Ann0
		public static void method0182() {}

		@Ann0
		public static void method0183() {}

		@Ann0
		public static void method0184() {}

		@Ann0
		public static void method0185() {}

		@Ann0
		public static void method0186() {}

		@Ann0
		public static void method0187() {}

		@Ann0
		public static void method0188() {}

		@Ann0
		public static void method0189() {}

		@Ann0
		public static void method0190() {}

		@Ann0
		public static void method0191() {}

		@Ann0
		public static void method0192() {}

		@Ann0
		public static void method0193() {}

		@Ann0
		public static void method0194() {}

		@Ann0
		public static void method0195() {}

		@Ann0
		public static void method0196() {}

		@Ann0
		public static void method0197() {}

		@Ann0
		public static void method0198() {}

		@Ann0
		public static void method0199() {}

		@Ann0
		public static void method0200() {}

		@Ann0
		public static void method0201() {}

		@Ann0
		public static void method0202() {}

		@Ann0
		public static void method0203() {}

		@Ann0
		public static void method0204() {}

		@Ann0
		public static void method0205() {}

		@Ann0
		public static void method0206() {}

		@Ann0
		public static void method0207() {}

		@Ann0
		public static void method0208() {}

		@Ann0
		public static void method0209() {}

		@Ann0
		public static void method0210() {}

		@Ann0
		public static void method0211() {}

		@Ann0
		public static void method0212() {}

		@Ann0
		public static void method0213() {}

		@Ann0
		public static void method0214() {}

		@Ann0
		public static void method0215() {}

		@Ann0
		public static void method0216() {}

		@Ann0
		public static void method0217() {}

		@Ann0
		public static void method0218() {}

		@Ann0
		public static void method0219() {}

		@Ann0
		public static void method0220() {}

		@Ann0
		public static void method0221() {}

		@Ann0
		public static void method0222() {}

		@Ann0
		public static void method0223() {}

		@Ann0
		public static void method0224() {}

		@Ann0
		public static void method0225() {}

		@Ann0
		public static void method0226() {}

		@Ann0
		public static void method0227() {}

		@Ann0
		public static void method0228() {}

		@Ann0
		public static void method0229() {}

		@Ann0
		public static void method0230() {}

		@Ann0
		public static void method0231() {}

		@Ann0
		public static void method0232() {}

		@Ann0
		public static void method0233() {}

		@Ann0
		public static void method0234() {}

		@Ann0
		public static void method0235() {}

		@Ann0
		public static void method0236() {}

		@Ann0
		public static void method0237() {}

		@Ann0
		public static void method0238() {}

		@Ann0
		public static void method0239() {}

		@Ann0
		public static void method0240() {}

		@Ann0
		public static void method0241() {}

		@Ann0
		public static void method0242() {}

		@Ann0
		public static void method0243() {}

		@Ann0
		public static void method0244() {}

		@Ann0
		public static void method0245() {}

		@Ann0
		public static void method0246() {}

		@Ann0
		public static void method0247() {}

		@Ann0
		public static void method0248() {}

		@Ann0
		public static void method0249() {}

		@Ann0
		public static void method0250() {}

		@Ann0
		public static void method0251() {}

		@Ann0
		public static void method0252() {}

		@Ann0
		public static void method0253() {}

		@Ann0
		public static void method0254() {}

		@Ann0
		public static void method0255() {}

		@Ann0
		public static void method0256() {}

		@Ann0
		public static void method0257() {}

		@Ann0
		public static void method0258() {}

		@Ann0
		public static void method0259() {}

		@Ann0
		public static void method0260() {}

		@Ann0
		public static void method0261() {}

		@Ann0
		public static void method0262() {}

		@Ann0
		public static void method0263() {}

		@Ann0
		public static void method0264() {}

		@Ann0
		public static void method0265() {}

		@Ann0
		public static void method0266() {}

		@Ann0
		public static void method0267() {}

		@Ann0
		public static void method0268() {}

		@Ann0
		public static void method0269() {}

		@Ann0
		public static void method0270() {}

		@Ann0
		public static void method0271() {}

		@Ann0
		public static void method0272() {}

		@Ann0
		public static void method0273() {}

		@Ann0
		public static void method0274() {}

		@Ann0
		public static void method0275() {}

		@Ann0
		public static void method0276() {}

		@Ann0
		public static void method0277() {}

		@Ann0
		public static void method0278() {}

		@Ann0
		public static void method0279() {}

		@Ann0
		public static void method0280() {}

		@Ann0
		public static void method0281() {}

		@Ann0
		public static void method0282() {}

		@Ann0
		public static void method0283() {}

		@Ann0
		public static void method0284() {}

		@Ann0
		public static void method0285() {}

		@Ann0
		public static void method0286() {}

		@Ann0
		public static void method0287() {}

		@Ann0
		public static void method0288() {}

		@Ann0
		public static void method0289() {}

		@Ann0
		public static void method0290() {}

		@Ann0
		public static void method0291() {}

		@Ann0
		public static void method0292() {}

		@Ann0
		public static void method0293() {}

		@Ann0
		public static void method0294() {}

		@Ann0
		public static void method0295() {}

		@Ann0
		public static void method0296() {}

		@Ann0
		public static void method0297() {}

		@Ann0
		public static void method0298() {}

		@Ann0
		public static void method0299() {}

		@Ann0
		public static void method0300() {}

		@Ann0
		public static void method0301() {}

		@Ann0
		public static void method0302() {}

		@Ann0
		public static void method0303() {}

		@Ann0
		public static void method0304() {}

		@Ann0
		public static void method0305() {}

		@Ann0
		public static void method0306() {}

		@Ann0
		public static void method0307() {}

		@Ann0
		public static void method0308() {}

		@Ann0
		public static void method0309() {}

		@Ann0
		public static void method0310() {}

		@Ann0
		public static void method0311() {}

		@Ann0
		public static void method0312() {}

		@Ann0
		public static void method0313() {}

		@Ann0
		public static void method0314() {}

		@Ann0
		public static void method0315() {}

		@Ann0
		public static void method0316() {}

		@Ann0
		public static void method0317() {}

		@Ann0
		public static void method0318() {}

		@Ann0
		public static void method0319() {}

		@Ann0
		public static void method0320() {}

		@Ann0
		public static void method0321() {}

		@Ann0
		public static void method0322() {}

		@Ann0
		public static void method0323() {}

		@Ann0
		public static void method0324() {}

		@Ann0
		public static void method0325() {}

		@Ann0
		public static void method0326() {}

		@Ann0
		public static void method0327() {}

		@Ann0
		public static void method0328() {}

		@Ann0
		public static void method0329() {}

		@Ann0
		public static void method0330() {}

		@Ann0
		public static void method0331() {}

		@Ann0
		public static void method0332() {}

		@Ann0
		public static void method0333() {}

		@Ann0
		public static void method0334() {}

		@Ann0
		public static void method0335() {}

		@Ann0
		public static void method0336() {}

		@Ann0
		public static void method0337() {}

		@Ann0
		public static void method0338() {}

		@Ann0
		public static void method0339() {}

		@Ann0
		public static void method0340() {}

		@Ann0
		public static void method0341() {}

		@Ann0
		public static void method0342() {}

		@Ann0
		public static void method0343() {}

		@Ann0
		public static void method0344() {}

		@Ann0
		public static void method0345() {}

		@Ann0
		public static void method0346() {}

		@Ann0
		public static void method0347() {}

		@Ann0
		public static void method0348() {}

		@Ann0
		public static void method0349() {}

		@Ann0
		public static void method0350() {}

		@Ann0
		public static void method0351() {}

		@Ann0
		public static void method0352() {}

		@Ann0
		public static void method0353() {}

		@Ann0
		public static void method0354() {}

		@Ann0
		public static void method0355() {}

		@Ann0
		public static void method0356() {}

		@Ann0
		public static void method0357() {}

		@Ann0
		public static void method0358() {}

		@Ann0
		public static void method0359() {}

		@Ann0
		public static void method0360() {}

		@Ann0
		public static void method0361() {}

		@Ann0
		public static void method0362() {}

		@Ann0
		public static void method0363() {}

		@Ann0
		public static void method0364() {}

		@Ann0
		public static void method0365() {}

		@Ann0
		public static void method0366() {}

		@Ann0
		public static void method0367() {}

		@Ann0
		public static void method0368() {}

		@Ann0
		public static void method0369() {}

		@Ann0
		public static void method0370() {}

		@Ann0
		public static void method0371() {}

		@Ann0
		public static void method0372() {}

		@Ann0
		public static void method0373() {}

		@Ann0
		public static void method0374() {}

		@Ann0
		public static void method0375() {}

		@Ann0
		public static void method0376() {}

		@Ann0
		public static void method0377() {}

		@Ann0
		public static void method0378() {}

		@Ann0
		public static void method0379() {}

		@Ann0
		public static void method0380() {}

		@Ann0
		public static void method0381() {}

		@Ann0
		public static void method0382() {}

		@Ann0
		public static void method0383() {}

		@Ann0
		public static void method0384() {}

		@Ann0
		public static void method0385() {}

		@Ann0
		public static void method0386() {}

		@Ann0
		public static void method0387() {}

		@Ann0
		public static void method0388() {}

		@Ann0
		public static void method0389() {}

		@Ann0
		public static void method0390() {}

		@Ann0
		public static void method0391() {}

		@Ann0
		public static void method0392() {}

		@Ann0
		public static void method0393() {}

		@Ann0
		public static void method0394() {}

		@Ann0
		public static void method0395() {}

		@Ann0
		public static void method0396() {}

		@Ann0
		public static void method0397() {}

		@Ann0
		public static void method0398() {}

		@Ann0
		public static void method0399() {}

		@Ann0
		public static void method0400() {}

		@Ann0
		public static void method0401() {}

		@Ann0
		public static void method0402() {}

		@Ann0
		public static void method0403() {}

		@Ann0
		public static void method0404() {}

		@Ann0
		public static void method0405() {}

		@Ann0
		public static void method0406() {}

		@Ann0
		public static void method0407() {}

		@Ann0
		public static void method0408() {}

		@Ann0
		public static void method0409() {}

		@Ann0
		public static void method0410() {}

		@Ann0
		public static void method0411() {}

		@Ann0
		public static void method0412() {}

		@Ann0
		public static void method0413() {}

		@Ann0
		public static void method0414() {}

		@Ann0
		public static void method0415() {}

		@Ann0
		public static void method0416() {}

		@Ann0
		public static void method0417() {}

		@Ann0
		public static void method0418() {}

		@Ann0
		public static void method0419() {}

		@Ann0
		public static void method0420() {}

		@Ann0
		public static void method0421() {}

		@Ann0
		public static void method0422() {}

		@Ann0
		public static void method0423() {}

		@Ann0
		public static void method0424() {}

		@Ann0
		public static void method0425() {}

		@Ann0
		public static void method0426() {}

		@Ann0
		public static void method0427() {}

		@Ann0
		public static void method0428() {}

		@Ann0
		public static void method0429() {}

		@Ann0
		public static void method0430() {}

		@Ann0
		public static void method0431() {}

		@Ann0
		public static void method0432() {}

		@Ann0
		public static void method0433() {}

		@Ann0
		public static void method0434() {}

		@Ann0
		public static void method0435() {}

		@Ann0
		public static void method0436() {}

		@Ann0
		public static void method0437() {}

		@Ann0
		public static void method0438() {}

		@Ann0
		public static void method0439() {}

		@Ann0
		public static void method0440() {}

		@Ann0
		public static void method0441() {}

		@Ann0
		public static void method0442() {}

		@Ann0
		public static void method0443() {}

		@Ann0
		public static void method0444() {}

		@Ann0
		public static void method0445() {}

		@Ann0
		public static void method0446() {}

		@Ann0
		public static void method0447() {}

		@Ann0
		public static void method0448() {}

		@Ann0
		public static void method0449() {}

		@Ann0
		public static void method0450() {}

		@Ann0
		public static void method0451() {}

		@Ann0
		public static void method0452() {}

		@Ann0
		public static void method0453() {}

		@Ann0
		public static void method0454() {}

		@Ann0
		public static void method0455() {}

		@Ann0
		public static void method0456() {}

		@Ann0
		public static void method0457() {}

		@Ann0
		public static void method0458() {}

		@Ann0
		public static void method0459() {}

		@Ann0
		public static void method0460() {}

		@Ann0
		public static void method0461() {}

		@Ann0
		public static void method0462() {}

		@Ann0
		public static void method0463() {}

		@Ann0
		public static void method0464() {}

		@Ann0
		public static void method0465() {}

		@Ann0
		public static void method0466() {}

		@Ann0
		public static void method0467() {}

		@Ann0
		public static void method0468() {}

		@Ann0
		public static void method0469() {}

		@Ann0
		public static void method0470() {}

		@Ann0
		public static void method0471() {}

		@Ann0
		public static void method0472() {}

		@Ann0
		public static void method0473() {}

		@Ann0
		public static void method0474() {}

		@Ann0
		public static void method0475() {}

		@Ann0
		public static void method0476() {}

		@Ann0
		public static void method0477() {}

		@Ann0
		public static void method0478() {}

		@Ann0
		public static void method0479() {}

		@Ann0
		public static void method0480() {}

		@Ann0
		public static void method0481() {}

		@Ann0
		public static void method0482() {}

		@Ann0
		public static void method0483() {}

		@Ann0
		public static void method0484() {}

		@Ann0
		public static void method0485() {}

		@Ann0
		public static void method0486() {}

		@Ann0
		public static void method0487() {}

		@Ann0
		public static void method0488() {}

		@Ann0
		public static void method0489() {}

		@Ann0
		public static void method0490() {}

		@Ann0
		public static void method0491() {}

		@Ann0
		public static void method0492() {}

		@Ann0
		public static void method0493() {}

		@Ann0
		public static void method0494() {}

		@Ann0
		public static void method0495() {}

		@Ann0
		public static void method0496() {}

		@Ann0
		public static void method0497() {}

		@Ann0
		public static void method0498() {}

		@Ann0
		public static void method0499() {}

		@Ann0
		public static void method0500() {}

		@Ann0
		public static void method0501() {}

		@Ann0
		public static void method0502() {}

		@Ann0
		public static void method0503() {}

		@Ann0
		public static void method0504() {}

		@Ann0
		public static void method0505() {}

		@Ann0
		public static void method0506() {}

		@Ann0
		public static void method0507() {}

		@Ann0
		public static void method0508() {}

		@Ann0
		public static void method0509() {}

		@Ann0
		public static void method0510() {}

		@Ann0
		public static void method0511() {}

		@Ann0
		public static void method0512() {}

		@Ann0
		public static void method0513() {}

		@Ann0
		public static void method0514() {}

		@Ann0
		public static void method0515() {}

		@Ann0
		public static void method0516() {}

		@Ann0
		public static void method0517() {}

		@Ann0
		public static void method0518() {}

		@Ann0
		public static void method0519() {}

		@Ann0
		public static void method0520() {}

		@Ann0
		public static void method0521() {}

		@Ann0
		public static void method0522() {}

		@Ann0
		public static void method0523() {}

		@Ann0
		public static void method0524() {}

		@Ann0
		public static void method0525() {}

		@Ann0
		public static void method0526() {}

		@Ann0
		public static void method0527() {}

		@Ann0
		public static void method0528() {}

		@Ann0
		public static void method0529() {}

		@Ann0
		public static void method0530() {}

		@Ann0
		public static void method0531() {}

		@Ann0
		public static void method0532() {}

		@Ann0
		public static void method0533() {}

		@Ann0
		public static void method0534() {}

		@Ann0
		public static void method0535() {}

		@Ann0
		public static void method0536() {}

		@Ann0
		public static void method0537() {}

		@Ann0
		public static void method0538() {}

		@Ann0
		public static void method0539() {}

		@Ann0
		public static void method0540() {}

		@Ann0
		public static void method0541() {}

		@Ann0
		public static void method0542() {}

		@Ann0
		public static void method0543() {}

		@Ann0
		public static void method0544() {}

		@Ann0
		public static void method0545() {}

		@Ann0
		public static void method0546() {}

		@Ann0
		public static void method0547() {}

		@Ann0
		public static void method0548() {}

		@Ann0
		public static void method0549() {}

		@Ann0
		public static void method0550() {}

		@Ann0
		public static void method0551() {}

		@Ann0
		public static void method0552() {}

		@Ann0
		public static void method0553() {}

		@Ann0
		public static void method0554() {}

		@Ann0
		public static void method0555() {}

		@Ann0
		public static void method0556() {}

		@Ann0
		public static void method0557() {}

		@Ann0
		public static void method0558() {}

		@Ann0
		public static void method0559() {}

		@Ann0
		public static void method0560() {}

		@Ann0
		public static void method0561() {}

		@Ann0
		public static void method0562() {}

		@Ann0
		public static void method0563() {}

		@Ann0
		public static void method0564() {}

		@Ann0
		public static void method0565() {}

		@Ann0
		public static void method0566() {}

		@Ann0
		public static void method0567() {}

		@Ann0
		public static void method0568() {}

		@Ann0
		public static void method0569() {}

		@Ann0
		public static void method0570() {}

		@Ann0
		public static void method0571() {}

		@Ann0
		public static void method0572() {}

		@Ann0
		public static void method0573() {}

		@Ann0
		public static void method0574() {}

		@Ann0
		public static void method0575() {}

		@Ann0
		public static void method0576() {}

		@Ann0
		public static void method0577() {}

		@Ann0
		public static void method0578() {}

		@Ann0
		public static void method0579() {}

		@Ann0
		public static void method0580() {}

		@Ann0
		public static void method0581() {}

		@Ann0
		public static void method0582() {}

		@Ann0
		public static void method0583() {}

		@Ann0
		public static void method0584() {}

		@Ann0
		public static void method0585() {}

		@Ann0
		public static void method0586() {}

		@Ann0
		public static void method0587() {}

		@Ann0
		public static void method0588() {}

		@Ann0
		public static void method0589() {}

		@Ann0
		public static void method0590() {}

		@Ann0
		public static void method0591() {}

		@Ann0
		public static void method0592() {}

		@Ann0
		public static void method0593() {}

		@Ann0
		public static void method0594() {}

		@Ann0
		public static void method0595() {}

		@Ann0
		public static void method0596() {}

		@Ann0
		public static void method0597() {}

		@Ann0
		public static void method0598() {}

		@Ann0
		public static void method0599() {}

		@Ann0
		public static void method0600() {}

		@Ann0
		public static void method0601() {}

		@Ann0
		public static void method0602() {}

		@Ann0
		public static void method0603() {}

		@Ann0
		public static void method0604() {}

		@Ann0
		public static void method0605() {}

		@Ann0
		public static void method0606() {}

		@Ann0
		public static void method0607() {}

		@Ann0
		public static void method0608() {}

		@Ann0
		public static void method0609() {}

		@Ann0
		public static void method0610() {}

		@Ann0
		public static void method0611() {}

		@Ann0
		public static void method0612() {}

		@Ann0
		public static void method0613() {}

		@Ann0
		public static void method0614() {}

		@Ann0
		public static void method0615() {}

		@Ann0
		public static void method0616() {}

		@Ann0
		public static void method0617() {}

		@Ann0
		public static void method0618() {}

		@Ann0
		public static void method0619() {}

		@Ann0
		public static void method0620() {}

		@Ann0
		public static void method0621() {}

		@Ann0
		public static void method0622() {}

		@Ann0
		public static void method0623() {}

		@Ann0
		public static void method0624() {}

		@Ann0
		public static void method0625() {}

		@Ann0
		public static void method0626() {}

		@Ann0
		public static void method0627() {}

		@Ann0
		public static void method0628() {}

		@Ann0
		public static void method0629() {}

		@Ann0
		public static void method0630() {}

		@Ann0
		public static void method0631() {}

		@Ann0
		public static void method0632() {}

		@Ann0
		public static void method0633() {}

		@Ann0
		public static void method0634() {}

		@Ann0
		public static void method0635() {}

		@Ann0
		public static void method0636() {}

		@Ann0
		public static void method0637() {}

		@Ann0
		public static void method0638() {}

		@Ann0
		public static void method0639() {}

		@Ann0
		public static void method0640() {}

		@Ann0
		public static void method0641() {}

		@Ann0
		public static void method0642() {}

		@Ann0
		public static void method0643() {}

		@Ann0
		public static void method0644() {}

		@Ann0
		public static void method0645() {}

		@Ann0
		public static void method0646() {}

		@Ann0
		public static void method0647() {}

		@Ann0
		public static void method0648() {}

		@Ann0
		public static void method0649() {}

		@Ann0
		public static void method0650() {}

		@Ann0
		public static void method0651() {}

		@Ann0
		public static void method0652() {}

		@Ann0
		public static void method0653() {}

		@Ann0
		public static void method0654() {}

		@Ann0
		public static void method0655() {}

		@Ann0
		public static void method0656() {}

		@Ann0
		public static void method0657() {}

		@Ann0
		public static void method0658() {}

		@Ann0
		public static void method0659() {}

		@Ann0
		public static void method0660() {}

		@Ann0
		public static void method0661() {}

		@Ann0
		public static void method0662() {}

		@Ann0
		public static void method0663() {}

		@Ann0
		public static void method0664() {}

		@Ann0
		public static void method0665() {}

		@Ann0
		public static void method0666() {}

		@Ann0
		public static void method0667() {}

		@Ann0
		public static void method0668() {}

		@Ann0
		public static void method0669() {}

		@Ann0
		public static void method0670() {}

		@Ann0
		public static void method0671() {}

		@Ann0
		public static void method0672() {}

		@Ann0
		public static void method0673() {}

		@Ann0
		public static void method0674() {}

		@Ann0
		public static void method0675() {}

		@Ann0
		public static void method0676() {}

		@Ann0
		public static void method0677() {}

		@Ann0
		public static void method0678() {}

		@Ann0
		public static void method0679() {}

		@Ann0
		public static void method0680() {}

		@Ann0
		public static void method0681() {}

		@Ann0
		public static void method0682() {}

		@Ann0
		public static void method0683() {}

		@Ann0
		public static void method0684() {}

		@Ann0
		public static void method0685() {}

		@Ann0
		public static void method0686() {}

		@Ann0
		public static void method0687() {}

		@Ann0
		public static void method0688() {}

		@Ann0
		public static void method0689() {}

		@Ann0
		public static void method0690() {}

		@Ann0
		public static void method0691() {}

		@Ann0
		public static void method0692() {}

		@Ann0
		public static void method0693() {}

		@Ann0
		public static void method0694() {}

		@Ann0
		public static void method0695() {}

		@Ann0
		public static void method0696() {}

		@Ann0
		public static void method0697() {}

		@Ann0
		public static void method0698() {}

		@Ann0
		public static void method0699() {}

		@Ann0
		public static void method0700() {}

		@Ann0
		public static void method0701() {}

		@Ann0
		public static void method0702() {}

		@Ann0
		public static void method0703() {}

		@Ann0
		public static void method0704() {}

		@Ann0
		public static void method0705() {}

		@Ann0
		public static void method0706() {}

		@Ann0
		public static void method0707() {}

		@Ann0
		public static void method0708() {}

		@Ann0
		public static void method0709() {}

		@Ann0
		public static void method0710() {}

		@Ann0
		public static void method0711() {}

		@Ann0
		public static void method0712() {}

		@Ann0
		public static void method0713() {}

		@Ann0
		public static void method0714() {}

		@Ann0
		public static void method0715() {}

		@Ann0
		public static void method0716() {}

		@Ann0
		public static void method0717() {}

		@Ann0
		public static void method0718() {}

		@Ann0
		public static void method0719() {}

		@Ann0
		public static void method0720() {}

		@Ann0
		public static void method0721() {}

		@Ann0
		public static void method0722() {}

		@Ann0
		public static void method0723() {}

		@Ann0
		public static void method0724() {}

		@Ann0
		public static void method0725() {}

		@Ann0
		public static void method0726() {}

		@Ann0
		public static void method0727() {}

		@Ann0
		public static void method0728() {}

		@Ann0
		public static void method0729() {}

		@Ann0
		public static void method0730() {}

		@Ann0
		public static void method0731() {}

		@Ann0
		public static void method0732() {}

		@Ann0
		public static void method0733() {}

		@Ann0
		public static void method0734() {}

		@Ann0
		public static void method0735() {}

		@Ann0
		public static void method0736() {}

		@Ann0
		public static void method0737() {}

		@Ann0
		public static void method0738() {}

		@Ann0
		public static void method0739() {}

		@Ann0
		public static void method0740() {}

		@Ann0
		public static void method0741() {}

		@Ann0
		public static void method0742() {}

		@Ann0
		public static void method0743() {}

		@Ann0
		public static void method0744() {}

		@Ann0
		public static void method0745() {}

		@Ann0
		public static void method0746() {}

		@Ann0
		public static void method0747() {}

		@Ann0
		public static void method0748() {}

		@Ann0
		public static void method0749() {}

		@Ann0
		public static void method0750() {}

		@Ann0
		public static void method0751() {}

		@Ann0
		public static void method0752() {}

		@Ann0
		public static void method0753() {}

		@Ann0
		public static void method0754() {}

		@Ann0
		public static void method0755() {}

		@Ann0
		public static void method0756() {}

		@Ann0
		public static void method0757() {}

		@Ann0
		public static void method0758() {}

		@Ann0
		public static void method0759() {}

		@Ann0
		public static void method0760() {}

		@Ann0
		public static void method0761() {}

		@Ann0
		public static void method0762() {}

		@Ann0
		public static void method0763() {}

		@Ann0
		public static void method0764() {}

		@Ann0
		public static void method0765() {}

		@Ann0
		public static void method0766() {}

		@Ann0
		public static void method0767() {}

		@Ann0
		public static void method0768() {}

		@Ann0
		public static void method0769() {}

		@Ann0
		public static void method0770() {}

		@Ann0
		public static void method0771() {}

		@Ann0
		public static void method0772() {}

		@Ann0
		public static void method0773() {}

		@Ann0
		public static void method0774() {}

		@Ann0
		public static void method0775() {}

		@Ann0
		public static void method0776() {}

		@Ann0
		public static void method0777() {}

		@Ann0
		public static void method0778() {}

		@Ann0
		public static void method0779() {}

		@Ann0
		public static void method0780() {}

		@Ann0
		public static void method0781() {}

		@Ann0
		public static void method0782() {}

		@Ann0
		public static void method0783() {}

		@Ann0
		public static void method0784() {}

		@Ann0
		public static void method0785() {}

		@Ann0
		public static void method0786() {}

		@Ann0
		public static void method0787() {}

		@Ann0
		public static void method0788() {}

		@Ann0
		public static void method0789() {}

		@Ann0
		public static void method0790() {}

		@Ann0
		public static void method0791() {}

		@Ann0
		public static void method0792() {}

		@Ann0
		public static void method0793() {}

		@Ann0
		public static void method0794() {}

		@Ann0
		public static void method0795() {}

		@Ann0
		public static void method0796() {}

		@Ann0
		public static void method0797() {}

		@Ann0
		public static void method0798() {}

		@Ann0
		public static void method0799() {}

		@Ann0
		public static void method0800() {}

		@Ann0
		public static void method0801() {}

		@Ann0
		public static void method0802() {}

		@Ann0
		public static void method0803() {}

		@Ann0
		public static void method0804() {}

		@Ann0
		public static void method0805() {}

		@Ann0
		public static void method0806() {}

		@Ann0
		public static void method0807() {}

		@Ann0
		public static void method0808() {}

		@Ann0
		public static void method0809() {}

		@Ann0
		public static void method0810() {}

		@Ann0
		public static void method0811() {}

		@Ann0
		public static void method0812() {}

		@Ann0
		public static void method0813() {}

		@Ann0
		public static void method0814() {}

		@Ann0
		public static void method0815() {}

		@Ann0
		public static void method0816() {}

		@Ann0
		public static void method0817() {}

		@Ann0
		public static void method0818() {}

		@Ann0
		public static void method0819() {}

		@Ann0
		public static void method0820() {}

		@Ann0
		public static void method0821() {}

		@Ann0
		public static void method0822() {}

		@Ann0
		public static void method0823() {}

		@Ann0
		public static void method0824() {}

		@Ann0
		public static void method0825() {}

		@Ann0
		public static void method0826() {}

		@Ann0
		public static void method0827() {}

		@Ann0
		public static void method0828() {}

		@Ann0
		public static void method0829() {}

		@Ann0
		public static void method0830() {}

		@Ann0
		public static void method0831() {}

		@Ann0
		public static void method0832() {}

		@Ann0
		public static void method0833() {}

		@Ann0
		public static void method0834() {}

		@Ann0
		public static void method0835() {}

		@Ann0
		public static void method0836() {}

		@Ann0
		public static void method0837() {}

		@Ann0
		public static void method0838() {}

		@Ann0
		public static void method0839() {}

		@Ann0
		public static void method0840() {}

		@Ann0
		public static void method0841() {}

		@Ann0
		public static void method0842() {}

		@Ann0
		public static void method0843() {}

		@Ann0
		public static void method0844() {}

		@Ann0
		public static void method0845() {}

		@Ann0
		public static void method0846() {}

		@Ann0
		public static void method0847() {}

		@Ann0
		public static void method0848() {}

		@Ann0
		public static void method0849() {}

		@Ann0
		public static void method0850() {}

		@Ann0
		public static void method0851() {}

		@Ann0
		public static void method0852() {}

		@Ann0
		public static void method0853() {}

		@Ann0
		public static void method0854() {}

		@Ann0
		public static void method0855() {}

		@Ann0
		public static void method0856() {}

		@Ann0
		public static void method0857() {}

		@Ann0
		public static void method0858() {}

		@Ann0
		public static void method0859() {}

		@Ann0
		public static void method0860() {}

		@Ann0
		public static void method0861() {}

		@Ann0
		public static void method0862() {}

		@Ann0
		public static void method0863() {}

		@Ann0
		public static void method0864() {}

		@Ann0
		public static void method0865() {}

		@Ann0
		public static void method0866() {}

		@Ann0
		public static void method0867() {}

		@Ann0
		public static void method0868() {}

		@Ann0
		public static void method0869() {}

		@Ann0
		public static void method0870() {}

		@Ann0
		public static void method0871() {}

		@Ann0
		public static void method0872() {}

		@Ann0
		public static void method0873() {}

		@Ann0
		public static void method0874() {}

		@Ann0
		public static void method0875() {}

		@Ann0
		public static void method0876() {}

		@Ann0
		public static void method0877() {}

		@Ann0
		public static void method0878() {}

		@Ann0
		public static void method0879() {}

		@Ann0
		public static void method0880() {}

		@Ann0
		public static void method0881() {}

		@Ann0
		public static void method0882() {}

		@Ann0
		public static void method0883() {}

		@Ann0
		public static void method0884() {}

		@Ann0
		public static void method0885() {}

		@Ann0
		public static void method0886() {}

		@Ann0
		public static void method0887() {}

		@Ann0
		public static void method0888() {}

		@Ann0
		public static void method0889() {}

		@Ann0
		public static void method0890() {}

		@Ann0
		public static void method0891() {}

		@Ann0
		public static void method0892() {}

		@Ann0
		public static void method0893() {}

		@Ann0
		public static void method0894() {}

		@Ann0
		public static void method0895() {}

		@Ann0
		public static void method0896() {}

		@Ann0
		public static void method0897() {}

		@Ann0
		public static void method0898() {}

		@Ann0
		public static void method0899() {}

		@Ann0
		public static void method0900() {}

		@Ann0
		public static void method0901() {}

		@Ann0
		public static void method0902() {}

		@Ann0
		public static void method0903() {}

		@Ann0
		public static void method0904() {}

		@Ann0
		public static void method0905() {}

		@Ann0
		public static void method0906() {}

		@Ann0
		public static void method0907() {}

		@Ann0
		public static void method0908() {}

		@Ann0
		public static void method0909() {}

		@Ann0
		public static void method0910() {}

		@Ann0
		public static void method0911() {}

		@Ann0
		public static void method0912() {}

		@Ann0
		public static void method0913() {}

		@Ann0
		public static void method0914() {}

		@Ann0
		public static void method0915() {}

		@Ann0
		public static void method0916() {}

		@Ann0
		public static void method0917() {}

		@Ann0
		public static void method0918() {}

		@Ann0
		public static void method0919() {}

		@Ann0
		public static void method0920() {}

		@Ann0
		public static void method0921() {}

		@Ann0
		public static void method0922() {}

		@Ann0
		public static void method0923() {}

		@Ann0
		public static void method0924() {}

		@Ann0
		public static void method0925() {}

		@Ann0
		public static void method0926() {}

		@Ann0
		public static void method0927() {}

		@Ann0
		public static void method0928() {}

		@Ann0
		public static void method0929() {}

		@Ann0
		public static void method0930() {}

		@Ann0
		public static void method0931() {}

		@Ann0
		public static void method0932() {}

		@Ann0
		public static void method0933() {}

		@Ann0
		public static void method0934() {}

		@Ann0
		public static void method0935() {}

		@Ann0
		public static void method0936() {}

		@Ann0
		public static void method0937() {}

		@Ann0
		public static void method0938() {}

		@Ann0
		public static void method0939() {}

		@Ann0
		public static void method0940() {}

		@Ann0
		public static void method0941() {}

		@Ann0
		public static void method0942() {}

		@Ann0
		public static void method0943() {}

		@Ann0
		public static void method0944() {}

		@Ann0
		public static void method0945() {}

		@Ann0
		public static void method0946() {}

		@Ann0
		public static void method0947() {}

		@Ann0
		public static void method0948() {}

		@Ann0
		public static void method0949() {}

		@Ann0
		public static void method0950() {}

		@Ann0
		public static void method0951() {}

		@Ann0
		public static void method0952() {}

		@Ann0
		public static void method0953() {}

		@Ann0
		public static void method0954() {}

		@Ann0
		public static void method0955() {}

		@Ann0
		public static void method0956() {}

		@Ann0
		public static void method0957() {}

		@Ann0
		public static void method0958() {}

		@Ann0
		public static void method0959() {}

		@Ann0
		public static void method0960() {}

		@Ann0
		public static void method0961() {}

		@Ann0
		public static void method0962() {}

		@Ann0
		public static void method0963() {}

		@Ann0
		public static void method0964() {}

		@Ann0
		public static void method0965() {}

		@Ann0
		public static void method0966() {}

		@Ann0
		public static void method0967() {}

		@Ann0
		public static void method0968() {}

		@Ann0
		public static void method0969() {}

		@Ann0
		public static void method0970() {}

		@Ann0
		public static void method0971() {}

		@Ann0
		public static void method0972() {}

		@Ann0
		public static void method0973() {}

		@Ann0
		public static void method0974() {}

		@Ann0
		public static void method0975() {}

		@Ann0
		public static void method0976() {}

		@Ann0
		public static void method0977() {}

		@Ann0
		public static void method0978() {}

		@Ann0
		public static void method0979() {}

		@Ann0
		public static void method0980() {}

		@Ann0
		public static void method0981() {}

		@Ann0
		public static void method0982() {}

		@Ann0
		public static void method0983() {}

		@Ann0
		public static void method0984() {}

		@Ann0
		public static void method0985() {}

		@Ann0
		public static void method0986() {}

		@Ann0
		public static void method0987() {}

		@Ann0
		public static void method0988() {}

		@Ann0
		public static void method0989() {}

		@Ann0
		public static void method0990() {}

		@Ann0
		public static void method0991() {}

		@Ann0
		public static void method0992() {}

		@Ann0
		public static void method0993() {}

		@Ann0
		public static void method0994() {}

		@Ann0
		public static void method0995() {}

		@Ann0
		public static void method0996() {}

		@Ann0
		public static void method0997() {}

		@Ann0
		public static void method0998() {}

		@Ann0
		public static void method0999() {}

	}
}
