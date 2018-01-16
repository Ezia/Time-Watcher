package esia.timewatcher.data_init;

/**
 * Created by esia on 08/01/18.
 */

public class TestException extends RuntimeException {
	public TestException() {
	}

	public TestException(String detailMessage) {
		super(detailMessage);
	}

	public TestException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public TestException(Throwable throwable) {
		super(throwable);
	}
}
