package springbook.template.callback.calc2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import springbook.template.callback.LineCallback;

public class Calculator2 {
	
	
	/**
	 * client
	 * 
	 * @param filepath
	 * @return
	 * @throws IOException
	 */
	public Integer calcSum(String filepath) throws IOException {
		return bufferedReaderTemplate(filepath, new BufferedReaderCallBack() {
			
			@Override
			public Integer callBackProcess(BufferedReader br) throws IOException {
				int sum = 0;
				String line = null;
				while((line = br.readLine()) != null)
					sum += Integer.valueOf(line);
				return sum;
			}
		});
	}
	
	public Integer calcMultiply(String filepath) throws IOException {
		return bufferedReaderTemplate(filepath, new BufferedReaderCallBack() {
			
			@Override
			public Integer callBackProcess(BufferedReader br) throws IOException {
				int product = 1;
				String line = null;
				while((line = br.readLine()) != null)
					product *= Integer.valueOf(line);
				return product;
			}
		});
		
	}

	/**
	 * template
	 * -> callback을 더 심플하게 만들어보자
	 * -> calcSum/calMultiply의 callback에서 처리하는 범위중 공통된것을 추려낸다.
	 * -> 초기값, while문 안의 라인단위 처리를 제외하고는 동일하다.
	 * 
	 * @param filepath
	 * @param cb
	 * @return
	 * @throws IOException
	 */
	private Integer bufferedReaderTemplate(String filepath, BufferedReaderCallBack cb) throws IOException {
		try(
			BufferedReader br = new BufferedReader(new FileReader(filepath));
		){
			return cb.callBackProcess(br);
		} catch(IOException e) {
			e.printStackTrace();
			throw e;
		}
	}


}
