/*
 *  soapUI, copyright (C) 2004-2011 eviware.com 
 *
 *  soapUI is free software; you can redistribute it and/or modify it under the 
 *  terms of version 2.1 of the GNU Lesser General Public License as published by 
 *  the Free Software Foundation.
 *
 *  soapUI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU Lesser General Public License for more details at gnu.org.
 */

package com.eviware.soapui.impl.wsdl.testcase;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.impl.wsdl.support.AbstractTestCaseRunner;
import com.eviware.soapui.model.testsuite.TestCaseRunner;
import com.eviware.soapui.model.testsuite.TestStep;
import com.eviware.soapui.model.testsuite.TestStepResult;
import com.eviware.soapui.security.SecurityTestRunContext;
import com.eviware.soapui.support.types.StringToObjectMap;

/**
 * WSDL TestCase Runner - runs all steps in a testcase and collects performance
 * data
 * 
 * @author Ole.Matzura
 */

public class WsdlTestCaseRunner extends AbstractTestCaseRunner<WsdlTestCase, WsdlTestRunContext> implements
		TestCaseRunner
{

	@SuppressWarnings( "unchecked" )
	public WsdlTestCaseRunner( WsdlTestCase testCase, StringToObjectMap properties )
	{
		super( testCase, properties );
	}

	public WsdlTestRunContext createContext( StringToObjectMap properties )
	{
		return new WsdlTestRunContext( this, properties );
	}

	@Override
	protected int runCurrentTestStep( WsdlTestRunContext runContext, int currentStepIndex )
	{
		TestStep currentStep = runContext.getCurrentStep();
		if( !currentStep.isDisabled() )
		{
			TestStepResult stepResult = runTestStep( currentStep, true, true );
			if( stepResult == null )
				return -2;

			if( !isRunning() )
				return -2;

			if( gotoStepIndex != -1 )
			{
				currentStepIndex = gotoStepIndex - 1;
				gotoStepIndex = -1;
			}
		}

		runContext.setCurrentStep( currentStepIndex + 1 );
		return currentStepIndex;
	}

	protected void notifyAfterRun()
	{
		super.notifyAfterRun();
	}

	protected void notifyBeforeRun()
	{
		super.notifyBeforeRun();
	}

	@Override
	public WsdlTestCase getTestCase()
	{
		return getTestRunnable();
	}

	@Override
	protected void clear( WsdlTestRunContext runContext )
	{
		runContext.clear();
		testRunListeners = null;
	}

	@Override
	protected void runSetupScripts( WsdlTestRunContext runContext ) throws Exception
	{
		getTestCase().runSetupScript( runContext, this );
	}

	@Override
	protected void runTearDownScripts( WsdlTestRunContext runContext ) throws Exception
	{
		getTestCase().runTearDownScript( runContext, this );
	}

	@Override
	protected void fillInTestRunnableListeners()
	{
		testRunListeners = getTestRunnable().getTestRunListeners();

	}

	@Override
	protected void failOnTestStepErrors( WsdlTestRunContext runContext, WsdlTestCase testCase )
	{
		if( runContext.getProperty( TestCaseRunner.Status.class.getName() ) == TestCaseRunner.Status.FAILED
				&& testCase.getFailTestCaseOnErrors() )
		{
			fail( "Failing due to failed test step" );
		}
	}
}
