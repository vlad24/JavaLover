package examples.functionalProgramming;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class CurriedExample {

	public static Function<List<Integer>, Boolean> checkDigitCurried(Integer d){
		return new Function<List<Integer>, Boolean>(){
			@Override
			public Boolean apply(List<Integer> list) {
				return list.stream().allMatch(x -> x.toString().contains(d.toString()));
			}};
	}
	
	public static void main(String[] args) {
		//examples
		List<Integer> list2 = new ArrayList<Integer>(Arrays.asList(12, 32, 52, 72, 29, 24));
		Function<List<Integer>, Boolean> f = checkDigitCurried(2);
		System.out.println(f.apply(list2));
		
	}

}
