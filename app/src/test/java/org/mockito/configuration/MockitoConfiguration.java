package org.mockito.configuration;

import android.support.annotation.NonNull;

import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * @author marian on 9.2.2017.
 */

public class MockitoConfiguration extends
	DefaultMockitoConfiguration {
	@Override
	public Answer<Object> getDefaultAnswer() {
		return new ReturnsEmptyValues() {
			private static final long serialVersionUID = 3969001829345523091L;

			@Override
			public Object answer(InvocationOnMock inv) {
				Class<?> type = inv.getMethod().getReturnType();
				if (type.isAssignableFrom(Observable.class)) {
					return Observable.error(createException(inv));
				} else if (type.isAssignableFrom(Flowable.class)) {
					return Flowable.error(createException(inv));
				} else if (type.isAssignableFrom(Single.class)) {
					return Single.error(createException(inv));
				} else {
					return super.answer(inv);
				}
			}
		};
	}

	@NonNull
	private RuntimeException createException(
		InvocationOnMock invocation) {
		String s = invocation.toString();
		return new RuntimeException(
			"No mock defined for invocation " + s);
	}
}