package springbook.template.callback.calc2;

import java.io.BufferedReader;
import java.io.IOException;

public interface BufferedReaderCallBack {

	Integer callBackProcess(BufferedReader br) throws IOException;
}
