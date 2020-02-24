package com.orangeman.example.licensingservice.hystrix;

import java.util.concurrent.Callable;

import com.orangeman.example.licensingservice.utils.UserContext;
import com.orangeman.example.licensingservice.utils.UserContextHolder;

public class DelegatingUserContextCallable<V> implements Callable<V> {

	private final Callable<V> delegate;
	private UserContext originalUesrContext;
	
	public DelegatingUserContextCallable(Callable<V> delegate,
			UserContext userContext) {
		this.delegate = delegate;
		this.originalUesrContext = userContext;
	}
	
	@Override
	public V call() throws Exception {
		// TODO Auto-generated method stub
		UserContextHolder.setContext(originalUesrContext);
		
		try {
			return delegate.call();
		}
		finally {
			this.originalUesrContext = null;
		}
	}
	
	public static <V> Callable<V> create(Callable<V> delegate,
			UserContext userContext) {
		return new DelegatingUserContextCallable<>(delegate, userContext);
	}

}
