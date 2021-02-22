package springbook.template.callback.calc1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator1 {
	
	
	public Integer calcSum(String filepath) throws IOException {
		int sum = 0;
		try(
			BufferedReader br = new BufferedReader(new FileReader(filepath));
		){
			String line = null;
			while((line = br.readLine()) != null)
				sum += Integer.valueOf(line);
			return sum;
		} catch(IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public Integer calcMultiply(String filepath) throws IOException {
		int product = 1;
		try(
			BufferedReader br = new BufferedReader(new FileReader(filepath));
		){
			String line = null;
			while((line = br.readLine()) != null)
				product *= Integer.valueOf(line);
			return product;
		} catch(IOException e) {
			e.printStackTrace();
			throw e;
		}
		
	}
	
	public String concatenate(String filepath) throws IOException {
		String result = "";
		try(
			BufferedReader br = new BufferedReader(new FileReader(filepath));
		){
			String line = null;
			while((line = br.readLine()) != null)
				result += line;
			return result;
		} catch(IOException e) {
			e.printStackTrace();
			throw e;
		}
		
	}


}
