package springbook.template.callback.calc3;

import java.io.IOException;

public interface LineCallBack {

	void callBackProcess(String line) throws IOException;

	Integer getResult();
}
