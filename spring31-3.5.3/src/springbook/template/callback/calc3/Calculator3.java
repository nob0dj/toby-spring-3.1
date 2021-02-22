package springbook.template.callback.calc3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import springbook.template.callback.LineCallback;

public class Calculator3 {
	
	
	/**
	 * client
	 * 
	 * @param filepath
	 * @return
	 * @throws IOException
	 */
	public Integer calcSum(String filepath) throws IOException {
		return lineTemplate(filepath, new LineCallBack() {
			int result = 0;//지역변수 선언가능
			
			@Override
			public void callBackProcess(String line) throws IOException {
				result += Integer.valueOf(line);
			}
			
			/**
			 * interface에 정의된 메소드만 LineCallBack객체로 제어할 수 있다.
			 * 그외는 현재 객체안에서만 사용가능하다.
			 */
			@Override
			public Integer getResult() {
				return this.result;
			}
		});
	}
	
	public Integer calcMultiply(String filepath) throws IOException {
		return lineTemplate(filepath, new LineCallBack() {
			int result = 1;//지역변수 선언가능
			
			@Override
			public void callBackProcess(String line) throws IOException {
				result *= Integer.valueOf(line);
			}
			
			/**
			 * interface에 정의된 메소드만 LineCallBack객체로 제어할 수 있다.
			 * 그외는 현재 객체안에서만 사용가능하다.
			 */
			@Override
			public Integer getResult() {
				return this.result;
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
	private Integer lineTemplate(String filepath, LineCallBack cb) throws IOException {
		try(
			BufferedReader br = new BufferedReader(new FileReader(filepath));
		){
			String line = null;
			while((line = br.readLine()) != null)
				cb.callBackProcess(line);
			return cb.getResult();

		} catch(IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

}
