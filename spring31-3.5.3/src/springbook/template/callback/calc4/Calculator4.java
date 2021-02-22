package springbook.template.callback.calc4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import springbook.template.callback.LineCallback;

public class Calculator4 {
	
	
	/**
	 * client
	 * 
	 * GenericsLineCallBack 익명객체 생성시 초기값을 인자로 전달한다.
	 * 
	 * @param filepath
	 * @return
	 * @throws IOException
	 */
	public Integer calcSum(String filepath) throws IOException {
		return genericsLineTemplate(filepath, new GenericsLineCallBack<Integer>(0) {
			
			@Override
			public void callBackProcess(String line) throws IOException {
				result += Integer.valueOf(line);
			}

		});
	}
	
	public Integer calcMultiply(String filepath) throws IOException {
		return genericsLineTemplate(filepath, new GenericsLineCallBack<Integer>(1) {
			
			@Override
			public void callBackProcess(String line) throws IOException {
				result *= Integer.valueOf(line);
			}
		});
		
	}
	
	public String concatenate(String filepath) throws IOException {
			return genericsLineTemplate(filepath, new GenericsLineCallBack<String>("") {
				@Override
				public void callBackProcess(String line) throws IOException {
					result += line;
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
	private <T> T genericsLineTemplate(String filepath, GenericsLineCallBack<T> cb) throws IOException {
		try(
			BufferedReader br = new BufferedReader(new FileReader(filepath));
		){
			String line = null;
			while((line = br.readLine()) != null) {
				System.out.println(line);
				cb.callBackProcess(line);
			}
			return cb.getResult();

		} catch(IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

}
