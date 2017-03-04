package ru.vlad24;

import java.util.List;

public class SubsequenceImpl implements Subsequence {


	public boolean find(List<?> fixed, List<?> changed) {
		if (fixed == null || changed == null){
			return false;
		}
		if (fixed.size() <= changed.size()){
			int i = 0;
			int j = 0;
			while(i < fixed.size()){
				Object aim = fixed.get(i);
				if (j < changed.size()){
					while (j < changed.size() && !changed.get(j).equals(aim)){
						j++;
					}
					j++;
					i++;
				}else{
					break;
				}
			}
			return (i == fixed.size() && j <= changed.size());
		}
		return false;
	}

}
